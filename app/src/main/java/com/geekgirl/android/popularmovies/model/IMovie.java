package com.geekgirl.android.popularmovies.model;

/**
 * Created by Rim Gazzah on 22/09/18
 */
public interface IMovie {
    Long getId();
    String getTitle();
    String getOverview();
    Double getPopularity();
    String getPosterCompletePath();
    String getPosterPath();
    String getReleaseDate();
    Double getVoteAverage();
    Long getVoteCount();
    boolean isFavorite();
}
