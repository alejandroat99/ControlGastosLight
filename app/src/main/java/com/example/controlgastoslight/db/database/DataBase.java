package com.example.controlgastoslight.db.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.controlgastoslight.db.dao.GrupoDao;
import com.example.controlgastoslight.db.dao.RegistroDao;
import com.example.controlgastoslight.db.dao.RegistroGrupoCrossRefDao;
import com.example.controlgastoslight.db.model.Grupo;
import com.example.controlgastoslight.db.model.Registro;
import com.example.controlgastoslight.db.model.RegistroGrupoCrossRef;


@Database(entities = {Registro.class, Grupo.class, RegistroGrupoCrossRef.class}, version = 1)
public abstract class DataBase extends RoomDatabase {
    public abstract RegistroDao registroDao();
    public abstract GrupoDao grupoDao();
    public abstract RegistroGrupoCrossRefDao registroGrupoCrossRefDao();
}
