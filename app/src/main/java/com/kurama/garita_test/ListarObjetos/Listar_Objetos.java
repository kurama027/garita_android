package com.kurama.garita_test.ListarObjetos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kurama.garita_test.ActualizarNota.Actualizar_Objeto;
import com.kurama.garita_test.Objetos.Objeto;
import com.kurama.garita_test.R;
import com.kurama.garita_test.ViewHolder.ViewHolder_Objeto;

import org.jetbrains.annotations.NotNull;

public class Listar_Objetos extends AppCompatActivity {

    RecyclerView recyclerviewObjetos;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BASE_DE_DATOS;


    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter<Objeto, ViewHolder_Objeto> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Objeto> options;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_objetos);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Listar");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        recyclerviewObjetos = findViewById(R.id.recyclerviewObjetos);
        recyclerviewObjetos.setHasFixedSize(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        BASE_DE_DATOS = firebaseDatabase.getReference("Objetos_Publicados");
        dialog = new Dialog(Listar_Objetos.this);
        ListarNotasUsuarios();
    }

    private void ListarNotasUsuarios(){
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
                        objeto.getEstado()
                );

            }

            @Override
            public ViewHolder_Objeto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_objeto,parent,false);
                ViewHolder_Objeto viewHolder_objeto = new ViewHolder_Objeto(view);
                viewHolder_objeto.setOnClickListener(new ViewHolder_Objeto.ClickListener(){

                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(Listar_Objetos.this, "on item click", Toast.LENGTH_SHORT).show();

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
                        String fecha_nota = getItem(position).getFecha_objeto();
                        String estado = getItem(position).getId_objeto();

                        //Declarar las vistas
                        Button CD_Eliminar, CD_Actualizar;

                        //Realizar la conexión con el diseño
                        dialog.setContentView(R.layout.dialogo_opciones);

                        //Inicializar las vistas
                        CD_Eliminar = dialog.findViewById(R.id.CD_Eliminar);
                        CD_Actualizar = dialog.findViewById(R.id.CD_Actualizar);
                        CD_Eliminar.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                EliminarNota(id_objeto);
                                dialog.dismiss();
                            }
                        });
                        CD_Actualizar.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                //Toast.makeText(Listar_Notas.this, "Actualizar nota", Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(Listar_Notas.this, Actualizar_Nota.class));
                                Intent intent = new Intent(Listar_Objetos.this, Actualizar_Objeto.class);
                                intent.putExtra("id_objeto",id_objeto);
                                intent.putExtra("uid_usuario",uid_usuario);
                                intent.putExtra("correo_usuario",correo_usuario);
                                intent.putExtra("fecha_hora_registro",fecha_hora_registro);
                                intent.putExtra("titulo",titulo);
                                intent.putExtra("descripcion",descripcion);
                                intent.putExtra("fecha_nota",fecha_nota);
                                intent.putExtra("estado",estado);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
                return viewHolder_objeto;
            }
        };

        linearLayoutManager = new LinearLayoutManager(Listar_Objetos.this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerviewObjetos.setLayoutManager(linearLayoutManager);
        recyclerviewObjetos.setAdapter(firebaseRecyclerAdapter);

    }


    private void EliminarNota(String id_objeto) {
        AlertDialog.Builder builder = new AlertDialog.Builder( Listar_Objetos.this );
        builder.setTitle("Eliminar Objeto");
        builder.setMessage("¿Desea eliminar el objeto?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //ELIMINAR NOTA EN BD
                Query query = BASE_DE_DATOS.orderByChild("id_objeto").equalTo(id_objeto);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(Listar_Objetos.this, "Eliminado", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Listar_Objetos.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Listar_Objetos.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter!=null){
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}