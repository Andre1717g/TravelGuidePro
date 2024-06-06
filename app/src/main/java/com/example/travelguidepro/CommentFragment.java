package com.example.travelguidepro;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CommentFragment extends Fragment {

    private EditText editComment;
    private Button btnPublish;
    private RecyclerView recyclerViewComments;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private UsuarioSharedPreferences mSharedPreferences;
    private Usuario mCurrentUser;
    private String currentID;

    public CommentFragment() {
        // Constructor público vacío requerido por Fragment
    }

    public CommentFragment(String id) {
        this.currentID = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_comment, container, false);
        editComment = rootView.findViewById(R.id.editComment);
        btnPublish = rootView.findViewById(R.id.btnPublish);
        recyclerViewComments = rootView.findViewById(R.id.recyclerViewComments);
        commentList = new ArrayList<>();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("comments").child(currentID);
        mDatabase.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Comment comment = postSnapshot.getValue(Comment.class);
                    if (comment != null) {
                       commentList.add(comment);
                    }
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
              Toast.makeText(getActivity(), "Error al cargar los comentarios", Toast.LENGTH_SHORT).show();
            }
        });
        commentAdapter = new CommentAdapter(commentList, new CommentAdapter.OnCommentDeleteListener() {
            @Override
            public void onCommentDelete(int position) {
                eliminarComentario(position);
            }
        });
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewComments.setAdapter(commentAdapter);

        // Inicializar SharedPreferences
        mSharedPreferences = new UsuarioSharedPreferences(getActivity());
        mCurrentUser = mSharedPreferences.getCurrentUser();

        // Configurar OnClickListener para el botón de publicar
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Capturar el comentario del EditText
                String commentText = editComment.getText().toString().trim();

                // Verificar si el comentario está vacío
                if (!commentText.isEmpty()) {
                    // Guardar el comentario
                    guardarComentario(commentText);
                } else {
                    // Mostrar un mensaje de error si el comentario está vacío
                    Toast.makeText(getActivity(), "Por favor, escribe un comentario", Toast.LENGTH_SHORT).show();
                }
            }
        });


        ImageButton btnBack = rootView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Regresar a la actividad anterior
                getActivity().onBackPressed();
            }
        });
        return rootView;
    }


    private void guardarComentario(String commentText) {
        if (mCurrentUser != null) {
            String username = mCurrentUser.getUsername();
            Comment comment = new Comment(username, commentText);
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("comments").child(currentID);
            mDatabase.child(comment.id).setValue(comment);
            Toast.makeText(getActivity(), "Comentario publicado", Toast.LENGTH_SHORT).show();
            editComment.setText("");
        } else {
            Toast.makeText(getActivity(), "Error: No se pudo obtener el usuario actual", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarComentario(int position) {
        try {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("comments").child(currentID);
            mDatabase.child(commentList.get(position).id).removeValue();
            Toast.makeText(getActivity(), "Comentario eliminado", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("CommentFragment", "Error eliminando comentario", e);
        }
    }
}
