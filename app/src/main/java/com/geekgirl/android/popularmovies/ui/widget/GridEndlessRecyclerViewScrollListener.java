package com.geekgirl.android.popularmovies.ui.widget;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Rim Gazzah on 15/09/18
 */
abstract public class GridEndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener{

    private GridLayoutManager mGridLayoutManager;
    private int mPreviousTotalItemCount = 0;
    private boolean mLoading = true;
    private int mStartingPageIndex = 0;
    private int currentPage = 0;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int mVisibleThreshold = 5;


    private GridEndlessRecyclerViewScrollListener(GridLayoutManager gridLayoutManager){
        mGridLayoutManager= gridLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int lastVisibleItemPosition = mGridLayoutManager.findLastVisibleItemPosition();
        int totalItemCount = mGridLayoutManager.getItemCount();
        if (mLoading && (totalItemCount > mPreviousTotalItemCount)) {
            mLoading = false;
            mPreviousTotalItemCount = totalItemCount;

            if (!mLoading && (lastVisibleItemPosition + mVisibleThreshold) > totalItemCount
                    && recyclerView.getAdapter().getItemCount() > mVisibleThreshold) {// This condition will useful when recyclerview has less than mVisibleThreshold items
                currentPage++;
                onLoadMore(currentPage, totalItemCount);
                mLoading = true;
            }
        }
        super.onScrolled(recyclerView, dx, dy);
    }

    public abstract void onLoadMore(int page, int totalItemsCount);

}
