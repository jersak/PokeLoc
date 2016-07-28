package com.apps.jersak.pokeloc.manager;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fuzi on 27/07/2016.
 */
public class PokeManager {
    private static PokeManager instance;

    private PokemonGo mPokemonGo;

    private List<CatchablePokemon> nearbyPokemon = new ArrayList<>();

    public static PokeManager getInstance(){
        if (instance == null){
            instance = new PokeManager();
        }
        return instance;
    }

    private PokeManager() {
    }

    public PokemonGo getPokemonGo() {
        return mPokemonGo;
    }

    public void setPokemonGo(PokemonGo mPokemonGo) {
        this.mPokemonGo = mPokemonGo;
    }

    public List<CatchablePokemon> getNearbyPokemon() {
        return nearbyPokemon;
    }

    public void setNearbyPokemon(List<CatchablePokemon> nearbyPokemon) {
        this.nearbyPokemon = nearbyPokemon;
    }
}
