package com.example.travelguidepro;



public class Comment {
    private String username;
    private String commentText;
    private boolean deleted;

    public Comment(String username, String commentText) {
        this.username = username;
        this.commentText = commentText;
        this.deleted = false;
    }

    public String getUsername() {
        return username;
    }

    public String getCommentText() {
        return commentText;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
