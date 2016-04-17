package com.example.admin.peek;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import com.esri.android.map.MapView;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.Menu;
import com.esri.android.map.MapOptions;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polygon;


public class MainActivity extends AppCompatActivity {

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


    //added this class variable to hold a reference to the MapView added to the layout
    // The MapView.
    MapView mMapView = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Retrieve the map and intial extent from XML layout
        mMapView = (MapView) findViewById(R.id.map);
        // Enable map to wrap around date line.
        mMapView.enableWrapAround(true);
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
}
