package com.sherdle.universal.db.objects;

import com.orm.SugarRecord;

public class NotificationObject extends SugarRecord{

    private String description;
    private int time;

    public NotificationObject() {
    }

    public NotificationObject(String description, int time) {
        this.description = description;
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public int getTime() {
        return time;
    }
}
