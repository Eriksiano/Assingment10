package edu.temple.abrowser;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * DbManager class handles the storage of Browser url bookmarks
 * It has a table called 'Bookmarks'
 * Which has two text fields, 'title' and 'url',
 * The 'title' is Primary Key, so there can not be two
 * rows witch same title
 */
public class DbManager extends SQLiteOpenHelper
{

    public static final String DB_NAME    = "Bookmark_database";
    public static final int    DB_VERSION = 1;
    public static final String TABLE        = "Bookmarks";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_URL   = "url";

    public DbManager(@Nullable Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Creates the database
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(
                "CREATE TABLE " + TABLE +
                "(title text primary key, url text)"
                );
    }

    /**
     * Upgrades the database (Contents will be removed)
     *
     * @param db
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    /**
     * Adds a bookmark to the Bookmarks table
     *
     * @param  title
     * @param  url
     * @return long         Affected row amount
     */
    public long insertBookmark(String title, String url)
    {
        long result = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_URL, url);

        try
        {
            // ----------------------------------------------------
            // - Check if bookmark exists
            // - Perform the insert only when there is no result
            // -
            // ----------------------------------------------------
            Cursor resource = db.rawQuery(
                    "SELECT " + COLUMN_TITLE + ", " + COLUMN_URL +
                    " FROM " + TABLE +
                            " WHERE " + COLUMN_TITLE + " = ?", new String[] { title });

            if (resource.getCount() == 0)
            {
                result = db.insert(TABLE, null, contentValues);
            }
        }
        catch (Exception e)
        {
            result = 0;
        }

        return result;
    }

    /**
     * Returns the bookmarks from table Bookmarks
     *
     * @return ArrayList<Bookmark>
     */
    public ArrayList<Bookmark> getBookmarks()
    {
        ArrayList<Bookmark> bookmarkList = new ArrayList<Bookmark>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor resource = db.rawQuery("SELECT " + COLUMN_TITLE + ", " + COLUMN_URL + " FROM " + TABLE, null);
        resource.moveToFirst();

        while (resource.isAfterLast() == false)
        {
            String title = resource.getString(resource.getColumnIndex(COLUMN_TITLE));
            String url   = resource.getString(resource.getColumnIndex(COLUMN_URL));
            bookmarkList.add(new Bookmark(title, url));
            resource.moveToNext();
        }

        return bookmarkList;
    }

    /**
     * Removes a bookmark (identified by title) from Bookmarks table
     *
     * @param  title
     * @return int   Affected rows
     */
    public int deleteBookmark(String title)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE, "title = ?", new String[] { title });
    }
}
