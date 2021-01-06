package com.example.controlgastoslight.db.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.List;
import java.util.Objects;

@Entity(tableName = "metas")
public class Meta {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int metaId;

    @ColumnInfo(name = "year")
    private int year; // Year sobre el que tiene efecto la meta

    @ColumnInfo(name = "number")
    private int number; //Utilizado para indicar el dia, semana o mes del year

    @ColumnInfo(name = "time_gap")
    /**
     * values:
     *  - 0: meta diaria
     *  - 1: meta semanal
     *  - 2: meta mensual
     *  - 3: meta anual
     */
    private int time_gap;

    @ColumnInfo(name = "cantidad")
    private float cantidad; // Cantidad establecida para la meta

    @ColumnInfo(name = "type")
    /**
     * tipos de meta:
     *  - 0: No gastar mas de x€
     *  - 1: Obtener x€ en ingresos
     *  - 2: No obtener balance negativo
     */
    private int type;


    public int getMetaId() {
        return metaId;
    }

    public void setMetaId(int metaId) {
        this.metaId = metaId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTime_gap() {
        return time_gap;
    }

    public void setTime_gap(int time_gap) {
        this.time_gap = time_gap;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meta meta = (Meta) o;
        return metaId == meta.metaId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(metaId);
    }
}
