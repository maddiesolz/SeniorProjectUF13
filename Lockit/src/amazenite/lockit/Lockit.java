package amazenite.lockit;

import java.io.File;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Lockit extends FragmentActivity {	
	
	/** Called when the user clicks the get image button */
	public void viewPictures(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, OpenImages.class);
		startActivity(intent);
		getPreview();
		view.invalidate();
	}
	
	public void getPreview(){
		//pictureSettings();
	    ImageView img = (ImageView) findViewById(R.id.preview);
	    if(img == null){
	    	Toast.makeText(Lockit.this, "" + "img is null", Toast.LENGTH_SHORT).show();
	    }
	    else{
			//img.setImageResource(0);
		    File file = getBaseContext().getFileStreamPath("lockimg");
		    String internalPath = "data/data/files/lockimg";
		    if (file.exists()) {
		    	 internalPath = file.getAbsolutePath();
		        	Toast.makeText(Lockit.this, "" + internalPath, Toast.LENGTH_SHORT).show();
		         Drawable d = Drawable.createFromPath(internalPath);
		         if(d!=null){
		        	 Toast.makeText(Lockit.this, "" + "drawable not null", Toast.LENGTH_SHORT).show();
		        	 img.setImageDrawable(d);
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
