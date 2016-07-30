package com.apps.jersak.pokeloc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.apps.jersak.pokeloc.async.LoginTask;
import com.apps.jersak.pokeloc.async.SearchNearbyPokemonTask;
import com.apps.jersak.pokeloc.manager.PokeManager;
import com.apps.jersak.pokeloc.models.LoginData;
import com.apps.jersak.pokeloc.models.PokemonBean;
import com.apps.jersak.pokeloc.services.MainService;
import com.apps.jersak.pokeloc.utils.Constants;
import com.apps.jersak.pokeloc.utils.DataManager;
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

        registerReceiver(onNearbyUpdatedReceiver,new IntentFilter(Constants.ON_NEARBY_POKEMON_LIST_UPDATED));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onNearbyUpdatedReceiver);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        new LoginTask(this).execute(new LoginData("jersak", "50825x"));

        LatLng location = new LatLng(34.008887136904356,-118.4983366727829);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18));

        populateMap();
    }

    public void populateMap() {

        List<PokemonBean> pokemons = DataManager.retrievePokemon(this);

        if (pokemons == null || mMap == null){
            Log.e(MapsActivity.class.getSimpleName(),"Pokemons or map null");
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

            Log.e(MapsActivity.class.getSimpleName(),"onReceive()");

            populateMap();
        }
    };

    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(this, MainService.class);
        intent.setAction(Constants.START_SEARCH_POKEMON);
        startService(intent);
    }

    @Override
    public void onLoginFailed() {
        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
    }
}
