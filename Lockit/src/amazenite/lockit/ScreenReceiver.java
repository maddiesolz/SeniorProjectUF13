package amazenite.lockit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {
	public static boolean wasScreenOn = true;
	
    @Override
    public void onReceive(final Context context, final Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
        	
        	Intent intent11 =  null;
        	if(Constants.LOCKSCREEN_SETTING && !wasScreenOn)
        	{
        		Constants.showScreenLock = true;
        	//	intent11 = new Intent(Intent.ACTION_MAIN, null);
                 intent11 = new Intent(context,LockScreen.class);
            	intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            	intent11.addCategory(Intent.CATEGORY_HOME);
                context.startActivity(intent11);
                Constants.showScreenLock = false;
        	}
        	wasScreenOn = true;
        }
        else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
        {
			Constants.inOpenImages = false;
        	wasScreenOn = false;
        }

    }
}


