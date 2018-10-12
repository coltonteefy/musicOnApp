package com.example.coltonteefy.moapp;

public class SingleItemModel {

    private String name;
    private String url;
    private String description;
    private String songName;


    public SingleItemModel() {
    }

    public SingleItemModel(String name,String songName, String url) {
        this.name = name;
        this.songName = songName;
        this.url = url;
    }


    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public String getName() {

        return name;
    }

    public void setSongName(String songName) {

        this.songName = songName;
    }

    public String getSongName() {

        return songName;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }
}
