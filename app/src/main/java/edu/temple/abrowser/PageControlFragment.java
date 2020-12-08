package edu.temple.abrowser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

public class PageControlFragment extends Fragment{ // implements PageViewerFragment.PageViewerInterface {

    private ImageButton goButton,
                        backButton,
                        forwardButton;
    private TextView    urlTextView;

    private PageControlInterface browserActivity;

    public PageControlFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof PageControlInterface) {
            browserActivity = (PageControlInterface) context;
        } else {
            throw new RuntimeException("You must implement the required interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View l = inflater.inflate(R.layout.fragment_page_control, container, false);

        urlTextView = l.findViewById(R.id.urlTextView);

        // -----------------------------------------------------
        // - Setup a focus change listener for the UrlTextView
        // - In order to hide the soft keyboard when UrlTextView
        // - Loses focus
        // -----------------------------------------------------

        urlTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {

                if (v.getId() == R.id.urlTextView && ! hasFocus)
                {
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

            }
        });

        goButton = l.findViewById(R.id.goButton);
        backButton = l.findViewById(R.id.backButton);
        forwardButton = l.findViewById(R.id.forwardButton);

        View.OnClickListener controlOcl = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (v.equals(goButton)) {
                    browserActivity.go(formatUrl(urlTextView.getText().toString()));
                }
                else if (v.equals(backButton))
                    browserActivity.back();
                else if (v.equals(forwardButton))
                    browserActivity.forward();
            }
        };

        goButton.setOnClickListener(controlOcl);
        backButton.setOnClickListener(controlOcl);
        forwardButton.setOnClickListener(controlOcl);

        return l;
    }



    /**
     * Here the parent Activity's intent is checked in order to extract a possible
     * Bookmark sent from BookmarksActivity
     */
    @Override
    public void onResume()
    {
        super.onResume();

        // -------------------------------------------------------
        // - Check if we'll get a URL from some of the intents
        // -------------------------------------------------------
        String urlString = "";
        Intent intent    = getActivity().getIntent();

        if ( intent.getAction() == Intent.ACTION_VIEW ) {

            // ---------------------------------------------------
            // - Category check, should be BROWSABLE
            // ---------------------------------------------------

            urlString = intent.getDataString();

        } else {

            String bookmark = intent.getStringExtra(BookmarksActivity.BOOKMARK_TAG);

            if ( bookmark != null ) urlString = bookmark; //browserActivity.go(bookmarkUrl);

        }

        if ( urlString.length() > 0 ) browserActivity.go( urlString );

    }

    public void updateUrl(String url) {
        urlTextView.setText(url);
    }

    public String getUrl()
    {
        return formatUrl(urlTextView.getText().toString());
    }

    /**
     * Format URL to ensure a protocol is specified
     * @param url that is checked for protocol
     * @return url with protocol prefixed if not found
     */
    private String formatUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return "http://" + url;
        } else {
            return url;
        }
    }

    interface PageControlInterface {
        void go(String url);
        void back();
        void forward();
    }
}