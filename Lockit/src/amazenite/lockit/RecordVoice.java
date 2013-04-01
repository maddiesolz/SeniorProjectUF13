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
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.AudioRecord.OnRecordPositionUpdateListener;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RecordVoice extends Activity{
	private MediaPlayer mPlayer = null;

 	/*
 	private ExtAudioRecorder audioRecorder;
	private String mFileName = "";
	private MediaPlayer mPlayer = null;
	private boolean isRecording = false;
	private boolean hasRecorded = false;
    private boolean isScrolling = false;
	private byte[] initialArray = new byte[30];
	private byte[] testArray = new byte[300];


    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_voice);
		audioRecorder = ExtAudioRecorder.getInstanse(false);	  
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
			mFileName += "/testSource.wav";
			convertWav(mFileName,"test");
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
            
           if(!(mPlayer.isPlaying()))
            		{
            mPlayer.release();
            mPlayer.reset();
            		}

        } catch (IOException e) {
            Log.d("media player", "prepare() failed");
        }
    }
	
	public void convertWav(String path, String type)
	{
		/*FileInputStream fis;

		try {
			fis = new FileInputStream(path);
			try {
				if(type.equals("initial"))
				{
					fis.read(initialArray);
					fis.close();
				}
				else {
					fis.read(testArray);
					fis.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	/*	ByteArrayOutputStream out = new ByteArrayOutputStream();
		BufferedInputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream(path));
			int read;
			byte[] buff = new byte[30];
			try {
				while ((read = in.read(buff)) > 0)
				{
				    out.write(buff, 0, read);
				}
				out.flush();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			initialArray = out.toByteArray();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} ssjdklfjlskd

		
		Log.d("HOW LONG IS IT? ", ""+initialArray.length);
		for(int i = 0; i < initialArray.length; i ++)
		{
			Log.d("whats in init", "" + initialArray[i]);
		}
	}
	*/
	

	  private int mAudioBufferSize;
	  private int mAudioBufferSampleSize;

	  private AudioRecord mAudioRecord;
	  private boolean inRecordMode = false;
	 	private GestureDetectorCompat mDetector;
 	    private boolean isScrolling = false;
        private boolean isRecording = false;
        private AudioTrack audioTrack;




	  

	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		mDetector = new GestureDetectorCompat(this, new MyGestureListener());
	    initAudioRecord();
	 }

	  public void startRecord()
		{
			final Toast toast = Toast.makeText(getApplicationContext(), "start record", Toast.LENGTH_SHORT);
	 	    toast.show();
	 	    inRecordMode = true;
	 	    
	 	    filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
		    filePath += "/initialSource.wav";
		    
		    File file = new File(filePath);
		    if(file.exists())
		    {
		    	file.delete();
		    }
			try {
				randomAccessWriter = new RandomAccessFile(filePath, "rw");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 	    
	 	   Thread t = new Thread(new Runnable() {
	 	      @Override
	 	      public void run() {
	 	        getSamples();
	 	      }
	 	    });
	 	    t.start();
		}
		
		public void stopRecord()
		{
			 final Toast toast = Toast.makeText(getApplicationContext(), "stop record", Toast.LENGTH_SHORT);
		 	    toast.show();
		 	    inRecordMode = false;
			    mAudioRecord.stop();
		}
		
		public void startPlaying() {
			if(inRecordMode==false) {
			 final Toast toast = Toast.makeText(getApplicationContext(), "play record", Toast.LENGTH_SHORT);
		 	    toast.show();
		 	    
		 	    
		 	    int minBufferSize = AudioTrack.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);
		 	    int bufferSize = 512;
		 	    AudioTrack at = new AudioTrack(AudioManager.STREAM_MUSIC, 8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize, AudioTrack.MODE_STREAM);
		 	    String filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
		 	    File file = new File(filepath + "/initialSource.wav");
		 	    int i = 0;
		 	    byte[]s = new byte[bufferSize];
		 	    try {
		 	        FileInputStream fin = new FileInputStream(filepath + "/initialSource.wav");
		 	        DataInputStream dis = new DataInputStream(fin);
		 	       

		 	        at.play();
		 	        while((i = dis.read(s, 0, bufferSize)) > -1){
		 	            at.write(s, 0, i);

		 	        }
		 	         
		 	        at.stop();
		 	        at.release();
		 	        dis.close();
		 	        fin.close();

		 	    } catch (FileNotFoundException e) {
		 	        // TODO
		 	        e.printStackTrace();
		 	    } catch (IOException e) {
		 	        // TODO
		 	        e.printStackTrace();
		 	    }       
	
		 	   double[] micBufferData = new double[bufferSize];
		 	    final int bytesPerSample = 2; // As it is 16bit PCM
		 	    final double amplification = 100.0; // choose a number as you like
		 	    for (int index = 0, floatIndex = 0; index < s.length - bytesPerSample + 1; index += bytesPerSample, floatIndex++) {
		 	        double sample = 0;
		 	        for (int b = 0; b < bytesPerSample; b++) {
		 	            int v = s[index + b];
		 	            if (b < bytesPerSample - 1 || bytesPerSample == 1) {
		 	                v &= 0xFF;
		 	            }
		 	            sample += v << (b * 8);
		 	        }
		 	        double sample32 = amplification * (sample / 32768.0);
		 	        micBufferData[floatIndex] = sample32;
		 	    }
		 	    
		 	    
			    
			    Complex[] data = new Complex[bufferSize];
			    for (int a=0; a<bufferSize; a++)
			    {
			        data[a] = new Complex(micBufferData[a], 0);
			    }
			    
			    Complex[] array = FFT.fft(data);    //calling FFT
			    double[] highscores = new double[10];
			    double[] recordPoints = new double[10];
		 
			    for (int freq = 0; freq < 512-1; freq++) {
			    		    //Get the magnitude:
			    		    double mag = array[freq].abs(); //+ 1;
			    		    Log.d("MAAGGGG??",""+mag);
			    		    //Find out which range we are in:
			    		    int index = getIndex(freq);
			    		 
			    		    //Save the highest magnitude and corresponding frequency:
			    		    if (mag > highscores[index]) {
			    		        highscores[index] = mag;
			    		        recordPoints[index] = freq;
			    		    }
			    		}
			    	 
			    		//LOG the points 
			    		for (int a = 0; a < recordPoints.length; a++) {
			    		    Log.d("LAALLALALA", ""+recordPoints[a]);
			    		    Log.d("BLBLBLBLBLBL", " " + highscores[a]);
			    		}	 
			}
	    }
		String filePath;
		
		//Find out in which range
		public static final int[] RANGE = new int[] {200,220,240,260,280,300,320,340,360, 512+1};
		public static int getIndex(int freq) {
		    int i = 0;
		    while(RANGE[i] < freq) i++;
		        return i;
		    }
		
	  @Override
	  public void onResume() {
	    super.onResume();
	    inRecordMode = false;
	    


	    /*Thread t = new Thread(new Runnable() {

	      @Override
	      public void run() {
	        getSamples();
	      }
	    });
	    t.start();*/
	  }

	  protected void onPause() {
	   // inRecordMode = false;
	    super.onPause();
	  }

	  @Override
	  protected void onDestroy() {
	    if(mAudioRecord != null) {
	      mAudioRecord.release();
	    }
	    super.onDestroy();
	  }

	    public OnRecordPositionUpdateListener mListener = new OnRecordPositionUpdateListener() {

	    public void onPeriodicNotification(AudioRecord recorder) 
	    {
	    }

	        public void onMarkerReached(AudioRecord recorder) {
	       // inRecordMode = false;
	        }
	    };

	  private void initAudioRecord() {
	    try {
	      int sampleRate = 8000;
	      int channelConfig = AudioFormat.CHANNEL_IN_MONO;
	      int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
	      mAudioBufferSize = 2 * AudioRecord.getMinBufferSize(sampleRate,
	          channelConfig, audioFormat);
	      mAudioBufferSampleSize = mAudioBufferSize / 2;
	      mAudioRecord = new AudioRecord(
	          MediaRecorder.AudioSource.MIC,
	          sampleRate,
	          channelConfig,
	          audioFormat,
	          mAudioBufferSize);
	    } catch (IllegalArgumentException e) {
	      e.printStackTrace();
	    }
	    
	    mAudioRecord.setNotificationMarkerPosition(10000);
	    mAudioRecord.setPositionNotificationPeriod(1000);
	    mAudioRecord.setRecordPositionUpdateListener(mListener);
	    
	    int audioRecordState = mAudioRecord.getState();
	    if(audioRecordState != AudioRecord.STATE_INITIALIZED) {
	      finish();
	    }
	  }
	  
	  	byte[] buffer = new byte[mAudioBufferSampleSize];
		private RandomAccessFile randomAccessWriter;

		
	  private void getSamples() {
	    if(mAudioRecord == null) return;
	    byte[] audioBuffer = new byte[mAudioBufferSampleSize];
	  //  short[] audioBuffer2 = new short[mAudioBufferSampleSize];
	    mAudioRecord.startRecording();
	    int audioRecordingState = mAudioRecord.getRecordingState();
	    if(audioRecordingState != AudioRecord.RECORDSTATE_RECORDING) {
	      finish();
	    }
	    while(inRecordMode) {
	        int samplesRead = mAudioRecord.read(audioBuffer, 0, mAudioBufferSampleSize);
	      //  int samplesRead2 = mAudioRecord.read(audioBuffer2, 0, mAudioBufferSampleSize);
	        try {
					randomAccessWriter.write(audioBuffer);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

	    }
	    buffer = audioBuffer;

	    

	   /* for(int i = 0; i < buffer.length; i ++)
	    {
	    	Log.d("audio buffer", "" + buffer[i]);
	    }
	    Log.d("size", "" + buffer.length);*/
	    
	    
	    
	/*	try {
			randomAccessWriter.seek(0);
			randomAccessWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	    
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
	 
	

	
	
