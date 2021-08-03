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

    private final int[] layers;

    /**
     * Initialize the neural network
     * @param layers Array with the amount of layers and simultaneously the amount of neurons in each layer
     */
    public NeuralNet(int[] layers) {

        this.layers = layers;

        //Creating all weight matrices
        for(int a = 1,b = 0; a < layers.length; a++,b++){
            Matrix mat = new Matrix(layers[a], layers[b]);
            weights.add(mat);
        }

        //Creating all biases vectors
        for(int i = 1; i < layers.length; i++){
            Matrix mat = new Matrix(layers[i], 1);
            biases.add(mat);
        }

    }

    /**
     * Uncompleted but functional training method using backpropagation and gradient descent
     * @param x Dataset input
     * @param y Correct dataset
     * @param batches Number of batches
     * @param batchSize Number of batches size (not currently used and just multiplied with the number of batches)
     * @param eta Learning constant
     */
    public void trainBackprop(Matrix x, Matrix y, int batches, int batchSize, double eta) throws DimensionMismatchException{
        Random rnd = new Random();
        for(int k = 0; k < batches; k++) {
            for (int i = 0; i < batchSize; i++) {
                int n = rnd.nextInt(x.getRow());

                try {
                    backprop(x.getMatrix()[n], y.getMatrix()[n], eta);
                }catch(DimensionMismatchException e){
                    e.printStackTrace();
                }

                if(k % 500 == 0){

                    double[][] ma = {{0},{1},{0},{1}};
                    double[][] mb = {{1},{0},{1},{0}};
                    double[][] mc = {{0},{0},{1},{1}};
                    double[][] md = {{0},{0},{0},{1}};

                    Matrix a = new Matrix(ma);
                    Matrix b = new Matrix(mb);
                    Matrix c = new Matrix(mc);
                    Matrix d = new Matrix(md);

                    System.out.println("------------------------------------------");
                    System.out.println("Expected to be a {1,1} {0,1,0,1}: " + Arrays.deepToString(predict(a).getMatrix()));
                    System.out.println("Expected to be a {1,0} {1,0,1,0}: " + Arrays.deepToString(predict(b).getMatrix()));
                    System.out.println("Expected to be a {0,0} {0,0,1,1}: "+ Arrays.deepToString(predict(c).getMatrix()));
                    System.out.println("Expected to be a {0,1} {0,0,0,1}: " + Arrays.deepToString(predict(d).getMatrix()));
                    System.out.println("------------------------------------------");

                }
            }
        }
    }

    /**
     * Backpropagation used in combination with gradient descent
     * @param x Dataset input
     * @param y Correct output
     * @param eta Learning constant
     * @return ArrayList of updated weight and biases values
     */
    private ArrayList<ArrayList<Matrix>> backprop(double[] x, double[] y, double eta) throws DimensionMismatchException {

        ArrayList<Matrix> zs = new ArrayList<>();
        ArrayList<Matrix> activations = new ArrayList<>();

        ArrayList<Matrix> nabla_b = new ArrayList<>();
        ArrayList<Matrix> nabla_w = new ArrayList<>();

        for(int i = 0; i < this.weights.size(); i++){
            nabla_b.add(i, null);
            nabla_w.add(i, null);
        }

        Matrix xs = new Matrix(new double[x.length][1]);

        for(int i = 0; i < x.length; i++){
            xs.getMatrix()[i][0] = x[i];
        }

        //Feedforword
        Matrix z;
        Matrix activation = xs;
        activations.add(0, xs);
        zs.add(null);

        for(int i = 1; i < layers.length; i++){
            z = MatrixMath.matrixAdd(MatrixMath.dotProd(weights.get(i-1), activation), biases.get(i-1));
            activation = sigmoid(z);
            activations.add(activation);
            zs.add(i,z);
        }

        //Back pass
        Matrix delta = MatrixMath.haradmanProd(cost_d(activations.get(layers.length - 1), y), dsigmoid(zs.get(layers.length-1)));
        nabla_b.add(layers.length - 1, delta);
        nabla_w.add(layers.length - 1, MatrixMath.dotProd(delta, MatrixMath.transpose(activations.get(layers.length - 2))));

        for(int i = 2; i < layers.length; i++){
            Matrix zi = zs.get(layers.length-i);
            Matrix sp = dsigmoid(zi);

            delta = MatrixMath.haradmanProd(sp, MatrixMath.dotProd(MatrixMath.transpose(weights.get(layers.length-i)), delta));
            nabla_b.set(layers.length - i,delta);
            nabla_w.set(layers.length - i, MatrixMath.dotProd(delta, MatrixMath.transpose(activations.get(layers.length-i-1))));
        }

        for(int i = 0; i < weights.size(); i++){
            weights.set(i, MatrixMath.matrixSub(weights.get(i), MatrixMath.multNumber(nabla_w.get(i+1), eta)));
            biases.set(i, MatrixMath.matrixSub(biases.get(i), MatrixMath.multNumber(nabla_b.get(i+1), eta)));
        }

        ArrayList<ArrayList<Matrix>> result = new ArrayList<>();
        result.add(nabla_w);
        result.add(nabla_b);
        return result;
    }

    /**
     * Make a prediction using a input matrix
     * @param input Matrix to make the prediction
     * @return Prediction matrix
     * @throws DimensionMismatchException If matrices don't match to do the calculations
     */
    public Matrix predict(Matrix input) throws DimensionMismatchException {
        Matrix output = input;
        Matrix z;
        for(int i = 1; i < layers.length; i++){
            z = MatrixMath.matrixAdd(MatrixMath.dotProd(weights.get(i-1), input), biases.get(i-1));
            output = sigmoid(z);
        }
        return output;
    }

    /**
     * Parse a number through a activation function
     * @param x Number to parse through
     * @return Activation number
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
     * Derivative from the sigmoid function
     */
    private Matrix dsigmoid(Matrix x){
        for(int i = 0; i < x.getRow(); i++){
            for(int j = 0; j < x.getColumn(); j++){
                x.getMatrix()[i][j] = Math.exp(-x.getMatrix()[i][j])/((1+Math.exp(-x.getMatrix()[i][j]))*(1+Math.exp(-x.getMatrix()[i][j])));
            }
        }
        return x;
    }

    private Matrix cost_d(Matrix matrixA, double[] vectorB) throws DimensionMismatchException {
        if(matrixA.getRow() != vectorB.length)
            throw new DimensionMismatchException("Dimension of two matrices did not match while calculation the cost derivative!");
        Matrix mat = new Matrix(matrixA.getRow(), matrixA.getColumn());

        for(int i = 0; i < matrixA.getRow(); i++){
            mat.getMatrix()[i][0] = (matrixA.getMatrix()[i][0] - vectorB[i]);
        }
        return mat;
    }
}
