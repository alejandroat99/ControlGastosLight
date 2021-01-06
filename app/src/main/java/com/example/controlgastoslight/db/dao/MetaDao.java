package com.example.controlgastoslight.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.controlgastoslight.db.model.Meta;

import java.util.List;

@Dao
public interface MetaDao {
    @Query("SELECT * FROM metas")
    List<Meta> getMetas();

    @Query("SELECT * FROM metas WHERE metaId=:id LIMIT 1")
    Meta getMeta(int id);

    @Query("SELECT * FROM metas WHERE time_gap=0 AND number=:day AND year=:year")
    List<Meta> getMetasDiarias(int day, int year);

    @Query("SELECT * FROM metas WHERE time_gap=1 AND number=:semana AND year=:year")
    List<Meta> getMetasSemanales(int semana, int year);

    @Query("SELECT * FROM metas WHERE time_gap=2 AND number=:mes AND year=:year")
    List<Meta> getMetasMensuales(int mes, int year);

    @Query("SELECT * FROM metas WHERE time_gap=3 AND year=:year")
    List<Meta> getMetasAnuales(int year);

    @Insert
    long addMeta(Meta m);

    @Update
    void updateMeta(Meta m);

    @Delete
    void deleteMeta(Meta m);

}
