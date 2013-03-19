package amazenite.lockit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {
	
    @Override
    public void onReceive(final Context context, final Intent intent)
    {
    	/*Intent lockIntent = new Intent();  
        lockIntent.setClass(context, LockScreen.class);
        lockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
        
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
        	Log.d("hey", "hey");
        	Intent intent11 = new Intent(context,LockScreen.class);
            intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent11);
     
      }
        
       /* Boolean lockOn = Constants.LOCKSCREEN_SETTING;
        if(lockOn){
	        context.startActivity(lockIntent);
        }
        else 
        {
        	//don't start the service!
        }  */
    }
}


