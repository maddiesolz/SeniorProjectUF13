package amazenite.lockit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class ScreenReceiver extends BroadcastReceiver {
	
    @Override
    public void onReceive(Context context, Intent intent) {
    	
    	//if( intent.getAction().equals(Intent.ACTION_USER_PRESENT))
    	//{
	    	Intent lockIntent = new Intent();  
	        lockIntent.setClass(context, LockScreen.class);
	        lockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        
	     //   if(extras == null){
	        String enabled = intent.getStringExtra("status");//(String) intent.getExtras().get("status");//extras.getString("status");//intent.getStringExtra("status");


	        
	      //  Log.d("IS IT ENABLED OR NOT OK? ", enabled);
        		if(enabled.equals("true")){
			        context.startActivity(lockIntent);
        			}

    	}
    	
 }
    




