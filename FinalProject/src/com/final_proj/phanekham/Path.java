package com.final_proj.phanekham;

import java.util.ArrayList;
import android.util.Log;
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


/**
 * 
 * @author Derek
 *
 *This class stores a path, which consists of
 *PolylineOptions pathpoints: a set of points on the map that make a path
 *MarkerOptions markerOptions: a point for a marker on the map, the starting location
 *LatLng latLng: the starting position
 *float length: the length of the path
 */
public class Path  {
	
	//private Polyline pathLine;
	private PolylineOptions pathPoints;
	//private Marker marker;
	private MarkerOptions markerOptions;
	private LatLng latLng;
	private float length;
	private String name;
	
	public Path(){
		pathPoints = new PolylineOptions();
	}
	
	public Path(LatLng point){
		pathPoints = new PolylineOptions();
		markerOptions = new MarkerOptions().position(point).title("start");
	}
	
	public Path(PolylineOptions p, MarkerOptions m, float l){
		pathPoints = p;
		markerOptions = m;
		length = l;
		latLng = markerOptions.getPosition();
	}
	
	public void setLength(float f){
		length = f;
	}
	
	public void updateLength(float f){
		length = length + f;
	}
	
	public void setLatLng(float lat, float lng){
		latLng = new LatLng(lat, lng);
	}

	public void setLatLng(LatLng l){
		latLng = l;
	}
	
	public LatLng getLatLng(){
		return latLng;
	}
	
	public void add(LatLng point){
		pathPoints.add(point);
	}
	
	public void setMarkerOptions(LatLng point){
		markerOptions = new MarkerOptions().position(point).title("start");
	}
	
	public MarkerOptions getMarkerOptions(){
		return markerOptions;
	}
	
	public PolylineOptions getPolylineOptions(){
		return pathPoints;
	}
	
	public void setName(String s){
		name = s;
	}
	
	public String getName(){
		return name;
	}
	
	public float getLength(){
		return length;
	}

}
