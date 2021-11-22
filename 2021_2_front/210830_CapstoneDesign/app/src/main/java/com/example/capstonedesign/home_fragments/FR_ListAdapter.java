package com.example.capstonedesign.home_fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.capstonedesign.R;
import com.example.capstonedesign.retrofit.Friend;
import com.example.capstonedesign.retrofit.ProceedFriendRequest.CancelFR_Request;
import com.example.capstonedesign.retrofit.ProceedFriendRequest.CancelFR_Response;
import com.example.capstonedesign.retrofit.ProceedFriendRequest.ProceedFR_Request;
import com.example.capstonedesign.retrofit.ProceedFriendRequest.ProceedFR_Response;
import com.example.capstonedesign.retrofit.RetrofitClient;
import com.example.capstonedesign.retrofit.initMyApi;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FR_ListAdapter extends BaseAdapter {

    public static boolean REQUESTING = false;
    public static boolean REQUESTED = true;
    Context mContext;
    ArrayList<Friend> arrayList;
    LayoutInflater mlayoutInflater;
    boolean listType = REQUESTING;
    RetrofitClient retrofitClient;
    initMyApi initMyApi;

    public FR_ListAdapter(Context mContext, ArrayList<Friend> arrayList){
        this.mContext = mContext;
        this.arrayList = arrayList;
        mlayoutInflater = LayoutInflater.from(mContext);
    }

    public void setRetrofit(){
        this.retrofitClient = RetrofitClient.getNewInstance(mContext.getApplicationContext());
        initMyApi = RetrofitClient.getNewRetrofitInterface();
    }
    public void setListType(boolean listType){
        this.listType = listType;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = mlayoutInflater.inflate(R.layout.friend_reqeuset_item,null);

        CircleImageView view_profile_img = view.findViewById(R.id.fri_req_profile_img);
        TextView view_content = view.findViewById(R.id.fri_req_contents);
        ImageView btn_ok = view.findViewById(R.id.fri_req_btn_ok);
        ImageView btn_no = view.findViewById(R.id.fri_req_btn_no);

        // 해당 follower의 정보 받기.
        Friend list_item = this.arrayList.get(position);

        String name = list_item.getName();
        String img_src = list_item.getImage();
        String email = list_item.getEmail();

        Glide.with(mContext).load(img_src).into(view_profile_img);

        if(listType == REQUESTING){
            btn_ok.setVisibility(View.GONE);
            view_content.setText(name + "님에게 한 요청");

            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 내가 한 요청 취소.
                    CancelFR_Request request = new CancelFR_Request(email);
                    initMyApi.CancelFR_Request(request).enqueue(new Callback<CancelFR_Response>() {
                        @Override
                        public void onResponse(Call<CancelFR_Response> call, Response<CancelFR_Response> response) {
                            if(response.isSuccessful()){
                                arrayList.remove(position);
                                notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onFailure(Call<CancelFR_Response> call, Throwable t) {}
                    });
                }
            });
        }else{
            view_content.setText(name + "님의 친구 요청");
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 친구 요청 수락.
                    ProceedFR_Request request = new ProceedFR_Request(email,"yes");
                    initMyApi.ProceedFR_Request(request).enqueue(new Callback<ProceedFR_Response>() {
                        @Override
                        public void onResponse(Call<ProceedFR_Response> call, Response<ProceedFR_Response> response) {
                            if(response.isSuccessful()){
                                arrayList.remove(position);
                                notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onFailure(Call<ProceedFR_Response> call, Throwable t) {}
                    });
                }
            });
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 친구 요청 거절.
                    ProceedFR_Request request = new ProceedFR_Request(email,"no");
                    initMyApi.ProceedFR_Request(request).enqueue(new Callback<ProceedFR_Response>() {
                        @Override
                        public void onResponse(Call<ProceedFR_Response> call, Response<ProceedFR_Response> response) {
                            if(response.isSuccessful()){
                                arrayList.remove(position);
                                notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onFailure(Call<ProceedFR_Response> call, Throwable t) {}
                    });
                }
            });
        }

        return view;
    }

}
