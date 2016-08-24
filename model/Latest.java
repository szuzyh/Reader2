package com.example.reader20.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 27721_000 on 2016/7/17.
 */
public class Latest implements Serializable{
    private String date;
    private List<StorySimple> stories;
//    @SerializedName("top_stories")
    private List<topStory> topStories;


    public void setDate(String date) {
        this.date = date;
    }
    public List<StorySimple> getStories() {
        return stories;
    }
    public void setStories(List<StorySimple> stories) {
        this.stories = stories;
    }
    public List<topStory> getTopStories() {
        return topStories;
    }
    public void setTopStories(List<topStory> topStories) {
        this.topStories = topStories;
    }
    public String getDate() {
        return date;
    }
    @Override
    public String toString() {
        return "Latest{"+
                "date='"+date+'\''+
                ",stories='"+stories+'\''+
                ",topStories='"+topStories+'\''+
                '}';
    }
    public class  topStory{
        private int id;
        private int type;
        private String image;
        private String title;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getImage() {
            return image;
        }

        @Override
        public String toString() {
            return "TopStory{"+
                    "id="+id+
                    ",type="+type+
                    ",title='"+title+'\''+
                    ",image='"+image+'\''+
                    '}';
        }
    }
}
