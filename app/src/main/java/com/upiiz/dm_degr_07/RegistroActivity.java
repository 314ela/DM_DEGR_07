package com.upiiz.dm_degr_07;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.upiiz.dm_degr_07.data.db.CrudUsuario;
import com.upiiz.dm_degr_07.data.entities.Usuario;
import com.upiiz.dm_degr_07.utils.SessionManager;
import com.upiiz.dm_degr_07.utils.Validaciones;

// Registrarse.

public class RegistroActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword, tilNombre;
    private TextInputEditText etEmail, etPassword, etNombre;
    private MaterialCheckBox cbTerminos;
    private MaterialButton btnContinuar;

    private CrudUsuario crudUsuario;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.ime());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        crudUsuario = new CrudUsuario(this);
        session = new SessionManager(this);

        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilNombre = findViewById(R.id.tilNombre);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etNombre = findViewById(R.id.etNombre);
        cbTerminos = findViewById(R.id.cbTerminos);
        btnContinuar = findViewById(R.id.btnContinuar);

        btnContinuar.setOnClickListener(v -> registrar());
        // El ¿Tienes una cuenta? Ingresar
        findViewById(R.id.tvIrIngresar).setOnClickListener(v -> finish());
        findViewById(R.id.btnAtras).setOnClickListener(v -> finish());
    }

    private void registrar() {
        tilEmail.setError(null);
        tilPassword.setError(null);
        tilNombre.setError(null);

        String email = texto(etEmail).toLowerCase();
        String password = texto(etPassword);
        String nombre = Validaciones.capitalizar(texto(etNombre));

        boolean ok = true;
        if (email.isEmpty()) {
            tilEmail.setError(getString(R.string.campo_requerido));
            ok = false;
        } else if (!Validaciones.emailValido(email)) {
            tilEmail.setError(getString(R.string.email_invalido));
            ok = false;
        }
        if (password.isEmpty()) {
            tilPassword.setError(getString(R.string.campo_requerido));
            ok = false;
        } else if (password.length() < Validaciones.MIN_PASSWORD) {
            tilPassword.setError(getString(R.string.password_corta, Validaciones.MIN_PASSWORD));
            ok = false;
        }
        if (nombre.isEmpty()) {
            tilNombre.setError(getString(R.string.campo_requerido));
            ok = false;
        }
        if (!ok) return;

        if (!cbTerminos.isChecked()) {
            Toast.makeText(this, R.string.debe_aceptar, Toast.LENGTH_SHORT).show();
            return;
        }

        btnContinuar.setEnabled(false);
        Usuario nuevo = new Usuario(email, Validaciones.hashPassword(password), nombre);
        crudUsuario.registrar(nuevo, new CrudUsuario.OnRegistroListener() {
            @Override
            public void onExito(Usuario usuario) {
                // Inicia sesión y pasa a "Mis mascotas"
                session.iniciarSesion(usuario.getId(), usuario.getNombre(), usuario.getEmail());
                Toast.makeText(RegistroActivity.this, R.string.registro_exitoso,
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegistroActivity.this, MascotasActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onEmailDuplicado() {
                btnContinuar.setEnabled(true);
                tilEmail.setError(getString(R.string.email_duplicado));
                etEmail.requestFocus();
            }
        });
    }

    private String texto(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }
}
