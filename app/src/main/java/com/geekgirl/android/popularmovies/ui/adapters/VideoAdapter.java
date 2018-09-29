package com.geekgirl.android.popularmovies.ui.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.geekgirl.android.popularmovies.R;
import com.geekgirl.android.popularmovies.databinding.ItemVideoBinding;
import com.geekgirl.android.popularmovies.model.IVideo;

import java.util.List;

/**
 * Created by Rim Gazzah on 25/09/18
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private List<? extends IVideo> mVideosArrayList;
    private OnVideoClickListener mOnVideoClickListener;

    public void setVideosArrayList(List<? extends IVideo> videosArrayList) {
        mVideosArrayList = videosArrayList;
        notifyDataSetChanged();
    }

    public void setOnVideoClickListener(OnVideoClickListener onVideoClickListener) {
        mOnVideoClickListener = onVideoClickListener;
    }


    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVideoBinding itemVideoBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_video, parent, false);
        return new VideoViewHolder(itemVideoBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.itemVideoBinding.setVideo(mVideosArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return mVideosArrayList == null ? 0 : mVideosArrayList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        private ItemVideoBinding itemVideoBinding;

        public VideoViewHolder(ItemVideoBinding binding) {
            super(binding.getRoot());
            itemVideoBinding = binding;
            itemVideoBinding.videoImage.setOnClickListener(v -> {
                if (mOnVideoClickListener!=null){
                    mOnVideoClickListener.onVideoClick(mVideosArrayList.get(getAdapterPosition()).getKey());
                }
            });
        }
    }

    public interface OnVideoClickListener {
        void onVideoClick(String videoUrl);
    }
}
