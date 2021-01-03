package com.example.controlgastoslight.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.controlgastoslight.GrupoDetailActivity;
import com.example.controlgastoslight.R;
import com.example.controlgastoslight.db.actions.GrupoActions;
import com.example.controlgastoslight.db.model.Grupo;

import java.util.ArrayList;

public class GrupoListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Grupo> list = new ArrayList<>();
    private Context context;

    public GrupoListAdapter(ArrayList<Grupo> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Grupo getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getGrupoId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_grupo_list_item, null);
        }

        TextView list_item_label = (TextView) view.findViewById(R.id.text_item_label);
        list_item_label.setText(list.get(position).getLabel());

        ImageButton deleteBtn = (ImageButton) view.findViewById(R.id.btn_delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(R.string.app_name);
                builder.setMessage("Do you want to delete this group?");
                builder.setIcon(R.drawable.ic_delete);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GrupoActions ga = new GrupoActions(context);
                        System.out.println("Se ha pulsado el objeto:" + position);
                        System.out.println("Grupo ID: " + getItemId(position));
                        System.out.println("Grupo: " + getItem(position));
                        ga.delete(getItem(position));
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        ImageButton detailBtn = (ImageButton) view.findViewById(R.id.btn_info);
        detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (int) getItemId(position);
                Intent intent = new Intent(v.getContext(), GrupoDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("grupoId", id);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });

        return view;
    }
}
