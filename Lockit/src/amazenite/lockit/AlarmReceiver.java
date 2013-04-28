package amazenite.lockit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	
    @Override
    public void onReceive(final Context context, final Intent intent)
    {
    final Intent myIntent = new Intent(context, LockScreen.class);

    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
    Log.d("IS THE ALARM MANAGER WORKING?!","IS IT?");
    Thread t = new Thread(new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.d("ran it!!!!", "lol");
		    context.startActivity(myIntent);
		}
    });
	t.start();
    }
}
