package me.CowGoesMOOOO.helper;

import com.TETOSOFT.tilegame.GameEngine;
import me.CowGoesMOOOO.main.Organizer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TimerThread implements Runnable{

    private final int maxCount;
    private final int secondsPerRound;

    /**
     * Creates the Timer Thread
     * @param count How many runs you want to execute
     * @param secondsPerRound How long each run should take in seconds
     */
    public TimerThread(int count, int secondsPerRound){
        this.maxCount = count;
        this.secondsPerRound = secondsPerRound;
    }

    /**
     * The overridden run method of the thread.
     * Create
     */
    @Override
    public void run() {

        // Creating the variables
        ArrayList<Integer> a = new ArrayList<Integer>();

        // Creating the while loops for the amount of runs
        int count = 0;
        while (count < maxCount) {

            // Creating the variable for the time loop
            int seconds = 0;
            count++;

            // Starting the game
            GameEngine.start(0.2,0.7);

            // Starting the time loop
            boolean running = true;
            while (running) {

                // Checking if the time is up
                if (seconds >= secondsPerRound) {

                    // Saving the results of the attempt in the arraylist
                    a.add((int)(Organizer.highestX + Organizer.ranX));
                    a.add(GameEngine.getMapCount());

                    // Stopping the game
                    GameEngine.gameEngine.stop();

                    // Waiting for the game to close
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Going out of the while loop
                    running = false;
                }

                // Count up and wait 1 second
                seconds++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // Writing the results to a file in attempts/
        try {
            writeToFile(a);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Writing the results of the attempt to a file.
    public void writeToFile(ArrayList<Integer> a) throws IOException {
        File f = new File(System.getProperty("user.dir") + "\\Mario.txt");
        if(!f.exists()){
            f.createNewFile();
        }
        BufferedWriter w = new BufferedWriter(new FileWriter(f));
        w.write("---------------------");
        w.newLine();
        for (int i = 0; i < a.size(); i += 2) {
            w.write("Neural Network: Managed to walk " + a.get(i) + " (" + a.get(i+1) + " Levels)");
            w.newLine();
        }
        w.close();
        System.exit(0);
    }
}
