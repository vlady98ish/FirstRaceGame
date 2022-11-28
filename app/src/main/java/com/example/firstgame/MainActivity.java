package com.example.firstgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    final int ROW_COUNT = 4;
    final int COLUMN_COUNT = 3;
    private AppCompatImageView[] main_IMG_hearts;


    private MediaPlayer music;

    private AppCompatImageView[][] main_MATRIX_bombs;
    private AppCompatImageView[] main_IMG_planes;


    private ExtendedFloatingActionButton main_BTN_left;
    private ExtendedFloatingActionButton main_BTN_right;


    public static final String KEY_LEFT = "KEY_LEFT";
    public static final String KEY_RIGHT = "KEY_RIGHT";
    public  final String GAME_OVER = "GAME OVER";
    public final String LOST_LIFE = "You lost 1 life";

    final int DELAY = 1000;

    GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        gameManager = new GameManager(main_IMG_hearts.length, ROW_COUNT, COLUMN_COUNT);
        initViews();
        initMusic();


    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
        music.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
        music.pause();
    }




    private Timer timer = new Timer();

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> updateTimeUI());
            }
        }, DELAY, DELAY);
    }

    private void stopTimer() {
        timer.cancel();
    }


    private void updateTimeUI() {
        if (gameManager.isEndGame()) {
            gameOver();
        } else {
            updateUI();
        }
    }

    private void updateUI() {
        gameManager.updateTable();
        if (gameManager.checkIsWrong()) {
            renderHearts();
            toast(LOST_LIFE);
            MySignal.getInstance().vibrate();
        }

        renderMatrix(gameManager.getMatrix());
    }

    private void gameOver() {
        toast(GAME_OVER);
        finish();

    }

    private void toast(String text) {
        MySignal.getInstance().toast(text);
    }


    /*RENDERING FUNCTIONS */
    private void renderMatrix(Item[][] items) {
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COLUMN_COUNT; j++) {
                Type type = items[i][j].getType();
                if (type == Type.VISIBLE) {
                    items[i][j].getImage().setVisibility(View.VISIBLE);
                } else {
                    items[i][j].getImage().setVisibility(View.INVISIBLE);
                }

            }
        }
    }

    private void renderHearts() {
        int index = gameManager.getWrong() - 1;
        main_IMG_hearts[index].setVisibility(View.INVISIBLE);

    }
    /*RENDERING FUNCTIONS */

    /*INIT FUNCTIONS*/
    private void initMatrixBombs() {
        main_MATRIX_bombs = new AppCompatImageView[ROW_COUNT][COLUMN_COUNT];
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COLUMN_COUNT; j++) {
                String name = "main_IMG_bomb_" + (i + 1) + "_" + (j + 1);
                System.out.println(name);
                int id = getResources().getIdentifier(name, "id", getPackageName());
                AppCompatImageView bomb = findViewById(id);
                main_MATRIX_bombs[i][j] = bomb;
            }
        }
    }
    private void initMusic(){
        music = MediaPlayer.create(MainActivity.this,R.raw.game_music);
    }
    private void initViews() {
        main_BTN_left.setOnClickListener(view -> {
            if (gameManager.move(KEY_LEFT)) {
                visibility(1);
            }


        });

        main_BTN_right.setOnClickListener(view -> {
            if (gameManager.move(KEY_RIGHT)) {
                visibility(-1);
            }
        });

        gameManager.initMatrix(main_MATRIX_bombs);
    }
    /*INIT FUNCTIONS*/

    private void visibility(int direction) {
        int place = gameManager.getCurrentPlace();
        main_IMG_planes[place].setVisibility(View.VISIBLE);
        main_IMG_planes[place + direction].setVisibility(View.INVISIBLE);
    }


    /*All FIND FUNCTIONS */
    private void findHearts() {
        main_IMG_hearts = new AppCompatImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3),
        };
    }

    private void findPlanes() {
        main_IMG_planes = new AppCompatImageView[]{
                findViewById(R.id.main_IMG_plane1),
                findViewById(R.id.main_IMG_plane2),
                findViewById(R.id.main_IMG_plane3),
        };
    }

    private void findButtons() {
        main_BTN_left = findViewById(R.id.main_BTN_left);
        main_BTN_right = findViewById(R.id.main_BTN_right);
    }

    private void findViews() {
        findHearts();
        findPlanes();
        findButtons();


        initMatrixBombs();


    }
    /*All FIND FUNCTIONS */

}