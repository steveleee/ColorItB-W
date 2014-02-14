package edu.berkeley.cs160.sangholee.coloritbw;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void rowClick(View v) {
		Intent intent = new Intent(this, ColorActivity.class);
		int imageId;
		switch(v.getId()) {
		    case R.id.parisRow:
		    	imageId = 0;
		    	intent.putExtra("IMAGE_ID", imageId);
		        break;
		}
		startActivity(intent);
	}

	
}