package com.example.controlgastoslight.db.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.util.Objects;

@Entity(primaryKeys = {"registroId", "grupoId"},
        tableName = "RegistroGrupoCrossRef",
        foreignKeys = {
                @ForeignKey(
                        entity = Registro.class,
                        parentColumns = {"registroId"},
                        childColumns = {"registroId"},
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Grupo.class,
                        parentColumns = {"grupoId"},
                        childColumns = {"grupoId"},
                        onDelete = ForeignKey.CASCADE
                )
        })
public class RegistroGrupoCrossRef{
    @ColumnInfo(index = true)
    public int registroId;

    @ColumnInfo(index = true)
    public int grupoId;

    public int getRegistroId() {
        return registroId;
    }

    public void setRegistroId(int registroId) {
        this.registroId = registroId;
    }

    public int getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(int grupoId) {
        this.grupoId = grupoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistroGrupoCrossRef that = (RegistroGrupoCrossRef) o;
        return registroId == that.registroId &&
                grupoId == that.grupoId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(registroId, grupoId);
    }
}

