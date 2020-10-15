package com.example.smartpasal.repository;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;


import androidx.annotation.RequiresApi;
import androidx.annotation.VisibleForTesting;

import com.example.smartpasal.Session.Session;

import com.example.smartpasal.SmartAPI.JsonResponse;
import com.example.smartpasal.SmartAPI.JwtResponse;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;



public class UserRepository {
    private Session session;
    private Context context;

    public UserRepository(Session session, Context context)
    {
        this.session=session;
        this.context=context;
    }

    public boolean checkIfLoggedIn()
    { if (!session.getToken().isEmpty()) {
            String token = session.getToken();
            String[] split_string = token.split("\\.");
            String payload = split_string[1];
            String body="";

        if (Build.VERSION.SDK_INT >= 26) {

            body = new String(Base64.getDecoder().decode(payload));
        } else {
           body = new String(android.util.Base64.decode(payload, android.util.Base64.DEFAULT));
        }
            HashMap<String, String> map = new Gson().fromJson(body, new TypeToken<HashMap<String, String>>() {
            }.getType());
            String exp = map.get("exp");
            String userName=map.get("sub");
            session.setUsername(userName);
            session.setJWT("Bearer "+token);
            if (checkISExpired(exp))
            {
                session.destroy();
                return false;
            }
            else return true;
        }
        else return false;

    }

    private Boolean checkISExpired(String exp) {
        Long currentMillis=System.currentTimeMillis()/1000;
        if (currentMillis >= Long.valueOf(exp))
            return true;
        else
            return false;
    }




}
