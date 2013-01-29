package amazenite.lockit;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private Vector<ImageView> SDCardImages;
    boolean empty = false;

    public ImageAdapter(Context c) {
        context = c;
        SDCardImages = new Vector<ImageView>();
        loadImages();
    }

    public void loadImages()
    {
    	ArrayList<Integer> picID = new ArrayList<Integer>();
    	int picNum = 0;
    	File picFolder = new File(Environment.getExternalStorageDirectory().getPath());
    	if (!picFolder.exists())
        {
    		empty = true;
        }
    	else
    	{
	    	File[] folderPics = picFolder.listFiles();
	    	if(folderPics != null)
	    	{
		    	String name = "";
		    	for(File singleFile : folderPics)
		    	{
		    		 name = singleFile.getName();
		    		 if(name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".gif"))
		    		 {
			    		 ImageView myImageView = new ImageView(context);
			    		 myImageView.setImageDrawable(Drawable.createFromPath(singleFile.getAbsolutePath()));
			    		 myImageView.setId(picNum);
			    		 picNum++;
			    		 picID.add(myImageView.getId());
			    		 SDCardImages.add(myImageView);
		    		 }
		    	}
	    	}
	    	else
	    	{
	    		empty = true;
	    	}
    	}
    	
    }
    
    public int getCount() {
    	int num = 0;
    	if(empty)
    	{
    		num= mThumbIds.length;
    	}
    	else
    	{
    		num= SDCardImages.size();
    	}
    	return num;
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
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(4, 4, 4, 4);
        } else {
            imageView = (ImageView) convertView;
        }
        if(empty)
        {
        	imageView.setImageResource(mThumbIds[position]);
        }
        else
        {
        	imageView.setImageDrawable(SDCardImages.get(position).getDrawable());
        }
        
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
}
