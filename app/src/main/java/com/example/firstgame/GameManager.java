package com.example.firstgame;


import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

public class GameManager {

    private int life;
    private int placeOfPlane = 1;
    private final int rows;
    private final int columns;
    private Item[][] matrix;
    private int lastIteration = -1;
    private int wrong;


    public GameManager(int life, int rows, int columns) {
        this.life = life;
        this.rows = rows;
        this.columns = columns;
        this.matrix = new Item[rows][columns];

    }


    public int getRandomIndex() {
        int min = 0;
        int max = rows - 1;
        return (int) Math.floor(Math.random() * (max - min) + min);
    }

    public Item[][] getMatrix() {
        return matrix;
    }

    private void shiftMatrix() {

        for (int i = rows - 1; i >= 0; i--) {
            for (int j = 0; j < columns; j++) {
                if (i == rows - 1) {
                    if(matrix[i][j].getType()==Type.VISIBLE) {
                        matrix[i][j].setType(Type.INVISIBLE);
                        lastIteration = j;
                    }
                } else {
                    matrix[i + 1][j].setType(matrix[i][j].getType());
                }
            }
        }





    }
    public void updateTable(){
        shiftMatrix();
        putRandomBomb();
    }

    private void putRandomBomb() {
        int randomNumber = getRandomIndex();
        for (int i = 0; i < columns; i++) {
            if (i == randomNumber) {
                matrix[0][i].setType(Type.VISIBLE);

            } else {
                matrix[0][i].setType(Type.INVISIBLE);

            }
        }
    }

    public boolean move(String move) {
        if (move.equals(MainActivity.KEY_LEFT)) {
            if (placeOfPlane > 0) {
                placeOfPlane--;
                return true;
            }
        } else if (move.equals(MainActivity.KEY_RIGHT)) {
            if (placeOfPlane < rows - 2) {
                placeOfPlane++;
                return true;
            }
        }
        return false;

    }

    public int getCurrentPlace() {
        return placeOfPlane;
    }


    public void initMatrix(AppCompatImageView[][] matrixView) {

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = new Item()
                        .setImage(matrixView[i][j])
                        .setType(Type.INVISIBLE);
                ;
            }
        }
    }

    public boolean checkIsWrong() {
        if(placeOfPlane == lastIteration && wrong < life){
            wrong++;
            System.out.println("- LIFE " + placeOfPlane + " -Iteration " + lastIteration);
            return true;
        }

        return false;
    }

    public int getWrong() {
        return wrong;
    }

    public boolean isEndGame(){
        return wrong == life;
    }
}
