package com.javaawesome.tagtesting;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;
    LatLng startingPoint;
    Session gameSession;
    final static long REFRESHRATE = 3*1000;
    final static int SUBJECT = 0;
    Handler locationHandler;
    private int index = 0;
    LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        startingPoint = new LatLng(47.653120, -122.351991);
        gameSession = new Session("testing", startingPoint, 200);
        Player me = new Player("Qyoung", gameSession);
        Player picolas = new Player("Picolas", gameSession);

        gameSession.addPlayer(me);
        gameSession.addPlayer(picolas);
        me.addLocations(new LatLng(47.653200, -122.352200));
        picolas.addLocations(new LatLng(47.653300, -122.351700));
        me.addLocations(new LatLng(47.653100, -122.352300));
        picolas.addLocations(new LatLng(47.653400, -122.351600));
        me.addLocations(new LatLng(47.653000, -122.352400));
        picolas.addLocations(new LatLng(47.653500, -122.351500));
        me.addLocations(new LatLng(47.652900, -122.352500));
        picolas.addLocations(new LatLng(47.653600, -122.351400));
        me.addLocations(new LatLng(47.652800, -122.352600));
        picolas.addLocations(new LatLng(47.653600, -122.351300));

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                updatePosition();
            }
        };

        mFusedLocationClient.requestLocationUpdates(getLocationRequest(), mLocationCallback, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTrackingLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);


//      Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(startingPoint).title("Game Center").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startingPoint));
        Circle gameBounds = mMap.addCircle(new CircleOptions()
                .center(startingPoint)
                .radius(gameSession.getRadius())
                .strokeColor(Color.RED)
                .fillColor(Color.TRANSPARENT)
                .strokeWidth(5));

        for (Player player: gameSession.getPlayers()) {
            mMap.addMarker(new MarkerOptions().position(player.getLocations().get(player.getLocations().size() - 1)).title(player.getUsername()));
            mMap.addCircle(new CircleOptions()
                    .center(player.getLocations().get(player.getLocations().size() - 1))
                    .radius(20)
                    .strokeColor(Color.BLUE)
                    .fillColor(Color.TRANSPARENT)
                    .strokeWidth(3));
        }

    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current Location:\n" + location, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "My Location button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    public void updatePosition() {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(startingPoint).title("Game Center").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startingPoint));
        Circle gameBounds = mMap.addCircle(new CircleOptions()
                .center(startingPoint)
                .radius(gameSession.getRadius())
                .strokeColor(Color.RED)
                .fillColor(Color.TRANSPARENT)
                .strokeWidth(5));

        for (Player player: gameSession.getPlayers()) {
            mMap.addMarker(new MarkerOptions().position(player.getLocations().get(index)).title(player.getUsername()));
            mMap.addCircle(new CircleOptions()
                    .center(player.getLocations().get(index))
                    .radius(20)
                    .strokeColor(Color.GREEN)
                    .fillColor(Color.TRANSPARENT)
                    .strokeWidth(2));
        }
        index++;
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        return locationRequest;
    }

    public void stopTrackingLocation() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(getLocationRequest(), mLocationCallback, Looper.getMainLooper());
    }
}
