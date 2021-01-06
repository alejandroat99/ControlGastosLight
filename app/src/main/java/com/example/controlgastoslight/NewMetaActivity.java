package com.example.controlgastoslight;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.controlgastoslight.db.database.DataBase;
import com.example.controlgastoslight.db.model.Meta;
import com.example.controlgastoslight.utils.SingletonMap;

import java.util.Calendar;

public class NewMetaActivity extends AppCompatActivity {
    private Spinner spinner_tiempo;
    private Spinner spinner_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meta);

        spinner_tiempo = (Spinner) findViewById(R.id.spinner_time);
        ArrayAdapter<CharSequence> adapter_time = ArrayAdapter.createFromResource(this,
                R.array.spinner_tiempo_meta, android.R.layout.simple_spinner_item);
        adapter_time.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tiempo.setAdapter(adapter_time);

        spinner_type = (Spinner) findViewById(R.id.spinner_type);
        ArrayAdapter<CharSequence> adapter_type = ArrayAdapter.createFromResource(this,
                R.array.spinner_type_goal, android.R.layout.simple_spinner_item);
        adapter_time.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(adapter_type);
    }

    public void save_goal(View view){
        int tiempo = spinner_tiempo.getSelectedItemPosition();
        int tipo = spinner_type.getSelectedItemPosition();
        if(tiempo != Spinner.INVALID_POSITION && tipo != Spinner.INVALID_POSITION) {

            Meta meta = new Meta();
            if(tipo != 2){
                EditText cantidad_text = (EditText) findViewById(R.id.edittext_cantidad);
                float cantidad = Float.valueOf(cantidad_text.getText().toString());
                meta.setCantidad(cantidad);
            }
            meta.setType(tipo);
            meta.setYear(Calendar.getInstance().get(Calendar.YEAR));
            meta.setTime_gap(tiempo);
            switch (tiempo){
                case 0: meta.setNumber(Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
                        break;
                case 1: meta.setNumber(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
                        break;
                case 2: meta.setNumber(Calendar.getInstance().get(Calendar.MONTH));
                        break;
            }

            DataBase db = (DataBase) SingletonMap.getSingletonMap("db");
            long id = db.metaDao().addMeta(meta);
            System.out.println("Meta guardada con ID: " + id);
            finish();

        }
    }
}