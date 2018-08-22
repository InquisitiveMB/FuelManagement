package com.example.tankup;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.doubleclick.CustomRenderedAd;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Route;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

    public MediaPlayer mp;
    TextView dist;
    UserVehicleData helper=new UserVehicleData(this);
    private GoogleMap mMap;
    private Button btnFindPath;
    private EditText etOrigin;
    private EditText etDestination;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    private ProgressDialog progressDialog;
    String veh1,em;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mp= MediaPlayer.create(this,R.raw.beep);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        etOrigin = (EditText) findViewById(R.id.etOrigin);
        etDestination = (EditText) findViewById(R.id.etDestination);


        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

        Intent in=getIntent();
        veh1=in.getStringExtra("VEH");
        em=in.getStringExtra("mail");

    }

    private void sendRequest()
    {
        String origin = etOrigin.getText().toString();
        String destination = etDestination.getText().toString();
        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng karv= new LatLng(18.4898,73.8203);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(karv, 18));
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .title("Karvenagar")
                .position(karv)));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }


    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);
            //((TextView) findViewById(R.id.tvDistance)).setText("212 km");

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    void Verify_fuel(View v)
    {
        mp.start();
        String dist=((TextView) findViewById(R.id.tvDistance)).getText().toString();  //get distance calculated

        double mg=0,requiredFuel;
        String avaFuel = null;

        Cursor data=helper.getDisplay();   //get vehicles table data
        while(data.moveToNext())
        {
            String vname=data.getString(0);
            if(vname.equals(veh1) && data.getString(3).equals(em))
            {
                mg=data.getDouble(2);    //take mileage of vehicle
                break;
            }
        }
        Cursor data1=helper.getDisplay1();   //get fuels table data
        while(data1.moveToNext())
        {
            String vname=data1.getString(1);
            if(vname.equals(veh1) && data1.getString(2).equals(em))
            {
                avaFuel=data1.getString(0);    //take available fuel in vehicle
                break;
            }
        }
        int index=dist.indexOf(" ");
        String FDist=dist.substring(0, index);    //take substring og distance

        requiredFuel=(Double.parseDouble(FDist)/mg);   //calcuate require fuel for journey
        
        if(requiredFuel>Double.parseDouble(avaFuel))   //if fuel is sufficient for travelling
        {
            Toast.makeText(this,"You required more fuel...", Toast.LENGTH_SHORT).show();
            Intent in=new Intent();
            in.setClass(this,CanNotDrive.class);
            in.putExtra("calFuel", requiredFuel);
            in.putExtra("vnm", veh1);
            in.putExtra("paM", em);
            startActivity(in);
        }
        else    //if mor fuel require
        {
            Intent in=new Intent();
            in.setClass(this,CanDrive.class);
            in.putExtra("calFuel", requiredFuel);
            in.putExtra("vnm", veh1);
            in.putExtra("paM", em);
            startActivity(in);
        }

    }

}
