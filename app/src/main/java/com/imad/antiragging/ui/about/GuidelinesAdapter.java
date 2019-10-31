package com.imad.antiragging.ui.about;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.imad.antiragging.R;

import java.util.List;

public class GuidelinesAdapter extends RecyclerView.Adapter<GuidelinesAdapter.MyViewHolder> {

    private List<String> dataset;
    static class MyViewHolder extends  RecyclerView.ViewHolder{
        private TextView guideline;

        MyViewHolder(final View itemView){
            super(itemView);
            guideline = itemView.findViewById(R.id.guideline);
        }
    }

    GuidelinesAdapter(List<String> dataset){
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public GuidelinesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_guideline, parent, false);
        return new GuidelinesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GuidelinesAdapter.MyViewHolder holder, int position) {
        holder.guideline.setText(dataset.get(position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

}
