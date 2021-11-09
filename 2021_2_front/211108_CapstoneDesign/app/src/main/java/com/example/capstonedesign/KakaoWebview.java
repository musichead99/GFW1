package com.example.capstonedesign;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.example.capstonedesign.retrofit.KakaoResponse;
import com.example.capstonedesign.retrofit.RetrofitClient;
import com.example.capstonedesign.retrofit.initMyApi;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KakaoWebview extends AppCompatActivity {
    private WebView mWebView;
    private String test_string = new String();
    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_webview);

        mWebView = findViewById(R.id.webView);

        mWebView.getSettings().setJavaScriptEnabled(true);

        //sharedPreferences = getSharedPreferences("email", MODE_PRIVATE);
        //editor = sharedPreferences.edit();

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();

        Call<KakaoResponse> call = initMyApi.getKakaoResponse();
        call.enqueue(new Callback<KakaoResponse>() {
            @Override
            public void onResponse(Call<KakaoResponse> call, Response<KakaoResponse> response) {
                if(response.isSuccessful()) {
                    KakaoResponse data = response.body();
                    test_string = data.getMessage();
                    mWebView.loadUrl(data.getMessage());
                    //String token = data.getToken();
                    //editor.putString("token", token);
                    //editor.commit();
                    //Log.d("token","토큰은"+token);
                }
            }

            @Override
            public void onFailure(Call<KakaoResponse> call, Throwable t) {

            }
        });
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClientClass());
    }
    private class WebViewClientClass extends android.webkit.WebViewClient {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Uri uri = Uri.parse(request.getUrl().toString());
            Log.d("request",request.getUrl().toString());
            Log.d("_URI_",uri.getPath().toString());
            sharedPreferences = getSharedPreferences("email", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            if(uri.getPath().contains("/user/kakao/callback")) {
                //token = request.getUrl().getQueryParameter("access token");                              //다시 봐야함 이부분은
                HashMap<String, String> headerMap = new HashMap<>();
                headerMap.put("access token", token);
                view.loadUrl(request.getUrl().toString(), headerMap);
                editor.putString("token", token);
                editor.commit();
                Log.d("token","토큰은"+token);
                view.destroy();
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                finish();
            }
            return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
