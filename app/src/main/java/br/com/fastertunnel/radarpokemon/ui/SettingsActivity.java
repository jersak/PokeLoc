package br.com.fastertunnel.radarpokemon.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.pokegoapi.api.PokemonGo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.fastertunnel.radarpokemon.R;
import br.com.fastertunnel.radarpokemon.adapter.NotificationsAdapter;
import br.com.fastertunnel.radarpokemon.manager.PokeManager;
import br.com.fastertunnel.radarpokemon.models.PokemonBean;
import br.com.fastertunnel.radarpokemon.ui.custom.LoginDialog;
import br.com.fastertunnel.radarpokemon.utils.DataManager;

/**
 * Created by cassioisquierdo on 7/31/16
 */
public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView mPokemonsListView;
    NotificationsAdapter mAdapter;
    TextView mLogoutButton;
    TextView mSelectPokemonButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mPokemonsListView = (ListView) findViewById(R.id.notification_list_view);
        mPokemonsListView.setOnItemClickListener(this);
        mAdapter = new NotificationsAdapter(this, 0, getPokemonsList());
        mPokemonsListView.setAdapter(mAdapter);

        mLogoutButton = (TextView) findViewById(R.id.logout_button);
        mSelectPokemonButton = (TextView) findViewById(R.id.select_button);

        mSelectPokemonButton.setOnClickListener(onSelectClicked);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        updateView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else return super.onOptionsItemSelected(item);
    }

    View.OnClickListener onLogoutClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DataManager.storeUsernameString(SettingsActivity.this, "");
            DataManager.storePasswordString(SettingsActivity.this, "");
            PokeManager.getInstance().setPokemonGo(null);
            finish();
        }
    };

    View.OnClickListener onLoginClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LoginDialog loginDialog = new LoginDialog(SettingsActivity.this);
            loginDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    updateView();
                }
            });
            loginDialog.show();
        }
    };

    View.OnClickListener onSelectClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String currentText = mSelectPokemonButton.getText().toString();

            if (currentText.equals(getString(R.string.all))){
                mAdapter.selectAllPokemon(true);
                mSelectPokemonButton.setText(getString(R.string.none));
            } else {
                mAdapter.selectAllPokemon(false);
                mSelectPokemonButton.setText(getString(R.string.all));
            }
        }
    };

    private void updateView() {
        if (isLoggedIn()) {
            mLogoutButton.setOnClickListener(onLogoutClicked);
            mLogoutButton.setText(getString(R.string.logout));
            mPokemonsListView.setVisibility(View.VISIBLE);
            findViewById(R.id.select_notify_container).setVisibility(View.VISIBLE);
            mSelectPokemonButton.setText(getString(R.string.all));
        } else {
            mLogoutButton.setOnClickListener(onLoginClicked);
            mLogoutButton.setText(getString(R.string.login));
            mPokemonsListView.setVisibility(View.GONE);
            findViewById(R.id.select_notify_container).setVisibility(View.GONE);
        }
    }

    private boolean isLoggedIn() {
        PokemonGo go = PokeManager.getInstance().getPokemonGo();
        String username = DataManager.retrieveUsername(this);
        String password = DataManager.retrievePassword(this);

        return !(go == null && username.isEmpty() && password.isEmpty());
    }

    private List<PokemonBean> getPokemonsList() {
        List<PokemonBean> pokemons = new ArrayList<>();

        pokemons.add(new PokemonBean("BULBASAUR", "Bulbasaur"));
        pokemons.add(new PokemonBean("IVYSAUR", "Ivysaur"));
        pokemons.add(new PokemonBean("VENUSAUR", "Venusaur"));
        pokemons.add(new PokemonBean("CHARMANDER", "Charmander"));
        pokemons.add(new PokemonBean("CHARMELEON", "Charmeleon"));
        pokemons.add(new PokemonBean("CHARIZARD", "Charizard"));
        pokemons.add(new PokemonBean("SQUIRTLE", "Squirtle"));
        pokemons.add(new PokemonBean("WARTORTLE", "Wartortle"));
        pokemons.add(new PokemonBean("BLASTOISE", "Blastoise"));
        pokemons.add(new PokemonBean("CATERPIE", "Caterpie"));
        pokemons.add(new PokemonBean("METAPOD", "Metapod"));
        pokemons.add(new PokemonBean("BUTTERFREE", "Butterfree"));
        pokemons.add(new PokemonBean("WEEDLE", "Weedle"));
        pokemons.add(new PokemonBean("KAKUNA", "Kakuna"));
        pokemons.add(new PokemonBean("BEEDRILL", "Beedrill"));
        pokemons.add(new PokemonBean("PIDGEY", "Pidgey"));
        pokemons.add(new PokemonBean("PIDGEOTTO", "Pidgeotto"));
        pokemons.add(new PokemonBean("PIDGEOT", "Pidgeot"));
        pokemons.add(new PokemonBean("RATTATA", "Rattata"));
        pokemons.add(new PokemonBean("RATICATE", "Raticate"));
        pokemons.add(new PokemonBean("SPEAROW", "Spearow"));
        pokemons.add(new PokemonBean("FEAROW", "Fearow"));
        pokemons.add(new PokemonBean("EKANS", "Ekans"));
        pokemons.add(new PokemonBean("ARBOK", "Arbok"));
        pokemons.add(new PokemonBean("PIKACHU", "Pikachu"));
        pokemons.add(new PokemonBean("RAICHU", "Raichu"));
        pokemons.add(new PokemonBean("SANDSHREW", "Sandshrew"));
        pokemons.add(new PokemonBean("SANDSLASH", "Sandslash"));
        pokemons.add(new PokemonBean("NIDORAN_FEMALE", "Nidoran F"));
        pokemons.add(new PokemonBean("NIDORINA", "Nidorina"));
        pokemons.add(new PokemonBean("NIDOQUEEN", "Nidoqueen"));
        pokemons.add(new PokemonBean("NIDORAN_MALE", "Nidoran M"));
        pokemons.add(new PokemonBean("NIDORINO", "Nidorino"));
        pokemons.add(new PokemonBean("NIDOKING", "Nidoking"));
        pokemons.add(new PokemonBean("CLEFAIRY", "Clefairy"));
        pokemons.add(new PokemonBean("CLEFABLE", "Clefable"));
        pokemons.add(new PokemonBean("VULPIX", "Vulpix"));
        pokemons.add(new PokemonBean("NINETALES", "Ninetails"));
        pokemons.add(new PokemonBean("JIGGLYPUFF", "Jigglypuff"));
        pokemons.add(new PokemonBean("WIGGLYTUFF", "Wigglytuff"));
        pokemons.add(new PokemonBean("ZUBAT", "Zubat"));
        pokemons.add(new PokemonBean("GOLBAT", "Golbat"));
        pokemons.add(new PokemonBean("ODDISH", "Oddish"));
        pokemons.add(new PokemonBean("GLOOM", "Gloom"));
        pokemons.add(new PokemonBean("VILEPLUME", "Vileplume"));
        pokemons.add(new PokemonBean("PARAS", "Paras"));
        pokemons.add(new PokemonBean("PARASECT", "Parasect"));
        pokemons.add(new PokemonBean("VENONAT", "Venonat"));
        pokemons.add(new PokemonBean("VENOMOTH", "Venomoth"));
        pokemons.add(new PokemonBean("DIGLETT", "Diglett"));
        pokemons.add(new PokemonBean("DUGTRIO", "Dugtrio"));
        pokemons.add(new PokemonBean("MEOWTH", "Meowth"));
        pokemons.add(new PokemonBean("PERSIAN", "Persian"));
        pokemons.add(new PokemonBean("PSYDUCK", "Psyduck"));
        pokemons.add(new PokemonBean("GOLDUCK", "Golduck"));
        pokemons.add(new PokemonBean("MANKEY", "Mankey"));
        pokemons.add(new PokemonBean("PRIMEAPE", "Primeape"));
        pokemons.add(new PokemonBean("GROWLITHE", "Growlithe"));
        pokemons.add(new PokemonBean("ARCANINE", "Arcanine"));
        pokemons.add(new PokemonBean("POLIWAG", "Poliwag"));
        pokemons.add(new PokemonBean("POLIWHIRL", "Poliwhirl"));
        pokemons.add(new PokemonBean("POLIWRATH", "Poliwrath"));
        pokemons.add(new PokemonBean("ABRA", "Abra"));
        pokemons.add(new PokemonBean("KADABRA", "Kadabra"));
        pokemons.add(new PokemonBean("ALAKAZAM", "Alakazam"));
        pokemons.add(new PokemonBean("MACHOP", "Machop"));
        pokemons.add(new PokemonBean("MACHOKE", "Machoke"));
        pokemons.add(new PokemonBean("MACHAMP", "Machamp"));
        pokemons.add(new PokemonBean("BELLSPROUT", "Bellsprout"));
        pokemons.add(new PokemonBean("WEEPINBELL", "Weepinbell"));
        pokemons.add(new PokemonBean("VICTREEBEL", "Victreebell"));
        pokemons.add(new PokemonBean("TENTACOOL", "Tentacool"));
        pokemons.add(new PokemonBean("TENTACRUEL", "Tentacruel"));
        pokemons.add(new PokemonBean("GEODUDE", "Geodude"));
        pokemons.add(new PokemonBean("GRAVELER", "Graveler"));
        pokemons.add(new PokemonBean("GOLEM", "Golem"));
        pokemons.add(new PokemonBean("PONYTA", "Ponyta"));
        pokemons.add(new PokemonBean("RAPIDASH", "Rapidash"));
        pokemons.add(new PokemonBean("SLOWPOKE", "Slowpoke"));
        pokemons.add(new PokemonBean("SLOWBRO", "Slowbro"));
        pokemons.add(new PokemonBean("MAGNEMITE", "Magnemite"));
        pokemons.add(new PokemonBean("MAGNETON", "Magneton"));
        pokemons.add(new PokemonBean("FARFETCHD", "FarfetchD"));
        pokemons.add(new PokemonBean("DODUO", "Doduo"));
        pokemons.add(new PokemonBean("DODRIO", "Dodrio"));
        pokemons.add(new PokemonBean("SEEL", "Seel"));
        pokemons.add(new PokemonBean("DEWGONG", "Dewgong"));
        pokemons.add(new PokemonBean("GRIMER", "Grimer"));
        pokemons.add(new PokemonBean("MUK", "Muk"));
        pokemons.add(new PokemonBean("SHELLDER", "Shellder"));
        pokemons.add(new PokemonBean("CLOYSTER", "Cloyster"));
        pokemons.add(new PokemonBean("GASTLY", "Gastly"));
        pokemons.add(new PokemonBean("HAUNTER", "Haunter"));
        pokemons.add(new PokemonBean("GENGAR", "Gengar"));
        pokemons.add(new PokemonBean("ONIX", "Onix"));
        pokemons.add(new PokemonBean("DROWZEE", "Drowzee"));
        pokemons.add(new PokemonBean("HYPNO", "Hypno"));
        pokemons.add(new PokemonBean("KRABBY", "Krabby"));
        pokemons.add(new PokemonBean("KINGLER", "Kingler"));
        pokemons.add(new PokemonBean("VOLTORB", "Voltorb"));
        pokemons.add(new PokemonBean("ELECTRODE", "Electrode"));
        pokemons.add(new PokemonBean("EXEGGCUTE", "Exeggcute"));
        pokemons.add(new PokemonBean("EXEGGUTOR", "Exeggutor"));
        pokemons.add(new PokemonBean("CUBONE", "Cubone"));
        pokemons.add(new PokemonBean("MAROWAK", "Marowak"));
        pokemons.add(new PokemonBean("HITMONLEE", "Hitmonlee"));
        pokemons.add(new PokemonBean("HITMONCHAN", "Hitmonchan"));
        pokemons.add(new PokemonBean("LICKITUNG", "Lickitung"));
        pokemons.add(new PokemonBean("KOFFING", "Koffing"));
        pokemons.add(new PokemonBean("WEEZING", "Weezing"));
        pokemons.add(new PokemonBean("RHYHORN", "Rhyhorn"));
        pokemons.add(new PokemonBean("RHYDON", "Rhydon"));
        pokemons.add(new PokemonBean("CHANSEY", "Chansey"));
        pokemons.add(new PokemonBean("TANGELA", "Tangela"));
        pokemons.add(new PokemonBean("KANGASKHAN", "Kangaskhan"));
        pokemons.add(new PokemonBean("HORSEA", "Horsea"));
        pokemons.add(new PokemonBean("SEADRA", "Seadra"));
        pokemons.add(new PokemonBean("GOLDEEN", "Goldeen"));
        pokemons.add(new PokemonBean("SEAKING", "Seaking"));
        pokemons.add(new PokemonBean("STARYU", "Staryu"));
        pokemons.add(new PokemonBean("STARMIE", "Starmie"));
        pokemons.add(new PokemonBean("MRMIME", "MrMime"));
        pokemons.add(new PokemonBean("SCYTHER", "Scyther"));
        pokemons.add(new PokemonBean("JYNX", "Jynx"));
        pokemons.add(new PokemonBean("ELECTABUZZ", "Electabuzz"));
        pokemons.add(new PokemonBean("MAGMAR", "Magmar"));
        pokemons.add(new PokemonBean("PINSIR", "Pinsir"));
        pokemons.add(new PokemonBean("TAUROS", "Tauros"));
        pokemons.add(new PokemonBean("MAGIKARP", "Magicarp"));
        pokemons.add(new PokemonBean("GYARADOS", "Gyarados"));
        pokemons.add(new PokemonBean("LAPRAS", "Lapras"));
        pokemons.add(new PokemonBean("DITTO", "Ditto"));
        pokemons.add(new PokemonBean("EEVEE", "Eevee"));
        pokemons.add(new PokemonBean("VAPOREON", "Vaporeon"));
        pokemons.add(new PokemonBean("JOLTEON", "Jolteon"));
        pokemons.add(new PokemonBean("FLAREON", "Flareon"));
        pokemons.add(new PokemonBean("PORYGON", "Porygon"));
        pokemons.add(new PokemonBean("OMANYTE", "Omanyte"));
        pokemons.add(new PokemonBean("OMASTAR", "Omastar"));
        pokemons.add(new PokemonBean("KABUTO", "Kabuto"));
        pokemons.add(new PokemonBean("KABUTOPS", "Kabutops"));
        pokemons.add(new PokemonBean("AERODACTYL", "Aerodactyl"));
        pokemons.add(new PokemonBean("SNORLAX", "Snorlax"));
        pokemons.add(new PokemonBean("ARTICUNO", "Articuno"));
        pokemons.add(new PokemonBean("ZAPDOS", "Zapdos"));
        pokemons.add(new PokemonBean("MOLTRES", "Moltres"));
        pokemons.add(new PokemonBean("DRATINI", "Dratini"));
        pokemons.add(new PokemonBean("DRAGONAIR", "Dragonair"));
        pokemons.add(new PokemonBean("DRAGONITE", "Dragonite"));
        pokemons.add(new PokemonBean("MEWTWO", "Mewtwo"));
        pokemons.add(new PokemonBean("MEW", "Mew"));

        Collections.sort(pokemons, new CustomComparator());

        return pokemons;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        PokemonBean pokemonBean = mAdapter.getItem(position);
        pokemonBean.setWanted(!pokemonBean.isWanted());
        Log.e(SettingsActivity.class.getSimpleName(), pokemonBean.getName() + ": " + String.valueOf(pokemonBean.isWanted()));
        mAdapter.saveSelectedPokemons();
        mAdapter.notifyDataSetChanged();
    }

    public class CustomComparator implements Comparator<PokemonBean> {
        @Override
        public int compare(PokemonBean p1, PokemonBean p2) {
            return p1.getName().compareTo(p2.getName());
        }
    }
}
