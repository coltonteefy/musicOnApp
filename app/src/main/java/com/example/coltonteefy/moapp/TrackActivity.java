package com.example.computerprogrammer.musicon;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;

public class TrackActivity extends Service implements LocationListener {
    private final Context context;
    Location location;
    protected LocationManager locMan;
    boolean isGPSenable = false;
    boolean networkEnable = false;
    boolean receiveLocation = false;
    public TrackActivity(Context context) {
        this.context = context;
    }
    public Location getLocation() {
        try {
            locMan = (LocationManager)context.getSystemService(LOCATION_SERVICE);
            isGPSenable = locMan.isProviderEnabled(locMan.GPS_PROVIDER);
            networkEnable = locMan.isProviderEnabled(locMan.NETWORK_PROVIDER);
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if(isGPSenable) {
                    if(location == null) {
                        locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, this);
                        if (locMan != null) {
                            location = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
                if(location == null) {
                    if(networkEnable) {
                        locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, this);
                        if(locMan != null) {
                            locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                    }
                }
            }
        }catch(Exception exc) {

        }
        return location;
    }
    public void onStatusChanged(String provider, int status, Bundle extra) {

    }
    public void onLocationChanged(Location location) {

    }
    public void onProviderEnabled(String provide) {

    }
    public void onProviderDisabled(String provide) {

    }
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
