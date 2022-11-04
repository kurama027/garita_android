package com.kurama.garita_test.controlador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kurama.garita_test.R;

public class Login extends AppCompatActivity {

    EditText CorreoLogin, PassLogin;
    Button Btn_Logeo;
    TextView UsuarioNuevoTXT;

    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;

    //Validar los datos
    String correo = "" , password = "";

    private boolean passwordShowding = false;
    private ImageView ivPasswordIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Login");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        CorreoLogin = findViewById(R.id.CorreoLogin);
        PassLogin = findViewById(R.id.PassLogin);
        Btn_Logeo = findViewById(R.id.Btn_Logeo);
        UsuarioNuevoTXT = findViewById(R.id.UsuarioNuevoTXT);
        ivPasswordIcon = (ImageView) findViewById(R.id.iv_passwordShow);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        Btn_Logeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidarDatos();
            }
        });

        UsuarioNuevoTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Registro.class));
            }
        });

        ivPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //verificar si la contraseña es visible o no
                if (passwordShowding) {
                    passwordShowding = false;
                    PassLogin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ivPasswordIcon.setImageResource(R.drawable.ic_password_hide);
                } else {
                    passwordShowding = true;
                    PassLogin.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ivPasswordIcon.setImageResource(R.drawable.ic_password_show);
                }

                //Mover el cursor al final del texto
                PassLogin.setSelection(PassLogin.length());
            }
        });

    }

    private void ValidarDatos() {
        correo = CorreoLogin.getText().toString();
        password = PassLogin.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Ingrese contraseña", Toast.LENGTH_SHORT).show();
        }
        else {
            LoginDeUsuario();
        }
    }

    private void LoginDeUsuario() {
        progressDialog.setMessage("Iniciando sesión ...");
        progressDialog.show();//permite el login
        firebaseAuth.signInWithEmailAndPassword(correo,password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(Login.this,MenuPrincipal.class));
                            Toast.makeText(Login.this, "Bienvenido(a): "+user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "Verifique si el correo y contraseña son los correctos", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}