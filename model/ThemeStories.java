package com.example.reader20.model;

import java.util.List;

/**
 * Created by 27721_000 on 2016/7/17.
 */
public class ThemeStories {

    private String description;
    private int color;
    private String background;
    private String name;
    private String image;
    private String image_source;

    private List<StorySimple> stories;
    private List<EditorsEntity> editors;


    @Override
    public String toString() {
        return "ThemeStories{" +
                "description='" + description + '\'' +
                ", background='" + background + '\'' +
                ", color=" + color +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", image_source='" + image_source + '\'' +
                ", stories=" + stories +
                ", editors=" + editors +
                '}';
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setDescription(String descrption) {
        this.description = descrption;
    }

    public void setEditors(List<EditorsEntity> editors) {
        this.editors = editors;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public void setStories(List<StorySimple> stories) {
        this.stories = stories;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public List<EditorsEntity> getEditors() {
        return editors;
    }

    public List<StorySimple> getStories() {
        return stories;
    }

    public String getBackground() {
        return background;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getImage_source() {
        return image_source;
    }


    public class storiesEntity{
        private int id;
        private String title;
        private int type;
        private List<String> images;

        public void setId(int id) {
            this.id = id;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public int getType() {
            return type;
        }

        public List<String> getImages() {
            return images;
        }

        public String getTitle() {
            return title;
        }

    }
    public static class EditorsEntity{
        private int id;
        private String url;
        private String bio;
        private String avater;
        private String name;


        public void setUrl(String url) {
            this.url = url;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAvater(String avater) {
            this.avater = avater;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public String getAvater() {
            return avater;
        }

        public String getBio() {
            return bio;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }
    }
}
