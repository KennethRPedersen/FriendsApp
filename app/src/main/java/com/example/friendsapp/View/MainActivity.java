package com.example.friendsapp.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.friendsapp.BE.BEFriend;
import com.example.friendsapp.Data.DataAccessFactory;
import com.example.friendsapp.Data.IDataAccess;
import com.example.friendsapp.FriendAdapter;
import com.example.friendsapp.Shared;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;

import com.example.friendsapp.R;

public class MainActivity extends AppCompatActivity {
    static final int DETAILACTIVITY_INTENT_ID = 100;
    ListView lvFriends;
    FriendAdapter fa; //This is the adapter for the listview.
    ArrayList<BEFriend> friends;
    IDataAccess dataAccess;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.new_friend:
                openFriendDetailView(-1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

        lvFriends = this.findViewById(R.id.lvFriends);
        DataAccessFactory factory = new DataAccessFactory(this);
        dataAccess = factory.getDataAccessUsing(DataAccessFactory.DataTechnology.SQLite);


        // GET MOCK FRIENDS
        friends = (ArrayList<BEFriend>) dataAccess.getAllFriends();
        friends.add(new BEFriend("Margrethe Alexandrine Þórhildur Ingrid", "TestAddress", "TestEmail",
                "TestPhoneNumber", "TestWebsite", new Date(11,11,11), new LatLng(0,0)));
        friends.add(new BEFriend("TestName222", "TestAddress222", "TestEmail222",
                "TestPhoneNumber222", "TestWebsit2e22", new Date(11,11,11), new LatLng(100,100)));

        // GET FRIENDS FROM DATABASE
        //friends = new ArrayList<>(dataAccess.getAllFriends());



        lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openFriendDetailView(position);
            }
        });

        fa = new FriendAdapter(this, R.layout.friendlistcell, friends);
        lvFriends.setAdapter(fa);
    }

    /**
     * Checks if the app has the required permissions, and prompts the user with the ones missing.
     */
    private void checkPermissions() {
        ArrayList<String> permissions = new ArrayList<String>();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.CAMERA);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.CALL_PHONE);


        if (permissions.size() > 0)
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), 1);
    }

    private void openFriendDetailView(int position) {
        long id = position == -1 ? -1 : friends.get(position).getId();
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Shared.ID_KEY, id);
        startActivityForResult(intent, DETAILACTIVITY_INTENT_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == DETAILACTIVITY_INTENT_ID)
            friends = new ArrayList<>(dataAccess.getAllFriends());
    }
}
