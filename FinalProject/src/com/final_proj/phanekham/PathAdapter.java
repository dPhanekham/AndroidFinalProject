package com.final_proj.phanekham;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.google.android.gms.maps.model.LatLng;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 
 * @author Derek
 *
 *Custom adapter for an array of paths. 
 *
 */
public class PathAdapter extends ArrayAdapter<Path> {
	
	private Activity activity;
    private static LayoutInflater inflater=null;
    private String [] c;
    private Context con;
    private boolean[] checked;
    private int count;
    TypedArray images;
    private Path[] paths;
    Location location;

    
	public PathAdapter(Context context, Path[] p, Location curLocation){
		super(context, R.layout.path_list_element, R.id.element_title, p);
		//c = context.getResources().getStringArray(R.array.ingredients);
		activity = (Activity) context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        paths = p;
        location = curLocation;
        //images = context.getResources().obtainTypedArray(R.array.icons);
        //creates local db40 database
        
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.path_list_element, null);
       
         TextView title = (TextView) vi.findViewById(R.id.element_title); // title
         TextView distance = (TextView) vi.findViewById(R.id.element_distance); // title
         TextView length = (TextView) vi.findViewById(R.id.element_length); // title
         
         title.setText(paths[position].getName());
         length.setText("Path Length: " + paths[position].getLength() + " meters");
         
         Location startLocation = new Location(location.getProvider());
         startLocation.setLatitude(paths[position].getLatLng().latitude);
         startLocation.setLongitude(paths[position].getLatLng().longitude);
         
         
         
         distance.setText("Distance from you: " + Math.floor(location.distanceTo(startLocation)) + " meters");
		
		
        return vi;
    }
	
	public boolean[] returnChecked(){
		return checked;
	}
	
	public int returnCount(){
		return count;
	}

}