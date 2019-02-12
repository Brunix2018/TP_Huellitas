package ar.edu.utn.frsf.isi.dam.TP_Huellitas.Notificaciones;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import ar.edu.utn.frsf.isi.dam.TP_Huellitas.R;

import static android.support.constraint.Constraints.TAG;

public class MyToken extends Thread{
    Context context;
    SharedPreferences preferences;

    public MyToken(Context context) {
        this.context=context;
        this.preferences = preferences = PreferenceManager.getDefaultSharedPreferences(context);

    }

    @Override
    public void run() {

        // si el token no fue obtenido antes, cargarlo en las preferencias
        if (!preferences.contains("registration_id")) {
            // Get token
            // [START retrieve_current_token]
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String token = task.getResult().getToken();
                            System.out.println("####TOKEN:"+token);
                            saveTokenToPrefs(token);
                        }
                    });
            // [END retrieve_current_token]
        }
    }



    private void saveTokenToPrefs(String _token){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("registration_id", _token);
        editor.apply();
    }

    public String getTokenFromPrefs(){
         return preferences.getString("registration_id", null);
    }



}
