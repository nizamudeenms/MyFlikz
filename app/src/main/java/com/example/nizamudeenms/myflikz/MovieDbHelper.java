package com.example.nizamudeenms.myflikz;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by nizamudeenms on 08/07/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;
    final String SQL_CREATE_POPULAR_MOVIE_TABLE = "CREATE TABLE IF NOT EXISTS " + MovieContract.MovieEntry.POPULAR_MOVIE_TABLE + " (" +
            MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_BACKDROP_URL + " INTEGER NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_OVERVIEW + " INTEGER NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_POSTER_URL + " INTEGER NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_TITLE + " INTEGER NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " INTEGER NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_FAVORITE + " CHAR NOT NULL DEFAULT 'N', " +
            " UNIQUE (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

    final String SQL_CREATE_TOP_MOVIE_TABLE = "CREATE TABLE IF NOT EXISTS " + MovieContract.MovieEntry.TOP_MOVIE_TABLE + " (" +
            MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_BACKDROP_URL + " INTEGER NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_OVERVIEW + " INTEGER NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_POSTER_URL + " INTEGER NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_TITLE + " INTEGER NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " INTEGER NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_FAVORITE + " CHAR NOT NULL DEFAULT 'N', " +
            " UNIQUE (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";


    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i("Database Created", "DB Created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("inside Oncrate of sqllite ");

        db.execSQL(SQL_CREATE_POPULAR_MOVIE_TABLE);
        Log.i("Table Created", "POPULAR_MOVIE_TABLE TableCreated");

        db.execSQL(SQL_CREATE_TOP_MOVIE_TABLE);
        Log.i("Table Created", "TOP_MOVIE TableCreated");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.POPULAR_MOVIE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TOP_MOVIE_TABLE);
        onCreate(db);
        Log.i("DB Dropped", "DB Dropped");
    }

    public void updateFavorites(String movieId, String fav) {
        String POP_TABLE_NAME = MovieContract.MovieEntry.POPULAR_MOVIE_TABLE;
        String TOP_TABLE_NAME = MovieContract.MovieEntry.TOP_MOVIE_TABLE;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        System.out.println(" fave from details activity in dbhelper : " + fav);
        System.out.println(" movied id from dbhelper : " + movieId);
        if (fav == "N" || fav.equals("N")) {
            cv.put(MovieContract.MovieEntry.COLUMN_FAVORITE, "Y");
        } else {
            cv.put(MovieContract.MovieEntry.COLUMN_FAVORITE, "N");
        }
        int popCount = db.update(POP_TABLE_NAME, cv, " id = ?", new String[]{movieId});
        int topCount = db.update(TOP_TABLE_NAME, cv, " id = ?", new String[]{movieId});
        db.close();
        System.out.println(" popCount is : " + popCount);
        System.out.println(" topCount is : " + topCount);
    }

}
