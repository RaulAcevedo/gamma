package com.gamma.test.raul.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Beer implements Parcelable {
    private int id;
    private String name;
    private String tagline;
    private Date first_brewed;
    private String description;
    private String image_url;
    private float ph;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Beer createFromParcel(Parcel in) {
            return new Beer(in);
        }

        public Beer[] newArray(int size) {
            return new Beer[size];
        }
    };


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public Date getFirst_brewed() {
        return first_brewed;
    }

    public void setFirst_brewed(Date first_brewed) {
        this.first_brewed = first_brewed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public float getPh() {
        return ph;
    }

    public void setPh(float ph) {
        this.ph = ph;
    }

    // Parcelling part
    public Beer(Parcel in){
        this.id = in.readInt();
        this.name = in.readString();
        this.tagline =  in.readString();
        this.first_brewed = new Date(in.readLong());
        this.description = in.readString();
        this.image_url = in.readString();
        this.ph = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.tagline);
        dest.writeLong(this.first_brewed.getTime());
        dest.writeString(this.description);
        dest.writeString(this.image_url);
        dest.writeFloat(this.ph);
    }
}
