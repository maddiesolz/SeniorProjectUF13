package amazenite.lockit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View; 
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;
import android.support.v4.view.GestureDetectorCompat;
import android.os.Vibrator;


public class SetPoints extends Activity { 	
	private static float x = -50;
 	private static float y = -50;
 	private GraphicView graphView; 
 	private GestureDetectorCompat mDetector; 
 	private float[] coordinates = {-1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f};

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		graphView = new GraphicView(this);
		 x = -50;
		 y = -50;
		
		//Set As Background Image
	    File file = getBaseContext().getFileStreamPath("lockimg");
	    String internalPath = "data/data/files/lockimg";
	    if (file.exists()) {
	    	 internalPath = file.getAbsolutePath();
	    }
        Drawable d = Drawable.createFromPath(internalPath);
        if(d!=null)
        {
        	 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        		 setContentView(graphView);
        		 graphView.setBackground(d);
        	 }
        	 else
        	 {
        		 setContentView(graphView);
        		 graphView.setBackgroundDrawable(d);
        	 }
         }    
	    mDetector = new GestureDetectorCompat(this, new MyGestureListener());
	}
	
    @Override 
    public boolean onTouchEvent(MotionEvent event){ 
        this.mDetector.onTouchEvent(event);        
        return super.onTouchEvent(event);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_set_points, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
		//	NavUtils.navigateUpFromSameTask(this);
			finish();
			return true;
		}
		return false;//return super.onOptionsItemSelected(item);
	}

	public void storeCoordinates(float x, float y)
	{
		for(int i = 0; i<coordinates.length; i++)
		{
			if(coordinates[i] == -1)
			{
				coordinates[i] = x;
				coordinates[i+1] = y;
				break;
			}
		}
		checkFull();
	}
	
	public void checkFull()
	{
		
		if(coordinates[coordinates.length-1] != -1)
		{
			//Full, save the array
			try {
				String space = " ";
	        	FileOutputStream fos = openFileOutput("coordinates", Context.MODE_PRIVATE);
		        for(int i = 0; i<coordinates.length; i++)
		        {
		        	try {
						fos.write(( Float.toString(coordinates[i]) + space).getBytes());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
	        	try {
		        		fos.close();
		        		fos = null;
		        	} 
		        	catch (IOException e) {
		        		e.printStackTrace();
		        	}
	        	} 
	        catch (FileNotFoundException e1) {
	        	e1.printStackTrace();
	        	}
			  Toast.makeText(SetPoints.this, "Coordinates Saved", Toast.LENGTH_SHORT).show();
			  finish();
		}
		
	}
	
	public void clearCoordiantes()
	{
		for(int i = 0; i<coordinates.length; i++)
		{
			coordinates[i] = -1;
		}
	}
	
	/* Gesture Dectector Class To Only listen On The Ones We Want */	
	public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		  @Override
		    public boolean onSingleTapConfirmed(MotionEvent event) {			 
			        x = event.getRawX();
			        y = event.getRawY()-40.0f;
			        graphView.invalidate();
				    
				    storeCoordinates(x, y);
				    
				    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			    	if( v.hasVibrator()) {
						 v.vibrate(50);
			    	}
		        return true;
		    }
		  
		  @Override
		  public boolean onDoubleTap(MotionEvent event)
		  {
			  clearCoordiantes();
			  final Toast toast = Toast.makeText(getApplicationContext(), "Gestures reset, please make 3 gestures again.", Toast.LENGTH_SHORT);
	    	    toast.show();
				//Toast.makeText(LockScreen.this, "" + "Incorrect Password! Please try agan.", Toast.LENGTH_SHORT).show();
				Handler handler = new Handler();
		        handler.postDelayed(new Runnable() {
		           @Override
		           public void run() {
		               toast.cancel(); 
		           }
		        }, 1000);
			  x = -100;
			  y = -100;
			  graphView.invalidate();
			  
			  return true;
		  }
	 }
	  
	 public class GraphicView extends View{		  
		  Paint dotColor = new Paint(Paint.ANTI_ALIAS_FLAG);
		  
	        public GraphicView(Context context){
	            super(context);
	            setFocusable(true);
	        }

	        @Override
	        public void onDraw(Canvas canvas){
	        	dotColor.setColor(0xff33CCCC);
	        	dotColor.setAlpha(80);
	        	super.onDraw(canvas);
	        	dotColor.setStyle(Paint.Style.FILL);        	

	        	canvas.drawCircle(x, y, 20, dotColor);
	        	//canvas.drawLine(startX, startY, stopX, stopY, paint)
	        	for(int i = 0; i<coordinates.length; i++)
	    		{
	    			if(coordinates[i] != -1)
	    			{
	    				Log.d("coordinates", Float.toString(coordinates[i]));
	    			}
	    		}
	        	
	        }	          
	   }
}


