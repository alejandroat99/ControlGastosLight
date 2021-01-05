package com.example.controlgastoslight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.controlgastoslight.db.database.DataBase;
import com.example.controlgastoslight.utils.SingletonMap;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.maltaisn.icondialog.pack.IconPack;
import com.maltaisn.icondialog.pack.IconPackLoader;
import com.maltaisn.iconpack.defaultpack.IconPackDefault;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SingletonMap.putSingletonMap("db", DataBase.getInMemoryDatabase(this));

        IconPackLoader loader = new IconPackLoader(this);
        IconPack iconPack = IconPackDefault.createDefaultIconPack(loader);
        iconPack.loadDrawables(loader.getDrawableLoader());

        SingletonMap.putSingletonMap("iconPack", iconPack);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_view);
        NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.host_fragment);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment, R.id.gruposFragment, R.id.estadisticasFragment
        ).build();

        //NavigationUI.setupActionBarWithNavController(this, hostFragment.getNavController(), appBarConfiguration);

        NavigationUI.setupWithNavController(bottomNavigationView, hostFragment.getNavController());

    }
}