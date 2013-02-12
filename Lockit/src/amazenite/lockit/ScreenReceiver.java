package amazenite.lockit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
    	if( intent.getAction().equals(Intent.ACTION_USER_PRESENT))
    	{
	    	Intent lockIntent = new Intent();  
	        intent.setClass(context, LockScreen.class);
	        context.startActivity(lockIntent);      
			Log.d("hey", "hey");
    	}
    }
}

