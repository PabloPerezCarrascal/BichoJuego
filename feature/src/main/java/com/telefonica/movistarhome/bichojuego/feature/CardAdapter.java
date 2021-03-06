package com.telefonica.movistarhome.bichojuego.feature;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
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

  //      holder.text.setText(card.text);
        holder.character.setText(card.character);
//        holder.left.setText(card.left);
//        holder.right.setText(card.right);
        Typeface typeface = ResourcesCompat.getFont(this.getContext(), R.font.press_start_2p);
//        holder.left.setTypeface(typeface);
//        holder.right.setTypeface(typeface);
        holder.character.setTypeface(typeface);
   //   Glide.with(getContext()).load(card.image).into(holder.image);
        int id = getContext().getResources().getIdentifier(card.image, "drawable", getContext().getPackageName());
        holder.image.setImageResource(id);
        return contentView;
    }

    private static class ViewHolder {
//        public TextView text;
        public TextView character;
//        public TextView left;
//        public TextView right;
        public ImageView image;

        public ViewHolder(View view) {
//            this.text = view.findViewById(R.id.item_text);
            this.character = view.findViewById(R.id.item_character);
//            this.left = view.findViewById(R.id.item_left);
//            this.right = view.findViewById(R.id.item_right);
            this.image = view.findViewById(R.id.item_image);
        }
    }

}

