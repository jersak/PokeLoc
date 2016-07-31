package br.com.fastertunnel.pokeloc.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.fastertunnel.pokeloc.R;
import br.com.fastertunnel.pokeloc.adapter.NotificationsAdapter;
import br.com.fastertunnel.pokeloc.manager.PokeManager;
import br.com.fastertunnel.pokeloc.models.PokemonBean;
import br.com.fastertunnel.pokeloc.utils.DataManager;

/**
 * Created by cassioisquierdo on 7/31/16
 */
public class SettingsActivity extends AppCompatActivity {

    ListView mPokemonsListView;
    NotificationsAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mPokemonsListView = (ListView) findViewById(R.id.notification_list_view);
        mAdapter = new NotificationsAdapter(this, 0, getPokemonsList());
        mPokemonsListView.setAdapter(mAdapter);

        findViewById(R.id.logout_button).setOnClickListener(onLogoutClicked);
    }

    View.OnClickListener onLogoutClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DataManager.storeUsernameString(SettingsActivity.this, "");
            DataManager.storePasswordString(SettingsActivity.this, "");
            PokeManager.getInstance().setPokemonGo(null);
        }
    };

    private List<PokemonBean> getPokemonsList() {
        List<PokemonBean> pokemons = new ArrayList<>();

        pokemons.add(new PokemonBean("ABRA", "Abra"));
        pokemons.add(new PokemonBean("AERODACTYL", "Aerodactyl"));
        pokemons.add(new PokemonBean("ALAKAZAM", "Alakazam"));

        return pokemons;
    }
}
