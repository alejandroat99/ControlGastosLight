package com.example.controlgastoslight;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
            EditText cantidad_text = (EditText) findViewById(R.id.edittext_cantidad);
            String s_cantidad = cantidad_text.getText().toString().trim();
            float cantidad;
            if(s_cantidad != null && !s_cantidad.isEmpty()) {
                cantidad = Float.valueOf(s_cantidad);
            }else{
                cantidad = 0;
            }
            Log.println(Log.INFO,"cantidad", String.valueOf(cantidad));
            if(tipo != 2){
                if(cantidad <= 0.0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle(R.string.error);
                    builder.setIcon(R.drawable.ic_error);
                    builder.setMessage(R.string.error_cantidad_meta);
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else {
                    meta.setCantidad(cantidad);
                    meta.setType(tipo);
                    meta.setYear(Calendar.getInstance().get(Calendar.YEAR));
                    meta.setTime_gap(tiempo);
                    switch (tiempo) {
                        case 0:
                            meta.setNumber(Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
                            break;
                        case 1:
                            meta.setNumber(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
                            break;
                        case 2:
                            meta.setNumber(Calendar.getInstance().get(Calendar.MONTH));
                            break;
                    }

                    DataBase db = (DataBase) SingletonMap.getSingletonMap("db");
                    long id = db.metaDao().addMeta(meta);
                    Toast.makeText(view.getContext(), R.string.goal_created, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }else {
                meta.setType(tipo);
                meta.setYear(Calendar.getInstance().get(Calendar.YEAR));
                meta.setTime_gap(tiempo);
                switch (tiempo) {
                    case 0:
                        meta.setNumber(Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
                        break;
                    case 1:
                        meta.setNumber(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
                        break;
                    case 2:
                        meta.setNumber(Calendar.getInstance().get(Calendar.MONTH));
                        break;
                }

                DataBase db = (DataBase) SingletonMap.getSingletonMap("db");
                long id = db.metaDao().addMeta(meta);
                Toast.makeText(view.getContext(), R.string.goal_created, Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }
}