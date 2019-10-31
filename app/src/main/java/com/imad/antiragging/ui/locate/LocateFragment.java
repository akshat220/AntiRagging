package com.imad.antiragging.ui.locate;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.imad.antiragging.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class LocateFragment extends Fragment {

    private Button share, pick;
    private TextView address;
    private static final int MY_CODE = 94;
    private LocationManager locationManager;
    private LocationListener locationListener;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_locate, container, false);
        share = root.findViewById(R.id.share);
        pick = root.findViewById(R.id.pick);
        address = root.findViewById(R.id.location);
        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setAddress(location);
                share.setEnabled(true);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_CODE);
                } else {
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                0, 0, locationListener);
                    }else{
                        showGPSDisabledAlert();
                    }
                }
            }
        });

        share.setEnabled(false);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return root;
    }

    private void setAddress(Location location){
        Geocoder geocoder = new Geocoder(getContext(), Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String a = addresses.get(0).getAddressLine(0);
            address.setText(a);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showGPSDisabledAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder
                .setTitle("GPS disabled")
                .setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }
}