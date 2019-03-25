package com.example.friendsapp.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.friendsapp.R;

public class DetailActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //TODO get id
        //TODO get friend from id

        setGui(); //Initiates the GUI
        setButtons(); //Makes OnClickListeners on all buttons
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
    }

    private void saveToDB() {
    }

    private void openSite() {
    }

    private void sendMail() {
    }

    private void SendSms() {
    }

    private void makeCall() {
    }

    private void showOnMap() {
    }

    private void setHome() {
    }
}
