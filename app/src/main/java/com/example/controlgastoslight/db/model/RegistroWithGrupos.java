package com.example.controlgastoslight.db.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class RegistroWithGrupos{
    @Embedded public Registro registro;
    @Relation(
            parentColumn = "registroId",
            entityColumn = "grupoId",
            associateBy = @Junction(RegistroGrupoCrossRef.class)
    )
    public List<Grupo> grupos;
}

