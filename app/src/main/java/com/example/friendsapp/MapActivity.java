package com.example.friendsapp;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.friendsapp.Data.DataAccessFactory;
import com.example.friendsapp.Data.IDataAccess;
import com.example.friendsapp.Exceptions.PermissionMissingException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.friendsapp.BE.BEFriend;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private BEFriend[] friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setupViewDependencies();
        getFriends();
    }

    /**
     * Gets the friends from intent if data is not null.
     * Else it will get all friends from the database.
     */
    private void getFriends() {
        Bundle extras = getIntent().getExtras();
        friends = (BEFriend[]) extras.getSerializable(Shared.FRIENDS_KEY);

        if (friends == null){
            friends = getAllFriendsFromData();
        }
    }

    /**
     * Gets all friends from the db.
     * @return array of friends
     */
    private BEFriend[] getAllFriendsFromData() {
        final IDataAccess dataAccess = new DataAccessFactory(this).getDataAccessUsing(DataAccessFactory.DataTechnology.SQLite);
        return dataAccess.getAllFriends().toArray(new BEFriend[]{});
    }

    /**
     * Setup inital view dependencies
     */
    private void setupViewDependencies() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Place dot on current location
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == getPackageManager().PERMISSION_GRANTED) {
                    throw new PermissionMissingException("Missing permission for location");
                }
            }
            map.setMyLocationEnabled(true);
        }catch (PermissionMissingException e){
            Log.e("", e.getMessage());
        }

        // Turns traffic layer on
        map.setTrafficEnabled(true);

        // Enables indoor maps
        map.setIndoorEnabled(true);

        // Turns on 3D buildings
        map.setBuildingsEnabled(true);

        // Show Zoom buttons
        map.getUiSettings().setZoomControlsEnabled(true);

        setHomeMarkersIn(map);
    }

    /**
     * Sets a Marker for every Friend based on their home location.
     * @param map the map to which to add the markers.
     */
    private void setHomeMarkersIn(GoogleMap map) {
        for (BEFriend friend:friends) {
            MarkerOptions markOpt = new MarkerOptions();
            LatLng home = friend.getHome();
            markOpt.position(home);
            markOpt.title(friend.getName());
            map.addMarker(markOpt);
        }
    }
}