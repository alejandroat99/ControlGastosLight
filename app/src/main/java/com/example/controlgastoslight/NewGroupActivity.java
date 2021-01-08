package com.example.controlgastoslight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.util.StringUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controlgastoslight.db.database.DataBase;
import com.example.controlgastoslight.db.model.Grupo;
import com.example.controlgastoslight.utils.SingletonMap;
import com.maltaisn.icondialog.IconDialog;
import com.maltaisn.icondialog.IconDialogSettings;
import com.maltaisn.icondialog.data.Icon;
import com.maltaisn.icondialog.pack.IconPack;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NewGroupActivity extends AppCompatActivity implements  IconDialog.Callback{
    private static final String ICON_DIALOG_TAG = "icon-dialog";

    private ColorPicker cp;
    private IconDialog iconDialog = null;

    private int selected_color;
    private int icon_id;

    private boolean icon_selected = false;
    private boolean color_selected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        IconDialog dialog = (IconDialog) getSupportFragmentManager().findFragmentByTag(ICON_DIALOG_TAG);
        iconDialog = dialog != null ? dialog :
                IconDialog.newInstance(new IconDialogSettings.Builder().build());

        cp = new ColorPicker(NewGroupActivity.this, 0,0,0);
        cp.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(int color) {
                // Cuando se elige un color
                selected_color = color;
                color_selected = true;
                TextView color_preview = (TextView) findViewById(R.id.color_preview);
                color_preview.setText("                         ");
                color_preview.setBackgroundColor(selected_color);

                if(icon_selected){
                    ImageView icon_preview = (ImageView) findViewById(R.id.icon_preview);
                    icon_preview.getBackground().setColorFilter(selected_color, PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
    }

    public void select_color(View v){
        cp.show();
        cp.enableAutoClose();
    }

    public void select_icon(View v){
        iconDialog.show(getSupportFragmentManager(), getString(R.string.icon));
    }

    public void save_grupo(View v){
        EditText edittext_label = (EditText) findViewById(R.id.edittext_label);
        String label = edittext_label.getText().toString().trim();

        if(!label.isEmpty()){
            Grupo grupo = new Grupo();
            grupo.setLabel(label);

            if(icon_selected){
                grupo.setIcono(icon_id);

                if(color_selected){
                    grupo.setColor(selected_color);
                }
            }else{
                grupo.setIcono(-1);
                grupo.setColor(-1);
            }
            DataBase db = (DataBase) SingletonMap.getSingletonMap("db");
            long id = db.grupoDao().addGrupo(grupo);
            Toast.makeText(v.getContext(), v.getContext().getString(R.string.group_created), Toast.LENGTH_SHORT).show();
            finish();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle(v.getContext().getString(R.string.error));
            builder.setMessage(R.string.error_label_grupo);
            builder.setIcon(R.drawable.ic_error);
            builder.setPositiveButton(v.getContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Nullable
    @Override
    public IconPack getIconDialogIconPack() {
        return (IconPack) SingletonMap.getSingletonMap("iconPack");
    }

    @Override
    public void onIconDialogCancelled() {

    }

    @Override
    public void onIconDialogIconsSelected(@NotNull IconDialog iconDialog, @NotNull List<Icon> icons) {
        Icon selected_icon = icons.get(0);
        icon_id = selected_icon.getId();
        icon_selected = true;

        ImageView icon_preview = (ImageView) findViewById(R.id.icon_preview);
        Drawable icon_image = selected_icon.getDrawable();
        if(color_selected){
            icon_image.setColorFilter(selected_color, PorterDuff.Mode.SRC_ATOP);
        }
        icon_preview.setBackground(icon_image);
    }
}