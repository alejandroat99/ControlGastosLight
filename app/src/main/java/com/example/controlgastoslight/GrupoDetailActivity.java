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
import com.example.controlgastoslight.utils.Utils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
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

        load_registros();
        load_graphs();


    }

    private void load_graphs() {
        load_barchar_totals();
        load_piechart_porcentajes();
        load_linechart_month();
        load_linechart_all();
    }

    private void load_linechart_month() {
        LineChart chart = (LineChart) findViewById(R.id.group_month_record);
        List<Registro> registros_month = new ArrayList<>();

        for (Registro r : registros){
            int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            int year = Calendar.getInstance().get(Calendar.YEAR);

            if(r.getFecha().contains(month + "/" + year)){
                registros_month.add(r);
            }
        }

        Map<String, List<Registro>> fechas = Utils.groupByDate(registros);

        List<Entry> entries_gastos = new ArrayList<>();
        List<Entry> entries_ingresos = new ArrayList<>();

        int index = 0;
        for(String key : fechas.keySet()){
            Map<Boolean, List<Registro>> registros_date_type = Utils.groupByType(fechas.get(key));
            float gastos_day_total = Utils.sum(registros_date_type.get(true));
            float ingresos_day_total = Utils.sum(registros_date_type.get(false));
            entries_gastos.add(new Entry(index, gastos_day_total));
            entries_ingresos.add(new Entry(index, ingresos_day_total));
            index++;
        }
        LineDataSet dataSet_gastos = new LineDataSet(entries_gastos, "");
        dataSet_gastos.setColor(R.color.gasto);
        dataSet_gastos.setCircleColor(R.color.gasto);
        LineDataSet dataSet_ingresos = new LineDataSet(entries_ingresos, "");
        dataSet_ingresos.setColor(R.color.ingreso);
        dataSet_ingresos.setCircleColor(R.color.ingreso);

        LineData data = new LineData();
        data.addDataSet(dataSet_gastos);
        data.addDataSet(dataSet_ingresos);

        chart.setData(data);
    }

    private void load_linechart_all() {
        LineChart chart = (LineChart) findViewById(R.id.group_linechart_all);
        Map<String, List<Registro>> fechas = Utils.groupByDate(registros);

        List<Entry> entries_gastos = new ArrayList<>();
        List<Entry> entries_ingresos = new ArrayList<>();

        int index = 0;
        for(String key : fechas.keySet()){
            Map<Boolean, List<Registro>> registros_date_type = Utils.groupByType(fechas.get(key));
            float gastos_day_total = Utils.sum(registros_date_type.get(true));
            float ingresos_day_total = Utils.sum(registros_date_type.get(false));
            entries_gastos.add(new Entry(index, gastos_day_total));
            entries_ingresos.add(new Entry(index, ingresos_day_total));
            index++;
        }
        LineDataSet dataSet_gastos = new LineDataSet(entries_gastos, "");
        dataSet_gastos.setColor(R.color.gasto);
        dataSet_gastos.setCircleColor(R.color.gasto);
        LineDataSet dataSet_ingresos = new LineDataSet(entries_ingresos, "");
        dataSet_ingresos.setColor(R.color.ingreso);
        dataSet_ingresos.setCircleColor(R.color.ingreso);

        LineData data = new LineData();
        data.addDataSet(dataSet_gastos);
        data.addDataSet(dataSet_ingresos);

        chart.setData(data);
    }

    private void load_piechart_porcentajes() {
        PieChart chart = (PieChart) findViewById(R.id.group_piechart_porcentajes);

        Map<Boolean, List<Registro>> tipos = Utils.groupByType(registros);
        float total = registros.size();
        float gastos = tipos.get(true).size() / total;
        float ingresos = tipos.get(false).size() / total;

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(gastos, R.string.spend));
        entries.add(new PieEntry(ingresos, R.string.income));

        PieDataSet dataSet = new PieDataSet(entries, " ");
        dataSet.setColors(R.color.gasto, R.color.ingreso);
        PieData data = new PieData(dataSet);
        chart.setData(data);
    }

    private void load_barchar_totals() {
        BarChart chart = (BarChart) findViewById(R.id.group_barchart_totals);

        Map<Boolean, List<Registro>> tipos = Utils.groupByType(registros);
        float gastos = Utils.sum(tipos.get(true));
        float ingresos = Utils.sum(tipos.get(false));

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, gastos));
        entries.add(new BarEntry(1, ingresos));

        BarDataSet dataSet = new BarDataSet(entries, " ");
        dataSet.setColors(R.color.gasto, R.color.ingreso);
        BarData data = new BarData(dataSet);
        chart.setData(data);
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