package com.example.controlgastoslight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.controlgastoslight.db.actions.RegistroActions;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        grupoViewModel = new ViewModelProvider(this).get(GrupoViewModel.class);

        // Associating Edit Texts
        eTTitle = findViewById(R.id.eTTitle);
        eTDescription = findViewById(R.id.eTDescription);
        eTQuantity = findViewById(R.id.formQuantity);

        // Setting Mov. Type Spinner
        spinnerMovType = findViewById(R.id.spinnerMovType);
        ArrayAdapter<CharSequence> spinnerMTAdapter = ArrayAdapter.createFromResource(this, R.array.movementTypes, android.R.layout.simple_spinner_item);
        spinnerMovType.setAdapter(spinnerMTAdapter);

        // Configuring save button
        btnSave = findViewById(R.id.btnSaveEntry);
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
    }

    private void saveChanges() {
        String title, description;
        float quantity;
        boolean movType;
        Grupo groupSelected;
        long registryId;

        groupSelected = (Grupo) spinnerGroup.getSelectedItem();

        title = eTTitle.getText().toString();
        description = eTDescription.getText().toString();
        quantity = Float.parseFloat(eTQuantity.getText().toString());
        movType = spinnerMovType.getSelectedItemPosition() != 0;


        Registro registry = new Registro();
        registry.setTitulo(title);
        registry.setDescripcion(description);
        registry.setGasto(movType);
        registry.setValue(quantity);
        registry.setFecha(Utils.getDate());
        registryId = DataBase.getInMemoryDatabase(this).registroDao().addRegistro(registry);
        System.out.println("Regstro Id: " + registryId);
        if(groupSelected.getGrupoId() != -1) { // Group selected
            RegistroGrupoCrossRef relacion = new RegistroGrupoCrossRef();
            relacion.setGrupoId(groupSelected.getGrupoId());
            relacion.setRegistroId((int) registryId);
            DataBase.getInMemoryDatabase(this).registroGrupoCrossRefDao().insert(relacion);
        }

        finish();
    }
}