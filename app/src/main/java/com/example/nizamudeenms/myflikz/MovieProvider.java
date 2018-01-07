package com.example.nizamudeenms.myflikz;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by nizamudeenms on 04/01/18.
 */

public class MovieProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.example.nizamudeenms.myflikz";
    static final String URL = "content://" + PROVIDER_NAME +"/"+ MovieContract.MovieEntry.FAV_MOVIE_TABLE;
    static final Uri CONTENT_URI = Uri.parse(URL);

    private static HashMap<String, String> MOVIE_PROJECTION_MAP;
    static final int MOVIES = 1;
    static final int MOVIE_ID = 2;

    private SQLiteDatabase db;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, MovieContract.MovieEntry.FAV_MOVIE_TABLE, MOVIES);
        uriMatcher.addURI(PROVIDER_NAME, MovieContract.MovieEntry.FAV_MOVIE_TABLE+"/#", MOVIE_ID);
    }



    @Override
    public boolean onCreate() {

        Context context = getContext();
        MovieDbHelper dbHelper = new MovieDbHelper(context);

        db = dbHelper.getWritableDatabase();
        return (dbHelper == null)? false:true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(MovieContract.MovieEntry.FAV_MOVIE_TABLE);

        switch (uriMatcher.match(uri)) {
            case MOVIE_ID:
                qb.appendWhere( MOVIE_ID + "=" + uri.getPathSegments().get(1));
                break;
            case MOVIES:
                qb.setProjectionMap(MOVIE_PROJECTION_MAP);
                break;
            default:
        }

        if (sortOrder == null || sortOrder == ""){
            sortOrder = MovieContract.MovieEntry.COLUMN_TITLE;
        }

        Cursor c = qb.query(db,	projection,	selection,
                selectionArgs,null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        long rowID = db.insert(MovieContract.MovieEntry.FAV_MOVIE_TABLE, "", values);

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case MOVIES:
                count = db.delete(MovieContract.MovieEntry.FAV_MOVIE_TABLE, selection, selectionArgs);
                break;

            case MOVIE_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(MovieContract.MovieEntry.FAV_MOVIE_TABLE, MovieContract.MovieEntry.COLUMN_MOVIE_ID +  " = " + id +
                                (!TextUtils.isEmpty(selection) ? "AND (" + selection + ')' : ""), selectionArgs);

                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
