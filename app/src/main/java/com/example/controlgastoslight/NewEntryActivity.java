package com.example.controlgastoslight;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.controlgastoslight.db.actions.RegistroActions;
import com.example.controlgastoslight.db.model.Registro;

import java.util.Calendar;
import java.util.Date;

public class NewEntryActivity extends AppCompatActivity {
    RegistroActions rA;

    Spinner spinnerMovType;
    EditText eTTitle, eTDescription, eTQuantity;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        // Associating Edit Texts
        eTTitle = findViewById(R.id.eTTitle);
        eTDescription = findViewById(R.id.eTDescription);
        eTQuantity = findViewById(R.id.formQuantity);

        // Setting Spinner
        spinnerMovType = findViewById(R.id.spinnerMovType);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.movementTypes, android.R.layout.simple_spinner_item);
        spinnerMovType.setAdapter(spinnerAdapter);

        // Configuring save button
        btnSave = findViewById(R.id.btnSaveEntry);
        btnSave.setOnClickListener(v -> saveChanges());

        // Configuring RegistroActions
        rA = new RegistroActions(this.getBaseContext());
    }

    private void saveChanges() {
        String title, description;
        float quantity;
        boolean movType;

        title = eTTitle.getText().toString();
        description = eTDescription.getText().toString();
        quantity = Float.parseFloat(eTQuantity.getText().toString());
        movType = spinnerMovType.getSelectedItemPosition() != 0;

        Registro registry = new Registro();
        registry.setTitulo(title);
        registry.setDescripcion(description);
        registry.setGasto(movType);
        registry.setValue(quantity);
        registry.setFecha(Calendar.getInstance().getTime().toString());
        rA.insert(registry);
        finish();
    }
}