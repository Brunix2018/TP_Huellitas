package ar.edu.utn.frsf.isi.dam.TP_Huellitas;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapaFragment extends SupportMapFragment implements OnMapReadyCallback {
    private GoogleMap miMapa;
    private LocationManager locationManager;


    /************** OnMapaListener *********************/
    private OnMapaListener listener;

    public interface OnMapaListener {
        public void coordenadasSeleccionadas(LatLng c);
    }

    public OnMapaListener getListener() {
        return listener;
    }

    public void setListener(OnMapaListener listener) {
        this.listener = listener;
    }

    /************** OnMapaListener *********************/


    private GoogleMap.OnMapLongClickListener listenerClickLargo = new GoogleMap.OnMapLongClickListener() {
        @Override
        public void onMapLongClick(LatLng latLng) {
             listener.coordenadasSeleccionadas(latLng);
        }
    };


    public MapaFragment() { // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container,savedInstanceState);
        getMapAsync(this);
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        miMapa = map;
        if (ActivityCompat.checkSelfPermission(MapaFragment.this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(MapaFragment.this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MapaFragment.this.getActivity(),new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            if(!miMapa.isMyLocationEnabled()) miMapa.setMyLocationEnabled(true);

            LocationManager lm = (LocationManager)MapaFragment.this.getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (myLocation == null) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                String provider = lm.getBestProvider(criteria, true);
                myLocation = lm.getLastKnownLocation(provider);
            }

            if(myLocation!=null){
                LatLng userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                miMapa.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17), 1500, null);
                miMapa.setOnMapLongClickListener(listenerClickLargo);
            }
        }

    }



}