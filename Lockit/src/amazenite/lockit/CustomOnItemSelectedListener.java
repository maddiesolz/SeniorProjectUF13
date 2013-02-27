
package amazenite.lockit;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
 
public class CustomOnItemSelectedListener implements OnItemSelectedListener {
 
  public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {	
		try {
			String number = parent.getItemAtPosition(pos).toString();
	    	FileOutputStream fos = parent.getContext().openFileOutput("numGestures", Context.MODE_PRIVATE);
	       
	        	try {
					fos.write((number).getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	try {
	        		fos.close();
	        		fos = null;
	        	} 
	        	catch (IOException e) {
	        		e.printStackTrace();
	        	}
	    	}
	    catch (FileNotFoundException e1) {
	    	e1.printStackTrace();
	    	}
  		}
 
  @Override
  public void onNothingSelected(AdapterView<?> arg0) {
	// TODO Auto-generated method stub
  }
 
}	  