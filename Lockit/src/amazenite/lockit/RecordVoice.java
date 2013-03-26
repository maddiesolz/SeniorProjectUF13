package amazenite.lockit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.media.MediaPlayer;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RecordVoice extends Activity
{
 	private GestureDetectorCompat mDetector;
 	private ExtAudioRecorder audioRecorder;
	private String mFileName = "";
	private MediaPlayer mPlayer = null;
	private boolean isRecording = false;
	private boolean hasRecorded = false;
    private boolean isScrolling = false;
	private byte[] wavArray = new byte[1000];
	private byte[] initialArray = new byte[1000];
	private byte[] testArray = new byte[1000];


    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_voice);
		audioRecorder = ExtAudioRecorder.getInstanse(true);	  
		mDetector = new GestureDetectorCompat(this, new MyGestureListener());
	
	}

	 @Override 
	    public boolean onTouchEvent(MotionEvent event){ 
	        this.mDetector.onTouchEvent(event);
	        if(event.getAction() == MotionEvent.ACTION_UP) {
	            if(isScrolling ) {
	                isScrolling  = false;
	                startPlaying();
	            };
	        }
	        return super.onTouchEvent(event);
	    }
	
	public void startRecord()
	{
		 final Toast toast = Toast.makeText(getApplicationContext(), "start record", Toast.LENGTH_SHORT);
 	    toast.show();
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		if(Constants.voiceOriginalRecord)
		{
			mFileName += "/initialSource.wav";
			Constants.voiceOriginalRecord = true;
		}
		else
		{
			mFileName += "/testSource.wav";
		}
		audioRecorder.setOutputFile(mFileName);
		audioRecorder.prepare();
		audioRecorder.start();
	}
	
	public void stopRecord()
	{
		 final Toast toast = Toast.makeText(getApplicationContext(), "stop record", Toast.LENGTH_SHORT);
	 	    toast.show();
		audioRecorder.stop();
		audioRecorder.release();
		audioRecorder.reset();
		hasRecorded = true;
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		if(Constants.voiceOriginalRecord)
		{
			mFileName += "/initialSource.wav";
			convertWav(mFileName,"initial");
		}
		else
		{
			if(hasRecorded)
			{
				mFileName += "/testSource.wav";
				convertWav(mFileName,"test");
			}
		}
	}
	
	public void startPlaying() {
		 final Toast toast = Toast.makeText(getApplicationContext(), "play record", Toast.LENGTH_SHORT);
	 	    toast.show();
        mPlayer = new MediaPlayer();
        try {
        	mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
    		if(Constants.voiceOriginalRecord)
    		{
    			mFileName += "/initialSource.wav";
    		}
    		else
    		{
    			if(hasRecorded)
    			{
    				mFileName += "/testSource.wav";
    			}
    		}
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.d("media player", "prepare() failed");
        }
    }
	
	public void convertWav(String path, String type)
	{
		FileOutputStream fos;

		try {
			fos = new FileOutputStream(path);
			try {
				fos.write(wavArray);
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(type.equals("initial"))
		{
			initialArray = wavArray;
		}
		else
		{
			testArray = wavArray;
		}
	}
	
	/*******************************************************************************************
	 * 									MyGeatureListener Class
	 *******************************************************************************************/
	public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		  @SuppressLint("NewApi")
		@Override
		    public boolean onSingleTapConfirmed(MotionEvent event) {
			  if(!isRecording)
			  {
				  isRecording = true;
				  startRecord();
			  }
			  else
			  {
				  isRecording = false;
				  stopRecord();
			  }
			    
			    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		    	if( v.hasVibrator()) {
					 v.vibrate(50);
		    	}
		        return true;
		    }
	  			  
		  @Override
		  public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
		  {
			  isScrolling = true;
			  return true;
		  }		  
	 }
	

}
