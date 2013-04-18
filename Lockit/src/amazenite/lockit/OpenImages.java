package amazenite.lockit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

public class OpenImages extends Activity {
	
	 /** Called when the activity is first created. */
	 private Uri mImageCaptureUri;
	 private static final int PICK_FROM_CAMERA = 1;
	 private static final int CROP_FROM_CAMERA = 2;
	 private static final int PICK_FROM_FILE = 3;
	 private Bitmap photo = null;
	 private AlertDialog dialog;

	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
		    }
	    }
	 
	 @Override
	 public void onPause() 
		{
			 super.onPause();
			 if (ScreenReceiver.wasScreenOn && !Constants.inOpenImages) {
				 dialog.dismiss();
				 finish();
			 }
			 else
			 {
				 getPreview();
				 dialog.dismiss();
			 }
		}
		 
	 	
	 
		@Override
		public void onResume() 
		{
			super.onResume();
	        setContentView(R.layout.activity_lockit);
			getPreview();
			//crop image
		    final String [] items   = new String [] {"Take Picture with Camera", "Select Picture from Gallery"};    
		    ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
		    AlertDialog.Builder builder  = new AlertDialog.Builder(this);
		    //Empty is if there are no pictures in the SDCard file
		    //final boolean empty = imageAdapt.empty;
		    final boolean empty = !(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED));
		
			if(!empty)
			{
				//crop image
			   builder.setTitle("Select Image");
			   builder.setAdapter( adapter, new DialogInterface.OnClickListener() 
			   {
				   public void onClick( DialogInterface dialog, int item ) 
				   { //pick from camera
					    if (item == 0) 
					    {
					    	Intent intent   = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					    	mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
					    	"tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
					    	intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
				
						      try {
						      intent.putExtra("return-data", true);
						       startActivityForResult(intent, PICK_FROM_CAMERA);
						     } catch (ActivityNotFoundException e) {
						      e.printStackTrace();
						     }
					    } else { //pick from file
					    	Intent intent = new Intent();
					    	intent.setType("image/*");
					    	intent.setAction(Intent.ACTION_GET_CONTENT);
					    	Log.d("PICK FROM FILE", "WGTTTTTTTTTT" );
					    	startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
					    }
				   }
			   	} );
		
			   	dialog = builder.create();
			   	dialog.setOnKeyListener(new Dialog.OnKeyListener() {

		            @Override
		            public boolean onKey(DialogInterface arg0, int keyCode,
		                    KeyEvent event) {
		                // TODO Auto-generated method stub
		                if (keyCode == KeyEvent.KEYCODE_BACK) {
		                	Intent goBackMain = new Intent(OpenImages.this,Lockit.class);
							   finish();
			      		       startActivity(goBackMain); 
		                }
		                return true;
		            }
		        });
			  	dialog.show();
			  	dialog.setCanceledOnTouchOutside(false);
		    }
		    else
		    {
			    Toast.makeText(this, "Cannot find gallery", Toast.LENGTH_SHORT).show();
			}
		}
	 
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
	   super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 
	   switch(requestCode) 
	   { 
		  case PICK_FROM_CAMERA:
		   if(resultCode != 0){
		    doCrop();
		    Log.d("pick from camera", "do crop");
		   }
		   break;
		
		   case PICK_FROM_FILE: 
		   if(resultCode != 0){
		    mImageCaptureUri = imageReturnedIntent.getData();
		    Log.d("pick from file", "do crop");
		     doCrop();
		   }
		   break;      
		
		   case CROP_FROM_CAMERA: 
		   if(resultCode != 0)
		   {
			   Bundle extras = imageReturnedIntent.getExtras();
			    if (extras != null) 
			    {          
				    photo = extras.getParcelable("data");
				    try 
				    {
						saveImage(photo);
						getBaseContext().getFileStreamPath("coordinates").delete();
						finish();
				    }catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
		   }
		   default:
		   {
			   finish();
		   }
		   File f = new File(mImageCaptureUri.getPath());  
		    if (f.exists()) f.delete();
		    break;
	   }
	}

	// crop method
  private void doCrop() {
	  Log.d("in do crop", "indo crop");
	  final ArrayList<cropOption> cropOptions = new ArrayList<cropOption>();
	
	   Intent intent = new Intent("com.android.camera.action.CROP");
	   intent.setType("image/*");
	
	   List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );
	
	   int size = list.size();
	   if (size == 0) 
	   {         
		    Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();
		    return;
	   } 
		   else {
		   intent.setData(mImageCaptureUri);
		   intent.putExtra("outputX", 200);
		   intent.putExtra("outputY", 300);
		   intent.putExtra("aspectX", 2);
		   intent.putExtra("aspectY", 3);
		   intent.putExtra("scale", true);
		   intent.putExtra("return-data", true);
		
		    if (size == 1) {
			    Intent i   = new Intent(intent);
			    ResolveInfo res = list.get(0);
			    i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
			    startActivityForResult(i, CROP_FROM_CAMERA);
		   } else {
			     for (ResolveInfo res : list) {
				     final cropOption co = new cropOption();
				     co.title  = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
				     co.icon  = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
				     co.appIntent= new Intent(intent);
				     co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
				     cropOptions.add(co);
		    }
		
		    cropOptionAdapter adapter = new cropOptionAdapter(getApplicationContext(), cropOptions);
		    AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setTitle("Choose Crop App");
		    builder.setCancelable(true);
		    builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
		    public void onClick( DialogInterface dialog, int item ) {
		    	Log.d("pick from file", "do crop from camra");
		      startActivityForResult( cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
		     }
		    });
		    
		     AlertDialog alert = builder.create();
		     alert.show();
		   }
	}
 }
  
    public void saveImage(Bitmap image) throws IOException
    {  
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
}