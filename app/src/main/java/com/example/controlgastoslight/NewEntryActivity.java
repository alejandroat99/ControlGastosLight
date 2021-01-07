package com.example.controlgastoslight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.controlgastoslight.db.actions.GrupoActions;
import com.example.controlgastoslight.db.actions.RegistroActions;
import com.example.controlgastoslight.db.actions.RegistroGrupoCrossRefActions;
import com.example.controlgastoslight.db.database.DataBase;
import com.example.controlgastoslight.db.model.Grupo;
import com.example.controlgastoslight.db.model.Registro;
import com.example.controlgastoslight.db.model.RegistroGrupoCrossRef;
import com.example.controlgastoslight.db.viewModels.GrupoViewModel;
import com.example.controlgastoslight.utils.Utils;

import java.util.List;

public class NewEntryActivity extends AppCompatActivity {
    GrupoViewModel grupoViewModel;
    RegistroActions rA;

    Spinner spinnerMovType, spinnerGroup;
    EditText eTTitle, eTDescription, eTQuantity;
    Button btnSave;

    Bundle bundle;

    int idRegistry;
    int idGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        grupoViewModel = new ViewModelProvider(this).get(GrupoViewModel.class);

        // Associating Edit Texts
        eTTitle = findViewById(R.id.edittext_label);
        eTDescription = findViewById(R.id.eTDescription);
        eTQuantity = findViewById(R.id.formQuantity);

        // Setting Mov. Type Spinner
        spinnerMovType = findViewById(R.id.spinnerMovType);
        ArrayAdapter<CharSequence> spinnerMTAdapter = ArrayAdapter.createFromResource(this, R.array.movementTypes, android.R.layout.simple_spinner_item);
        spinnerMovType.setAdapter(spinnerMTAdapter);

        // Configuring save button
        btnSave = findViewById(R.id.btn_save_group);
        btnSave.setOnClickListener(v -> saveChanges());

        // Configuring RegistroActions
        rA = new RegistroActions(this.getBaseContext());

        // Setting Group Spinner
        spinnerGroup = findViewById(R.id.spinnerGroup);

        // Configuring Grupos
        List<Grupo> groups = grupoViewModel.getGrupos();

        Grupo vacio = new Grupo();
        vacio.setLabel(getResources().getString(R.string.no_group));
        vacio.setGrupoId(-1);
        groups.add(0, vacio);

        ArrayAdapter spinnerGroupAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, groups);
        spinnerGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroup.setAdapter(spinnerGroupAdapter);

        // Getting Bundle
        bundle = getIntent().getExtras();

        idRegistry = -1;
        idGroup = -1;

        // Check if we are editing
        if(bundle.getBoolean("edit")) {
            // Change header
            TextView tVHeader = findViewById(R.id.tVNewRegistro);
            tVHeader.setText(getResources().getString(R.string.editar_registro));

            // Filling data
            fillData(spinnerGroupAdapter);
        }

    }

    private void fillData(ArrayAdapter spinnerGroupAdapter) {
        RegistroActions rA = new RegistroActions(this);
        GrupoActions gA = new GrupoActions(this);
        RegistroGrupoCrossRefActions refActions = new RegistroGrupoCrossRefActions(this);

        // Defining global variable
        idRegistry = bundle.getInt("regId");
        try {
            // Get registry
            Registro reg = rA.getRegistro(bundle.getInt("regId"));

            // Get relation
            RegistroGrupoCrossRef rg = null;
            List<RegistroGrupoCrossRef> l = refActions.getRelacionByRegistro(reg.getRegistroId());

            if(l != null && l.size()>0) { // Is relationed
                rg = l.get(0);
            }

            // Get Group
            Grupo grp = null;
            if(rg != null) {
                idGroup = rg.getGrupoId();
                grp = gA.getGrupo(idGroup);
            }
            // Basics
            eTTitle.setText(reg.getTitulo());
            eTDescription.setText(reg.getDescripcion());
            eTQuantity.setText(Double.toString(reg.getValue()));

            // Movement type spinner
            int pos_movType = reg.isGasto() ? 1 : 0;
            spinnerMovType.setSelection(pos_movType);

            // Group spinner
            int pos_grupo = 0;

            if(grp != null) {
                pos_grupo = spinnerGroupAdapter.getPosition(grp);
            }
            spinnerGroup.setSelection(pos_grupo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveChanges() {
        String title, description;
        float quantity;
        boolean movType;
        Grupo groupSelected;
        long registryId;

        groupSelected = (Grupo) spinnerGroup.getSelectedItem();

        // Getting data
        title = eTTitle.getText().toString();
        description = eTDescription.getText().toString();
        quantity = Float.parseFloat(eTQuantity.getText().toString());
        movType = spinnerMovType.getSelectedItemPosition() != 0;

        // Creating models
        Registro registry = new Registro();
        RegistroGrupoCrossRef rel = new RegistroGrupoCrossRef();

        // Fill registry
        if(idRegistry!= -1) { // Editing existing Registry
            registry.setRegistroId(idRegistry);
        }
        registry.setTitulo(title);
        registry.setDescripcion(description);
        registry.setGasto(movType);
        registry.setValue(quantity);
        registry.setFecha(Utils.getDate());

        // Persist
        if(idRegistry != -1) {// Modification
            DataBase.getInMemoryDatabase(this).registroDao().updateRegistro(registry);
        } else { // Modification
            idRegistry = (int) DataBase.getInMemoryDatabase(this).registroDao().addRegistro(registry);
        }

        // Fill relation
        rel.setRegistroId(idRegistry);

        if(idGroup != -1) { // Editing existing relation
            rel.setGrupoId(idGroup);
        }

        if(groupSelected.getGrupoId() != -1) { // Group selected
            // Persist
            if(idGroup != -1) {// Modification
                // Es tarde y no me quiero poner con el update del CrossRef, así que borro y vuelvo a insertar
                DataBase.getInMemoryDatabase(this).registroGrupoCrossRefDao().delete(rel);
                rel.setGrupoId(groupSelected.getGrupoId());
                DataBase.getInMemoryDatabase(this).registroGrupoCrossRefDao().insert(rel);
            } else {// Creation
                rel.setGrupoId(groupSelected.getGrupoId());
                DataBase.getInMemoryDatabase(this).registroGrupoCrossRefDao().insert(rel);
            }
        } else if(idGroup != -1) {// Delete existing relation
            // Persist delete
            DataBase.getInMemoryDatabase(this).registroGrupoCrossRefDao().delete(rel);
        }

        finish();
    }
}