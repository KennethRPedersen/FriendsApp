package com.example.friendsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.friendsapp.BE.BEFriend;
import com.example.friendsapp.Data.DataAccessFactory;
import com.example.friendsapp.Data.IDataAccess;

import java.sql.Date;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    LinearLayout llMenuBar;
    ListView lvFriends;
    Button btnNew;
    FriendAdapter fa;
    ArrayList<BEFriend> friends;
    IDataAccess dataAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvFriends = this.findViewById(R.id.lvFriends);
        DataAccessFactory factory = new DataAccessFactory(this);
        dataAccess = factory.getDataAccessUsing(DataAccessFactory.DataTechnology.SQLite);


        // GET MOCK FRIENDS
        friends = new ArrayList<>();
        friends.add(new BEFriend("Margrethe Alexandrine Þórhildur Ingrid", "TestAddress", "TestEmail",
                "TestPhoneNumber", "TestWebsite", new Date(11,11,11)));
        friends.add(new BEFriend("TestName222", "TestAddress222", "TestEmail222",
                "TestPhoneNumber222", "TestWebsit2e22", new Date(11,11,11)));

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

    private void openFriendDetailView(int position) {
        long id = friends.get(position).getId();
        //TODO ADD DETAIL ACTIVITY TO INTENT
        Intent intent = new Intent();
        intent.putExtra("ID", id);
        startActivity(intent);
    }
}
