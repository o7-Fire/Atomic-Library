package Atom.Algorithm.MachineLearning.Reinforcement;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;

public class Sarsa {
    private double[][] QValue;
    private int[][] action;
    private double alpha;
    private double gamma;
    private int stateNum;
    private int actionNum;
    private int episode;
    private int step;
    private int maxStep;
    private int maxEpisode;
    private int maxAction;
    private int maxState;
    private int[] state;
    private int[] action_;
    private int[] nextState;
    private double[] reward;
    private double[] nextReward;
    private double[] actionValue;
    private double[] nextActionValue;
    private double[] actionValue_;
    
    public Sarsa(int stateNum, int actionNum, double alpha, double gamma, int maxStep, int maxEpisode) {
        this.stateNum = stateNum;
        this.actionNum = actionNum;
        this.alpha = alpha;
        this.gamma = gamma;
        this.maxStep = maxStep;
        this.maxEpisode = maxEpisode;
        this.maxState = stateNum;
        this.maxAction = actionNum;
        this.QValue = new double[maxState][maxAction];
        this.action = new int[maxState][maxAction];
        this.state = new int[maxStep];
        this.action_ = new int[maxStep];
        this.nextState = new int[maxStep];
        this.reward = new double[maxStep];
        this.nextReward = new double[maxStep];
        this.actionValue = new double[maxStep];
        this.nextActionValue = new double[maxStep];
        this.actionValue_ = new double[maxStep];
    }
    
    //load using gson
    public static Sarsa deserialize(String fileName) {
        Gson gson = new Gson();
        Sarsa sarsa = gson.fromJson(fileName, Sarsa.class);
        return sarsa;
    }
    
    //add training
    public void train(int[] state, int[] action, int[] nextState, double[] reward, double[] nextReward) {
        this.state = state;
        this.action_ = action;
        this.nextState = nextState;
        this.reward = reward;
        this.nextReward = nextReward;
        for (int i = 0; i < maxStep; i++) {
            actionValue[i] = QValue[state[i]][action[i]];
        }
        for (int i = 0; i < maxStep; i++) {
            if (i == 0){
                nextActionValue[i] = QValue[nextState[i]][action[i]];
            }else{
                nextActionValue[i] = QValue[nextState[i]][action[i]];
            }
        }
        for (int i = 0; i < maxStep; i++) {
            actionValue_[i] = actionValue[i] + alpha * (reward[i] + gamma * nextActionValue[i] - actionValue[i]);
        }
        for (int i = 0; i < maxStep; i++) {
            QValue[state[i]][action[i]] = actionValue_[i];
        }
    }
    
    //add test
    public void test(int[] state, int[] action) {
        this.state = state;
        this.action_ = action;
        for (int i = 0; i < maxStep; i++) {
            actionValue[i] = QValue[state[i]][action[i]];
        }
    }
    
    //serialize using gson
    public void serialize(String fileName) {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(json);
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
}
