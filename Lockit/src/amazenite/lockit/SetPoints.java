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
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.view.View; 
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
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
 	private boolean isScrolling = false;
 	private String[] coordinates;
 	private float[] moveCoordinates = {-50, -50, -50, -50};
 	private float midpointX = 0.0f;
 	private float midpointY = 0.0f;
 	private String type = "";
 	private float averageRadius = 0.0f;
 	private int chosenColor;
 	private int numberOfGestures;

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		graphView = new GraphicView(this);
		
		chosenColor = Constants.gestureColor;
		loadNumberGestures();
		

		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		                      WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
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
        //Set Gesture Detector 
	    mDetector = new GestureDetectorCompat(this, new MyGestureListener());
	}
	
	public void loadNumberGestures()
	{
		numberOfGestures = Constants.gestureCount;
		coordinates = new String[numberOfGestures];
		for(int i = 0; i < numberOfGestures; i++)
		{
			coordinates[i] = "";
		}
			
	}
	
	 @Override 
    public boolean onTouchEvent(MotionEvent event){ 
        this.mDetector.onTouchEvent(event);
        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(isScrolling ) {
                isScrolling  = false;
                trimArray();
  			  	checkGesture();
            };
        }
        return super.onTouchEvent(event);
    }
    
	/*******************************************************************************************
	 * 									MyGeatureListener Class
	 *******************************************************************************************/
	public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		  @SuppressLint("NewApi")
		@Override
		    public boolean onSingleTapConfirmed(MotionEvent event) {			 
		        x = event.getX();
		        y = event.getY();
		        type = "Adot";
		        graphView.invalidate();
			    storeCoordinates();			    
			    
			    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		    	if( v.hasVibrator()) {
					 v.vibrate(50);
		    	}
		        return true;
		    }
/*
 * 		onDoubleTap - Clear Coordinates
 */
		  @Override
		  public boolean onDoubleTap(MotionEvent event)
		  {
			  clearCoordinates();
			  final Toast toast = Toast.makeText(getApplicationContext(), "Gestures reset, please make "+numberOfGestures+" gestures again.", Toast.LENGTH_SHORT);
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
			  x = e1.getX();
			  y = e1.getY();
			  x2 = e2.getX();
			  y2 = e2.getY();
			  
			  storeMoveCoordinates();
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
		fillRadiusArray();
		boolean line = checkLine();  //Checks if this is a line or not
		
		if(line)
		{
			type = "Line";
			storeCoordinates();
			graphView.invalidate();
		}
		 else
		{
			type = "Circ";
			storeCoordinates();
			graphView.invalidate();
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
					coordinates[i] = "Adot," + Float.toString(x) + "," + Float.toString(y);
				}
				else if(type == "Line")
				{
					coordinates[i] = "Line," + Float.toString(moveCoordinates[0]) + "," + Float.toString(moveCoordinates[1]) + "," + Float.toString(moveCoordinates[moveCoordinates.length-2]) + "," + Float.toString(moveCoordinates[moveCoordinates.length-1]);
				}
				else if(type == "Circ")
				{
					coordinates[i] = "Circ," + Float.toString(moveCoordinates[0]) + "," + Float.toString(moveCoordinates[1]) + "," + Float.toString(midpointX) + "," + Float.toString(midpointY) + "," + Float.toString(averageRadius);
				}				
				break;
			}
		}
		checkFull();
	}

	public void fillRadiusArray()
	{
		float avgX = 0.0f;
		float avgY = 0.0f;
		for(int i = 0; i < moveCoordinates.length; i = i+2)
		{
			avgX += moveCoordinates[i];
			avgY += moveCoordinates[i+1];
		}
		midpointX = avgX/(moveCoordinates.length/2);
		midpointY = avgY/(moveCoordinates.length/2);
		
		float[] distanceArray = new float[moveCoordinates.length/2];
		for(int i = 0; i < distanceArray.length; i++)
		{
			float diffX = midpointX - moveCoordinates[2*i];
			float diffY = midpointY - moveCoordinates[2*i + 1];
			diffX = diffX * diffX;
			diffY = diffY * diffY;
			float sum = (float) Math.sqrt(diffX + diffY);
			distanceArray[i] = sum;
		}
		float avgDist = 0.0f;
		for(int i = 0; i < distanceArray.length; i++)
		{
			avgDist += distanceArray[i];
		}
		averageRadius = avgDist/distanceArray.length;
	}
	
	public boolean checkLine()
	{
		int halfway = moveCoordinates.length/2;
		if(halfway % 2 != 0)
		{
			halfway--;
		}
		float diffX = moveCoordinates[halfway] - moveCoordinates[0];
		int countX = 0;
		for(int i = 0; i < moveCoordinates.length-2; i+=2)
		{
			if(diffX > 0)
			{
				if(moveCoordinates[i] < moveCoordinates[i+2])
				{
					countX++;
				}
			}
			else
			{
				if(moveCoordinates[i] > moveCoordinates[i+2])
				{
					countX++;
				}
			}
		}
		if(countX >= (moveCoordinates.length/2)-3)
		{
			
			return true;
		}
		else
		{

			float diffY = moveCoordinates[halfway+1] - moveCoordinates[1];
			int countY = 0;
			for(int i = 1; i < moveCoordinates.length-1; i+=2)
			{
				if(diffY > 0)
				{
					if(moveCoordinates[i] < moveCoordinates[i+2])
					{
						countY++;
					}
				}
				else
				{
					if(moveCoordinates[i] > moveCoordinates[i+2])
					{
						countY++;
					}
				}
			}
			if(countY >= (moveCoordinates.length/2)-3)
			{	
				return true;
			}
			else
			{
				return false;
			}
		}
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
	
	public void clearMoveCoordinates()
	{
		float[] temp =  {-50, -50, -50, -50};
		moveCoordinates = temp;
	}
		
	public void clearCoordinates()
	{
		for(int i = 0; i<coordinates.length; i++)
		{
			coordinates[i] = "";
		}
		clearMoveCoordinates();
	}
	 
	//Creates canvas
	/*******************************************************************************************
	 * 									GraphicView Class
	 *******************************************************************************************/
	 public class GraphicView extends View{		  
		  	Paint dotColor = new Paint(Paint.ANTI_ALIAS_FLAG);
		  	Paint lineColor = new Paint(Paint.ANTI_ALIAS_FLAG);
		  	Paint circColor = new Paint(Paint.ANTI_ALIAS_FLAG);
		  	
	        public GraphicView(Context context){
	            super(context);
	            setFocusable(true);
	        }

	        @Override
	        public void onDraw(Canvas canvas){
	        	super.onDraw(canvas);
	        	if(type == "Adot")
	        	{
	        		dotColor.setColor(chosenColor);
		        	dotColor.setStyle(Paint.Style.FILL);
	        		canvas.drawCircle(x, y, 20, dotColor);
	        	}
	        	else if(type == "Line")
	        	{
	        		lineColor.setColor(chosenColor);
	        		lineColor.setStyle(Paint.Style.FILL);
	        		lineColor.setStrokeWidth(30);
	        		lineColor.setStrokeCap(Paint.Cap.ROUND);
	        		canvas.drawLine(moveCoordinates[0], moveCoordinates[1], moveCoordinates[moveCoordinates.length-2], moveCoordinates[moveCoordinates.length-1], lineColor);
	        		clearMoveCoordinates();
	        	}
	        	else if(type == "Circ")
	        	{
	        		circColor.setColor(chosenColor);
	        		circColor.setStyle(Style.STROKE);
	        		circColor.setStrokeWidth(30);
	        		canvas.drawCircle(midpointX, midpointY, averageRadius, circColor);
	        		clearMoveCoordinates();
	        	}
	        	type = "";
	        }
	   }
}


