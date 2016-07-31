package br.com.fastertunnel.pokeloc.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.fastertunnel.pokeloc.R;
import br.com.fastertunnel.pokeloc.models.PokemonBean;
import br.com.fastertunnel.pokeloc.utils.ImageManager;

/**
 * Created by cassioisquierdo on 7/31/16.
 */
public class NotificationsAdapter extends ArrayAdapter<PokemonBean> {

    LayoutInflater mInflater;

    public NotificationsAdapter(Context context, int resource, List<PokemonBean> objects) {
        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NotificationsViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.notification_list_item_view, parent, false);
            holder = new NotificationsViewHolder(convertView);
        } else holder = (NotificationsViewHolder) convertView.getTag();

        PokemonBean pokemonBean = getItem(position);

        holder.icon.setImageResource(ImageManager.getImage(pokemonBean.getId()));
        holder.name.setText(pokemonBean.getName());



        return convertView;
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
