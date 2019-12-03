package com.geekgirl.android.popularmovies.ui.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geekgirl.android.popularmovies.R;
import com.geekgirl.android.popularmovies.databinding.ItemReviewBinding;
import com.geekgirl.android.popularmovies.model.IReview;

import java.util.List;

/**
 * Created by Rim Gazzah on 25/09/18
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<? extends IReview> mReviewList;
    private OnReviewClickListener mOnReviewClickListener;
    private Context mContext;

    public void setReviewList(List<? extends IReview> reviewList) {
        mReviewList = reviewList;
        notifyDataSetChanged();
    }

    public void setOnReviewClickListener(OnReviewClickListener onReviewClickListener) {
        mOnReviewClickListener = onReviewClickListener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        ItemReviewBinding itemReviewBinding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.item_review, parent, false);
        return new ReviewViewHolder(itemReviewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        IReview iReview = mReviewList.get(position);
        holder.itemReviewBinding.setReview(mReviewList.get(position));
        if(iReview.isOpened()){
            holder.itemReviewBinding.tvDesc.setMaxLines(Integer.MAX_VALUE);
        }else {
            holder.itemReviewBinding.tvDesc.setMaxLines(2);
        }
    }

    @Override
    public int getItemCount() {
        return mReviewList == null ? 0 : mReviewList.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemReviewBinding itemReviewBinding;

        public ReviewViewHolder(ItemReviewBinding binding) {
            super(binding.getRoot());
            itemReviewBinding = binding;
            itemReviewBinding.relativeLayoutDetails.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            IReview iReview = mReviewList.get(getAdapterPosition());
            if(mOnReviewClickListener!=null){
                if(v == itemReviewBinding.relativeLayoutDetails){
                    iReview.setOpened(!iReview.isOpened());
                    mOnReviewClickListener.onReviewOpenClick(getAdapterPosition());
                }
            }

        }
    }

    public interface OnReviewClickListener {
        void onReviewOpenClick(int position);
    }
}
