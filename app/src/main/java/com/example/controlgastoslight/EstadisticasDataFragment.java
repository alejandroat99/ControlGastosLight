package com.example.controlgastoslight;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        switch (this.mode){
            case 0: text_test.setText("Modo TODAY");
                    break;
            case 1: text_test.setText("Modo WEEK");
                    break;
            case 2: text_test.setText("Modo MONTH");
                    break;
            case 3: text_test.setText("Modo YEAR");
                    break;
            default: text_test.setText("ERROR");
                    break;
        }
    }
}