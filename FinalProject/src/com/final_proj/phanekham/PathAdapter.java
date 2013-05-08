package com.final_proj.phanekham;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
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

public class PathAdapter extends ArrayAdapter<Path> {
	
	private Activity activity;
    private static LayoutInflater inflater=null;
    private String [] c;
    private Context con;
    private boolean[] checked;
    private int count;
    TypedArray images;
    private Path[] paths;

    
	public PathAdapter(Context context, Path[] p){
		super(context, R.layout.path_list_element, R.id.element_title, p);
		//c = context.getResources().getStringArray(R.array.ingredients);
		activity = (Activity) context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        paths = p;

        //images = context.getResources().obtainTypedArray(R.array.icons);
        //creates local db40 database
        
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.path_list_element, null);
        
        //CheckBox box = (CheckBox) vi.findViewById(R.id.checkbox);
 	    //boolean contains = mHash.containsKey(position);
 	    
 	   //if (contains && mHash.get(position)) {
	   //  box.setChecked(true);
	   //} else {
	   //  box.setChecked(false);
	   //}//

 
         TextView title = (TextView) vi.findViewById(R.id.element_title); // title
         TextView distance = (TextView) vi.findViewById(R.id.element_distance); // title
         TextView length = (TextView) vi.findViewById(R.id.element_length); // title
         
         title.setText("Title " + position);
         distance.setText("Distance: " + paths[position].getLatLng().toString());
		
		
        return vi;
    }
	
	public boolean[] returnChecked(){
		return checked;
	}
	
	public int returnCount(){
		return count;
	}

}