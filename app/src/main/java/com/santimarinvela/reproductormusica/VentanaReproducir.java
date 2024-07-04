package com.santimarinvela.reproductormusica;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;

import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class VentanaReproducir extends AppCompatActivity {
    private File[] songs;
    private int currentSongIndex = 0;
    private MediaPlayer mp;

    SeekBar barraProgreso_SMV;
    Button botonPlay_SMV, botonPause_SMV, botonStop_SMV, botonAvanzar_SMV,
            botonRetroceder_SMV, botonSiguiente_SMV, botonAnterior_SMV;
    Button botonVolver_SMV;
    TextView titulo_SMV;
    ImageView portada_SMV;
    int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_reproducir);
        titulo_SMV = (TextView) findViewById(R.id.titulo);
        portada_SMV = (ImageView) findViewById(R.id.portadaCancion);
        barraProgreso_SMV = (SeekBar) findViewById(R.id.barra);

        //AQUI RECOJO LA CANCION QUE SE HA SELECCIONADO EN LA RECYCLERVIEW
        Intent intent = getIntent();
        String songTitle = intent.getStringExtra("songTitle");
        position = intent.getIntExtra("pos", -1);
        String songUrl = intent.getStringExtra("songUrl");
        titulo_SMV.setText(songTitle);
        MediaMetadataRetriever retriever_SMV = new MediaMetadataRetriever();
        retriever_SMV.setDataSource(songUrl);
        byte[] picture = retriever_SMV.getEmbeddedPicture();
        portada_SMV.setImageBitmap(BitmapFactory.decodeByteArray(picture, 0, picture.length));

        // MENSAJE DE CUAL SE ESTA ESCUCHANDO
        Toast.makeText(this, "Reproduciendo: " + songTitle, Toast.LENGTH_SHORT).show();


        //CREO EL OBJETO MEDIAPLAYER PARA TRABAJAR CON EL
        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mp.setDataSource(songUrl);
            mp.prepare();
            mp.start();
        }catch(IOException e){
            e.printStackTrace();
        }

        //CREO UN HILO PARA EL PROGRESO DE LA CANCION EN LA BARRA
        new Thread(new Runnable() {
            private AtomicBoolean stop = new AtomicBoolean(false);
            public void stop(){
                stop.set(true);
            }

            @Override
            public void run() {
                while(!stop.get()){
                    barraProgreso_SMV.setProgress((int)((double)mp.getCurrentPosition() / (double)mp.getDuration()*100));
                    try {
                        Thread.sleep(200);
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }).start();


        //BOTON PLAY
        botonPlay_SMV = (Button) findViewById(R.id.boton_play);
        botonPlay_SMV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp == null){
                    mp.start();
                }else if(!mp.isPlaying()){
                    mp.start();
                Toast.makeText(VentanaReproducir.this, "Play", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //BOTON PAUSE
        botonPause_SMV = (Button) findViewById(R.id.boton_pause);
        botonPause_SMV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mp.pause();
                Toast.makeText(VentanaReproducir.this, "Pause", Toast.LENGTH_SHORT).show();
            }
        });
        //BOTON STOP
        botonStop_SMV = (Button) findViewById(R.id.boton_stop);
        botonStop_SMV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp != null){
                    mp.stop();
                }
                Toast.makeText(VentanaReproducir.this, "Stop", Toast.LENGTH_SHORT).show();
                try{
                    mp.prepare();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        //BOTON AVANZAR 10 SEGUNDOS
        botonAvanzar_SMV = (Button) findViewById(R.id.boton_avanzar);
        botonAvanzar_SMV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mp.getCurrentPosition();
                mp.seekTo(currentPosition + 10000);
                Toast.makeText(VentanaReproducir.this, ">> 10s", Toast.LENGTH_SHORT).show();
            }
        });
        //RETROCEDER 10 SEGUNDOS
        botonRetroceder_SMV = (Button) findViewById(R.id.boton_retroceder);
        botonRetroceder_SMV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mp.getCurrentPosition();
                mp.seekTo(currentPosition - 10000);
                Toast.makeText(VentanaReproducir.this, "<< 10s", Toast.LENGTH_SHORT).show();
            }
        });
        //ESTA ES LA RUTA CON LA QUE SE TRABAJA
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File directory = new File(path);
        songs = directory.listFiles();

        //PASAR SIGUIENTE CANCION
        botonSiguiente_SMV = (Button) findViewById(R.id.boton_siguiente);
        botonSiguiente_SMV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentSongIndex < songs.length -1){
                    CambiarCancion(currentSongIndex + 1);
                }
                mp.reset();
                try {
                    mp.setDataSource(songs[currentSongIndex].getAbsolutePath());
                    mp.prepare();
                    mp.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(VentanaReproducir.this, "Siguiente", Toast.LENGTH_SHORT).show();
            }
        });

        //VOLVER A LA CANCION ANTERIOR
        botonAnterior_SMV = (Button) findViewById(R.id.boton_anterior);
        botonAnterior_SMV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentSongIndex > 0){
                    CambiarCancion(currentSongIndex - 1);
                }
                mp.reset();
                try {
                    mp.setDataSource(songs[currentSongIndex].getAbsolutePath());
                    mp.prepare();
                    mp.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(VentanaReproducir.this, "Anterior", Toast.LENGTH_SHORT).show();
            }
        });


        //BOTON PARA VOLVER A LA ACTIVIDAD PRINCIPAL
        botonVolver_SMV = (Button) findViewById(R.id.boton_volver);
        botonVolver_SMV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                try{
                    mp.prepare();
                }catch(Exception e){
                    e.printStackTrace();
                }
                Intent miIntent_SMV = new Intent(VentanaReproducir.this,MainActivity.class);
                startActivity(miIntent_SMV);
            }
        });

    }


    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
    }

    private void CambiarCancion(int index){
        currentSongIndex = index;
        String rutaNewCancion = songs[currentSongIndex].getAbsolutePath();
        MediaMetadataRetriever retriever_SMV = new MediaMetadataRetriever();
        retriever_SMV.setDataSource(rutaNewCancion);
        String nombre = retriever_SMV.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        byte[] picture = retriever_SMV.getEmbeddedPicture();
        titulo_SMV.setText(nombre);
        portada_SMV.setImageBitmap(BitmapFactory.decodeByteArray(picture, 0, picture.length));
    }







}