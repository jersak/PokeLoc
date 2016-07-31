package br.com.fastertunnel.pokeloc;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import br.com.fastertunnel.pokeloc.R;

import br.com.fastertunnel.pokeloc.async.LoginTask;
import br.com.fastertunnel.pokeloc.models.LoginData;
import br.com.fastertunnel.pokeloc.models.PokemonBean;
import br.com.fastertunnel.pokeloc.services.MainService;
import br.com.fastertunnel.pokeloc.utils.Constants;
import br.com.fastertunnel.pokeloc.utils.DataManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LoginTask.LoginCallback {

    private int GPS_PERMISSION_RC = 12;

    private GoogleMap mMap;

    private List<Marker> mPokemonMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mPokemonMarkers = new ArrayList<>();

        registerReceiver(onNearbyUpdatedReceiver, new IntentFilter(Constants.ON_NEARBY_POKEMON_LIST_UPDATED));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onNearbyUpdatedReceiver);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);

        populateMap();

        new LoginTask(this).execute(new LoginData("fastertunnel", "50825x"));

        checkGpsPermission();

    }

    private boolean hasGpsPermission() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private void checkGpsPermission() {
        if (!hasGpsPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, GPS_PERMISSION_RC);
        } else {
            startService(new Intent(this, MainService.class));
            moveToUserLocation();
        }
    }

    private void moveToUserLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (hasGpsPermission()) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (mMap != null && location != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == GPS_PERMISSION_RC) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startService(new Intent(this, MainService.class));
                moveToUserLocation();
            } else {
                Toast.makeText(this, R.string.gps_permission_denied, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void populateMap() {

        List<PokemonBean> pokemons = DataManager.retrievePokemon(this);

        if (pokemons == null || mMap == null) {
            Log.e(MapsActivity.class.getSimpleName(), "Pokemons or map null");
            return;
        }

        for (Marker m : mPokemonMarkers) {
            m.remove();
        }

        for (PokemonBean pokemon : pokemons) {
            LatLng currentPokemon = new LatLng(pokemon.getLatitude(), pokemon.getLongitude());
            String name = pokemon.getId();
            String time = ((pokemon.getTime() - System.currentTimeMillis()) / 1000) + "s";
            String title = String.format("%s\n%s", name, time);
            MarkerOptions options = new MarkerOptions()
                    .position(currentPokemon)
                    .title(time)
                    .icon(pokemon.getImage());
            Marker marker = mMap.addMarker(options);
            marker.showInfoWindow();
            mPokemonMarkers.add(marker);
        }
    }

    BroadcastReceiver onNearbyUpdatedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.e(MapsActivity.class.getSimpleName(), "onReceive()");

            populateMap();
        }
    };

    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(this, MainService.class);
        startService(intent);
    }

    @Override
    public void onLoginFailed() {
        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
    }
}
