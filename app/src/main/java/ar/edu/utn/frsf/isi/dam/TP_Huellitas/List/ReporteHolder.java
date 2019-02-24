package ar.edu.utn.frsf.isi.dam.TP_Huellitas.List;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ar.edu.utn.frsf.isi.dam.TP_Huellitas.R;

public class ReporteHolder {
    public ImageView foto;
    public TextView tbFecha;
    public Button btnDetalle;



    public ReporteHolder(View base){
        this.foto= base.findViewById(R.id.foto);
        this.tbFecha= base.findViewById(R.id.tbFecha);
        this.btnDetalle=base.findViewById(R.id.btnDetalle);
    }
}
