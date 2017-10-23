package com.example.nizamudeenms.myflikz;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private String TAG = "DROGON" + MainActivity.class.getSimpleName();
    private static final String endpoint = "http://image.tmdb.org/t/p/";
    private ArrayList<Movie> movies;
    private ProgressDialog pDialog;
    private MovieAdapter mAdapter;
    private String urlFromJson = null;
    private RecyclerView recyclerView;
    private SQLiteDatabase mMovieDb;

    final String GET_POPULAR = "popular";
    final String GET_TOP = "top_rated";
    public String sortBy = GET_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movies = new ArrayList<>();
        Context context = this;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);


        MovieDbHelper movieDbHelper = new MovieDbHelper(this);
        mMovieDb = movieDbHelper.getWritableDatabase();


        FetchMoviesTask fetchMovies = new FetchMoviesTask();
        fetchMovies.execute();

        FetchMoviesTask topMoviesTask = new FetchMoviesTask();
        topMoviesTask.execute();

        Cursor cPopularMovies = getPopularMovies();
        Cursor cTopMovies = getTopMovies();
        Log.i(TAG, "Adapter Set");

        System.out.println("sortBy : " + sortBy);
        if (sortBy.equals(GET_POPULAR)) {
            System.out.println("inside popular movies");
            mAdapter = new MovieAdapter(getApplicationContext(), cPopularMovies);
            recyclerView.setAdapter(mAdapter);
        } else if (sortBy.equals(GET_TOP)) {
            System.out.println("inside top movies");
            mAdapter = new MovieAdapter(getApplicationContext(), cTopMovies);
            recyclerView.setAdapter(mAdapter);
        }


    }

    private Cursor getPopularMovies() {
        return mMovieDb.query(
                MovieContract.MovieEntry.POPULAR_MOVIE_TABLE,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private Cursor getTopMovies() {
        return mMovieDb.query(
                MovieContract.MovieEntry.TOP_MOVIE_TABLE,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }


    public class FetchMoviesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            final String FORECAST_BASE_URL = "https://api.themoviedb.org/3/movie/";
//        pDialog.setMessage("Downloading json...");
//        pDialog.show();

            String popular_url = FORECAST_BASE_URL + GET_POPULAR + "?api_key=" + BuildConfig.TMDB_KEY;
            String top_url = FORECAST_BASE_URL + GET_TOP + "?api_key=" + BuildConfig.TMDB_KEY;

            OkHttpClient client = new OkHttpClient();
            try {
                Request request = new Request.Builder()
                        .url(popular_url)
                        .build();

                Response response = client.newCall(request).execute();
                String res = response.body().string();
                JSONObject jsonObject = new JSONObject(res);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject c = jsonArray.getJSONObject(j);
                    String posterPath = c.getString("poster_path");
                    String backdropPath = c.getString("backdrop_path");
//                                Log.i(TAG, posterPath);
                    Log.i(TAG, c.getString("original_title"));
                    Log.i(TAG, c.getString("vote_average"));
                    Log.i(TAG, c.getString("release_date"));

                    Movie movie = new Movie();
                    movie.setPOSTER_PATH(endpoint + "w185/" + posterPath);
                    movie.setBACKDROP_PATH(endpoint + "w500/" + backdropPath);
                    movie.setID(c.getString("id"));
                    movie.setOVERVIEW(c.getString("overview"));
                    movie.setRELEASE_DATE(c.getString("release_date"));
                    movie.setTITLE(c.getString("original_title"));
                    movie.setVOTE_AVERAGE(c.getString("vote_average"));
                    movies.add(movie);

                    ContentValues cv = new ContentValues();
                    cv.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, endpoint + "w185/" + posterPath);
                    cv.put(MovieContract.MovieEntry.COLUMN_BACKDROP_URL, endpoint + "w500/" + backdropPath);
                    cv.put(MovieContract.MovieEntry.COLUMN_TITLE, c.getString("original_title"));
                    cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, c.getString("id"));
                    cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, c.getString("overview"));
                    cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, c.getString("release_date"));
                    cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, c.getString("vote_average"));
                    mMovieDb.insert(MovieContract.MovieEntry.POPULAR_MOVIE_TABLE, null, cv);
                    System.out.println("Value inserted in Popular DB ");
                }

                Request request2 = new Request.Builder()
                        .url(top_url)
                        .build();


                //Top Movies Starting
                Response response2 = client.newCall(request2).execute();
                String res2 = response2.body().string();
                JSONObject jsonObject2 = new JSONObject(res2);
                JSONArray jsonArray2 = jsonObject2.getJSONArray("results");

                for (int j = 0; j < jsonArray2.length(); j++) {
                    JSONObject c = jsonArray2.getJSONObject(j);
                    String posterPath = c.getString("poster_path");
                    String backdropPath = c.getString("backdrop_path");
                    Log.i(TAG, c.getString("original_title"));
                    Log.i(TAG, c.getString("vote_average"));
                    Log.i(TAG, c.getString("release_date"));

                    Movie movie = new Movie();
                    movie.setPOSTER_PATH(endpoint + "w185/" + posterPath);
                    movie.setBACKDROP_PATH(endpoint + "w500/" + backdropPath);
                    movie.setID(c.getString("id"));
                    movie.setOVERVIEW(c.getString("overview"));
                    movie.setRELEASE_DATE(c.getString("release_date"));
                    movie.setTITLE(c.getString("original_title"));
                    movie.setVOTE_AVERAGE(c.getString("vote_average"));
                    movies.add(movie);

                    ContentValues cv = new ContentValues();
                    cv.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, endpoint + "w185/" + posterPath);
                    cv.put(MovieContract.MovieEntry.COLUMN_BACKDROP_URL, endpoint + "w500/" + backdropPath);
                    cv.put(MovieContract.MovieEntry.COLUMN_TITLE, c.getString("original_title"));
                    cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, c.getString("id"));
                    cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, c.getString("overview"));
                    cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, c.getString("release_date"));
                    cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, c.getString("vote_average"));
                    mMovieDb.insert(MovieContract.MovieEntry.TOP_MOVIE_TABLE, null, cv);
                    System.out.println("Value inserted in Top DB ");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }
    }
            @Override
            public boolean onCreateOptionsMenu (Menu menu){
                getMenuInflater().inflate(R.menu.main, menu);
                return true;
            }

            @Override
            public boolean onOptionsItemSelected (MenuItem item){
                int menuSelected = item.getItemId();

                if (menuSelected == R.id.popular) {
                    sortBy = GET_POPULAR;
                    Cursor cPopularMovies = getPopularMovies();
                    mAdapter = new MovieAdapter(getApplicationContext(), cPopularMovies);
                    recyclerView.setAdapter(mAdapter);
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.popular_sel_mes), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (menuSelected == R.id.topRated) {
                    sortBy = GET_TOP;
                    Cursor cTopMovies = getTopMovies();
                    mAdapter = new MovieAdapter(getApplicationContext(), cTopMovies);
                    recyclerView.setAdapter(mAdapter);
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.top_rated_sel_msg), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
                    toast.show();
                }
                return true;
            }


//    @Override
//    public void onClick(String weatherForDay) {
//        Context context = this;
//        Intent intent = new Intent(this,DetailActivity.class);
//        Toast.makeText(context, " Activity Launched", Toast.LENGTH_SHORT).show();
//        startActivity(intent);
//    }
        }