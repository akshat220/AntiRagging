package com.imad.antiragging.ui.squad;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.imad.antiragging.R;
import com.imad.antiragging.data.Member;

import java.util.ArrayList;
import java.util.List;

public class SquadFragment extends Fragment {

    private FirebaseFirestore db;
    private List<Member> dataset;
    private SquadAdapter squadAdapter;
    private static final int MY_CODE = 56;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_squad, container, false);
        db = FirebaseFirestore.getInstance();
        dataset = new ArrayList<>();

        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_CODE);
        }
        RecyclerView list = root.findViewById(R.id.member_list);
        squadAdapter = new SquadAdapter(dataset);
        list.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        list.setAdapter(squadAdapter);
        updateData();
        return root;
    }

    private void updateData(){
        db.collection("Squad")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Member> data = new ArrayList<>();
                        for (DocumentSnapshot document: queryDocumentSnapshots){
                            data.add(document.toObject(Member.class));
                        }
                        dataset.clear();
                        dataset.addAll(data);
                        squadAdapter.notifyDataSetChanged();
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