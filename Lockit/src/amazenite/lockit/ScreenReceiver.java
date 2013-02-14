package amazenite.lockit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
    	if( intent.getAction().equals(Intent.ACTION_USER_PRESENT))
    	{
	    	Intent lockIntent = new Intent();  
	        lockIntent.setClass(context, LockScreen.class);
	        lockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(lockIntent);
    	}
    }
}

