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

public class Utils {

    public static String stripAccentsAndStuff(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
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


    public static List<Card> createCards(Context context) {
        try {
            JSONObject cardsJson = new JSONObject(loadJSONFromAsset(context));
            JSONObject cardsObject = cardsJson.getJSONObject("cards");
            String[] dificultyModes = {"easy", "medium", "hard"};
            List<Card> cards = new ArrayList<>();
            for (int d = 0; d < dificultyModes.length; d++) {
                JSONArray cardsSubArray = cardsObject.getJSONArray(dificultyModes[d]);
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
            }
            return cards;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
