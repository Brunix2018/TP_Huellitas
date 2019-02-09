package ar.edu.utn.frsf.isi.dam.TP_Huellitas.Modelo;

public class ReporteExtravio {
    private long id;
    private boolean contactoEsDuenio;
    private String nombreContacto;
    private String telContacto;
    private String nombreMascota;
    private String pathFoto;
    private String coodenadas;
    private boolean esgato;
    private String Fecha;

    public void setId(long id) {
        this.id = id;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public long getId() {
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

}
