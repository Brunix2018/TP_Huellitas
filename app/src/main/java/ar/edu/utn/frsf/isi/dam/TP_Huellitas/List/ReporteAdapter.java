package ar.edu.utn.frsf.isi.dam.TP_Huellitas.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import ar.edu.utn.frsf.isi.dam.TP_Huellitas.Modelo.ReporteExtravio;
import ar.edu.utn.frsf.isi.dam.TP_Huellitas.R;

public class ReporteAdapter extends ArrayAdapter<ReporteExtravio> {

    private Context ctx;
    private List<ReporteExtravio> listaReportes;

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

        ReporteExtravio unRporte = super.getItem(position);

        holder.tbFecha.setText(unRporte.getFecha());


        return fila;
    }
}
