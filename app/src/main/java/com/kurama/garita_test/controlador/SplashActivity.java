package com.kurama.garita_test.controlador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kurama.garita_test.ListaUsuario.Lista_Usuario;
import com.kurama.garita_test.R;

public class SplashActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, Lista_Usuario.class));
                finish();
                VerificarUsuario();
            }
        }, 3000);
    }
    private void VerificarUsuario(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null){
            startActivity(new Intent(SplashActivity.this, Lista_Usuario.class));
        }else{
            startActivity(new Intent(SplashActivity.this,MenuPrincipal.class));
            finish();
        }
    }
}