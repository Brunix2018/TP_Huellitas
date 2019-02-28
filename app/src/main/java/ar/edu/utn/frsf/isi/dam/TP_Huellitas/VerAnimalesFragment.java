package ar.edu.utn.frsf.isi.dam.TP_Huellitas;

import android.app.DownloadManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.isi.dam.TP_Huellitas.List.ReporteAdapter;
import ar.edu.utn.frsf.isi.dam.TP_Huellitas.Modelo.ReporteExtravio;
import ar.edu.utn.frsf.isi.dam.TP_Huellitas.Modelo.StorageApi;

import static android.support.constraint.Constraints.TAG;

public class VerAnimalesFragment extends Fragment {

    private RadioGroup optTipoBusqueda;
    private RadioButton optPerdidas;
    private RadioButton optEncontradas;
    private RadioGroup optbusqTipo;
    private RadioButton optBusquedaCanino;
    private RadioButton optBusquedaFelino;
    private ListView listView;
    private StorageApi storage;
    private ArrayAdapter<ReporteExtravio> reportes;
    private Button btnBuscar;

    //private RecyclerView mRecyclerView;
    //private RecyclerView.Adapter mAdapter;










    public VerAnimalesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage= StorageApi.getInstance();
        setRetainInstance(true);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmento_ver_animales, container, false);

        optTipoBusqueda = v.findViewById(R.id.optTipoBusqueda);
        optEncontradas = v.findViewById(R.id.optEncontradas);
        optEncontradas.setChecked(true);
        optPerdidas = v.findViewById(R.id.optPerdidas);

        optBusquedaFelino = v.findViewById(R.id.optBusquedaFelino);
        optBusquedaFelino.setChecked(true);
        optBusquedaCanino = v.findViewById(R.id.optBusquedaCanino);
        listView= v.findViewById(R.id.listView);

        //mRecyclerView = v.findViewById(R.id.mRecyclerView);
       // mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        optbusqTipo = v.findViewById(R.id.optbusqTipo);

        btnBuscar= v.findViewById(R.id.btnBuscar);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  reportes = new ArrayAdapter(this, android.R.layout.simple_list_item_1,obtenerConsulta());
                System.out.println("Empieza ObtenerConsulta");
                obtenerConsulta();
            }
        });







        return v;
    }


    public Query prepararConsulta(){
        String tipoReporte;
        if (optPerdidas.isChecked()){
            tipoReporte= "Perdidos";
        }else {
            tipoReporte= "Encontrados";
        }
        return storage.getDb().prepararConsulta(tipoReporte,optBusquedaFelino.isChecked());

    }

  /*  public List<ReporteExtravio> obtenerConsulta(){
        String tipoReporte;
        if (optPerdidas.isChecked()){
            tipoReporte= "Perdidos";
        }else {
            tipoReporte= "Encontrados";
        }
        return storage.getDb().obtenerConsulta(tipoReporte,optBusquedaFelino.isChecked());

    }*/

    public void obtenerConsulta(){
        System.out.println("Empieza ObtenerConsulta");
        String tipoReporte;
        if (optPerdidas.isChecked()){
            tipoReporte= "Perdidos";
        }else {
            tipoReporte= "Encontrados";
        }

        storage.getDb().getDb().collection(tipoReporte)
                .whereEqualTo("esgato", optBusquedaFelino.isChecked())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    List<ReporteExtravio> listaReportes = new ArrayList<>();
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Empieza a cargar lista");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                ReporteExtravio report = document.toObject(ReporteExtravio.class);
                                listaReportes.add(report);
                            }
                            ReporteAdapter unAdapter = new ReporteAdapter(getActivity(),listaReportes);
                            listView.setAdapter(unAdapter);
                            unAdapter.notifyDataSetChanged();
                            System.out.println("Terminó de cargar lista");
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


}
