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

        View rootView = inflater.inflate(R.layout.fragment_comment, container, false);


        editComment = rootView.findViewById(R.id.editComment);
        btnPublish = rootView.findViewById(R.id.btnPublish);
        recyclerViewComments = rootView.findViewById(R.id.recyclerViewComments);


        commentList = new ArrayList<>();
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


        actualizarRecyclerView();

        return rootView;
    }


    private void guardarComentario(String commentText) {

        if (mCurrentUser != null) {
            String username = mCurrentUser.getUsername();
            Comment comment = new Comment(username, commentText);


            mSharedPreferences.saveComment(comment);


            actualizarRecyclerView();
        } else {

            Toast.makeText(getActivity(), "Error: No se pudo obtener el usuario actual", Toast.LENGTH_SHORT).show();
        }
    }


    private void actualizarRecyclerView() {

        List<Comment> comments = mSharedPreferences.getNonDeletedComments();

        commentList.clear();

        commentList.addAll(comments);

        commentAdapter.notifyDataSetChanged();
    }


    private void eliminarComentario(int position) {
        try {

            Comment comment = commentList.get(position);
            mSharedPreferences.deleteComment(comment);


            commentList.remove(position);


            commentAdapter.notifyItemRemoved(position);

            commentAdapter.notifyItemRangeChanged(position, commentList.size());
        } catch (Exception e) {
            Log.e("CommentFragment", "Error eliminando comentario", e);
        }
    }
}
