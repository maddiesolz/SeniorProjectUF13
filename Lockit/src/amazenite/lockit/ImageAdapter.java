package amazenite.lockit;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    //private Vector<ImageView> SDCardImages;
    private Vector<String> SDCardImages;
    private boolean empty = false;
    private int pageNumber;
    
    public ImageAdapter(Context c, int pageNumber) {
        context = c;
        //SDCardImages = new Vector<ImageView>();
        
        this.pageNumber = pageNumber;
        loadImages(pageNumber);
    }
    
    public static Bitmap decodeSampledBitmapFromFile(String imagePath, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, options);
    }
        public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }
        return inSampleSize;
    }
        
    public void changePage()
    {
    	pageNumber++;
    	loadImages(pageNumber);
    }
        
    public void loadImages(int pageNumber)
    {
    	SDCardImages = new Vector<String>();
    	ArrayList<Integer> picID = new ArrayList<Integer>();
    	int picNum = 0;
    	File picFolder = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/100MEDIA/");
    	if (!picFolder.exists())
        {
    		empty = true;
        }
    	else
    	{
	    	File[] folderPics = picFolder.listFiles();
	    	int totalNumberFiles = folderPics.length;
	    	if(folderPics != null)
	    	{
		    	String name = "";		    	
		    	//for(File singleFile : folderPics)
		    	for(int i = 2*pageNumber; i < 2*pageNumber+2; i++)
		    	{
		    		File singleFile = folderPics[i];
		    		name = singleFile.getName();
		    		 
		    		 if(name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".gif"))
		    		 {
		    			 /*
			    		 ImageView myImageView = new ImageView(context);
			    		 myImageView.setImageDrawable(Drawable.createFromPath(singleFile.getAbsolutePath()));
			    		 myImageView.setId(picNum);
			    		 picNum++;
			    		 picID.add(myImageView.getId());
			    		 myImageView.setImageBitmap(decodeSampledBitmapFromFile(singleFile.getAbsolutePath(), 500, 500));
			    		 */		    			 
			    		 //SDCardImages.add(myImageView);
		    			 String path = singleFile.getAbsolutePath();
		    			 SDCardImages.add(path);
		    		 }
		    	}
	    	}
	    	else
	    	{
	    		empty = true;
	    	}
    	}
    	
    }
    
    public Vector<String> getFiles()
    {
    	return SDCardImages;   	
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
        	//imageView.setImageDrawable(SDCardImages.get(position).getDrawable());
        	imageView.setImageBitmap(decodeSampledBitmapFromFile(SDCardImages.get(position), 500, 500));
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
