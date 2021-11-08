package com.example.capstonedesign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.capstonedesign.retrofit.Friend;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyArrayAdapter extends ArrayAdapter {
    public MyArrayAdapter(Context context, ArrayList users){
        super(context,0,users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cfriend_list_item, parent, false);
        }

        // Get the data item for this position
        Friend friend = (Friend) getItem(position);

        // Lookup view for data population
        CircleImageView profile_img = convertView.findViewById(R.id.cmp_friendItem_profile_img);
        TextView name = (TextView) convertView.findViewById(R.id.cfriend_list_Item_name);
        CheckBox checkBox = convertView.findViewById(R.id.cfriend_list_Item_check_box);
        // Populate the data into the template view using the data object
        profile_img.setImageResource(R.drawable.ic_launcher_background);
        name.setText(friend.getName());
        if(position == 1){
            checkBox.setChecked(true);
        }else checkBox.setChecked(false);

        // Return the completed view to render on screen
        return convertView;
    }
}
