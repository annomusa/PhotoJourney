package com.musa.raffi.photojourney;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Asus on 8/20/2016.
 */

public class CaptureFragment extends Fragment implements LocationListener{
    private static final int CAMERA_REQUEST = 123;
    private ImageView mImageView;
    String mCurrentPhotoPath;
    private Uri mImageUri = Uri.EMPTY;

    private DataManager mDataManager;

    private Location mLocation = new Location("");
    private LocationManager mLocationManager;
    private String mProvider;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mDataManager = new DataManager(getActivity().getApplicationContext());

        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mProvider = mLocationManager.getBestProvider(criteria, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_capture, container, false);

        mImageView = (ImageView) view.findViewById(R.id.imageView);
        Button btnCapture = (Button) view.findViewById(R.id.btnCapture);
        Button btnSave = (Button) view.findViewById(R.id.btnSave);

        final EditText mEditTextTitle = (EditText) view.findViewById(R.id.editTextTitle);
        final EditText mEditTextTag1 = (EditText) view.findViewById(R.id.editTextTag1);
        final EditText mEditTextTag2 = (EditText) view.findViewById(R.id.editTextTag2);
        final EditText mEditTextTag3 = (EditText) view.findViewById(R.id.editTextTag3);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File photoFile = null;
                try{
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.e("error", "error creating file");
                }
                if(photoFile != null){
                    mImageUri = Uri.fromFile(photoFile);
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(intent, CAMERA_REQUEST);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mImageUri != null){
                    if(!mImageUri.equals(Uri.EMPTY)){
                        Photo photo = new Photo();
                        photo.setTitle(mEditTextTitle.getText().toString());
                        photo.setStorageLocation(mImageUri);

                        photo.setGpsLocation(mLocation);

                        String tag1 = mEditTextTag1.getText().toString();
                        String tag2 = mEditTextTag2.getText().toString();
                        String tag3 = mEditTextTag3.getText().toString();

                        photo.setTag1(tag1);
                        photo.setTag2(tag2);
                        photo.setTag3(tag3);

                        Log.d("onClick capturefrag: ", mImageUri.toString());
                        Log.d("onClick capturefrag: ", mEditTextTitle.getText().toString());

                        mDataManager.addPhoto(photo);
//                        Log.d("onClick capturefraf: ", );
                        Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "No image to save", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.e("Error: ", "uri is null");
                }
            }
        });

        return view;
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            Log.d("asdf", "onActivityResult: ");
            try{
                mImageView.setImageURI(Uri.parse(mImageUri.toString()));
            } catch (Exception e){
                Log.e("ERROR", "Uri not set");
            }
        } else {
            mImageUri = Uri.EMPTY;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        BitmapDrawable bd = (BitmapDrawable) mImageView.getDrawable();
        bd.getBitmap().recycle();
        mImageView.setImageBitmap(null);
    }

    @Override
    public void onLocationChanged(Location location){
        mLocation = location;
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){

    }
    @Override
    public void onProviderDisabled(String provider){

    }
    @Override
    public void onProviderEnabled(String provide){

    }

    @Override
    public void onResume(){
        super.onResume();
        mLocationManager.requestLocationUpdates(mProvider, 500, 1, this);
    }

    @Override
    public void onPause(){
        super.onPause();

        mLocationManager.removeUpdates(this);
    }
}
