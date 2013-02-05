package amazenite.lockit;

import java.io.File;
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
    private Vector<String> SDCardImages;
    private Vector<Integer> DefaultImages;
    public boolean empty = false;
    private int pageNumber;
    private int totalNumberFiles = 0;
    
    public ImageAdapter(Context c, int pageNumber) {
        context = c;        
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
    	int maxPicNumbers = 12; 
    	
    	SDCardImages = new Vector<String>();
    	File picFolder = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/100MEDIA/");
    	if (!picFolder.exists())
        {
    		empty = true;
        }
    	else
    	{
	    	File[] folderPics = picFolder.listFiles();
	    	totalNumberFiles = folderPics.length - (pageNumber*maxPicNumbers);
	    	int endingPicNum = maxPicNumbers;
		    	if(totalNumberFiles < 12){
		    		endingPicNum = totalNumberFiles;
		    	}
	    	
	    	if(folderPics != null)
	    	{
	    		
			    	String name = "";
			    	SDCardImages.removeAllElements();
			    	for(int i = maxPicNumbers*pageNumber; i < maxPicNumbers*pageNumber+endingPicNum; i++)
			    	{
			    		File singleFile = folderPics[i];
			    		name = singleFile.getName();
			    		 
			    		 if(name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".gif"))
			    		 {
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
    	if(empty)
    	{
    		DefaultImages = new Vector<Integer>();
    		DefaultImages.add(R.drawable.rainbow);
    		DefaultImages.add(R.drawable.rainbow);
    		DefaultImages.add(R.drawable.rainbow);
    		DefaultImages.add(R.drawable.rainbow);
    		DefaultImages.add(R.drawable.rainbow);
    		DefaultImages.add(R.drawable.rainbow);
    		DefaultImages.add(R.drawable.rainbow);
    		DefaultImages.add(R.drawable.rainbow);
    		DefaultImages.add(R.drawable.rainbow);
    		DefaultImages.add(R.drawable.rainbow);
    	}
    }
    
    public int getFileCount(){

    	return totalNumberFiles;
    	
    }
    
    public boolean getCurrentCount(){
    	//Returns true if don't need to display button
    	if ((totalNumberFiles - 12) < 12){
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    public Vector<String> getFiles()
    {
    	for(int i = 0; i<SDCardImages.size(); i++)
    	{
    	//	Toast.makeText(context, SDCardImages.get(i), Toast.LENGTH_SHORT).show();
    	}
    	return SDCardImages;   	
    }
    public Vector<Integer> getFiles2()
    {
    	return DefaultImages;
    }
    
    public int getCount() {
    	int num = 0;
    	if(empty)
    	{
    		num= DefaultImages.size();
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
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(4, 4, 4, 4);
        } else {
            imageView = (ImageView) convertView;
        }
        if(empty)
        {
        	imageView.setImageResource(DefaultImages.get(position));
        }
        else
        {
        	//imageView.setImageDrawable(SDCardImages.get(position).getDrawable());
        	imageView.setImageBitmap(decodeSampledBitmapFromFile(SDCardImages.get(position), 500, 500));
        }        
        return imageView;
    }
}
