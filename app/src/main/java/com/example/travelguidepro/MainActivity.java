package com.example.travelguidepro;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentContainerView fragmentContainerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.BNavigation);
        fragmentContainerView = findViewById(R.id.fragment_container_view);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getTitle().toString()) {
                case "Inicio":
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, new HomeFragment()).commit();
                    break;
                case "Favoritos":
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, new FavoriteFragment()).commit();
                    break;
            }
            return true;
        });
    }
}