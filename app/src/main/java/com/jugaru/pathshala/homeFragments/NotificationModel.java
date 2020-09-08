package com.jugaru.pathshala.homeFragments;

public class NotificationModel {
    String className;
    String content;
    String type;

    public NotificationModel() {
    }

    public NotificationModel(String className, String content, String type) {
        this.className = className;
        this.content = content;
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
