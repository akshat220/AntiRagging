package com.imad.antiragging.ui.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imad.antiragging.R;
import com.imad.antiragging.data.Post;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private List<Post> dataset;
    private DatabaseReference fb;
    private FirebaseAuth auth;
    private PostAdapter postAdapter;
    private EditText message;
    private ImageButton send;
    private ProgressBar progressBar;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_home, container, false);
        message = root.findViewById(R.id.msg_text);
        send = root.findViewById(R.id.send);
        progressBar = getActivity().findViewById(R.id.progress_bar);
        auth = FirebaseAuth.getInstance();
        fb = FirebaseDatabase.getInstance().getReference();
        dataset = new ArrayList<>();
        postAdapter = new PostAdapter(dataset);
        RecyclerView list = root.findViewById(R.id.post_list);
        list.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        list.setAdapter(postAdapter);
        updateData();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!message.getText().toString().isEmpty()){
                    showAnonymousAlert(message.getText().toString());
                }
            }
        });

        return root;
    }

    private void updateData(){
        progressBar.setVisibility(View.VISIBLE);
        fb.child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataset.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    dataset.add(dataSnapshot1.getValue(Post.class));
                }
                postAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        dataset.clear();
        postAdapter.notifyDataSetChanged();
        updateData();
    }

    private void showAnonymousAlert(final String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder
                .setTitle("Anonymous Post")
                .setMessage("Do you want to post this as Anonymous.")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Post post = new Post("Anonymous",
                                        Timestamp.now().getSeconds(),
                                        msg,
                                        "",
                                        auth.getCurrentUser().getUid());
                                addPost(post);
                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseUser user = auth.getCurrentUser();
                                String url;
                                if(user.getPhotoUrl() != null)
                                    url = user.getPhotoUrl().toString();
                                else
                                    url = "";
                                Post post = new Post(user.getDisplayName(),
                                        Timestamp.now().getSeconds(),
                                        msg,
                                        url,
                                        user.getUid());
                                addPost(post);
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void addPost(Post post){
        fb.child("posts").child(post.getDate() + "").setValue(post);
        updateData();
        message.setText("");
    }
}