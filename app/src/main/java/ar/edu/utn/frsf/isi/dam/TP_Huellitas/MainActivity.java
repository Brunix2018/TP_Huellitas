package ar.edu.utn.frsf.isi.dam.TP_Huellitas;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import ar.edu.utn.frsf.isi.dam.TP_Huellitas.List.ReporteAdapter;
import ar.edu.utn.frsf.isi.dam.TP_Huellitas.Modelo.ReporteExtravio;
import ar.edu.utn.frsf.isi.dam.TP_Huellitas.Modelo.DBApi;
import ar.edu.utn.frsf.isi.dam.TP_Huellitas.Modelo.StorageApi;
import ar.edu.utn.frsf.isi.dam.TP_Huellitas.Notificaciones.MyToken;
import ar.edu.utn.frsf.isi.dam.TP_Huellitas.Notificaciones.ReportesExpidarosService;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener,
        MapaFragment.OnMapaListener, ExtraviadoFragment.coordenadasListener,CalendarioFragment.calendarioListener {
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private LatLng lat;
    private String pathFoto;
    private Uri uriFoto;
    static final int SELECT_IMAGE_GALLERY = 1;
    static final int REQUEST_IMAGE_SAVE = 2;
    private String llamadaFragment;
    private StorageApi storage;
    private MyToken myToken;




    //private ReporteExtravio unReporteTemp;

  /*  public void setUnReporteTemp(ReporteExtravio unReporteTemp) {
        this.unReporteTemp = unReporteTemp;
    }

    public ReporteExtravio getUnReporteTemp() {
        return unReporteTemp;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        //Handle when activity is recreated like on orientation Change
        //shouldDisplayHomeUp();

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navView = (NavigationView)findViewById(R.id.navview);
        IntroFragment fragmentInicio = new IntroFragment();




        //MapaFragment fragment = new MapaFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenido, fragmentInicio,"inicio")
                .commit();

        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        boolean fragmentTransaction = false;
                        Fragment fragment = null;
                        String tag = "";
                        boolean fragmentoExiste = true;
                        switch (menuItem.getItemId()) {
                            case R.id.optAnimalExtraviado:
                                tag = "optAnimalExtraviado";
                                fragment =  getSupportFragmentManager().findFragmentByTag(tag);

                                if(fragment==null) {

                                    fragment = new ExtraviadoFragment();
                                    ((ExtraviadoFragment) fragment).setListener(MainActivity.this);
                                    fragmentoExiste = false;


                                }
                               // unReporteTemp.setContactoEsDuenio(true);
                               // ((ExtraviadoFragment) fragment).setUnReporte(unReporteTemp);

                                fragmentTransaction = true;
                                break;
                            case R.id.optAnimalSinDueno:
                                tag="optAnimalSinDueno";
                                fragment =  getSupportFragmentManager().findFragmentByTag(tag);
                                if(fragment==null) {
                                    fragment = new ExtraviadoFragment();
                                    ((ExtraviadoFragment) fragment).setListener(MainActivity.this);
                                    fragmentoExiste = false;

                                }
                               // unReporteTemp.setContactoEsDuenio(false);
                               // ((ExtraviadoFragment) fragment).setUnReporte(unReporteTemp);

                                fragmentTransaction = true;
                                break;
                            case R.id.optVer:

                                tag="optVer";

                                fragment =  getSupportFragmentManager().findFragmentByTag(tag);
                                if(fragment==null) {
                                    fragment = new VerAnimalesFragment();

                                    fragmentoExiste = false;

                                }
                                // unReporteTemp.setContactoEsDuenio(false);
                                // ((ExtraviadoFragment) fragment).setUnReporte(unReporteTemp);

                                fragmentTransaction = true;
                                break;
            /*                case R.id.optHistorial:

                                tag="optHistorial";
                                fragment =  getSupportFragmentManager().findFragmentByTag(tag);

                                fragmentTransaction = true;
                                break;
                            case R.id.optParecidos:
                                tag = "optParecidos";
                                fragment =  getSupportFragmentManager().findFragmentByTag(tag);
                                if(fragment==null) {
                                                                   }

                                fragmentTransaction = true;
                                break;*/
                        }

                        if(fragmentTransaction) {
                            if (fragmentoExiste){
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.contenido, fragment,tag)
                                        .commit();

                                menuItem.setChecked(true);
                            }else{
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.contenido, fragment,tag)
                                        .addToBackStack("inicio")
                                        .commit();

                                menuItem.setChecked(true);
                                //getSupportActionBar().setTitle(menuItem.getTitle());
                            }



                        }

                        drawerLayout.closeDrawers();

                        return true;
                    }
                });






        myToken = new MyToken(this);
        myToken.run();
        storage = StorageApi.getInstance(this, myToken);
        storage.getDb().borrarExpirados();








       //   System.out.println("RUTA: "+storage.getRuta());



        //unReporteTemp = new ReporteExtravio();

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        System.out.println("onBackPressed");
        System.out.println(getSupportFragmentManager().getBackStackEntryCount());

        super.onBackPressed();

    }

    @Override
    public void onBackStackChanged() {
        System.out.println("onbackStackChanged");
        System.out.println(getSupportFragmentManager().getBackStackEntryCount());

       // shouldDisplayHomeUp();
    }

 /*   public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canback = getSupportFragmentManager().getBackStackEntryCount()>0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
    }*/


    @Override
    public void coordenadasSeleccionadas(LatLng c) {

        Fragment fragment =  getSupportFragmentManager().findFragmentByTag(llamadaFragment);
        if(fragment==null) {
            fragment = new ExtraviadoFragment();
            //((NuevoReclamoFragment) fragment).setListener(listenerReclamo);
            ((ExtraviadoFragment) fragment).setListener(MainActivity.this);
        }
        Bundle bundle = new Bundle();
        lat=c;
        bundle.putString("latLng",c.latitude+";"+c.longitude);
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenido, fragment,llamadaFragment)
                .commit();

    }

    @Override
    public void obtenerCoordenadas(String unFragmentTag) {
        this.llamadaFragment=unFragmentTag;
        Fragment fragment = null;
        String tag = "mapa";

        fragment =  getSupportFragmentManager().findFragmentByTag(tag);

        if(fragment==null) {
            fragment = new MapaFragment();
            // configurar a la actividad como listener de los eventos del mapa ((MapaFragment) fragment).setListener(this);
            ((MapaFragment) fragment).setListener(MainActivity.this);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenido, fragment,tag)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void sacarFoto(String unFragmentTag) {
        this.llamadaFragment=unFragmentTag;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "ar.edu.utn.frsf.isi.dam.TP_Huellitas.fileprovider",
                        photoFile);
                this.uriFoto= photoURI;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_SAVE);
            }
        }
    }

    public MyToken getMyToken() {
        return myToken;
    }

    @Override
    public void cargarGaleria(String unFragmentTag) {
        this.llamadaFragment=unFragmentTag;
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, SELECT_IMAGE_GALLERY);

    }

    @Override
    public void guardarReporte(String unFragmentTag,ReporteExtravio unReporte) {


        storage.subirFotoObtenerPath(unReporte);

        //storage.subirFotoObtenerPath("/storage/emulated/0/Android/data/ar.edu.utn.frsf.isi.dam.TP_Huellitas/files/Pictures/JPEG_20190210_212102_7358263715635031331.jpg");




        Fragment fragment = getSupportFragmentManager().findFragmentByTag("inicio");
        Fragment fragment2 = getSupportFragmentManager().findFragmentByTag(unFragmentTag);
        if (fragment == null) {
            fragment = new IntroFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenido, fragment,"inicio" )
                //.detach(fragment2)
               // .remove(fragment2)
               // .addToBackStack(null)

                .commitAllowingStateLoss();

    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        pathFoto = image.getAbsolutePath();
        //this.unReporteTemp.setPathFoto(image.getAbsolutePath());

        return image;
    }

    private void createImageFile(Uri unUri) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        InputStream in =  getContentResolver().openInputStream(unUri);
        OutputStream out = new FileOutputStream(image);
        byte[] buf = new byte[1024];
        int len;
        while((len=in.read(buf))>0){
            out.write(buf,0,len);
        }
        out.close();
        in.close();
        pathFoto = image.getAbsolutePath();
        //this.unReporteTemp.setPathFoto(image.getAbsolutePath());


    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_SAVE && resultCode == RESULT_OK) {

            IniciarFragmentoConImagen(llamadaFragment,pathFoto);




        }else {
            if (requestCode == SELECT_IMAGE_GALLERY && resultCode == RESULT_OK) {
                Uri imageUri = data.getData();
                try {
                    createImageFile(imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                IniciarFragmentoConImagen(llamadaFragment,pathFoto);
            }
        }
    }

    public void IniciarFragmentoConImagen(String tagFragmento, String PathImagen){

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tagFragmento);
        if (fragment == null) {
            fragment = new ExtraviadoFragment();

            ((ExtraviadoFragment) fragment).setListener(MainActivity.this);
        }

        ((ExtraviadoFragment) fragment).setPathFoto(PathImagen);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenido, fragment, tagFragmento)
                .commitAllowingStateLoss();
    }


    public void iniciarCalendario(String unFragmentTag) {
        this.llamadaFragment=unFragmentTag;
        String tag = "CalendarioFragment";
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new CalendarioFragment();

            ((CalendarioFragment) fragment).setListener(MainActivity.this);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenido, fragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    @Override
    public void enviarFecha(String Fecha) {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(llamadaFragment);
        if (fragment == null) {
            fragment = new CalendarioFragment();

            ((ExtraviadoFragment) fragment).setListener(MainActivity.this);
        }
        ((ExtraviadoFragment) fragment).setTvFecha(Fecha);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenido, fragment, llamadaFragment)
                .commitAllowingStateLoss();
    }


    private void createNotificationChannel() {
// Crear el canal de notificaciones pero solo para API 26 io superior
// dado que NotificationChannel es una clase nueva que no está incluida
// en las librerías de soporte qeu brindan compatibilidad hacía atrás
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.canal_estado_nombre);
            String description = getString(R.string.canal_estado_descr);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CANAL01", name, importance);
            channel.setDescription(description);
// Registrar el canal en el sistema
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public void verDetalles(ReporteExtravio unReporte) {

        String tag = "detalles";
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new DetallesFragment();
        }


        Fragment fragment2 =  getSupportFragmentManager().findFragmentByTag("optVer");

        Bundle bundle = new Bundle();
        bundle.putParcelable("reporte",unReporte);
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.contenido, fragment)
                .hide(fragment2)
                //.replace(fragment2.getId(), fragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

}
