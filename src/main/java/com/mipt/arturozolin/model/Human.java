package com.mipt.arturozolin.model;

public class Human {
    private String firstName;
    private String surname;
    private int age;
    private boolean isWorking;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }

    public boolean isWorking() {
        return isWorking;
    }
}
