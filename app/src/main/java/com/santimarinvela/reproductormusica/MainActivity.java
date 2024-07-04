package com.santimarinvela.reproductormusica;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_MEDIA_AUDIO};
    ArrayList<Canciones> listaCanciones_SMV;
    RecyclerView recycler_SMV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Verificar si se tienen los permisos necesarios
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // Si no se tienen los permisos, solicitarlos al usuario
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } else {
            onRequestPermissionsResult(REQUEST_EXTERNAL_STORAGE,PERMISSIONS_STORAGE, new int[]{PERMISSION_GRANTED});
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            // Verificar si se han concedido los permisos
            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                // SE INVOCA EL RECYCLERVIEW PARA QUE SE VISUALICE LA LISTA
                // Y SE INVOCA EL METODO LLENARCANCIONES QUE ES EL QUE VA A EXTRAER LOS METADATOS
                recycler_SMV = (RecyclerView) findViewById(R.id.vistaRecycler);
                LinearLayoutManager linear = new LinearLayoutManager(this);
                recycler_SMV.setLayoutManager(linear);
                listaCanciones_SMV = llenarCanciones(this);
                AdaptadorRecycle adapter = new AdaptadorRecycle(listaCanciones_SMV,this);
                recycler_SMV.setAdapter(adapter);
            } else {
                // Los permisos fueron denegados, mostrar un mensaje o realizar acciones adicionales
                Toast.makeText(this, "Los permisos fueron denegados.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //ESTE METODO EXTRAE LOS DATOS
    private ArrayList<Canciones> llenarCanciones(Context c){
        MediaMetadataRetriever retriever_SMV = null;
        ArrayList<Canciones>listaCanciones_SMV = new ArrayList<>();
        String patho = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File directorio_SMV = new File(patho);

        File[] ficheros = directorio_SMV.listFiles();
        for(File archi_SMV: ficheros){
            try{
                if(archi_SMV.isFile() && archi_SMV.getName().endsWith(".mp3")) {
                    retriever_SMV = new MediaMetadataRetriever();
                    retriever_SMV.setDataSource(archi_SMV.getAbsolutePath());
                    String nombre = retriever_SMV.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                    String autor = retriever_SMV.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                    String album = retriever_SMV.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                    String path = archi_SMV.getAbsolutePath();
                    Canciones canciones = new Canciones(nombre, autor, album, path, obtenerImagen(archi_SMV.getAbsolutePath()));
                    listaCanciones_SMV.add(canciones);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return listaCanciones_SMV;
    }

    //ESTE METODO OBTIENE LA IMAGEN DE LA PORTADA DEL ALBUM
    private Bitmap obtenerImagen(String ruta){
        MediaMetadataRetriever retriever_SMV = new MediaMetadataRetriever();
        retriever_SMV.setDataSource(ruta);
        try{
            byte[] picture = retriever_SMV.getEmbeddedPicture();
            if (picture != null) {
                return BitmapFactory.decodeByteArray(picture, 0, picture.length);
            }
        }catch(Exception e){
            Log.e("Error", "Error al obtener imagen");
        }
        return BitmapFactory.decodeResource(getResources(), R.drawable.portada2);
    }
}

