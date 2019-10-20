package com.imad.antiragging.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.imad.antiragging.R;
import com.imad.antiragging.data.Post;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private List<Post> dataset;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private PostAdapter postAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final EditText message = root.findViewById(R.id.msg_text);
        final ImageButton send = root.findViewById(R.id.send);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        dataset = new ArrayList<>();
        postAdapter = new PostAdapter(dataset);
        RecyclerView list = root.findViewById(R.id.post_list);
        list.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        list.setAdapter(postAdapter);
        updateData();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = auth.getCurrentUser();
                if(!message.getText().toString().isEmpty()){
                    String url;
                    if(user.getPhotoUrl() != null)
                        url = user.getPhotoUrl().toString();
                    else
                        url = "";
                    Post post = new Post(user.getDisplayName(),
                            Timestamp.now(),
                            message.getText().toString(),
                            url,
                            user.getUid());
                    db.collection("Posts")
                            .document(post.getDate().getSeconds()+"")
                            .set(post);
                    message.setText("");
                    updateData();
                }
            }
        });

        return root;
    }

    private void updateData(){
        db.collection("Posts")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Post> data = new ArrayList<>();
                        for (DocumentSnapshot document: queryDocumentSnapshots){
                            data.add(document.toObject(Post.class));
                        }
                        dataset.clear();
                        dataset.addAll(data);
                        postAdapter.notifyDataSetChanged();
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
}