package com.example.friendsapp.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.friendsapp.BE.BEFriend;
import com.example.friendsapp.Data.DataAccessFactory;
import com.example.friendsapp.Data.IDataAccess;
import com.example.friendsapp.FriendAdapter;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Date;
import java.util.ArrayList;

import com.example.friendsapp.R;

public class MainActivity extends AppCompatActivity {
    ListView lvFriends;
    FriendAdapter fa;
    ArrayList<BEFriend> friends;
    IDataAccess dataAccess;

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
        intent.putExtra("ID", id);
        startActivity(intent);
    }
}
