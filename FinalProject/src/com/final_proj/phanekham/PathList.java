package com.final_proj.phanekham;

import java.util.Arrays;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.final_proj.phanekham.R;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


/**
 * 
 * @author Derek
 * This object stores a path (a Polyline) and information about the path
 * such as length, and a marker at its start
 *
 *
 */
public class PathList extends Activity {
	
	private static ObjectContainer db = null;
	private static final String DB4O_NAME = "path_database.db4o";
	private ListView listView;
	public static final int MAP_ACTIVITY_REQUEST_CODE = 1234;
	public static final int LIST_ACTIVITY_REQUEST_CODE = 5678;
	private Path[] p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pathlist_view);
        Intent intent = getIntent();
        Location location = intent.getExtras().getParcelable("location");
        
    	listView = (ListView) findViewById(R.id.list_view);
    	
    	ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),   this.getDir("data", 0) + "/" + DB4O_NAME);
        try {
        	Path t = new Path();
        	ObjectSet list= db.queryByExample(t);
        	Object[] ob = list.toArray();
        	p = Arrays.copyOf(ob, ob.length, Path[].class);
        } finally {
            db.close();
        }

		// First parameter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Forth - the Array of data
		PathAdapter adapter = new PathAdapter(this, p, location);

		// Assign adapter to ListView
		listView.setAdapter(adapter); 
		
		listView.setOnItemClickListener(new OnItemClickListener() {
    	  @Override
    	  public void onItemClick(AdapterView<?> listView, View view,
    		int position, long id) {
    		  	Intent intent = new Intent(listView.getContext(), MapActivity.class);
    		  	intent.putExtra("KnownPath", true);
	    		intent.putExtra("MapPath", p[position].getPolylineOptions());
	    		intent.putExtra("MapMarker", p[position].getMarkerOptions());
				startActivity(intent);
    		  
//    		Toast.makeText(getApplicationContext(),
//    		  "Click ListItem Number " + position, Toast.LENGTH_LONG)
//    		  .show();
    		  
    	  }
    	});
	        
		
		
	}
    
    
        
}
