package br.com.fastertunnel.radarpokemon.services;


import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.pokegoapi.api.PokemonGo;

import java.util.ArrayList;
import java.util.List;

import br.com.fastertunnel.radarpokemon.R;
import br.com.fastertunnel.radarpokemon.async.LoginTask;
import br.com.fastertunnel.radarpokemon.async.SearchNearbyPokemonTask;
import br.com.fastertunnel.radarpokemon.manager.PokeManager;
import br.com.fastertunnel.radarpokemon.models.LoginData;
import br.com.fastertunnel.radarpokemon.models.PokemonBean;
import br.com.fastertunnel.radarpokemon.ui.MapsActivity;
import br.com.fastertunnel.radarpokemon.utils.Constants;
import br.com.fastertunnel.radarpokemon.utils.DataManager;

/**
 * Created by Fuzi on 28/07/2016
 */
public class MainService extends Service implements LocationListener {

    private static final int GPS_UPDATE_INTERVAL = 15000;
    private static final float GPS_UPDATE_MIN_DISTANCE = 25;

    private Location location;

    List<Long> encounterIds = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        executeSearch();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        configGpsListener();
        loginUser();
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
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                PokemonGo go = PokeManager.getInstance().getPokemonGo();

                if (go == null) {
                    Log.e(MainService.class.getSimpleName(), "User not logged in.");
                    loginUser();
                    executeSearch();
                    return;
                }

                if (location == null) {
                    executeSearch();
                    return;
                }

                new SearchNearbyPokemonTask(new SearchNearbyPokemonTask.SearchNearbyCallback() {
                    @Override
                    public void onSearchCompleted(List<PokemonBean> pokemons) {

                        if (pokemons != null){
                            DataManager.storePokemon(getApplicationContext(), pokemons);
                            sendBroadcast(new Intent(Constants.ON_NEARBY_POKEMON_LIST_UPDATED));
                            checkWantedPokemon(pokemons);
                        }

                        executeSearch();

                    }
                }).execute(location);
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

    private void loginUser() {
        String username = DataManager.retrieveUsername(getApplicationContext());
        String password = DataManager.retrievePassword(getApplicationContext());

        if (!username.isEmpty() && !password.isEmpty()) {
            LoginData loginData = new LoginData(username, password);

            new LoginTask(new LoginTask.LoginCallback() {
                @Override
                public void onLoginSuccess() {
                    sendBroadcast(new Intent(Constants.ON_SERVICE_LOGIN_RESULT));
                }

                @Override
                public void onLoginFailed() {
                    sendBroadcast(new Intent(Constants.ON_SERVICE_LOGIN_RESULT));
                }
            }).execute(loginData);
        }
    }

    private void checkWantedPokemon(List<PokemonBean> nearbyPokemons) {
        List<String> wantedPokemonIds = DataManager.retrieveWantedPokemons(getApplicationContext());

        if (wantedPokemonIds == null || nearbyPokemons == null)
            return;

        if (encounterIds.size() > 250){
            encounterIds.clear();
        }

        for (PokemonBean pokemon : nearbyPokemons) {
            if (wantedPokemonIds.contains(pokemon.getId()) && !encounterIds.contains(pokemon.getEncounterId()) ) {
                encounterIds.add(pokemon.getEncounterId());
                showNotification();
                break;
            }
        }
    }

    private void showNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle(getString(R.string.pokemon_found))
                .setContentText(getString(R.string.pokemon_fround_text));

        Intent resultIntent = new Intent(this, MapsActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

        mBuilder.setVibrate(new long[]{1000, 1000});
        mBuilder.setLights(Color.RED, 3000, 3000);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(0, mBuilder.build());
    }
}
