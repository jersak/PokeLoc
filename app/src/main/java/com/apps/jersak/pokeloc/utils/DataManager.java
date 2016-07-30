package com.apps.jersak.pokeloc.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.apps.jersak.pokeloc.models.PokemonBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Fuzi on 30/07/2016.
 */
public class DataManager {

    private static final String FILE_NAME = "PokeData";
    private static final String POKEMON_LIST = "PokeList";

    public static boolean storePokemon(Context context, List<PokemonBean> pokemonList){

        Gson gson = new Gson();
        String pokemons = gson.toJson(pokemonList);

        SharedPreferences sharedPref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(POKEMON_LIST,pokemons);
        return editor.commit();
    }

    public static List<PokemonBean> retrievePokemon(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String pokemons =  sharedPref.getString(POKEMON_LIST,"");
        Gson gson = new Gson();
        return gson.fromJson(pokemons, new TypeToken<List<PokemonBean>>(){}.getType());
    }



}
