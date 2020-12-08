package edu.temple.abrowser;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PageListFragment extends ListFragment {

    private PageListFragment.PageListInterface browserActivity;

     public PageListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof PageListFragment.PageListInterface) {
            browserActivity = (PageListFragment.PageListInterface) context;
        } else {
            throw new RuntimeException("You must implement the required interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_page_list, container, false);
        return view;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        if(utils.pbBackForwardList.size() != 0) {
            browserActivity.goToListItem(position, utils.pbBackForwardList.get(position));
        }
    }

    public void displayListItem(ArrayList<String> itemArrayString){
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_activated_1, itemArrayString);
        setListAdapter(adapter);
    }

    interface PageListInterface {
        void goToListItem(int position, String itemString);
        void displayListItem(ArrayList<String> itemArrayString);
    }
}