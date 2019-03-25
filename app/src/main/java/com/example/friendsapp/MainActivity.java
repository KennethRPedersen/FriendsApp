package com.example.friendsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.friendsapp.BE.BEFriend;

import java.sql.Date;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    LinearLayout llMenuBar;
    ListView lvFriends;
    Button btnNew;
    FriendAdapter fa;
    ArrayList<BEFriend> friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvFriends = this.findViewById(R.id.lvFriends);
        friends = new ArrayList<BEFriend>();

        lvFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO OPEN DETAILS VIEW FOR SINGLE FRIEND
            }
        });
        friends.add(new BEFriend("Margrethe Alexandrine Þórhildur Ingrid", "TestAddress", "TestEmail",
                "TestPhoneNumber", "TestWebsite", new Date(11,11,11)));
        friends.add(new BEFriend("TestName222", "TestAddress222", "TestEmail222",
                "TestPhoneNumber222", "TestWebsit2e22", new Date(11,11,11)));

        fa = new FriendAdapter(this, R.layout.friendlistcell, friends);
        lvFriends.setAdapter(fa);
    }
}
