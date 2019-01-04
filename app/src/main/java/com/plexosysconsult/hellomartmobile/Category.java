package com.plexosysconsult.hellomartmobile;

/**
 * Created by Eric on 7/28/2017.
 */

public class Category {

    int id;
    String name;
    int parentId;
    int count;
    String slug;
    String imageUrl;


    public Category(int id, String name, int parentId, int count, String slug) {

        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.count = count;
        this.slug = slug;


    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getParentId() {
        return parentId;
    }

    public int getCount() {
        return count;
    }

    public String getSlug() {
        return slug;
    }
}
