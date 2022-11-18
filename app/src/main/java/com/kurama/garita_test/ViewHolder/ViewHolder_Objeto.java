package com.kurama.garita_test.ViewHolder;
//permite conectar con la base de datos

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kurama.garita_test.R;

public class ViewHolder_Objeto extends RecyclerView.ViewHolder{

    View mView;

    private ViewHolder_Objeto.ClickListener mClickListener;

    public interface  ClickListener{
        void onItemClick(View view, int position);/*SE EJECUTA AL PRESIONAR EN EL ITEM*/
        void onItemLongClick(View view, int position);/*SE EJECUTA AL MANTENER PRESIONADO EN EL ITEM*/

    }

    public void setOnClickListener(ViewHolder_Objeto.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolder_Objeto(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getBindingAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getBindingAdapterPosition());
                return false;
            }
        });

    }

    //los id de objetos
    public void SetearDatos(Context context, String id_objeto , String uid_usuario, String correo_usuario,
                            String fecha_hora_registro, String titulo, String descripcion, String fecha_objeto,
                            String estado){

        //DECLARAR LAS VISTAS
        TextView Id_objeto_Item, Uid_Usuario_Item, Correo_usuario_Item,Fecha_hora_registro_Item,Titulo_Item,
                Descripcion_Item, Fecha_Item, Estado_Item;

        ImageView Objeto_Encontrado_Item, Objeto_No_Encontrado_Item;

        //ESTABLECER LA CONEXIÓN CON EL ITEM
        Id_objeto_Item = mView.findViewById(R.id.Id_objeto_Item);
        Uid_Usuario_Item = mView.findViewById(R.id.Uid_Usuario_Item);
        Correo_usuario_Item = mView.findViewById(R.id.Correo_usuario_Item);
        Fecha_hora_registro_Item = mView.findViewById(R.id.Fecha_hora_registro_Item);
        Titulo_Item = mView.findViewById(R.id.Titulo_Item);
        Descripcion_Item = mView.findViewById(R.id.Descripcion_Item);
        Fecha_Item = mView.findViewById(R.id.Fecha_Item);
        Estado_Item = mView.findViewById(R.id.Estado_Item);
        Objeto_Encontrado_Item = mView.findViewById(R.id.Objeto_Encontrado_Item);
        Objeto_No_Encontrado_Item = mView.findViewById(R.id.Objeto_No_Encontrado_Item);

        //SETEAR LA INFORMACIÓN DENTRO DEL ITEM
        Id_objeto_Item.setText(id_objeto);
        Uid_Usuario_Item.setText(uid_usuario);
        Correo_usuario_Item.setText(correo_usuario);
        Fecha_hora_registro_Item.setText(fecha_hora_registro);
        Titulo_Item.setText(titulo);
        Descripcion_Item.setText(descripcion);
        Fecha_Item.setText(fecha_objeto);
        Estado_Item.setText(estado);

        // GESTIONAMOS EL COLOR DEL ESTADO
        if(estado.equals("Encontrado")){
            Objeto_Encontrado_Item.setVisibility(View.VISIBLE);
        } else {
            Objeto_No_Encontrado_Item.setVisibility(View.VISIBLE);
        }
    }

}
