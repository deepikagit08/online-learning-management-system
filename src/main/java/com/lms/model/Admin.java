package com.lms.model;

public class Admin extends User {
    @Override public String getRole() { return "ADMIN"; }
    @Override public String getHomeTitle() { return "Admin Panel"; }
}
