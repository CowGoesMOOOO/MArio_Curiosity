package me.CowGoesMOOOO.main;

import me.CowGoesMOOOO.helper.exceptions.DimensionMismatchException;
import me.CowGoesMOOOO.helper.Matrix;
import me.CowGoesMOOOO.helper.MatrixMath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class NeuralNet {

    private final ArrayList<Matrix> weights = new ArrayList<>();
    private final ArrayList<Matrix> biases = new ArrayList<>();

    private final double eta = 0.3;

    private final int[] layers;

    /**
     * Initialize the neural network
     * @param layers Array with the amount of layers and simultaneously the amount of neurons in each layer
     */
    public NeuralNet(int[] layers) {

        this.layers = layers;

        // Creating all weight matrices
        for(int a = 1,b = 0; a < layers.length; a++,b++){
            Matrix mat = new Matrix(layers[a], layers[b]);
            weights.add(mat);
        }

        // Creating all biases vectors
        for(int i = 1; i < layers.length; i++){
            Matrix mat = new Matrix(layers[i], 1);
            biases.add(mat);
        }

    }

    /**
     * Uncompleted but functional training method using backpropagation and gradient descent
     * @param x Dataset input
     * @param y Correct dataset
     */
    public void trainBackprop(Matrix x, Matrix y) throws DimensionMismatchException{
        backprop(x,y,eta);
    }

    /**
     * Backpropagation used in combination with gradient descent
     * @param input Dataset input
     * @param expected Expected output
     * @param eta Learning constant
     */
    private void backprop(Matrix input, Matrix expected, double eta) throws DimensionMismatchException {

        // Initiating the activation and output matrices
        ArrayList<Matrix> outputList = new ArrayList<>(), activationList = new ArrayList<>();

        // Creating the matrices to adjust the weights and biases later
        ArrayList<Matrix> nabla_b = new ArrayList<>(), nabla_w = new ArrayList<>();
        for(int i = 0; i < this.weights.size(); i++){
            nabla_b.add(i, null);
            nabla_w.add(i, null);
        }

        // Feeding the input forward, calculating all outputs and activations and saving them in the arraylist
        Matrix output, activation = input;
        activationList.add(0, input);
        outputList.add(null);

        for(int i = 1; i < layers.length; i++){
            output = MatrixMath.matrixAdd(MatrixMath.dotProd(weights.get(i-1), activation), biases.get(i-1));
            activation = sigmoid(output);
            activationList.add(activation);
            outputList.add(i,output);
        }

        // Backwards pass

        // Calculating the delta of the last layer
        Matrix delta = MatrixMath.haradmanProd(cost_d(activationList.get(layers.length - 1), expected), dsigmoid(outputList.get(layers.length-1)));

        // Calculating the values for the gradient descent
        nabla_b.add(layers.length - 1, delta);
        nabla_w.add(layers.length - 1, MatrixMath.dotProd(delta, MatrixMath.transpose(activationList.get(layers.length - 2))));

        // Following the error backwards
        for(int i = 2; i < layers.length; i++){
            // Creating the output matrix with the sigmoid derivative
            Matrix dOutput = dsigmoid(outputList.get(layers.length-i));

            // Calculating the error of the layer i
            delta = MatrixMath.haradmanProd(dOutput, MatrixMath.dotProd(MatrixMath.transpose(weights.get(layers.length-i)), delta));

            // Calculating the values for the gradient descent
            nabla_b.set(layers.length - i,delta);
            nabla_w.set(layers.length - i, MatrixMath.dotProd(delta, MatrixMath.transpose(activationList.get(layers.length-i-1))));
        }

        // Adjusting the weights and the biases values
        for(int i = 0; i < weights.size(); i++){
            weights.set(i, MatrixMath.matrixSub(weights.get(i), MatrixMath.multNumber(nabla_w.get(i+1), eta)));
            biases.set(i, MatrixMath.matrixSub(biases.get(i), MatrixMath.multNumber(nabla_b.get(i+1), eta)));
        }
    }

    /**
     * Make a prediction using a input matrix
     * @param input Matrix to make the prediction
     * @return Prediction matrix
     * @throws DimensionMismatchException If matrices don't match to do the calculations
     */
    public Matrix predict(Matrix input) throws DimensionMismatchException {
        Matrix output = input, z;
        for(int i = 1; i < layers.length; i++){
            z = MatrixMath.matrixAdd(MatrixMath.dotProd(weights.get(i-1), output), biases.get(i-1));
            output = sigmoid(z);
        }
        return output;
    }

    /**
     * Parse a number through the sigmoid function
     * @param x Matrix to parse through
     * @return Activation Matrix
     */
    private Matrix sigmoid(Matrix x){
        for(int i = 0; i < x.getRow(); i++){
            for(int j = 0; j < x.getColumn(); j++){
                x.getMatrix()[i][j] = 1/(1+Math.exp(-x.getMatrix()[i][j]));
            }
        }
        return x;
    }

    /**
     * Feeds a Matrix through the derivative of the sigmoid function
     * @param x Matrix input that needs to go through the derivative function
     * @return Resulting matrix
     */
    private Matrix dsigmoid(Matrix x){
        for(int i = 0; i < x.getRow(); i++){
            for(int j = 0; j < x.getColumn(); j++){
                x.getMatrix()[i][j] = Math.exp(-x.getMatrix()[i][j])/((1+Math.exp(-x.getMatrix()[i][j]))*(1+Math.exp(-x.getMatrix()[i][j])));
            }
        }
        return x;
    }

    /**
     * Calculates the cost derivative from two matrices
     * @param matrixA Output of the neural Network in form of a matrix
     * @param matrixB Expected solutions
     * @return Returns the cost derivative vector
     * @throws DimensionMismatchException
     */
    private Matrix cost_d(Matrix matrixA, Matrix matrixB) throws DimensionMismatchException {
        // Checking if the matrices have the same dimensions
        // We don't have to check the columns because both matrixA and matrixB are vectors
        if(matrixA.getRow() != matrixB.getRow())
            throw new DimensionMismatchException("Dimension of two matrices did not match while trying to calculate the cost derivative!");

        // Creating the resulting matrix and calculating the cost derivative
        Matrix mat = new Matrix(matrixA.getRow(), matrixA.getColumn());
        for(int i = 0; i < matrixA.getRow(); i++){
            mat.getMatrix()[i][0] = (matrixA.getMatrix()[i][0] - matrixB.getMatrix()[i][0]);
        }
        return mat;
    }
}
