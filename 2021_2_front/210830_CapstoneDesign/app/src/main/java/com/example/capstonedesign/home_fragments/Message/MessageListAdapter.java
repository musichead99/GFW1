package com.example.capstonedesign.home_fragments.Message;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.capstonedesign.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageListAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mlayoutInflater = null;
    ArrayList<Message> messages;

    public MessageListAdapter(Context context, ArrayList<Message> messageList){
        mContext = context;
        messages = messageList;
        mlayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Message getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = mlayoutInflater.inflate(R.layout.message_list_item,null);

        CircleImageView profile_img = (CircleImageView)view.findViewById(R.id.profile_img);
        TextView msg_title_view = (TextView)view.findViewById(R.id.msg_title);
        TextView msg_content_view = (TextView)view.findViewById(R.id.msg_content);

        Message message = (Message) messages.get(position);

        String img_src = message.getImage();
        String msg_title = message.getMsgTitle();
        String msg_content = message.getMsgContent();

        if(msg_content == null) msg_content_view.setVisibility(View.GONE);

        // Setting all the contents of views
        // profile_img.setImageURI(img_uri);
        Glide.with(mContext).load(img_src).into(profile_img);
        msg_title_view.setText(msg_title);
        msg_content_view.setText(msg_content);

        return view;
    }
}
