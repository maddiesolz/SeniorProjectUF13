package amazenite.lockit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class Lockit extends Activity {	
	
	private static Context context;
	
	/** Called when the user clicks the get image button */
	public void viewPictures(View view) {
	    // Do something in response to button
		final Intent intent = new Intent(this, OpenImages.class);
		startActivity(intent);
	}
	
	/** Lockscreen Debug */
	public void debugLockscreen(View view) {
	    // debug the lockscreen
		final Intent intent = new Intent(this, LockScreen.class);
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
		/*Window window = getWindow();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		*/
		
		BroadcastReceiver displayPicture = new BroadcastReceiver() {		    
		    @Override
			public void onReceive(Context arg0, Intent intent) {
		    	Intent lockIntent = new Intent();  
	            intent.setClass(context, LockScreen.class);
	            startActivity(lockIntent);      
				Log.d("hey", "hey");
			}
		};		
		registerReceiver(displayPicture, new IntentFilter(Intent.ACTION_USER_PRESENT));		
	}
	
	public void pictureSettings()
	{
		//
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
