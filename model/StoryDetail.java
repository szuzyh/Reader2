package com.example.reader20.model;

import java.util.List;

/**
 * Created by 27721_000 on 2016/7/17.
 */
public class StoryDetail {




    private int id;
    private String body;
    private String image;
    private String title;
    private int type;
    private List<String> css;


    @Override
    public String toString() {
        return "StoryDetail{"+
                "id="+id+
                ",body='"+body+'\''+
                ",image='"+image+'\''+
                ",title='"+title+'\''+
                ",type="+type+
                ",css="+css+
                '}';

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getCss() {
        return css;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }
}
