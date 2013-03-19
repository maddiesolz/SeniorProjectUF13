package amazenite.lockit;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.util.Log;
import android.media.MediaPlayer;

import java.io.IOException;

public class RecordVoice extends Activity
{
	ExtAudioRecorder audioRecorder;
	String mFileName = "";
	private MediaPlayer   mPlayer = null;
    
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_voice);
		audioRecorder = ExtAudioRecorder.getInstanse(true);
	}
	
	public void startRecord(View view)
	{
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
	    mFileName += "/audiorecordtest.wav";
		audioRecorder.setOutputFile(mFileName);
		audioRecorder.prepare();
		audioRecorder.start();
	}
	
	public void stopRecord(View view)
	{
		audioRecorder.stop();
		audioRecorder.release();
	}
	
	public void startPlaying(View view) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.d("media player", "prepare() failed");
        }
    }

}
