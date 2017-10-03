package com.example.nizamudeenms.myflikz;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView posterImageView;
        ImageView backDropImageView;
        TextView movieNameTextView;
        TextView overviewTextView, ratingTextView,releaseDateTextView;
        final FloatingActionButton favoriteFAB;


        posterImageView = (ImageView) findViewById(R.id.poster);
        backDropImageView = (ImageView) findViewById(R.id.back_drop);
        movieNameTextView = (TextView) findViewById(R.id.movie_name);
        overviewTextView = (TextView) findViewById(R.id.overview);
        ratingTextView = (TextView) findViewById(R.id.rating);
        releaseDateTextView = (TextView) findViewById(R.id.release_date);
        favoriteFAB = (FloatingActionButton) findViewById(R.id.favorite_fab);


        if (getIntent().getStringExtra("favorite") == "N") {
            favoriteFAB.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));
        }else{
            favoriteFAB.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp));
        }

        favoriteMovies = checkFavorite();

        System.out.println("favoriteMovies.getCount() : "+favoriteMovies.getCount());
        if (favoriteMovies.getCount() != 0) {
            System.out.println("favoriteMovies Cursor : "+favoriteMovies);
            isFavorite =  favoriteMovies.getString(favoriteMovies.getColumnIndex(MovieContract.MovieEntry.COLUMN_FAVORITE));
        }

        System.out.println("isFavorite  "+isFavorite);




        favoriteFAB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (isFavorite.equals("N")) {

                    System.out.println("CURRENT MOVIE ID : "+ getIntent().getStringExtra("id"));

                    favoriteFAB.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));
                    Toast toast = Toast.makeText(getApplicationContext(), "Added to Favorites", Toast.LENGTH_LONG);
                    toast.show();
                }else{
                    favoriteFAB.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp));
                    Toast toast = Toast.makeText(getApplicationContext(), "Removed from Favorites", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });

        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("poster_url")).placeholder(R.mipmap.ic_launcher).crossFade().thumbnail(0.5f).into(posterImageView);
        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("backdrop_url")).placeholder(R.mipmap.ic_launcher).crossFade().thumbnail(0.5f).into(backDropImageView);

//        Log.i("Nizam",getIntent().getStringExtra("title"));
        String title = getIntent().getStringExtra("title");
        String releaseDate = "Release Date : " +  getIntent().getStringExtra("release_date");
        String rating = "Rating : " + getIntent().getStringExtra("vote_average");

        movieNameTextView.setText(title);
        overviewTextView.setText(getIntent().getStringExtra("overview"));
        releaseDateTextView.setText(releaseDate);
        ratingTextView.setText(rating);

        LinearLayout video_layout = (LinearLayout )findViewById(R.id.videos_layout);
        video_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent videos_intent = new Intent(DetailActivity.this ,VideoActivity.class);
                videos_intent.putExtra("id",getIntent().getStringExtra("id"));
                startActivity(videos_intent);
            }
        });

        LinearLayout review_layout = (LinearLayout )findViewById(R.id.review_layout_detail);
        review_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent review_intent = new Intent(DetailActivity.this ,ReviewActivity.class);
                review_intent.putExtra("id",getIntent().getStringExtra("id"));
                startActivity(review_intent);
            }
        });
    }

    private Cursor checkFavorite() {
        MovieDbHelper movieDbHelper = new MovieDbHelper(this);
        mMovieDb = movieDbHelper.getReadableDatabase();
        Cursor cFavoriteMovies = getFavoriteMovies();
        return cFavoriteMovies;
    }

    private Cursor getFavoriteMovies() {
        return mMovieDb.rawQuery("SELECT  * FROM POPULAR_MOVIES_TABLE POP WHERE  POP.FAVORITE = 'Y' " +
                "UNION ALL " +
                "SELECT  * FROM TOP_MOVIES_TABLE TOP WHERE  TOP.FAVORITE = 'Y' ", null);
    }


}
