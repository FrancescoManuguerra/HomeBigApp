package com.manuguerra.homebigapp.services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.Map;

public class RestController {

    private final static String base_url = "http://138.68.86.70/";
    private final static String version = "v1/";
    private RequestQueue requestQueue;


    public RestController(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getRequest(String endpoint, Response.Listener<String> success, Response.ErrorListener error) {
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,//HTTP request method
                base_url.concat(endpoint),// Server link
                success,
                error
        );
        requestQueue.add(stringRequest);
    }

    public void postRequest(String endpoint, Response.Listener<String> success, Response.ErrorListener error, final Map<String,String> params)  {
        //
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                base_url.concat(endpoint),
                success,
                error
        ){
            @Override
            protected Map<String,String> getParams(){
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }

}
