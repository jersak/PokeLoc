package com.apps.jersak.pokeloc.services;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
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
public class MainService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        executeSearch();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    private void executeSearch() {

        Log.e(MainService.class.getSimpleName(), "executeSearch()");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                PokemonGo go = PokeManager.getInstance().getPokemonGo();

                if (go == null){
                    executeSearch();
                    return;
                }

                new SearchNearbyPokemonTask(new SearchNearbyPokemonTask.SearchNearbyCallback() {
                    @Override
                    public void onSearchCompleted(List<PokemonBean> pokemons) {

                        DataManager.storePokemon(getApplicationContext(),pokemons);
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
}
