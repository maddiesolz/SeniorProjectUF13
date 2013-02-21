package amazenite.lockit;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class OpenImages extends Activity {

private static final int SELECT_PICTURE = 1;   //For the Gallery
private String selectedImagePath;			   //For the Gallery

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        //Empty is if there are no pictures in the SDCard file
	           //final boolean empty = imageAdapt.empty;
	        final boolean empty = !(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED));
        	if(!empty)
        	{
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        	else
        	{
                saveImage2();
        	}
	    }
	    
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (resultCode == RESULT_OK) {
	            if (requestCode == SELECT_PICTURE) {
	                Uri selectedImageUri = data.getData();
	                selectedImagePath = getPath(selectedImageUri);
	                //Is it the correct image?
	     
	            	try {
						saveImage(selectedImagePath);
					} catch (IOException e) {
						e.printStackTrace();
					}
	            }
	        }
	        finish();
	    }
	    
	    public String getPath(Uri uri) {
	        String[] projection = { MediaStore.Images.Media.DATA };
	        Cursor cursor = managedQuery(uri, projection, null, null, null);
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        cursor.moveToFirst();
	        return cursor.getString(column_index);
	    }

	    public void saveImage2()
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
        	
        	Bitmap samplePic = BitmapFactory.decodeResource(getResources(), R.drawable.rainbow, o2);
    		if(samplePic != null)
    		{
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
	    
	    public void saveImage(String imagePath) throws IOException
	    {  
	    	Toast.makeText(OpenImages.this, "IN SAVE IMAGE", Toast.LENGTH_SHORT).show();
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
		        		e.printStackTrace();
		        	}
		        	chosenImage.recycle();
	        	} 
	        catch (FileNotFoundException e1) {
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

