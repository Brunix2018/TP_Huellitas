package ar.edu.utn.frsf.isi.dam.TP_Huellitas.Modelo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import ar.edu.utn.frsf.isi.dam.TP_Huellitas.MainActivity;
import ar.edu.utn.frsf.isi.dam.TP_Huellitas.Notificaciones.MyToken;
import ar.edu.utn.frsf.isi.dam.TP_Huellitas.Notificaciones.ReportesExpidarosService;

import static android.support.constraint.Constraints.TAG;

public class DBApi {

  /*  private static DBApi INSTANCIA_UNICA=null;

    public static DBApi getInstance(){
        if(INSTANCIA_UNICA==null) INSTANCIA_UNICA = new DBApi();
        return INSTANCIA_UNICA;
    }*/

    private FirebaseFirestore db;
    private Context contexto;
    private MyToken token;


    //private DBApi() {
    public DBApi(Context context, MyToken untoken) {
        this.db = FirebaseFirestore.getInstance();
        this.contexto= context;
        this.token=untoken;



    }

    public void crearNuevoUsuario(ReporteExtravio unReporte,String pathString) {
        // Create a new user with a first and last name
        ReporteExtravio reporte = unReporte;
        reporte.setPathFoto(pathString);
        System.out.println("####TOKEN:"+token.getTokenFromPrefs());
        String tipoReporte;
        if (reporte.isContactoEsDuenio()){
            tipoReporte= "Perdidos";
        }else {
            tipoReporte= "Encontrados";
        }
        reporte.setId(this.token.getTokenFromPrefs());


         db.collection(tipoReporte)
        .add(reporte)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(contexto,"Reporte Guardado con éxito",Toast.LENGTH_LONG).show();
                System.out.println("DocumentSnapshot added with ID: " + documentReference.getId());
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(contexto,"No se guardó el reporte",Toast.LENGTH_LONG).show();
                System.out.println("Error adding document" + e);
            }
        });

    }


    public void borrarExpirados() {
        db.collection("Perdidos")
                .whereEqualTo("id", token.getTokenFromPrefs())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                ReporteExtravio report = document.toObject(ReporteExtravio.class);
                                if(report.isFechaExpirada()){

                                    db.collection("Perdidos").document(document.getId())
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                    Intent intent = new Intent(contexto, ReportesExpidarosService.class);
                                                    contexto.startService(intent);

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error deleting document", e);
                                                }
                                            });
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


    public Query prepararConsulta( String tipoBusqueda, boolean tipoAnimal){
        return this.db.collection(tipoBusqueda).whereEqualTo("esgato", tipoAnimal);
    }

    public List<ReporteExtravio> obtenerConsulta(String tipoBusqueda, boolean tipoAnimal){
        final List<ReporteExtravio> listaReportes = new ArrayList<>();

        db.collection(tipoBusqueda)
                .whereEqualTo("esgato", tipoAnimal)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                ReporteExtravio report = document.toObject(ReporteExtravio.class);
                                listaReportes.add(report);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return listaReportes;
    }

}
