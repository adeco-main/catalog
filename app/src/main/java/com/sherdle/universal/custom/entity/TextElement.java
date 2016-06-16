package com.sherdle.universal.custom.entity;

public class TextElement implements Element {

    private String textContent;
    private String textColor;
    private String textLink;
    private int textSize;

    public TextElement(String textContent, String textColor, int textSize, String textLink) {
        this.textContent = textContent;
        this.textColor = textColor;
        this.textSize = textSize;
        this.textLink = textLink;
    }
    public String getTextLink() {
        return textLink;
    }

    public int getTextSize() {
        return textSize;
    }

    public String getTextColor() {
        return textColor;
    }

    public String getTextContent() {
        return textContent;
    }

    @Override
    public String toString() {
        return this.textContent;
    }
}
