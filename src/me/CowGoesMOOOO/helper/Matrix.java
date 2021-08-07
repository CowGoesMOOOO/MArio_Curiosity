package me.CowGoesMOOOO.helper;

import java.util.Random;

public class Matrix {

    private double[][] mat;
    private int row;
    private int column;


    public Matrix(int row, int col){
        this.column = col;
        this.row = row;
        mat = new double[row][col];
        fillGaussian();
    }

    public void fillGaussian(){
        Random rnd = new Random();
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                mat[i][j] = rnd.nextGaussian();
            }
        }
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public double[][] getMatrix() {
        return mat;
    }
}
