package com.example.nizamudeenms.myflikz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * Created by nizamudeenms on 26/03/17.
 */

public class DetailActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        YouTubePlayerFragment youTubePlayerFragment;

        ImageView posterImageView;
        ImageView backDropImageView;
        TextView movieNameTextView;
        TextView overviewTextView, ratingTextView,releaseDateTextView;

        youTubePlayerFragment = (YouTubePlayerFragment)getFragmentManager().findFragmentById(R.id.youtube_playerviewfragment);
        youTubePlayerFragment.initialize(VideoPlayerConfig.API_KEY,this);
        posterImageView = (ImageView) findViewById(R.id.poster);
        backDropImageView = (ImageView) findViewById(R.id.back_drop);
        movieNameTextView = (TextView) findViewById(R.id.movie_name);
        overviewTextView = (TextView) findViewById(R.id.overview);
        ratingTextView = (TextView) findViewById(R.id.rating);
        releaseDateTextView = (TextView) findViewById(R.id.release_date);

        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("poster_url")).placeholder(R.mipmap.ic_launcher).crossFade().thumbnail(0.5f).into(posterImageView);
        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("backdrop_url")).placeholder(R.mipmap.ic_launcher).crossFade().thumbnail(0.5f).into(backDropImageView);

        Log.i("Nizam",getIntent().getStringExtra("title"));
        String title = getIntent().getStringExtra("title");
        String releaseDate = "Release Date : " +  getIntent().getStringExtra("release_date");
        String rating = "Rating : " + getIntent().getStringExtra("vote_average");

        movieNameTextView.setText(title);
        overviewTextView.setText(getIntent().getStringExtra("overview"));
        releaseDateTextView.setText(releaseDate);
        ratingTextView.setText(rating);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.cueVideo(VideoPlayerConfig.VIDEO_ID);
            youTubePlayer.cueVideo("s4C1gnqdrew");
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, 1).show();
        } else {
            String errorMessage = String.format("Eror in youtube api",youTubeInitializationResult.toString());
            Toast.makeText(this,errorMessage,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            getYoutubePlayerProvider().initialize(VideoPlayerConfig.API_KEY,this);
        }
    }

    protected YouTubePlayer.Provider getYoutubePlayerProvider(){
        return (YouTubePlayerView) findViewById(R.id.youtube_playerviewfragment);
    }
}
