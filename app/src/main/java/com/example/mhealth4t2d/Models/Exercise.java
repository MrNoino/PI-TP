package com.example.mhealth4t2d.Models;

public class Exercise {

    private int id;

    private String title;

    private String description;

    private String category;

    private String image_link;

    private String video_link;

    private String datetime;

    public Exercise(int id, String title, String description, String category, String image_link, String video_link, String datetime) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.image_link = image_link;
        this.video_link = video_link;
        this.datetime = datetime;

    }

    public int getId() {

        return id;

    }

    public void setId(int id) {

        this.id = id;

    }

    public String getTitle() {

        return title;

    }

    public void setTitle(String title) {

        this.title = title;

    }

    public String getDescription() {

        return description;

    }

    public void setDescription(String description) {

        this.description = description;

    }

    public String getCategory() {

        return category;

    }

    public void setCategory(String category) {

        this.category = category;

    }

    public String getImage_link() {

        return image_link;

    }

    public void setImage_link(String image_link) {

        this.image_link = image_link;

    }

    public String getVideo_link() {

        return video_link;

    }

    public void setVideo_link(String video_link) {

        this.video_link = video_link;

    }

    public String getDatetime() {

        return datetime;

    }

    public void setDatetime(String datetime) {

        this.datetime = datetime;

    }
}
