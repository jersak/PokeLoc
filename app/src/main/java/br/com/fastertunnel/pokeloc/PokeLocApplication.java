package br.com.fastertunnel.pokeloc;

import android.app.Application;
import android.support.multidex.MultiDex;

/**
 * Created by Fuzi on 30/07/2016.
 */
public class PokeLocApplication extends Application {

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
    }
}
