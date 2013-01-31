package amazenite.lockit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class Lockit extends FragmentActivity {	
	
	/** Called when the user clicks the get image button */
	public void viewPictures(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, OpenImages.class);
		startActivity(intent);
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_lockit, menu);
		return true;
	}

}
