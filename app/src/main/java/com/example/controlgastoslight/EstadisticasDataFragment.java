package com.example.controlgastoslight;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.controlgastoslight.db.actions.RegistroActions;
import com.example.controlgastoslight.db.model.Registro;
import com.example.controlgastoslight.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EstadisticasDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EstadisticasDataFragment extends Fragment {

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
        TextView text_test = (TextView) view.findViewById(R.id.text_test);
        List<Registro> registros = null;
        switch (this.mode){
            case 0: text_test.setText("Modo TODAY");
                    registros = Utils.getRegistrosToday(view.getContext());
                    break;
            case 1: text_test.setText("Modo WEEK");
                    registros = Utils.getRegistrosWeek(view.getContext());
                    break;
            case 2: text_test.setText("Modo MONTH");
                    registros = Utils.getRegistrosMonth(view.getContext());
                    break;
            case 3: text_test.setText("Modo YEAR");
                    registros = Utils.getRegistrosYear(view.getContext());
                    break;
            default: text_test.setText("ERROR");
                    break;
        }

        if(registros != null) {
            ListView list = (ListView) view.findViewById(R.id.list_registros);
            List<String> list_data = new ArrayList<>();
            for (Registro r : registros) {
                list_data.add(r.toString());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, list_data);
            list.setAdapter(adapter);
        }
    }
}