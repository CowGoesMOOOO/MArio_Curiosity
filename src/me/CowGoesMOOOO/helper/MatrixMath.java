package me.CowGoesMOOOO.helper;

public class MatrixMath {

    public static Matrix dotProd(Matrix matrixA, Matrix matrixB) throws DimensionMismatchException {
        if(matrixA.getColumn() != matrixB.getRow()) {
            throw new DimensionMismatchException("The dimension of two matrices mismatched when trying to calculate the dot product!");
        }
        Matrix mat = new Matrix(matrixA.getRow(), matrixB.getColumn());
        for(int i = 0; i < matrixA.getRow(); i++) {
            for (int j = 0; j < matrixB.getColumn(); j++) {
                double prod = 0;
                for (int k = 0; k < matrixA.getColumn(); k++) {
                    prod += matrixA.getMatrix()[i][k] * matrixB.getMatrix()[k][j];
                }
                mat.getMatrix()[i][j] = prod;
            }
        }
        return mat;
    }

    public static Matrix matrixAdd(Matrix matrixA, Matrix matrixB) throws DimensionMismatchException {
        if(matrixA.getColumn() != matrixB.getColumn() && matrixA.getRow() != matrixB.getRow())
            throw new DimensionMismatchException("The dimension of two matrices mismatched when trying to calculate the addition product!");

        Matrix mat = new Matrix(matrixA.getColumn(), matrixB.getRow());
        for(int i = 0; i < mat.getColumn(); i++){
            for(int j = 0; j < mat.getRow(); j++){
                mat.getMatrix()[i][j] = matrixA.getMatrix()[i][j] + matrixB.getMatrix()[i][j];
            }
        }
        return mat;
    }

    public static Matrix matrixSub(Matrix matrixA, Matrix matrixB) throws DimensionMismatchException {
        if(matrixA.getColumn() != matrixB.getColumn() && matrixA.getRow() != matrixB.getRow())
            throw new DimensionMismatchException("The dimension of two matrices mismatched when trying to calculate the subtraction product!");

        Matrix mat = new Matrix(matrixA.getColumn(), matrixB.getRow());
        for(int i = 0; i < mat.getColumn(); i++){
            for(int j = 0; j < mat.getRow(); j++){
                mat.getMatrix()[i][j] = matrixA.getMatrix()[i][j] - matrixB.getMatrix()[i][j];
            }
        }
        return mat;
    }



}
