package com.example.controlgastoslight.db.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "registro")
public class Registro {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int registroId;

    @ColumnInfo(name = "value")
    private double value;
    
    @ColumnInfo(name = "gasto")
    private boolean gasto; // True -> es un gasto, False -> es un ingreso
    
    @ColumnInfo(name = "titulo")
    private String titulo;
    
    @ColumnInfo(name = "descripcion")
    private String descripcion;
    
    @ColumnInfo(name = "fecha")
    private String fecha;

    public int getRegistroId() {
        return registroId;
    }

    public String getTitulo() {
        return titulo;
    }

    public double getValue() {
        return value;
    }

    public boolean isGasto() {
        return gasto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setRegistroId(int registroId) {
        this.registroId = registroId;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setGasto(boolean gasto) {
        this.gasto = gasto;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Registro registro = (Registro) o;
        return registroId == registro.registroId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(registroId);
    }
}
