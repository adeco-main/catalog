package com.sherdle.universal.custom.entity;

public class TextElement implements Element {

    private String textContent;
    private String textColor;
    private String textLink;
    private String textGravity;
    private String textFonts;
    private String textTypeFace;
    private int textSize;

    public TextElement(String textContent, String textColor, int textSize, String textLink, String textGravity, String textFonts, String textTypeFace) {
        this.textContent = textContent;
        this.textColor = textColor;
        this.textSize = textSize;
        this.textLink = textLink;
        this.textGravity = textGravity;
        this.textFonts = textFonts;
        this.textTypeFace = textTypeFace;
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

    public String getTextGravity() {
        return textGravity;
    }

    public String getTextFonts() {
        return textFonts;
    }

    public String getTextTypeFace() {
        return textTypeFace;
    }

    @Override
    public String toString() {
        return this.textContent;
    }
}
