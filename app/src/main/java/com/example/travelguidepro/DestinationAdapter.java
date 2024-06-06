package com.example.travelguidepro;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder> {
    private Context mContext;
    private List<Destination> mDestinations;

    private UsuarioSharedPreferences mSharedPreferences;
    private Usuario mCurrentUser;



    public DestinationAdapter(Context context, List<Destination> destinations) {
        mContext = context;
        mDestinations = destinations;
        mSharedPreferences = new UsuarioSharedPreferences(context);
        mCurrentUser = mSharedPreferences.getCurrentUser();
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

        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el FragmentManager desde la actividad
                FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                // Crear una instancia del fragmento de comentarios
                CommentFragment fragment = new CommentFragment(destination.id);
                // Iniciar la transacción para agregar el fragmento al contenedor

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, fragment)
                        .addToBackStack(null)  // Opcional: agregar el fragmento al back stack
                        .commit();
            }
        });

        holder.btnFavorite.setOnClickListener(v ->{
            // Agregar lógica para marcar como favorito
            UsuarioSharedPreferences usuarioSharedPreferences = new UsuarioSharedPreferences(v.getContext());
            Usuario usuario = usuarioSharedPreferences.getCurrentUser();
            String idUser = usuario.getId();
            String currentID = destination.id;

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("favorites").child(idUser);
            if (holder.btnFavorite.isSelected()) {
                databaseReference.child(String.valueOf(currentID)).removeValue();
                holder.btnFavorite.setSelected(false);
            } else {
                databaseReference.child(String.valueOf(currentID)).setValue(destination);
                holder.btnFavorite.setSelected(true);
            }
        });
    }

    public interface OnCommentClickListener {
        void onCommentClick(int position);
    }

    private OnCommentClickListener mOnCommentClickListener;

    public void setOnCommentClickListener(OnCommentClickListener listener) {
        mOnCommentClickListener = listener;
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

        public ImageButton btnComment; // Agrega el botón de comentarios
        public ImageButton btnFavorite; // Agrega el botón de favoritos

        public DestinationViewHolder(@NonNull View itemView) {
            super(itemView);
            destinationName = itemView.findViewById(R.id.destinationName);
            destinationDescription = itemView.findViewById(R.id.destinationDescription);
            destinationLocation = itemView.findViewById(R.id.destinationLocation);
            destinationImage = itemView.findViewById(R.id.destinationImage);
            btnComment = itemView.findViewById(R.id.btnComment);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
        }
    }
}