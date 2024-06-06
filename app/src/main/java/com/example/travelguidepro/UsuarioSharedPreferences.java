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
    private Gson gson;

    public UsuarioSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        gson = new Gson();
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



    public void saveComment(Comment comment) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        List<Comment> comments = getAllComments();
        comments.add(comment);
        editor.putString("comments", gson.toJson(comments));
        editor.apply();
    }




    public List<Comment> getAllComments() {
        String commentsJson = sharedPreferences.getString("comments", null);
        if (commentsJson != null) {
            Type type = new TypeToken<List<Comment>>() {}.getType();
            return gson.fromJson(commentsJson, type);
        } else {
            return new ArrayList<>();
        }
    }
}
