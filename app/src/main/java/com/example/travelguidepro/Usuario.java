package com.example.travelguidepro;

public class Usuario {
    private String id;
    private String email;
    private String username;

    public Usuario(String id, String email) {
        this.id = id;
        this.email = email;
        this.username = generateUsernameFromEmail(email);
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    private String generateUsernameFromEmail(String email) {
        if (email != null && email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email; // fallback to email if format is unexpected
        }
    }
}
