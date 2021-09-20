package me.CowGoesMOOOO.main;

import me.CowGoesMOOOO.helper.Matrix;

public class QMatrix {

    private double learningRate;
    private double discountRate;
    private Matrix mat;

    public QMatrix(int states, int actions, double learningRate, double discountRate){
        mat = new Matrix(states, actions, false);
        this.learningRate = learningRate;
        this.discountRate = discountRate;
    }

    public Matrix getMatrix() {
        return mat;
    }

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
