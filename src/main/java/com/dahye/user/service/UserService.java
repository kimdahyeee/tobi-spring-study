package com.dahye.user.service;

import com.dahye.main.MailSender;
import com.dahye.user.dao.UserDao;
import com.dahye.user.domain.Grade;
import com.dahye.user.domain.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

public class UserService {

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    UserDao userDao;

    private PlatformTransactionManager transactionManager;

    private MailSender mailSender;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void upgradeGrades() throws Exception {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<User> users = userDao.getAll();

            for (User user : users) {
                if(canUpgradeGrade(user)) {
                    upgradeGrade(user);
                }
            }

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    public void add(User user) {
        if(user.getGrade() == null) user.setGrade(Grade.BASIC);
        userDao.add(user);
    }

    private boolean canUpgradeGrade(User user) {
        Grade currentGrade = user.getGrade();
        switch (currentGrade) {
            case BASIC: return (user.getLoin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw  new IllegalArgumentException("unknown level : " + currentGrade);
        }
    }

    protected void upgradeGrade(User user) {
        user.upgradeGrade();
        userDao.update(user);
        sendEmail(user);
    }

    private void sendEmail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("dahyekim@dahye.com");
        mailMessage.setSubject("upgrade 안내");
        mailMessage.setText("사용자의 등급이 " + user.getGrade().name() + "로 업그레이드");

        this.mailSender.send(mailMessage);
    }
}
