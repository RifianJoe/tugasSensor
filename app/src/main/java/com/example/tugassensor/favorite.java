package com.example.tugassensor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.tugassensor.ModelResults;
import com.example.tugassensor.databinding.ActivityFavoriteBinding;
//import com.example.tugassensor.favorite
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import im.delight.android.location.SimpleLocation;

/*
NIM : 10120045
Nama : Fifit Siti Nurfitriyati Basyari
Kelas : IF-2
 */

public class favorite extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityFavoriteBinding binding;
    int REQ_PERMISSION = 100;
    double strCurrentLatitude;
    double strCurrentLongitude;
    String strCurrentLocation, strKeyword;
    GoogleMap mapsView;
    SimpleLocation simpleLocation;
    ProgressDialog progressDialog;
    MainViewModel mainViewModel;
    SupportMapFragment supportMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //set transparent statusbar
        setStatusBar();

        //show permission
        setPermission();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Tunggu…");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("sedang menampilkan lokasi");

        simpleLocation = new SimpleLocation(this);

        if (!simpleLocation.hasLocationEnabled()) {
            SimpleLocation.openSettings(this);
        }

        //get location
        strCurrentLatitude = simpleLocation.getLatitude();
        strCurrentLongitude = simpleLocation.getLongitude();

        //set location lat long
        strCurrentLocation = strCurrentLatitude + "," + strCurrentLongitude;

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(binding.mapFragment.getId());
        supportMapFragment.getMapAsync(this);

        binding.imageClear.setOnClickListener(view -> {
            binding.searchLocation.getText().clear();
            binding.imageClear.setVisibility(View.GONE);
            mapsView.clear();
        });

        //search location
        binding.searchLocation.setOnEditorActionListener((v, actionId, event) -> {
            strKeyword = binding.searchLocation.getText().toString();
            if (strKeyword.isEmpty()) {
                Toast.makeText(favorite.this, "Form tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            } else {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    progressDialog.show();
                    mainViewModel.setMarkerLocation(strCurrentLocation, strKeyword);
                    InputMethodManager inputMethodManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    binding.imageClear.setVisibility(View.VISIBLE);
                    return true;
                }
            }
            return false;
        });
        setContentView(R.layout.activity_favorite);
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNav);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;

                //Menantukan halaman Fragment yang akan tampil
                int itemId = item.getItemId();
                if (itemId == R.id.profileMenu) {
                    Intent intent1 = new Intent(favorite.this, MainActivity.class);
                    startActivity(intent1);
                } else if (itemId == R.id.favMenu) {
                    Intent intent2 = new Intent(favorite.this, favorite.class);
                    startActivity(intent2);
                } else if (itemId == R.id.locMenu) {
                    Intent intent3 = new Intent(favorite.this, location.class);
                    startActivity(intent3);
                } else if (itemId == R.id.infoMenu) {
                    Intent intent4 = new Intent(favorite.this, info.class);
                    startActivity(intent4);
                }
                return true;
            }
        });
    }
    private void setPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
        }
    }

    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PERMISSION && resultCode == RESULT_OK) {

            //load viewmodel
            getLocationViewModel(strKeyword);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.mapsView = googleMap;

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(strCurrentLatitude, strCurrentLongitude, 1);
            if (addressList != null && addressList.size() > 0) {
                String strCurrentLocation = addressList.get(0).getSubLocality();
                String strAddress = addressList.get(0).getAddressLine(0);
                binding.tvCurrentLocation.setText(strCurrentLocation);
                binding.tvAddress.setText(strAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //viewmodel
        getLocationViewModel(strKeyword);
    }

    private void getLocationViewModel(String Keyword) {
        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.getMarkerLocation().observe(this, modelResults -> {
            if (modelResults.size() != 0) {

                //get multiple marker
                getMarker(modelResults);
                progressDialog.dismiss();
            } else {
                Toast.makeText(this, "Oops, tidak bisa mendapatkan lokasi kamu!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getMarker(ArrayList<ModelResults> modelResultsArrayList) {
        for (int i = 0; i < modelResultsArrayList.size(); i++) {

            //set LatLong from API
            LatLng latLngMarker = new LatLng((
                    modelResultsArrayList.get(i)
                            .getModelGeometry()
                            .getModelLocation()
                            .getLat()), (
                    modelResultsArrayList.get(i)
                            .getModelGeometry()
                            .getModelLocation()
                            .getLng()));

            //get LatLong to Marker
            mapsView.addMarker(new MarkerOptions()
                    .position(latLngMarker)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title(modelResultsArrayList.get(i).getName()));

            //show Marker
            LatLng latLngResult = new LatLng((
                    modelResultsArrayList.get(0).getModelGeometry()
                            .getModelLocation()
                            .getLat()), (
                    modelResultsArrayList.get(0)
                            .getModelGeometry()
                            .getModelLocation()
                            .getLng()));

            //set position marker
            mapsView.moveCamera(CameraUpdateFactory.newLatLng(latLngResult));
            mapsView.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLngResult.latitude, latLngResult.longitude), 14));
            mapsView.getUiSettings().setAllGesturesEnabled(true);
            mapsView.getUiSettings().setZoomGesturesEnabled(true);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (on) {
            layoutParams.flags |= bits;
        } else {
            layoutParams.flags &= ~bits;
        }
        window.setAttributes(layoutParams);
    }
}