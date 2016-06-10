package com.sherdle.universal.db.objects;

import com.orm.SugarRecord;

public class NavItemObject extends SugarRecord {

    private String title;
    private int icon;
    private String fragment;
    private String data;
    private boolean visible;

    public NavItemObject() {
    }

    public NavItemObject(String title, int icon, String fragment, String data, boolean visible) {
        this.title = title;
        this.icon = icon;
        this.fragment = fragment;
        this.data = data;
        this.visible = visible;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
