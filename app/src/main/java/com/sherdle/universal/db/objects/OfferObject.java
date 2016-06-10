package com.sherdle.universal.db.objects;

import com.orm.SugarRecord;

public class OfferObject extends SugarRecord {

    private String name;
    private String thumbUrl;
    private Integer rating;
    private String downloadUrl;
    private String category;
    private Integer priority;

    public OfferObject(){
    }

    public OfferObject(String name, String thumbUrl, Integer rating, String downloadUrl, int priority, String category) {
        this.name = name;
        this.thumbUrl = thumbUrl;
        this.rating = rating;
        this.downloadUrl = downloadUrl;
        this.priority = priority;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public Integer getRating() {
        return rating;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
