package com.example.controlgastoslight.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.example.controlgastoslight.db.model.RegistroGrupoCrossRef;

import java.util.List;

@Dao
public interface RegistroGrupoCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(RegistroGrupoCrossRef rel);

    @Delete()
    void delete(RegistroGrupoCrossRef rel);

    @Query("SELECT * FROM RegistroGrupoCrossRef")
    List<RegistroGrupoCrossRef> getAll();

    @Query("SELECT * FROM RegistroGrupoCrossRef WHERE registroId=:registroId AND grupoId=:grupoId")
    RegistroGrupoCrossRef getRelacion(int registroId, int grupoId);

    @Query("SELECT * FROM RegistroGrupoCrossRef WHERE grupoId=:grupoId")
    List<RegistroGrupoCrossRef> getRelacionByGrupo(int grupoId);

    @Query("SELECT * FROM RegistroGrupoCrossRef WHERE registroId=:registroId")
    List<RegistroGrupoCrossRef> getRelacionByRegistro(int registroId);

}
