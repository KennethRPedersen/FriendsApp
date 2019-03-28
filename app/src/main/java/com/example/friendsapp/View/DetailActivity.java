package com.example.friendsapp.View;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.friendsapp.BE.BEFriend;
import com.example.friendsapp.Data.DataAccessFactory;
import com.example.friendsapp.Data.IDataAccess;
import com.example.friendsapp.R;
import com.example.friendsapp.Shared;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Date;

public class DetailActivity extends AppCompatActivity {

    static final String LOGTAG = "DETAILACTIVITY";

    //EditText
    EditText etName;
    EditText etAddress;
    EditText etPhone;
    EditText etMail;
    EditText etBDay;
    EditText etWeb;

    //TextView
    TextView tvAddress;
    TextView tvPhone;
    TextView tvMail;
    TextView tvBDay;
    TextView tvWeb;

    //ImageView
    ImageView iv;

    //Buttons
    Button btnHome;
    Button btnShowMap;
    Button btnCall;
    Button btnSMS;
    Button btnMail;
    Button btnWeb;
    Button btnSave;
    Button btnCancel;

    BEFriend friend;
    LocationManager lm;
    IDataAccess dataAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(LOGTAG, "Detail Activity started");

        setGui(); //Initiates the GUI
        setButtons(); //Makes OnClickListeners on all buttons

        long id = (long) getIntent().getSerializableExtra("ID");

        Log.d(LOGTAG, id + "");

        DataAccessFactory factory = new DataAccessFactory(this);
        dataAccess = factory.getDataAccessUsing(DataAccessFactory.DataTechnology.SQLite);

        if (id > 0) {
            friend = dataAccess.getFriendById(id);
            initFields();
        } else {
            friend = new BEFriend();
        }

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    /*
    populate edit texts with friends details
     */
    private void initFields() {
        etName.setText(friend.getName());
        etWeb.setText(friend.getWebsite());
        etBDay.setText(friend.getBirthdate().toString());
        etMail.setText(friend.getEmail());
        etPhone.setText(friend.getPhoneNumber());
        etAddress.setText(friend.getAddress());
    }

    /*
        binds the ui
    */

    private void setGui() {
        //EditText
        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);
        etMail = findViewById(R.id.etMail);
        etBDay = findViewById(R.id.etBDay);
        etWeb = findViewById(R.id.etWeb);

        //TextView
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);
        tvMail = findViewById(R.id.tvMail);
        tvBDay = findViewById(R.id.tvBDay);
        tvWeb = findViewById(R.id.tvWeb);

        //ImageView
        iv = findViewById(R.id.iv);

        //Buttons
        btnHome = findViewById(R.id.btnHome);
        btnShowMap = findViewById(R.id.btnShowMap);
        btnCall = findViewById(R.id.btnCall);
        btnSMS = findViewById(R.id.btnSMS);
        btnMail = findViewById(R.id.btnMail);
        btnWeb = findViewById(R.id.btnWeb);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }


    /*
        Sets all the on click listeners on the buttons
    */
    private void setButtons() {

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHome();
            }
        });

        btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOnMap();
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });

        btnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendSms();
            }
        });

        btnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSite();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDB();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    private void cancel() {
        finish();
    }

    private void saveToDB() {
        BEFriend newFriend = new BEFriend(etName.getText().toString()
                , etAddress.getText().toString()
                , etMail.getText().toString()
                , etPhone.getText().toString()
                , etWeb.getText().toString()
                , new Date(11,11,11)
                , friend.getHome());

        newFriend.setId(friend.getId());
        dataAccess.addFriend(newFriend);
        finish();
    }

    private void openSite() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(friend.getWebsite()));
        startActivity(browserIntent);
    }

    private void sendMail() {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        /* Fill it with Data */
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{friend.getEmail()});


        /* Send it off to the Activity-Chooser */
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    private void SendSms() {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + friend.getPhoneNumber()));
        startActivity(smsIntent);
    }

    private void makeCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + friend.getPhoneNumber()));
        startActivity(callIntent);

    }

    private void showOnMap() {
        if (friend.getHome() == null) {
            Toast.makeText(this, "Please set the home cords", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, MapActivity.class);
        BEFriend[] friends = new BEFriend[]{friend};
        intent.putExtra(Shared.FRIENDS_KEY, friends);
        startActivity(intent);
    }

    private void setHome() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng latlng = new LatLng(loc.getLatitude(), loc.getLongitude());
        friend.setHome(latlng);
        Log.d(LOGTAG, "Home cords set");
    }
}
