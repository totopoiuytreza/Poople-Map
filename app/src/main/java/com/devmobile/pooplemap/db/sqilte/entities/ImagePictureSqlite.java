package com.devmobile.pooplemap.db.sqilte.entities;

public class ImagePictureSqlite {
    private String imagePath;
    private String description;

    public ImagePictureSqlite(String imagePath, String description) {
        this.imagePath = imagePath;
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
