package edu.temple.abrowser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.ArrayList;

public class PageViewerFragment extends Fragment {

    public  WebView                            webView;
    private PagerFragment.PagerInterface       browserActivity;
    private PageListFragment.PageListInterface browserActivity_list;

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_POSITION = "position";
    private static final String ARG_URL = "url";

    WebBackForwardList mWebBackForwardList;

    // TODO: Rename and change types and number of parameters
    public static PageViewerFragment newInstance(int position, String url) {
        PageViewerFragment fragment = new PageViewerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    // Save reference to parent
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof PagerFragment.PagerInterface) {
            browserActivity = (PagerFragment.PagerInterface) context;
        } else {
            throw new RuntimeException("You must implement the required interface");
        }

        if (context instanceof PageListFragment.PageListInterface) {
            browserActivity_list = (PageListFragment.PageListInterface) context;
        } else {
            throw new RuntimeException("You must implement the required interface");
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(

            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState

    ){

        View l = inflater.inflate(R.layout.fragment_page_viewer, container, false);

        webView = (WebView) l.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        if(getArguments().getString(ARG_URL) != null) {
            webView.loadUrl(getArguments().getString(ARG_URL));
        }

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!utils.changeSave) {

                    mWebBackForwardList = view.copyBackForwardList();
                    for (int i = 0; i < mWebBackForwardList.getSize(); i++) {
                        if (!mWebBackForwardList.getItemAtIndex(i).getUrl().equals("about:blank")) {
                            utils.pbBackForwardList.add(mWebBackForwardList.getItemAtIndex(i).getUrl());
                        }
                    }
                    utils.pbBackForwardList = removeDuplicates(utils.pbBackForwardList);
                    utils.pBackForwardCount = utils.pbBackForwardList.size();

                    browserActivity_list.displayListItem(utils.pbBackForwardList);
                }
                utils.changeSave = false;
            }
        });

        // Restore WebView session
        if (savedInstanceState != null)
            webView.restoreState(savedInstanceState);

        return l;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Store URL and previous/back in case fragment is restarted
        webView.saveState(outState);
    }

    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {
        ArrayList<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (T element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }

}