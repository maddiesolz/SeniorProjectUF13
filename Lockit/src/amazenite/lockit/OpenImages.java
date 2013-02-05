package amazenite.lockit;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

public class OpenImages extends Activity {
private int pageNumber = 0;
private ImageAdapter imageAdapt;
private Vector<String> images;
private Vector<Integer> images2;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_open_images);

	        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	            // Show the Up button in the action bar.
	            getActionBar().setDisplayHomeAsUpEnabled(true);
	        }
	        
	        final GridView gridview = (GridView) findViewById(R.id.GridView1);
	        imageAdapt = new ImageAdapter(this, pageNumber);
	        gridview.setAdapter(imageAdapt);
	        
	        //Empty is if there are no pictures in the SDCard file
	        final boolean empty = imageAdapt.empty;
	        if(!empty)
	        {
	        	images = imageAdapt.getFiles();
	        }
	        else
	        {
	        	images2 = imageAdapt.getFiles2();
	        }	        	
	        
	        gridview.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	                
	                if(!empty)
	    	        {
	    	        	images = imageAdapt.getFiles();
	    	        }
	    	        else
	    	        {
	    	        	images2 = imageAdapt.getFiles2();
	    	        }	        	
	    	        
	            	
	            	if(!empty)
	            	{
		                if(images.get(position) != null)
		                {
		               // 	Toast.makeText(OpenImages.this, "" + images.get(position), Toast.LENGTH_LONG).show();
		                //	Toast.makeText(OpenImages.this, "" + position, Toast.LENGTH_SHORT).show();

		                	try {
								saveImage(images.get(position));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		                }   
	            	}
	            	else
	            	{
	            		if(images2.get(position) != null)
	            		{
	            			Toast.makeText(OpenImages.this, "" + images2.get(position), Toast.LENGTH_SHORT).show();
		                	saveImage2(images2, position);
	            		}
	            	}
	                
	                finish();
	            }
	        });
	        
	        
	        if(imageAdapt.getFileCount() > 12){
		        final ImageButton b = (ImageButton) findViewById(R.id.moreImagesButton);
		        b.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						pageNumber++;
						imageAdapt.changePage();
					
						gridview.invalidateViews();
						if(imageAdapt.getCurrentCount())
						{
							b.setEnabled(false);
							b.setVisibility(View.GONE);
						}
					}
		        });	  
	        }
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
		        	// TODO Auto-generated catch block
		        		e.printStackTrace();
		        	}
	        	samplePic.recycle();
	        	} 
		        catch (FileNotFoundException e1) {
		        	// TODO Auto-generated catch block
		        	e1.printStackTrace();
		        }
    		}
	    }
	    
	    public void saveImage(String imagePath) throws IOException
	    {  
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        final int size = 70;
	        int scale = 2;
	        while(o.outWidth/scale/2 >= size && o.outHeight/scale/2 >= size)
	        {
	        	scale *= 2;
	        }
        	BitmapFactory.Options o2 = new BitmapFactory.Options();
        	o2.inSampleSize=scale;
        	Bitmap chosenImage = BitmapFactory.decodeFile(imagePath, o2);
	        try {
	        	FileOutputStream fos = openFileOutput("lockimg", Context.MODE_PRIVATE);
	        	chosenImage.compress(CompressFormat.JPEG, 100, fos);

		        	try {
		        		fos.close();
		        		fos = null;
		        	} 
		        	catch (IOException e) {
		        	// TODO Auto-generated catch block
		        		e.printStackTrace();
		        	}
		        	chosenImage.recycle();
	        	} 
	        catch (FileNotFoundException e1) {
	        	// TODO Auto-generated catch block
	        	e1.printStackTrace();
	        	}
	        
	    }
	    
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case android.R.id.home:
	        	finish();
	        	//NavUtils.navigateUpFromSameTask();
	            return true;
	        }
	        return false; //super.onOptionsItemSelected(item);
	    }

}

