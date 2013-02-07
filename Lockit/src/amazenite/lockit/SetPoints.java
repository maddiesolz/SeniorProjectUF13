package amazenite.lockit;

import java.io.File;

import android.os.Build;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.support.v4.view.GestureDetectorCompat;


public class SetPoints extends Activity { 
	private static final String DEBUG_TAG = "Gestures"; 	
	private static float x = -50;
 	private static float y = -50;
 	private GraphicView graphView; 
 	private GestureDetectorCompat mDetector; 

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		graphView = new GraphicView(this);
		//setContentView(R.layout.activity_set_points);
		
		
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//Set As Background Image
	    
	    File file = getBaseContext().getFileStreamPath("lockimg");
	    String internalPath = "data/data/files/lockimg";
	    if (file.exists()) {
	    	 internalPath = file.getAbsolutePath();
	    }
        Drawable d = Drawable.createFromPath(internalPath);
        if(d!=null)
        {
        	 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
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
        graphView.invalidate();
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
		  @Override
		    public boolean onSingleTapUp(MotionEvent event) {
		        Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
		        x = event.getRawX();
		        y = event.getRawY()-75.0f;
		        
		        Log.d(DEBUG_TAG, "X is: " + x);
			    Log.d(DEBUG_TAG, "Y is: " + y);
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
	        }	          
	   }
}


