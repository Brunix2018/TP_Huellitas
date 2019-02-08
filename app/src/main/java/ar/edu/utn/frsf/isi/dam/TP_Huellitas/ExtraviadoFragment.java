package ar.edu.utn.frsf.isi.dam.TP_Huellitas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import ar.edu.utn.frsf.isi.dam.TP_Huellitas.Modelo.ReporteExtravio;

public class ExtraviadoFragment extends Fragment {




    private TextView mascota_coord;
    private Button btnBuscarCoordenadas;
    private Button btnFoto;
    private Button btnGalleria;
    private ImageView foto;
    private EditText edtNombreContacto;
    private EditText edtTelContacto;
    private EditText edtNombreMascota;
    private Spinner lstAnio;
    private Spinner lstMes;
    private Spinner lstDia;
    private RadioGroup optTipo;
    private RadioButton optCanino;
    private RadioButton optFelino;
    private Button btnGuardar;
    private ReporteExtravio unReporte;
    private TextView nombMascota;
    private String pathFoto="";
    private String fragmentTag;


    private String[] meses = {"1","2","3","4","5","6","7","8","9","10","11","12"};
    private String[] anios = {"2018","2019","2020"};
    private String[] dias = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};









    /************** OnNuevoLugarListener *********************/
    private coordenadasListener listener;

    public interface coordenadasListener {
        public void obtenerCoordenadas(String fragmentTag);
        public void sacarFoto(String fragmentTag);
        public void cargarGaleria(String fragmentTag);
       // public void guardarReporte(ReporteExtravio unReporte);


    }


    public void setPathFoto(String pathFoto) {
        this.pathFoto = pathFoto;
        cargarFoto();
    }

    public ReporteExtravio getUnReporte() {
        return unReporte;
    }

    public void setUnReporte(ReporteExtravio unReporte) {
        this.unReporte = unReporte;

        cargarDatos();
        cargarFoto();
    }

    public coordenadasListener getListener() {
        return listener;
    }

    public void setListener(coordenadasListener listener) {
        this.listener = listener;
    }


    /************** OnNuevoLugarListener *********************/



    public ExtraviadoFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        System.out.println("************Fragmento "+this.getTag()+" Dibujado************");
        View v = inflater.inflate(R.layout.fragmento_animal_extraviado, container, false);
        mascota_coord = v.findViewById(R.id.mascota_coord);
        btnBuscarCoordenadas= v.findViewById(R.id.btnBuscarCoordenadas);
        btnBuscarCoordenadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.obtenerCoordenadas(fragmentTag);
            }
        });
        foto = v.findViewById(R.id.foto);
        edtNombreContacto = v.findViewById(R.id.edtNombreContacto);
        edtTelContacto = v.findViewById(R.id.edtTelContacto);
        edtNombreMascota = v.findViewById(R.id.edtNombreMascota );
        lstAnio = v.findViewById(R.id.lstAnio);
        lstAnio.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, anios));
        lstAnio.setSelection(1);
        lstMes = v.findViewById(R.id.lstMes);
        lstMes.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, meses));
        lstMes.setSelection(Calendar.getInstance().get(Calendar.MONTH));
        lstDia = v.findViewById(R.id.lstDia);
        lstDia.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, dias));
        lstDia.setSelection(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)-1);
        optTipo = v.findViewById(R.id.optTipo);
        optCanino = v.findViewById(R.id.optCanino);
        optFelino = v.findViewById(R.id.optFelino);
        btnGalleria = v.findViewById(R.id.btnGalleria);
        btnGalleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    listener.cargarGaleria(fragmentTag);
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            2000);


                }
            }
        });

        nombMascota = v.findViewById(R.id.nombMascota);
        btnGuardar = v.findViewById(R.id.btnGuardar);

        btnFoto = (Button) v.findViewById(R.id.btnFoto);
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

                    listener.sacarFoto(fragmentTag);
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            2000);


                }
            }
        });

        String coordenadas = "0;0";
        if(getArguments()!=null) coordenadas = getArguments().getString("latLng","0;0");
        mascota_coord.setText(coordenadas);



        if(this.getTag().equals("optAnimalExtraviado")){
            btnFoto.setEnabled(false);
            btnFoto.setVisibility(View.GONE);
        }else{
            edtNombreMascota.setEnabled(false);
            edtNombreMascota.setVisibility(View.GONE);
            nombMascota.setVisibility(View.GONE);

        }

        return v;
    }

    private void cargarFoto(){
System.out.println("cargar foto "+this.pathFoto);
       // if (!unReporte.getPathFoto().equals("")) {
          if (!pathFoto.equals("")) {
            System.out.println("####cargarFoto####");
           // File file = new File(this.unReporte.getPathFoto());
            File file = new File(pathFoto);
            //File file2 = new File(this.getUriFoto().getPath());
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), Uri.fromFile(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (imageBitmap != null) {
                foto.setImageBitmap(imageBitmap);
                System.out.println("####Foto Cargada####");

                //Galería

                //MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), imageBitmap, "" , "");
                //
            }
        }
    }

    private void cargarDatos(){

        System.out.println(unReporte.toString());
        if (!unReporte.getNombreContacto().equals("")) {
            this.edtNombreContacto.setText(unReporte.getNombreContacto());
        }
        if (!unReporte.getCoodenadas().equals("")) {
            this.mascota_coord.setText(unReporte.getCoodenadas());
        }
        if (!unReporte.getNombreMascota().equals("")) {
            this.edtNombreMascota.setText(unReporte.getNombreMascota());
        }
        if (!unReporte.isEsgato()) {
            this.optFelino.setSelected(unReporte.isEsgato());
        }
        if (!unReporte.getTelContacto().equals("")) {
            this.edtTelContacto.setText(unReporte.getTelContacto());
        }
        if (unReporte.getPathFoto() == null){
            unReporte.setPathFoto("");
        }


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        unReporte= new ReporteExtravio();
        fragmentTag=this.getTag();
        super.onCreate(savedInstanceState);
        //inicializar variables a conservar
        System.out.println("************Fragmento  "+this.getTag()+" creado************");
    }

    @Override
    public void onPause() {

        super.onPause();
        // usuario está abandonando el fragmento
        System.out.println("************Fragmento  "+this.getTag()+" Pausado************");
    }

    @Override
    public void onStop() {
        super.onStop();
        // fragmento no visible
        System.out.println("************Fragmento  "+this.getTag()+" Detenido************");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("************Fragmento  "+this.getTag()+" Destruido************");
    }

    /*public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString("reclamoDesc", reclamoDesc.getText().toString());
        //outState.putString("mail", mail.getText().toString());
        //outState.putString("mascota_coord", mascota_coord.getText().toString());
       // outState.putString("pathFoto", unReporte.getPathFoto());



    }*/

}
