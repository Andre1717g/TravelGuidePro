package com.example.travelguidepro;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder> {
    private Context mContext;
    private List<Destination> mDestinations;

    public DestinationAdapter(Context context, List<Destination> destinations) {
        mContext = context;
        mDestinations = destinations;
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_destination, parent, false);
        return new DestinationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder holder, int position) {
        Destination destination = mDestinations.get(position);
        holder.destinationName.setText(destination.nombre);
        holder.destinationDescription.setText(destination.descripción);
        holder.destinationLocation.setText(destination.ubicación);

        Log.d("DestinationAdapter", "Nombre: " + destination.nombre);
        Log.d("DestinationAdapter", "Descripción: " + destination.descripción);
        Log.d("DestinationAdapter", "Ubicación: " + destination.ubicación);


        // Referencia de la imagen en Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(destination.img);
        storageReference.getDownloadUrl().addOnSuccessListener(uri ->
                Picasso.get().load(uri).into(holder.destinationImage)
        ).addOnFailureListener(exception -> {
            // Manejo de errores si no se puede obtener la URL
            holder.destinationImage.setImageResource(R.drawable.placeholder); // Placeholder image
        });
    }

    @Override
    public int getItemCount() {
        return mDestinations.size();
    }

    public static class DestinationViewHolder extends RecyclerView.ViewHolder {
        public TextView destinationName;
        public TextView destinationDescription;
        public TextView destinationLocation;
        public ImageView destinationImage;

        public DestinationViewHolder(@NonNull View itemView) {
            super(itemView);
            destinationName = itemView.findViewById(R.id.destinationName);
            destinationDescription = itemView.findViewById(R.id.destinationDescription);
            destinationLocation = itemView.findViewById(R.id.destinationLocation);
            destinationImage = itemView.findViewById(R.id.destinationImage);
        }
    }
}
