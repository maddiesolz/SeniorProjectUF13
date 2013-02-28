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
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.support.v4.view.GestureDetectorCompat;
import android.widget.Toast;

public class LockScreen extends Activity {	
	private static float x = -50;
 	private static float y = -50;
 	private static float x2 = -50;
 	private static float y2 = -50;
 	private int counter;
 	private GraphicView graphView;
 	private GestureDetectorCompat mDetector; 
	private boolean correctGestures = true;
	private String type = "";
	private String[] numbers;
 	private float[] moveCoordinates = {-50, -50, -50, -50};
 	float[] circleCoordinates = new float[3];
 	private boolean isScrolling = false;
 	private boolean isVisible = true;
	
	@Override
	@SuppressLint("NewApi")
	//@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 x = -50;
		 y = -50;
		 x2 = -50;
		 y2 = -50;
		 graphView = new GraphicView(this);
		 counter = 0;
		
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
	    loadNumberGestures();
		getCoordinates();
		openStatus("visible","togVisible");  //status of visibility
		
		
	}
	
	public void openStatus(String Status,String fileName){
		
		String line = "";
		
		File file = getBaseContext().getFileStreamPath(fileName);
		Scanner sc;
		try {
			sc = new Scanner(new File(file.getAbsolutePath()));
			line = sc.nextLine();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(Status.equals("visible")){
			if(line.equals("true"))
			{
				isVisible = true;
		    }
			else
			{
				isVisible = false;
			}
		}
		
	}
	
	public void loadNumberGestures()
	{
		try {
			File file = getBaseContext().getFileStreamPath("numGestures");
			Scanner sc = new Scanner(new File(file.getAbsolutePath()));
			String line = sc.nextLine();
			int num = Integer.parseInt(line);
			if(num != 0)
			{
				numbers = new String[num];
				for(int i = 0; i < num; i++)
				{
					numbers[i] = "";
				}
			}
		}
        catch (FileNotFoundException e1) {
        	e1.printStackTrace();
        	numbers = new String[3];
        	}
	}
	
	public void getCoordinates(){
		try {
			File file = getBaseContext().getFileStreamPath("coordinates");
			Scanner sc = new Scanner(new File(file.getAbsolutePath()));
			String line = sc.nextLine();			
			String[] coordinatesFile = line.split("\\s+");
			if(coordinatesFile.length != numbers.length)
			{
				final Toast toast = Toast.makeText(getApplicationContext(), "Please set the correct number of gestures", Toast.LENGTH_SHORT);
				toast.show();
				finish();
			}
			else
			{
				for(int i = 0; i < coordinatesFile.length; i ++)
				{
					numbers[i] = coordinatesFile[i];
				}
			}
		}
        catch (FileNotFoundException e1) {
        	e1.printStackTrace();
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

	 /* Gesture Dectector Class To Only listen On The Ones We Want */	
	public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		  @Override
		    public boolean onSingleTapUp(MotionEvent event) {
				if(counter <= numbers.length-1) {
			        x = event.getRawX();
			        y = event.getRawY()-40.0f;
			        type = "Adot";
			        if(isVisible == true)
			        {
			        	graphView.invalidate();
			        }			    
				    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				    if( v.hasVibrator()) {
						 v.vibrate(35);
			    	}
				    String coordinates[] = numbers[counter].split(",");	
				    //Not a dot
				    if(!coordinates[0].equals(type))
			    	{
			    		correctGestures = false;
			    	}
				    else
				    {
				    	if((x > (Float.parseFloat(coordinates[1])+30) || (x <  (Float.parseFloat(coordinates[1])-30.0f))) || (y > (Float.parseFloat(coordinates[2])+30.0f)) || (y < (Float.parseFloat(coordinates[2])-30.0f))){
				    		correctGestures = false;
				    	}
				    }
			    	counter++;
			    	checkFinished(counter);
				}
			    return true;
		    }
		  
		  @Override
		  public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
		  {
			  isScrolling = true;
			  if(counter <= numbers.length-1 ) {
				  x = e1.getRawX();
				  y = e1.getRawY()-40.0f;
				  x2 = e2.getRawX();
				  y2 = e2.getRawY()-40.0f;
				    					    
			      storeMoveCoordinates();
			  }
			  return true;
		  }
	}
	
	public void storeMoveCoordinates()
	{
		 if(moveCoordinates[0] == -50)
		  {
			  moveCoordinates[0] = x;
			  moveCoordinates[1] = y;
		  }
		  //Array full
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
		boolean vertical = false;
		float slopeHalf = -1;
		float slopeEnd = -1;
		int halfway = moveCoordinates.length/2;
		//y coordinate
		if(halfway % 2 != 0)
		{
			halfway--;
		}
		if((moveCoordinates[halfway] - moveCoordinates[0] < 30 && moveCoordinates[halfway] - moveCoordinates[0] > -30) && moveCoordinates[moveCoordinates.length-2] - moveCoordinates[0] < 30 && moveCoordinates[moveCoordinates.length-2] - moveCoordinates[0] > -30)
		{
			vertical = true;
		}
		else
		{
			slopeHalf = (moveCoordinates[halfway+1] - moveCoordinates[1])/(moveCoordinates[halfway] - moveCoordinates[0]);
			slopeEnd = (moveCoordinates[moveCoordinates.length-1] - moveCoordinates[1])/(moveCoordinates[moveCoordinates.length-2] - moveCoordinates[0]);
		}
		
		if(slopeHalf-slopeEnd < 2 && slopeHalf-slopeEnd > -2 || vertical)
		{
			 type = "Line";			 
			 String coordinates[] = numbers[counter].split(",");
			 
			  //Not a line
			  if(!coordinates[0].equals(type))
			  {
				  correctGestures = false;
			  }
			  else
			  {
					if(x > (Float.parseFloat(coordinates[1])+30) || (x <  (Float.parseFloat(coordinates[1])-30.0f)) || (y > (Float.parseFloat(coordinates[2])+30.0f)) || (y < (Float.parseFloat(coordinates[2])-30.0f)) ||
						(x2 > (Float.parseFloat(coordinates[3])+30) || (x2 <  (Float.parseFloat(coordinates[3])-30.0f))) || (y2 > (Float.parseFloat(coordinates[4])+30.0f)) || (y2 < (Float.parseFloat(coordinates[4])-30.0f)))
					{
				    	correctGestures = false;
					}
					float slope = (y2-y)/(x2-x);
					if(slope - slopeEnd > .5 || slope - slopeEnd < -.5)
					{
						correctGestures = false;
					}
			  }

			  counter++;
			  if(isVisible == true)
			  {
				  graphView.invalidate();
			  }
			  checkFinished(counter);
		}
		else
		{
			type = "Circ";
			String coordinates[] = numbers[counter].split(",");
			circleCoordinates = circleCalc();
			if(!coordinates[0].equals(type))
			  {
				  correctGestures = false;
			  }
			else
			{
				if(x > (Float.parseFloat(coordinates[1])+30) || (x <  (Float.parseFloat(coordinates[1])-30.0f)) || (y > (Float.parseFloat(coordinates[2])+30.0f)) || (y < (Float.parseFloat(coordinates[2])-30.0f)))
				{
					correctGestures = false;
				}
				if(circleCoordinates[0] > (Float.parseFloat(coordinates[3])+30) || (circleCoordinates[0] <  (Float.parseFloat(coordinates[3])-30.0f)) || (circleCoordinates[1] > (Float.parseFloat(coordinates[4])+30.0f)) || (circleCoordinates[1] < (Float.parseFloat(coordinates[4])-30.0f)))
				{
					correctGestures = false;
				}
				if(circleCoordinates[2] > (Float.parseFloat(coordinates[5])+50) || (circleCoordinates[2] <  (Float.parseFloat(coordinates[5])-50.0f)))
				{
					correctGestures = false;
				}
				
			}
			if(isVisible == true)
			{
				graphView.invalidate();
			}
			checkFinished(counter);
		}		
	}
	
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
	
	public void clearMoveCoordinates()
	{
		float[] temp =  {-50, -50, -50, -50};
		moveCoordinates = temp;
	}
	
	public void checkFinished(int counter)
	{
		if(counter >= numbers.length)
		  { 
    			if(correctGestures)
    			{
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
    				this.counter = 0;  
    				correctGestures=true;
		    		final Toast toast = Toast.makeText(getApplicationContext(), "Incorrect Password! Please try again.", Toast.LENGTH_SHORT);
		    	    toast.show();
    				Handler handler = new Handler();
    		        handler.postDelayed(new Runnable() {
    		           @Override
    		           public void run() {
    		        	   type = "";
    		        	   graphView.invalidate();
    		               toast.cancel(); 
    		           }
    		        }, 1000);
    			}
		  }
	}

	//Draws the points on screen
	 public class GraphicView extends View{		  
		  Paint dotColor = new Paint(Paint.ANTI_ALIAS_FLAG);
		  Paint lineColor = new Paint(Paint.ANTI_ALIAS_FLAG);
		  Paint circColor = new Paint(Paint.ANTI_ALIAS_FLAG);
		  
	        public GraphicView(Context context){
	            super(context);
	            setFocusable(true);
	        }

	        @Override
	        public void onDraw(Canvas canvas) {
	        	if(type == "")
	        	{
	        		canvas.drawCircle(-50, -50, 20, dotColor);
	        	}
	        	if(type == "Adot")
	        	{
	        		dotColor.setColor(0xff33CCCC);
		        	dotColor.setAlpha(80);
		        	dotColor.setStyle(Paint.Style.FILL);
	        		canvas.drawCircle(x, y, 20, dotColor);
	        	}
	        	else if(type == "Line")
	        	{
	        		lineColor.setColor(0xff33CCCC);
	        		lineColor.setAlpha(80);
	        		lineColor.setStyle(Paint.Style.FILL);
	        		lineColor.setStrokeWidth(20);
	        		lineColor.setStrokeCap(Paint.Cap.ROUND);
	        		canvas.drawLine(moveCoordinates[0], moveCoordinates[1], moveCoordinates[moveCoordinates.length-2], moveCoordinates[moveCoordinates.length-1], lineColor);
	        		clearMoveCoordinates();
	        	}
	        	else if(type == "Circ")
	        	{
	        		circColor.setColor(0xff33CCCC);
	        		circColor.setAlpha(80);
	        		circColor.setStyle(Style.STROKE);
	        		circColor.setStrokeWidth(30);
	        		canvas.drawCircle(circleCoordinates[0], circleCoordinates[1], circleCoordinates[2], circColor);
	        		clearMoveCoordinates();
	        	}
	        	type = "";
	        }	          
	   }
}

