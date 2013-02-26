package amazenite.lockit;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class cropOptionAdapter extends ArrayAdapter<cropOption> {
 private ArrayList<cropOption> mOptions;
 private LayoutInflater mInflater;

  public cropOptionAdapter(Context context, ArrayList<cropOption> options) {
  super(context, R.layout.crop_selecter, options);

   mOptions  = options;

   mInflater = LayoutInflater.from(context);
 }

  @Override
 public View getView(int position, View convertView, ViewGroup group) {
  if (convertView == null)
   convertView = mInflater.inflate(R.layout.crop_selecter, null);

   cropOption item = mOptions.get(position);

   if (item != null) {
   ((ImageView) convertView.findViewById(R.id.iv_icon)).setImageDrawable(item.icon);
   ((TextView) convertView.findViewById(R.id.tv_name)).setText(item.title);

    return convertView;
  }

   return null;
 }
}
