package com.example.nizamudeenms.myflikz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by nizamudeenms on 26/03/17.
 */

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView posterImageView;
        ImageView backDropImageView;
        TextView movieNameTextView;
        TextView overviewTextView, ratingTextView,releaseDateTextView;


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

}
