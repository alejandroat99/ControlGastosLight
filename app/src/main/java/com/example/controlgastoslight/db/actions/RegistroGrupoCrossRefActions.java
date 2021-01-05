package com.example.controlgastoslight.db.actions;

import android.content.Context;
import android.os.AsyncTask;
import androidx.room.Room;
import com.example.controlgastoslight.db.database.DataBase;
import com.example.controlgastoslight.db.model.RegistroGrupoCrossRef;
import com.example.controlgastoslight.utils.SingletonMap;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RegistroGrupoCrossRefActions {
    private String DB_NAME = "ControlGastos";
    private DataBase db;

    public RegistroGrupoCrossRefActions(Context context){
        //db = Room.databaseBuilder(context, DataBase.class, DB_NAME).build();
        db = (DataBase) SingletonMap.getSingletonMap("db");
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

    public RegistroGrupoCrossRef getRelacion(int registroId, int grupoId) throws ExecutionException, InterruptedException {
        return new getAsyncTask(db,registroId,grupoId).execute().get();
    }

    private class getAsyncTask extends AsyncTask<Void,Void,RegistroGrupoCrossRef>{
        private final DataBase db;
        private final int registroId;
        private final int grupoId;

        getAsyncTask(DataBase db, int registroId, int grupoId){
            this.db=db;
            this.registroId=registroId;
            this.grupoId=grupoId;
        }

        @Override
        protected RegistroGrupoCrossRef doInBackground(Void... voids) {
            return db.registroGrupoCrossRefDao().getRelacion(registroId,grupoId);
        }
    }

    public void delete(int registroId, int grupoId) throws ExecutionException, InterruptedException {
        RegistroGrupoCrossRef rel = getRelacion(registroId, grupoId);
        if(rel != null){
            new deleteAsyncTask(db, rel).execute();
        }
    }

    public List<RegistroGrupoCrossRef> getRelacionByGrupo(int grupoId){
        try {
            return new getByGrupoAsyncTask(db,grupoId).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class getByGrupoAsyncTask extends AsyncTask<Void,Void,List<RegistroGrupoCrossRef>>{
        private DataBase db;
        private int grupoId;

        getByGrupoAsyncTask(DataBase db, int id){
            this.db = db;
            this.grupoId = id;
        }

        @Override
        protected List<RegistroGrupoCrossRef> doInBackground(Void... voids) {
            return db.registroGrupoCrossRefDao().getRelacionByGrupo(grupoId);
        }
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
        try {
            return new getByRegistroAsyncTask(db,registroId).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class getByRegistroAsyncTask extends AsyncTask<Void, Void, List<RegistroGrupoCrossRef>>{
        private DataBase db;
        private int registroId;

        getByRegistroAsyncTask(DataBase db, int id){
            this.db = db;
            this.registroId = id;
        }

        @Override
        protected List<RegistroGrupoCrossRef> doInBackground(Void... voids) {
            return db.registroGrupoCrossRefDao().getRelacionByRegistro(registroId);
        }
    }

}
