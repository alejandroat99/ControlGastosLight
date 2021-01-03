package com.example.controlgastoslight.db.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class GrupoWithRegistros{
    @Embedded public Grupo grupo;
    @Relation(
            parentColumn = "grupoId",
            entityColumn = "registroId",
            associateBy = @Junction(RegistroGrupoCrossRef.class)
    )
    public List<Registro> registros;
}
