package com.example.capstonedesign;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ValidateRequest extends StringRequest {
    final static private String URL="http//자신의 주소 IP/Register.php";
    private Map<String,String> map;

    public ValidateRequest(String userEmail, Response.Listener<String>listener) {
        super(Request.Method.POST, URL, listener, null);

        map=new HashMap<>();
        map.put("userEmail", userEmail);
    }

    public Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
