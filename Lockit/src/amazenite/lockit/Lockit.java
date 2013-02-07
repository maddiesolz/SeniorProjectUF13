package amazenite.lockit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import android.widget.ImageView;
import android.widget.Toast;

public class Lockit extends Activity {	
	
	/** Called when the user clicks the get image button */
	public void viewPictures(View view) {
	    // Do something in response to button
		final Intent intent = new Intent(this, OpenImages.class);
		startActivity(intent);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getPreview();
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
	    	Log.d("save images", "im in it");
	    	BitmapFactory.Options o = new BitmapFactory.Options();
	    	Log.d("save images", "bit mpa factory open");
		    o.inJustDecodeBounds = true;
		    Log.d("save images", "w/e this thing is");
	        final int size = 70;
	        int scale = 2;
	        while(o.outWidth/scale/2 >= size && o.outHeight/scale/2 >= size)
	        {
	        	scale *=2;
	        }
	        Log.d("save images", "did that");
     	BitmapFactory.Options o2 = new BitmapFactory.Options();
     	Log.d("save images", "made it");
     	o2.inSampleSize=scale;
     	if(images.get(num) == null)
     	{
     		Log.d("open images", "image null");
     	}
     	Bitmap samplePic = BitmapFactory.decodeResource(getResources(), images.get(num), o2); //THIS LINE OF CODE DOESN'T WORK!
 		if(samplePic != null)
 		{
 		Log.d("open Images", "Got the samplePic");
 		 try {
	        	FileOutputStream fos = openFileOutput("lockimg", Context.MODE_PRIVATE);
	        	samplePic.compress(CompressFormat.JPEG, 100, fos);
	        	Log.d("open images", "compressed it");
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
	    
	
	public void pictureSettings()
	{
		setContentView(R.layout.activity_lockit);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lockit);
		getPreview();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_lockit, menu);
		return true;
	}
}
