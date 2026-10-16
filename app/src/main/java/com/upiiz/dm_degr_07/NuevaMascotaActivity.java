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

//Pantalla: Nueva mascota (CREATE).

public class NuevaMascotaActivity extends AppCompatActivity {

    private TextInputLayout tilNombre, tilRaza, tilEdad;
    private TextInputEditText etNombre, etEdad;
    private MaterialAutoCompleteTextView etRaza;
    private MaterialButton btnGuardar;

    private CrudMascota crudMascota;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nueva_mascota);
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
        btnGuardar = findViewById(R.id.btnGuardar);

        // Sugerencias de raza
        etRaza.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.razas_sugeridas)));

        btnGuardar.setOnClickListener(v -> guardar());
        findViewById(R.id.btnRegresar).setOnClickListener(v -> finish());
    }

    private void guardar() {
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

        btnGuardar.setEnabled(false);
        Mascota mascota = new Mascota(nombre, raza, edad, session.getUsuarioId());
        crudMascota.agregar(mascota, exito -> {
            if (exito) {
                Toast.makeText(this, getString(R.string.mascota_guardada, nombre),
                        Toast.LENGTH_SHORT).show();
                finish(); // regresa a "Mis mascotas"
            } else {
                btnGuardar.setEnabled(true);
                Toast.makeText(this, R.string.error_operacion, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String texto(CharSequence cs) {
        return cs == null ? "" : cs.toString().trim();
    }
}
