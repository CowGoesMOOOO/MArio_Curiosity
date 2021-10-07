package me.CowGoesMOOOO.helper;

import com.TETOSOFT.tilegame.GameEngine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TimerThread implements Runnable{

    private int counter;
    private int secondsPerRound;

    public TimerThread(int count, int secondsPerRound){
        this.counter = count;
        this.secondsPerRound = secondsPerRound;
    }

    @Override
    public void run() {
        int fileCount = 0;
        Random rnd = new Random();
        while(true) {
            double learningRate = rnd.nextDouble();
            double gamma = rnd.nextDouble();
            ArrayList<Integer> a = new ArrayList<Integer>();
            int count = 0;
            while (count < counter) {
                int seconds = 0;
                count++;
                GameEngine.start(learningRate,gamma);
                boolean running = true;
                while (running) {
                    if (seconds >= secondsPerRound) {
                        a.add(GameEngine.getMapCount());
                        GameEngine.gameEngine.stop();
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        running = false;
                    }
                    seconds++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                fileCount++;
                writeToFile(a, learningRate,gamma,fileCount);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeToFile(ArrayList<Integer> a, double learningRate, double gamma, int fileCount) throws IOException {
        File f = new File("C:\\Users\\Yanick$\\Desktop\\MArio.txt");
        if (!f.exists()) {
            f.createNewFile();
        }
        BufferedWriter w = new BufferedWriter(new FileWriter("C:\\Users\\Yanick$\\Desktop\\MArio\\MArio" + fileCount + " .txt"));
        w.write("---------------------");
        w.write("LearningRate: " + learningRate + ", DiscountRate: " + gamma);
        w.write("---------------------");
        w.newLine();
        for (int i = 0; i < a.size(); i++) {
            w.write("Neural Network " + i + ": Managed to complete " + a.get(i) + " levels!");
            w.newLine();
        }
        w.close();
    }
}
