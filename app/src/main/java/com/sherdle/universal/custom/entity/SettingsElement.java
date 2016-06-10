package com.sherdle.universal.custom.entity;

public class SettingsElement implements Element {

    private String backgroundColor;
    private String backgroundImage;

    public SettingsElement(String backgroundColor, String backgroundImage) {
        this.backgroundColor = backgroundColor;
        this.backgroundImage = backgroundImage;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }
}
