package com.apps.jersak.pokeloc.async;

import android.os.AsyncTask;
import android.util.Log;

import com.apps.jersak.pokeloc.manager.PokeManager;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.Map;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fuzi on 27/07/2016.
 */
public class SearchNearbyPokemonTask extends AsyncTask<Void, Void, List<CatchablePokemon>> {

    public interface SearchNearbyCallback {
        void onSearchCompleted(List<CatchablePokemon> pokemons);
    }

    SearchNearbyCallback callback;

    public SearchNearbyPokemonTask(SearchNearbyCallback callback){
        this.callback = callback;
    }


    @Override
    protected List<CatchablePokemon> doInBackground(Void... voids) {

        try {
            PokemonGo go = PokeManager.getInstance().getPokemonGo();

            //System.out.println(go.getPlayerProfile());

            go.setLocation(34.008887136904356,-118.4983366727829, 0);

            Map map = new Map(go);

            List<CatchablePokemon> nearbyPokemon = map.getCatchablePokemon();

            for (CatchablePokemon temp : nearbyPokemon) {
                Log.e(SearchNearbyPokemonTask.class.getSimpleName(), "Pokemon próximo: " + temp.getPokemonId());
                Log.e(SearchNearbyPokemonTask.class.getSimpleName(), "Posição: " + temp.getLatitude() + " : " + temp.getLongitude());
                Log.e(SearchNearbyPokemonTask.class.getSimpleName(), "Disponível por: " + ((temp.getExpirationTimestampMs() - System.currentTimeMillis()) / 1000) + " segundos");
                Log.e(SearchNearbyPokemonTask.class.getSimpleName(), "");
            }

            return nearbyPokemon;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(List<CatchablePokemon> catchablePokemons) {
        super.onPostExecute(catchablePokemons);

        if(callback != null){
            callback.onSearchCompleted(catchablePokemons);
        }
    }
}
