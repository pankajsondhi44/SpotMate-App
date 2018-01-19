package com.spotmate.spotmate;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class Locationservice extends Service {

    private String username;
    private static final String TAG = "location";

    private LocationListener listener;
    private LocationManager locationManager;

    private String db = "https://spotmate-feb1e.firebaseio.com/";
    private DatabaseReference mDatabase;

    private long Location_interval = 1000*60*5;
    private float Location_distance = 0; // in meters

    private Date date;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(db);
        Calendar calendar = Calendar.getInstance();
        date = calendar.getTime();

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Intent i = new Intent("location_update");
                i.putExtra("longitude", location.getLongitude());
                i.putExtra("latitude", location.getLatitude());
                Log.e(TAG, "onLocationChanged: " + location.getLongitude() + " " + location.getLatitude());

                String lat = "latitude";
                String lon = "longitude";

                mDatabase.child("location").child(username).child(lon).setValue(location.getLongitude());
                mDatabase.child("location").child(username).child(lat).setValue(location.getLatitude());
                mDatabase.child("location").child(username).child("time_of_last_location").setValue(date.toString());

                sendBroadcast(i);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Log.e(TAG, "onStatusChanged: ");
            }

            @Override
            public void onProviderEnabled(String s) {
                Log.e(TAG, "onProviderEnabled: ");
            }

            @Override
            public void onProviderDisabled(String s) {
                Log.e(TAG, "onProviderDisabled: ");
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Location_interval, Location_distance, listener);
        }
        catch (java.lang.SecurityException ex) {
            Log.d(TAG, "failed to get location");
            Log.d(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null) {
            //noinspection MissingPermission
            mDatabase.child("service").child(username).setValue("off");
            locationManager.removeUpdates(listener);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        username = intent.getStringExtra("email");
        mDatabase.child("service").child(username).setValue("on");
        Log.e(TAG, "username: " + username);
        return super.onStartCommand(intent, flags, startId);
    }
}