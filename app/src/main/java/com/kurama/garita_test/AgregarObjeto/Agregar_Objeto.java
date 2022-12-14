package com.kurama.garita_test.AgregarObjeto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kurama.garita_test.Objetos.Objeto;
import com.kurama.garita_test.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Agregar_Objeto extends AppCompatActivity {

    TextView Uid_Usuario, Correo_usuario, Fecha_hora_actual, Fecha, Estado;
    ImageView Imagen_objeto;
    ImageView Editar_imagen;
    EditText Titulo, Descripcion;
    Button Btn_Calendario;

    int dia, mes , anio;



    DatabaseReference BD_Firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_objeto);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        InicializarVariables();
        ObtenerDatos();
        obtener_Fecha_Hora_Actual();
        Btn_Calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendario = Calendar.getInstance();
                dia = calendario.get(Calendar.DAY_OF_MONTH);
                mes = calendario.get(Calendar.MONTH);
                anio = calendario.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Agregar_Objeto.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int AnioSelecionado, int MesSelecionado, int DiaSelecionado) {
                        String diaFormateado, mesFormateado;

                        //Obtener el dia
                        if (DiaSelecionado < 10) {
                            diaFormateado = "0" + String.valueOf(DiaSelecionado);
                            //Antes: 9/11/2022 - Ahora 09/11/2022
                        } else {
                            diaFormateado = String.valueOf(DiaSelecionado);
                            //Ejemplo 13/08/2022
                        }
                        //Obtener el mes
                        int Mes = MesSelecionado + 1;
                        if (Mes < 10) {
                            mesFormateado = "0" + String.valueOf(Mes);
                            //Antes: 09/8/2022 - Ahora 09/08/2022
                        } else {
                            mesFormateado = String.valueOf(Mes);
                            //ejemplo 13/10/2022
                        }
                        //Setear fecha en TextView
                        Fecha.setText(diaFormateado + "/" + mesFormateado + "/" + AnioSelecionado);
                    }
                }
                        , anio, mes, dia);
                datePickerDialog.show();

            }
        });
    }
    private void InicializarVariables() {Uid_Usuario = findViewById(R.id.Uid_Usuario);
        Correo_usuario = findViewById(R.id.Correo_usuario);
        Fecha_hora_actual = findViewById(R.id.Fecha_hora_actual);
        Fecha = findViewById(R.id.Fecha);
        Estado = findViewById(R.id.Estado);
        Imagen_objeto = findViewById(R.id.Imagen_objeto);
        Titulo = findViewById(R.id.Titulo);
        Descripcion = findViewById(R.id.Descripcion);
        Btn_Calendario = findViewById(R.id.Btn_Calendario);
        BD_Firebase = FirebaseDatabase.getInstance().getReference();
    }

    private void ObtenerDatos() {
        String uid_recuperado = getIntent().getStringExtra("Uid");
        String correo_recuperado = getIntent().getStringExtra("Correo");



        Uid_Usuario.setText(uid_recuperado);
        Correo_usuario.setText(correo_recuperado);
    }

    private void obtener_Fecha_Hora_Actual() {
        String Fecha_hora_registro = new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss a", Locale.getDefault()).format(System.currentTimeMillis());
        Fecha_hora_actual.setText(Fecha_hora_registro);
    }

    private void Agregar_Objeto() {
//Obtener los datos
        String uid_usuario = Uid_Usuario.getText().toString();
        String correo_usuario = Correo_usuario.getText().toString();
        String fecha_hora_actual = Fecha_hora_actual.getText().toString();
        String titulo = Titulo.getText().toString();
        String descripcion = Descripcion.getText().toString();
        String fecha = Fecha.getText().toString();
        String estado = Estado.getText().toString();
        String id_objeto = BD_Firebase.push().getKey();


        ;

        //Validar datos
        if (!uid_usuario.equals("") && !correo_usuario.equals("") && !fecha_hora_actual.equals("") &&
                !titulo.equals("") && !descripcion.equals("") && ! fecha.equals("") && !estado.equals("")){

            Objeto objeto = new Objeto(id_objeto,
                    uid_usuario,
                    correo_usuario,
                    fecha_hora_actual,
                    titulo,
                    descripcion,
                    fecha,
                    estado,
                 ""
                   );

            String Objeto_usuario = BD_Firebase.push().getKey();
            //Establecer el nombre de la BD
            String Nombre_BD = "Objetos_Publicados";

            BD_Firebase.child(Nombre_BD).child(Objeto_usuario).setValue(objeto);

            Toast.makeText(this, "Se ha agregado el objeto exitosamente", Toast.LENGTH_SHORT).show();
            onBackPressed();

        }
        else {
            Toast.makeText(this, "Llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void Cargar_Imagen(String imagen_perfil) {
        try {
            /*Cuando la imagen ha sido traida exitosamente desde Firebase*/
            Glide.with(getApplicationContext()).load(imagen_perfil).placeholder(R.drawable.paimon_lost).into(Imagen_objeto);

        }catch (Exception e){
            /*Si la imagen no fue traida con éxito*/
            Glide.with(getApplicationContext()).load(R.drawable.paimon_lost).into(Imagen_objeto);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_agregar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Agregar_Objeto_BD:
                Agregar_Objeto();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}