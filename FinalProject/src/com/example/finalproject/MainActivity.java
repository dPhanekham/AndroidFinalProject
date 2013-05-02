package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private Button bStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        bStart = (Button) findViewById(R.id.start);
        
        bStart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(v.getContext(), mapActivity.class));
			}
		});
        
    }
}