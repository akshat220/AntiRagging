package com.imad.antiragging.ui.squad;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imad.antiragging.R;
import com.imad.antiragging.data.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SquadFragment extends Fragment {

    private DatabaseReference reference;
    private List<Member> dataset;
    private SquadAdapter squadAdapter;
    private ProgressBar progressBar;
    private static final int MY_CODE = 56;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_squad, container, false);
        reference = FirebaseDatabase.getInstance().getReference();
        dataset = new ArrayList<>();

        if(ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_CODE);
        }
        progressBar = Objects.requireNonNull(getActivity()).findViewById(R.id.progress_bar);
        RecyclerView list = root.findViewById(R.id.member_list);
        squadAdapter = new SquadAdapter(dataset);
        list.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        list.setAdapter(squadAdapter);
        updateData();
        return root;
    }

    private void updateData(){
        progressBar.setVisibility(View.VISIBLE);
        reference.child("squad").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataset.clear();
                for(DataSnapshot member: dataSnapshot.getChildren()) {
                    dataset.add(member.getValue(Member.class));
                }
                squadAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] != PackageManager.PERMISSION_GRANTED || requestCode != MY_CODE) {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_home);
        }
    }
}