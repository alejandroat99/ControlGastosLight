package com.example.controlgastoslight.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.controlgastoslight.db.model.Grupo;
import com.example.controlgastoslight.db.model.GrupoWithRegistros;

import java.util.List;

@Dao
public interface GrupoDao {
    @Query("SELECT * FROM grupo")
    List<Grupo> getGrupos();

    @Query("SELECT * FROM grupo WHERE grupoId=:id")
    LiveData<Grupo> getGrupo(int id);

    @Insert
    void addGrupo(Grupo g);

    @Delete
    void deleteGrupo(Grupo r);

    @Update
    void updateGrupo(Grupo g);

    @Transaction
    @Query("SELECT * FROM grupo")
    public List<GrupoWithRegistros> getGrupoWithRegistros();
}