package edu.berkeley.cs160.sangholee.coloritbw;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.LinearLayout;

public class ColorActivity extends Activity {

	private BitmapFactory bmpFactory;
	private Bitmap bmp;
	private Options importOptions;
	private int imageId;
	private Canvas canvas;
	DrawingView view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_color);
		Intent intent = getIntent();
		imageId = intent.getIntExtra("IMAGE_ID", 0);
		
		//
		view = new DrawingView(this, null);
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.layout);
		linearLayout.addView(view);
		
		//view = (DrawingView) findViewById(R.id.drawingView);
		//view.setBackgroundColor(Color.GREEN);
		sendImageToView();
		//setContentView(view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.color, menu);
		return true;
	}
	
	public void sendImageToView() {
		view.setImage(imageId);
	}
	
	public void createBitmapFromFile() {
		switch(imageId) {
			case 0:
				bmp = BitmapFactory.decodeResource(getResources(), R.drawable.paris, importOptions);
				Log.v("test", bmp.toString());
				canvas = new Canvas(bmp.copy(Bitmap.Config.ARGB_8888, true));
			}
		}
}
