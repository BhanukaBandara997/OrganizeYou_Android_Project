package com.example.bhanukabandara.mobile_cw2.other;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ServerConnection {

    private static ServerConnection mInstance;
    private RequestQueue requestQueue;
    private static Context mctx;


    private ServerConnection(Context context){
        mctx=context;
        requestQueue=getRequestQueue();
    }


    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(mctx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized ServerConnection getInstance(Context context){
        if(mInstance==null){
            mInstance=new ServerConnection(context);
        }
        return mInstance;
    }

    public <T> void addToRequestque(Request<T> request){
        requestQueue.add(request);
    }
}

