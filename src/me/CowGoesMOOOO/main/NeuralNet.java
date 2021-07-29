package me.CowGoesMOOOO.main;

import me.CowGoesMOOOO.helper.Matrix;

import java.util.ArrayList;

public class NeuralNet {

    private final ArrayList<Matrix> weights = new ArrayList<>();
    private final ArrayList<Matrix> biases = new ArrayList<>();

    private final int[] layers;

    /**
     * Initialize the neural network
     * @param layers Array with the amount of layers and simultaneously the amount of neurons in each layer
     */
    public NeuralNet(int[] layers) {
        this.layers = layers;

        //Creating all weight matrices
        for(int i = 1, j = 0; i < layers.length; i++,j++){
            Matrix mat = new Matrix(layers[i], layers[j]);
            weights.add(mat);
        }

        for(int i = 1; i < layers.length; i++){
            Matrix mat = new Matrix(layers[i], 1);
            biases.add(mat);
        }

    }

    public Matrix predict(){
        return null;
    }

    /**
     * Parse a number through a activation function
     * @param x Number to parse through
     * @return Activation number
     */
    private double[][] sigmoid(double[][] x){
        for(int i = 0; i < x.length; i++){
            for(int j = 0; j < x[0].length; j++){
                x[i][j] = 1/(1+Math.exp(-x[i][j]));
            }
        }

        return x;
    }

    /**
     * Derivative from the sigmoid function
     */
    private double[][] dsigmoid(double[][] x){
        for(int i = 0; i < x.length; i++){
            for(int j = 0; j < x[0].length; j++){
                x[i][j] = Math.exp(-x[i][j])/((1+Math.exp(-x[i][j]))*(1+Math.exp(-x[i][j])));
            }
        }

        return x;
    }
}
