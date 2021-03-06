package com.myapplicationdev.android.c347_l9_ps_getting_my_location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

    GoogleMap map;
    Button btnGetLocationUpdate, btnRemoveLocationUpdate, btnCheckRecords;
    TextView tvLocation, tvLatitude, tvLongitude;
    LatLng downtownCoreCoordinates;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    Marker central;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetLocationUpdate = findViewById(R.id.btnGetLocationUpdate);
        btnRemoveLocationUpdate = findViewById(R.id.btnRemoveLocationUpdate);
        btnCheckRecords = findViewById(R.id.btnCheckRecords);
        tvLocation = findViewById(R.id.tvLocation);
        tvLatitude = findViewById(R.id.tvLatitude);
        tvLongitude = findViewById(R.id.tvLongitude);

        // The primary point of contact with the fused location provider.
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        // FragmentManager for interacting with fragments associated with this activity.
        FragmentManager supportFragmentManager = getSupportFragmentManager();

        // The SupportMapFragment is the simplest way to include a map in an application.
        SupportMapFragment mapFragment = (SupportMapFragment) supportFragmentManager.findFragmentById(R.id.map);

        // TODO: Folder creation
        String myFolderLocation = getFilesDir().getAbsolutePath() + "/Folder";

        // by converting the given pathname string into an abstract pathname,
        // a new File object is created.
        File myFolder = new File(myFolderLocation);

        // checks to see if the file/directory specified by this abstract pathname exists.
        if (!myFolder.exists()) {
            boolean result = myFolder.mkdir(); // This function creates the directory specified by the abstract pathname.

            if (result) {
                Log.d("File Read/Write", "Folder created");
            }
        }

        assert mapFragment != null;

        // TODO: get the instance of the googleMap
        mapFragment.getMapAsync((GoogleMap googleMap) -> {

            map = googleMap;
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);// sets the map type to be NORMAL

            checkPermission(); // checking permission

            //Returns the most recent location that is currently available in the form of a Task.
            Task<Location> currentLocation = client.getLastLocation();

            // TODO: get the instance of the current Location
            currentLocation.addOnSuccessListener(MainActivity.this, (Location location) -> {

                if (location != null) {

                    tvLocation.setText("Last Location:");
                    tvLatitude.setText(location.getLatitude() + "");
                    tvLongitude.setText(location.getLongitude() + "");

                    // an immutable object storing a pair of latitude and longitude coordinates as degrees
                    downtownCoreCoordinates = new LatLng(location.getLatitude(), location.getLongitude());

                    // A class that contains methods for creating CameraUpdate objects that change the camera on a map.
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(downtownCoreCoordinates, 11));

                    central = map.addMarker(new MarkerOptions()
                            .position(downtownCoreCoordinates)
                            .title("Last Location")
                            .snippet("user's last location")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

                    );
                } else {
                    String toastMsg = "No Last Known Location is found";
                    Toast.makeText(MainActivity.this, toastMsg, Toast.LENGTH_SHORT).show();
                }
            });

            // once the map location's marker has been clicked
            map.setOnMarkerClickListener((Marker marker) -> {
                Toast.makeText(MainActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            });

            // Gets the user interface settings for the map.
            UiSettings ui = map.getUiSettings();

            ui.setCompassEnabled(true); // Enables  the compass.
            ui.setZoomControlsEnabled(true); // Enables the zoom controls

            // Determine whether you have been granted a particular permission.
            int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

            // if permission is granted
            if (permissionCheck == PermissionChecker.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);
                // if permission is NOT granted
            } else {
                Log.e("Gmap-Permission", "Gps access has not been granted");
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }


        });
        // TODO: get location update information
        btnGetLocationUpdate.setOnClickListener(v -> {
            checkPermission();

            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30000);
            locationRequest.setFastestInterval(5000);
            locationRequest.setSmallestDisplacement(500);


            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    Location data = locationResult.getLastLocation();

                    double lat = data.getLatitude();
                    double lng = data.getLongitude();

                    String toastMsg = "Latitude:" + lat + " Longitude:" + lng;
                    Toast.makeText(MainActivity.this, toastMsg, Toast.LENGTH_SHORT).show();
                    tvLocation.setText("Last Location:");
                    tvLatitude.setText(lat + "");
                    tvLongitude.setText(lng + "");
                    downtownCoreCoordinates = new LatLng(lat, lng);

                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(downtownCoreCoordinates, 11));


                    LatLng newloc = new LatLng(data.getLatitude(), data.getLongitude());
                    if (central == null) {
                        central = map.addMarker(new MarkerOptions()
                                .position(newloc)
                                .title("Last Location")
                                .snippet("user's last location")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    } else {
                        central.setPosition(newloc);
                        try {
                            String myFolderLocation1 = getFilesDir().getAbsolutePath() + "/Folder";
                            File myTargetFile = new File(myFolderLocation1, "location.txt");
                            FileWriter myFileWriter = new FileWriter(myTargetFile, true);
                            myFileWriter.write(newloc.latitude + "," + newloc.longitude + "\n");
                            myFileWriter.flush();
                            myFileWriter.close();
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this,
                                    "Failed to write!",
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();

                        }
                    }
                }

            };

            client.requestLocationUpdates(locationRequest, locationCallback, null);

        });

        btnRemoveLocationUpdate.setOnClickListener((View v) -> {
            checkPermission();
            client.removeLocationUpdates(locationCallback);
            Toast.makeText(MainActivity.this,
                    "The given location result listener's location updates have been removed.",
                    Toast.LENGTH_LONG).show();
        });

        btnCheckRecords.setOnClickListener((View v) -> {
            Intent newIntent = new Intent(MainActivity.this, LocationListActivity.class);
            startActivity(newIntent);
            Toast.makeText(MainActivity.this,
                    "here are the records.",
                    Toast.LENGTH_LONG).show();
        });


    }

    private void checkPermission() {
        ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

    }
}