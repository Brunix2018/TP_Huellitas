package ar.edu.utn.frsf.isi.dam.TP_Huellitas.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import ar.edu.utn.frsf.isi.dam.TP_Huellitas.MainActivity;
import ar.edu.utn.frsf.isi.dam.TP_Huellitas.Modelo.ReporteExtravio;
import ar.edu.utn.frsf.isi.dam.TP_Huellitas.R;

public class ReporteAdapter extends ArrayAdapter<ReporteExtravio> {

    private Context ctx;
    private List<ReporteExtravio> listaReportes;
    private ReporteExtravio unRporte;

    public ReporteAdapter(Context context,List<ReporteExtravio> objects) {
        super(context, 0, objects);
        this.ctx = context;
        this.listaReportes = objects;
    }



    @NonNull
    @Override
    public View getView(int position,  @Nullable View convertView,  @NonNull ViewGroup parent) {

        View fila = convertView;
        ReporteHolder holder;

        if(fila== null) {
            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            fila = inflater.inflate(R.layout.reporte, parent, false);
            holder = new ReporteHolder(fila);
            fila.setTag(holder);
        }else{
            holder = (ReporteHolder) fila.getTag();
        }

        unRporte = super.getItem(position);

        holder.tbFecha.setText(unRporte.getFecha());
        holder.btnDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ctx.getClass().equals(MainActivity.class)) {
                    ((MainActivity) ctx).verDetalles(unRporte);
                }

            }
        });



        new DownLoadImageTask(holder.foto).execute(unRporte.getPathFoto());


        return fila;
    }


    /*
        AsyncTask enables proper and easy use of the UI thread. This class
        allows to perform background operations and publish results on the UI
        thread without having to manipulate threads and/or handlers.
     */

    /*
        final AsyncTask<Params, Progress, Result>
            execute(Params... params)
                Executes the task with the specified parameters.
     */
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




}
