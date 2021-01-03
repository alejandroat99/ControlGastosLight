package com.example.controlgastoslight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_view);
        NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.host_fragment);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment, R.id.gruposFragment, R.id.estadisticasFragment
        ).build();

        NavigationUI.setupActionBarWithNavController(this, hostFragment.getNavController(), appBarConfiguration);

        NavigationUI.setupWithNavController(bottomNavigationView, hostFragment.getNavController());

    }
}