package edu.temple.abrowser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class BrowserActivity extends AppCompatActivity implements PageControlFragment.PageControlInterface, PagerFragment.PagerInterface,
        BrowserControlFragment.BrowserControlInterface, PageListFragment.PageListInterface {

    FragmentManager        fm;

    BrowserControlFragment browserControlFragment;
    PageControlFragment    pageControlFragment;
    PagerFragment          pagerFragment;
    PageListFragment       pageListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ---------------------------------------------------------
        // - Grab the Toolbar from the activityt_main layout
        // - And set it as BrowserActivity's ActionBar
        // ---------------------------------------------------------

        Toolbar toolbar = (Toolbar) findViewById(R.id.browser_activity_toolbar);
        setSupportActionBar(toolbar);

        // ---------------------------------------------------------
        // - Initialize the fragment manager and create a temporary
        // - fragment
        // ---------------------------------------------------------

        fm = getSupportFragmentManager();
        Fragment tmpFragment;

        // If fragment already added (activity restarted) then hold reference
        // otherwise add new fragment. Only one instance of fragment is ever present
        if ((tmpFragment = fm.findFragmentById(R.id.page_control)) instanceof PageControlFragment)
            pageControlFragment = (PageControlFragment) tmpFragment;
        else {
            pageControlFragment = new PageControlFragment();
            fm.beginTransaction()
                    .add(R.id.page_control, pageControlFragment)
                    .commit();
        }

        // If fragment already added (activity restarted) then hold reference
        // otherwise add new fragment. Only one instance of fragment is ever present
        if ((tmpFragment = fm.findFragmentById(R.id.browser_control)) instanceof BrowserControlFragment)
            browserControlFragment = (BrowserControlFragment) tmpFragment;
        else {
            browserControlFragment = new BrowserControlFragment();
            fm.beginTransaction()
                    .add(R.id.browser_control, browserControlFragment)
                    .commit();
        }

        // If fragment already added (activity restarted) then hold reference
        // otherwise add new fragment. Only one instance of fragment is ever present
        if ((tmpFragment = fm.findFragmentById(R.id.page_list)) instanceof PageListFragment)
            pageListFragment = (PageListFragment) tmpFragment;
        else {
            pageListFragment = new PageListFragment();
            fm.beginTransaction()
                    .add(R.id.page_list, pageListFragment)
                    .commit();
        }

        // If fragment already added (activity restarted) then hold reference
        // otherwise add new fragment. Only one instance of fragment is ever present
        if ((tmpFragment = fm.findFragmentById(R.id.pager)) instanceof PagerFragment)
            pagerFragment = (PagerFragment) tmpFragment;
        else {
            pagerFragment = new PagerFragment();
            fm.beginTransaction()
                    .add(R.id.pager, pagerFragment)
                    .commit();
        }

    }

    @Override
    public void onResume() {

        super.onResume();
        // for the system's orientation sensor registered listeners
    }

    @Override
    public void onPause() {
        super.onPause();
        // to stop the listener and save battery
    }

    /**
     * Overridden callback, called when Activity creates its options menus
     *
     * @param  menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);

        return true;
    }

    /**
     * Overridden callback, called when an Action Bar item is touched
     * (There's only one, share action, thus far)
     *
     * @param  item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_share:
                shareUrl();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    /**
     * Update WebPage whenever PageControlFragment sends a new Url
     * @param url to load
     */
    @Override
    public void go(String url) {
        pagerFragment.go(url);
    }

    /**
     * Go back to previous page when user presses Back in PageControlFragment
     */
    @Override
    public void back() {
        pagerFragment.back();
    }

    /**
     * Go back to next page when user presses Forward in PageControlFragment
     */
    @Override
    public void forward() {
        pagerFragment.forward();
    }

    /**
     * Update displayed Url in PageControlFragment when Webpage Url changes
     * @param url to display
     */
    @Override
    public void updateUrl(String url) {
        pageControlFragment.updateUrl(url);
    }

    @Override
    public void newPage() {
        pagerFragment.newPage();
    }

    @Override
    public void goToListItem(int position, String itemString) {
        pagerFragment.goToListItem(position, itemString);
    }

    @Override
    public void displayListItem(ArrayList<String> itemArrayString) {
        pageListFragment.displayListItem(itemArrayString);
    }

    /**
     * Mediates the url from PageControlFragment
     * @return url
     */
    public String getUrl() { return pageControlFragment.getUrl(); }


    /**
     * Shares a url of a webpage with other applications
     */
    public void shareUrl()
    {
        String url = getUrl();

        if ( url.length() > 0 ) {

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, url);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share URL!"));

        } else Toast.makeText(this, "You cannot send an empty URL", Toast.LENGTH_LONG).show();
    }


}