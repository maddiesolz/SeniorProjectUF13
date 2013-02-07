package amazenite.lockit;

import java.io.File;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View; 
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GestureDetectorCompat;


public class SetPoints extends Activity { 
	private int x = 0;
 	private int y = 0;
 
 private GestureDetectorCompat mDetector; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_points);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		

		
		//Set As Background Image
	    ImageView img = (ImageView) findViewById(R.id.setPointsPic);
	    if(img != null)
	    {
		    File file = getBaseContext().getFileStreamPath("lockimg");
		    String internalPath = "data/data/files/lockimg";
		    if (file.exists()) {
		    	 internalPath = file.getAbsolutePath();
		    }
		        	Drawable d = Drawable.createFromPath(internalPath);
		        if(d!=null)
		        {
		        	 img.setImageDrawable(d);
		        	 img.invalidate();
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
	


/* Gesture Dectector Class To Only listen On The Ones We Want */
	
	 public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		 //	private int x = 0;
		 //	private int y = 0;
	        private static final String DEBUG_TAG = "Gestures"; 	
	/*Detect The Simple Gestures */
	  @Override
	    public boolean onSingleTapUp(MotionEvent event) {
	        Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
	        x = (int)event.getRawX();
	        y = (int)event.getRawY();
 
	       Log.d(DEBUG_TAG, "X is: " + x);
	       Log.d(DEBUG_TAG, "Y is: " + y);
	
	        return true;
	    }
	  
	  
	   }
	 
	  public final int getX(){
		  return x;  
	  }
	  
	  public final int getY(){
		  return y;  
	  }
	  public static  class GraphicView extends View{
		  
		  Paint dotColor = new Paint(Paint.ANTI_ALIAS_FLAG);
		  
		  
	        public GraphicView(Context context){
	            super(context);
	        }

	        @Override
	        public void onDraw(Canvas canvas){
	        	dotColor.setColor(0xff0000ff);
	        	super.onDraw(canvas);
	        	   Log.d("Graphic View","IM HERE");
	        	if(getX() != 0 && getY() != 0){
	        	//canvas.drawPoint(getX(), getY(), dotColor);
	        	       canvas.drawCircle(getX(), getY(), 6, dotColor);
	        	       Log.d("Graphic View","IM DRAWING");
	        	   }
	     
	        	}
	          
	        }
	  }


