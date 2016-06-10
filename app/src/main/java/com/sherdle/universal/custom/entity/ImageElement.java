package com.sherdle.universal.custom.entity;

public class ImageElement implements Element {

    private String imageUrl;
    private String linkUrl;
    private String imageGravity;

    public ImageElement(String imageUrl,String linkUrl, String imageGravity) {
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.imageGravity = imageGravity;
    }

    public String getImageGravity() {
        return imageGravity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    @Override
    public String toString() {
        return this.imageUrl;
    }
}
