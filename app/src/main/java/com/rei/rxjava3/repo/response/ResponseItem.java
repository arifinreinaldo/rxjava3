package com.rei.rxjava3.repo.response;

import com.google.gson.annotations.SerializedName;

public class ResponseItem {
    @SerializedName("img")
    private String img;

    @SerializedName("level")
    private String level;

    @SerializedName("name")
    private String name;

    public String getImg() {
        return img;
    }

    public String getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }
}