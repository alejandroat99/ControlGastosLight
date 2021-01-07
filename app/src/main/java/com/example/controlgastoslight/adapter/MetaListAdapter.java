package com.example.controlgastoslight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.controlgastoslight.R;
import com.example.controlgastoslight.db.model.Meta;
import com.example.controlgastoslight.db.model.Registro;
import com.example.controlgastoslight.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MetaListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Meta> metas;
    private Context context;

    public MetaListAdapter(ArrayList<Meta> metas, Context context){
        this.metas = metas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return metas.size();
    }

    @Override
    public Meta getItem(int position) {
        return metas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getMetaId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Meta meta = getItem(position);

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_meta_list_item, null);
        }

        int time = meta.getTime_gap();
        int type = meta.getType();

        List<Registro> registros = null;
        switch (time){
            case 0: // Es una meta diaria
                registros = Utils.getRegistrosToday(view.getContext());
                break;
            case 1: // Es una meta semanal
                registros = Utils.getRegistrosWeek(view.getContext());
                break;
            case 2: // Es una meta mensual
                registros = Utils.getRegistrosMonth(view.getContext());
                break;
            case 3: // Es una meta anual
                registros = Utils.getRegistrosYear(view.getContext());
                break;
        }
        ImageView image_status = (ImageView) view.findViewById(R.id.image_state);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
        progressBar.setMax(100);
        TextView text_progress = (TextView) view.findViewById(R.id.text_progress);
        TextView text_tipo_meta = (TextView) view.findViewById(R.id.text_tipo_meta);
        Map<Boolean, List<Registro>> tipos = Utils.groupByType(registros);
        float progress;
        switch (type){
            case 0: // No gastar mas de x€
                text_tipo_meta.setText(view.getContext().getString(R.string.no_spend));
                float gastos = Utils.sum(tipos.get(true));
                if(gastos > meta.getCantidad()){
                    image_status.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_incomplete));
                }else{
                    image_status.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_complete));
                }
                progress = (gastos / meta.getCantidad()) * 100;
                progressBar.setProgress((int) progress);
                text_progress.setText(String.format("%.2f/%.2f", gastos, meta.getCantidad()));
                break;
            case 1: // Obtener x€ en ingresos
                text_tipo_meta.setText(view.getContext().getString(R.string.save_money));
                float ingresos = Utils.sum(tipos.get(false));
                if(ingresos < meta.getCantidad()){
                    image_status.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_incomplete));
                }else{
                    image_status.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_complete));
                }
                progress = (ingresos / meta.getCantidad()) * 100;
                progressBar.setProgress((int) progress);
                text_progress.setText(String.format("%.2f/%.2f", ingresos, meta.getCantidad()));
                break;
            case 2: // No obtener balance negativo
                float balance = Utils.sum(tipos.get(false)) - Utils.sum(tipos.get(true));
                if(balance < 0){
                    image_status.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_incomplete));
                    progressBar.setProgress(0);
                }else{
                    image_status.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_complete));
                    progressBar.setProgress(100);
                }
                text_progress.setText(String.format("%.2f", balance));
                text_tipo_meta.setText(view.getContext().getString(R.string.positive_balance));
                break;
        }

        return view;
    }
}
