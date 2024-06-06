package com.example.travelguidepro;


import java.util.UUID;

public class Comment {
    public String id;
    public String username;
    public String commentText;

    public Comment() {
        // Constructor vacío requerido por Firebase
    }

    public Comment(String username, String commentText) {
        this.username = username;
        this.commentText = commentText;
        this.id = UUID.randomUUID().toString();
    }

    public Comment(String id,String username, String commentText) {
        this.username = username;
        this.commentText = commentText;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getCommentText() {
        return commentText;
    }
}
