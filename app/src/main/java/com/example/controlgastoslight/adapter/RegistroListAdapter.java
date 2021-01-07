package com.example.controlgastoslight.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.controlgastoslight.NewEntryActivity;
import com.example.controlgastoslight.R;
import com.example.controlgastoslight.db.actions.GrupoActions;
import com.example.controlgastoslight.db.actions.RegistroActions;
import com.example.controlgastoslight.db.actions.RegistroGrupoCrossRefActions;
import com.example.controlgastoslight.db.model.Grupo;
import com.example.controlgastoslight.db.model.Registro;
import com.example.controlgastoslight.db.model.RegistroGrupoCrossRef;
import com.example.controlgastoslight.utils.SingletonMap;
import com.maltaisn.icondialog.data.Icon;
import com.maltaisn.icondialog.pack.IconPack;

import java.util.ArrayList;
import java.util.List;

public class RegistroListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Registro> list = new ArrayList<>();
    private Context context;

    public RegistroListAdapter(ArrayList<Registro> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getRegistroId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Basic data
        View itemView = convertView;
        Registro register = list.get(position);

        if(itemView == null) { // Assigning item view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.layout_registro_list_item, null);
        }

        // Title
        TextView tVTitle = itemView.findViewById(R.id.title_registry);
        tVTitle.setText(register.getTitulo());

        // Quantity
        TextView tVQuantity = itemView.findViewById(R.id.quantity_registry);
        int valueColor = register.isGasto() ? R.color.p_rojo : R.color.p_verde;
        tVQuantity.setTextColor(ContextCompat.getColor(context, valueColor));
        tVQuantity.setText(String.format("%.2f€", register.getValue()));

        // Image
        setImage(itemView, register);

        // Build Delete button
        buildDeleteButton(position, itemView);

        // Build Edit button
        ImageButton editBtn = itemView.findViewById(R.id.btn_info_registry);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bundle
                Bundle data = new Bundle();

                // We are editing
                data.putBoolean("edit", true);
                data.putInt("regId", register.getRegistroId());

                // Creating indent
                Intent intent = new Intent(context, NewEntryActivity.class);
                intent.putExtras(data);

                // Go
                context.startActivity(intent);
            }
        });

        return itemView;
    }

    private void buildDeleteButton(int position, View itemView) {
        ImageButton deleteButton = itemView.findViewById(R.id.btn_delete_registry);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(R.string.app_name);
                Registro r = (Registro) getItem(position);
                String p1 = context.getResources().getString(R.string.borrado_registro) + " \"";
                String p2 = "\"?";
                builder.setMessage(p1+ r.getTitulo() + p2);
                builder.setIcon(R.drawable.ic_delete_v2);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RegistroActions ra = new RegistroActions(context);
                        try {
                            ra.delete(r.getRegistroId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                builder.show();
            }
        });
    }

    private void setImage(View itemView, Registro register) {
        ImageView viewIcon = itemView.findViewById(R.id.icon_view_registry);

        // Getting group...
        RegistroGrupoCrossRefActions registroGrupoCrossRefActions = new RegistroGrupoCrossRefActions(context);
        GrupoActions grupoActions = new GrupoActions(context);

        List<RegistroGrupoCrossRef> listRel = registroGrupoCrossRefActions.getRelacionByRegistro(register.getRegistroId());
        if(listRel.size() > 0) { // If a registry belong to a group
            int groupId = listRel.get(0).getGrupoId();
            try {
                Grupo group = grupoActions.getGrupo(groupId);
                int icono_id = group.getIcono();
                int color = group.getColor();

                if(icono_id != -1){
                    IconPack iconPack = (IconPack) SingletonMap.getSingletonMap("iconPack");
                    Icon icon = iconPack.getIcon(icono_id);
                    Drawable icono_image = icon.getDrawable();

                    if(color != -1){
                        icono_image.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    }

                    viewIcon.setBackground(icono_image);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
