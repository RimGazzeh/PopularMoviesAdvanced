
package com.geekgirl.android.popularmovies.model;

import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;


/**
 * Created by Rim Gazzah on 16/09/18
 */

public class Video implements IVideo {

    @PrimaryKey
    @NonNull
    private String id;
    @SerializedName("iso_3166_1")
    private String iso31661;
    @SerializedName("iso_639_1")
    private String iso6391;
    private String key;
    private String name;
    private String site;
    private Long size;
    private String type;
    private String videoThumbnail;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso31661() {
        return iso31661;
    }

    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    @Override
    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getVideoThumbnail() {
        return String.format("https://img.youtube.com/vi/%s/0.jpg", key);
    }

    @Override
    public String getVideoUrl() {
        return String.format("https://www.youtube.com/watch?v=", key);
    }
}
