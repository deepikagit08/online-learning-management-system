package com.lms.model;

/** Abstract base class for all users (abstraction + inheritance). */
public abstract class User {
    private int id;
    private String name;
    private String email;
    private String passwordHash;

    public abstract String getRole();          // polymorphic
    public abstract String getHomeTitle();     // polymorphic

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public boolean isAdmin() { return "ADMIN".equals(getRole()); }
    public boolean isInstructor() { return "INSTRUCTOR".equals(getRole()); }
    public boolean isStudent() { return "STUDENT".equals(getRole()); }
}
