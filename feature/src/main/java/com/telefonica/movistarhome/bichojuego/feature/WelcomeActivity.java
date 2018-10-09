package com.telefonica.movistarhome.bichojuego.feature;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hanks.htextview.base.HTextView;
import com.hanks.htextview.fade.FadeTextView;
import com.hanks.htextview.base.AnimationListener;


public class WelcomeActivity extends Activity {

    int step = 1;
    String[] tuto = {"Te han contratado como directivo en la empresa de Software más loca que nunca has visto.",
            "Distintos personajes te irán proponiendo acciones que debes aceptar o rechazar.",
            "Tus decisiones afectarán a 4 factores de tu empresa que deben mantenerse equilibrados.",
            "Tu intuición y memoria te ayudarán a mantenerte en el puesto."};
    FadeTextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        textView = findViewById(R.id.tutorial_text);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.press_start_2p);
        textView.setTypeface(typeface);

        textView.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(HTextView hTextView) {
                if (step < 3) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textView.animateText(tuto[step]);
                            step++;
                        }
                    }, 3000);
                } else {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }, 3000);
                }
            }
        });

        textView.animateText(getString(R.string.tutorial1));
    }
}
