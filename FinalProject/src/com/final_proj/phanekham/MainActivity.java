package com.final_proj.phanekham;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.final_proj.phanekham.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends Activity {
	
	private Button bStart, listButton;
	private static ObjectContainer db = null;
	private static final String DB4O_NAME = "path_database.db4o";
	public static final int MAP_ACTIVITY_REQUEST_CODE = 1234;
	public static final int LIST_ACTIVITY_REQUEST_CODE = 5678;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        bStart = (Button) findViewById(R.id.start);
        listButton = (Button) findViewById(R.id.list_button);
        
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),   this.getDir("data", 0) + "/" + DB4O_NAME);
        try {
        	Path t = new Path();
            ObjectSet s = db.queryByExample(t);
            Log.d("DSP", "Size of database : " + s.size());
        } 	
        finally {
        
            db.close();
        }

        
        
        bStart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), MapActivity.class);
				startActivityForResult(intent, MAP_ACTIVITY_REQUEST_CODE);
			}
		});
        
        listButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), PathList.class);
				startActivityForResult(intent, LIST_ACTIVITY_REQUEST_CODE);
			}
		});
        
    }
    
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode == RESULT_OK && requestCode == MAP_ACTIVITY_REQUEST_CODE){
			if(data.getBooleanExtra("MapSaved", false) == true){
				Path p;
				PolylineOptions po = data.getExtras().getParcelable("MapPath");
				MarkerOptions mo = data.getExtras().getParcelable("MapMarker");
				float fo = data.getFloatExtra("length", 0);
				
				p = new Path(po, mo, fo);
				
				//creates local db40 database
		        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),   this.getDir("data", 0) + "/" + DB4O_NAME);
		        try {
		            db.store(p);
		        } 	
		        finally {
		        
		            db.close();
		        }
			
				Log.d("DSP", "lol");
				//text.setText(combined);
			}
		}
		
		else if(resultCode == RESULT_OK && requestCode == LIST_ACTIVITY_REQUEST_CODE){
			/*(if(data.hasExtra("returnkey1")){
				//ch =  data.getBooleanArrayExtra("returnkey1");
				String combined = "";
				for(int i = 0; i < 15; i++){
					if(ch[i]){
						Log.d("DSP", "TRUE");
						combined += toppings[i] + "\n";
					}
				}
				Log.d("DSP", combined);
				//text.setText(combined);
			}*/
		}
	}
}