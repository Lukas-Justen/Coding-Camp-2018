package de.th_bingen.mobilecomputing.codingcamp2018;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener
{

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 15123;
    private GoogleMap mMap;
    private FloatingActionButton fab;
    private static final int DRAW_NONE = 0, DRAW_POLYGON = 1, DRAW_CIRCLE = 2, DRAW_LINE = 3;

    private int draw = DRAW_NONE;
    private List<LatLng> points = new ArrayList<>();
    private Polygon polygon;
    private Circle circle;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setItems(R.array.shapes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        draw = i+1;
                        points.clear();
                    }
                });
                builder.show();
            }
        });
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
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            return;
        }
        initMap();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if( requestCode == MY_PERMISSIONS_REQUEST_FINE_LOCATION )
        {
            initMap();
        }
    }

    @SuppressLint("MissingPermission")
    private void initMap()
    {
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onMapClick(LatLng latLng)
    {
        if( draw != DRAW_NONE )
        {
            points.add(latLng);
            draw();
        }
    }

    private void draw()
    {
        switch (draw)
        {
            case DRAW_POLYGON:
                if( polygon != null )
                    polygon.remove();
                PolygonOptions options = new PolygonOptions().add(points.toArray(new LatLng[points.size()]));
                polygon = mMap.addPolygon(options);
                break;
            case DRAW_CIRCLE:
                break;
            case DRAW_LINE:
                break;
        }
    }
}
