package edu.temple.abrowser;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class BrowserControlFragment extends Fragment {

    DbManager   dbManager;
    ImageButton newPage;
    ImageButton saveBookmarkButton;
    ImageButton launchBookmarksButton;

    private BrowserControlFragment.BrowserControlInterface browserActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof BrowserControlFragment.BrowserControlInterface) {
            browserActivity = (BrowserControlFragment.BrowserControlInterface) context;
        } else {
            throw new RuntimeException("You must implement the required interface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View browserControl = inflater.inflate(R.layout.fragment_browser_control, container, false);

        // --------------------------------------
        // - Create a DbManager instance for
        // - bookmark storage
        // --------------------------------------

        dbManager = new DbManager(getActivity());

        // --------------------------------------
        // - Instantiate the ImageButtons
        // --------------------------------------

        newPage               = (ImageButton) browserControl.findViewById(R.id.newpage);
        saveBookmarkButton    = (ImageButton) browserControl.findViewById(R.id.browser_control_button_save_bookmark);
        launchBookmarksButton = (ImageButton) browserControl.findViewById(R.id.browser_control_button_display_bookmarks);

        View.OnClickListener controlBrowser = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (v.equals(newPage)) {
                    browserActivity.newPage();
                }

            }
        };

        newPage.setOnClickListener(controlBrowser);

        // -----------------------------------
        // - Save bookmark on click
        // -----------------------------------

        saveBookmarkButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String url = browserActivity.getUrl();

                if ( ! url.equals("http://"))
                {
                    saveBookmark(url);
                }

            }
        });

        // -----------------------------------
        // - Launch BookmarksActivity on click
        // -----------------------------------

        launchBookmarksButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(), BookmarksActivity.class));
            }
        });

        return browserControl;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Attempt to insert a bookmark to the database and
     * notify the user with a Toast as a result
     * @param title
     */
    public void saveBookmark(String title)
    {
        if (title.length() > 0)
        {
            if (dbManager.insertBookmark(title, title) > 0)
            {
                Toast.makeText(getActivity(),title + " added to bookmarks", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getActivity(),title + " is already saved", Toast.LENGTH_LONG).show();
            }
        }
    }

    interface BrowserControlInterface {
        void newPage();
        String getUrl();
    }
}