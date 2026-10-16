package com.upiiz.dm_degr_07;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.upiiz.dm_degr_07.data.db.CrudMascota;
import com.upiiz.dm_degr_07.data.entities.Mascota;
import com.upiiz.dm_degr_07.utils.SessionManager;
import com.upiiz.dm_degr_07.utils.Validaciones;

//el update
public class ActualizarMascotaActivity extends AppCompatActivity {

    private TextInputLayout tilNombre, tilRaza, tilEdad;
    private TextInputEditText etNombre, etEdad;
    private MaterialAutoCompleteTextView etRaza;
    private MaterialButton btnActualizar;

    private CrudMascota crudMascota;
    private SessionManager session;
    private Mascota mascota; // la mascota que se está editando

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_actualizar_mascota);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.ime());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        crudMascota = new CrudMascota(this);
        session = new SessionManager(this);

        ((TextView) findViewById(R.id.tvUsuario)).setText(session.getNombre());

        tilNombre = findViewById(R.id.tilNombre);
        tilRaza = findViewById(R.id.tilRaza);
        tilEdad = findViewById(R.id.tilEdad);
        etNombre = findViewById(R.id.etNombre);
        etRaza = findViewById(R.id.etRaza);
        etEdad = findViewById(R.id.etEdad);
        btnActualizar = findViewById(R.id.btnActualizar);

        etRaza.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.razas_sugeridas)));

        btnActualizar.setEnabled(false); // hasta que cargue la mascota
        btnActualizar.setOnClickListener(v -> actualizar());
        findViewById(R.id.btnRegresar).setOnClickListener(v -> finish());

        long id = getIntent().getLongExtra(MascotasActivity.EXTRA_MASCOTA_ID, -1);
        cargarMascota(id);
    }

    private void cargarMascota(long id) {
        crudMascota.obtenerPorId(id, pet -> {
            // Si no existe o no pertenece al usuario logueado, no se puede editar
            if (pet == null || pet.getUsuarioId() != session.getUsuarioId()) {
                Toast.makeText(this, R.string.mascota_no_encontrada, Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            mascota = pet;
            etNombre.setText(pet.getNombre());
            etRaza.setText(pet.getRaza(), false); // false para no abrir sugerencias
            etEdad.setText(String.valueOf(pet.getEdad()));
            btnActualizar.setEnabled(true);
        });
    }

    private void actualizar() {
        if (mascota == null) return;
        tilNombre.setError(null);
        tilRaza.setError(null);
        tilEdad.setError(null);

        String nombre = Validaciones.capitalizar(texto(etNombre.getText()));
        String raza = Validaciones.capitalizar(texto(etRaza.getText()));
        String edadTxt = texto(etEdad.getText());

        boolean ok = true;
        if (nombre.isEmpty()) { tilNombre.setError(getString(R.string.campo_requerido)); ok = false; }
        if (raza.isEmpty())   { tilRaza.setError(getString(R.string.campo_requerido));   ok = false; }

        int edad = -1;
        if (edadTxt.isEmpty()) {
            tilEdad.setError(getString(R.string.campo_requerido));
            ok = false;
        } else {
            try { edad = Integer.parseInt(edadTxt); } catch (NumberFormatException ignored) { }
            if (edad < 0 || edad > Validaciones.EDAD_MAX) {
                tilEdad.setError(getString(R.string.edad_invalida, Validaciones.EDAD_MAX));
                ok = false;
            }
        }
        if (!ok) return;

        mascota.setNombre(nombre);
        mascota.setRaza(raza);
        mascota.setEdad(edad);

        btnActualizar.setEnabled(false);
        crudMascota.actualizar(mascota, exito -> {
            if (exito) {
                Toast.makeText(this, getString(R.string.mascota_actualizada, nombre),
                        Toast.LENGTH_SHORT).show();
                finish();
            } else {
                btnActualizar.setEnabled(true);
                Toast.makeText(this, R.string.error_operacion, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String texto(CharSequence cs) {
        return cs == null ? "" : cs.toString().trim();
    }
}
