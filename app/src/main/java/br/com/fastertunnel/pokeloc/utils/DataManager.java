package br.com.fastertunnel.pokeloc.utils;

import android.content.Context;
import android.content.SharedPreferences;

import br.com.fastertunnel.pokeloc.models.PokemonBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Fuzi on 30/07/2016
 */
public class DataManager {

    private static final String FILE_NAME = "PokeData";
    private static final String POKEMON_LIST = "PokeList";
    private static final String KEY_PTC_LOGIN = "ptc_login";
    private static final String KEY_PTC_PASSWORD = "ptc_password";

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

    public static boolean storeUsernameString(Context context, String username) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PTC_LOGIN, username);
        return editor.commit();
    }

    public static String retrieveUsername(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PTC_LOGIN, "");
    }

    public static boolean storePasswordString(Context context, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PTC_PASSWORD, password);
        return editor.commit();
    }

    public static String retrievePassword(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PTC_PASSWORD, "");
    }
}
