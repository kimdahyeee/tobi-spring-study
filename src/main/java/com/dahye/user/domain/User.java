package com.dahye.user.domain;

public class User {
    String id;
    String name;
    String password;
    Grade grade;
    int loin;
    int recommend;
    String email;

    public User(String id, String name, String password, Grade grade, int loin, int recommend, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.grade = grade;
        this.loin = loin;
        this.recommend = recommend;
        this.email = email;
    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public int getLoin() {
        return loin;
    }

    public void setLoin(int loin) {
        this.loin = loin;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void upgradeGrade() {
        Grade nextGrade = this.grade.nextGrade();
        if (nextGrade == null) {
            throw new IllegalStateException(this.grade + "은 업데이트가 불가능합니다.");
        } else {
            this.grade = nextGrade;
        }
    }
}
