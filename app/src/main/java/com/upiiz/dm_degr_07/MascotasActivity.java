package com.upiiz.dm_degr_07;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.upiiz.dm_degr_07.adapters.MascotaAdapter;
import com.upiiz.dm_degr_07.data.db.CrudMascota;
import com.upiiz.dm_degr_07.data.entities.Mascota;
import com.upiiz.dm_degr_07.utils.IconoMascota;
import com.upiiz.dm_degr_07.utils.SessionManager;

// Pantallade Mis mascotas
public class MascotasActivity extends AppCompatActivity {

    public static final String EXTRA_MASCOTA_ID = "mascota_id";

    private ListView lvMascotas;
    private LinearLayout layoutVacio;
    private TextView tvContador, tvAyuda, tvVacioTitulo, tvVacioSub;
    private EditText etBuscar;

    private MascotaAdapter adapter;
    private CrudMascota crudMascota;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(this);
        if (!session.haySesion()) {   // Protección sin sesión regresa al login
            volverAlLogin();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mascotas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        crudMascota = new CrudMascota(this);

        TextView tvUsuario = findViewById(R.id.tvUsuario);
        lvMascotas = findViewById(R.id.lvMascotas);
        layoutVacio = findViewById(R.id.layoutVacio);
        tvContador = findViewById(R.id.tvContador);
        tvAyuda = findViewById(R.id.tvAyuda);
        tvVacioTitulo = findViewById(R.id.tvVacioTitulo);
        tvVacioSub = findViewById(R.id.tvVacioSub);
        etBuscar = findViewById(R.id.etBuscar);

        tvUsuario.setText(session.getNombre());

        adapter = new MascotaAdapter(this);
        lvMascotas.setAdapter(adapter);

        // Agregar -> NuevaMascotaActivity
        findViewById(R.id.btnAgregar).setOnClickListener(v ->
                startActivity(new Intent(this, NuevaMascotaActivity.class)));

        // Click -> ActualizarMascotaActivity
        lvMascotas.setOnItemClickListener((parent, view, position, id) -> {
            Mascota mascota = adapter.getItem(position);
            Intent intent = new Intent(this, ActualizarMascotaActivity.class);
            intent.putExtra(EXTRA_MASCOTA_ID, mascota.getId());
            startActivity(intent);
        });

        // Long click -> AlertDialog para confirmar eliminación
        lvMascotas.setOnItemLongClickListener((parent, view, position, id) -> {
            confirmarEliminar(adapter.getItem(position));
            return true; // true = consumimos el evento (no dispara el click normal)
        });

        // Buscar mascota
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filtrar(s.toString());
                actualizarEstado();
            }
            @Override public void afterTextChanged(Editable s) { }
        });

        // Salir -> cerrar sesión
        findViewById(R.id.btnSalir).setOnClickListener(v -> {
            session.cerrarSesion();
            volverAlLogin();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Se recarga al volver de Nueva / Actualizar
        if (crudMascota != null) cargarMascotas();
    }

    private void cargarMascotas() {
        crudMascota.obtenerPorUsuario(session.getUsuarioId(), mascotas -> {
            adapter.setMascotas(mascotas);
            actualizarEstado();
        });
    }

    private void confirmarEliminar(Mascota mascota) {
        new MaterialAlertDialogBuilder(this)
                .setIcon(IconoMascota.obtener(mascota.getRaza()))
                .setTitle(R.string.eliminar_titulo)
                .setMessage(getString(R.string.eliminar_mensaje, mascota.getNombre()))
                .setNegativeButton(R.string.cancelar, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(R.string.confirmar, (dialog, which) ->
                        crudMascota.eliminar(mascota, exito -> {
                            if (exito) {
                                Toast.makeText(this,
                                        getString(R.string.mascota_eliminada, mascota.getNombre()),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, R.string.error_operacion,
                                        Toast.LENGTH_SHORT).show();
                            }
                            cargarMascotas();
                        }))
                .show();
    }


    private void actualizarEstado() {
        int total = adapter.getTotal();
        tvContador.setText(getResources().getQuantityString(
                R.plurals.contador_mascotas, total, total));
        tvAyuda.setVisibility(total > 0 ? View.VISIBLE : View.GONE);

        boolean vacio = adapter.getCount() == 0;
        layoutVacio.setVisibility(vacio ? View.VISIBLE : View.GONE);
        lvMascotas.setVisibility(vacio ? View.GONE : View.VISIBLE);

        if (total == 0) {
            tvVacioTitulo.setText(R.string.sin_mascotas);
            tvVacioSub.setVisibility(View.VISIBLE);
        } else {
            tvVacioTitulo.setText(R.string.sin_resultados);
            tvVacioSub.setVisibility(View.GONE);
        }
    }

    private void volverAlLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
