package Atom.Algorithm.MachineLearning;

import java.io.Serializable;

public class NeuralNetwork implements Serializable {
    //Simple Neural Network
    //Input Layer
    //Hidden Layer
    //Output Layer
    protected double[] weights;
    protected double[] biases;
    protected int[] layers;
    
    public void train(double[][] input, double[][] output, int iterations) {
        //initialize weights and biases
        this.weights = new double[input.length];
        this.biases = new double[input.length];
        this.layers = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            this.weights[i] = 0;
            this.biases[i] = 0;
            this.layers[i] = input[i].length;
        }
        //train the network
        for (int i = 0; i < iterations; i++) {
            for (int j = 0; j < input.length; j++) {
                //calculate the output
                double[] outputs = this.calculate(input[j]);
                //calculate the error
                double[] errors = new double[outputs.length];
                for (int k = 0; k < errors.length; k++) {
                    errors[k] = output[j][k] - outputs[k];
                }
                //update the weights and biases
                for (int k = 0; k < errors.length; k++) {
                    this.weights[j] += errors[k] * input[j][k];
                    this.biases[j] += errors[k];
                }
            }
        }
    }
    
    public double[] calculate(double[] input) {
        double[] outputs = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            outputs[i] = this.weights[i] * input[i] + this.biases[i];
        }
        return outputs;
    }
    
    
}
