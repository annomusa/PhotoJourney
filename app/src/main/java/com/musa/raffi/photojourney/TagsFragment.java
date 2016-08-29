package com.musa.raffi.photojourney;

import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Asus on 8/20/2016.
 */

public class TagsFragment extends ListFragment {
    private ActivityComs mActivityComs;

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        DataManager d = new DataManager(getActivity().getApplicationContext());
        Cursor c = d.getTags();

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_list_item_1,
                c,
                new String[] {DataManager.TABLE_ROW_TAG},
                new int[] {android.R.id.text1},
                0
        );

        setListAdapter(cursorAdapter);

    }

    public void onListItemClick(ListView l, View v, int position, long id){
        Cursor c = ((SimpleCursorAdapter)l.getAdapter()).getCursor();
        c.moveToPosition(position);
        String clickedTag = c.getString(1);
        Log.d("Clicked Tag: ", clickedTag);

        mActivityComs.onTagsListItemSelected(clickedTag);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mActivityComs = (ActivityComs) context;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mActivityComs = null;
    }
}
