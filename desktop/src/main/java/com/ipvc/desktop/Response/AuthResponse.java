package com.ipvc.desktop.Response;

public class AuthResponse {
    private String message;
    private boolean success;

    public AuthResponse() {}

    public String getMessage() { return message; }
    public boolean isSuccess() { return success; }

    public void setMessage(String message) { this.message = message; }
    public void setSuccess(boolean success) { this.success = success; }
}
