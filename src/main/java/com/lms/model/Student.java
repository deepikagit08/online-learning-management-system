package com.lms.model;

public class Student extends User {
    @Override public String getRole() { return "STUDENT"; }
    @Override public String getHomeTitle() { return "My Learning"; }
}
