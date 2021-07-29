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
    }

    public Matrix(int row, int col, double[][] mat){
        this.column = col;
        this.row = row;
        this.mat = mat;
    }

    public void fillGaussian(){
        Random rnd = new Random();
        for(int i = 0; i < column; i++){
            for(int j = 0; j < row; j++){
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
