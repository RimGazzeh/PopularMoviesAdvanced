package com.geekgirl.android.popularmovies.data;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Created by Rim Gazzah on 16/09/18
 */
public class ListLongConverter {

    private static Gson gson = new Gson();
    @TypeConverter
    public static List<Long> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Long>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public String objectListToString(List<Long> objects) {
        return gson.toJson(objects);
    }
}
