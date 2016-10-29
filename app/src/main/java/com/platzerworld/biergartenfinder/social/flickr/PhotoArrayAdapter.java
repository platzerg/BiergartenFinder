package com.platzerworld.biergartenfinder.social.flickr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.platzerworld.biergartenfinder.R;
import com.platzerworld.biergartenfinder.social.flickr.models.FlickrPhoto;

import java.util.List;

public class PhotoArrayAdapter extends ArrayAdapter<FlickrPhoto> {
	public PhotoArrayAdapter(Context context, List<FlickrPhoto> photoList) {
		super(context, R.layout.flickr_photo_item, photoList);
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		FlickrPhoto photo = this.getItem(position);
		LinearLayout itemView;
		ImageView ivImage;
		ImageLoader imageLoader = ImageLoader.getInstance();
        if (convertView == null) {
    		LayoutInflater inflator = LayoutInflater.from(getContext());
    		itemView = (LinearLayout) inflator.inflate(R.layout.flickr_photo_item, parent, false);
        } else {
            itemView = (LinearLayout) convertView;
        }
        ivImage = (ImageView) itemView.findViewById(R.id.ivPhoto);
        ivImage.setImageResource(android.R.color.transparent); 
        imageLoader.displayImage(photo.getUrl(), ivImage);
        return itemView;
	}
}
