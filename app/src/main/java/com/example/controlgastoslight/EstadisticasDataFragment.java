package com.example.controlgastoslight;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.controlgastoslight.db.actions.RegistroActions;
import com.example.controlgastoslight.db.model.Registro;
import com.example.controlgastoslight.utils.Utils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EstadisticasDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class    EstadisticasDataFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_MODE = "mode";

    // TODO: Rename and change types of parameters
    private int mode;

    public EstadisticasDataFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static EstadisticasDataFragment newInstance(int mode) {
        EstadisticasDataFragment fragment = new EstadisticasDataFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getInt(ARG_MODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_estadisticas_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        List<Registro> registros = null;
        switch (this.mode){
            case 0:
                    registros = Utils.getRegistrosToday(view.getContext());
                    break;
            case 1:
                    registros = Utils.getRegistrosWeek(view.getContext());
                    break;
            case 2:
                    registros = Utils.getRegistrosMonth(view.getContext());
                    break;
            case 3:
                    registros = Utils.getRegistrosYear(view.getContext());
                    break;
        }
        load_graphs(view.findViewById(R.id.linear_layout), registros);


    }

    /**
     * Este metodo cargara e insertara en la pantalla las estadisticas correspondientes
     * de cada filtro de tiempo
     * @param linear_layout
     * @param registros
     */
    private void load_graphs(LinearLayout linear_layout, List<Registro> registros) {
        if (registros != null && registros.size() > 0) {
            load_chart_resume(linear_layout, registros);
            load_piechart_porcentajes(linear_layout, registros);
            load_linechart_balance(linear_layout, registros);
        }
    }

    /**
     * Carga un resumen de los gastos e ingresos totales, en el caso del filtro dia se
     * muestra un grafico de barras con los totales diarios, en los otros filtros se
     * muestra un grafico de puntos con los totales de cada dia y de cada tipo.
     * @param linear_layout
     * @param registros
     */
    private void load_chart_resume(LinearLayout linear_layout, List<Registro> registros) {
        if(this.mode == 0){
            BarChart barchart = new BarChart(linear_layout.getContext());
            Map<Boolean, List<Registro>> tipos = Utils.groupByType(registros);
            List<BarEntry> barEntries = new ArrayList<>();
            barEntries.add(new BarEntry(0, Utils.sum(tipos.get(true))));
            barEntries.add(new BarEntry(1, Utils.sum(tipos.get(false))));

            BarDataSet barDataSet = new BarDataSet(barEntries, "");
            barDataSet.setColors(
                    ContextCompat.getColor(linear_layout.getContext(), R.color.gasto),
                    ContextCompat.getColor(linear_layout.getContext(), R.color.ingreso)
            );
            BarData barData = new BarData(barDataSet);
            barchart.setData(barData);
            barchart.setMinimumHeight(646);

            XAxis xAxis = barchart.getXAxis();
            xAxis.setGranularity(1f);
            String[] x_labels = {getString(R.string.spend), getString(R.string.income)};
            xAxis.setValueFormatter(new IndexAxisValueFormatter(x_labels));
            linear_layout.addView(barchart);

            barchart.invalidate();
        }else{
            LineChart lineChart = new LineChart(linear_layout.getContext());
            Map<String, List<Registro>> fechas = Utils.groupByDate(registros);

            List<Entry> entries_gasto = new ArrayList<>();
            List<Entry> entries_ingreso = new ArrayList<>();
            int index = 0;
            String[] x_labels = new String[fechas.keySet().size()];
            for(String key : fechas.keySet()){
                x_labels[index] = key;
                Map<Boolean, List<Registro>> registros_day = Utils.groupByType(fechas.get(key));
                float gastos = Utils.sum(registros_day.get(true));
                float ingresos = Utils.sum(registros_day.get(false));
                entries_gasto.add(new Entry(index, gastos));
                entries_ingreso.add(new Entry(index, ingresos));
                index++;
            }

            LineDataSet dataSet_gasto = new LineDataSet(entries_gasto, "");
            dataSet_gasto.setCircleColor(ContextCompat.getColor(linear_layout.getContext(), R.color.gasto));
            dataSet_gasto.setColor(ContextCompat.getColor(linear_layout.getContext(), R.color.gasto));

            LineDataSet dataSet_ingreso = new LineDataSet(entries_ingreso, "");
            dataSet_ingreso.setColor(ContextCompat.getColor(linear_layout.getContext(), R.color.ingreso));
            dataSet_ingreso.setCircleColor(ContextCompat.getColor(linear_layout.getContext(), R.color.ingreso));

            LineData lineData = new LineData();
            lineData.addDataSet(dataSet_gasto);
            lineData.addDataSet(dataSet_ingreso);

            lineChart.setData(lineData);
            lineChart.setMinimumHeight(646);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(x_labels));

            YAxis rightYAxis = lineChart.getAxisRight();
            rightYAxis.setEnabled(false);
            linear_layout.addView(lineChart);

            lineChart.invalidate();
        }
    }

    /**
     * Carga un Pie Chart con los porcentajes de cantidades totales de cada tipo
     * de la coleccion de registros correspondiente
     * @param linear_layout
     * @param registros
     */
    private void load_piechart_porcentajes(LinearLayout linear_layout, List<Registro> registros){
        PieChart pieChart = new PieChart(linear_layout.getContext());
        float total = registros.size();
        Map<Boolean, List<Registro>> tipos = Utils.groupByType(registros);
        float gastos = tipos.get(true).size() / total;
        float ingresos = tipos.get(false).size() / total;

        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(gastos, getResources().getString(R.string.spend)));
        pieEntries.add(new PieEntry(ingresos, getResources().getString(R.string.income)));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(
                ContextCompat.getColor(linear_layout.getContext(), R.color.gasto),
                ContextCompat.getColor(linear_layout.getContext(), R.color.ingreso)
        );

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setMinimumHeight(646);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelColor(ContextCompat.getColor(linear_layout.getContext(), R.color.black));
        linear_layout.addView(pieChart);

        pieChart.invalidate();
    }

    /**
     * Carga un grafico de puntos con el balance de cada entrada si NO se usa el filtro de dia
     * @param linear_layout
     * @param registros
     */
    private void  load_linechart_balance(LinearLayout linear_layout, List<Registro> registros){
        if(this.mode != 0) {
            LineChart lineChart = new LineChart(linear_layout.getContext());
            Map<String, List<Registro>> fechas = Utils.groupByDate(registros);

            List<Entry> entries = new ArrayList<>();
            int index = 0;
            String[] x_labels = new String[fechas.keySet().size()];
            for(String key : fechas.keySet()){
                x_labels[index] = key;
                Map<Boolean, List<Registro>> registros_day = Utils.groupByType(fechas.get(key));
                float gastos = Utils.sum(registros_day.get(true));
                float ingresos = Utils.sum(registros_day.get(false));
                entries.add(new Entry(index, ingresos-gastos));
                index++;
            }

            LineDataSet dataSet = new LineDataSet(entries, getString(R.string.balance));
            dataSet.setCircleColor(ContextCompat.getColor(linear_layout.getContext(), R.color.balance));
            dataSet.setColor(ContextCompat.getColor(linear_layout.getContext(), R.color.balance));


            LineData lineData = new LineData(dataSet);

            lineChart.setData(lineData);
            lineChart.setMinimumWidth(linear_layout.getWidth());
            lineChart.setMinimumHeight(646);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(x_labels));

            YAxis rightYAxis = lineChart.getAxisRight();
            rightYAxis.setEnabled(false);
            linear_layout.addView(lineChart);

            lineChart.invalidate();
        }

    }
}