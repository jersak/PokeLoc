package com.apps.jersak.pokeloc.manager;

import com.pokegoapi.api.PokemonGo;

/**
 * Created by Fuzi on 27/07/2016.
 */
public class PokeManager {
    private static PokeManager instance;

    private PokemonGo mPokemonGo;

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
}
