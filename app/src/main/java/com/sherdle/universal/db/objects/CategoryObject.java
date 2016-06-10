package com.sherdle.universal.db.objects;

import com.orm.SugarRecord;

public class CategoryObject extends SugarRecord {

    private String title;

    public CategoryObject(){
    }

    public CategoryObject(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
