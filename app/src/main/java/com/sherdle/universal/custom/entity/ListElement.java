package com.sherdle.universal.custom.entity;

public class ListElement implements Element {

    private String title;
    private String description;
    private String linkImage;
    private String linkUrl;
    private String linkFragment;
    private String textButton;
    private int buttonTextSize;
    private String buttonTextColor;
    private String buttonColor;
    private int type;

    public ListElement(String title, String description, String linkImage, String linkUrl, String linkFragment, String textButton, int buttonTextSize, String buttonTextColor, String buttonColor, int type) {
        this.title = title;
        this.description = description;
        this.linkImage = linkImage;
        this.linkUrl = linkUrl;
        this.linkFragment = linkFragment;
        this.textButton = textButton;
        this.buttonTextSize = buttonTextSize;
        this.buttonTextColor = buttonTextColor;
        this.buttonColor = buttonColor;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public String getLinkFragment() {
        return linkFragment;
    }

    public String getTextButton() {
        return textButton;
    }

    public int getButtonTextSize() {
        return buttonTextSize;
    }

    public String getButtonTextColor() {
        return buttonTextColor;
    }

    public String getButtonColor() {
        return buttonColor;
    }

    public int getType() {
        return type;
    }
}
