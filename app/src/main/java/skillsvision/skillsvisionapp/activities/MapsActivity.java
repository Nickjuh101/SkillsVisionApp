package skillsvision.skillsvisionapp.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import skillsvision.skillsvisionapp.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean locationGranted;

    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private LatLng latLng;
    private Marker marker;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final String TAG = "Log";
    private CameraPosition mCameraPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600){
            setContentView(R.layout.activity_maps);
        } else {
            setContentView(R.layout.activity_maps);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            locationGranted = extras.getBoolean("mLocationPermissionGranted");
        }

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        initMap();

        mGeoDataClient = Places.getGeoDataClient(this);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        TextView mAddMarker = (TextView) findViewById(R.id.fab);
        mAddMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).draggable(true).visible(true));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            }
        });

    }

    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
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
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
        updateLocationUI();

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                latLng = marker.getPosition();
            }
        });


//        getDeviceLocation();
    }



    private void initMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationGranted) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    initMap();
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                }
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            final Task locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(Task<Location> task) {
                    if (task.isSuccessful()) {
                            if (task.getResult() == null){
                                return;
                            } else {
                                mLastKnownLocation = task.getResult();
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), 16));

                                final LatLng location = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());

                                Geocoder currentLoc = new Geocoder(MapsActivity.this, Locale.getDefault());
                                try {
                                    List<Address> myList = currentLoc.getFromLocation(location.latitude, location.longitude, 1);
                                    Address address = myList.get(0);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                MarkerOptions marker = new MarkerOptions().position(location);

//                                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//                                    @Override
//                                    public View getInfoWindow(Marker marker) {
//                                        // Getting view from the layout file info_window_layout
//                                        View v = getLayoutInflater().inflate(R.layout.infowindow_marker, null);
//
//                                        // Getting the position from the marker
//                                        LatLng latLng = location;
//
//                                        // Getting reference to the TextView to set title
//                                        TextView tvTitle = v.findViewById(R.id.infowindow_title);
//
//                                        // Getting reference to the TextView to set address
//                                        TextView tvAddress = v.findViewById(R.id.infowindow_address);
//
//                                        // Getting reference to the TextView to set the remember message
//                                        TextView tvRemember = v.findViewById(R.id.infowindow_remember);
//
//                                        // Setting the title in the info window
//                                        tvTitle.setText("Uw Locatie:");
//
//                                        // Setting the address in the info window
//                                        tvAddress.setText(addressStr);
//
//                                        // Setting the remember message in the info window
//                                        tvRemember.setText("Onthoud deze locatie voor het telefoongesprek");
//
//                                        // Returning the view containing InfoWindow contents
//                                        return v;
//                                    }

//                                    @Override
//                                    public View getInfoContents(Marker marker) {
//                                        return null;
//                                    }


//                                mMap.addMarker(marker).showInfoWindow();
                                mMap.addMarker(marker).setDraggable(true);
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults. ");
                            Log.e(TAG, "Exception: %s", task.getException());
                        }
                    }
                });
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

}