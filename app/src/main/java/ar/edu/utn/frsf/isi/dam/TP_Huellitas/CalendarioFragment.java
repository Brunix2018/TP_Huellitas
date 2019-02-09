package ar.edu.utn.frsf.isi.dam.TP_Huellitas;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

public class CalendarioFragment extends Fragment {


    private CalendarView calendarView;



    /************** calendarioListener *********************/
    private calendarioListener listener;
    public calendarioListener getListener() {
        return listener;
    }
    public void setListener(calendarioListener listener) {
        this.listener = listener;
    }
    public interface calendarioListener { public void enviarFecha(String Fecha);}
    /************** calendarioListener *********************/

    public CalendarioFragment() {}


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_calendario, container, false);

        calendarView = v.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange( @NonNull CalendarView view, int year, int month, int dayOfMonth) {
                listener.enviarFecha(dayOfMonth+"-"+(month+1)+"-"+year);
            }
        });




        return v;
    }


}
