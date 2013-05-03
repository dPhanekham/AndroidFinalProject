package com.final_proj.phanekham;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

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


public class mapActivity extends FragmentActivity implements  OnMyLocationChangeListener {
	
	/**
     * Note that this may be null if the Google Play services APK is not available.
     */
    private GoogleMap mMap;
    
    private PolylineOptions pathPoints;
    private Polyline path;
    private ArrayList<Location> locationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        locationList = new ArrayList<Location>();
        pathPoints = new PolylineOptions();
        
        setUpMapIfNeeded();
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
    	//mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        //Location current = mMap.getMyLocation();
        //Log.d("DSP", current.toString());
        
        
    }

	@Override
	public void onMyLocationChange(Location location) {
		
		//if initial point
		if(locationList.isEmpty()){
			//TODO set marker here
			
			locationList.add(location);
			LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
			pathPoints.add(point);
			//sets camera to current location
		    //change default zoom here
			CameraUpdate myLoc = CameraUpdateFactory.newCameraPosition(
		            new CameraPosition.Builder().target(new LatLng(location.getLatitude(),
		                    location.getLongitude())).zoom(13.8f).build());
		    mMap.moveCamera(myLoc);
			
		}
		
		//if this location is at least 10 meters from previous, add new point
		else{
			int size = locationList.size();
			if(locationList.get(size-1).distanceTo(locationList.get(size-2)) > 10){
				locationList.add(location);
				LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
				pathPoints.add(point);
				path = mMap.addPolyline(pathPoints);
			}
			else{
				Log.d("DSP", "NOT LONG ENOUGH");
			}
			
		}
		
		
		
	    
	    
	}
}
