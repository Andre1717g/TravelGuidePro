package com.example.travelguidepro;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DestinationAdapter mAdapter;
    private List<Destination> mDestinations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDestinations = new ArrayList<>();
        mAdapter = new DestinationAdapter(this, mDestinations);
        mRecyclerView.setAdapter(mAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("destinations");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDestinations.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Log.d("MainActivity", "Datos de destino: " + postSnapshot.getValue().toString());
                    Destination destination = postSnapshot.getValue(Destination.class);
                    if (destination != null) {
                        mDestinations.add(destination);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
