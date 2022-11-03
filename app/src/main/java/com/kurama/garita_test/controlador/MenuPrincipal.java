package com.kurama.garita_test.controlador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kurama.garita_test.AgregarObjeto.Agregar_Objeto;
import com.kurama.garita_test.ListarObjetos.Listar_Objetos;
import com.kurama.garita_test.ObjetosArchivados.Objetos_Archivados;
import com.kurama.garita_test.Perfil.Perfil_Usuario;
import com.kurama.garita_test.R;

public class MenuPrincipal extends AppCompatActivity {
    Button AgregarObjetos, ListarObjetos, Archivados,Perfil,CerrarSesion;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    TextView UidPrincipal, NombresPrincipal, CorreoPrincipal;
    ProgressBar progressBarDatos;
    DatabaseReference Usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Garita");
        NombresPrincipal = findViewById(R.id.NombresPrincipal);
        CorreoPrincipal = findViewById(R.id.CorreoPrincipal);
        progressBarDatos = findViewById(R.id.progressBarDatos);
        UidPrincipal = findViewById(R.id.UidPrincipal);
        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
        AgregarObjetos = findViewById(R.id.AgregarObjetos);
        ListarObjetos = findViewById(R.id.ListarObjetos);
        Archivados = findViewById(R.id.Archivados);
        Perfil = findViewById(R.id.Perfil);

        CerrarSesion = findViewById(R.id.CerrarSesion);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        AgregarObjetos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // obtenemos la informacion de los TextView
                String uid_usuario = UidPrincipal.getText().toString();
                String correo_usuario = CorreoPrincipal.getText().toString();

                Intent intent = new Intent(MenuPrincipal.this, Agregar_Objeto.class);
                intent.putExtra("Uid", uid_usuario);
                intent.putExtra("Correo", correo_usuario);
                startActivity(intent);
            }
        });

        ListarObjetos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, Listar_Objetos.class));
                Toast.makeText(MenuPrincipal.this, "Listar Objetos", Toast.LENGTH_SHORT).show();
            }
        });

        Archivados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, Objetos_Archivados.class));
                Toast.makeText(MenuPrincipal.this, "Objetos Archivadas", Toast.LENGTH_SHORT).show();
            }
        });

        Perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, Perfil_Usuario.class));
                Toast.makeText(MenuPrincipal.this, "Perfil Usuario", Toast.LENGTH_SHORT).show();
            }
        });

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalirAplicacion();
            }
        });
    }

    @Override
    protected void onStart() {
        ComprobarInicioSesion();
        super.onStart();
    }

    private void ComprobarInicioSesion(){
        if (user!=null){
            //El usuario ha iniciado sesión
            CargaDeDatos();
        }else {
            //Lo dirigirá al MainActivity
            startActivity(new Intent(MenuPrincipal.this,MainActivity.class));
            finish();
        }
    }

    private void CargaDeDatos(){
        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Si el usuario existe
                if (snapshot.exists()){
                    //El progressbar se oculta
                    progressBarDatos.setVisibility(View.GONE);
                    //Los TextView se muestran
                    UidPrincipal.setVisibility(View.VISIBLE);
                    NombresPrincipal.setVisibility(View.VISIBLE);
                    CorreoPrincipal.setVisibility(View.VISIBLE);

                    //Obtener los datos
                    String uid = ""+snapshot.child("uid").getValue();
                    String nombres = ""+snapshot.child("nombres").getValue();
                    String correo = ""+snapshot.child("correo").getValue();

                    //Setear los datos en los respectivos TextView
                    UidPrincipal.setText(uid);
                    NombresPrincipal.setText(nombres);
                    CorreoPrincipal.setText(correo);

                    //Habilitar los botones del menú
                    AgregarObjetos.setEnabled(true);
                    ListarObjetos.setEnabled(true);
                    Archivados.setEnabled(true);
                    Perfil.setEnabled(true);
                    CerrarSesion.setEnabled(true);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void SalirAplicacion() {
        firebaseAuth.signOut();
        startActivity(new Intent(MenuPrincipal.this,MainActivity.class));
        Toast.makeText(this, "Cerraste sesion exitosamente", Toast.LENGTH_SHORT).show();
    }
    }
