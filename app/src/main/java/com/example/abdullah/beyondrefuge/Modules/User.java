package com.example.abdullah.beyondrefuge.Modules;

import com.google.firebase.auth.FirebaseUser;

public class User {
    String userId;
    String name;
    String email;
    Boolean isTagComplete = false;

    public User() {

    }

    public User(String userId, String name, String email, Boolean isTagComplete) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.isTagComplete = isTagComplete;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getTagComplete() {
        return isTagComplete;
    }

    public void setTagComplete(Boolean tagComplete) {
        isTagComplete = tagComplete;
    }
}
