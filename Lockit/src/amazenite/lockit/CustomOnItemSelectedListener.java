
package amazenite.lockit;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
 
public class CustomOnItemSelectedListener implements OnItemSelectedListener {
 
  public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {	
	  String number = parent.getItemAtPosition(pos).toString();
	  if(Integer.parseInt(number) != Constants.gestureCount)
	  {
		  Constants.gestureCount = Integer.parseInt(number);
		  Constants.picPasswordSet = false;
		  Constants.picPasswordHasTested = false;
	  }
  }
 
  @Override
  public void onNothingSelected(AdapterView<?> arg0) {
	// TODO Auto-generated method stub
  }
 
}	  