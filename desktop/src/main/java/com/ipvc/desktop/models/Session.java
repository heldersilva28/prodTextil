package com.ipvc.desktop.models;

public class Session {
    private static Session instance;
    private Utilizador currentUser;

    private Session() { }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public Utilizador getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Utilizador user) {
        this.currentUser = user;
    }
}

