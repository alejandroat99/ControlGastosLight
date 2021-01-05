package com.example.controlgastoslight.db.actions;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.Room;


import com.example.controlgastoslight.db.dao.GrupoDao;
import com.example.controlgastoslight.db.database.DataBase;
import com.example.controlgastoslight.db.model.Grupo;
import com.example.controlgastoslight.utils.SingletonMap;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class GrupoActions {
    private String DB_NAME = "ControlGastos";
    private DataBase db;

    public GrupoActions(Context context){
        //db = DataBase.getInMemoryDatabase(context);
        db = (DataBase) SingletonMap.getSingletonMap("db");
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

    public void delete(final int id) throws ExecutionException, InterruptedException {
        final Grupo grupo = getGrupo(id);
        if(grupo != null){
            new deleteAsyncTask(db, grupo).execute();
        }
    }

    public void delete(final Grupo grupo){
        new deleteAsyncTask(db, grupo).execute();
    }

    public Grupo getGrupo(int id) throws ExecutionException, InterruptedException {
        return new getAsyncTask(db, id).execute().get();
    }

    private static class getAsyncTask extends AsyncTask<Void,Void, Grupo>{
        private DataBase db;
        private int id;

        getAsyncTask(DataBase dao, int id){
            db = dao;
            this.id = id;
        }

        @Override
        protected Grupo doInBackground(Void... voids) {
            return this.db.grupoDao().getGrupo(this.id);
        }
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

    public void update(final Grupo grupo) throws ExecutionException, InterruptedException {
        final Grupo g = getGrupo(grupo.getGrupoId());
        if(g != null){
            new updateAsyncTask(db, g).execute();
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
