
package com.geekgirl.android.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created by Rim Gazzah on 16/09/18
 */

public class ApiResponse<T> {

    @SerializedName("results")
    private List<T> listData;



    public List<T> getListData() {
        return listData;
    }

    public void setListData(List<T> listData) {
        this.listData = listData;
    }
}
