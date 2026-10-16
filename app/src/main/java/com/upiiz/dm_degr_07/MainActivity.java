package com.upiiz.dm_degr_07;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.upiiz.dm_degr_07.data.db.CrudUsuario;
import com.upiiz.dm_degr_07.utils.SessionManager;
import com.upiiz.dm_degr_07.utils.Validaciones;

// Ingresar (Login)
public class MainActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnIngresar, btnRegistrarse;

    private CrudUsuario crudUsuario;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(this);
        // Si ya hay sesión iniciada, pasa directo a "Mis mascotas"
        if (session.haySesion()) {
            irAMascotas();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.ime());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        crudUsuario = new CrudUsuario(this);

        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);

        btnIngresar.setOnClickListener(v -> ingresar());
        btnRegistrarse.setOnClickListener(v ->
                startActivity(new Intent(this, RegistroActivity.class)));

        etPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                ingresar();
                return true;
            }
            return false;
        });
    }

    private void ingresar() {
        tilEmail.setError(null);
        tilPassword.setError(null);

        String email = texto(etEmail).toLowerCase();
        String password = texto(etPassword);

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
        }
        if (!ok) return;

        btnIngresar.setEnabled(false);
        // Consulta en segundo plano y el resultado regresa al hilo principal
        crudUsuario.login(email, Validaciones.hashPassword(password), usuario -> {
            btnIngresar.setEnabled(true);
            if (usuario == null) {
                tilPassword.setError(getString(R.string.login_incorrecto));
                return;
            }
            session.iniciarSesion(usuario.getId(), usuario.getNombre(), usuario.getEmail());
            Toast.makeText(this, getString(R.string.bienvenida, usuario.getNombre()),
                    Toast.LENGTH_SHORT).show();
            irAMascotas();
        });
    }

    private void irAMascotas() {
        Intent intent = new Intent(this, MascotasActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private String texto(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }
}
