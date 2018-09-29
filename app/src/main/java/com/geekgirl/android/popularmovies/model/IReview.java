package com.geekgirl.android.popularmovies.model;

/**
 * Created by Rim Gazzah on 25/09/18
 */
public interface IReview {

    String getId();
    String getAuthor();
    String getContent();
    String getUrl();
    boolean isOpened();
    void setOpened(boolean opened);
}
