package amazenite.lockit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class OpenImages extends Activity {

private static final int SELECT_PICTURE = 1;   //For the Gallery
private static final int REQUEST_CROP_ICON = 0;      //For Image Crop
private static final String TEMP_PHOTO_FILE = "temporary_holder.jpg";  
private Uri selectedImageUri;
private String selectedImagePath;			   //For the Gallery

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        //Empty is if there are no pictures in the SDCard file
	        //final boolean empty = imageAdapt.empty;
	        final boolean empty = !(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED));

        	if(!empty){
        	    Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
        	}
        	else
        	{
	    	    Toast.makeText(this, "Cannot find gallery", Toast.LENGTH_SHORT).show();
        	}	        
	    }
	    
	    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
	    	
	    	Intent intent = new Intent("com.android.camera.action.CROP");   //for image crop
	    	
	    	if (resultCode != RESULT_OK) return;	    	
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = imageReturnedIntent.getData();
                selectedImagePath = getPath(selectedImageUri);     
            	try {
					saveImage(selectedImagePath);
				} catch (IOException e) {
					e.printStackTrace();
				}
            	
                doCrop();              
            }
            else if(resultCode == REQUEST_CROP_ICON){
            	Log.d("HEY", "request crop icon");
            	Bundle extras = imageReturnedIntent.getExtras();
                try{
                    if (extras != null) {
                    	Log.d("hey", "image cropped");
                         Bitmap myImage = extras.getParcelable("data");
                         saveImage(myImage);
                         finish();
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
	    }
	    
	    
	    public void doCrop(){
	    	Intent intent = new Intent("com.android.camera.action.CROP");
	    	intent.setType("image/*");

	    	// Check if there is image cropper application installed.
	    	List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );

	    	int size = list.size();

	    	// If no cropper application is found, throw a message.
	    	if (size == 0) {            
	    	    Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();
	    	    return;

	    	// If there're cropper applications found, use the first
	    	} else {
	    	    // Specify image path and cropping parameters
	    	    intent.setData(selectedImageUri);
	    	    intent.putExtra("outputX", 0);
	    	    intent.putExtra("outputY", 0);
	    	    intent.putExtra("return-data", true);

	    	    Intent i = new Intent(intent);
	    	    ResolveInfo res = list.get(0);
	    	    i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
	    	    startActivityForResult(i, REQUEST_CROP_ICON);
	    	}
	    }
	    
	    private Uri getTempUri() {
	        return Uri.fromFile(getTempFile());
	    }

	    private File getTempFile() {

	        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
	            File file = new File(Environment.getExternalStorageDirectory(),TEMP_PHOTO_FILE);
	            try {
	                file.createNewFile();
	            } catch (IOException e) {}

	            return file;
	        } else {
	            return null;
	        }
	    }
	    
	    public String getPath(Uri uri) {
	        String[] projection = { MediaStore.Images.Media.DATA };
	        Cursor cursor = managedQuery(uri, projection, null, null, null);
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        cursor.moveToFirst();
	        return cursor.getString(column_index);
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
		        		e.printStackTrace();
		        	}
		        	chosenImage.recycle();
	        	} 
	        catch (FileNotFoundException e1) {
	        	e1.printStackTrace();
	        	}
	    }
	    
	    public void saveImage(Bitmap image) throws IOException
	    {  
	    	/*
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
        	*/
	        try {
	        	FileOutputStream fos = openFileOutput("lockimg", Context.MODE_PRIVATE);
	        	image.compress(CompressFormat.JPEG, 100, fos);
		        	try {
		        		fos.close();
		        		fos = null;
		        	} 
		        	catch (IOException e) {
		        		e.printStackTrace();
		        	}
		        	image.recycle();
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

