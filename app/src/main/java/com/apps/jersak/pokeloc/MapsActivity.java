package com.apps.jersak.pokeloc;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.apps.jersak.pokeloc.async.LoginTask;
import com.apps.jersak.pokeloc.async.SearchNearbyPokemonTask;
import com.apps.jersak.pokeloc.models.LoginData;
import com.apps.jersak.pokeloc.utils.ImageManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LoginTask.LoginCallback {

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
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        new LoginTask(this).execute(new LoginData("jersak", "50825x"));

        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(34.008887136904356,-118.4983366727829);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18));
    }

    public void populateMap(List<CatchablePokemon> pokemons) {

        for (Marker m : mPokemonMarkers) {
            m.remove();
        }

        for (CatchablePokemon pokemon : pokemons) {
            LatLng currentPokemon = new LatLng(pokemon.getLatitude(), pokemon.getLongitude());
            String name = pokemon.getPokemonId().name();
            String time = ((pokemon.getExpirationTimestampMs() - System.currentTimeMillis()) / 1000) + "s";
            String title = String.format("%s\n%s", name, time);
            MarkerOptions options = new MarkerOptions()
                    .position(currentPokemon)
                    .title(time)
                    .icon(BitmapDescriptorFactory.fromResource(ImageManager.getImage(name)));
            Marker marker = mMap.addMarker(options);
            marker.showInfoWindow();
            mPokemonMarkers.add(marker);
        }
    }

    private void executeSearch() {

        new SearchNearbyPokemonTask(new SearchNearbyPokemonTask.SearchNearbyCallback() {
            @Override
            public void onSearchCompleted(List<CatchablePokemon> pokemons) {
                if (pokemons == null || pokemons.isEmpty()) {
                    Toast.makeText(MapsActivity.this, "No nearby pokemon", Toast.LENGTH_SHORT).show();
                } else {
                    populateMap(pokemons);
                }

                onLoginSuccess();
            }
        }).execute();

    }

    @Override
    public void onLoginSuccess() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                executeSearch();
            }
        }, 5000);
    }

    @Override
    public void onLoginFailed() {
        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
    }
}
