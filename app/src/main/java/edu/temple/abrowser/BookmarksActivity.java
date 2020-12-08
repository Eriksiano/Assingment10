package edu.temple.abrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

public class BookmarksActivity extends AppCompatActivity
{
    public static final String BOOKMARK_TAG = "bookmark";

    DbManager           dbManager;
    ArrayList<Bookmark> bookmarkArray;
    ListView            bookmarksListView;
    Button              buttonToBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        dbManager = new DbManager(this);

        // --------------------------------------
        // - Initialize the bookmarksListView
        // --------------------------------------

        bookmarksListView = findViewById(R.id.bookmarks_listview);

        bookmarkArray = dbManager.getBookmarks();

        // -------------------------------------------
        // - Check here if there are saved bookmarks
        // - If none are found, display only a TextView
        // - Displaying "No bookmarks"
        // -------------------------------------------

        if (bookmarkArray.size() == 0)
        {
            // -------------------------------------------------------
            // - Initialize the 'No bookmarks' TextView
            // - Set its visibility on
            // -------------------------------------------------------

            TextView noBookmarksView = (TextView) findViewById(R.id.bookmarks_textview_no_bookmarks);
            noBookmarksView.setVisibility(View.VISIBLE);
        }
        else
        {
            // -------------------------------------------------------
            // - Initialize the Bookmark ListView
            // -------------------------------------------------------

            BookmarkListAdapter adapter = new BookmarkListAdapter(this, bookmarkArray);
            bookmarksListView.setAdapter(adapter);
            bookmarksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    // -----------------------------------------------
                    // - Handle the bookmark row title click
                    // -----------------------------------------------
                    TextView openBookmarkTitle = (TextView) view.findViewById(R.id.bookmark_textview_title);

                    openBookmarkTitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("Open bookmark", "title: " + bookmarkArray.get(position).getName());
                            launchBrowserActivity(bookmarkArray.get(position).getUrl());
                        }
                    });

                    // -----------------------------------------------
                    // - Handle the individual row delete button click
                    // -----------------------------------------------

                    ImageButton deleteBookmark = (ImageButton) view.findViewById(R.id.bookmark_button_delete);

                    deleteBookmark.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            displayDeleteBookmarkDialog(bookmarkArray.get(position).getName());
                            Log.d("Delete Bookmark", "title: " + bookmarkArray.get(position).getName());
                        }
                    });

                    long itemId = parent.getItemIdAtPosition(position);
                    Log.d("BookmarksActivity", "id: " + itemId);
                }
            });

        }

        // --------------------------------------
        // - Initialize buttonToBrowser
        // --------------------------------------

        buttonToBrowser   = findViewById(R.id.bookmarks_button_back);
        buttonToBrowser.setOnClickListener(new View.OnClickListener()
        {
           @Override
           public void onClick(View v)
           {
               launchBrowserActivity("");
           }
       });

    }

    /**
     * Display a popup dialog to ensure the removal of a bookmark
     */
    public void displayDeleteBookmarkDialog(final String title)
    {
        // -------------------------------
        // - Define the Popup Window
        // -------------------------------

        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.bookmark_delete_dialog, null);
        int width  = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        final PopupWindow dialogWindow = new PopupWindow(dialogView, width, height, true);
        dialogWindow.showAtLocation(findViewById(R.id.bookmarks_root), Gravity.CENTER, 0, 0);

        // -------------------------------
        // - Initialize The Popup Window's
        // - Child Views
        // -------------------------------

        TextView bookmarkTitle = dialogView.findViewById(R.id.delete_dialog_title);
        Button cancelButton    = dialogView.findViewById(R.id.delete_dialog_button_cancel);
        Button okButton        = dialogView.findViewById(R.id.delete_dialog_button_ok);

        // ---------------------------
        // - Set the bookmark title
        // - In the Top TextView
        // ---------------------------

        bookmarkTitle.setText(title);

        // ---------------------------
        // - Setup cancel button
        // ---------------------------

        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialogWindow.dismiss();
            }
        });

        // ---------------------------
        // - Setup Ok button
        // ---------------------------

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dbManager.deleteBookmark(title);
                dialogWindow.dismiss();
                recreate();
            }
        });

    }

    /**
     * Launches the BrowserActivity with a bookmark
     * If bookmark if empty, nothing is send, otherwise the bookmark
     * is passed to the Intent as an extra
     *
     * @param bookmarlUrl
     */
    public void launchBrowserActivity(String bookmarkUrl)
    {
        Intent intent = new Intent(this, BrowserActivity.class);

        if (bookmarkUrl.length() > 0)
        {
            intent.putExtra(BOOKMARK_TAG, bookmarkUrl);
        }

        startActivity(intent);
    }
}