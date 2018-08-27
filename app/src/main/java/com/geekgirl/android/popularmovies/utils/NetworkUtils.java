package com.geekgirl.android.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.geekgirl.android.popularmovies.model.Movie;
import com.geekgirl.android.popularmovies.model.MoviesData;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Rim Gazzah on 21/08/18
 */
public class NetworkUtils {

    final static String PARAM_API_KEY = "api_key";

    public static URL getUrlQuery(Context context){
        String sortOption = Prefs.getPref(Prefs.SORT_OPTION, context);
        URL queryhUrl = null;
        if(sortOption.equals(Prefs.MOST_POPULAR_VALUE)){
            queryhUrl = buildUrl(ApiEndpoints.GET_MOVIES_POPULAR);
        }else {
            queryhUrl = buildUrl(ApiEndpoints.GET_MOVIES_TOP_RATED);
        }
        return queryhUrl;
    }

    private static URL buildUrl(String baseUrl) {
        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, ApiEndpoints.API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static List<Movie> parseMoviesData(String data) {
        Gson gson = new Gson();
        MoviesData moviesData = gson.fromJson(data, MoviesData.class);
        return moviesData.getMovies();
    }


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


}
