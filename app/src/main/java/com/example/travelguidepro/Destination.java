package com.example.travelguidepro;

public class Destination {
    public String nombre;
    public String descripción;
    public String ubicación;
    public String img;

    public Destination() {
        // Constructor vacío requerido por Firebase
    }

    public Destination(String nombre, String descripción, String ubicación, String img) {
        this.nombre = nombre;
        this.descripción = descripción;
        this.ubicación = ubicación;
        this.img = img;
    }
}
