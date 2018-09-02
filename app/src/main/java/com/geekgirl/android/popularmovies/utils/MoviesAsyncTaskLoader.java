package com.geekgirl.android.popularmovies.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import com.geekgirl.android.popularmovies.model.Movie;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by Rim Gazzah on 02/09/18
 */
public class MoviesAsyncTaskLoader extends AsyncTaskLoader<List<Movie>> {

    Bundle args;
    List<Movie> movieArrayList;


    public MoviesAsyncTaskLoader(Context context, Bundle args) {
        super(context);
        this.args = args;
    }

    @Override
    protected void onStartLoading() {
        if (args == null) {
            Logger.e("Null argument");
            return;
        }

        if (movieArrayList != null) {
            deliverResult(movieArrayList);
        } else {
            forceLoad();
        }

    }

    @Nullable
    @Override
    public List<Movie> loadInBackground() {

        String queryUrlString = args.getString(Constants.QUERY_URL_EXTRA);

        if (queryUrlString == null || TextUtils.isEmpty(queryUrlString)) {
            return null;
        }

        try {
            URL movieQueryUrl = new URL(queryUrlString);
            String movieQueryResult = NetworkUtils.getResponseFromHttpUrl(movieQueryUrl);
            return NetworkUtils.parseMoviesData(movieQueryResult);
        } catch (IOException e) {
            Logger.e(e.getMessage());
            return null;
        }
    }


    @Override
    public void deliverResult(List<Movie> data) {
        movieArrayList = data;
        super.deliverResult(data);
    }
}
