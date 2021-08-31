package com.example.capstonedesign;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PasswordRequest extends StringRequest {
    final static private String URL = "";
    private Map<String, String> map;

    public PasswordRequest(String UserPwd, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("UserPwd", UserPwd);
    }

    public Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
