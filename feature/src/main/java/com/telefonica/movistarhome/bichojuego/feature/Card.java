package com.telefonica.movistarhome.bichojuego.feature;

public class Card {


    public String text;
    public String left;
    public String character;
    public String right;
    public String image;
    public int[] effects_left;
    public int[] effects_right;


    public Card(String text, String left, String right, String character , String image, int[] effects_left, int[] effects_right) {
        this.text = text;
        this.left = left;
        this.right = right;
        this.character= character;
        this.image = image;
        this.effects_left = effects_left;
        this.effects_right = effects_right;

    }
}