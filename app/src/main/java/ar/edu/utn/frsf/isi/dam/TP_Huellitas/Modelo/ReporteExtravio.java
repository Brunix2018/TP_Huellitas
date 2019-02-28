package ar.edu.utn.frsf.isi.dam.TP_Huellitas.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReporteExtravio implements Parcelable {
    private String id;
    private boolean contactoEsDuenio;
    private String nombreContacto;
    private String telContacto;
    private String nombreMascota;
    private String pathFoto;
    private String coodenadas;
    private boolean esgato;
    private String Fecha;

    protected ReporteExtravio(Parcel in) {
        id = in.readString();
        contactoEsDuenio = in.readByte() != 0;
        nombreContacto = in.readString();
        telContacto = in.readString();
        nombreMascota = in.readString();
        pathFoto = in.readString();
        coodenadas = in.readString();
        esgato = in.readByte() != 0;
        Fecha = in.readString();
    }

    public static final Creator<ReporteExtravio> CREATOR = new Creator<ReporteExtravio>() {
        @Override
        public ReporteExtravio createFromParcel(Parcel in) {
            return new ReporteExtravio(in);
        }

        @Override
        public ReporteExtravio[] newArray(int size) {
            return new ReporteExtravio[size];
        }
    };

    public void setId(String id) {
        this.id = id;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public String getFecha() {
        return Fecha;
    }

    public ReporteExtravio() {
        this.setContactoEsDuenio(true);
        this.nombreContacto="";
        this.telContacto="";
        this.nombreMascota="";
        this.pathFoto="";
        this.coodenadas="";
        this.esgato=true;
    }

    public boolean isEsgato() {
        return esgato;
    }

    public void setEsgato(boolean esgato) {
        this.esgato = esgato;
    }



    public boolean isContactoEsDuenio() {
        return contactoEsDuenio;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public String getTelContacto() {
        return telContacto;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public String getPathFoto() {
        return pathFoto;
    }

    public String getCoodenadas() {
        return coodenadas;
    }


    public void setContactoEsDuenio(boolean contactoEsDuenio) {
        this.contactoEsDuenio = contactoEsDuenio;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public void setTelContacto(String telContacto) {
        this.telContacto = telContacto;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public void setPathFoto(String pathFoto) {
        this.pathFoto = pathFoto;
    }

    public void setCoodenadas(String coodenadas) {
        this.coodenadas = coodenadas;
    }

    public String toString(){
        return "nombre Contacto :"+this.nombreContacto+"nombre Mascota :"+this.nombreMascota+"Tel Contacto :"+this.telContacto+"Coordenadas :"+this.coodenadas+"Path Foto :"+this.pathFoto;
    }

    public boolean isFechaExpirada() {
        // true: fecha mayor a 6 meses o a futuro
        String fechaA= this.Fecha;
        Date hoy;
        SimpleDateFormat formatDate;
        Date fechA = new Date();
        hoy = new Date();
        formatDate = new SimpleDateFormat("dd-MM-yyyy");
        try {
            fechA = formatDate.parse(fechaA);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diferencia = hoy.getTime() - fechA.getTime();
        diferencia = diferencia / 1000; // milisegundos
        diferencia = diferencia / 60 / 60; // horas y minutos


        System.out.println("diferencia: " + diferencia);
        if ((diferencia < 0) || (diferencia > 4320)) {
            return true;
        } else {
            return false;
        }

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeByte((byte) (contactoEsDuenio ? 1 : 0));
        dest.writeString(nombreContacto);
        dest.writeString(telContacto);
        dest.writeString(nombreMascota);
        dest.writeString(pathFoto);
        dest.writeString(coodenadas);
        dest.writeByte((byte) (esgato ? 1 : 0));
        dest.writeString(Fecha);
    }
}
