package com.telefonica.movistarhome.bichojuego.feature;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeActivity extends Activity {

    private static final String LOG_TAG =
            WelcomeActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
    }

    public void launchMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        Typeface typeface = ResourcesCompat.getFont(this, R.font.press_start_2p);

        TextView welcomeText = findViewById(R.id.welcome);
        welcomeText.setTypeface(typeface);

        Button button = findViewById(R.id.button);
        button.setTypeface(typeface);
    }
}
