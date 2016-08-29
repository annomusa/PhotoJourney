package com.musa.raffi.photojourney;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by Asus on 8/22/2016.
 */

public class ViewFragment extends Fragment {
    private Cursor mCursor;
    private ImageView mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        int position = getArguments().getInt("Position");

        DataManager d = new DataManager(getActivity().getApplicationContext());

        mCursor = d.getPhoto(position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState){
        View view = inflater.inflate(R.layout.fragment_view, container, false);

        TextView textView = (TextView) view.findViewById(R.id.textView);
        Button button = (Button) view.findViewById(R.id.buttonShowLocation);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        textView.setText(mCursor.getString(mCursor.getColumnIndex(DataManager.TABLE_ROW_TITLE)));
        imageView.setImageURI(Uri.parse(mCursor.getString(mCursor.getColumnIndex(DataManager.TABLE_ROW_URI))));
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                double latitude = Double.valueOf(mCursor.getString(mCursor.getColumnIndex(DataManager.TABLE_ROW_LOCATION_LAT)));
                double longitude = Double.valueOf(mCursor.getString(mCursor.getColumnIndex(DataManager.TABLE_ROW_LOCATION_LONG)));

                String uri = String.format(Locale.ENGLISH, "geo:%f,%f",latitude,longitude);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getActivity().startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        BitmapDrawable bd = (BitmapDrawable) mImageView.getDrawable();
        bd.getBitmap().recycle();
        mImageView.setImageBitmap(null);
    }
}
