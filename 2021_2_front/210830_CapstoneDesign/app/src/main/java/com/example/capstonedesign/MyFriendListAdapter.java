package com.example.capstonedesign;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.capstonedesign.retrofit.Friend;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyFriendListAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mlayoutInflater = null;
    ArrayList<Friend> friends;

    public MyFriendListAdapter(Context context, ArrayList<Friend> friendList){
        mContext = context;
        friends = friendList;
        mlayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Friend getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = mlayoutInflater.inflate(R.layout.friendlist_item,null);

        // View를 findViewById를 통해서 Binding
        CircleImageView view_profile_img = view.findViewById(R.id.profile_img);
        TextView view_name = view.findViewById(R.id.name);

        // Friend 현재 선택된 View의 객체 정보를 가져옴.
        Friend friend = (Friend) friends.get(position);
        
        // Friend 객체에서 원하는 정보 꺼내기
        String img_src = friend.getImage();
        Log.d("image_src",img_src);
        String name = friend.getName();

        // 각각의 View에 원하는 정보 set
        Glide.with(mContext).load(img_src).into(view_profile_img);
        view_name.setText(name);

        return view;
    }
}
