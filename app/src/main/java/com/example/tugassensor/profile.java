package com.example.tugassensor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNav);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;

                //Menantukan halaman Fragment yang akan tampil
                int itemId = item.getItemId();
                if (itemId == R.id.profileMenu) {
                    Intent intent1 = new Intent(profile.this, profile.class);
                    startActivity(intent1);
                } else if (itemId == R.id.favMenu) {
                    Intent intent2 = new Intent(profile.this, favorite.class);
                    startActivity(intent2);
                } else if (itemId == R.id.locMenu) {
                    Intent intent3 = new Intent(profile.this, location.class);
                    startActivity(intent3);
                } else if (itemId == R.id.infoMenu) {
                    Intent intent4 = new Intent(profile.this, info.class);
                    startActivity(intent4);
                }
                return true;
            }
        });
    }
//    public void pindahMenu(View view) {
//        Button btn_pndhMenu = (Button) findViewById(R.id.btnProfile);
//        String pndhMenu = btn_pndhMenu.getText().toString();
//        Log.d("Main", "Nama : " + pndhMenu);
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//    }
}