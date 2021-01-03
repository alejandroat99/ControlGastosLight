package com.example.controlgastoslight.db.actions;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;


import com.example.controlgastoslight.db.database.DataBase;
import com.example.controlgastoslight.db.model.RegistroGrupoCrossRef;

import java.util.List;

public class RegistroGrupoCrossRefActions {
    private String DB_NAME = "ControlGastos";
    private DataBase db;

    public RegistroGrupoCrossRefActions(Context context){
        db = Room.databaseBuilder(context, DataBase.class, DB_NAME).build();
    }

    public void insertTask(int registroId, int grupoId){
        RegistroGrupoCrossRef rel = new RegistroGrupoCrossRef();
        rel.setRegistroId(registroId);
        rel.setGrupoId(grupoId);

        insert(rel);
    }

    public void insert(RegistroGrupoCrossRef rel){
        new insertAsyncTask(db, rel).execute();
    }

    private class insertAsyncTask extends AsyncTask<Void, Void, Void>{
        private DataBase db;
        private RegistroGrupoCrossRef r;

        insertAsyncTask(DataBase dao, RegistroGrupoCrossRef rel){
            db = dao;
            r = rel;
        }

        @Override
        protected Void doInBackground(Void... voids){
            db.registroGrupoCrossRefDao().insert(r);
            return null;
        }
    }

    public LiveData<RegistroGrupoCrossRef> getRelacion(int registroId, int grupoId){
        return db.registroGrupoCrossRefDao().getRelacion(registroId, grupoId);
    }

    public void delete(int registroId, int grupoId){
        LiveData<RegistroGrupoCrossRef> rel = getRelacion(registroId, grupoId);
        if(rel != null){
            new deleteAsyncTask(db, rel.getValue()).execute();
        }
    }

    public List<RegistroGrupoCrossRef> getRelacionByGrupo(int grupoId){
        return db.registroGrupoCrossRefDao().getRelacion(grupoId);
    }


    private class deleteAsyncTask extends AsyncTask<Void, Void, Void>{
        private DataBase db;
        private RegistroGrupoCrossRef r;

        deleteAsyncTask(DataBase dao, RegistroGrupoCrossRef rel){
            db = dao;
            r = rel;
        }

        @Override
        protected Void doInBackground(Void... voids){
            db.registroGrupoCrossRefDao().delete(r);
            return null;
        }
    }

    public List<RegistroGrupoCrossRef> getRelacionByRegistro(int registroId){
        return db.registroGrupoCrossRefDao().getRelacionByRegistro(registroId);
    }

}
