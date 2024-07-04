package com.santimarinvela.reproductormusica;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptadorRecycle extends RecyclerView.Adapter<AdaptadorRecycle.ViewHolder> {
    private ArrayList<Canciones> listaDatos_SMV;
    private Context c;

    public AdaptadorRecycle(ArrayList<Canciones> listaDatos_SMV, Context c) {
        this.listaDatos_SMV = listaDatos_SMV;
        this.c = c;
    }

    @NonNull
    @Override
    public AdaptadorRecycle.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista_SMV = LayoutInflater.from(parent.getContext()).inflate(R.layout.campos_lista, null,false);
        return new ViewHolder(vista_SMV);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorRecycle.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Canciones song = listaDatos_SMV.get(position);
        holder.name_SMV.setText(song.getNombre_SMV());
        holder.autor_SMV.setText(song.getAutor_SMV());
        holder.album_SMV.setText(song.getAlbum_SMV());
        holder.photo_SMV.setImageBitmap(song.getFoto_SMV());
        holder.getAbsoluteAdapterPosition();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAbsoluteAdapterPosition();
                // Pasar los datos de la canción a la siguiente actividad
                Intent intent = new Intent(v.getContext(), VentanaReproducir.class);
                intent.putExtra("pos", position);
                intent.putExtra("songTitle", song.getNombre_SMV());
                intent.putExtra("songUrl", song.getPath());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaDatos_SMV.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container_SMV;
        TextView name_SMV;
        TextView autor_SMV;
        TextView album_SMV;
        ImageView photo_SMV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container_SMV = (LinearLayout) itemView.findViewById(R.id.contenedor);
            name_SMV = (TextView) itemView.findViewById(R.id.nombre);
            autor_SMV = (TextView) itemView.findViewById(R.id.autor);
            album_SMV = (TextView) itemView.findViewById(R.id.album);
            photo_SMV = (ImageView) itemView.findViewById(R.id.foto);
        }
    }
}
