package com.example.nizamudeenms.myflikz;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

/**
 * Created by nizamudeenms on 26/03/17.
 */

public class DetailActivity extends AppCompatActivity {
    private SQLiteDatabase mMovieDb;
    Cursor favoriteMovies;
    String isFavorite = "N";
    String movieId = null;
    String posterUrl = null;
    String overview = null;
    String backdropUrl = null;
    String title = null;
    String releaseDate = null;
    String rating = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView posterImageView;
        ImageView backDropImageView;
        TextView movieNameTextView;
        TextView overviewTextView, ratingTextView, releaseDateTextView;
        CardView videoCardView, reviewCardView;
        final FloatingActionButton favoriteFAB;


        posterImageView = (ImageView) findViewById(R.id.poster);
        backDropImageView = (ImageView) findViewById(R.id.back_drop);
        movieNameTextView = (TextView) findViewById(R.id.movie_name);
        overviewTextView = (TextView) findViewById(R.id.overview);
        ratingTextView = (TextView) findViewById(R.id.rating);
        releaseDateTextView = (TextView) findViewById(R.id.release_date);
        favoriteFAB = (FloatingActionButton) findViewById(R.id.favorite_fab);
        videoCardView = findViewById(R.id.video_card_view);
        reviewCardView = findViewById(R.id.review_card_view);


        movieId = getIntent().getStringExtra("id");
        backdropUrl = getIntent().getStringExtra("backdrop_url");
        posterUrl = getIntent().getStringExtra("poster_url");
        overview = getIntent().getStringExtra("overview");
        title = getIntent().getStringExtra("title");
        releaseDate = "Release Date : " + getIntent().getStringExtra("release_date");
        rating = "Rating : " + getIntent().getStringExtra("vote_average");

        favoriteMovies = checkFavorite(movieId);

        if (favoriteMovies.moveToFirst()) {
            isFavorite = favoriteMovies.getString(favoriteMovies.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
        }
        favoriteMovies.close();

        if (!isFavorite.equals(movieId)) {
            favoriteFAB.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));
        } else {
            favoriteFAB.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp));
        }

        favoriteFAB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isFavorite.equals("N")) {
                    addFavorites(movieId, backdropUrl, posterUrl, overview, title, releaseDate, rating);
                    favoriteFAB.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp));
                    Toast toast = Toast.makeText(getApplicationContext(), "Added to Favorites", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    removeFavorites(movieId);
                    favoriteFAB.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));
                    Toast toast = Toast.makeText(getApplicationContext(), "Removed from Favorites", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        Glide.with(getApplicationContext()).load(posterUrl).placeholder(R.mipmap.ic_launcher).crossFade().thumbnail(0.5f).into(posterImageView);
        Glide.with(getApplicationContext()).load(backdropUrl).placeholder(R.mipmap.ic_launcher).crossFade().thumbnail(0.5f).into(backDropImageView);

        movieNameTextView.setText(title);
        overviewTextView.setText(overview);
        releaseDateTextView.setText(releaseDate);
        ratingTextView.setText(rating);

        videoCardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent videos_intent = new Intent(DetailActivity.this, VideoActivity.class);
                videos_intent.putExtra("id", getIntent().getStringExtra("id"));
                startActivity(videos_intent);
            }
        });

        reviewCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent review_intent = new Intent(DetailActivity.this, ReviewActivity.class);
                review_intent.putExtra("id", getIntent().getStringExtra("id"));
                startActivity(review_intent);
            }
        });
    }

    private void removeFavorites(String movieId) {
        Uri uriWithId = MovieProvider.CONTENT_URI.buildUpon().appendPath(movieId).build();
        getContentResolver().delete(uriWithId, null, null);

    }

    private Cursor checkFavorite(String movieId) {

        Cursor cFavoriteMovies = getContentResolver().query(ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, 2), null, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=" + movieId, null, null);
        return cFavoriteMovies;
    }

    private void addFavorites(String movieId, String backdropUrl, String posterUrl, String overview, String title, String releaseDate, String rating) {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);
        values.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, posterUrl);
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, overview);
        values.put(MovieContract.MovieEntry.COLUMN_BACKDROP_URL, backdropUrl);
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
        values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, rating);
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, title);


        Uri uri = getContentResolver().insert(
                MovieProvider.CONTENT_URI, values);

    }
}
