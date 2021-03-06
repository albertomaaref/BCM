package com.example.alim.bcm.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alim.bcm.R;
import com.example.alim.bcm.utilities.AsynchResponse;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.example.alim.bcm.data.model.Constants.TAG;


public class MapFragment extends Fragment implements OnMapReadyCallback, AsynchResponse {

    private GoogleMap mMap;
    private HashMap<String,LatLng> listaLatLng;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_maps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null && listaLatLng != null){

            LatLng milan = new LatLng(45.4642700, 9.18951);

            Iterator iterator = listaLatLng.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry pair = (Map.Entry) iterator.next();
                mMap.addMarker(new MarkerOptions().position((LatLng) pair.getValue()).title((String) pair.getKey()));

            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(milan));
            CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(milan, 9);
            mMap.animateCamera(zoom);
        }
    }

    @Override
    public void onResponse(HashMap<String,LatLng> list) {
        listaLatLng = list;
        onMapReady(mMap);
        Log.i(TAG,"risposta"+ list);
    }
}
