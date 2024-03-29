package com.example.friendsapp.Model;


import android.location.Location;
import android.os.Bundle;
import android.util.Log;

public class LocationListener implements android.location.LocationListener {

    private String TAG = "LOCATIONLISTENER";
    IViewCallBack m_view;

    public LocationListener(IViewCallBack view) {
        m_view = view;
    }

    public void onStatusChanged(String provider, int status,
                                Bundle extras) {
        // called when the provider status changes. Possible status:
        // OUT_OF_SERVICE, TEMPORARILY_UNAVAILABLE or AVAILABLE.
    }

    public void onProviderEnabled(String provider) {
        // called when the provider is enabled by the user
    }

    public void onProviderDisabled(String provider) {
        // called when the provider is disabled by the user, if it's
        // already disabled, it's called immediately after
        // requestLocationUpdates
    }

    int count = 0;

    public void onLocationChanged(Location location) {
        Log.d(TAG, "Notified on onLocationChanged");
        m_view.setCurrentLocation(location);
    }
}
