package edu.temple.abrowser;

import android.app.Activity;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BookmarkListAdapter implements ListAdapter {

    private ArrayList<Bookmark> bookmarkList;
    private final Activity      context;

    /**
     * BookmarkListAdapter constructor
     *
     * @param context
     * @param bookmarkList
     */
    public BookmarkListAdapter(Activity context, ArrayList<Bookmark> bookmarkList)
    {
        this.context      = context;
        this.bookmarkList = bookmarkList;
    }


    @Override
    public void registerDataSetObserver(DataSetObserver observer) { }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) { }

    @Override
    public int getCount()
    {
        return bookmarkList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return bookmarkList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    /**
     * Creates a custom ListView item from layout 'bookmark_row'
     *
     * @param  position
     * @param  rowView
     * @param  parent
     * @return View
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View rowView, @NonNull ViewGroup parent)
    {
        Bookmark bookmark = bookmarkList.get(position);

        if (rowView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.bookmark_row, null);

            TextView title = rowView.findViewById(R.id.bookmark_textview_title);
            title.setText(bookmark.getName());
        }

        return rowView;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @Override
    public int getViewTypeCount()
    {
        return bookmarkList.size();
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled()
    {
        return false;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }
}
