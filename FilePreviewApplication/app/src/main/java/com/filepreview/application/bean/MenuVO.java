package com.filepreview.application.bean;

import java.io.Serializable;

public class MenuVO implements Serializable {
    private String title;
    private MenuType type;

    public MenuVO(String title, MenuType type) {
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MenuType getType() {
        return type;
    }

    public void setType(MenuType type) {
        this.type = type;
    }
}
