package com.example.travelguidepro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    public CommentFragment() {
        // Constructor público vacío requerido por Fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View rootView = inflater.inflate(R.layout.fragment_comment, container, false);

        // Inicializar los elementos de la interfaz de usuario
        editComment = rootView.findViewById(R.id.editComment);
        btnPublish = rootView.findViewById(R.id.btnPublish);
        recyclerViewComments = rootView.findViewById(R.id.recyclerViewComments);

        // Configurar el RecyclerView
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList);
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

        // Configurar OnClickListener para el botón de regreso
        ImageButton btnBack = rootView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Regresar a la actividad anterior
                getActivity().onBackPressed();
            }
        });

        // Cargar los comentarios existentes
        actualizarRecyclerView();

        return rootView;
    }

    // Método para guardar el comentario
    // Método para guardar el comentario
    private void guardarComentario(String commentText) {
        // Obtener el usuario actualmente conectado
        if (mCurrentUser != null) {
            String username = mCurrentUser.getUsername();
            Comment comment = new Comment(username, commentText);

            // Guardar el comentario en SharedPreferences
            mSharedPreferences.saveComment(comment);

            // Actualizar el RecyclerView para mostrar el comentario recién publicado
            actualizarRecyclerView();
        } else {
            // Manejar el caso en que no haya un usuario conectado
            Toast.makeText(getActivity(), "Error: No se pudo obtener el usuario actual", Toast.LENGTH_SHORT).show();
        }
    }


    // Método para actualizar el RecyclerView
    private void actualizarRecyclerView() {
        // Obtener todos los comentarios guardados en SharedPreferences
        List<Comment> comments = mSharedPreferences.getAllComments();
        // Limpiar la lista actual de comentarios
        commentList.clear();
        // Añadir los comentarios obtenidos a la lista
        commentList.addAll(comments);
        // Notificar al adaptador que los datos han cambiado
        commentAdapter.notifyDataSetChanged();
    }
}
