package com.example.travelguidepro;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UsuarioSharedPreferences {
    private SharedPreferences sharedPreferences;

    public UsuarioSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
    }

    public void saveCurrentUser(String userId, String userEmail) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", userId);
        editor.putString("userEmail", userEmail);
        editor.apply();
    }

    public Usuario getCurrentUser() {
        String userId = sharedPreferences.getString("userId", null);
        String userEmail = sharedPreferences.getString("userEmail", null);
        if (userId != null && userEmail != null) {
            return new Usuario(userId, userEmail);
        } else {
            return null;
        }
    }

}
