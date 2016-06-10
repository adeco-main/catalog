package com.sherdle.universal.custom.entity;

public class ButtonElement implements Element {

    private String textButton;
    private String linkButton;
    private String textColorButton;
    private String colorButton;
    private String linkFragment;
    private String buttonGravity;
    private int sizeButton;

    public ButtonElement(String textButton, int sizeButton, String textColorButton, String colorButton, String linkButton, String linkFragment, String buttonGravity) {
        this.textButton = textButton;
        this.linkButton = linkButton;
        this.textColorButton = textColorButton;
        this.colorButton = colorButton;
        this.sizeButton = sizeButton;
        this.linkFragment = linkFragment;
        this.buttonGravity = buttonGravity;
    }

    public String getButtonGravity() {
        return buttonGravity;
    }

    public String getTextColorButton() {
        return textColorButton;
    }

    public void setTextColorButton(String textColorButton) {
        this.textColorButton = textColorButton;
    }

    public String getColorButton() {
        return colorButton;
    }

    public void setColorButton(String colorButton) {
        this.colorButton = colorButton;
    }

    public int getSizeButton() {
        return sizeButton;
    }

    public void setSizeButton(int sizeButton) {
        this.sizeButton = sizeButton;
    }

    public String getTextButton() {
        return textButton;
    }

    public void setTextButton(String textButton) {
        this.textButton = textButton;
    }

    public String getLinkButton() {
        return linkButton;
    }

    public void setLinkButton(String linkButton) {
        this.linkButton = linkButton;
    }

    public String getLinkFragment() {
        return linkFragment;
    }

    public void setLinkFragment(String linkFragment) {
        this.linkFragment = linkFragment;
    }
}
