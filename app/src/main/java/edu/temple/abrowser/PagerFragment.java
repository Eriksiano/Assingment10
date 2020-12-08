package edu.temple.abrowser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import static edu.temple.abrowser.PageViewerFragment.removeDuplicates;

public class PagerFragment extends Fragment {

     /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter;
    private PagerFragment.PagerInterface browserActivity;
    PageViewerFragment pageViewerFragment;

    ArrayList<String> mWebBackForwardList  = new ArrayList<String>();

    public PagerFragment() {
        // Required empty public constructor
        pageViewerFragment = new PageViewerFragment();
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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pager, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) view.findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager(), mWebBackForwardList);
        mPager.setAdapter(pagerAdapter);
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<String> pWebBackForwardList = new ArrayList<String>();

        public ScreenSlidePagerAdapter(FragmentManager fm, ArrayList<String> mWebBackForwardList) {
            super(fm);
            this.pWebBackForwardList = mWebBackForwardList;
        }

        @Override
        public Fragment getItem(int position) {

            if(this.pWebBackForwardList.size() == 0) {
                return pageViewerFragment.newInstance(position, "");
            }
            else {
                return pageViewerFragment.newInstance(position, this.pWebBackForwardList.get(position));
            }
        }

        @Override
        public int getCount() {
            this.pWebBackForwardList = removeDuplicates(this.pWebBackForwardList);
            return (pWebBackForwardList == null || pWebBackForwardList.size() == 0) ? 1 : pWebBackForwardList.size();
        }

        public void notifyDataSetChanged() {
            this.pWebBackForwardList = utils.pbBackForwardList;
            super.notifyDataSetChanged();
        }
    }

    /**
     * Load provided URL in webview
     * @param url to load
     */
    public void go (String url) {
        mWebBackForwardList = new ArrayList<String>();
        utils.pbBackForwardList = new ArrayList<>();
        mWebBackForwardList.add(url);

        utils.changeSave = true;
        pagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager(), mWebBackForwardList);
        mPager.setAdapter(pagerAdapter);
        mPager.setCurrentItem(0);
        browserActivity.updateUrl(url);
    }

    /**
     * Go to previous page
     */
    public void back () {
        utils.changeSave = true;
        if (mPager.getCurrentItem() == 0) {
        } else {
            int currentIndex = mPager.getCurrentItem();
            pagerAdapter.notifyDataSetChanged();
            browserActivity.updateUrl(utils.pbBackForwardList.get(currentIndex-1));
            mPager.setCurrentItem(currentIndex - 1);
        }
    }

    /**
     * Go to next page
     */
    public void forward () {
        utils.changeSave = true;
        if ( mPager.getCurrentItem() == utils.pBackForwardCount -1) {
        }
        else if(mPager.getCurrentItem() >= 0 ) {
            int currentIndex = mPager.getCurrentItem();
            pagerAdapter.notifyDataSetChanged();
            browserActivity.updateUrl(utils.pbBackForwardList.get(currentIndex+1));
            mPager.setCurrentItem(currentIndex + 1);
        }
    }

    /**
     * create new page
     */
    public void newPage() {
        utils.pbBackForwardList.add("");
        pagerAdapter.notifyDataSetChanged();
    }

    public void goToListItem(int postion, String itemString) {
        utils.changeSave = true;
        pagerAdapter.notifyDataSetChanged();

        mPager.setCurrentItem(postion);
        browserActivity.updateUrl(itemString);
    }

    interface PagerInterface {
        void updateUrl(String url);
    }
}