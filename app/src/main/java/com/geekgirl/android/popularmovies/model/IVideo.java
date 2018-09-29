package com.geekgirl.android.popularmovies.model;

/**
 * Created by Rim Gazzah on 25/09/18
 */
public interface IVideo {
    String getId();
    String getKey();
    String getName();
    String getSite();
    Long getSize();
    String getType();
    String getVideoThumbnail();
    String getVideoUrl();
}
