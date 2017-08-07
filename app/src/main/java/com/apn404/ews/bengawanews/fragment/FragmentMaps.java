package com.apn404.ews.bengawanews.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.apn404.ews.bengawanews.R;
import com.apn404.ews.bengawanews.application.Controller;
import com.apn404.ews.bengawanews.utils.Server;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static android.content.ContentValues.TAG;

public class FragmentMaps extends Fragment implements OnMapReadyCallback,LocationListener {

    private LocationManager mLocationManager = null;
    private String provider = null;
    private Marker mCurrentPosition = null;

    private GoogleMap mMap;
    private String[] id_lokasi,nama_lokasi,ketinggian_air,date_time,status;
    int numData;
    LatLng latLng[];
    Boolean markerD[];
    private Double[] lattitude, longitude;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v =  inflater.inflate(R.layout.fragment_maps, container, false);

        return v;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment fragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.maps);
        fragment.getMapAsync(this);
        getLokasi();
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

        if (ContextCompat.checkSelfPermission(this.getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (isProviderAvailable() && (provider != null)) {
            locateCurrentPosition();
        }

    }

    private void locateCurrentPosition() {
        int status =getActivity().getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION,
                getActivity().getPackageName());
        if (status == PackageManager.PERMISSION_GRANTED) {
            Location location = mLocationManager.getLastKnownLocation(provider);
            updateWithNewLocation(location);
            //  mLocationManager.addGpsStatusListener(this);
            long minTime = 5000;// ms
            float minDist = 5.0f;// meter
            mLocationManager.requestLocationUpdates(provider, minTime, minDist,
                    this);
        }
    }
    private boolean isProviderAvailable() {
        mLocationManager = (LocationManager) getActivity().getSystemService(
                Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        provider = mLocationManager.getBestProvider(criteria, true);
        if (mLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
            return true;
        }
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
            return true;
        }
        if (provider != null) {
            return true;
        }
        return false;
    }
    private void updateWithNewLocation(Location location) {
        if (location != null && provider != null) {
            double lng = location.getLongitude();
            double lat = location.getLatitude();
            addBoundaryToCurrentPosition(lat, lng);
            CameraPosition camPosition = new CameraPosition.Builder()
                    .target(new LatLng(lat, lng)).zoom(10f).build();
            if (mMap != null)
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(camPosition));
        } else {
            Log.d("Location error", "Something went wrong");
        }
    }
    private void addBoundaryToCurrentPosition(double lat, double lang) {
        MarkerOptions mMarkerOptions = new MarkerOptions();
        mMarkerOptions.position(new LatLng(lat, lang));
        mMarkerOptions.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.atm));
        mMarkerOptions.anchor(0.5f, 0.5f);
        CircleOptions mOptions = new CircleOptions()
                .center(new LatLng(lat, lang)).radius(10000)
                .strokeColor(0x110000FF).strokeWidth(1).fillColor(0x110000FF);
        mMap.addCircle(mOptions);
        if (mCurrentPosition != null)
            mCurrentPosition.remove();
        mCurrentPosition = mMap.addMarker(mMarkerOptions);
    }
    @Override
    public void onLocationChanged(Location location) {
        updateWithNewLocation(location);
    }
    @Override
    public void onProviderDisabled(String provider) {
        updateWithNewLocation(null);
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                break;
            case LocationProvider.AVAILABLE:
                break;
        }
    }


    //mulai ngoding
    private void getLokasi() {

        String url_select 	 = Server.URL + "getMaps.php";

        JsonArrayRequest request = new JsonArrayRequest(url_select, new Response.Listener<JSONArray>() {
            //JsonArrayRequest request = new JsonArrayRequest
            @Override
            public void onResponse(JSONArray response) {
                numData = response.length();
                Log.d("DEBUG_", "Parse JSON");
                latLng = new LatLng[numData];
                markerD = new Boolean[numData];
                nama_lokasi = new String[numData];
                ketinggian_air = new String[numData];
                status = new String[numData];
                date_time = new String[numData];
                id_lokasi = new String[numData];
                lattitude = new Double[numData];
                longitude = new Double[numData];


                for (int i = 0; i < numData; i++) {
                    try {
                        JSONObject data = response.getJSONObject(i);
                        id_lokasi[i] = data.getString("id_lokasi");
                        latLng[i] = new LatLng(data.getDouble("lattitude"),
                                data.getDouble("longitude"));
                        nama_lokasi[i] = data.getString("nama_lokasi");
                        ketinggian_air[i] = data.getString("ketinggian_air");
                        date_time[i] = data.getString("date_time");
                        lattitude[i] = data.getDouble("lattitude");
                        longitude[i] = data.getDouble("longitude");
                        status[i] = data.getString("status");

                        if (Objects.equals(status[i], "SIAGA")){
                            markerD[i] = false;
                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng[i])
                                    .title(nama_lokasi[i]+" ("+status[i]+")")
                                    .snippet(ketinggian_air[i]+" M"+" ("+date_time[i]+")")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.kuning)));
                        }
                        else if (Objects.equals(status[i], "WASPADA")){
                            markerD[i] = false;
                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng[i])
                                    .title(nama_lokasi[i]+" ("+status[i]+")")
                                    .snippet(ketinggian_air[i]+" M"+" ("+date_time[i]+")")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.orange)));
                        }else if (Objects.equals(status[i], "AWAS")){
                            markerD[i] = false;
                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng[i])
                                    .title(nama_lokasi[i]+" ("+status[i]+")")
                                    .snippet(ketinggian_air[i]+" M"+" ("+date_time[i]+")")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.merah)));
                        }
                        else{
                            markerD[i] = false;
                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng[i])
                                    .title(nama_lokasi[i]+" ("+status[i]+")")
                                    .snippet(ketinggian_air[i]+" M"+" ("+date_time[i]+")")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.hijau)));
                        }

                    } catch (JSONException je) {
                        je.printStackTrace();
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng[i], 15.5f));
                }
            }

        } , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(),"Periksa Koneksi Internet",Toast.LENGTH_LONG).show();
            }
        });
        //Volley.newRequestQueue(this).add(request);
        Controller.getInstance().addToRequestQueue(request);
    }
}
