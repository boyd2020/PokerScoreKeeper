package com.example.pokerscorekeeper.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.pokerscorekeeper.managers.Constants;
import com.example.pokerscorekeeper.managers.DatabaseHandler;

public class PokerProvider extends ContentProvider {

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private DatabaseHandler handler;
    private static final String AUTHORITY = "com.example.pokerscorekeeper";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // UriMatcher codes
    private static final int PLAYER = 100, PLAYER_ID = 101, SCORE = 102, SCORE_ID = 103;

    public static final Uri PLAYER_URI = Uri.withAppendedPath(BASE_CONTENT_URI, Constants.PLAYER_TABLE);
    public static final Uri SCORE_URI = Uri.withAppendedPath(BASE_CONTENT_URI, Constants.SCORES_TABLE);

    private static UriMatcher buildUriMatcher(){
        // Build a UriMatcher by adding a specific code to return based on a match
        // It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = AUTHORITY;

        // add a code for each type of URI you want
        matcher.addURI(authority, Constants.PLAYER_TABLE, PLAYER);
        matcher.addURI(authority, Constants.PLAYER_TABLE + "/#", PLAYER_ID);
        matcher.addURI(authority, Constants.SCORES_TABLE, SCORE);
        matcher.addURI(authority, Constants.SCORES_TABLE + "/#", SCORE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        handler = new DatabaseHandler(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        SQLiteDatabase db = handler.getReadableDatabase();

        switch (uriMatcher.match(uri))
        {
            case PLAYER:
                cursor = db.query(Constants.PLAYER_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case PLAYER_ID:
                cursor = db.query(Constants.PLAYER_TABLE, projection, Constants.PLAYER_ID + " =?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))}, null, null, sortOrder);
                break;

            case SCORE:
                cursor = db.query(Constants.PLAYER_TABLE + ", " + Constants.SCORES_TABLE, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case SCORE_ID:
                cursor = db.query(Constants.SCORES_TABLE, projection, Constants.SCORE_ID + " =?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))}, null, null, sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = handler.getWritableDatabase();
        long id;

        int match = uriMatcher.match(uri);

        switch (match)
        {
            case PLAYER:
                id = db.insert(Constants.PLAYER_TABLE, null, values);
                break;

            case SCORE:
                id = db.insert(Constants.SCORES_TABLE, null, values);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = handler.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int numDeleted;

        switch (match)
        {
            case PLAYER:
                numDeleted = db.delete(Constants.PLAYER_TABLE, selection, selectionArgs);
                break;

            case PLAYER_ID:
                numDeleted = db.delete(Constants.PLAYER_TABLE,
                        Constants.PLAYER_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;

            case SCORE:
                numDeleted = db.delete(Constants.SCORES_TABLE, selection, selectionArgs);
                break;

            case SCORE_ID:
                numDeleted = db.delete(Constants.SCORES_TABLE,
                        Constants.SCORES_TABLE + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return numDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = handler.getWritableDatabase();
        int numUpdated = 0;

        if (values == null)
            throw new IllegalArgumentException("Cannot have null content values");


        switch (uriMatcher.match(uri))
        {
            case PLAYER:
                numUpdated = db.update(Constants.PLAYER_TABLE, values, selection, selectionArgs);
                break;

            case PLAYER_ID:
                numUpdated = db.update(Constants.PLAYER_TABLE,
                        values,
                        Constants.PLAYER_ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;

            case SCORE:
                numUpdated = db.update(Constants.SCORES_TABLE, values, selection, selectionArgs);
                break;

            case SCORE_ID:
                numUpdated = db.update(Constants.SCORES_TABLE,
                        values,
                        Constants.SCORE_ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return numUpdated;
    }

    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);
        return "type";
    }
}
