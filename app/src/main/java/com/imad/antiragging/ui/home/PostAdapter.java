package com.imad.antiragging.ui.home;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.imad.antiragging.R;
import com.imad.antiragging.data.Post;

import java.text.SimpleDateFormat;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private List<Post> dataset;
    static class MyViewHolder extends  RecyclerView.ViewHolder{
        private TextView date;
        private TextView name;
        private TextView post;
        private ImageButton delete;
        private ImageView image;

        MyViewHolder(final View itemView){
            super(itemView);
            date = itemView.findViewById(R.id.date);
            name = itemView.findViewById(R.id.user_name);
            post = itemView.findViewById(R.id.post);
            delete = itemView.findViewById(R.id.delete);
            image = itemView.findViewById(R.id.user_image);
        }
    }

    PostAdapter(List<Post> dataset){
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public PostAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostAdapter.MyViewHolder holder, final int position) {
        Post currentPost = dataset.get(position);
        holder.post.setText(currentPost.getPost());
        if(currentPost.getImage().isEmpty()){
            holder.image.setImageResource(R.drawable.ic_person_black_24dp);
        } else {
            Glide.with(holder.image.getContext())
                    .load(currentPost.getImage())
                    .into(holder.image);
        }
        holder.name.setText(currentPost.getName());
        if (auth.getCurrentUser().getUid().equals(currentPost.getUserid())){
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.delete.setVisibility(View.GONE);
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setTitle("Delete Post");
                dialog.setMessage("Are you sure you want to Delete the post");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("Posts")
                                .document(dataset.get(position).getDate().getSeconds()+"")
                                .delete();
                        dataset.remove(position);
                        notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });
        SimpleDateFormat format = new SimpleDateFormat("K:mm a E, MMM d");
        String date = format.format(currentPost.getDate().toDate());
        holder.date.setText(date);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

}
