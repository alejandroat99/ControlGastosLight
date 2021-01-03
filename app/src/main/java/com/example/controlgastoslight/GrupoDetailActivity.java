package com.example.controlgastoslight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.controlgastoslight.db.actions.GrupoActions;
import com.example.controlgastoslight.db.actions.RegistroActions;
import com.example.controlgastoslight.db.actions.RegistroGrupoCrossRefActions;
import com.example.controlgastoslight.db.model.Grupo;
import com.example.controlgastoslight.db.model.Registro;
import com.example.controlgastoslight.db.model.RegistroGrupoCrossRef;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GrupoDetailActivity extends AppCompatActivity {
    private Grupo grupo;
    private List<Registro> registros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo_detail);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int id = bundle.getInt("grupoId");

        GrupoActions ga = new GrupoActions(this);
        try {
            grupo = ga.getGrupo(id);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TextView textView = (TextView) findViewById(R.id.text_group_label);
        textView.setText(grupo.getLabel());

        System.out.println("Grupo BD: " + grupo);
        load_registros();
        System.out.println("Registros Cargados: " + registros.size());


    }

    private void load_registros() {
        RegistroGrupoCrossRefActions rga = new RegistroGrupoCrossRefActions(this);
        List<RegistroGrupoCrossRef> relaciones = rga.getRelacionByGrupo(grupo.getGrupoId());
        RegistroActions ra = new RegistroActions(this);
        registros = new ArrayList<>();
        for(RegistroGrupoCrossRef rel : relaciones){
            try {
                Registro r = ra.getRegistro(rel.getRegistroId());
                registros.add(r);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}