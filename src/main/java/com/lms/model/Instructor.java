package com.lms.model;

public class Instructor extends User {
    @Override public String getRole() { return "INSTRUCTOR"; }
    @Override public String getHomeTitle() { return "Instructor Dashboard"; }
}
