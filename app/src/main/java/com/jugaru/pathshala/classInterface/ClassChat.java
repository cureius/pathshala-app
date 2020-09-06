package com.jugaru.pathshala.classInterface;

public class ClassChat {
    private String sender;
    private String classUid;
    private String message;
    private String username;
    private String photoUrl;

    public ClassChat(String sender, String classUid, String message, String photoUrl, String username) {
        this.sender = sender;
        this.classUid = classUid;
        this.message = message;
        this.photoUrl = photoUrl;
        this.username = username;
    }

    public ClassChat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getClassUid() {
        return classUid;
    }

    public void setClassUid(String classUid) {
        this.classUid = classUid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
