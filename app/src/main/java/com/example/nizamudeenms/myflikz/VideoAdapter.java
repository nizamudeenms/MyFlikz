package com.example.nizamudeenms.myflikz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.util.Vector;

/**
 * Created by nizamudeenms on 29/06/17.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    public VideoAdapter(Vector<Video> videoList) {
        this.videoList = videoList;
    }

    Vector<Video> videoList;
    public VideoAdapter(Context context, Vector<Video> youtubeVideos) {
        super();
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        holder.videoWebView.loadData(videoList.get(position).getVideoUrl(),"text/html","utf-8" );
    }


    @Override
    public int getItemCount() {
        return videoList.size();
    }


    public class VideoViewHolder extends RecyclerView.ViewHolder {
        WebView videoWebView;

        public VideoViewHolder(View itemView) {
            super(itemView);
            videoWebView = (WebView) itemView.findViewById(R.id.webview_video);
            videoWebView.getSettings().setJavaScriptEnabled(true);
            videoWebView.setWebChromeClient(new WebChromeClient(){

            });

        }
    }
}
