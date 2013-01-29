package amazenite.lockit;

import java.io.File;
import java.util.Vector;

import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

public class OpenImages extends Activity {
private int pageNumber = 0;
private ImageAdapter imageAdapt;
private Vector<String> images;

	
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
	                
	                //Need to do save file
	                
	                //Look at beanstalk SDCardListAdapter and onitemclicklistener
	                
	                if(images.get(position) != null)
	                {
	                	Toast.makeText(OpenImages.this, "" + images.get(position), Toast.LENGTH_SHORT).show();
	                	SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
	                	SharedPreferences.Editor editor = sharedPref.edit();
	                	editor.putString(getString(R.string.image_location), images.get(position));
	                	editor.commit();
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

