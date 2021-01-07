package com.example.controlgastoslight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.controlgastoslight.db.actions.GrupoActions;
import com.example.controlgastoslight.db.actions.RegistroActions;
import com.example.controlgastoslight.db.actions.RegistroGrupoCrossRefActions;
import com.example.controlgastoslight.db.database.DataBase;
import com.example.controlgastoslight.db.model.Grupo;
import com.example.controlgastoslight.db.model.Registro;
import com.example.controlgastoslight.db.model.RegistroGrupoCrossRef;
import com.example.controlgastoslight.utils.SingletonMap;
import com.example.controlgastoslight.utils.Utils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.maltaisn.icondialog.data.Icon;
import com.maltaisn.icondialog.pack.IconPack;

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

        ImageView icon_view = (ImageView) findViewById(R.id.icono_view);
        int icono_id = grupo.getIcono();
        int color = grupo.getColor();

        if(icono_id != -1){
            IconPack iconPack = (IconPack) SingletonMap.getSingletonMap("iconPack");
            Icon icon = iconPack.getIcon(icono_id);
            Drawable icono_image = icon.getDrawable();

            if(color != -1){
                icono_image.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }
            icon_view.setBackground(icono_image);
        }

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
        String[] x_labels = new String[fechas.keySet().size()];
        for(String key : fechas.keySet()){
            x_labels[index] = key;
            Map<Boolean, List<Registro>> registros_date_type = Utils.groupByType(fechas.get(key));
            float gastos_day_total = Utils.sum(registros_date_type.get(true));
            float ingresos_day_total = Utils.sum(registros_date_type.get(false));
            entries_gastos.add(new Entry(index, gastos_day_total));
            entries_ingresos.add(new Entry(index, ingresos_day_total));
            index++;
        }
        LineDataSet dataSet_gastos = new LineDataSet(entries_gastos, getString(R.string.spend));
        dataSet_gastos.setColor(getColor(R.color.p_rojo));
        dataSet_gastos.setCircleColor(getColor(R.color.p_rojo));
        LineDataSet dataSet_ingresos = new LineDataSet(entries_ingresos, getString(R.string.income));
        dataSet_ingresos.setColor(getColor(R.color.verde_money));
        dataSet_ingresos.setCircleColor(getColor(R.color.verde_money));

        LineData data = new LineData();
        data.addDataSet(dataSet_gastos);
        data.addDataSet(dataSet_ingresos);

        chart.setData(data);
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(x_labels));
        chart.invalidate();
    }

    private void load_linechart_all() {
        LineChart chart = (LineChart) findViewById(R.id.group_linechart_all);
        Map<String, List<Registro>> fechas = Utils.groupByDate(registros);

        List<Entry> entries_gastos = new ArrayList<>();
        List<Entry> entries_ingresos = new ArrayList<>();

        int index = 0;
        String[] x_labels = new String[fechas.keySet().size()];
        for(String key : fechas.keySet()){
            x_labels[index] = key;
            Map<Boolean, List<Registro>> registros_date_type = Utils.groupByType(fechas.get(key));
            float gastos_day_total = Utils.sum(registros_date_type.get(true));
            float ingresos_day_total = Utils.sum(registros_date_type.get(false));
            entries_gastos.add(new Entry(index, gastos_day_total));
            entries_ingresos.add(new Entry(index, ingresos_day_total));
            index++;
        }
        LineDataSet dataSet_gastos = new LineDataSet(entries_gastos, getString(R.string.spend));
        dataSet_gastos.setColor(getColor(R.color.p_rojo));
        dataSet_gastos.setCircleColor(getColor(R.color.p_rojo));
        LineDataSet dataSet_ingresos = new LineDataSet(entries_ingresos, getString(R.string.income));
        dataSet_ingresos.setColor(getColor(R.color.verde_money));
        dataSet_ingresos.setCircleColor(getColor(R.color.verde_money));

        LineData data = new LineData();
        data.addDataSet(dataSet_gastos);
        data.addDataSet(dataSet_ingresos);

        chart.setData(data);
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(x_labels));
        chart.invalidate();
    }

    private void load_piechart_porcentajes() {
        PieChart chart = (PieChart) findViewById(R.id.group_piechart_porcentajes);
        if(registros.size() > 0) {
            Map<Boolean, List<Registro>> tipos = Utils.groupByType(registros);
            float total = registros.size();
            float gastos = tipos.get(true).size() / total;
            float ingresos = tipos.get(false).size() / total;

            List<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(gastos, getString(R.string.spend)));
            entries.add(new PieEntry(ingresos, getString(R.string.income)));

            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setColors(
                    getColor(R.color.p_rojo), getColor(R.color.verde_money)
            );
            PieData data = new PieData(dataSet);
            chart.setData(data);
            chart.setUsePercentValues(true);
            chart.setDrawEntryLabels(true);
            chart.setEntryLabelColor(getColor(R.color.p_negro));
        }
        chart.invalidate();
    }

    private void load_barchar_totals() {
        BarChart chart = (BarChart) findViewById(R.id.group_barchart_totals);

        Map<Boolean, List<Registro>> tipos = Utils.groupByType(registros);
        float gastos = Utils.sum(tipos.get(true));
        float ingresos = Utils.sum(tipos.get(false));

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, gastos));
        entries.add(new BarEntry(1, ingresos));

        BarDataSet dataSet = new BarDataSet(entries, getString(R.string.spend) + " " + getString(R.string.income));
        dataSet.setColors(
                getColor(R.color.p_rojo), getColor(R.color.verde_money)
        );
        BarData data = new BarData(dataSet);
        chart.setData(data);

        String[] x_labels = {getString(R.string.spend), getString(R.string.income)};
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(x_labels));
    }

    private void load_registros() {
        List<RegistroGrupoCrossRef> relaciones = DataBase.getInMemoryDatabase(this).registroGrupoCrossRefDao()
                .getRelacionByGrupo(grupo.getGrupoId());

        System.out.println("Relaciones Cargadas: " + relaciones.size());
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