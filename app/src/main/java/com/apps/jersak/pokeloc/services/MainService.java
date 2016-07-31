package com.apps.jersak.pokeloc.services;


import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.apps.jersak.pokeloc.async.SearchNearbyPokemonTask;
import com.apps.jersak.pokeloc.manager.PokeManager;
import com.apps.jersak.pokeloc.models.PokemonBean;
import com.apps.jersak.pokeloc.utils.Constants;
import com.apps.jersak.pokeloc.utils.DataManager;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;

import java.util.List;

/**
 * Created by Fuzi on 28/07/2016.
 */
public class MainService extends Service implements LocationListener {

    private static final int GPS_UPDATE_INTERVAL = 15000;
    private static final float GPS_UPDATE_MIN_DISTANCE = 25;

    private Location location;

    @Override
    public void onCreate() {
        super.onCreate();
        executeSearch();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        configGpsListener();
        return START_STICKY;
    }

    private void configGpsListener() {
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, GPS_UPDATE_INTERVAL, GPS_UPDATE_MIN_DISTANCE, this);
    }


    private void executeSearch() {

        Log.e(MainService.class.getSimpleName(), "executeSearch()");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                PokemonGo go = PokeManager.getInstance().getPokemonGo();

                if (go == null) {
                    executeSearch();
                    return;
                }

                if (location == null){
                    executeSearch();
                    return;
                }

                new SearchNearbyPokemonTask(new SearchNearbyPokemonTask.SearchNearbyCallback() {
                    @Override
                    public void onSearchCompleted(List<PokemonBean> pokemons) {

                        DataManager.storePokemon(getApplicationContext(), pokemons);
                        sendBroadcast(new Intent(Constants.ON_NEARBY_POKEMON_LIST_UPDATED));

                        executeSearch();

                    }
                }).execute();
            }
        }, 5000);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
