package br.com.fastertunnel.pokeloc.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.fastertunnel.pokeloc.R;
import br.com.fastertunnel.pokeloc.models.PokemonBean;
import br.com.fastertunnel.pokeloc.utils.DataManager;
import br.com.fastertunnel.pokeloc.utils.ImageManager;

/**
 * Created by cassioisquierdo on 7/31/16.
 */
public class NotificationsAdapter extends ArrayAdapter<PokemonBean> {

    LayoutInflater mInflater;

    public NotificationsAdapter(Context context, int resource, List<PokemonBean> objects) {
        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
        loadSelectedPokemons();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NotificationsViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.notification_list_item_view, parent, false);
            holder = new NotificationsViewHolder(convertView);
        } else holder = (NotificationsViewHolder) convertView.getTag();

        final PokemonBean pokemonBean = getItem(position);

        holder.icon.setImageResource(ImageManager.getImage(pokemonBean.getId()));
        holder.name.setText(pokemonBean.getName());
        holder.checkBox.setChecked(pokemonBean.isWanted());

        return convertView;
    }

    public void saveSelectedPokemons() {
        List<String> pokemonIds = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            PokemonBean pokemonBean = getItem(i);
            if(pokemonBean.isWanted()) {
                pokemonIds.add(pokemonBean.getId());
            }
        }

        DataManager.storeWantedPokemons(getContext(), pokemonIds);
    }

    public void loadSelectedPokemons() {
        List<String> wantedPokemons = DataManager.retrieveWantedPokemons(getContext());
        for (int i=0; i < getCount(); i++) {
            PokemonBean pokemonBean = getItem(i);
            if(wantedPokemons.contains(pokemonBean.getId()))
                pokemonBean.setWanted(true);
            else pokemonBean.setWanted(false);
        }
        notifyDataSetChanged();
    }

    public void selectAllPokemon(boolean selected){
        for (int i = 0; i < getCount(); i++) {
            PokemonBean pokemonBean = getItem(i);
            pokemonBean.setWanted(selected);
        }

        saveSelectedPokemons();
        loadSelectedPokemons();
    }

    static class NotificationsViewHolder {

        ImageView icon;
        TextView name;
        AppCompatCheckBox checkBox;

        public NotificationsViewHolder(View view) {
            icon = (ImageView) view.findViewById(R.id.pokemon_icon_image_view);
            name = (TextView) view.findViewById(R.id.pokemon_name_text_view);
            checkBox = (AppCompatCheckBox) view.findViewById(R.id.pokemon_checkbox);
            view.setTag(this);
        }
    }
}
