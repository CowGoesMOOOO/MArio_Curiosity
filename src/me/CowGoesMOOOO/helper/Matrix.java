package me.CowGoesMOOOO.helper;

import java.util.Random;

public class Matrix {

    private double[][] mat;
    private int row;
    private int column;

    /**
     * Creates the matrix object. Fills the matrix with random numbers
     * @param row The amount of rows in the matrix
     * @param col The amount of columns in the matrix
     */
    public Matrix(int row, int col){
        this.column = col;
        this.row = row;
        mat = new double[row][col];
        fillGaussian();
    }

    /**
     * Creates the matrix object. Does not fill the matrix with random numbers
     * @param row The amount of rows in the matrix
     * @param col The amount of colums in the matrix
     */
    public Matrix(int row, int col, boolean b){
        this.column = col;
        this.row = row;
        mat = new double[row][col];
    }

    public void fillGaussian(){
        Random rnd = new Random();
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                mat[i][j] = rnd.nextGaussian();
            }
        }
    }

    /**
     * Returns the amount of columns of the matrix
     * @return Amount of columns
     */
    public int getColumn() {
        return column;
    }

    /**
     * Returns the amount of rows of the matrix
     * @return Amount of rows
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the double array
     * @return Returns double array
     */
    public double[][] getMatrix() {
        return mat;
    }
}
