package amazenite.lockit;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class LockScreen extends Activity {
	
	private float[] coordinates = {-1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_screen);
		Log.d("lockscreen", "in lockscreen");
		// Show the Up button in the action bar.
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		
		getCoordinates();
	}
	
	public void getCoordinates(){
					
        		try {
    				String space = " ";
    				FileInputStream fis = openFileInput("coordinates");
    		        for(int i = 0; i<coordinates.length; i++)
    		        {
    		        	try {
    						fis.read((space + Float.toString(coordinates[i])).getBytes());
    					} catch (IOException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
    		        }
    	        	try {
    		        		fis.close();
    		        		fis = null;
    		        	} 
    		        	catch (IOException e) {
    		        		e.printStackTrace();
    		        	}
    	        	} 
    	        catch (FileNotFoundException e1) {
    	        	e1.printStackTrace();
    	        	}	
			}
	
	
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_lock_screen, menu);
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
*/
}
