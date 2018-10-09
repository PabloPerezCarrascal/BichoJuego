package com.telefonica.movistarhome.bichojuego.feature;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Utils {

    public static String stripAccentsAndStuff(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        s = s.replaceAll("[^a-zA-Z ]", "");
        return s.toLowerCase();
    }

    public static String loadJSONFromAsset(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open("cards.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        Log.e("data", json);
        return json;

    }


    public static ArrayList<Card> readJSONcards(Context context, int difficulty) {
        String[] difficultyModes = {"easy", "medium", "hard"};
        ArrayList<Card> cards = new ArrayList<Card>();

        try {
            JSONObject cardsJson = new JSONObject(loadJSONFromAsset(context));
            JSONObject cardsObject = cardsJson.getJSONObject("cards");


            JSONArray cardsSubArray = cardsObject.getJSONArray(difficultyModes[difficulty]);
            for (int i = 0; i < cardsSubArray.length(); i++) {
                JSONObject cardJson = cardsSubArray.getJSONObject(i);
                String text = cardJson.getString("text");
                String image = cardJson.getString("image");
                String character = cardJson.getString("character");
                String left = cardJson.getString("left");
                String right = cardJson.getString("right");
                JSONArray arrayLeft = cardJson.optJSONArray("effects_left");
                JSONArray arrayRight = cardJson.optJSONArray("effects_right");
                int[] effects_left = new int[arrayLeft.length()];
                int[] effects_right = new int[arrayRight.length()];
                for (int j = 0; j < arrayLeft.length(); ++j) {
                    effects_left[j] = arrayLeft.optInt(j);
                    effects_right[j] = arrayRight.optInt(j);
                }

                cards.add(new Card(text, left, right, character, image, effects_left, effects_right));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return cards;
    }


    public static List<Card> createCards(Context context) {

        ArrayList<Card> easy_cards;
        ArrayList<Card> medium_cards;
        ArrayList<Card> hard_cards;
        ArrayList<Card> all_cards = new ArrayList<Card>();

        easy_cards = readJSONcards(context, 0);
        medium_cards = readJSONcards(context, 1);
        hard_cards = readJSONcards(context, 2);


        // incremental difficulty
        Collections.shuffle(easy_cards);
        medium_cards.addAll(easy_cards);
        Collections.shuffle(medium_cards);
        hard_cards.addAll(medium_cards);
        Collections.shuffle(hard_cards);

        all_cards.addAll(easy_cards);
        all_cards.addAll(medium_cards);
        all_cards.addAll(hard_cards);

        return all_cards;
    }

    public static Card readCard(Context context, String cardType) {
        try {
            JSONObject cardsJson = new JSONObject(loadJSONFromAsset(context));
            JSONObject cardsObject = cardsJson.getJSONObject("cards");
            JSONObject cardJson = cardsObject.getJSONObject(cardType);

            String text = cardJson.getString("text");
            String image = cardJson.getString("image");
            String character = cardJson.getString("character");
            String left = cardJson.getString("left");
            String right = cardJson.getString("right");
            JSONArray arrayLeft = cardJson.optJSONArray("effects_left");
            JSONArray arrayRight = cardJson.optJSONArray("effects_right");
            int[] effects_left = new int[arrayLeft.length()];
            int[] effects_right = new int[arrayRight.length()];
            for (int j = 0; j < arrayLeft.length(); ++j) {
                effects_left[j] = arrayLeft.optInt(j);
                effects_right[j] = arrayRight.optInt(j);
            }

            return new Card(text, left, right, character, image, effects_left, effects_right);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
