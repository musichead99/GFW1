package com.example.capstonedesign;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstonedesign.retrofit.KakaoResponse;
import com.example.capstonedesign.retrofit.RetrofitClient;
import com.example.capstonedesign.retrofit.initMyApi;

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
    WebResourceRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_webview);

        mWebView = findViewById(R.id.webView);

        mWebView.getSettings().setJavaScriptEnabled(true); //웹뷰에 자바스크립트 접근을 허용해주는 셋팅

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
                    mWebView.loadUrl(data.getMessage()); //url주소를 불러오는 함수
                    Log.d("data","data"+data.getMessage());
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
        mWebView.setWebChromeClient(new WebChromeClient()); //웹뷰로 띄운 웹 페이지를 컨트롤하는 함수, 크롬에 맞춰 쾌적한 환경조성을 위한 셋팅
        mWebView.setWebViewClient(new WebViewClientClass()); //페이지 컨트롤을 위한 기본적인 함수, 다양한 요청, 알림을 수신하는 기능을 함
        /*mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('html')[0].innerHTML);");
            }
        });
        //mWebView.addJavascriptInterface(new MyJavascriptInterface(), "Android");
        //mWebView.loadUrl("http://m.naver.com");
        Log.d("리퀘스트","리퀘스트"+request);*/
    }
    private class WebViewClientClass extends android.webkit.WebViewClient {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Uri uri = Uri.parse(request.getUrl().toString());
            Log.d("request",request.getUrl().toString());
            Log.d("_URI_",uri.getPath().toString());

            //sharedPreferences = getSharedPreferences("email", MODE_PRIVATE);
            //editor = sharedPreferences.edit();
            if(uri.getPath().contains("/user/kakao/callback")) {
                //token = request.getUrl().getQueryParameter("access token");                              //다시 봐야함 이부분은
                //HashMap<String, String> headerMap = new HashMap<>();
                //headerMap.put("access token", token);
                //view.loadUrl(request.getUrl().toString(), headerMap);
                view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('html')[0].innerHTML);");
                mWebView.addJavascriptInterface(new MyJavascriptInterface(), "Android");
                mWebView.loadUrl(request.getUrl().toString());
                //editor.putString("token", token);
                //editor.commit();
                //Log.d("token","토큰은"+token);
                view.destroy();
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                finish();
            }
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { //폰 내에서 뒤로가기 버튼을 눌렀을 때 웹이 뒤로 감
        if((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public class MyJavascriptInterface {
        @JavascriptInterface
        public void getHtml(String html) {
            Log.d("html","넹"+html);
        }
    }
}

