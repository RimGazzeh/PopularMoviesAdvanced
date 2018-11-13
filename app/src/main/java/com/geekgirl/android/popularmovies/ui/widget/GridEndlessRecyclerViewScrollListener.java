package com.geekgirl.android.popularmovies.ui.widget;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Rim Gazzah on 15/09/18
 */
abstract public class GridEndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener{

    private final GridLayoutManager mLayoutManager;
    private int previousTotalItemCount = 10;
    private int page = 2;
    private boolean loading = true;

    protected GridEndlessRecyclerViewScrollListener(GridLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        int totalItemCount = mLayoutManager.getItemCount();
        int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();


        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        int visibleThreshold = 2;
        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            onLoadMore(++page);
            loading = true;
        }
    }

    public void resetState() {
        this.page = 1;
        this.previousTotalItemCount = 0;
        this.loading = true;
    }

    public void setState(int page, int count) {
        this.page = page;
        this.previousTotalItemCount = count;
        this.loading = false;
    }

    public int getCount() {
        return previousTotalItemCount;
    }

    public int getPage() {
        return page;
    }

    public abstract void onLoadMore(int page);

}
