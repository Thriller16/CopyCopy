package com.lawrene.falcon.copycopy;

/**
 * Created by lawrene on 6/13/18.
 */

public class MyTasks {
    String title, description, type, willAlert, willNotify;

    public MyTasks(String title, String description, String type, String willAlert, String willNotify) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.willAlert = willAlert;
        this.willNotify = willNotify;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getWillAlert() {
        return willAlert;
    }

    public String getWillNotify() {
        return willNotify;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWillAlert(String willAlert) {
        this.willAlert = willAlert;
    }

    public void setWillNotify(String willNotify) {
        this.willNotify = willNotify;
    }
}
