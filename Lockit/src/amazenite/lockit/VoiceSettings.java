package amazenite.lockit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class VoiceSettings extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice_settings);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_voice_settings, menu);
		return true;
	}
	
	public void enableVoice(View view)
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
	
	public void pictureSettings(View view)
	{
		final Intent intent = new Intent(this, Lockit.class);
		startActivity(intent);
	}
	
	public void recordVoice(View view)
	{
		Constants.inSetVoice = true;
		Intent intent = new Intent(this, RecordVoice.class);
		startActivity(intent);
	}
	
	public void testVoice(View view)
	{
		Constants.inSetVoice = false;
		Constants.inTestVoice = true;
		Intent intent = new Intent(this, RecordVoice.class);
		startActivity(intent);
	}
	
    @Override
    public void onBackPressed() {
        Intent goBackMain = new Intent(VoiceSettings.this,Lockit.class);
        startActivity(goBackMain); 
        return;
    }   

}
