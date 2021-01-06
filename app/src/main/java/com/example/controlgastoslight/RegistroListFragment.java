package com.example.controlgastoslight;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.controlgastoslight.adapter.RegistroListAdapter;
import com.example.controlgastoslight.db.model.Registro;
import com.example.controlgastoslight.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistroListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistroListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_MODE = "param1";

    // TODO: Rename and change types of parameters
    private int mode;

    public RegistroListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mode mode used.
     * @return A new instance of fragment RegistroListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistroListFragment newInstance(int mode) {
        RegistroListFragment fragment = new RegistroListFragment();
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
        return inflater.inflate(R.layout.fragment_registro_list, container, false);
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
        registros = registros == null ? new ArrayList<>() : registros;

        // Cargando fragment...
        ListView list = (ListView) view.findViewById(R.id.list_registry);

        try {
            Log.d("RLFragment", registros.size() + " registros");
            RegistroListAdapter adapter = new RegistroListAdapter((ArrayList) registros, getContext());
            list.setAdapter(adapter);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}