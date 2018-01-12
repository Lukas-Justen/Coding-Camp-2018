package de.th_bingen.mobilecomputing.codingcamp2018;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 15123;
    private GoogleMap mMap;
    private FloatingActionButton fab;
    private static final int DRAW_NONE = 0, DRAW_POLYGON = 1, DRAW_CIRCLE = 2, DRAW_LINE = 3;

    private int draw = DRAW_NONE;
    private List<LatLng> points = new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setItems(R.array.shapes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        draw = i + 1;
                        points.clear();
                    }
                });
                builder.show();
            }
        });
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            return;
        }
        initMap();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_FINE_LOCATION) {
            initMap();
        }
    }

    @SuppressLint("MissingPermission")
    private void initMap() {
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (draw != DRAW_NONE) {
            points.add(latLng);
            draw();
        }
    }

    private void draw() {
        switch (draw) {
            case DRAW_POLYGON:
                PolygonOptions polygonOptions = new PolygonOptions().add(points.toArray(new LatLng[points.size()]));
                mMap.addPolygon(polygonOptions);
                break;
            case DRAW_CIRCLE:
                CircleOptions circleOptions = new CircleOptions().center(points.get(0)).radius(5 * 1000);
                mMap.addCircle(circleOptions);
                break;
            case DRAW_LINE:
                PolylineOptions polylineOptions = new PolylineOptions().add(points.toArray(new LatLng[points.size()]));
                mMap.addPolyline(polylineOptions);
                break;
        }
    }

}
