package com.example.reader20.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by 27721_000 on 2016/7/17.
 */
public class StorySimple implements Parcelable {
    String date;
    private String id;
    private List<String> images;
    private String title;
    private String multipic;
    private int type;

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getImages() {
        return images;
    }

    public void setMultipic(String multipic) {
        this.multipic = multipic;
    }

    public String getMultipic() {
        return multipic;
    }

    @Override
    public String toString() {
        return "StorySimple{" +
                "id=" + id +
                ", images=" + images +
                ", title='" + title + '\'' +
                ", multipic='" + multipic + '\'' +
                '}';
    }



    public StorySimple(){}

    protected StorySimple(Parcel in) {
        this.id=in.readString();
        this.images=in.createStringArrayList();
        this.title=in.readString();
        this.multipic=in.readString();
    }

    public static final Creator<StorySimple> CREATOR = new Creator<StorySimple>() {
        @Override
        public StorySimple createFromParcel(Parcel in) {
            return new StorySimple(in);
        }

        @Override
        public StorySimple[] newArray(int size) {
            return new StorySimple[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeStringList(this.images);
        dest.writeString(this.title);
        dest.writeString(this.multipic);
    }

}
