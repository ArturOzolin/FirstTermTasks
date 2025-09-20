package com.mipt.arturozolin;

public abstract class WorkingHuman {

    public abstract void work(int hours);

    public boolean goHome(String location1, String location2) {
        return location1.equals(location2);
    }
}
