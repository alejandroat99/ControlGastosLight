package com.example.controlgastoslight.db.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "grupo")
public class Grupo {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int grupoId;
    
    @ColumnInfo(name = "label")
    private String label;

    @ColumnInfo(name = "icono")
    private int icono;

    @ColumnInfo(name = "color")
    private int color;

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getGrupoId() {
        return grupoId;
    }

    public String getLabel() {
        return label;
    }

    public void setGrupoId(int grupoId) {
        this.grupoId = grupoId;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grupo grupo = (Grupo) o;
        return grupoId == grupo.grupoId &&
                label.equals(grupo.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(grupoId, label);
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
