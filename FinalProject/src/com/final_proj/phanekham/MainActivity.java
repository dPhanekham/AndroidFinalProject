package com.final_proj.phanekham;

import java.util.Arrays;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.final_proj.phanekham.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends Activity {
	
	private Button startMapButton, listButton;
	private static ObjectContainer db = null;
	private static final String DB4O_NAME = "path_database.db4o";
	public static final int MAP_ACTIVITY_REQUEST_CODE = 1234;
	public static final int LIST_ACTIVITY_REQUEST_CODE = 5678;
	private long timeElapsed;
	private String title;
	private float length;
	private TextView miles, pathName;
	private Chronometer chrono;
	private LocationManager locationManager;
	private Location location;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        startMapButton = (Button) findViewById(R.id.start);
        listButton = (Button) findViewById(R.id.list_button);
        miles = (TextView) findViewById(R.id.miles);
        chrono = (Chronometer) findViewById(R.id.chrono);
        pathName = (TextView) findViewById(R.id.path_name);
        
        
        //This is too get a coarse location for distance calculations
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        
        String provider = locationManager.getBestProvider(criteria, true);
        location = locationManager.getLastKnownLocation(provider);
        
        //debug code, removes all items f
        /*
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),   this.getDir("data", 0) + "/" + DB4O_NAME);
        try {
        	Path t = new Path();
            ObjectSet s = db.queryByExample(t);
            Log.d("DSP", "Size of database : " + s.size());
            
           
            Path [] p;
            Object[] ob = s.toArray();
        	p = Arrays.copyOf(ob, ob.length, Path[].class);
        	int l = p.length;
            for(int i = 0; i < l; i++){
            	db.delete(p[i]);
            }
           
        } 	
        finally {
        
            db.close();
        }
        */
        
        /**
         * click listener for starting a new path
         */
        startMapButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), MapActivity.class);
				startActivityForResult(intent, MAP_ACTIVITY_REQUEST_CODE);
			}
		});
        
        
        /**
         * button to look for nearby paths
         */
        listButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), PathList.class);
				intent.putExtra("location", location);
				startActivityForResult(intent, LIST_ACTIVITY_REQUEST_CODE);
			}
		});
        
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // TODO Auto-generated method stub

	    switch(keyCode)
	    {
	    case KeyEvent.KEYCODE_MENU:
	        Toast.makeText(getApplicationContext(),
	    			   "Developer: Derek Phanekham" , Toast.LENGTH_LONG)
	    			      .show();
	    	
	        break;
	    }

	    return super.onKeyDown(keyCode, event);
	}
    
    /**
     * for the new path returned from MapActivity
     * displays information in the UI
     * stores the path in the local database
     */
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode == RESULT_OK && requestCode == MAP_ACTIVITY_REQUEST_CODE){
			if(data.getBooleanExtra("MapSaved", false) == true){
				Path p;
				PolylineOptions po = data.getExtras().getParcelable("MapPath");
				MarkerOptions mo = data.getExtras().getParcelable("MapMarker");
				length = data.getFloatExtra("length", 0);
				timeElapsed = data.getLongExtra("TimeElapsed", 0);
				title = data.getStringExtra("Title");
				
				pathName.setText(title);
				
				Log.d("DSP", length + ":" + timeElapsed + ":" + title);
				
				Integer lengthRounded = (int) length;
				String lengthText = lengthRounded.toString();
				miles.setText(lengthText + " meters");
				
				long hours   = timeElapsed/3600;
				long minutes = timeElapsed/60%60;
				long seconds = timeElapsed%60;
				String chronoText = "";
				if(hours < 10){
					chronoText = chronoText + "0" + hours + ":";
				}
				else{
					chronoText = chronoText + hours + ":";
				}
				if(minutes < 10){
					chronoText = chronoText + "0" + minutes + ":";
				}
				else{
					chronoText = chronoText + minutes + ":";
				}
				if(seconds < 10){
					chronoText = chronoText + "0" + seconds;
				}
				else{
					chronoText = chronoText + seconds;
				}
				
				chrono.setText(chronoText);
				
				
				p = new Path(po, mo, length);
				p.setName(title);
				
				//creates local db40 database
		        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),   this.getDir("data", 0) + "/" + DB4O_NAME);
		        try {
		            db.store(p);
		        } 	
		        finally {
		        
		            db.close();
		        }
			
				Log.d("DSP", "Path recieved and stored");
				//text.setText(combined);
			}
		}
		
		else if(resultCode == RESULT_OK && requestCode == LIST_ACTIVITY_REQUEST_CODE){
			//I dont need anything here
		}
	}
}