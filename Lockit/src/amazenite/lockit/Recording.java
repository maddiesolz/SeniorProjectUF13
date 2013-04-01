package amazenite.lockit;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.AudioRecord.OnRecordPositionUpdateListener;
import android.os.Bundle;
import android.util.Log;

public class Recording extends Activity {
  private int mAudioBufferSize;
  private int mAudioBufferSampleSize;
  private AudioRecord mAudioRecord;
  private boolean inRecordMode = false;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initAudioRecord();
  }


  @Override
  public void onResume() {
    super.onResume();
    inRecordMode = true;
    Thread t = new Thread(new Runnable() {

      @Override
      public void run() {
        getSamples();
      }
    });
    t.start();
  }

  protected void onPause() {
    inRecordMode = false;
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

    public void onPeriodicNotification(AudioRecord recorder) {
      }

        public void onMarkerReached(AudioRecord recorder) {
        inRecordMode = false;
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
    
  private void getSamples() {
    if(mAudioRecord == null) return;
    short[] audioBuffer = new short[mAudioBufferSampleSize];
    mAudioRecord.startRecording();
    int audioRecordingState = mAudioRecord.getRecordingState();
    if(audioRecordingState != AudioRecord.RECORDSTATE_RECORDING) {
      finish();
    }
    while(inRecordMode) {
        int samplesRead = mAudioRecord.read(audioBuffer, 0, mAudioBufferSampleSize);
    }
    mAudioRecord.stop();
  }}
