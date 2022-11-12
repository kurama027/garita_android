package com.kurama.garita_test.Objetos;

public class Objeto {
    String id_objeto, uid_usuario, correo_usuario, fecha_hora_actual, titulo, descripcion, fecha_objeto, estado;

    public Objeto() {
    }

    public Objeto(String id_objeto, String uid_usuario, String correo_usuario, String fecha_hora_actual, String titulo, String descripcion, String fecha_objeto, String estado) {
        this.id_objeto = id_objeto;
        this.uid_usuario = uid_usuario;
        this.correo_usuario = correo_usuario;
        this.fecha_hora_actual = fecha_hora_actual;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha_objeto = fecha_objeto;
        this.estado = estado;
    }

    public String getId_objeto() {
        return id_objeto;
    }

    public void setId_objeto(String id_objeto) {
        this.id_objeto = id_objeto;
    }

    public String getUid_usuario() {
        return uid_usuario;
    }

    public void setUid_usuario(String uid_usuario) {
        this.uid_usuario = uid_usuario;
    }

    public String getCorreo_usuario() {
        return correo_usuario;
    }

    public void setCorreo_usuario(String correo_usuario) {
        this.correo_usuario = correo_usuario;
    }

    public String getFecha_hora_actual() {
        return fecha_hora_actual;
    }

    public void setFecha_hora_actual(String fecha_hora_actual) {
        this.fecha_hora_actual = fecha_hora_actual;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha_objeto() {
        return fecha_objeto;
    }

    public void setFecha_objeto(String fecha_objeto) {
        this.fecha_objeto = fecha_objeto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
