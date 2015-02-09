package com.codepath.instaviewer;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vrumale on 2/6/2015.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto>{
    //What data do we need from activity
    // Context and DataSouce
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }
    //What are item looks like

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //gET THE DATA item for this position
        InstagramPhoto photo = getItem(position);
        //check if we are using a recycled view, if not we need to inflate
        if(convertView == null) {
            // No recycl view Create new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        //lookup the views for populating the data (image, caption)
        TextView tvProfile = (TextView) convertView.findViewById(R.id.tvUserProfile);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        TextView tvComments = (TextView) convertView.findViewById(R.id.tvComments);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        TextView tvTimeCreated = (TextView) convertView.findViewById(R.id.tvTimeCreated);
        //Insert the model data into each of the view items
        tvLikes.setText(photo.likes + " likes");
        tvComments.setText(photo.commentCount + " Comments");
        tvCaption.setText(photo.caption);
        //Insert the Profile pic using
        tvProfile.setText(photo.username);
        ImageView ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
        tvTimeCreated.setText(DateUtils.getRelativeTimeSpanString((photo.createdTime* 1000), System.currentTimeMillis(),DateUtils.SECOND_IN_MILLIS));
       // Picasso.with(getContext()).load(photo.profilePic).into((com.squareup.picasso.Target) tvProfile);
        // clear out the imageView
        ivProfile.setImageResource(0);
        ivPhoto.setImageResource(0); // clear out
        Picasso.with(getContext()).load(photo.profilePic).transform(new CircleTransform()).into(ivProfile);
        //Insert the image  using picasso : it takes time
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);

        //Comments
        tvComments.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LayoutInflater Li = LayoutInflater.from(getContext());
                View Cv = Li.inflate(R.layout.comments_layout, null);
            }
        });

        //Return the created item as a view
        return convertView;
    }
}
