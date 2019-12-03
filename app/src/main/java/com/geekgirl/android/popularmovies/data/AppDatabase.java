package com.geekgirl.android.popularmovies.data;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;

import com.geekgirl.android.popularmovies.model.Movie;
import com.geekgirl.android.popularmovies.utils.Logger;

/**
 * Created by Rim Gazzah on 11/09/18
 */
@Database(entities = {Movie.class}, version = 1, exportSchema = false)
@TypeConverters(ListLongConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "popularmovies";
    private static AppDatabase sInstance;
    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Logger.d("Creating new database instance");
                sInstance = Room.databaseBuilder(context,
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Logger.d("Getting the database instance");
        return sInstance;
    }


    public abstract MovieDAO movieDAO();
}
