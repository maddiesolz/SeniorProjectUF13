package amazenite.lockit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class VoiceSettings extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice_settings);
		ImageView img = (ImageView) findViewById(R.id.voiceSettings);
	  	img.invalidate();
	  	img.setImageResource(R.drawable.voice);
		ImageView img2 = (ImageView) findViewById(R.id.voiceEnable);
  		
 		 if(Constants.voiceSettingEnable && Constants.voicePassHasTested)
		     {
	  			img2.invalidate();
	  			img2.setImageResource(R.drawable.enable2);
		     }
		     else
		     {
		    	 img2.setImageResource(R.drawable.enable);
		     }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_voice_settings, menu);
		return true;
	}
	
	public void enableVoice(View view)
	{

		if(!Constants.voicePassHasTested || Constants.noMatch)
		{
		    Toast.makeText(VoiceSettings.this, "Please Test Your Voice Password Before Enabling!" ,Toast.LENGTH_SHORT).show();
		}
		else
		{
			Constants.voiceSettingEnable = !Constants.voiceSettingEnable;
			
			if(Constants.voiceSettingEnable)
			{
				startService(new Intent(this, myService.class));
			}
			else
			{
	            stopService(new Intent(this, myService.class));
			}
		}
		
		ImageView img2 = (ImageView) findViewById(R.id.voiceEnable);
  		
		 if(Constants.voiceSettingEnable && Constants.voicePassHasTested)
		     {
	  			img2.invalidate();
	  			img2.setImageResource(R.drawable.enable2);
		     }
		     else
		     {
		    	 img2.setImageResource(R.drawable.enable);
		     }

	}
	
	public void pictureSettings(View view)
	{
		final Intent intent = new Intent(this, Lockit.class);
		startActivity(intent);
	}
	
	public void recordVoice(View view)
	{
		Constants.inSetVoice = true;
		Constants.inTestVoice = false;
		Intent intent = new Intent(this, RecordVoice.class);
		startActivity(intent);
	}
	
	public void testVoice(View view)
	{
		Constants.voicePassVisible = false;
		if(!Constants.voicePassSet)
		{
	    	Toast.makeText(VoiceSettings.this, "Please Set Your Voice Password Before Testing!" ,Toast.LENGTH_SHORT).show();
		}
		else
		{
			Constants.inSetVoice = false;
			Constants.inTestVoice = true;
			Intent intent = new Intent(this, RecordVoice.class);
			startActivity(intent);
		}
	}
	
    @Override
    public void onBackPressed() {
        Intent goBackMain = new Intent(VoiceSettings.this,Lockit.class);
        startActivity(goBackMain); 
        return;
    }   
    
	@Override
	public void onPause() {
		super.onPause();
		overridePendingTransition(0,0);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		overridePendingTransition(0,0);
	}


}
