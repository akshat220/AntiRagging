package com.imad.antiragging.ui.squad;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.imad.antiragging.R;
import com.imad.antiragging.data.Member;

import java.util.List;

public class SquadAdapter extends RecyclerView.Adapter<SquadAdapter.MyViewHolder> {

    private FirebaseFirestore db;
    private List<Member> dataset;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView role;
        private TextView name;
        private ImageButton call;

        MyViewHolder(final View itemView) {
            super(itemView);
            role = itemView.findViewById(R.id.role);
            name = itemView.findViewById(R.id.name);
            call = itemView.findViewById(R.id.call_button);
        }
    }

    SquadAdapter(List<Member> dataset) {
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public SquadAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_member, parent, false);
        db = FirebaseFirestore.getInstance();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SquadAdapter.MyViewHolder holder, final int position) {
        final Member currentMember = dataset.get(position);
        holder.role.setText(currentMember.getRole());
        holder.name.setText(currentMember.getName());
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + currentMember.getPhone()));
                if (ActivityCompat.checkSelfPermission(holder.call.getContext(),Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                view.getContext().startActivity(callIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

}