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
import android.widget.ListView;

import com.example.friendsapp.BE.BEFriend;
import com.example.friendsapp.Data.DataAccessFactory;
import com.example.friendsapp.Data.IDataAccess;
import com.example.friendsapp.FriendAdapter;
import com.example.friendsapp.Shared;

import java.util.ArrayList;

import com.example.friendsapp.R;

public class MainActivity extends AppCompatActivity {
    static final int DETAILACTIVITY_INTENT_ID = 100;
    ListView lvFriends;
    FriendAdapter friendAdapter; //This is the adapter for the listview.
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
            case R.id.map_view:
                openMapViewWithAllFriends();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openMapViewWithAllFriends() {
        Intent mapIntent = new Intent(this, MapsActivity.class);
        mapIntent.putExtra(Shared.FRIENDS_KEY, friends.toArray(new BEFriend[]{}));
        startActivity(mapIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

        lvFriends = this.findViewById(R.id.lvFriends);
        DataAccessFactory factory = new DataAccessFactory(this);
        dataAccess = factory.getDataAccessUsing(DataAccessFactory.DataTechnology.SQLite);

        // GET FRIENDS FROM DATABASE
        friends = new ArrayList<>(dataAccess.getAllFriends());

        lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openFriendDetailView(position);
            }
        });

        friendAdapter = new FriendAdapter(this, R.layout.friendlistcell, friends);
        lvFriends.setAdapter(friendAdapter);
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
        if (requestCode == DETAILACTIVITY_INTENT_ID && resultCode == RESULT_OK){
            friends = new ArrayList<>(dataAccess.getAllFriends());
            friendAdapter.clear();
            friendAdapter.addAll(friends);
        }
    }
}