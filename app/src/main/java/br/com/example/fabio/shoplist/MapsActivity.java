package br.com.example.fabio.shoplist;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;

import static br.com.example.fabio.shoplist.R.*;

public class MapsActivity extends FragmentActivity implements LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    LocationManager locationmanager;
    Location l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_maps);

        locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setUpMapIfNeeded();

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng point) {
                Intent it = new Intent();
                it.putExtra("latitude", point.latitude);
                it.putExtra("longitude", point.longitude);
                setResult(RESULT_OK, it);
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        if (locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000,0,this);
            l =  locationmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }else
        {
            locationmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000,0,this);
            l =  locationmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        double latitude = 0;
        double longitude = 0;
        CameraUpdate centro, zoom;
        if (l != null) {
            latitude = l.getLatitude();
            longitude =  l.getLongitude();
        }
        MarkerOptions maker =new MarkerOptions();
        maker.position(new LatLng(latitude, longitude));
        maker.title("Marker");
        maker.icon(BitmapDescriptorFactory.fromResource(mipmap.ic_launcher));
        mMap.addMarker(maker);
        centro = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
        zoom = CameraUpdateFactory.zoomTo(16);

        mMap.moveCamera(centro);
        mMap.animateCamera(zoom);

    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
