package com.example.civiladvocacy;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static LinearLayoutManager linearLayoutManager;
    private TextView textView;
    private RecyclerView recyclerView;
    private static IndividualListAdapter individualListAdapter;

    public static NormalizedInput normalizedInput;
    public static ArrayList<Offices> offices;
    public static ArrayList<Officials> officials;

    public String city = "Washington D.C";
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST = 111;

    private static String locationString = "Unspecified Location";
    private static String deniedText = "Location Access Denied";
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.locationText);
        recyclerView = findViewById(R.id.mainRView);

        mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);

        if(savedInstanceState != null){
            locationString = savedInstanceState.getString("location");
            GetCivilAdvocacyData dataObj = new GetCivilAdvocacyData();
            dataObj.downloadData(this, locationString);
            textView.setText(locationString);
        } else if(!hasNetworkConnection()){
            textView.setText("No Internet Connection");
            Intent intent = new Intent(this, NoInternetConnection.class);
            startActivity(intent);
        }
        else {
            determineLocation();
        }
        activityResultLauncher = registerForActivityResult
                (new ActivityResultContracts.StartActivityForResult(), this::handleResult);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("location", locationString);
        super.onSaveInstanceState(outState);
    }

    private void handleResult(ActivityResult result) {

    }

    public void showCivilAdvocacies(NormalizedInput normalizedInput_, ArrayList<Offices> aloffc, ArrayList<Officials> aloffcls)
    {
        normalizedInput = normalizedInput_;
        Log.d(TAG, "onCreate: getting normalized data"+ normalizedInput);
        offices = aloffc;
        officials = aloffcls;

        individualListAdapter = new IndividualListAdapter(getIndividualList(), this);
        recyclerView.setAdapter(individualListAdapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public boolean onCreateOptionsMenu(@NotNull Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        if (menu.getItemId() == R.id.about) {
            Intent intent = new Intent(this, AboutActivity.class);
            activityResultLauncher.launch(intent);
        } else if( menu.getItemId() == R.id.search){
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setTitle("Enter a Location");
            final EditText inp = new EditText(this);
            inp.setInputType(InputType.TYPE_CLASS_TEXT);
            alertBuilder.setView(inp);
            alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    locationString = inp.getText().toString();
                    GetCivilAdvocacyData dataObj = new GetCivilAdvocacyData();
                    dataObj.downloadData(MainActivity.this, locationString);
                    textView.setText(locationString);

                }
            });

            alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            alertBuilder.show();
        }
        return true;
    }

    public static ArrayList<IndividualListClass> getIndividualList(){

        ArrayList<IndividualListClass> al = new ArrayList<>();
        for(int i=0;i<offices.size();i++){
            String name = offices.get(i).name;
            for(int j=0;j<offices.get(i).officIndices.size();j++){
                Officials offcs =  officials.get(offices.get(i).officIndices.get(j));
                HashMap<String, String> hm = offcs.channels;
                HashMap<String, String> addr = offcs.address;

                String address = addr.getOrDefault("line1", "")+addr.getOrDefault("line3", "")+addr.getOrDefault("line3", "")+
                        ", "+addr.get("city")+",\n"+addr.get("state")+", "+addr.get("zip");
                String facebook = hm.getOrDefault("Facebook","");
                String twitter = hm.getOrDefault("Twitter", "");
                String youtube = hm.getOrDefault("You Tube", "");
                al.add(new IndividualListClass(offcs.photoUrl,offcs.name,name,offcs.party,address,offcs.phoneNumber,offcs.email,offcs.url,facebook,twitter,youtube ));
            }
        }
        al.stream().forEach(x -> Log.d(TAG, "getIndividualList: "+ x));
        return al;
    }


    // Location Block ---------------------------------------------------------------------------------------------
    private void determineLocation() {
        // Check perm - if not then start the  request and return
        Log.d(TAG, "determineLocation: Inside Determine Location");
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        Log.d(TAG, "determineLocation: Inside Determine Location After If");
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some situations this can be null.
                    if (location != null) {
                        Log.d(TAG, "determineLocation: "+ location);
                        locationString = getPlace(location);
                        city = locationString;
                        Log.d(TAG, "determineLocation: "+city);

                        GetCivilAdvocacyData dataObj = new GetCivilAdvocacyData();
                        dataObj.downloadData(this, locationString);
                        textView.setText(locationString);
                    }
                })
                .addOnFailureListener(this, e ->{
                        Toast.makeText(MainActivity.this,
                                e.getMessage(), Toast.LENGTH_LONG).show();

                    Log.d(TAG, "determineLocation: Error in finding the location" );
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                } else {
                    textView.setText(deniedText);
                }
            }
        }
    }

    private String getPlace(Location loc) {

        StringBuilder sb = new StringBuilder();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            sb.append(String.format(
                    Locale.getDefault(),
                    "%s, %s",
                    city, state));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    //---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onClick(View view){
        int position = recyclerView.getChildAdapterPosition(view);
        IndividualListClass ivl = getIndividualList().get(position);
        Intent intent = new Intent(this, officialActivity.class);
        intent.putExtra("individualListClass", ivl);
        intent.putExtra("location", locationString);
        activityResultLauncher.launch(intent);

    }

    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

}


