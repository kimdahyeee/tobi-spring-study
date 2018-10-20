package com.dahye.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

    public Integer calcSum(String filePath) throws IOException {
        LineCallback callback = new LineCallback() {
            public Integer doSomethingWithLine(String line, Integer value) {
                return value += Integer.valueOf(line);
            }
        };

        return lineReadTemplate(filePath, callback, 0);
    }

    public Integer calcMultiply(String filePath) throws IOException {
        LineCallback callback = new LineCallback() {
            public Integer doSomethingWithLine(String line, Integer value) {
                return value *= Integer.valueOf(line);
            }
        };

        return lineReadTemplate(filePath, callback, 1);
    }

    public Integer lineReadTemplate(String filePath, LineCallback callback, int initVal) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath));
            Integer res = initVal;
            String line = null;
            while((line = br.readLine()) != null) {
                res = callback.doSomethingWithLine(line, res);
            }

            return res;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if(br!= null) {
                try {
                    br.close();
                } catch (IOException e ) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

}
