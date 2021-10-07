package me.CowGoesMOOOO.main;

import me.CowGoesMOOOO.helper.Matrix;

public class QMatrix {

    private double learningRate;
    private double discountRate;
    private Matrix mat;

    /**
     * Creates the qMatrix object
     * @param states The amount of states the player can have
     * @param actions The amount of actions the player can perform
     *
     */
    public QMatrix(int states, int actions, double learningRate, double discountRate){
        mat = new Matrix(states, actions, false);
        this.learningRate = learningRate;
        this.discountRate = discountRate;
    }

    /**
     * Returns the matrix of the object
     * @return Matrix
     */
    public Matrix getMatrix() {
        return mat;
    }

    /**
     * Recalculates the qValue of the current State and action
     * @param currentState The current state the player is in
     * @param nextState The next state the player will be in
     * @param action The performed action of the player
     * @param reward The gained reward of the player
     */
    public void train(int currentState, int nextState, int action, double reward){
        double qValue = this.mat.getMatrix()[currentState][action];
        double firstFactor = (1-learningRate)*qValue;

        double maxQ = Double.NEGATIVE_INFINITY;
        for(int i = 0; i < mat.getRow(); i++){
            if(this.mat.getMatrix()[nextState][i] > maxQ){
                maxQ = this.mat.getMatrix()[nextState][i];
            }
        }

        double secondFactor = learningRate*(reward+(discountRate*(maxQ)));
        this.mat.getMatrix()[currentState][action] = firstFactor + secondFactor;
    }

}
