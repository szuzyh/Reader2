package com.example.reader20.model;

import java.util.List;

/**
 * Created by 27721_000 on 2016/8/19.
 */
public class Before {
    private String date;

    private List<StorySimple> stories;

    public String getDate() {
        return date;
    }

    public List<StorySimple> getStories() {
        return stories;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStories(List<StorySimple> stories) {
        this.stories = stories;
    }
}
