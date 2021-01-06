package com.example.controlgastoslight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.example.controlgastoslight.R;
import com.example.controlgastoslight.db.database.DataBase;
import com.example.controlgastoslight.db.model.Meta;
import com.example.controlgastoslight.db.model.Registro;
import com.example.controlgastoslight.utils.SingletonMap;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

        List<Registro> registros;
        DataBase db = (DataBase) SingletonMap.getSingletonMap("db");
        // Ejemplo today
        int year = Calendar.getInstance().get(Calendar.YEAR);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, meta.getNumber());
        calendar.set(Calendar.YEAR, year);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        String key = String.format("%d/%d/%d", day,month,year);


        // FIN Ejemplo
        switch (time){
            case 0:
            case 1:
            case 2:
            case 3:
        }

        // Configurar la vista de cada objeto de la lista
        return view;
    }
}
