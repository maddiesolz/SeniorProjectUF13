package amazenite.lockit;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
public class myService  extends Service{
	      BroadcastReceiver mReceiver;
	      // Intent myIntent;
	      @Override
	      public IBinder onBind(Intent intent) {
	            // TODO Auto-generated method stub
	            return null;
	      }

	      @Override
	      public void onCreate() {

	            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
	            filter.addAction(Intent.ACTION_SCREEN_OFF);
	            filter.addAction(Intent.ACTION_USER_PRESENT);

	            Log.d("IS THE FREAKING SERICE STARTED OR WHAT","HUH!?");
	            
	            mReceiver = new ScreenReceiver();
	            registerReceiver(mReceiver, filter);
	            super.onCreate();
	      }
	      @Override
	      public void onStart(Intent intent, int startId) {
	            // TODO Auto-generated method stub
	            super.onStart(intent, startId);
	}

	      @Override
	      public void onDestroy() {
	            unregisterReceiver(mReceiver);
	            super.onDestroy();
	      }}



