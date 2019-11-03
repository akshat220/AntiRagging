package com.imad.antiragging;

import android.Manifest;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class SosWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            SharedPreferences sp = context.getSharedPreferences("com.imad.antiragging.phone",
                    Context.MODE_PRIVATE);

            Intent callIntent;
            String phone = sp.getString("Phone", "");
            if (phone.isEmpty()) {
                Toast.makeText(context, "Phone Number not saved. Please save a number", Toast.LENGTH_SHORT).show();
                callIntent = new Intent(context, LoginActivity.class);
            } else {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    callIntent = new Intent(context, LoginActivity.class);
                    Toast.makeText(context, "Permission to make calls not given.", Toast.LENGTH_SHORT).show();
                } else {
                    callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                }
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, callIntent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.sos_widget);
            views.setOnClickPendingIntent(R.id.call_button, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}

