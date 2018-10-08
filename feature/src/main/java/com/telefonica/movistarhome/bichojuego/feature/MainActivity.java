package com.telefonica.movistarhome.bichojuego.feature;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.SwipeDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.telefonica.movistarhome.bichojuego.feature.Utils.readCard;

public class MainActivity extends Activity {

    public int cardIndex = 0;
    private ProgressBar progressBar;
    private CardStackView cardStackView;
    private CardAdapter adapter;
    List<Card> cards = new ArrayList<>();
    private TextView cardText;
    private TextView scoreText;
    private ImageView cardImage;
    private CardStackView cardStack;
    private int[] indicators = {10, 10, 10, 10};
    private String[] indicatorNames = {"hype", "crew", "software", "money"};
    private boolean gameOver = false;
    private ImageView[] indicatorIcons = new ImageView[4];
    private TextView leftText;
    private TextView rightText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardText = findViewById(R.id.card_text);
        scoreText = findViewById(R.id.score);
        cardImage = findViewById(R.id.item_image);
        indicatorIcons[0] = findViewById(R.id.icon_hype);
        indicatorIcons[1] = findViewById(R.id.icon_crew);
        indicatorIcons[2] = findViewById(R.id.icon_software);
        indicatorIcons[3] = findViewById(R.id.icon_money);
        leftText = findViewById(R.id.item_left);
        rightText = findViewById(R.id.item_right);

        Typeface typeface = ResourcesCompat.getFont(this, R.font.press_start_2p);
        cardText.setTypeface(typeface);
        scoreText.setTypeface(typeface);
        leftText.setTypeface(typeface);
        rightText.setTypeface(typeface);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);

//        MediaPlayer mediaPlayer = new MediaPlayer();
//        AssetFileDescriptor afd = this.getApplicationContext().getResources().openRawResourceFd(R.raw.cancion);
//
//        mediaPlayer.reset();
//        //mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
//
//        try {
//            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
//            mediaPlayer.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        mediaPlayer.start();


        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Activity act = this;
        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES");

        AudioManager audio = (AudioManager) act.getSystemService(Context.AUDIO_SERVICE);
        //audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        //audio.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

        mSpeechRecognizer.setRecognitionListener(new CustomRecognitionListener(this, mSpeechRecognizer, mSpeechRecognizerIntent));
//        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);

        setup();
        reload();
    }


    private CardAdapter createCardCardAdapter() {
        final CardAdapter adapter = new CardAdapter(getApplicationContext());
        if (!gameOver) {
            cards = Utils.createCards(getApplicationContext());
        }
        cardText.setText(cards.get(0).text);
        leftText.setText(cards.get(0).left);
        rightText.setText(cards.get(0).right);
        adapter.addAll(cards);
        return adapter;
    }

    private void setup() {
        progressBar = findViewById(R.id.activity_main_progress_bar);
        cardStackView = findViewById(R.id.cardstack);
        cardStackView.setCardEventListener(new CardStackView.CardEventListener() {
            @Override
            public void onCardDragging(float percentX, float percentY) {
                Log.d("CardStackView", "onCardDragging");
            }

            @Override
            public void onCardSwiped(SwipeDirection direction) {
                Log.d("CardStackView", "onCardSwiped: " + direction.toString());
                Log.d("CardStackView", "topIndex: " + cardStackView.getTopIndex());
                if (gameOver) {
                    gameOver = false;
                    for (int i = 0; i < indicators.length; i++) {
                        indicators[i] = 10;
                        int id = getApplicationContext().getResources().getIdentifier("icon_" + indicatorNames[i] + "_10", "drawable", getApplicationContext().getPackageName());
                        indicatorIcons[i].setImageResource(id);
                    }
                    reload();
                } else {
                    if (cardStackView.getTopIndex() == adapter.getCount() - 5) {
                        Log.d("CardStackView", "Paginate: " + cardStackView.getTopIndex());
                        paginate();
                    }
                    updateIndicators(direction == SwipeDirection.Left);
                    updateCard();
                }
            }

            @Override
            public void onCardReversed() {
                Log.d("CardStackView", "onCardReversed");
            }

            @Override
            public void onCardMovedToOrigin() {
                Log.d("CardStackView", "onCardMovedToOrigin");
            }

            @Override
            public void onCardClicked(int index) {
                Log.d("CardStackView", "onCardClicked: " + index);
            }
        });
    }

    private void reload() {
        cardStackView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        cardIndex = 0;
        if (!gameOver) {
            scoreText.setText("0");
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter = createCardCardAdapter();
                cardStackView.setAdapter(adapter);
                cardStackView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }, 0);
    }

    private LinkedList<Card> extractRemainingCards() {
        LinkedList<Card> spots = new LinkedList<>();
        for (int i = cardStackView.getTopIndex(); i < adapter.getCount(); i++) {
            spots.add(adapter.getItem(i));
        }
        return spots;
    }


    private void paginate() {
        cardStackView.setPaginationReserved();
        adapter.addAll(cards);
        adapter.notifyDataSetChanged();
    }

    public void swipeLeft() {
        List<Card> spots = extractRemainingCards();
        if (spots.isEmpty()) {
            return;
        }

        View target = cardStackView.getTopView();
        View targetOverlay = cardStackView.getTopView().getOverlayContainer();

        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", -10f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, -2000f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 0f, 500f));
        translateX.setStartDelay(100);
        translateY.setStartDelay(100);
        translateX.setDuration(500);
        translateY.setDuration(500);
        AnimatorSet cardAnimationSet = new AnimatorSet();
        cardAnimationSet.playTogether(rotation, translateX, translateY);

        ObjectAnimator overlayAnimator = ObjectAnimator.ofFloat(targetOverlay, "alpha", 0f, 1f);
        overlayAnimator.setDuration(200);
        AnimatorSet overlayAnimationSet = new AnimatorSet();
        overlayAnimationSet.playTogether(overlayAnimator);

        cardStackView.swipe(SwipeDirection.Left, cardAnimationSet, overlayAnimationSet);
        this.updateIndicators(true);
        this.updateCard();
    }

    public void swipeRight() {
        List<Card> spots = extractRemainingCards();
        if (spots.isEmpty()) {
            return;
        }

        View target = cardStackView.getTopView();
        View targetOverlay = cardStackView.getTopView().getOverlayContainer();

        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", 10f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, 2000f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 0f, 500f));
        translateX.setStartDelay(100);
        translateY.setStartDelay(100);
        translateX.setDuration(500);
        translateY.setDuration(500);
        AnimatorSet cardAnimationSet = new AnimatorSet();
        cardAnimationSet.playTogether(rotation, translateX, translateY);

        ObjectAnimator overlayAnimator = ObjectAnimator.ofFloat(targetOverlay, "alpha", 0f, 1f);
        overlayAnimator.setDuration(200);
        AnimatorSet overlayAnimationSet = new AnimatorSet();
        overlayAnimationSet.playTogether(overlayAnimator);

        cardStackView.swipe(SwipeDirection.Right, cardAnimationSet, overlayAnimationSet);
        this.updateIndicators(false);
        this.updateCard();
    }


    private void updateIndicators(Boolean isLeft) {
        int[] update;
        if (isLeft) {
            update = this.cards.get(this.cardIndex).effects_left;
        } else {
            update = this.cards.get(this.cardIndex).effects_right;
        }
        for (int i = 0; i < update.length; i++) {
            this.indicators[i] = this.indicators[i] + update[i];
            Log.i("INDICATORS", Arrays.toString(this.indicators));
            if (this.indicators[i] < 1 || this.indicators[i] > 19) {
                cards.clear();
                String maxMin = this.indicators[i] < 1 ? "_min" : "_max";
                this.indicators[i] = this.indicators[i] < 1 ? 0 : 20;
                int value = this.indicators[i] < 1 ? 0 : 20;
                String lostReason = this.indicatorNames[i] + maxMin;
                Log.i("LOST", lostReason);
                int id = this.getApplicationContext().getResources().getIdentifier("icon_" + this.indicatorNames[i] + "_" + String.valueOf(value), "drawable", getApplicationContext().getPackageName());
                indicatorIcons[i].setImageResource(id);
                gameOver = true;
                Card gameOverCard = (readCard(getApplicationContext(), lostReason));
                cards.add(gameOverCard);
                //cardStackView.setSwipeEnabled(false);
                reload();
                break;
            }
            int id = this.getApplicationContext().getResources().getIdentifier("icon_" + this.indicatorNames[i] + "_" + String.valueOf(this.indicators[i]), "drawable", getApplicationContext().getPackageName());
            indicatorIcons[i].setImageResource(id);
        }

    }

    private void updateCard() {
        if (!gameOver) {
            scoreText.setText(String.valueOf(100 * (cardIndex + 1)));
        }
        cardIndex++;
        if (cardIndex < cards.size()) {
            cardText.setText(cards.get(cardIndex).text);
            leftText.setText(cards.get(cardIndex).left);
            rightText.setText(cards.get(cardIndex).right);
            Log.i("CARDTEXT", cards.get(cardIndex).text);
        }
    }

    private void reverse() {
        cardStackView.reverse();
    }

}
