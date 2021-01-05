package com.example.controlgastoslight.db.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.controlgastoslight.db.actions.GrupoActions;
import com.example.controlgastoslight.db.actions.RegistroActions;
import com.example.controlgastoslight.db.model.Grupo;
import com.example.controlgastoslight.db.model.Registro;

import java.util.List;

public class RegistroViewModel extends AndroidViewModel {
    private RegistroActions registroActions;

    public RegistroViewModel(@NonNull Application application) {
        super(application);
        registroActions = new RegistroActions(application);
    }

    public List<Registro> getRegistros() {
        try {
            return registroActions.getAllRegistro();
        }catch (Exception e) {
            e.printStackTrace();
        } return null;
    }

    public double[] getBalance() {
        List<Registro> registros = getRegistros();
        double[] amounts = new double[2];
        amounts[0] = 0.0;
        amounts[1] = 0.0;

        for(Registro r : registros) {
            if(r.isGasto()) {
                amounts[1] += r.getValue();
            } else {
                amounts[0] += r.getValue();
            }
        }

        return amounts;
    }
}
