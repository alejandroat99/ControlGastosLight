package com.example.controlgastoslight;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.controlgastoslight.adapter.HomePageAdapter;
import com.example.controlgastoslight.db.actions.RegistroActions;
import com.example.controlgastoslight.db.viewModels.RegistroViewModel;
import com.example.controlgastoslight.utils.MyViewPager;
import com.example.controlgastoslight.utils.ProgressBarAnimation;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Starts here
    TabLayout tabLayout;
    MyViewPager viewPager;
    TabItem tabToday, tabWeek, tabMonth, tabYear;
    ImageButton btnNewEntry;
    TextView tVIncomes, tVExpenses;
    RegistroViewModel registroViewModel;
    ProgressBar pGIncome, pGExpenses;

    HomePageAdapter homePageAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        associateElements(view);

        // Calculating Balance
        refresh(view);

        // Configuring "New Entry" button
        btnNewEntry.setOnClickListener(v -> startActivity(new Intent(getContext(), NewEntryActivity.class)));

        // Getting View Pager ready
        homePageAdapter = new HomePageAdapter(getChildFragmentManager());
        viewPager.setAdapter(homePageAdapter);

        configureTLListener();
        configureVPListener();
    }

    private void configureVPListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabLayout.setScrollPosition(position, positionOffset, true);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void configureTLListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void associateElements(@NonNull View view) {
        tabLayout = view.findViewById(R.id.tLPeriodMode);
        viewPager = view.findViewById(R.id.vPListRegistry);
        tabToday = view.findViewById(R.id.tabToday);
        tabWeek = view.findViewById(R.id.tabWeek);
        tabMonth = view.findViewById(R.id.tabMonth);
        tabYear = view.findViewById(R.id.tabYear);
        btnNewEntry = view.findViewById(R.id.btNewEntry);
        tVIncomes = view.findViewById(R.id.tVIncomes);
        tVExpenses = view.findViewById(R.id.tVExpenses);
        pGIncome = view.findViewById(R.id.pGIncome);
        pGExpenses = view.findViewById(R.id.pGLosses);
    }

    private void refresh(View view) {
        double[] balance;
        double total;
        registroViewModel = new ViewModelProvider(this).get(RegistroViewModel.class);

        // Setting progress bars
        balance = registroViewModel.getBalance();
        total = balance[0] + balance[1];


        tVIncomes.setText(String.format("%.2f€", balance[0]));
        tVExpenses.setText(String.format("%.2f€", balance[1]));

        // W/ Animations
        makeAnimations(balance, total);

        // Register number
        registerNumber(view);
    }

    private void registerNumber(View view) {
        TextView text_test_registros = (TextView) view.findViewById(R.id.text_test_registros);
        RegistroActions ra = new RegistroActions(view.getContext());
        try {
            int size = ra.getAllRegistro().size();
            text_test_registros.setText("Registros: " + size);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void makeAnimations(double[] balance, double total) {
        ProgressBarAnimation pGAIncomes = new ProgressBarAnimation(pGIncome, 1200);
        ProgressBarAnimation pGAExpenses = new ProgressBarAnimation(pGExpenses, 1200);
        pGAIncomes.setProgressAndRotation(( (int) ((balance[0]/total) * 100)),180);
        pGAExpenses.setProgressAndRotation(( (int) ((balance[1]/total) * 100)),(float) (360*balance[0]/total)+180);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager.setAdapter(homePageAdapter);
        refresh(getView());

        double[] balance = registroViewModel.getBalance();
        double total = balance[0] + balance[1];
        makeAnimations(balance, total);
    }
}