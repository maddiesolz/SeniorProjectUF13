package amazenite.lockit;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
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
	           /* KeyguardManager.KeyguardLock k1;

	                    //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

	            KeyguardManager km =(KeyguardManager)getSystemService(KEYGUARD_SERVICE);
	            k1= km.newKeyguardLock("IN");
	            k1.disableKeyguard();*/

	            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
	            filter.addAction(Intent.ACTION_SCREEN_OFF);
	            filter.addAction(Intent.ACTION_USER_PRESENT);
	            
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



