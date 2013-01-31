package amazenite.lockit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class OpenImages extends Activity {
private int pageNumber = 0;
private ImageAdapter imageAdapt;
private Vector<String> images;
public final static String PIC_PATH = "amazenite.lockit.MESSAGE";

	
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
	        
	        images = imageAdapt.getFiles();

	        gridview.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	                
	                if(images.get(position) != null)
	                {
	                	Toast.makeText(OpenImages.this, "" + images.get(position), Toast.LENGTH_SHORT).show();
	                	
	                	saveImage(images.get(position));
	                }      
	                
	                finish();
	            }
	        });
	        
	        ImageButton b = (ImageButton) findViewById(R.id.moreImagesButton);
	        b.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					imageAdapt.changePage();
					gridview.invalidateViews();
				}
	        });	  
	        
	    }
	    
	    public static int calculateInSampleSize(
	            BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {

	        // Calculate ratios of height and width to requested height and width
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);

	        // Choose the smallest ratio as inSampleSize value, this will guarantee
	        // a final image with both dimensions larger than or equal to the
	        // requested height and width.
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }

	    return inSampleSize;
	    }
	    
	    public void saveImage(String imagePath)
	    {
	    	ImageView img = new ImageView(OpenImages.this);
	    	
	    Drawable d = Drawable.createFromPath(imagePath);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath,options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options,80,80);
        options.inJustDecodeBounds = false;
        Bitmap b = BitmapFactory.decodeFile(imagePath, options);
        
    	Toast.makeText(OpenImages.this, "" + "NULL!!", Toast.LENGTH_SHORT).show();


        if (d!=null)
        {
        img.setImageResource(0);
        img.setImageDrawable(d);		                	

        	Toast.makeText(OpenImages.this, "" + "Not Null", Toast.LENGTH_SHORT).show();
        }
        
        /*******************************
         * Save Image in Internal Storage
         * *****************************/
        String FILENAME = "lockimg";
        if(imagePath.endsWith(".jpg")){
        	FILENAME = "lockimg.jpg";
        	
        }
        else if (imagePath.endsWith(".png")){
        	FILENAME = "lockimg.png";
        }
        else if (imagePath.endsWith(".gif")){
        	FILENAME = "lockimg.gif";
        }
        else{
        	Toast.makeText(OpenImages.this, "" + "Invalid image file extension", Toast.LENGTH_SHORT).show();
        }
    	Toast.makeText(OpenImages.this, "" + FILENAME, Toast.LENGTH_SHORT).show();

    //   final FileOutputStream fos;
        

        
		try {
		   FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
	        b.compress(CompressFormat.JPEG, 80, fos); 
	       

	     //   b.recycle();
	        try {
				fos.close();
				fos = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        b.recycle();
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
        }
        	    
	    /*
	    public void saveImage(String imagePath) throws IOException
	    {
	    	ImageView img = new ImageView(OpenImages.this);
	        
	        if(img != null){		  
	        	Toast.makeText(OpenImages.this, "Preview found", Toast.LENGTH_SHORT).show();
		        BitmapFactory.Options o = new BitmapFactory.Options();
		        o.inJustDecodeBounds = true;
		        Bitmap b = BitmapFactory.decodeFile(imagePath, o);
		        final int size = 70;
		        int scale = 1;
		        while(o.outWidth/scale/2 >= size && o.outHeight/scale/2 >= size)
		        {
		        	scale *=2;
		        }
	        	BitmapFactory.Options o2 = new BitmapFactory.Options();
	        	o2.inSampleSize=scale;
	        	Bitmap b2 = BitmapFactory.decodeFile(imagePath, o2);
		        if (b2!=null)
		        {
		        	img.setImageResource(0);
		        	img.setImageBitmap(b2);	  
		        }

		        String FILENAME = "lockimg";
		        if(imagePath.endsWith(".jpg")){
		        	FILENAME = "lockimg.jpg";
		        }
		        else if (imagePath.endsWith(".png")){
		        	FILENAME = "lockimg.png";
		        }
		        else if (imagePath.endsWith(".gif")){
		        	FILENAME = "lockimg.gif";
		        }
		        else{
		        	Toast.makeText(OpenImages.this, "" + "Invalid image file extension", Toast.LENGTH_SHORT).show();
		        }
		        
		        try {
		        	FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
		        	b2.compress(CompressFormat.JPEG, 100, fos);

			        	try {
			        		fos.close();
			        		fos = null;
			        	} 
			        	catch (IOException e) {
			        	// TODO Auto-generated catch block
			        		e.printStackTrace();
			        	}
		        	b.recycle();
		        	b2.recycle();
		        	} 
		        catch (FileNotFoundException e1) {
		        	// TODO Auto-generated catch block
		        	e1.printStackTrace();
		        	}
	        }
	    }
	    */
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

