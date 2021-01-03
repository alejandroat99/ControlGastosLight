package com.example.controlgastoslight.db.actions;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;


import com.example.controlgastoslight.db.database.DataBase;
import com.example.controlgastoslight.db.model.Grupo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class GrupoActions {
    private String DB_NAME = "ControlGastos";
    private DataBase db;

    public GrupoActions(Context context){
        db = Room.databaseBuilder(context, DataBase.class, DB_NAME).build();
    }

    public List<Grupo> getAllGrupos() throws ExecutionException, InterruptedException {
        return new getAllAsyncTask(db).execute().get();
    }

    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<Grupo>>{
        private DataBase db;
        List<Grupo> g;

        getAllAsyncTask(DataBase dao){
            db = dao;
        }

        @Override
        protected  List<Grupo> doInBackground(Void... voids){
            return db.grupoDao().getGrupos();
        }
    }

    public void insertTask(String nombre){
        Grupo grupo = new Grupo();
        grupo.setLabel(nombre);

        insert(grupo);
    }

    public void insert(final Grupo grupo){
        new insertAsyncTask(db, grupo).execute();
    }

    private static class insertAsyncTask extends AsyncTask<Void, Void, Void>{
        private DataBase db;
        private Grupo g;

        insertAsyncTask(DataBase dao, Grupo grupo){
            db = dao;
            g = grupo;
        }

        @Override
        protected Void doInBackground(Void... voids){
            db.grupoDao().addGrupo(g);
            return null;
        }
    }

    public void delete(final int id){
        final LiveData<Grupo> grupo = getGrupo(id);
        if(grupo != null){
            new deleteAsyncTask(db, grupo.getValue()).execute();
        }
    }

    public void delete(final Grupo grupo){
        new deleteAsyncTask(db, grupo).execute();
    }

    public LiveData<Grupo> getGrupo(int id){
        return db.grupoDao().getGrupo(id);
    }

    private static class deleteAsyncTask extends AsyncTask<Void, Void, Void>{
        private DataBase db;
        private Grupo g;

        deleteAsyncTask(DataBase dao, Grupo grupo){
            db = dao;
            g = grupo;
        }

        @Override
        protected Void doInBackground(Void... voids){
            db.grupoDao().deleteGrupo(g);
            return null;
        }
    }

    public void update(final Grupo grupo){
        final LiveData<Grupo> g = getGrupo(grupo.getGrupoId());
        if(g != null){
            new updateAsyncTask(db, g.getValue()).execute();
        }
    }

    private static class updateAsyncTask extends AsyncTask<Void, Void, Void>{
        private DataBase db;
        private Grupo g;

        updateAsyncTask(DataBase dao, Grupo grupo){
            db = dao;
            g = grupo;
        }

        @Override
        protected Void doInBackground(Void... voids){
            db.grupoDao().updateGrupo(g);
            return null;
        }
    }
}
