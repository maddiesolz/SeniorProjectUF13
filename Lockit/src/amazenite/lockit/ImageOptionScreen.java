package amazenite.lockit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class ImageOptionScreen extends Activity {
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_option_screen);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
        // Get the path from the intent
		Intent getPath = getIntent();
        String imagePath = getPath.getStringExtra(OpenImages.PIC_PATH);       
        
        ImageView img = (ImageView) findViewById(R.id.preview);
        
        if(img == null){
        	Toast.makeText(ImageOptionScreen.this, "" + "NULL!!", Toast.LENGTH_SHORT).show();

        }
        else{
        
        Drawable d = Drawable.createFromPath(imagePath);

        if (d!=null)
        {
        img.setImageResource(0);
        img.setImageDrawable(d);	                	
        	Toast.makeText(ImageOptionScreen.this, "" + "Not Null", Toast.LENGTH_SHORT).show();
        }
        }
        
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_image_option_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
