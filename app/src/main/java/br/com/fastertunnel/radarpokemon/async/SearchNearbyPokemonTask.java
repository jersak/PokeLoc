package br.com.fastertunnel.radarpokemon.async;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import br.com.fastertunnel.radarpokemon.manager.PokeManager;
import br.com.fastertunnel.radarpokemon.models.PokemonBean;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.Map;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fuzi on 27/07/2016.
 */
public class SearchNearbyPokemonTask extends AsyncTask<Location, Void, List<PokemonBean>> {

    public interface SearchNearbyCallback {
        void onSearchCompleted(List<PokemonBean> pokemons);
    }

    SearchNearbyCallback callback;

    public SearchNearbyPokemonTask(SearchNearbyCallback callback) {
        this.callback = callback;
    }


    @Override
    protected List<PokemonBean> doInBackground(Location... locations) {

        if (locations == null || locations.length == 0 || locations[0] == null) {
            return null;
        }

        Location location = locations[0];

        try {
            PokemonGo go = PokeManager.getInstance().getPokemonGo();
            go.setLocation(location.getLatitude(), location.getLongitude(), 0);
            Map map = new Map(go);

            List<CatchablePokemon> nearbyPokemon = map.getCatchablePokemon();

            if (nearbyPokemon == null) {
                Log.e(SearchNearbyPokemonTask.class.getSimpleName(), "nearbyPokemon list is null");
                return null;
            }

            if (nearbyPokemon.isEmpty()) {
                Log.e(SearchNearbyPokemonTask.class.getSimpleName(), "nearbyPokemon list is empty");
            }

            List<PokemonBean> pokemonBeanList = new ArrayList<>();

            for (CatchablePokemon temp : nearbyPokemon) {
                PokemonBean pokemonBean = new PokemonBean(temp.getPokemonId().name(), temp.getExpirationTimestampMs(), temp.getLatitude(), temp.getLongitude(), temp.getEncounterId());
                pokemonBeanList.add(pokemonBean);
            }

            return pokemonBeanList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<PokemonBean> catchablePokemons) {
        super.onPostExecute(catchablePokemons);

        if (callback != null) {
            callback.onSearchCompleted(catchablePokemons);
        }
    }
}
