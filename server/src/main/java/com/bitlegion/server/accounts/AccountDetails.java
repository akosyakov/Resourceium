package com.bitlegion.server.accounts;

public class AccountDetails {
    private String username;
    private String password;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "{" + " username='" + getUsername() + "'" + ", password='" + getPassword() + "'" + "}";
    }

}
