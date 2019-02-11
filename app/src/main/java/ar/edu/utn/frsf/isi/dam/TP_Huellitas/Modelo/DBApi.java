package ar.edu.utn.frsf.isi.dam.TP_Huellitas.Modelo;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


import ar.edu.utn.frsf.isi.dam.TP_Huellitas.MainActivity;

public class DBApi {

  /*  private static DBApi INSTANCIA_UNICA=null;

    public static DBApi getInstance(){
        if(INSTANCIA_UNICA==null) INSTANCIA_UNICA = new DBApi();
        return INSTANCIA_UNICA;
    }*/

    private FirebaseFirestore db;
    private Context contexto;

    //private DBApi() {
    public DBApi(Context context) {
        this.db = FirebaseFirestore.getInstance();
        this.contexto= context;

    }

    public void crearNuevoUsuario(ReporteExtravio unReporte,String pathString) {
        // Create a new user with a first and last name
        ReporteExtravio reporte = unReporte;
        reporte.setPathFoto(pathString);


         db.collection("users")
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



        public void obtenerUsuario (){

            db.collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete( Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    System.out.println( document.getId() + " => " + document.getData());
                                }
                            } else {
                                System.out.println( "Error getting documents."+ task.getException());
                            }
                        }
                    });
        }



}
