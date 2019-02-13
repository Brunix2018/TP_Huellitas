package ar.edu.utn.frsf.isi.dam.TP_Huellitas.Modelo;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import ar.edu.utn.frsf.isi.dam.TP_Huellitas.Notificaciones.MyToken;

public class StorageApi {

    private static StorageApi INSTANCIA_UNICA=null;

    public static StorageApi getInstance(Context unContexto, MyToken untoken){
        if(INSTANCIA_UNICA==null) INSTANCIA_UNICA = new StorageApi(unContexto, untoken);
        return INSTANCIA_UNICA;
    }

    public static StorageApi getInstance(){
        return INSTANCIA_UNICA;
    }


    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private static final String TAG = "StorageApi";
    public String ruta="vacia";

    private DBApi db;



    private StorageApi(Context unContext, MyToken untoken) {

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.signInAnonymously();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        db = new DBApi(unContext, untoken);

    }



    public void subirFotoObtenerPath(final ReporteExtravio unReporte) {
        // [START upload_get_download_url]
        ReporteExtravio reporte= unReporte;
        Uri file = Uri.fromFile(new File(reporte.getPathFoto()));
        System.out.println("File:"+file.getPath());

        final StorageReference ref = storageRef.child("images/" + file.getLastPathSegment());
        UploadTask uploadTask = ref.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    ruta=downloadUri.toString();
                    db.crearNuevoUsuario(unReporte, ruta);
                    System.out.println("UNRUTA:"+ruta);

                } else {
                    System.out.println("Problema con la subida");
                }
            }
        });

        // [END upload_get_download_url]
    }

    public DBApi getDb() {
        return db;
    }

    /* public void subirFotoObtenerPath(String path) {
        // [START upload_get_download_url]
        Uri file = Uri.fromFile(new File(path));

        final StorageReference ref = storageRef.child("images/" + file.getLastPathSegment());
        UploadTask uploadTask = ref.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    ruta=downloadUri.toString();
                    System.out.println("RUTA:::"+ruta);
                    System.out.println("subida Exitosa");

                } else {
                    System.out.println("Problema con la subida");
                }
            }
        });

        // [END upload_get_download_url]
    }*/




}



