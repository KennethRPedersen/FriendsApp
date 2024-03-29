package com.example.friendsapp.View;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.friendsapp.BE.BEFriend;
import com.example.friendsapp.Data.DataAccessFactory;
import com.example.friendsapp.Data.IDataAccess;
import com.example.friendsapp.Model.FileHelper;
import com.example.friendsapp.Model.IViewCallBack;
import com.example.friendsapp.Model.LocationListener;
import com.example.friendsapp.R;
import com.example.friendsapp.Shared;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DetailActivity extends AppCompatActivity implements IViewCallBack {

    static final String LOGTAG = "DETAILACTIVITY";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

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
    LocationListener locListener;
    IDataAccess dataAccess;
    File mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setGui(); //Initiates the GUI
        setButtons(); //Makes OnClickListeners on all buttons
        getFriend();
        setLocListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_friend:
                deleteFriend(friend.getId());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Get friend from database from the id in the intent
     */
    private void getFriend() {
        long friendId = (long) getIntent().getSerializableExtra(Shared.ID_KEY);
        dataAccess = new DataAccessFactory(this)
                .getDataAccessUsing(DataAccessFactory.DataTechnology.SQLite);
        if (friendId > 0) {
            friend = dataAccess.getFriendById(friendId);
            initFields();
        } else {
            friend = new BEFriend();
            iv.setImageResource(R.drawable.picture_placeholder_with_text);
        }
    }

    private void deleteFriend(long id) {
        dataAccess.deleteFriend(id);
        stopListener();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * populate edit texts with friends details
     */
    private void initFields() {
        etName.setText(friend.getName());
        etWeb.setText(friend.getWebsite());
        etBDay.setText(friend.getBirthdate().toString());
        etMail.setText(friend.getEmail());
        etPhone.setText(friend.getPhoneNumber());
        etAddress.setText(friend.getAddress());
        if (friend.getImgPath() != null) {
            iv.setImageURI(Uri.parse(friend.getImgPath()));
        } else {
            iv.setImageResource(R.drawable.picture_placeholder_with_text);
        }
    }

    /**
     * binds the ui
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


    /**
     * Sets all the on click listeners on the buttons
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
        etBDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
    }

    private void openCamera() {
        mFile = FileHelper.getOutputMediaFile(friend); // create a file to save the image
        if (mFile == null) {
            Toast.makeText(this, "Could not create file...", Toast.LENGTH_LONG).show();
            return;
        }

        // create Intent to take a picture
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                this,
                "com.example.friendsapp.example.provider", //(use your app signature + ".provider" )
                mFile));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        } else
            Log.d(LOGTAG, "camera app could NOT be started");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                iv.setImageURI(Uri.fromFile(mFile));
                friend.setImgPath(mFile.getPath());
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled...", Toast.LENGTH_LONG).show();
                return;
            } else
                Toast.makeText(this, "Picture NOT taken - unknown error...", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Opens a dialog window with a date picker inside.
     * On date selected we replace the text in etBDay.
     */
    private void openDatePicker() {
        final Calendar c = Calendar.getInstance(); // Get Current Date

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        etBDay.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    /**
     * returns a Date based on the a dateString written as yyyy-MM-dd
     * the divider can be what ever you want, as long as its the same as one used in the dateString
     * @param dateString
     * @param divider
     * @return Date
     */
    private Date getDateFromString(String dateString, String divider) {
        try {
            Long millis = new SimpleDateFormat("yyyy" + divider + "MM" + divider + "dd").parse(dateString).getTime();
            return new Date(millis);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Stops the Location listener when the back button is pressed
     */
    @Override
    public void onBackPressed() {
        stopListener();
        super.onBackPressed();
    }

    /**
     * Stops the listener and closes the activity
     */
    private void cancel() {
        stopListener();
        finish();
    }

    /**
     * Checkes the fields, if they are empty we prompt the user to enter what is missing
     * then makes a new friend object based on the fields
     * then saves it to the BD and closes the activity
     */
    private void saveToDB() {
        if (etName.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            return;
        } else if (etBDay.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter a date", Toast.LENGTH_LONG).show();
            return;
        } else if (etAddress.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter a address", Toast.LENGTH_LONG).show();
            return;
        } else if (etMail.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter a mail", Toast.LENGTH_LONG).show();
            return;
        } else if (etPhone.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_LONG).show();
            return;
        } else if (etWeb.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter a website", Toast.LENGTH_LONG).show();
            return;
        } else {
            Date date = getDateFromString(etBDay.getText().toString(), "-");

            BEFriend newFriend = new BEFriend(etName.getText().toString()
                    , etAddress.getText().toString()
                    , etMail.getText().toString()
                    , etPhone.getText().toString()
                    , etWeb.getText().toString()
                    , date
                    , friend.getHome()
                    , friend.getImgPath());

            newFriend.setId(friend.getId());
            dataAccess.addFriend(newFriend);
            stopListener();
            setResult(RESULT_OK);
            finish();
        }
    }

    /**
     * Opens the default browser with a website from the friend
     */
    private void openSite() {
        String url = friend.getWebsite();

        if (!url.contains("http")) url = "http://" + url;

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    /**
     * Opens the default mail app and set the receiver in the mail based on the mail in the friend object
     */
    private void sendMail() {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        //Fill it with Data
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{friend.getEmail()});

        //Send it off to the Activity-Chooser
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    /**
     * Opens the default SMS app and sets the receiver based on the number in the friend object
     */
    private void SendSms() {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + friend.getPhoneNumber()));
        startActivity(smsIntent);
    }

    /**
     * Makes a call the the number in the friend object
     */
    private void makeCall() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + friend.getPhoneNumber()));
        startActivity(callIntent);
    }

    /**
     * Starts a location listener
     */
    private void setLocListener() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locListener = new LocationListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        final int UPDATE_TIME = 1000;
        final int MIN_LOCATION_CHANGED_DISTANCE = 0;
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_TIME, MIN_LOCATION_CHANGED_DISTANCE, locListener);
    }

    /**
     * Removes the location listener from the location manager
     */
    private void stopListener() {
        if (locListener == null) return;
        lm.removeUpdates(locListener);
    }

    /**
     * Opens the map activity and sends the friend object as a extra in the intent
     * then stops the location listener
     */
    private void showOnMap() {
        if (friend.getHome() == null) {
            Toast.makeText(this, "Please set the home cords", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, MapsActivity.class);
        BEFriend[] friends = new BEFriend[]{friend};
        intent.putExtra(Shared.FRIENDS_KEY, friends);
        stopListener();
        startActivity(intent);
    }

    /**
     * Gets a location from the location manager and set the home in the friend object
     */
    private void setHome() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng latlng = new LatLng(loc.getLatitude(), loc.getLongitude());
        friend.setHome(latlng);
    }

    /**
     * Method form the LocationListener to see if its running
     *
     * @param location
     */
    @Override
    public void setCurrentLocation(Location location) {
        Log.d(LOGTAG, location.getLatitude() + " : " + location.getLongitude());
    }
}
