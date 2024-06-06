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
        List<Comment> comments = getAllComments();
        comments.add(comment);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("comments", gson.toJson(comments));
        editor.apply();
    }

    public List<Comment> getNonDeletedComments() {
        List<Comment> comments = getAllComments();
        List<Comment> nonDeletedComments = new ArrayList<>();

        for (Comment c : comments) {
            if (!c.isDeleted()) {
                nonDeletedComments.add(c);
            }
        }

        return nonDeletedComments;
    }

    public List<Comment> getAllComments() {
        String commentsJson = sharedPreferences.getString("comments", null);
        List<Comment> allComments = new ArrayList<>();

        if (commentsJson != null) {
            Type type = new TypeToken<List<Comment>>() {}.getType();
            allComments = gson.fromJson(commentsJson, type);
        }

        // Filtrar los comentarios eliminados y devolver solo los no eliminados
        List<Comment> nonDeletedComments = new ArrayList<>();
        for (Comment comment : allComments) {
            if (!comment.isDeleted()) {
                nonDeletedComments.add(comment);
            }
        }

        return nonDeletedComments;
    }

    public void deleteComment(Comment comment) {
        List<Comment> comments = getAllComments();
        // Marcar el comentario como eliminado en la lista
        for (Comment c : comments) {
            if (c.equals(comment)) {
                c.setDeleted(true);
                break;
            }
        }
        // Guardar la lista actualizada en SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("comments", gson.toJson(comments));
        editor.apply();
    }
}
