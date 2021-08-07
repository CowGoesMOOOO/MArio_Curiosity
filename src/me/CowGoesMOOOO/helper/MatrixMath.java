package me.CowGoesMOOOO.helper;

import me.CowGoesMOOOO.helper.exceptions.DimensionMismatchException;

public class MatrixMath {

    /**
     * Dot product of two matrices
     * @param matrixA First matrix
     * @param matrixB Second matrix
     * @return Resulting matrix
     * @throws DimensionMismatchException The amount of columns in the first matrix have to match the amount of rows in the second matrix
     */
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

    /**
     * Addition of two matrices
     * @param matrixA First matrix
     * @param matrixB Second matrix
     * @return Resulting matrix
     * @throws DimensionMismatchException Both matrices have to have the same dimensions
     */
    public static Matrix matrixAdd(Matrix matrixA, Matrix matrixB) throws DimensionMismatchException {
        if(matrixA.getColumn() != matrixB.getColumn() && matrixA.getRow() != matrixB.getRow())
            throw new DimensionMismatchException("The dimension of two matrices mismatched when trying to calculate the addition product!");

        Matrix mat = new Matrix(matrixA.getRow(), matrixB.getColumn());
        for(int i = 0; i < mat.getRow(); i++){
            for(int j = 0; j < mat.getColumn(); j++){
                mat.getMatrix()[i][j] = matrixA.getMatrix()[i][j] + matrixB.getMatrix()[i][j];
            }
        }
        return mat;
    }

    /**
     * Subtraction of two matrices
     * @param matrixA First matrix
     * @param matrixB Second matrix
     * @return Resulting matrix
     * @throws DimensionMismatchException Both matrices have to have the same dimensions
     */
    public static Matrix matrixSub(Matrix matrixA, Matrix matrixB) throws DimensionMismatchException {
        if(matrixA.getColumn() != matrixB.getColumn() && matrixA.getRow() != matrixB.getRow())
            throw new DimensionMismatchException("The dimension of two matrices mismatched when trying to calculate the subtraction product!");

        Matrix mat = new Matrix(matrixA.getRow(), matrixB.getColumn());
        for(int i = 0; i < mat.getRow(); i++){
            for(int j = 0; j < mat.getColumn(); j++){
                mat.getMatrix()[i][j] = matrixA.getMatrix()[i][j] - matrixB.getMatrix()[i][j];
            }
        }
        return mat;
    }

    /**
     * Elementwise multiplication of two matrices
     * @param matrixA First matrix
     * @param matrixB Second matrix
     * @return Resulting matrix
     * @throws DimensionMismatchException Both matrices have to have the same dimensions
     */
    public static Matrix haradmanProd(Matrix matrixA, Matrix matrixB) throws DimensionMismatchException {
        if(matrixA.getColumn() != matrixB.getColumn() && matrixA.getRow() != matrixB.getRow())
            throw new DimensionMismatchException("The dimension of two matrices mismatched when trying to calculate the haradman product!");

        Matrix mat = new Matrix(matrixA.getRow(), matrixB.getColumn());

        for(int i = 0; i < mat.getRow(); i++) {
            for (int j = 0; j < mat.getColumn(); j++) {
                mat.getMatrix()[i][j] = matrixA.getMatrix()[i][j] * matrixB.getMatrix()[i][j];
            }
        }
        return mat;
    }

    /**
     * Transposes given matrix
     * @param matrixA Input matrix
     * @return Resulting matrix
     */
    public static Matrix transpose(Matrix matrixA){
        Matrix mat = new Matrix(matrixA.getColumn(), matrixA.getRow());
        for(int i = 0; i < matrixA.getColumn(); i++){
            for(int j = 0; j < matrixA.getRow(); j++){
                mat.getMatrix()[i][j] = matrixA.getMatrix()[j][i];
            }
        }
        return mat;
    }

    /**
     * Multiplies each element of given matrix by the given number
     * @param matrixA Given matrix
     * @param d Given number
     * @return Resulting matrix
     */
    public static Matrix multNumber(Matrix matrixA, double d){
        Matrix mat = matrixA;
        for(int i = 0; i < matrixA.getRow(); i++){
            for(int j = 0; j < matrixA.getColumn(); j++){
                mat.getMatrix()[i][j] *= d;
            }
        }
        return mat;
    }

    public static Matrix addNumber(Matrix matrixA, double d){
        Matrix mat = matrixA;
        for(int i = 0; i < matrixA.getRow(); i++){
            for(int j = 0; j < matrixA.getColumn(); j++){
                mat.getMatrix()[i][j] += d;
            }
        }
        return mat;
    }




}
