package com.example.controlgastoslight.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.controlgastoslight.R;
import com.example.controlgastoslight.db.actions.GrupoActions;
import com.example.controlgastoslight.db.actions.RegistroGrupoCrossRefActions;
import com.example.controlgastoslight.db.model.Grupo;
import com.example.controlgastoslight.db.model.Registro;
import com.example.controlgastoslight.db.model.RegistroGrupoCrossRef;
import com.example.controlgastoslight.utils.SingletonMap;
import com.maltaisn.icondialog.data.Icon;
import com.maltaisn.icondialog.pack.IconPack;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static androidx.core.content.res.ResourcesCompat.*;

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
        Log.d("RLAdapt", "gogo");
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
                } else {
                    viewIcon.setBackground(getDrawable(context.getResources(), R.drawable.ic_launcher, null));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return itemView;
    }
}
