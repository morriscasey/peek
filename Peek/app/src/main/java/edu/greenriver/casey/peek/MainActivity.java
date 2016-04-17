package edu.greenriver.casey.peek;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOptions;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.android.map.MapOptions;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.tasks.ags.geoprocessing.GPServiceInfo;
import com.esri.core.tasks.na.RouteParameters;
import com.esri.*;

import java.security.Provider;

public class MainActivity extends AppCompatActivity implements LocationListener {
    float gpsY;
    float gpsX;

    // The basemap switching menu items.
    MenuItem mStreetsMenuItem = null;
    MenuItem mTopoMenuItem = null;
    MenuItem mGrayMenuItem = null;
    MenuItem mOceansMenuItem = null;

    // Create MapOptions for each type of basemap.
    final MapOptions mTopoBasemap = new MapOptions(MapOptions.MapType.TOPO);
    final MapOptions mStreetsBasemap = new MapOptions(MapOptions.MapType.STREETS);
    final MapOptions mGrayBasemap = new MapOptions(MapOptions.MapType.GRAY);
    final MapOptions mOceansBasemap = new MapOptions(MapOptions.MapType.OCEANS);


    MapView mMapView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // wait for web map to load prior to adding graphic layer
        mMapView = (MapView) findViewById(R.id.map);

        // Enable map to wrap around date line.
        mMapView.enableWrapAround(true);

        // GPS Initialize
        gpsX = 0;
        gpsY = 0;

        final Button button = (Button) findViewById(R.id.button);

        // Enable location services
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, this);
            return;
        }

        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);


        // Locations service enabled
        boolean isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void onStatusChanged(Object source, STATUS status) {
                if (source == mMapView && status == STATUS.INITIALIZED) {

                    // Retrieve point from gps
                    Point point = mMapView.toMapPoint(gpsX, gpsY);

                    // Symbol to display
                    SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(Color.BLUE, 24, SimpleMarkerSymbol.STYLE.DIAMOND);

                    // creates a new graphic layer that will show route
                    GraphicsLayer routeLayer = new GraphicsLayer();

                    // Adds point and symbol to route layer
                    routeLayer.addGraphic(new Graphic(point, symbol));

                    // Add to layer
                    mMapView.addLayer(routeLayer);
                }
            }

        });



        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                if(button.getText().equals("Start"))
                {
                    turnGPSOn();
                    button.setText("Stop");
                }else
                {
                    turnGPSOff();
                    button.setText("Start");
                }

            }
        });



    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        // Get the basemap switching menu items.
        mStreetsMenuItem = menu.getItem(0);
        mTopoMenuItem = menu.getItem(1);
        mGrayMenuItem = menu.getItem(2);
        mOceansMenuItem = menu.getItem(3);

        // Also set the topo basemap menu item to be checked, as this is the default.
        mTopoMenuItem.setChecked(true);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item selection.
        switch (item.getItemId()) {
            case R.id.World_Street_Map:
                mMapView.setMapOptions(mStreetsBasemap);
                mStreetsMenuItem.setChecked(true);
                return true;
            case R.id.World_Topo:
                mMapView.setMapOptions(mTopoBasemap);
                mTopoMenuItem.setChecked(true);
                return true;
            case R.id.Gray:
                mMapView.setMapOptions(mGrayBasemap);
                mGrayMenuItem.setChecked(true);
                return true;
            case R.id.Ocean_Basemap:
                mMapView.setMapOptions(mOceansBasemap);
                mOceansMenuItem.setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onPause() {
        super.onPause();
        mMapView.pause();
    }

    protected void onResume() {
        super.onResume();
        mMapView.unpause();
    }

    // Turn on GPS
    public void turnGPSOn()
    {

    }

    // Turn off GPS
    public void turnGPSOff()
    {

    }

    @Override
    public void onLocationChanged(Location location) {
        gpsX = (float) location.getLatitude();
        gpsY = (float) location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

