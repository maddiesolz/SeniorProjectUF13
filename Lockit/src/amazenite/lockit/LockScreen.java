package amazenite.lockit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.support.v4.view.GestureDetectorCompat;
import android.widget.Toast;

public class LockScreen extends Activity {	
	private static float x = -50;
 	private static float y = -50;
 	private int counter = 0;
 	private GraphicView graphView;
 	private GestureDetectorCompat mDetector; 
	private float[] coordinates = {-1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f};
	private boolean correctGestures = true;
	
	
	@Override
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 x = -50;
		 y = -50;
		 graphView = new GraphicView(this);
				
		
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
			    public boolean onSingleTapUp(MotionEvent event) {
					if(counter <= 4 ) {
				        x = event.getRawX();
				        y = event.getRawY()-40.0f;
				        graphView.invalidate();
					    					    
					    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					    if( v.hasVibrator()) {
							 v.vibrate(35);
				    	}
				    	
				    	if((x > (coordinates[counter]+30.0f )) || (x <  (coordinates[counter]-30.0f)) || 
				    	   (y > (coordinates[counter+1]+30.0f)) || (y < (coordinates[counter+1]-30.0f))){
				    		correctGestures = false;
				    	}
				    	counter = counter+2;
				    	if(counter >= 5){
			    			if(correctGestures){
			    				final Toast toast = Toast.makeText(getApplicationContext(), "Unlocked!", Toast.LENGTH_SHORT);
					    	    toast.show();
			    				Handler handler = new Handler();
			    		        handler.postDelayed(new Runnable() {
			    		           @Override
			    		           public void run() {
			    		               toast.cancel(); 
			    		           }
			    		        }, 1000);
				    			finish();
			    			}
			    			else
			    			{
			    				counter = 0;
					    		correctGestures = true;
					    		final Toast toast = Toast.makeText(getApplicationContext(), "Incorrect Password! Please try again.", Toast.LENGTH_SHORT);
					    	    toast.show();
			    				Handler handler = new Handler();
			    		        handler.postDelayed(new Runnable() {
			    		           @Override
			    		           public void run() {
			    		               toast.cancel(); 
			    		           }
			    		        }, 1000);
			    			}
			    		}
					}
			        return true;
			    }
			}
		
			//Draws the points on screen
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
			        	canvas.drawCircle(x, y, 20, dotColor);
			        }	          
			   }
			 
			 @Override
			 public void onBackPressed() {
			     // Do Here what ever you want do on back press;
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

