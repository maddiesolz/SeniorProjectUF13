package amazenite.lockit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.media.MediaRecorder;
import android.media.AudioRecord.OnRecordPositionUpdateListener;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class RecordVoice extends Activity{
	  private int mAudioBufferSize;
	  private int mAudioBufferSampleSize;
	  private AudioRecord mAudioRecord;
	  private boolean inRecordMode = false;
	  private GestureDetectorCompat mDetector;
 	  private boolean isScrolling = false;
      private boolean isRecording = false;
      private boolean hasRecorded = false;
      String filePath;

	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		  setContentView(R.layout.activity_record_voice); 

		mDetector = new GestureDetectorCompat(this, new MyGestureListener());
	    initAudioRecord();
	    checkIfHasRecorded();
	 }
	  
	  public void checkIfHasRecorded()
	  {
		  filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
		  filePath += "/initialSource.wav";
	
		    File file = new File(filePath);
		    if(file.exists())
		    {
		    	hasRecorded = true;
		    }
		    else
		    {
		    	final Toast toast = Toast.makeText(getApplicationContext(), "Please go back and set the voice password", Toast.LENGTH_SHORT);
		 	    toast.show();
		    }
		  
	  }

	  public void startRecord()
		{
	 	   inRecordMode = true;
	 	    
	 	   filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
	 	   if(Constants.inSetVoice)
			{
				filePath += "/initialSource.wav";
			}
			else
			{
				if(hasRecorded)
				{
					filePath += "/testSource.wav";
				}
			}
	 	    
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
	 	    
	 	  getSamples();
		}
		
		public void stopRecord()
		{
			final Toast toast = Toast.makeText(getApplicationContext(), "stop record", Toast.LENGTH_SHORT);
	 	    toast.show();
	 	    mAudioRecord.stop();
	 	    inRecordMode = false;
			performFFT();
		}
		
		static int sampleNumber = 50;
		public void performFFT()
		{
			String filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
    		if(Constants.inSetVoice)
    		{
    			filepath += "/initialSource.wav";
    		}
    		else
    		{
    			if(hasRecorded)
    			{
    				filepath += "/testSource.wav";
    			}
    		}
	 	    File file = new File(filepath);
		    final double[] highscores = new double[sampleNumber];
		    final double[] recordPoints = new double[sampleNumber];
	        final int bytesPerSample = 2; // As it is 16bit PCM
	 	    final double amplification = 100.0; // choose a number as you like
	 	    
	 	    try {
				t.join();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		try {
		 	  int  x = (int)(file.length());//in.read(myByteBuffer);
			  final byte[] byteArr = new byte[x];
			  	// Read the file into the short array.
		        InputStream input = new FileInputStream(file);
		        BufferedInputStream bis = new BufferedInputStream(input);
		        DataInputStream dataIn = new DataInputStream(bis);
		        int a = 0;
		        while (dataIn.available() > 0) {
		        	byteArr[a] = dataIn.readByte();
		        	a++;
		        }
		        
		        Thread t2 = new Thread(new Runnable() {
		 			   @Override
		 			   public void run() {
		 				    int arrSize = (int) Math.pow(2,(Math.floor(Math.log(byteArr.length)/Math.log(2))));//(int) Math.pow(2,Math.floor(Math.log(shortArr.length)));
			 				final double[] micBufferData = new double[byteArr.length];

						    for (int index = 0, floatIndex = 0; index < arrSize - bytesPerSample + 1; index += bytesPerSample, floatIndex++) {
				 	        double sample = 0;
				 	        for (int b = 0; b < bytesPerSample; b++) {
				 	            int v = byteArr[index + b];
				 	            if (b < bytesPerSample - 1 || bytesPerSample == 1) {
				 	                v &= 0xFF;
				 	            }
				 	            sample += v << (b * 8);
				 	        }
				 	        double sample32 = amplification * (sample / 32768.0);
				 	        micBufferData[floatIndex] = sample32;
				 	    }
			 				
		 			    final Complex[] data = new Complex[arrSize];
					    for (int c=0; c<arrSize; c++)
					    {
					        data[c] = new Complex(micBufferData[c], 0);
					    }
					    
					    Complex[] array = FFT.fft(data);    //calling FFT
	
					    for(int b = 0; b < sampleNumber; b++)
					    {
					    	RANGE[b] = (int)(arrSize/sampleNumber )* (b+1);
					    }
					    RANGE[sampleNumber-1] = arrSize-1;
					    
					    for (int freq = 0; freq < arrSize-1; freq++) {
			    		    //Get the magnitude:
			    		    double mag = array[freq].abs(); //+ 1;
			    		    //Find out which range we are in:
			    		    int index = getIndex(freq);

			    		    //Save the highest magnitude and corresponding frequency:
			    		    if (mag > highscores[index]) {
			    		        highscores[index] = mag;
			    		        recordPoints[index] = freq;
			    		    }
					    }
					    saveVoicePoints(highscores, recordPoints);
					   // getWavArray();
			    		//LOG the points 
			    		for (int c = 0; c < Constants.originalLocation.length/2; c++)
			    		{
			    		   Log.d("LAALLALALA", "LOCATION: "+Constants.originalLocation[c] + " MAG: "+Constants.originalMagnitude[c]);
			    		}

			    		for (int c = 0; c < Constants.testLocation.length/2; c++)
			    		{
			    		   Log.d("LAALLALALA2", "LOCATION: "+Constants.testLocation[c] + " MAG: "+Constants.testMagnitude[c]);
			    		}
			    		
			    		if(!Constants.inSetVoice)
						{
							compareFFTs();
						}
	 			      }
		 			});
					t2.start();

			}
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	    @SuppressWarnings("deprecation")
		public void startPlaying() {
			if(inRecordMode==false) {
				final Toast toast = Toast.makeText(getApplicationContext(), "play record", Toast.LENGTH_SHORT);
		 	    toast.show();
				int minBufferSize = AudioTrack.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);
				AudioTrack at =  new AudioTrack(AudioManager.STREAM_MUSIC, 8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize, AudioTrack.MODE_STREAM);
		 	    final int bufferSize = 8192;//8192;
		 	    String filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
	    		if(Constants.inSetVoice)
	    		{
	    			filepath += "/initialSource.wav";
	    		}
	    		else
	    		{
	    			if(hasRecorded)
	    			{
	    				filepath += "/testSource.wav";
	    			}
	    		}

	 	    	final byte[]s = new byte[bufferSize];
				// Write and play
		        int i = 0;
	 	        FileInputStream fin;
				try {
					fin = new FileInputStream(filepath);
					DataInputStream dis = new DataInputStream(fin);
		 	        at.play();
		 	        while((i = dis.read(s, 0, bufferSize)) > -1)
		 	        {
		 	            at.write(s, 0, i);
		 	        }
					    at.stop();
			 	        at.release();
				        dis.close();   
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	    
		double [] trimArray(double [] input, int newsize)
		{
		double result[] = new double[newsize];
		for(int i=0;i<newsize;i++) result[i] = input[i];
		return result;
		}

		public void compareFFTs()
		{
			Log.d("IN COMPAREFFT", ""+Constants.originalLocation.length);
			Log.d("IN COMPAREFFT", ""+Constants.testLocation.length);
			if(Constants.originalLocation.length == Constants.testLocation.length)
			{
				Log.d("IN COMPAREFFT", "");
				double sumOriginal = 0;
				double sumTestOriginal = 0;
				double[] originalMag = trimArray(Constants.originalMagnitude,Constants.originalMagnitude.length/2);
				double[] testMag = trimArray(Constants.testMagnitude,Constants.testMagnitude.length/2);
				int start = 0;
			

			for(int i = 0; i < 14; i++)	
			{
				if(Constants.originalMagnitude[i] < 1000 && Constants.testMagnitude[i] < 1000)
				{
					start = i;
					break;
				}		
			}
			Log.d("THAT STARTING POINT?",""+start);
			double sumAverage = 0;
				for(int i = start; i < Constants.originalMagnitude.length; i++)
				{
					sumOriginal = Constants.originalMagnitude[i]+sumOriginal;
					sumTestOriginal = Constants.testMagnitude[i]+sumTestOriginal;
					sumAverage+= (Constants.originalMagnitude[i]/Constants.testMagnitude[i]);
					
				}
				sumAverage = sumAverage/(Constants.originalMagnitude.length-start);
				double factor = sumAverage;//-1;
				Log.d("WHATS FACTOR",""+factor);
				if(sumOriginal > sumTestOriginal)
				{
					//factor = sumTestOriginal/sumOriginal;
					for(int i = 0; i < originalMag.length; i++)
					{
						if(factor <= 1)
						{	
							originalMag[i] = originalMag[i]*factor;
						}
						else
						{
							originalMag[i] = originalMag[i]/factor;
						}
					}
				}
				else
				{
					//factor = sumOriginal/sumTestOriginal;
					for(int i = 0; i < testMag.length; i++)
					{
						if(factor <= 1)
						{
							testMag[i] = testMag[i]*factor;
						}
						else
						{
							testMag[i] = testMag[i]/factor;
						}
					}
				}
				int totalSum = 0;
				for(int i = start; i < testMag.length; i++)
				{
					totalSum+= Math.abs(testMag[i]-originalMag[i]);
					Log.d("ABS SUM", ""+totalSum);
				}
				
				for(int i = 0; i < testMag.length; i++)
				{
					Log.d("TEST MAGNITUDE", ""+testMag[i]);
				}
				for(int i = 0; i < originalMag.length; i++)
				{
					Log.d("ORIGINAL MAGNITUDE", ""+originalMag[i]);
				}
				
	

			}
		}
	    
		public void saveVoicePoints(double[] highscore, double[] record)
		{
			if(Constants.inSetVoice)
			{
				 Constants.originalLocation = record;
				 Constants.originalMagnitude = highscore;
			}
			else
			{
				Constants.testLocation = record;
				 Constants.testMagnitude = highscore;
			}
		}
		
		public void getWavArray(){
			try {
				File file;
				if(Constants.inSetVoice)
				{
					 file = getBaseContext().getFileStreamPath("setVoice");
				}
				else
				{
					 file = getBaseContext().getFileStreamPath("testVoice");
				}
				Scanner sc = new Scanner(new File(file.getAbsolutePath()));
				String line = sc.nextLine();			
				String[] wavFile = line.split("\\s+");
				
					for(int i = 0; i < wavFile.length; i ++)
					{
						Log.d("sup", "" + wavFile[i]);
					}
			
			}
	        catch (FileNotFoundException e1) {
	        	e1.printStackTrace();
	        	}
		}
		
		//Find out in which range
		public static final int[] RANGE = new int[sampleNumber];
		public static int getIndex(int freq) {
		    int i = 0;
		    while(RANGE[i] < freq) i++;
		        return i;
		    }
		
	  @Override
	  public void onResume() {
	    super.onResume();
	    inRecordMode = false;
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
	  
	  private RandomAccessFile randomAccessWriter;
	  private void getSamples() {
	    if(mAudioRecord == null) return;
	    byte[] buffer = new byte[mAudioBufferSampleSize];
	    mAudioRecord.startRecording();
	    int audioRecordingState = mAudioRecord.getRecordingState();
	    if(audioRecordingState != AudioRecord.RECORDSTATE_RECORDING) {
	      finish();
	    }
	    while(inRecordMode)
	    {
	    	int samplesRead =  mAudioRecord.read(buffer, 0, mAudioBufferSampleSize);
		    try {
				randomAccessWriter.write(buffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	    }
	    
		try {
			randomAccessWriter.seek(0);
			randomAccessWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
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
    	
    	Thread t;
    	public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
    		  @SuppressLint("NewApi")
    		@Override
    		    public boolean onSingleTapConfirmed(MotionEvent event) {
    			  if(!isRecording)
    			  {
    				  isRecording = true;
    				  final Toast toast = Toast.makeText(getApplicationContext(), "start record", Toast.LENGTH_SHORT);
    			 	    toast.show();
    				   t = new Thread(new Runnable() {
    			 	      @Override
    			 	      public void run() {
    			 	        startRecord();
    			 	      }
    			 	    });
    			 	    t.start();
    				  //startRecord();
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
    	
        @Override
        public void onBackPressed() {
        	if(Constants.inSetVoice || Constants.inTestVoice)
        	{
	            Constants.inTestVoice = false;
	            Intent goBackMain = new Intent(RecordVoice.this,VoiceSettings.class);
	            startActivity(goBackMain); 
	            return;
        	}
        }
}
	 
	

	
	
