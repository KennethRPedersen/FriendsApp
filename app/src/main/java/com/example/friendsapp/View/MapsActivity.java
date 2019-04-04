package com.example.friendsapp.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.friendsapp.BE.BEFriend;
import com.example.friendsapp.R;
import com.example.friendsapp.Shared;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final long DOUBLECLICK_TIME_THRESHOLD = 10000;
    private GoogleMap mMap;
    private BEFriend[] friends;
    private int DETAILACTIVITY_INTENT_ID = 1345;
    private boolean markerClicked = false;
    private long lastClickedId;
    private long lastClickedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.friends = (BEFriend[]) getIntent().getSerializableExtra(Shared.FRIENDS_KEY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.new_friend:
                openActivityByFriendId(-1);
                return true;
            case R.id.list_view:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.getUiSettings().setZoomControlsEnabled(true);

        setHomeMarkersIn(googleMap);
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                openActivityByFriendId((long) marker.getTag());
            }
        });

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(friends[0].getHome()));
    }

    private void setHomeMarkersIn(GoogleMap map) {
        for (BEFriend friend:friends) {
            MarkerOptions markOpt = new MarkerOptions();
            LatLng home = friend.getHome();
            markOpt.position(home);
            markOpt.title(friend.getName());
            map.addMarker(markOpt).setTag(friend.getId());
        }
    }

    /**
     * Open a friend in detail view.
     * For adding a new friend give id -1.
     * @param id
     */
    private void openActivityByFriendId(long id){
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Shared.ID_KEY, id);
        startActivityForResult(intent, DETAILACTIVITY_INTENT_ID);
    }


}
