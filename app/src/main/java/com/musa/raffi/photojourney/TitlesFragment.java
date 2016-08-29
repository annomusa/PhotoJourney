package com.musa.raffi.photojourney;

import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Asus on 8/20/2016.
 */

public class TitlesFragment extends ListFragment {
    private Cursor mCursor;
    private ActivityComs mActivityComs;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        String tag = getArguments().getString("Tag");

        DataManager d = new DataManager(getActivity().getApplicationContext());

        if(tag == "_NO_TAG"){
            mCursor = d.getTitles();
        } else {
            mCursor = d.getTitlesWithTag(tag);
        }

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1,
                mCursor,
                new String[] {DataManager.TABLE_ROW_TITLE},
                new int[] {android.R.id.text1} ,
                0
        );
        setListAdapter(cursorAdapter);
    }

    public void onListItemClick(ListView l, View v, int position, long id){
        mCursor.moveToPosition(position);

        int dBID = mCursor.getInt(mCursor.getColumnIndex(DataManager.TABLE_ROW_ID));

        mActivityComs.onTitlesListItemSelected(dBID);
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
