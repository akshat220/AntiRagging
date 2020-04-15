package com.imad.antiragging.ui.home;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.imad.antiragging.R;
import com.imad.antiragging.data.Post;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private FirebaseDatabase fb;
    private FirebaseAuth auth;
    private List<Post> dataset;
    static class MyViewHolder extends  RecyclerView.ViewHolder{
        private TextView date;
        private TextView name;
        private TextView post;
        private ImageButton delete;
        private ImageButton edit;
        private ImageView image;

        MyViewHolder(final View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.user_name);
            post = itemView.findViewById(R.id.post);
            edit = itemView.findViewById(R.id.edit);
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
        fb = FirebaseDatabase.getInstance();
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
            holder.edit.setVisibility(View.VISIBLE);
        } else {
            holder.delete.setVisibility(View.GONE);
            holder.edit.setVisibility(View.GONE);
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setTitle(R.string.delete_post);
                dialog.setMessage(R.string.delete_post_content);
                dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fb.getReference().child("posts")
                                .child(dataset.get(position).getDate() + "")
                                .removeValue();
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
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(position, view);
            }
        });
        SimpleDateFormat format = new SimpleDateFormat("K:mm a E, MMM d", Locale.ENGLISH);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    private void showDialog(final int position, View view){
        View root = LayoutInflater.from(view.getContext()).inflate(R.layout.item_edit_post, null);
        final EditText editText = root.findViewById(R.id.edit);
        editText.setText(dataset.get(position).getPost());
        AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
        dialog.setTitle("Edit Post");
        dialog.setView(root);
        dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fb.getReference().child("posts")
                        .child(dataset.get(position).getDate() + "")
                        .child("post").setValue(editText.getText().toString());
                dataset.get(position).setPost(editText.getText().toString());
                notifyDataSetChanged();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }

}
