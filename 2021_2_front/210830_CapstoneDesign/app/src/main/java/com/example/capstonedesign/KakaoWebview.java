package com.example.capstonedesign;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstonedesign.retrofit.KakaoResponse;
import com.example.capstonedesign.retrofit.RetrofitClient;
import com.example.capstonedesign.retrofit.initMyApi;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KakaoWebview extends AppCompatActivity {
    public WebView mWebView;
    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Handler handler;
    private boolean token_result= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_webview);

        mWebView = findViewById(R.id.webView);

        mWebView.getSettings().setJavaScriptEnabled(true);

        sharedPreferences = getSharedPreferences("email", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        handler = new Handler();

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();

        Call<KakaoResponse> call = initMyApi.getKakaoResponse();
        call.enqueue(new Callback<KakaoResponse>() {
            @Override
            public void onResponse(Call<KakaoResponse> call, Response<KakaoResponse> response) {
                if(response.isSuccessful()) {
                    KakaoResponse data = response.body();
                    String test_string = data.getMessage();
                    mWebView.loadUrl(data.getMessage());
                    Log.d("메시지",data.getMessage());
                }
            }

            @Override
            public void onFailure(Call<KakaoResponse> call, Throwable t) {

            }
        });
        mWebView.setWebChromeClient(new WebChromeClient());

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(token_result == true) {
                    view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0]['innerText']);");
                    view.setVisibility(View.INVISIBLE);
                    token_result = false;
                    //view.destroy();
                    //finish();
                }

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = Uri.parse(request.getUrl().toString());
                Log.d("request",request.getUrl().toString());
                Log.d("_URI_",uri.getPath().toString());
                if(uri.getPath().contains("/user/kakao/callback")) {
                    token_result = true;
                }

                return false;
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new MyJavascriptInterface(), "Android");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public class MyJavascriptInterface {

        @JavascriptInterface
        public void getHtml(String html) throws JSONException { //위 자바스크립트가 호출되면 여기로 html이 반환됨
            Log.d("html","html"+html);
            JSONObject jsonObject = new JSONObject(html);
            String token = jsonObject.getString("access token");
            editor.putString("token", token);
            editor.commit();
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
        }
    }
}

