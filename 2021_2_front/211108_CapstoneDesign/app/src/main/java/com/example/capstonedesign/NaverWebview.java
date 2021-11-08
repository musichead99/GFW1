package com.example.capstonedesign;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.example.capstonedesign.retrofit.NaverResponse;
import com.example.capstonedesign.retrofit.RetrofitClient;
import com.example.capstonedesign.retrofit.initMyApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NaverWebview extends AppCompatActivity {
    private WebView mWebView;
    private String test_string = new String();
    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naver_webview);

        mWebView = findViewById(R.id.NaverwebView);

        mWebView.getSettings().setJavaScriptEnabled(true);

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();

        Call<NaverResponse> call = initMyApi.getNaverResponse();
        call.enqueue(new Callback<NaverResponse>() {
            @Override
            public void onResponse(Call<NaverResponse> call, Response<NaverResponse> response) {
                if(response.isSuccessful()) {
                    NaverResponse data = response.body();
                    test_string = data.getMessage();
                    mWebView.loadUrl(data.getMessage());
                }
            }

            @Override
            public void onFailure(Call<NaverResponse> call, Throwable t) {

            }
        });
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new NaverWebview.WebViewClientClass());
    }
    private class WebViewClientClass extends android.webkit.WebViewClient {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Uri uri = Uri.parse(request.getUrl().toString());
            if(uri.getPath().contains("/user/Naver/callback")) {
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