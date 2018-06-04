package com.example.alim.bcm.utilities;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

public interface AsynchResponse {
    public void onResponse(HashMap<String, LatLng>list);
}
