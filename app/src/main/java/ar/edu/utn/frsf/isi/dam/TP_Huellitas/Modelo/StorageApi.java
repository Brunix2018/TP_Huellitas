package ar.edu.utn.frsf.isi.dam.TP_Huellitas.Modelo;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class StorageApi {

    private FirebaseStorage storage;
    private StorageReference storageRef;
    //private FirebaseAuth auth;
    public String ruta;

    public StorageApi() {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
      //  auth = FirebaseAuth.getInstance();
       // auth.signInAnonymously();
    }


  /*  public void subirFoto(String path) {
        Uri file = Uri.fromFile(new File(path));
        final StorageReference riversRef = storageRef.child("images/" + file.getLastPathSegment());

        UploadTask uploadTask = riversRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                System.out.println("falla en la subida");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
           // public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                System.out.println (riversRef.getDownloadUrl().getResult().toString());
                System.out.println("subida Exitosa");
            }
        });

    }*/

    public String getRuta() {
        return ruta;
    }

    public void subirFotoObtenerPath(String path) {
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



