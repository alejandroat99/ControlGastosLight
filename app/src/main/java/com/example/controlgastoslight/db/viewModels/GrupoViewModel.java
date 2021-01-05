package com.example.controlgastoslight.db.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.controlgastoslight.db.actions.GrupoActions;
import com.example.controlgastoslight.db.model.Grupo;

import java.util.List;

public class GrupoViewModel extends AndroidViewModel {
    private GrupoActions grupoActions;

    public GrupoViewModel(@NonNull Application application) {
        super(application);
        grupoActions = new GrupoActions(application);
    }

    public List<Grupo> getGrupos() {
        try {
            return grupoActions.getAllGrupos();
        }catch (Exception e) {
            e.printStackTrace();
        } return null;
    }
}
