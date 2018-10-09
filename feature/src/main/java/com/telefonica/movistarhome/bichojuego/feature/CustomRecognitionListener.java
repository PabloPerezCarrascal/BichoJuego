package com.telefonica.movistarhome.bichojuego.feature;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;

import static com.telefonica.movistarhome.bichojuego.feature.Utils.stripAccentsAndStuff;

public class CustomRecognitionListener implements RecognitionListener {

    MainActivity act;
    SpeechRecognizer sp;
    Intent in;
    int status = 0; //0 ready 1 beginning 2 end 3 error 4 results 5 partialresults

    public CustomRecognitionListener(MainActivity act, SpeechRecognizer sp, Intent in) {
        this.act = act;
        this.sp = sp;
        this.in = in;
    }


    @Override
    public void onReadyForSpeech(Bundle bundle) {
        Log.i("SPEECH", "onReadyForSpeech");
        this.status = 0;
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i("SPEECH", "onBeginningOfSpeech");
        this.status = 1;
    }

    @Override
    public void onRmsChanged(float v) {
    }

    @Override
    public void onBufferReceived(byte[] bytes) {
        Log.i("SPEECH", "onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        Log.i("SPEECH", "onEndOfSpeech");
        this.status = 2;
    }

    @Override
    public void onError(int i) {
        Log.i("SPEECH", "onError" + String.valueOf(i));
        this.status = 3;
//        sp.stopListening();
//        sp.startListening(in);
    }

    @Override
    public void onResults(Bundle bundle) {
        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        Log.i("ACTIVITY", String.valueOf(act.cardIndex));
        Log.i("ACTIVITY", act.cards.toString());

        if (matches != null && act.cardIndex < act.cards.size()) {
            for (int i = 0; i < matches.size(); i++) {
                if (stripAccentsAndStuff(matches.get(i)).contains(stripAccentsAndStuff(act.cards.get(act.cardIndex).left))) {
                    Log.i("SPEECH", "SWIPELEFT" + stripAccentsAndStuff(matches.get(i) + stripAccentsAndStuff(act.cards.get(act.cardIndex).left)));
                    act.swipeLeft();
                    break;
                }
                if (stripAccentsAndStuff(matches.get(i)).contains(stripAccentsAndStuff(act.cards.get(act.cardIndex).right))) {
                    Log.i("SPEECH", "SWIPERIGHT" + stripAccentsAndStuff(matches.get(i) + stripAccentsAndStuff(act.cards.get(act.cardIndex).left)));
                    act.swipeRight();
                    break;
                }
            }
        }
        Log.i("SPEECH", "onResults" + matches);
        this.status = 4;
//        sp.stopListening();
//        sp.startListening(in);
    }

    @Override
    public void onPartialResults(Bundle bundle) {
        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//
//        if (matches != null && act.cardIndex < act.cards.size()) {
//            for (int i = 0; i < matches.size(); i++) {
//                if (stripAccentsAndStuff(matches.get(i)).contains(stripAccentsAndStuff(act.cards.get(act.cardIndex).left))) {
//                    Log.i("SPEECH", "SWIPELEFT");
//                    act.swipeLeft();
//                    act.cardIndex++;
//                    break;
//                }
//                if (stripAccentsAndStuff(matches.get(i)).contains(stripAccentsAndStuff(act.cards.get(act.cardIndex).right))) {
//                    Log.i("SPEECH", "SWIPERIGHT");
//                    act.swipeRight();
//                    act.cardIndex++;
//                    break;
//                }
//            }
//        }
        this.status = 5;
        Log.i("SPEECH", "onPartialResults" + matches);

    }

    @Override
    public void onEvent(int i, Bundle bundle) {
        Log.i("SPEECH", "onEvent");

    }
}
