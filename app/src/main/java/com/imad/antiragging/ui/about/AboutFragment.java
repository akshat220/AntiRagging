package com.imad.antiragging.ui.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.imad.antiragging.R;

import java.util.ArrayList;
import java.util.List;

public class AboutFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);

        List<String> dataset = new ArrayList<>();
        dataset.add(getString(R.string.guideline1));
        dataset.add(getString(R.string.guideline2));
        dataset.add(getString(R.string.guideline3));
        dataset.add(getString(R.string.guideline4));
        dataset.add(getString(R.string.guideline5));
        dataset.add(getString(R.string.guideline6));
        dataset.add(getString(R.string.guideline7));
        dataset.add(getString(R.string.guideline8));
        dataset.add(getString(R.string.guideline9));
        dataset.add(getString(R.string.guideline10));
        dataset.add(getString(R.string.guideline11));
        dataset.add(getString(R.string.guideline12));

        RecyclerView list = root.findViewById(R.id.guidelines_list);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(list.getContext(),
                RecyclerView.VERTICAL);
        list.addItemDecoration(dividerItemDecoration);
        GuidelinesAdapter guidelinesAdapter = new GuidelinesAdapter(dataset);
        list.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        list.setAdapter(guidelinesAdapter);
        return root;
    }
}