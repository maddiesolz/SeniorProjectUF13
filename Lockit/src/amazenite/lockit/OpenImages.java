package amazenite.lockit;

import java.io.File;
import java.util.ArrayList;


import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;


public class OpenImages extends Activity {
	
public class ImageAdapter extends BaseAdapter {
    	
    	private Context mContext;
    	ArrayList<String> SDCardImages = new ArrayList<String>();
    	
    	public ImageAdapter(Context c) {
    		mContext = c;	
    	}
    	
    	void add(String path){
    		SDCardImages.add(path);	
    	}

		@Override
		public int getCount() {
			return SDCardImages.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView myImageView;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	        	myImageView = new ImageView(mContext);
	        	myImageView.setLayoutParams(new GridView.LayoutParams(100, 100));
	        	myImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	        	myImageView.setPadding(8, 8, 8, 8);
	        } else {
	        	myImageView = (ImageView) convertView;
	        }

	        Bitmap sdImage = decodeSampledBitmapFromUri(SDCardImages.get(position), 220, 220);

	        myImageView.setImageBitmap(sdImage);
	        return myImageView;
		}
		
		public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
			
			Bitmap bm = null;
			// First decode with inJustDecodeBounds=true to check dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
		     
			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		     
			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			bm = BitmapFactory.decodeFile(path, options); 
		     
			return bm;  	
		}
		
		public int calculateInSampleSize(
				
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
			// Raw height and width of image
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;
			
			if (height > reqHeight || width > reqWidth) {
				if (width > height) {
					inSampleSize = Math.round((float)height / (float)reqHeight);   	
				} else {
					inSampleSize = Math.round((float)width / (float)reqWidth);   	
				}   
			}
			
			return inSampleSize;   	
		}

	}
    
    ImageAdapter myImageAdapter;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		getActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_images);
        
        GridView gridview = (GridView) findViewById(R.id.GridView1);
        myImageAdapter = new ImageAdapter(this);
        gridview.setAdapter(myImageAdapter);
        
        String ExternalStorageDirectoryPath = Environment
        		.getExternalStorageDirectory()
        		.getAbsolutePath();
        
        String targetPath = ExternalStorageDirectoryPath + "/DCIM/100MEDIA/";
        
        Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
        File targetDirector = new File(targetPath);
        
        File[] files = targetDirector.listFiles();
        for (File file : files){
        	myImageAdapter.add(file.getAbsolutePath());
        } 
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // This is called when the Home (Up) button is pressed
	            // in the Action Bar.
	            Intent parentActivityIntent = new Intent(this, Lockit.class);
	            parentActivityIntent.addFlags(
	                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
	                    Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(parentActivityIntent);
	            finish();
	            return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

}
