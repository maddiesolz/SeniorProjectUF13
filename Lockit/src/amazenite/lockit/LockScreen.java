package amazenite.lockit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import amazenite.lockit.SetPoints.GraphicView;
import amazenite.lockit.SetPoints.MyGestureListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GestureDetectorCompat;
import android.widget.Toast;
import android.os.Vibrator;

public class LockScreen extends Activity {
	private static final String DEBUG_TAG = "Gestures"; 	
	private static float x = -50;
 	private static float y = -50;
 	private int numGestures = 0;
 	private int counter = 0;
	private static final String DEBUG_TAG2 = "Coordinates"; 
 	private GraphicView graphView;
 	private GestureDetectorCompat mDetector; 
	private float[] coordinates = {-1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f};
	
	
	@Override
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_lock_screen);
		 x = -50;
		 y = -50;
		Log.d("lockscreen", "in lockscreen");
		// Show the Up button in the action bar.
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		graphView = new GraphicView(this);
				
		
		//Set As Background Image
	    
	    File file = getBaseContext().getFileStreamPath("lockimg");
	    String internalPath = "data/data/files/lockimg";
	    if (file.exists()) {
	    	 internalPath = file.getAbsolutePath();
	    	 Log.d(DEBUG_TAG2, "Coordinates exist");

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

		getCoordinates();
		
		  for(int i = 0; i<coordinates.length; i++)
	        {
		Log.d("coordinates are: ", Float.toString(coordinates[i]));
	        }
	}
	
	public void getCoordinates(){
		try {
			File file = getBaseContext().getFileStreamPath("coordinates");
			Scanner sc = new Scanner(new File(file.getAbsolutePath()));
			String line = sc.nextLine();
			String[] numbers = line.split(" ");

		        for(int i = 0; i<coordinates.length; i++)
		        {
		        		coordinates[i] = Float.parseFloat(numbers[i].trim());
		        }
	    
			}
        catch (FileNotFoundException e1) {
        	e1.printStackTrace();
        	}
		}
	
	 @Override 
	    public boolean onTouchEvent(MotionEvent event){ 
	        this.mDetector.onTouchEvent(event);        
	        return super.onTouchEvent(event);
	    }

	 /* Gesture Dectector Class To Only listen On The Ones We Want */	
		public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
			  @Override
			    public boolean onSingleTapConfirmed(MotionEvent event) {
				  					
					if(numGestures < 3 && counter <= 4) {
				  
				        Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
				        x = event.getRawX();
				        y = event.getRawY()-75.0f;
				        graphView.invalidate();
				        Log.d(DEBUG_TAG, "X is: " + x);
					    Log.d(DEBUG_TAG, "Y is: " + y);
					    					    
					    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					    
					    	if( v.hasVibrator()) {
								 v.vibrate(50);
					    	}
					    	
					    	if((x <=coordinates[counter]+100.0f  && x >= coordinates[counter]-100.0f) && 
					    	   (y <= coordinates[counter+1]+100.0f && y >= coordinates[counter+1]-100.0f)){
					    		numGestures++;
					    		counter++;
						    /*	Log.d("x: " +  x ,"set x: " + coordinates[counter]);
						    	Log.d("y: " +  y , "set y: " + coordinates[counter+1]);
						    	Log.d("counter ", ""+counter);*/
						    		if(counter >= 3){
								    	Toast.makeText(LockScreen.this, "" + "Unlocked!", Toast.LENGTH_SHORT).show();
						    			finish();
						    		}
					    	}
					    	else {
						    	Toast.makeText(LockScreen.this, "" + "Incorrect Password! Please try agan.", Toast.LENGTH_SHORT).show();
						    	/*Log.d("x: " +  x ,"set x: " + coordinates[counter]);
						    	Log.d("y: " +  y , "set y: " + coordinates[counter+1]);
						    	Log.d("counter ", ""+counter);*/
						    	graphView.invalidate();
						    	numGestures = 0;
						    	counter = 0;
					    	}
					}
				    
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
				        public void onDraw(Canvas canvas) {
				        	dotColor.setColor(0xff33CCCC);
				        	dotColor.setAlpha(80);
				        	super.onDraw(canvas);
				        	dotColor.setStyle(Paint.Style.FILL);
				        	
				        	if(numGestures > 0) {
					        	canvas.drawCircle(x, y, 20, dotColor);
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
		}
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_lock_screen, menu);
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
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
*/

