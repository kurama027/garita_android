package com.kurama.garita_test.ListaUsuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kurama.garita_test.ActualizarObjeto.Actualizar_Objeto;
import com.kurama.garita_test.Detalle.Detalle_Objeto;
import com.kurama.garita_test.ListarObjetos.Listar_Objetos;
import com.kurama.garita_test.Objetos.Objeto;
import com.kurama.garita_test.R;
import com.kurama.garita_test.ViewHolder.ViewHolder_Objeto;
import com.kurama.garita_test.controlador.Login;
import com.kurama.garita_test.controlador.MainActivity;
import com.kurama.garita_test.controlador.MenuPrincipal;
import com.kurama.garita_test.controlador.Pantalla_de_Carga;

public class Lista_Usuario extends AppCompatActivity {
    RecyclerView recyclerviewObjetos;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BASE_DE_DATOS;
    LinearLayoutManager linearLayoutManager;

    FirebaseRecyclerAdapter<Objeto, ViewHolder_Objeto> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Objeto> options;
    Dialog dialog, dialog_filtrar;
    FirebaseAuth auth;
    FirebaseUser user;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuario);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Objetos en Garita");

        recyclerviewObjetos = findViewById(R.id.recyclerviewObjetosUser);
        recyclerviewObjetos.setHasFixedSize(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dialog_filtrar = new Dialog(Lista_Usuario.this);
        BASE_DE_DATOS = firebaseDatabase.getReference("Objetos_Publicados");
        Estado_Filtro();

    }

    private void ListarTodasNotas(){
        options = new FirebaseRecyclerOptions.Builder<Objeto>().setQuery(BASE_DE_DATOS, Objeto.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Objeto, ViewHolder_Objeto>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Objeto viewHolder_objeto, int position, @NonNull Objeto objeto) {
                viewHolder_objeto.SetearDatos(
                        getApplicationContext(),
                        objeto.getId_objeto(),
                        objeto.getUid_usuario(),
                        objeto.getCorreo_usuario(),
                        objeto.getFecha_hora_actual(),
                        objeto.getTitulo(),
                        objeto.getDescripcion(),
                        objeto.getFecha_objeto(),
                        objeto.getEstado(),
                        objeto.getImagen()
                );

            }

            @Override
            public ViewHolder_Objeto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_objeto,parent,false);
                ViewHolder_Objeto viewHolder_objeto = new ViewHolder_Objeto(view);
                viewHolder_objeto.setOnClickListener(new ViewHolder_Objeto.ClickListener(){

                    @Override
                    public void onItemClick(View view, int position) {
                        //startActivity(new Intent(Listar_Objetos.this, Detalle_Objeto.class));
                        //Toast.makeText(Listar_Objetos.this, "on item click", Toast.LENGTH_SHORT).show();
                        //Obtener los datos de la nota seleccionada
                        String id_objeto = getItem(position).getId_objeto();
                        String uid_usuario = getItem(position).getUid_usuario();
                        String correo_usuario = getItem(position).getCorreo_usuario();
                        String fecha_hora_registro = getItem(position).getFecha_hora_actual();
                        String titulo = getItem(position).getTitulo();
                        String descripcion = getItem(position).getDescripcion();
                        String fecha_objeto = getItem(position).getFecha_objeto();
                        String estado = getItem(position).getEstado();
                        String imagen = getItem(position).getImagen();
                        //enviamos los datos a la siguieten actividad
                        Intent intent = new Intent(Lista_Usuario.this, Detalle_Usuario.class);
                        intent.putExtra("id_objeto",id_objeto);
                        intent.putExtra("uid_usuario",uid_usuario);
                        intent.putExtra("correo_usuario",correo_usuario);
                        intent.putExtra("fecha_hora_registro",fecha_hora_registro);
                        intent.putExtra("titulo",titulo);
                        intent.putExtra("descripcion",descripcion);
                        intent.putExtra("fecha_objeto",fecha_objeto);
                        intent.putExtra("estado",estado);
                        intent.putExtra("imagen", imagen);
                        startActivity(intent);

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        //Obtener los datos de la nota seleccionada
                        String id_objeto = getItem(position).getId_objeto();
                        String uid_usuario = getItem(position).getUid_usuario();
                        String correo_usuario = getItem(position).getCorreo_usuario();
                        String fecha_hora_registro = getItem(position).getFecha_hora_actual();
                        String titulo = getItem(position).getTitulo();
                        String descripcion = getItem(position).getDescripcion();
                        String fecha_objeto = getItem(position).getFecha_objeto();
                        String estado = getItem(position).getEstado();
                        String imagen = getItem(position).getImagen();

                    }
                });
                return viewHolder_objeto;
            }
        };

        linearLayoutManager = new LinearLayoutManager(Lista_Usuario.this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerviewObjetos.setLayoutManager(linearLayoutManager);
        recyclerviewObjetos.setAdapter(firebaseRecyclerAdapter);

    }

    private void ListarNotasEncontradas(){
        //Consulta
        String estado_nota = "Encontrado";
        Query query = BASE_DE_DATOS.orderByChild("estado").equalTo(estado_nota);
        options = new FirebaseRecyclerOptions.Builder<Objeto>().setQuery(query, Objeto.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Objeto, ViewHolder_Objeto>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Objeto viewHolder_objeto, int position, @NonNull Objeto objeto) {
                viewHolder_objeto.SetearDatos(
                        getApplicationContext(),
                        objeto.getId_objeto(),
                        objeto.getUid_usuario(),
                        objeto.getCorreo_usuario(),
                        objeto.getFecha_hora_actual(),
                        objeto.getTitulo(),
                        objeto.getDescripcion(),
                        objeto.getFecha_objeto(),
                        objeto.getEstado(),
                        objeto.getImagen()
                );

            }

            @Override
            public ViewHolder_Objeto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_objeto,parent,false);
                ViewHolder_Objeto viewHolder_objeto = new ViewHolder_Objeto(view);
                viewHolder_objeto.setOnClickListener(new ViewHolder_Objeto.ClickListener(){

                    @Override
                    public void onItemClick(View view, int position) {
                        //startActivity(new Intent(Listar_Objetos.this, Detalle_Objeto.class));
                        //Toast.makeText(Listar_Objetos.this, "on item click", Toast.LENGTH_SHORT).show();
                        //Obtener los datos de la nota seleccionada
                        String id_objeto = getItem(position).getId_objeto();
                        String uid_usuario = getItem(position).getUid_usuario();
                        String correo_usuario = getItem(position).getCorreo_usuario();
                        String fecha_hora_registro = getItem(position).getFecha_hora_actual();
                        String titulo = getItem(position).getTitulo();
                        String descripcion = getItem(position).getDescripcion();
                        String fecha_objeto = getItem(position).getFecha_objeto();
                        String estado = getItem(position).getEstado();
                        String imagen = getItem(position).getImagen();
                        //enviamos los datos a la siguieten actividad
                        Intent intent = new Intent(Lista_Usuario.this, Detalle_Usuario.class);
                        intent.putExtra("id_objeto",id_objeto);
                        intent.putExtra("uid_usuario",uid_usuario);
                        intent.putExtra("correo_usuario",correo_usuario);
                        intent.putExtra("fecha_hora_registro",fecha_hora_registro);
                        intent.putExtra("titulo",titulo);
                        intent.putExtra("descripcion",descripcion);
                        intent.putExtra("fecha_objeto",fecha_objeto);
                        intent.putExtra("estado",estado);
                        intent.putExtra("imagen", imagen);
                        startActivity(intent);

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        //Obtener los datos de la nota seleccionada
                        String id_objeto = getItem(position).getId_objeto();
                        String uid_usuario = getItem(position).getUid_usuario();
                        String correo_usuario = getItem(position).getCorreo_usuario();
                        String fecha_hora_registro = getItem(position).getFecha_hora_actual();
                        String titulo = getItem(position).getTitulo();
                        String descripcion = getItem(position).getDescripcion();
                        String fecha_objeto = getItem(position).getFecha_objeto();
                        String estado = getItem(position).getEstado();
                        String imagen = getItem(position).getImagen();

                    }
                });
                return viewHolder_objeto;
            }
        };

        linearLayoutManager = new LinearLayoutManager(Lista_Usuario.this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerviewObjetos.setLayoutManager(linearLayoutManager);
        recyclerviewObjetos.setAdapter(firebaseRecyclerAdapter);

    }

    private void ListarTodasPerdidas(){
        String estado_nota = "Perdido";
        Query query = BASE_DE_DATOS.orderByChild("estado").equalTo(estado_nota);
        options = new FirebaseRecyclerOptions.Builder<Objeto>().setQuery(query, Objeto.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Objeto, ViewHolder_Objeto>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Objeto viewHolder_objeto, int position, @NonNull Objeto objeto) {
                viewHolder_objeto.SetearDatos(
                        getApplicationContext(),
                        objeto.getId_objeto(),
                        objeto.getUid_usuario(),
                        objeto.getCorreo_usuario(),
                        objeto.getFecha_hora_actual(),
                        objeto.getTitulo(),
                        objeto.getDescripcion(),
                        objeto.getFecha_objeto(),
                        objeto.getEstado(),
                        objeto.getImagen()
                );

            }

            @Override
            public ViewHolder_Objeto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_objeto,parent,false);
                ViewHolder_Objeto viewHolder_objeto = new ViewHolder_Objeto(view);
                viewHolder_objeto.setOnClickListener(new ViewHolder_Objeto.ClickListener(){

                    @Override
                    public void onItemClick(View view, int position) {
                        //startActivity(new Intent(Listar_Objetos.this, Detalle_Objeto.class));
                        //Toast.makeText(Listar_Objetos.this, "on item click", Toast.LENGTH_SHORT).show();
                        //Obtener los datos de la nota seleccionada
                        String id_objeto = getItem(position).getId_objeto();
                        String uid_usuario = getItem(position).getUid_usuario();
                        String correo_usuario = getItem(position).getCorreo_usuario();
                        String fecha_hora_registro = getItem(position).getFecha_hora_actual();
                        String titulo = getItem(position).getTitulo();
                        String descripcion = getItem(position).getDescripcion();
                        String fecha_objeto = getItem(position).getFecha_objeto();
                        String estado = getItem(position).getEstado();
                        String imagen = getItem(position).getImagen();
                        //enviamos los datos a la siguieten actividad
                        Intent intent = new Intent(Lista_Usuario.this, Detalle_Usuario.class);
                        intent.putExtra("id_objeto",id_objeto);
                        intent.putExtra("uid_usuario",uid_usuario);
                        intent.putExtra("correo_usuario",correo_usuario);
                        intent.putExtra("fecha_hora_registro",fecha_hora_registro);
                        intent.putExtra("titulo",titulo);
                        intent.putExtra("descripcion",descripcion);
                        intent.putExtra("fecha_objeto",fecha_objeto);
                        intent.putExtra("estado",estado);
                        intent.putExtra("imagen", imagen);
                        startActivity(intent);

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        //Obtener los datos de la nota seleccionada
                        String id_objeto = getItem(position).getId_objeto();
                        String uid_usuario = getItem(position).getUid_usuario();
                        String correo_usuario = getItem(position).getCorreo_usuario();
                        String fecha_hora_registro = getItem(position).getFecha_hora_actual();
                        String titulo = getItem(position).getTitulo();
                        String descripcion = getItem(position).getDescripcion();
                        String fecha_objeto = getItem(position).getFecha_objeto();
                        String estado = getItem(position).getEstado();
                        String imagen = getItem(position).getImagen();

                    }
                });
                return viewHolder_objeto;
            }
        };

        linearLayoutManager = new LinearLayoutManager(Lista_Usuario.this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerviewObjetos.setLayoutManager(linearLayoutManager);
        recyclerviewObjetos.setAdapter(firebaseRecyclerAdapter);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_user, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(Lista_Usuario.this, Login.class));
        if (item.getItemId() == R.id.Filtrar_Notas){
            FiltrarNotas();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter!=null){
            firebaseRecyclerAdapter.startListening();
        }
    }

    private void FiltrarNotas(){
        Button Todas_objetos, Objetos_Encontrado, Objetos_Perdido;

        dialog_filtrar.setContentView(R.layout.cuadro_dialogo_filtrar_objetos);

        Todas_objetos = dialog_filtrar.findViewById(R.id.Todas_notas);
        Objetos_Encontrado = dialog_filtrar.findViewById(R.id.Objetos_Encontrado);
        Objetos_Perdido = dialog_filtrar.findViewById(R.id.Objetos_Perdido);

        Todas_objetos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Listar", "Todas");
                editor.apply();
                recreate();
                Toast.makeText(Lista_Usuario.this, "Todas las notas", Toast.LENGTH_SHORT).show();
                dialog_filtrar.dismiss();
            }
        });

        Objetos_Encontrado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Listar", "Encontrado");
                editor.apply();
                recreate();
                Toast.makeText(Lista_Usuario.this, "Objetos Encontrados", Toast.LENGTH_SHORT).show();
                dialog_filtrar.dismiss();
            }
        });

        Objetos_Perdido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Listar", "Perdido");
                editor.apply();
                recreate();
                Toast.makeText(Lista_Usuario.this, "Objetos Perdidos", Toast.LENGTH_SHORT).show();
                dialog_filtrar.dismiss();
            }
        });

        dialog_filtrar.show();
    }

    private void Estado_Filtro(){
        sharedPreferences = Lista_Usuario.this.getSharedPreferences("Objetos", MODE_PRIVATE);

        String estado_filtro = sharedPreferences.getString("Listar", "Todas");

        switch (estado_filtro){
            case "Todas":
                ListarTodasNotas();
                break;
            case "Encontrado":
                ListarNotasEncontradas();
                break;
            case "Perdido":
                ListarTodasPerdidas();
                break;
        }

    }


}