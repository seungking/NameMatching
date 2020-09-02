package com.e.namematching.model;

public class Set {
    private String question;
    private int answer;
    private String o1;
    private String o2;
    private String o3;
    private String o4;

    public Set(String question, int answer, String o1, String o2, String o3, String o4) {
        this.question = question;
        this.answer = answer;
        this.o1 = o1;
        this.o2 = o2;
        this.o3 = o3;
        this.o4 = o4;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String geto1() {
        return o1;
    }

    public void seto1(String o1) {
        this.o1 = o1;
    }

    public String geto2() {
        return o2;
    }

    public void seto2(String o2) {
        this.o2 = o2;
    }

    public String geto3() {
        return o3;
    }

    public void seto3(String o3) {
        this.o3 = o3;
    }

    public String geto4() {
        return o4;
    }

    public void seto4(String o4) {
        this.o4 = o4;
    }
}
