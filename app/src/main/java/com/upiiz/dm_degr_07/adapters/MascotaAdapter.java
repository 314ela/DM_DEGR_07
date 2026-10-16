package com.upiiz.dm_degr_07.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.upiiz.dm_degr_07.R;
import com.upiiz.dm_degr_07.data.entities.Mascota;
import com.upiiz.dm_degr_07.utils.IconoMascota;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Adaptador del ListView de mascotas que guarda la lista completa y una lista filtrada para el buscador.
public class MascotaAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final Context context;
    private final List<Mascota> todas = new ArrayList<>();
    private final List<Mascota> visibles = new ArrayList<>();
    private String filtroActual = "";

    public MascotaAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    // Reemplaza los datos después de consultar la BD y vuelve a aplicar el filtro
    public void setMascotas(List<Mascota> mascotas) {
        todas.clear();
        if (mascotas != null) todas.addAll(mascotas);
        filtrar(filtroActual);
    }

    // Filtra por nombre o raza (Buscar mascota)
    public void filtrar(String texto) {
        filtroActual = texto == null ? "" : texto.trim().toLowerCase(Locale.ROOT);
        visibles.clear();
        if (filtroActual.isEmpty()) {
            visibles.addAll(todas);
        } else {
            for (Mascota m : todas) {
                String nombre = m.getNombre() == null ? "" : m.getNombre().toLowerCase(Locale.ROOT);
                String raza = m.getRaza() == null ? "" : m.getRaza().toLowerCase(Locale.ROOT);
                if (nombre.contains(filtroActual) || raza.contains(filtroActual)) {
                    visibles.add(m);
                }
            }
        }
        notifyDataSetChanged();
    }

    public int getTotal() {
        return todas.size();
    }

    @Override
    public int getCount() {
        return visibles.size();
    }

    @Override
    public Mascota getItem(int position) {
        return visibles.get(position);
    }

    @Override
    public long getItemId(int position) {
        Long id = visibles.get(position).getId();
        return id == null ? position : id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_mascota, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Mascota mascota = getItem(position);
        holder.tvNombre.setText(mascota.getNombre());
        holder.tvDetalle.setText(mascota.getRaza());
        holder.tvEdad.setText(context.getResources().getQuantityString(
                R.plurals.edad_anios, mascota.getEdad(), mascota.getEdad()));
        holder.imgAvatar.setImageResource(IconoMascota.obtener(mascota.getRaza()));
        return convertView;
    }

    // Patrón ViewHolder: evita llamar findViewById en cada renglón
    private static class ViewHolder {
        final ImageView imgAvatar;
        final TextView tvNombre;
        final TextView tvDetalle;
        final TextView tvEdad;

        ViewHolder(View v) {
            imgAvatar = v.findViewById(R.id.imgAvatar);
            tvNombre = v.findViewById(R.id.tvNombre);
            tvDetalle = v.findViewById(R.id.tvDetalle);
            tvEdad = v.findViewById(R.id.tvEdad);
        }
    }
}
