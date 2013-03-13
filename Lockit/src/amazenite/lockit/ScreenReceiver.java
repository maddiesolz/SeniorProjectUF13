package amazenite.lockit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {
	
    @Override
    public void onReceive(final Context context, final Intent intent)
    {
    	Intent lockIntent = new Intent();  
        lockIntent.setClass(context, LockScreen.class);
        lockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        
        Boolean lockOn = Constants.LOCKSCREEN_SETTING;
        if(lockOn){
	        context.startActivity(lockIntent);
        }
        else 
        {
        	//don't start the service!
        }  
    }
}


