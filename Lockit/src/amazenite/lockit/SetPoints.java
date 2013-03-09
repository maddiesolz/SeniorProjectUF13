package amazenite.lockit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

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
 	private String[] coordinates;
 	private float[] moveCoordinates = {-50, -50, -50, -50};
 	private boolean isScrolling = false;
 	private String type = "";
 	float[] circleCoordinates = new float[3];
 	private int chosenColor;

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		graphView = new GraphicView(this);
		
		loadNumberGestures();
		loadColor();
		
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
	
	/*
	 * 							LOAD NUMBER OF GESTURES
	 */
	
	public void loadNumberGestures()
	{
		try {
			File file = getBaseContext().getFileStreamPath("numGestures");
			Scanner sc = new Scanner(new File(file.getAbsolutePath()));
			String line = sc.nextLine();
			int num = Integer.parseInt(line);
			if(num != 0)
			{
				coordinates = new String[num];
				for(int i = 0; i < num; i++)
				{
					coordinates[i] = "";
				}
			}
		}
        catch (FileNotFoundException e1) {
        	e1.printStackTrace();
        	coordinates = new String[3];
        	}
	}
	
	/*
	 * 							LOAD CHOSEN COLOR
	 */
	
	public void loadColor()
	{
		try
		{
			File file = getBaseContext().getFileStreamPath("pickedColor");
			Scanner sc = new Scanner(new File(file.getAbsolutePath()));
			String line = sc.nextLine();	
			chosenColor = Integer.parseInt(line);
		}
        catch (FileNotFoundException e1)
        {
        	e1.printStackTrace();
        }
	}
	
	/*
	 * 						   OnTouchEvent Characteristics
	 */
	
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
	
	/* Gesture Detector Class To Only listen On The Ones We Want */	
    
	/*******************************************************************************************
	 * 									MyGeatureListener Class
	 *******************************************************************************************/
	public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		  @SuppressLint("NewApi")
		@Override
		    public boolean onSingleTapConfirmed(MotionEvent event) {			 
		        x = event.getRawX();
		        y = event.getRawY()-40.0f;
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
 * 							onDoubleTap - Clear Coordinates
 */
		  @Override
		  public boolean onDoubleTap(MotionEvent event)
		  {
			  clearCoordinates();
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
 /*
* 							onScroll - Lines and Circles
*/		  			  
		  @Override
		  public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
		  {
			  isScrolling = true;
			  x = e1.getRawX();
			  y = e1.getRawY()-40.0f;
			  x2 = e2.getRawX();
			  y2 = e2.getRawY()-40.0f;
			  
			  storeMoveCoordinates();
			  return true;
		  }		  
	 }
/*
 * 							storeMoveCoordinates - onScroll Store Coordinates
*/
			
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
/*
* 							checkGesture()  ASSIGNS GESTURE
*/
			
	public void checkGesture() 
	{
		boolean line = checkLine();  //Checks if this is a line or not
		//float slopeHalf = -1;
		//float slopeEnd = -1;
		int halfway = moveCoordinates.length/2;
		Log.d("halfway mark?", "" + halfway);
		Log.d("HOW BIG IS IT?", ""+moveCoordinates.length);
		//y coordinate
	/*	if(halfway % 2 != 0)
		{
			halfway--;
		}*/
	/*	else
		{
			slopeHalf  = (moveCoordinates[halfway+1] - moveCoordinates[1])/(moveCoordinates[halfway] - moveCoordinates[0]);
			slopeEnd   = (moveCoordinates[moveCoordinates.length-1] - moveCoordinates[halfway+1])/(moveCoordinates[moveCoordinates.length-2] - moveCoordinates[halfway]);
		}*/

		Log.d("line?", "" + line);

		//if((slopeHalf-slopeEnd < .7 && slopeHalf-slopeEnd > -.7) || vertical)
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
/*
* 							storeCoordinates()  Store Coordinates of each type
*/	
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
					float[] circleCoordinates = circleCalc();
					coordinates[i] = "Circ," + Float.toString(moveCoordinates[0]) + "," + Float.toString(moveCoordinates[1]) + "," + Float.toString(circleCoordinates[0]) + "," + Float.toString(circleCoordinates[1]) + "," + Float.toString(circleCoordinates[2]);
				}				
				break;
			}
		}
		checkFull();
	}
/*
* 							circleCalc()  Calculates center x, center y, and radius
*/	
	public float[] circleCalc()
	{
		float avgX = 0.0f;
		float avgY = 0.0f;
		for(int i = 0; i < moveCoordinates.length; i = i+2)
		{
			avgX += moveCoordinates[i];
			avgY += moveCoordinates[i+1];
		}
		avgX = avgX/(moveCoordinates.length/2);
		avgY = avgY/(moveCoordinates.length/2);		
		
		float[] distanceArray = new float[moveCoordinates.length/2];
		for(int i = 0; i < distanceArray.length; i++)
		{
			float diffX = avgX - moveCoordinates[2*i];
			float diffY = avgY - moveCoordinates[2*i + 1];
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
		avgDist = avgDist/distanceArray.length;
		
		circleCoordinates[0] = avgX;
		circleCoordinates[1] = avgY;
		circleCoordinates[2] = avgDist;
		return circleCoordinates;
	}
/*
* 						checkLine()  Checks if this is a Horizontal, Vertical, or Regular Line
*/	
	public boolean checkLine()
	{
	//is it vertical or not?
		int correctCount = 0;
		int iterations = 0;
		boolean result = false;
		
			for(int i = 0; i < moveCoordinates.length-1; i+=2)
			{
				if(i+2 > moveCoordinates.length-1)
				{
					break;
				}
				int diffX = (int)(moveCoordinates[i]-moveCoordinates[i+2]);
					if(diffX <= 2 && diffX >= -2)
					{
						correctCount++;
					}
			iterations++;
				
			}
			if(correctCount >= iterations/2) //vert
			{
				result = true;
				Log.d("IS IT FREAKING VERT?", ""+result);

			}
			else  //check slope to see if its any other kind of line
			{
		    correctCount = 0;
		    int horitzontalCount = 0;
		    iterations = 0;
			float[] slope = new float[moveCoordinates.length];
				for(int i = 0; i < moveCoordinates.length-1; i++)
				{
				  if(i+3 > moveCoordinates.length-1)
				  {
					  break;
				  }
					 slope[i]  = (moveCoordinates[i+3] - moveCoordinates[i+1])/(moveCoordinates[i+2] - moveCoordinates[i]);
				}
				for(int i = 0; i < slope.length-1; i++)
				{
					Log.d("SLOPES?", ""+slope[i]);

					iterations++;
					if((slope[i]-slope[i+1]) < 2f && (slope[i]-slope[i+1]) > -2f)
					{
						correctCount++;
					}
					else if ((int)slope[i] == 0)  //horizontal line
					{
						horitzontalCount++;
					}
				}
				Log.d("HOW MANY RIGHT?", ""+correctCount);

				if((correctCount >= ((6*iterations)/10)) || horitzontalCount >= ((iterations)/2)-2) //LINE
				{
					result = true;
				}
				else
				{
					result = false;
				}
			}

		return result;
	}
/*
* 						checkFull() - Save Array if All coordinates obtained
*/		
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
/*
* 						clearMoveCoordinates()  Clear Coordinates Associated with onScroll
*/		
	public void clearMoveCoordinates()
	{
		float[] temp =  {-50, -50, -50, -50};
		moveCoordinates = temp;
	}
/*
* 						clearCoordinates()  Clear dot Coordinates
*/			
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
	        		lineColor.setStrokeWidth(40);
	        		lineColor.setStrokeCap(Paint.Cap.ROUND);
	        		canvas.drawLine(moveCoordinates[0], moveCoordinates[1], moveCoordinates[moveCoordinates.length-2], moveCoordinates[moveCoordinates.length-1], lineColor);
	        		clearMoveCoordinates();
	        	}
	        	else if(type == "Circ")
	        	{
	        		circColor.setColor(chosenColor);
	        		circColor.setStyle(Style.STROKE);
	        		circColor.setStrokeWidth(30);
	        		canvas.drawCircle(circleCoordinates[0], circleCoordinates[1], circleCoordinates[2], circColor);
	        		clearMoveCoordinates();
	        	}
	        	type = "";
	        }
	   }
}


