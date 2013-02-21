package amazenite.lockit;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


	public class TestGallery extends Activity {

	    private static final int SELECT_PICTURE = 1;

	    private String selectedImagePath;

	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_test_gallery);

	        ((Button) findViewById(R.id.button1))
	                .setOnClickListener(new OnClickListener() {

	                    public void onClick(View arg0) {

	                        // in onCreate or any event where your want the user to
	                        // select a file
	                        Intent intent = new Intent();
	                        intent.setType("image/*");
	                        intent.setAction(Intent.ACTION_GET_CONTENT);
	                        startActivityForResult(Intent.createChooser(intent,
	                                "Select Picture"), SELECT_PICTURE);
	                    }
	                });
	    }

	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (resultCode == RESULT_OK) {
	            if (requestCode == SELECT_PICTURE) {
	                Uri selectedImageUri = data.getData();
	                selectedImagePath = getPath(selectedImageUri);
			    	Toast.makeText(TestGallery.this, "" + selectedImagePath, Toast.LENGTH_SHORT).show();

	            }
	        }
	    }

	    public String getPath(Uri uri) {
	        String[] projection = { MediaStore.Images.Media.DATA };
	        Cursor cursor = managedQuery(uri, projection, null, null, null);
	        int column_index = cursor
	                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        cursor.moveToFirst();
	        return cursor.getString(column_index);
	    }

	}