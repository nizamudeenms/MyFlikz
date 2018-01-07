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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    String rating  = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView posterImageView;
        ImageView backDropImageView;
        TextView movieNameTextView;
        TextView overviewTextView, ratingTextView, releaseDateTextView;
        final FloatingActionButton favoriteFAB;


        posterImageView = (ImageView) findViewById(R.id.poster);
        backDropImageView = (ImageView) findViewById(R.id.back_drop);
        movieNameTextView = (TextView) findViewById(R.id.movie_name);
        overviewTextView = (TextView) findViewById(R.id.overview);
        ratingTextView = (TextView) findViewById(R.id.rating);
        releaseDateTextView = (TextView) findViewById(R.id.release_date);
        favoriteFAB = (FloatingActionButton) findViewById(R.id.favorite_fab);

//        System.out.println("favorite movie id is : "+ favoriteMovies.getString(favoriteMovies.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));

        final String movieId = getIntent().getStringExtra("id");
        final String backdropUrl = getIntent().getStringExtra("backdrop_url");
        final String posterUrl = getIntent().getStringExtra("poster_url");
        final String overview = getIntent().getStringExtra("overview");
        final String title = getIntent().getStringExtra("title");
        final String releaseDate = "Release Date : " + getIntent().getStringExtra("release_date");
        final String rating = "Rating : " + getIntent().getStringExtra("vote_average");

        System.out.println("movieid : "+movieId);
        favoriteMovies = checkFavorite(movieId);
        System.out.println("favoriteMovies.getCount() : " + favoriteMovies.getCount());

        if (favoriteMovies.moveToFirst()) {
            System.out.println(favoriteMovies.getString(favoriteMovies.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
            isFavorite = favoriteMovies.getString(favoriteMovies.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
            System.out.println("isFavorite  " + isFavorite);
        }
        favoriteMovies.close();

        if (!isFavorite.equals(movieId)) {
            favoriteFAB.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));
            System.out.println(" not favorite");
        } else {
            favoriteFAB.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp));
            System.out.println(" its favorite ");
        }


//        if (favoriteMovies.getCount() != 0) {
//            System.out.println("favoriteMovies Cursor : "+favoriteMovies);
//            isFavorite =  favoriteMovies.getString(favoriteMovies.getColumnIndex(MovieContract.MovieEntry.COLUMN_FAVORITE));
//        }


        favoriteFAB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isFavorite.equals("N")) {
//                    updateFavorites(movieId, isFavorite);
                    addFavorites(movieId,backdropUrl,posterUrl,overview,title,releaseDate,rating);
                    favoriteFAB.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp));
                    Toast toast = Toast.makeText(getApplicationContext(), "Added to Favorites", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
//                    updateFavorites(movieId, isFavorite);
                    removeFavorites(movieId);
                    favoriteFAB.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));
                    Toast toast = Toast.makeText(getApplicationContext(), "Removed from Favorites", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        Glide.with(getApplicationContext()).load(posterUrl).placeholder(R.mipmap.ic_launcher).crossFade().thumbnail(0.5f).into(posterImageView);
        Glide.with(getApplicationContext()).load(backdropUrl).placeholder(R.mipmap.ic_launcher).crossFade().thumbnail(0.5f).into(backDropImageView);


//        Log.i("Nizam",getIntent().getStringExtra("title"));


        movieNameTextView.setText(title);
        overviewTextView.setText(overview);
        releaseDateTextView.setText(releaseDate);
        ratingTextView.setText(rating);

        LinearLayout video_layout = (LinearLayout) findViewById(R.id.videos_layout);
        video_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent videos_intent = new Intent(DetailActivity.this, VideoActivity.class);
                videos_intent.putExtra("id", getIntent().getStringExtra("id"));
                startActivity(videos_intent);
            }
        });

        LinearLayout review_layout = (LinearLayout) findViewById(R.id.review_layout_detail);
        review_layout.setOnClickListener(new View.OnClickListener() {

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

        Cursor cFavoriteMovies = getContentResolver().query( ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, 2),null, MovieContract.MovieEntry.COLUMN_MOVIE_ID +"=" +movieId,null,null);
        return cFavoriteMovies;
    }

//    private boolean updateFavorites(String movieId, String fav) {
//        MovieDbHelper movieDbHelper = new MovieDbHelper(this);
//        movieDbHelper.updateFavorites(movieId, fav);
//        System.out.println(" updated");
//        return true;
//    }

    private void addFavorites(String movieId, String backdropUrl, String posterUrl, String overview, String title, String releaseDate, String rating){
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

        Toast.makeText(getBaseContext(),
                uri.toString(), Toast.LENGTH_LONG).show();
    }
}
