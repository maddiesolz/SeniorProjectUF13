package amazenite.lockit;

import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class OpenImages extends Activity {

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_open_images);

	        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	            // Show the Up button in the action bar.
	            getActionBar().setDisplayHomeAsUpEnabled(true);
	        }
	        
	        GridView gridview = (GridView) findViewById(R.id.GridView1);
	        gridview.setAdapter(new ImageAdapter(this));

	        gridview.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	                Toast.makeText(OpenImages.this, "" + position, Toast.LENGTH_SHORT).show();
	            }
	        });
	    }

	    public class ImageAdapter extends BaseAdapter {
	        private Context mContext;

	        public ImageAdapter(Context c) {
	            mContext = c;
	        }

	        public int getCount() {
	            return mThumbIds.length;
	        }

	        public Object getItem(int position) {
	            return null;
	        }

	        public long getItemId(int position) {
	            return 0;
	        }

	        // create a new ImageView for each item referenced by the Adapter
	        public View getView(int position, View convertView, ViewGroup parent) {
	            ImageView imageView;
	            if (convertView == null) {  // if it's not recycled, initialize some attributes
	                imageView = new ImageView(mContext);
	                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
	                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	                imageView.setPadding(8, 8, 8, 8);
	            } else {
	                imageView = (ImageView) convertView;
	            }

	            imageView.setImageResource(mThumbIds[position]);
	            return imageView;
	        }

	        // references to our images
	        private Integer[] mThumbIds = {
	                R.drawable.rainbow, R.drawable.rainbow,
	                R.drawable.rainbow, R.drawable.rainbow,
	                R.drawable.rainbow, R.drawable.rainbow,
	                R.drawable.rainbow, R.drawable.rainbow,
	                R.drawable.rainbow, R.drawable.rainbow,
	                R.drawable.rainbow, R.drawable.rainbow,
	                R.drawable.rainbow, R.drawable.rainbow,
	                R.drawable.rainbow, R.drawable.rainbow,
	                R.drawable.rainbow, R.drawable.rainbow,
	                R.drawable.rainbow, R.drawable.rainbow,
	                R.drawable.rainbow, R.drawable.rainbow
	                
	        };
	    /*
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case android.R.id.home:
	            NavUtils.navigateUpFromSameTask(this);
	            return true;
	        }
	        return super.onOptionsItemSelected(item);
	    }*/
}
}

