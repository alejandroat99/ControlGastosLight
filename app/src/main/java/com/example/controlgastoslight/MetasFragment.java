package com.example.controlgastoslight;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.controlgastoslight.db.database.DataBase;
import com.example.controlgastoslight.db.model.Meta;
import com.example.controlgastoslight.utils.SingletonMap;

import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MetasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MetasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MetasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MetasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MetasFragment newInstance(String param1, String param2) {
        MetasFragment fragment = new MetasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_metas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageButton btn_add = (ImageButton) view.findViewById(R.id.btn_add_goal);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), NewMetaActivity.class);
                startActivity(intent);
            }
        });
        DataBase db = (DataBase) SingletonMap.getSingletonMap("db");
        int dia = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        int semana = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        int mes = Calendar.getInstance().get(Calendar.MONTH);
        int year = Calendar.getInstance().get(Calendar.MONTH);

        List<Meta> metas_diarias = db.metaDao().getMetasDiarias(dia, year);
        List<Meta> metas_semanales = db.metaDao().getMetasSemanales(semana, year);
        List<Meta> metas_mensuales = db.metaDao().getMetasMensuales(mes, year);
        List<Meta> metas_anuales = db.metaDao().getMetasAnuales(year);
    }
}