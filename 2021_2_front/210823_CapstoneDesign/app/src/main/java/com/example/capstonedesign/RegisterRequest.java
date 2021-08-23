package com.example.capstonedesign;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    //서버 URL 설정(PHP 파일 연동)
    final static private String URL = "http//자신의 주소 IP/Register.php";
    private Map<String, String> map;

    public RegisterRequest(String userName, String userEmail, String userPass, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userName", userName);
        map.put("userEmail", userEmail);
        map.put("userPass", userPass);
    }

    public Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
