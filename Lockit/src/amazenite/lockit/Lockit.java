package amazenite.lockit;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
		    }
	    }
    }

	
	public void setPoints(View view)
	{
		Intent intent = new Intent(this, SetPoints.class);
		startActivity(intent);
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
