
package com.geekgirl.android.popularmovies.model;

import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;


/**
 * Created by Rim Gazzah on 16/09/18
 */

public class Review  implements IReview{

    @PrimaryKey
    @NonNull
    private String id;
    private String author;
    private String content;
    private String url;

    @Ignore
    private boolean isOpened = false;

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
