package com.example.controlgastoslight.db.actions;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;


import com.example.controlgastoslight.db.database.DataBase;
import com.example.controlgastoslight.db.model.Registro;
import com.example.controlgastoslight.utils.SingletonMap;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RegistroActions {
    private String DB_NAME = "ControlGastos";
    private DataBase db;

    public RegistroActions(Context context){
        //db = Room.databaseBuilder(context, DataBase.class, DB_NAME).build();
        db = (DataBase) SingletonMap.getSingletonMap("db");
    }

    public List<Registro> getAllRegistro() throws ExecutionException, InterruptedException {
        return new getAllAsyncTask(db).execute().get();
    }

    public static class getAllAsyncTask extends AsyncTask<Void,Void,List<Registro>>{
        private DataBase db;
        List<Registro> rs;

        getAllAsyncTask(DataBase dao){
            db = dao;
        }

        @Override
        protected List<Registro> doInBackground(Void... voids){
            return db.registroDao().getRegistros();
        }
    }

    public Registro getRegistro(int id) throws ExecutionException, InterruptedException {
        return new getAsyncTask(db,id).execute().get();
    }

    private class getAsyncTask extends AsyncTask<Void,Void,Registro>{
        private DataBase db;
        private int id;

        getAsyncTask(DataBase dao, int id){
            db = dao;
            this.id = id;
        }

        @Override
        protected Registro doInBackground(Void... voids) {
            return db.registroDao().getRegistro(id);
        }
    }

    public void insertTask(double value, boolean gasto, String titulo, String descripcion, String fecha){
        Registro registro = new Registro();
        registro.setTitulo(titulo);
        registro.setDescripcion(descripcion);
        registro.setValue(value);
        registro.setGasto(gasto);
        registro.setFecha(fecha);

        insert(registro);
    }

    public void insert(final Registro registro){
        new insertAsyncTask(db, registro).execute();
    }

    private class insertAsyncTask extends AsyncTask<Void,Void,Void>{
        private DataBase db;
        private Registro r;

        insertAsyncTask(DataBase dao, Registro registro){
            db = dao;
            r = registro;
        }

        @Override
        public Void doInBackground(Void... voids){
            db.registroDao().addRegistro(r);
            return null;
        }
    }

    public void delete(final int id) throws ExecutionException, InterruptedException {
        final Registro registro = getRegistro(id);
        if(registro != null){
            new deleteAsyncTask(db, registro);
        }
    }

    private class deleteAsyncTask extends AsyncTask<Void,Void,Void>{
        private DataBase db;
        private Registro r;

        deleteAsyncTask(DataBase dao, Registro registro){
            db = dao;
            r = registro;
        }

        @Override
        public Void doInBackground(Void... voids){
            db.registroDao().deleteRegistro(r);
            return null;
        }
    }

    public void update(final Registro registro) throws ExecutionException, InterruptedException {
        final Registro r = getRegistro(registro.getRegistroId());
        if(r != null){
            new updateAsyncTask(db, registro).execute();
        }
    }

    private class updateAsyncTask extends AsyncTask<Void,Void,Void>{
        private DataBase db;
        private Registro r;

        updateAsyncTask(DataBase dao, Registro registro){
            db = dao;
            r = registro;
        }

        @Override
        public Void doInBackground(Void... voids){
            db.registroDao().updateRegistro(r);
            return null;
        }
    }
}
