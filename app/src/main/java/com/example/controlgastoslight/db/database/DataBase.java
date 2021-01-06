package com.example.controlgastoslight.db.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.controlgastoslight.db.dao.GrupoDao;
import com.example.controlgastoslight.db.dao.RegistroDao;
import com.example.controlgastoslight.db.dao.RegistroGrupoCrossRefDao;
import com.example.controlgastoslight.db.model.Grupo;
import com.example.controlgastoslight.db.model.Registro;
import com.example.controlgastoslight.db.model.RegistroGrupoCrossRef;


@Database(entities = {Registro.class, Grupo.class, RegistroGrupoCrossRef.class}, version = 2)
public abstract class DataBase extends RoomDatabase {
    // Singleton patron
    private static DataBase INSTANCE;

    public abstract RegistroDao registroDao();
    public abstract GrupoDao grupoDao();
    public abstract RegistroGrupoCrossRefDao registroGrupoCrossRefDao();

    public static synchronized DataBase getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context, DataBase.class, "ControlGastos").allowMainThreadQueries().
                            fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
