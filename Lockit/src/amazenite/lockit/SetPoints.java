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
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.widget.Toast;
import android.support.v4.view.GestureDetectorCompat;
import android.os.Vibrator;

//Sets the points/gestures
public class SetPoints extends Activity { 	
	private static float x = -50;
 	private static float y = -50;
 	private static float x2 = -50;
 	private static float y2 = -50;
 	private GraphicView graphView; 
 	private GestureDetectorCompat mDetector;
 	private OnTouchListener mGestureListener;
 	private String[] coordinates = {"", "", ""};
 	private float[] moveCoordinates = {-50, -50, -50, -50};
 	private boolean isScrolling = false;
 	private String type = "";

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		graphView = new GraphicView(this);
		
		//Set coordinates to be outside of the viewing frame
		x = -50;
		y = -50;
		x2 = -50;
		y2 = -50;
		
		//Set As Background Image from Stored Image
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
        
        //Set Gesture Detector && Gesture Listener
	    mDetector = new GestureDetectorCompat(this, new MyGestureListener());
	    mGestureListener = new View.OnTouchListener() {
	        public boolean onTouch(View v, MotionEvent event) {
	            if (mDetector.onTouchEvent(event)) {
	            	Log.d("hey", "gestures");
	                return true;
	            }

	            if(event.getAction() == MotionEvent.ACTION_UP) {
	                if(isScrolling ) {
	                    isScrolling  = false;
	                    //handleScrollFinished();
	                    Log.d("hey", "finished scroll");
	                };
	            }
	            return false;
	        }
	    };
	    graphView.setOnTouchListener(mGestureListener);	    
	}
	
    @Override 
    public boolean onTouchEvent(MotionEvent event){ 
        this.mDetector.onTouchEvent(event);        
        return super.onTouchEvent(event);
    }
	
	/* Gesture Detector Class To Only listen On The Ones We Want */	
    
	public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		  @Override
		    public boolean onSingleTapConfirmed(MotionEvent event) {			 
		        x = event.getRawX()-40;
		        y = event.getRawY();
		        graphView.invalidate();
			    
		        type = "Adot";
			    storeCoordinates();			    
			    
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
		  
		  @Override
		  public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
		  {
			  isScrolling = true;
			  x = e1.getRawX();
			  y = e1.getRawY()-40.0f;
			  x2 = e2.getRawX();
			  y2 = e2.getRawY()-40.0f;
			  
			  graphView.invalidate();
			  
			  storeMoveCoordinates();
			  //checkgesture & trim array needs to be moved
			  trimArray();
			  checkGesture();
			  
			  return true;
		  }
		  
		  
	 }
	
	//Store coordinates of gesture path
	public void storeMoveCoordinates()
	{
		 if(moveCoordinates[0] == -50)
		  {
			  moveCoordinates[0] = x;
			  moveCoordinates[1] = y;
		  }
		  
		  if(moveCoordinates[moveCoordinates.length-1] != -50)
		  {
			  float temp[] = new float[moveCoordinates.length*2];
			  for(int k = 0; k<moveCoordinates.length; k++)
			  {
				  temp[k] = moveCoordinates[k];
			  }
			  for(int j = moveCoordinates.length; j<temp.length; j++)
			  {
				  temp[j] = -50;
			  }
			  moveCoordinates = temp;
		  }			
		  
		  for(int i = 0; i<moveCoordinates.length; i++)
		  {
			  if(moveCoordinates[i] == -50)
			  {
				  moveCoordinates[i] = x2;
				  moveCoordinates[i+1] = y2;
				  break;
			  }
		  }
	}
	
	public void trimArray()
	{
		int i = 0;
		for(; i<moveCoordinates.length; i++)
		{
			if(moveCoordinates[i] == -50)
			{
				break;
			}
		}
		
		float[] temp = new float[i];
		for(int j = 0; j < i; j++)
		{
			temp[j] = moveCoordinates[j];
		}
		
		moveCoordinates = temp;
	}
	public void checkGesture()
	{
		int halfway = moveCoordinates.length/2;
		//y coordinate
		if(halfway % 2 != 0)
		{
			halfway++;
		}
		float slopeHalf = (moveCoordinates[halfway+1] - moveCoordinates[1])/(moveCoordinates[halfway] - moveCoordinates[0]);
		Log.d("Hey", "" + moveCoordinates[halfway+1] + "-" + moveCoordinates[1] + "/" + moveCoordinates[halfway] + "-" + moveCoordinates[0]);
		float slopeEnd = (moveCoordinates[moveCoordinates.length-1] - moveCoordinates[1])/(moveCoordinates[moveCoordinates.length-2] - moveCoordinates[0]);
		Log.d("Hey", "" + moveCoordinates[moveCoordinates.length-1] + "-" + moveCoordinates[1] + "/" + moveCoordinates[moveCoordinates.length-2] + "-" + moveCoordinates[0]);
		Log.d("Hey", "" + slopeHalf);
		Log.d("hey", "" + slopeEnd);
		if(slopeHalf-slopeEnd < 1 && slopeHalf-slopeEnd > -1)
		{
			type = "Line";
			storeCoordinates();
		}
		else
		{
			//circle
		}
		
	}
	
	public void storeCoordinates()
	{
		for(int i = 0; i<coordinates.length; i++)
		{
			if(coordinates[i] == "")
			{
				if(type == "Adot")
				{
					coordinates[i] = "Adot:" + Float.toString(x) + "," + Float.toString(y);
				}
				else if(type == "Line")
				{
					coordinates[i] = "Line:" + Float.toString(moveCoordinates[0]) + "," + Float.toString(moveCoordinates[1]) + "," + Float.toString(moveCoordinates[moveCoordinates.length-2]) + "," + Float.toString(moveCoordinates[moveCoordinates.length-1]);
					clearMoveCoordiantes();
				}
				else if(type == "Circ")
				{
					//UNFINSHED
					clearMoveCoordiantes();
				}
				
				break;
			}
		}
		checkFull();
	}
	
	public void checkFull()
	{	
		for(int i = 0; i<coordinates.length; i++)
		{
			if(coordinates[i] != "")
			{
				Log.d("coordinates", coordinates[i]);
			}
		}
		if(coordinates[coordinates.length-1] != "")
		{
			//Full, save the array
			try {
				String space = " ";
	        	FileOutputStream fos = openFileOutput("coordinates", Context.MODE_PRIVATE);
		        for(int i = 0; i<coordinates.length; i++)
		        {
		        	try {
						fos.write(( coordinates[i] + space).getBytes());
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
	
	public void clearMoveCoordiantes()
	{
		float[] temp =  {-50, -50, -50, -50};
		moveCoordinates = temp;
	}
	
	public void clearCoordiantes()
	{
		for(int i = 0; i<coordinates.length; i++)
		{
			coordinates[i] = "";
		}
		clearMoveCoordiantes();
	}
	 
	//Creates canvas for dot
	 public class GraphicView extends View{		  
		  	Paint dotColor = new Paint(Paint.ANTI_ALIAS_FLAG);
		  	Paint lineColor = new Paint(Paint.ANTI_ALIAS_FLAG);
		  
	        public GraphicView(Context context){
	            super(context);
	            setFocusable(true);
	        }

	        @Override
	        public void onDraw(Canvas canvas){
	        	super.onDraw(canvas);
	        	
	        	if(type == "Adot")
	        	{
	        		dotColor.setColor(0xff33CCCC);
		        	dotColor.setAlpha(80);
		        	dotColor.setStyle(Paint.Style.FILL);
		        	//dotColor.setStrokeWidth(10);
	        		canvas.drawCircle(x, y, 20, dotColor);
		        	//canvas.drawLine(x, y, x+100, y+100, dotColor);
	        	}
	        	else if(type == "Line")
	        	{
	        		lineColor.setColor(0xff33CCCC);
	        		lineColor.setAlpha(80);
	        		lineColor.setStyle(Paint.Style.FILL);
	        		lineColor.setStrokeWidth(10);
	        		canvas.drawLine(moveCoordinates[0], moveCoordinates[1], moveCoordinates[moveCoordinates.length-2], moveCoordinates[moveCoordinates.length-1], lineColor);
	        	}
	        }	          
	   }
}


