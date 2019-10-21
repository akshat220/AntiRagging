package com.imad.antiragging.ui.sos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.imad.antiragging.R;

public class SosFragment extends Fragment {

    private static final int MY_CODE = 8934;
    private EditText phoneNumber;
    private Button save, call;
    private SharedPreferences sp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sos, container, false);
        phoneNumber = root.findViewById(R.id.phone_number);
        save = root.findViewById(R.id.save_button);
        call = root.findViewById(R.id.call_button);

        sp = getContext().getSharedPreferences("com.imad.antiragging.phone",
                Context.MODE_PRIVATE);

        String savedPhone = sp.getString("Phone", "");
        phoneNumber.setText(savedPhone);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phoneNumber.getText().toString().isEmpty() ||
                        phoneNumber.getText().toString().length() != 10){
                    phoneNumber.setError("Enter Valid Phone Number");
                } else {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("Phone", phoneNumber.getText().toString());
                    editor.commit();
                    Toast.makeText(getContext(), "Phone Number Saved", Toast.LENGTH_SHORT).show();
                }
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED){
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_CODE);
                } else {
                    callNow();
                }
            }
        });
        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            callNow();
        }
    }

    private void callNow(){
        String phone = sp.getString("Phone", "");
        if(phone.isEmpty()){
            phoneNumber.setError("Phone Number not saved. Please save a number");
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivity(callIntent);
        }
    }
}