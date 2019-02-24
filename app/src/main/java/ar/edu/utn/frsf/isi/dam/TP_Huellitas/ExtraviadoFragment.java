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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private RadioGroup optTipo;
    private RadioButton optCanino;
    private RadioButton optFelino;
    private Button btnGuardar;
    private ReporteExtravio unReporte;
    private TextView nombMascota;
    private String pathFoto="";
    private String fragmentTag;
    private TextView tvFecha;
    private String unaFecha;
    private Date hoy;
    private SimpleDateFormat formatDate;

    public void setTvFecha(String unfecha) {
        unaFecha=unfecha;
        System.out.println("fecha mal: "+verificarFechaMal(unfecha));
    }


    /************** OnNuevoLugarListener *********************/
    private coordenadasListener listener;

    public interface coordenadasListener {
        public void obtenerCoordenadas(String fragmentTag);
        public void sacarFoto(String fragmentTag);
        public void cargarGaleria(String fragmentTag);
        public void guardarReporte(String fragmentTag,ReporteExtravio unReporte);
        public void iniciarCalendario(String fragmentTag);

    }


    public void setPathFoto(String pathFoto) {
        this.pathFoto = pathFoto;
        cargarFoto();
    }

    public ReporteExtravio getUnReporte() {
        return unReporte;
    }

   /* public void setUnReporte(ReporteExtravio unReporte) {
        this.unReporte = unReporte;

        cargarDatos();
        cargarFoto();
    }*/

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


        tvFecha= v.findViewById(R.id.tvFecha);


        tvFecha.setText(unaFecha);

        tvFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.iniciarCalendario(fragmentTag);
            }
        });


        optTipo = v.findViewById(R.id.optTipo);
        optCanino = v.findViewById(R.id.optCanino);
        optCanino.setChecked(!unReporte.isEsgato());
        optFelino = v.findViewById(R.id.optFelino);
        optFelino.setChecked(unReporte.isEsgato());
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
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               String incompleto="";

                if (mascota_coord.getText().length()==3) incompleto = "Ingrese Coordenadas donde fue visto el animal";
                if (edtNombreContacto.getText().length()==0) incompleto = "Ingrese un nombre de Contacto";
                if (edtTelContacto.getText().length()==0) incompleto = "Ingrese un teléfono de contacto";
                if (edtNombreMascota.getText().length()==0 && (fragmentTag == "optAnimalExtraviado")) incompleto = "Ingrese nombre de la Mascota";
                if (verificarFechaMal(tvFecha.getText().toString())) incompleto = "La fecha debe ser menor a 6 meses de antiguedad";
                if (pathFoto.length()==0) incompleto = "Ingrese foto del animal ";

                if (incompleto !=""){
                    Toast.makeText(getActivity(),incompleto,Toast.LENGTH_LONG).show();
                }else {
                    unReporte.setPathFoto(pathFoto);
                    unReporte.setNombreContacto(mascota_coord.getText().toString());
                    unReporte.setNombreMascota(edtNombreMascota.getText().toString());
                    unReporte.setNombreContacto(edtNombreContacto.getText().toString());
                    unReporte.setTelContacto(edtTelContacto.getText().toString());
                    unReporte.setEsgato(optFelino.isChecked());
                    unReporte.setFecha(tvFecha.getText().toString());
                    unReporte.setCoodenadas(mascota_coord.getText().toString());

                    vaciarDatos();
                    listener.guardarReporte(fragmentTag,unReporte);

                }


            }
        });


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
        if(getArguments()!=null) {
            coordenadas = getArguments().getString("latLng","0;0");
            getArguments().remove("latLng");
        }
        mascota_coord.setText(coordenadas);



        if(this.getTag().equals("optAnimalExtraviado")){
            btnFoto.setEnabled(false);
            btnFoto.setVisibility(View.GONE);
            unReporte.setContactoEsDuenio(true);
        }else{
            edtNombreMascota.setEnabled(false);
            edtNombreMascota.setVisibility(View.GONE);
            nombMascota.setVisibility(View.GONE);
            unReporte.setContactoEsDuenio(false);
        }
        cargarFoto();
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

    private void vaciarDatos(){


            this.edtNombreContacto.setText("");

            this.mascota_coord.setText("0;0");

            this.edtNombreMascota.setText("");

            this.optFelino.setSelected(true);

            this.edtTelContacto.setText("");

            pathFoto="";
            foto.setImageResource(R.drawable.catdog);
            mascota_coord.setText("0;0");
            unaFecha= formatDate.format(hoy);




    }

    private boolean verificarFechaMal(String fechaA){
        // true: fecha mayor a 6 meses o a futuro
        Date fechA= new Date();
        try {
            fechA= formatDate.parse(fechaA);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diferencia = hoy.getTime()-fechA.getTime();
        diferencia= diferencia/1000; // milisegundos
        diferencia= diferencia/60/60; // horas y minutos


        System.out.println("diferencia: "+diferencia);
        if (( diferencia< 0) || (diferencia >4320)){
            return true;
        }else{
            return false;
        }


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        unReporte= new ReporteExtravio();
        fragmentTag=this.getTag();
        hoy = new Date();
        formatDate = new SimpleDateFormat("dd-MM-yyyy");
        unaFecha=formatDate.format(hoy);

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


}
