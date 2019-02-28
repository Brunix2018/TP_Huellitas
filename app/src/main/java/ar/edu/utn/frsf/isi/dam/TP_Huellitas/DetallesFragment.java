package ar.edu.utn.frsf.isi.dam.TP_Huellitas;


import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.URL;

import ar.edu.utn.frsf.isi.dam.TP_Huellitas.Modelo.ReporteExtravio;


public class DetallesFragment extends Fragment implements OnMapReadyCallback {

    private ImageView imgMascota;
    private TextView tvxMascota;
    private TextView tvxContacto;
    private TextView tvxTel;
    private TextView tvxFecha;
    private MapView mapView;
    private TextView tvxMensaje;
    private TextView tvxMensajeTel;
    private Button btnEnviar;
    private ReporteExtravio unReporte;
    private GoogleMap gmap;



    public DetallesFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_detalles, container, false);

        this.imgMascota= v.findViewById(R.id.imgMascota);
        this.tvxMascota= v.findViewById(R.id.tvxMascota);
        this.tvxContacto= v.findViewById(R.id.tvxContacto);
        this.tvxTel= v.findViewById(R.id.tvxTel);
        this.tvxFecha= v.findViewById(R.id.tvxFecha);
/*
        this.tvxMensaje= v.findViewById(R.id.tvxMensaje);
        this.tvxMensaje.setHint("Escriba un mensaje a enviar");
        this.tvxMensajeTel= v.findViewById(R.id.tvxMensajeTel);
        this.tvxMensajeTel.setHint("Indique su número de teléfono");
*/
     /*   this.btnEnviar= v.findViewById(R.id.btnEnviar);
        this.btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvxMensaje.getText().toString().equals("")){
                    Toast.makeText(view.getContext(),"Ingrese un mensaje",Toast.LENGTH_LONG).show();
                }else
                    if (tvxMensajeTel.getText().toString().equals("")){
                    Toast.makeText(view.getContext(),"Ingrese un número de contacto",Toast.LENGTH_LONG).show();
                    }else{
                        FirebaseMessaging fm = FirebaseMessaging.getInstance();
                        fm.send(new RemoteMessage.Builder(unReporte.getId() + "@gcm.googleapis.com")
                                .setMessageId(Integer.toString(123))
                                .addData("my_message", "Hello World")
                                .addData("my_action","SAY_HELLO")
                                .build());

                    }
            }
        });*/






        this.mapView= v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        unReporte= new ReporteExtravio();
        unReporte.setCoodenadas("0;0");

        if(getArguments()!=null) {
            unReporte = getArguments().getParcelable("reporte");

            getArguments().clear();
        }
        System.out.println("Reporte:"+unReporte.getNombreContacto());
        this.tvxContacto.setText(unReporte.getNombreContacto());
        this.tvxFecha.setText(unReporte.getFecha());
        this.tvxMascota.setText(unReporte.getNombreMascota());
        this.tvxTel.setText(unReporte.getTelContacto());

        new DownLoadImageTask(this.imgMascota).execute(unReporte.getPathFoto());

        return v;




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;


        if (ActivityCompat.checkSelfPermission(DetallesFragment.this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(DetallesFragment.this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(DetallesFragment.this.getActivity(),new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{

            String[] latlong = unReporte.getCoodenadas().split(";");
            double latitude = Double.parseDouble(latlong[0]);
            double longitude = Double.parseDouble(latlong[1]);
            LatLng posicion = new LatLng(latitude, longitude);
            gmap.addMarker(new MarkerOptions().position(posicion)
                    .title("L")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, 17));
            //gmap.getUiSettings().setAllGesturesEnabled(false);

            }
    }




    private class DownLoadImageTask extends AsyncTask<String,Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }




}
