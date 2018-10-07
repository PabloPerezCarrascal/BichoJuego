package com.telefonica.movistarhome.bichojuego.feature;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class CardAdapter extends ArrayAdapter<Card> {

    public CardAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder;

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            contentView = inflater.inflate(R.layout.card, parent, false);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        Card card = getItem(position);

        holder.text.setText(card.text);
        holder.character.setText(card.character);
        holder.left.setText(card.left);
        holder.right.setText(card.right);

        Glide.with(getContext()).load(card.image).into(holder.image);

        return contentView;
    }

    private static class ViewHolder {
        public TextView text;
        public TextView character;
        public TextView left;
        public TextView right;
        public ImageView image;

        public ViewHolder(View view) {
            this.text = view.findViewById(R.id.item_text);
            this.character = view.findViewById(R.id.item_character);
            this.left = view.findViewById(R.id.item_left);
            this.right = view.findViewById(R.id.item_right);
            this.image = view.findViewById(R.id.item_tourist_spot_card_image);
        }
    }

}

