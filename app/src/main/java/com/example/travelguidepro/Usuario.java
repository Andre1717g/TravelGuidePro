package com.example.travelguidepro;

class Usuario {
    private String id;
    private String email;

    public Usuario(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
