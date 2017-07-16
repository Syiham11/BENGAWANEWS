package com.apn404.ews.bengawanews.fragment;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class FragmentMaps extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String[] id_lokasi,nama_lokasi,ketinggian_air,date_time;
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

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

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

                        markerD[i] = false;
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng[i])
                                .title(nama_lokasi[i])
                                .snippet(ketinggian_air[i]+"CM")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.atm)));
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
                Toast.makeText(getActivity(),"Failed To Load",Toast.LENGTH_LONG).show();
            }
        });
        //Volley.newRequestQueue(this).add(request);
        Controller.getInstance().addToRequestQueue(request);
    }
}
