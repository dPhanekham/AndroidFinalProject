package com.final_proj.phanekham;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.final_proj.phanekham.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.support.v4.app.FragmentActivity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;


public class MapActivity extends FragmentActivity implements  OnMyLocationChangeListener {
	
	/**
     * Note that this may be null if the Google Play services APK is not available.
     */
    private GoogleMap mMap;
    private MarkerOptions startPointMarker;
    private PolylineOptions pathPoints;
    private Polyline path;
    private LatLng startPointCoord;
    private float length;
    private boolean newPath;
    private boolean pause;
    //private Path pathPoints;
    private ArrayList<Location> locationList;
    private Time beginningTime;
    private long lastTimeLong;
    private long secondsPassed;
    
    private PolylineOptions knownPathPoints;
    private MarkerOptions knownMarker;
    private ImageButton pauseButton, stopButton;
    private TextView status;
    private EditText input;
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.map_activity);
        locationList = new ArrayList<Location>();
        pathPoints = new PolylineOptions();
        length = 0;
        newPath = true;
        pause = false;
        Intent intent = getIntent();
        if(intent.getBooleanExtra("KnownPath", false) == true){
	        knownPathPoints = intent.getExtras().getParcelable("MapPath");
			knownMarker = intent.getExtras().getParcelable("MapMarker");
			newPath = false;
        }
        secondsPassed = 0;
        beginningTime = new Time(Time.getCurrentTimezone());
        beginningTime.setToNow();
        lastTimeLong = System.currentTimeMillis();
        
        pauseButton = (ImageButton) findViewById(R.id.pause_start);
        stopButton = (ImageButton) findViewById(R.id.stop);
        status = (TextView) findViewById(R.id.status_text);
        
        
        setUpMapIfNeeded();
        
        
        //PAUSE BUTTON LOGIC
        pauseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(pause){
					pauseButton.setImageDrawable(v.getResources().getDrawable(R.drawable.pause));
					long temp = System.currentTimeMillis() - lastTimeLong;
					lastTimeLong = temp + lastTimeLong;
					secondsPassed = secondsPassed + temp/1000;
					
					pause = false;
				}
				else{
					pauseButton.setImageDrawable(v.getResources().getDrawable(R.drawable.resume));
					lastTimeLong = System.currentTimeMillis();
					
					pause = true;
				}
			}
		});
        
        	stopButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//status.getLayoutParams().height = 200;
			}
		});
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not have been
     * completely destroyed during this process (it is likely that it would only be stopped or
     * paused), {@link #onCreate(Bundle)} may not be called again so we should call this method in
     * {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
        	mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        	
        	
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
    	mMap.setMyLocationEnabled(true);
    	mMap.setOnMyLocationChangeListener(this);
    	mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    	//mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        //Location current = mMap.getMyLocation();
        //Log.d("DSP", current.toString());
    	if(!newPath){
    		knownPathPoints.color(0xff0ff000);
    		mMap.addMarker(knownMarker);
    		mMap.addPolyline(knownPathPoints);
    	}
        
        
    }

	@Override
	public void onMyLocationChange(Location location) {
		
		//update elapsed time
		long temp = System.currentTimeMillis() - lastTimeLong;
		lastTimeLong = temp + lastTimeLong;
		secondsPassed = secondsPassed + temp/1000;
		
		if(!pause){
			//if initial point
			if(locationList.isEmpty()){
				status.setText("Waiting for precise location");
				Log.d("DSP", "Waiting for precise Location");
				if(location.getAccuracy() < 30)
				{
					length = 0;
					Log.d("DSP", "Precise location added");
					status.setText("GPS location found");
					locationList.add(location);
					
					LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
					pathPoints.add(point);
					
					if(startPointMarker == null && newPath == true){
						startPointMarker = new MarkerOptions().position(point).title("Start");
						mMap.addMarker(new MarkerOptions().position(point).title("Start"));
						startPointCoord = point;
					}
					
					//sets camera to current location
					//change default zoom here
					CameraUpdate myLoc = CameraUpdateFactory.newCameraPosition(
			            new CameraPosition.Builder().target(new LatLng(location.getLatitude(),
			                    location.getLongitude())).zoom(13.8f).build());
					mMap.moveCamera(myLoc);
				}
				
				CameraUpdate myLoc = CameraUpdateFactory.newCameraPosition(
			            new CameraPosition.Builder().target(new LatLng(location.getLatitude(),
			                    location.getLongitude())).zoom(13.8f).build());
					mMap.moveCamera(myLoc);
				
			    	
			}
			
			//if this location is at least 10 meters from previous, add new point
			else{
				
				if(location.getAccuracy() < 40)
				{
					status.setText("Tracking your path!");
					int size = locationList.size();
					Log.d("DSP", "Size: " + size);
					float d = location.distanceTo(locationList.get(size-1));
					
					if( d > 20 ){  ///TODO change these parameters
						length = length + d;
						locationList.add(location);
						LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
						pathPoints.add(point);
						path = mMap.addPolyline(pathPoints);
					}
					else{
						Log.d("DSP", "NOT LONG ENOUGH");
					}
					
				}
				else{
					status.setText("GPS location lost");
				}
				
				
			}
			
			
		} //end if(pause)
		
	    
	} //end onMyLocationChange
	
	@Override
	public void onPause() {
	    super.onPause();  // Always call the superclass method first
	    finish();

	}
	
	@Override
	public void onStop() {
	    super.onStop();  // Always call the superclass method first
	    finish();

	}
	
	public void onBackPressed() {
	    super.onBackPressed();
	}
	
	
	//DIALOG BOX FOR BACK KEY PRESSED
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // TODO Auto-generated method stub

	    switch(keyCode)
	    {
	    case KeyEvent.KEYCODE_BACK:
	    	if(newPath){
	    		AlertDialog.Builder ab = new AlertDialog.Builder(MapActivity.this);
	    		input = new EditText(this);
	    		input.setText("Untitled Route");
	    		input.setSelectAllOnFocus(true);
	    		ab.setView(input);
		        ab.setMessage("Do you want to save your route?").setPositiveButton("Yes", dialogClickListener)
		        .setNegativeButton("No", dialogClickListener).show();
	    	}
	        
	        break;
	    }

	    return super.onKeyDown(keyCode, event);
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	        switch (which){
	        case DialogInterface.BUTTON_POSITIVE:
	        	Intent data;
	        	if(newPath){
	        		Editable inputText  = input.getText();
	        		
	        		String inputString = "lol";
	        		inputString = inputText.toString();
	        		data = new Intent();
		    		Log.d("DSP", "on back pressed");
		    		Log.d("DSP", inputString);
		    		data.putExtra("MapSaved", true);
		    		data.putExtra("MapPath", pathPoints);
		    		data.putExtra("MapMarker", startPointMarker);
		    		data.putExtra("length", length);
		    		data.putExtra("Title", inputString);
		    		data.putExtra("TimeElapsed", secondsPassed);
		    		setResult(RESULT_OK, data);
	        	}
	        	
	    		onBackPressed();
	    		finish();
	            break;

	        case DialogInterface.BUTTON_NEGATIVE:
	            onBackPressed();
	            break;
	        }
	    }
	};
}
