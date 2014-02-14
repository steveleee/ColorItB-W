package edu.berkeley.cs160.sangholee.coloritbw;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {
	Canvas mainCanvas;
	Canvas shadowCanvas;
	Bitmap colorBitmap;
	Bitmap greyscaleBitmap;
	Bitmap mainBitmap;
	Bitmap shadowBitmap;
	Paint paint;
	ArrayList<Point> points;
	Path drawPath;

	int colorPixels[];
	int greyscalePixels[];
	
	int imageId;
	static int width = 700;
	static int height = 700;
	BitmapFactory bmpFactory;
	Options importOptions;
	Bitmap temp;
	boolean colorToGray;
	
	public DrawingView(Context context, AttributeSet attr) {
		super(context, attr);
		
		// Make BitmapFactory and import options
		bmpFactory = new BitmapFactory();
		importOptions = new BitmapFactory.Options();
		importOptions.outWidth = width;
		importOptions.outHeight = height;
		importOptions.inMutable = true;
		
		// Create other bitmaps
		createBitmapArrays();
		
		// Create Paint object
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(50);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);

		// Create list of points for drawing
		points = new ArrayList<Point>();
		drawPath = new Path();
		
		// Color to greyscale switch
		colorToGray = true;
		
		greyscalePixels = new int[width * height];
		greyscaleBitmap.getPixels(greyscalePixels, 0, greyscaleBitmap.getWidth(), 0, 0,
				greyscaleBitmap.getWidth(), greyscaleBitmap.getHeight());
		colorPixels = new int[width * height];
		colorBitmap.getPixels(colorPixels, 0, colorBitmap.getWidth(), 0, 0,
				colorBitmap.getWidth(), colorBitmap.getHeight());
		
	}

	public void setImage(int imageId) {
		this.imageId = imageId;
	}
	
	public void setStrokeWidth(int width) {
		paint.setStrokeWidth(width);
	}

	public void createBitmapArrays() {
		switch(imageId) {
		case 0:
			temp = BitmapFactory.decodeResource(getResources(), R.drawable.paris, importOptions);
			break;

		}
		colorBitmap = temp.copy(Bitmap.Config.ARGB_8888, true);
		mainBitmap = temp.copy(Bitmap.Config.ARGB_8888, true);
		width = colorBitmap.getWidth();
		height = colorBitmap.getHeight();
		//Log.v("pixelTest", Integer.toString(temp.getPixel(504, 203)));
		greyscaleBitmap = toGrayscale(temp);
		mainCanvas = new Canvas(colorBitmap);
		temp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		shadowBitmap = temp.copy(Bitmap.Config.ARGB_8888, true);
		shadowCanvas = new Canvas();
		shadowCanvas.setBitmap(shadowBitmap);
		
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float touchX = e.getX();
		float touchY = e.getY();
		Point point = new Point();
		point.x = (int) e.getX();
		point.y = (int) e.getY();
		Log.v("x position", Integer.toString(point.x));
		Log.v("y position", Integer.toString(point.y));
		switch(e.getAction()) {
			case MotionEvent.ACTION_DOWN:
				drawPath.moveTo(touchX, touchY);
				shadowCanvas.drawPath(drawPath, paint);
				break;
			case MotionEvent.ACTION_MOVE:
				drawPath.lineTo(touchX, touchY);
				shadowCanvas.drawPath(drawPath, paint);
				break;
			case MotionEvent.ACTION_UP:
				shadowCanvas.drawPath(drawPath, paint);
			    drawPath.reset();
				break;
		}
		updateBitmap();
		invalidate();
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// Draw mainBitmap
		canvas.drawBitmap(mainBitmap, null, new Rect(0, 0, width, height), null);
		//canvas.drawPath(drawPath, paint);
	}

	// Updates the main bitmap
	public void updateBitmap() {
		
		// get int array from shadowBitmap
		int[] shadowPixels = new int[width * height];
		shadowBitmap.getPixels(shadowPixels, 0, shadowBitmap.getWidth(), 0, 0,
				shadowBitmap.getWidth(), shadowBitmap.getHeight());
		int[] mainPixels = new int[width * height];
		//Log.v("bitmap width", Integer.toString(mainBitmap.getWidth()));
		//Log.v("bitmap height", Integer.toString(mainBitmap.getHeight()));
		mainBitmap.getPixels(mainPixels, 0, mainBitmap.getWidth(), 0, 0,
				mainBitmap.getWidth(), mainBitmap.getHeight());
		//Log.v("shadowArray", Arrays.toString(shadowPixels));
		//Log.v("width", Integer.toString(shadowBitmap.getWidth()));
		//Log.v("height", Integer.toString(shadowBitmap.getHeight()));
		for (int i = 0; i < width * height; i++) {
			if (shadowPixels[i] == Color.BLACK) {
				if (colorToGray) {
					mainPixels[i] = greyscalePixels[i];
				} else {
					mainPixels[i] = colorPixels[i];
				}
			}
		}
		mainBitmap.setPixels(mainPixels, 0, shadowBitmap.getWidth(),0, 0,
						mainBitmap.getWidth(), mainBitmap.getHeight());
		invalidate();
	}
	
	// see http://androidsnippets.com/convert-bitmap-to-grayscale
	/**
	 * Convert bitmap to the grayscale
	 *
	 * @param bmpOriginal Original bitmap
	 * @return Grayscale bitmap
	 */
	public Bitmap toGrayscale(Bitmap bmpOriginal) {
		final int height = bmpOriginal.getHeight();
		final int width = bmpOriginal.getWidth();

		final Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		final Canvas c = new Canvas(bmpGrayscale);
		final Paint paint = new Paint();
		final ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		final ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}
}
