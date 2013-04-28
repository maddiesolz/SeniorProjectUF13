package amazenite.lockit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Lockit extends Activity {	
	Spinner NumberofGestures;
	boolean enabled	= false;
	boolean visible = true;
	int chosenColor = 0xff33CCCC;

	
	/** Called when the user clicks the get image button */
	public void viewPictures(View view) {
	    // Do something in response to button
		Constants.inOpenImages = true;
		final Intent intent = new Intent(this, OpenImages.class);
		startActivity(intent);
	}
	
	/** Lockscreen Test Points */
	public void debugLockscreen(View view) {
	    // debug the lockscreen
		Constants.inTestPic = true;
		if(Constants.picPasswordSet == false && Constants.inTestPic)
		{
			final Toast toast = Toast.makeText(getApplicationContext(), "Please Set Gestures!", Toast.LENGTH_SHORT);
			toast.show();
		}
		else
		{
			final Intent intent = new Intent(this, LockScreen.class);
			startActivity(intent);
		}
	}
	
	/** Lockscreen Gesture Visibility */
	public void toggleVisible(View view) {
	    // toggle gesture visibility
		visible = !visible;
		Constants.gestureVisibility = visible;
  		ImageView img = (ImageView) findViewById(R.id.visiblebutton);
  		 if(Constants.gestureVisibility)
	     {
  			img.invalidate();
  			img.setImageResource(R.drawable.setvisible2);
	     }
	     else
	     {
	    	 img.setImageResource(R.drawable.setvisible);
	     }
	}
	
	/** Lockscreen Gesture Color Picker */
	public void setColors(View view){
		final Intent intent = new Intent(this, ColorSelection.class);
		startActivity(intent);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		overridePendingTransition(0,0);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		addItemsToSpinner();
		addListenerOnSpinnerItemSelection();
		getPreview();
		overridePendingTransition(0,0);
		visible = Constants.gestureVisibility;
		enabled = Constants.LOCKSCREEN_SETTING;
		chosenColor = Constants.gestureColor;
		NumberofGestures.setSelection(Constants.gestureCount-2);
	}
	
	public void getPreview(){
		//pictureSettings();
	    ImageView img = (ImageView) findViewById(R.id.preview);
	    if(img != null)
	    {
		    File file = getBaseContext().getFileStreamPath("lockimg");
		    String internalPath = "data/data/files/lockimg";
		    if (file.exists()) {
		    	 internalPath = file.getAbsolutePath();
		        	Drawable d = Drawable.createFromPath(internalPath);
		         if(d!=null)
		         {
		        	 img.setImageDrawable(d);
		        	 img.invalidate();
		         }
		    }
		    else{
		    	Toast.makeText(Lockit.this, "" + "Unable To Find File", Toast.LENGTH_SHORT).show();
		    	Vector<Integer> defaultPic = new Vector<Integer>();
		    	defaultPic.add(R.drawable.ic_launcher);
		    	if(defaultPic.get(0) != null)
		    	{
			    	saveImage2(defaultPic, 0);
		    	}
		    }
	    }
    }
	
	public void setPoints(View view)
	{
		Intent intent = new Intent(this, SetPoints.class);
		startActivity(intent);
	}
	
	 public void saveImage2(Vector<Integer> images, int num)
	    {
	    	BitmapFactory.Options o = new BitmapFactory.Options();
		    o.inJustDecodeBounds = true;
	        final int size = 70;
	        int scale = 2;
	        while(o.outWidth/scale/2 >= size && o.outHeight/scale/2 >= size)
	        {
	        	scale *=2;
	        }
	     	BitmapFactory.Options o2 = new BitmapFactory.Options();
	     	o2.inSampleSize=scale;
	     	if(images.get(num) == null)
	     	{
	     		Log.d("open images", "image null");
	     	}
	     	Bitmap samplePic = BitmapFactory.decodeResource(getResources(), images.get(num), o2);
	 		if(samplePic != null)
	 		{
		 		 try {
			        	FileOutputStream fos = openFileOutput("lockimg", Context.MODE_PRIVATE);
			        	samplePic.compress(CompressFormat.JPEG, 100, fos);
			        	try {
			        		fos.close();
			        		fos = null;
			        	} 
			        	catch (IOException e) {
			        		e.printStackTrace();
			        	}
			        	samplePic.recycle();
		        	} 
			        catch (FileNotFoundException e1) {
			        	e1.printStackTrace();
				  }
	 		}
	}
	    
	public void enablePicPw(View view)
	{
  		ImageView img2 = (ImageView) findViewById(R.id.enablePicturePassword);

		
		if(!Constants.picPasswordHasTested)
		{
	    	Toast.makeText(Lockit.this, "Please Test Your Gestures Before Enabling!" ,Toast.LENGTH_SHORT).show();
		}
		else
		{
			enabled = !enabled;
			Constants.LOCKSCREEN_SETTING = enabled;
			
			if(enabled)
			{
				startService(new Intent(this, myService.class));
			}
			else
			{
	            stopService(new Intent(this, myService.class));
			}
		}
		
		 if(Constants.LOCKSCREEN_SETTING && Constants.picPasswordHasTested)
	     {
  			img2.invalidate();
  			img2.setImageResource(R.drawable.enable2);
	     }
	     else
	     {
	    	 img2.setImageResource(R.drawable.enable);
	     }  

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lockit);
		addItemsToSpinner();
		addListenerOnSpinnerItemSelection();
		getPreview();
		visible = Constants.gestureVisibility;
		enabled = Constants.LOCKSCREEN_SETTING;
		chosenColor = Constants.gestureColor;
		NumberofGestures.setSelection(Constants.gestureCount-2);
		ImageView img = (ImageView) findViewById(R.id.pictureSettings);
	  	img.invalidate();
	  	img.setImageResource(R.drawable.picture);
  		ImageView img2 = (ImageView) findViewById(R.id.enablePicturePassword);
  		ImageView img3 = (ImageView) findViewById(R.id.visiblebutton);
  		 if(Constants.LOCKSCREEN_SETTING && Constants.picPasswordHasTested)
		     {
	  			img2.invalidate();
	  			img2.setImageResource(R.drawable.enable2);
		     }
		     else
		     {
		    	 img2.setImageResource(R.drawable.enable);
		     }  
 		 if(Constants.gestureVisibility)
	     {
  			img3.invalidate();
  			img3.setImageResource(R.drawable.setvisible2);
	     }
	     else
	     {
	    	 img3.setImageResource(R.drawable.setvisible);
	     }
		
		
	}
	
	public void addItemsToSpinner()
	{
		NumberofGestures = (Spinner) findViewById(R.id.spinner);
		List<String> list = new ArrayList<String>();
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
		R.layout.spinner_view, list);
		dataAdapter.setDropDownViewResource

		(android.R.layout.simple_spinner_dropdown_item);
		NumberofGestures.setAdapter(dataAdapter);
	}
	
	  public void addListenerOnSpinnerItemSelection() {
		  NumberofGestures = (Spinner) findViewById(R.id.spinner);
		  NumberofGestures.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	  }
	  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_lockit, menu);
		return true;
	}
	
	public void voiceSettings(View view)
	{
		
		final Intent intent = new Intent(this, VoiceSettings.class);
		startActivity(intent);
	}
	
    @Override
    public void onBackPressed() 
    {
    		finish();
    		return;
    }  
}
