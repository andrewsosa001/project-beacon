package com.andrewsosa.beacon;

import java.util.ArrayList;

import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.location.LocationManager;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.UiSettings;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapViewFragment extends BeaconFragment implements OnMapReadyCallback {

    private MapFragment mapFragment;
    public static String TAG = "MAP_VIEW_FRAGMENT";


    public MapViewFragment() {
        // Required empty public constructor
    }

    GoogleMap googleMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mapFragment = (MapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        Log.d("Beacon", "onMapReady()");

        this.googleMap = gMap;

        googleMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, false);

        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //set map type
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);



   // Get latitude of the current location

        double latitude;
        double longitude;

        try {
            latitude = myLocation.getLatitude();
            longitude = myLocation.getLongitude();
        } catch (Exception e) {
            latitude = 0;
            longitude = 0;
        }

        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Show the current location in Google Map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14));


        //googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!"));
        ArrayList<Beacon> beacons = ((MainActivity)getActivity()).getBeaconList();

        for(int i = 0; i < beacons.size(); ++i)
        {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(beacons.get(i).getLatitude(), beacons.get(i).getLongitude()))
                    .title(beacons.get(i).getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));

        }

    }

    public void updateDataSet(ArrayList<Beacon> beacons) {
        googleMap.clear();

        for(int i = 0; i < beacons.size(); ++i)
        {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(beacons.get(i).getLatitude(), beacons.get(i).getLongitude()))
                    .title(beacons.get(i).getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));

        }
    }

    @Override
    public void updateDataSet(Cursor c) {

    }
}
