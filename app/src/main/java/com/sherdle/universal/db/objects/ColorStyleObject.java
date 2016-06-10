package com.sherdle.universal.db.objects;

import com.orm.SugarRecord;

public class ColorStyleObject extends SugarRecord{

    private String toolbarColor = "#3F51B5";
    private String tabLayoutColor = "#3F51B5";
    private String statusBarColor = "#3F51B5";
    private String drawerHeadColor = "#3F51B5";

    public ColorStyleObject() {
    }

    public ColorStyleObject(String toolbarColor, String tabLayoutColor, String statusBarColor, String drawerHeadColor) {
        this.toolbarColor = toolbarColor;
        this.tabLayoutColor = tabLayoutColor;
        this.statusBarColor = statusBarColor;
        this.drawerHeadColor = drawerHeadColor;
    }

    public String getToolbarColor() {

        return toolbarColor;
    }

    public void setToolbarColor(String toolbarColor) {
        this.toolbarColor = toolbarColor;
    }

    public String getTabLayoutColor() {
        return tabLayoutColor;
    }

    public void setTabLayoutColor(String tabLayoutColor) {
        this.tabLayoutColor = tabLayoutColor;
    }

    public String getStatusBarColor() {
        return statusBarColor;
    }

    public void setStatusBarColor(String statusBarColor) {
        this.statusBarColor = statusBarColor;
    }

    public String getDrawerHeadColor() {
        return drawerHeadColor;
    }

    public void setDrawerHeadColor(String drawerHeadColor) {
        this.drawerHeadColor = drawerHeadColor;
    }


}
