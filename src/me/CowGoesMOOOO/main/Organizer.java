package me.CowGoesMOOOO.main;

import com.TETOSOFT.graphics.Sprite;
import com.TETOSOFT.tilegame.GameEngine;
import com.TETOSOFT.tilegame.TileMap;
import com.TETOSOFT.tilegame.TileMapDrawer;
import com.TETOSOFT.tilegame.sprites.Player;
import me.CowGoesMOOOO.helper.Action;
import me.CowGoesMOOOO.helper.Matrix;
import me.CowGoesMOOOO.helper.MatrixMath;
import me.CowGoesMOOOO.helper.exceptions.DimensionMismatchException;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.SQLSyntaxErrorException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import static me.CowGoesMOOOO.helper.Action.*;

public class Organizer implements Runnable {

    private final NeuralNet dummyNet;
    private final NeuralNet network;
    private final QMatrix qMatrix;
    public static float ranX;
    public static float highestX;
    private int currentState;
    private GameEngine engine;

    /**
     * Initializing the Organizer
     */
    public Organizer(){
        dummyNet = new NeuralNet(new int[]{25,120,200,300,100,25});
        network = new NeuralNet(new int[]{25,120,200,300,100,25});
        qMatrix = new QMatrix(3, 3, 0.2,0.7);
    }

    /**
     * Initializing the Organizer with given alpha and gamma
     * @param learningRate Alpha for the Q-Matrix to use
     * @param gamma Gamma for the Q-Matrix to use
     */
    public Organizer(double learningRate, double gamma){
        dummyNet = new NeuralNet(new int[]{25,120,70,60,25});
        network = new NeuralNet(new int[]{25,120,70,60,25});
        qMatrix = new QMatrix(3, 3, learningRate,gamma);
    }

    /**
     * Overriding the run Method of the Thread
     */
    @Override
    public void run() {

        // Initializing variables
        ranX = 0;
        engine = GameEngine.getInstance();
        highestX = 0;

        while(engine.isRunning()){
            Matrix mapMatrix = createMapMatrix();

            // Neural Network algorithm
            try{

                // Finding the current state
                if(inFrontOfBlock()){
                    currentState = 1;
                } else {
                    currentState = 0;
                }

                // Choosing and then executing the player action
                double[] actions = qMatrix.getMatrix().getMatrix()[currentState];
                Robot robot = new Robot();
                Action a = null;
                if(actions[0] > actions[1] && actions[0] > actions[2]){
                    a = RIGHT;
                    robot.keyPress(KeyEvent.VK_RIGHT);
                    robot.keyRelease(KeyEvent.VK_RIGHT);
                } else if(actions[0] < actions[1] && actions[0] >= actions[2]){
                    a = LEFT;
                    robot.keyPress(KeyEvent.VK_LEFT);
                    robot.keyRelease(KeyEvent.VK_LEFT);
                } else if(actions[0] < actions[1] && actions[1] < actions[2]){
                    a = UP;
                    robot.keyPress(KeyEvent.VK_SPACE);
                    robot.keyRelease(KeyEvent.VK_SPACE);
                } else {
                    switch(new Random().nextInt(3)){
                        case 0:
                            a = RIGHT;
                            robot.keyPress(KeyEvent.VK_RIGHT);
                            robot.keyRelease(KeyEvent.VK_RIGHT);
                            break;
                        case 1:
                            a = LEFT;
                            robot.keyPress(KeyEvent.VK_LEFT);
                            robot.keyRelease(KeyEvent.VK_LEFT);
                            break;
                        case 2:
                            a = UP;
                            robot.keyPress(KeyEvent.VK_SPACE);
                            robot.keyRelease(KeyEvent.VK_SPACE);
                            break;
                        default:
                            break;
                    }
                }
                
                // Calculating the internal reward
                Matrix passThrough = dummyNet.predict(mapMatrix);
                Matrix predictedMatrix = network.predict(mapMatrix);
                network.trainBackprop(mapMatrix, passThrough);
                double internalReward = 0;
                Matrix rewardMatrix = MatrixMath.matrixSub(passThrough, predictedMatrix);
                for(int i = 0; i < rewardMatrix.getRow(); i++) {
                    internalReward += rewardMatrix.getMatrix()[i][0] * rewardMatrix.getMatrix()[i][0];
                }

                // Calculating the external reward
                float playerX = engine.getMap().getPlayer().getX();
                double externalReward = (playerX - highestX);

                // Adjusting the max X
                if(highestX < playerX) {
                    highestX = playerX;
                    ranX += highestX - playerX;
                }

                // Finding the next state of the player
                int nextState = inFrontOfBlock() == true ? 1 : 0;

                // Adjusting the Q-Matrix
                qMatrix.train(currentState, nextState, a.getNumber(), internalReward + externalReward/10);
            }catch (DimensionMismatchException | AWTException e){
                e.printStackTrace();
             }
        }
    }

    /**
     * Calculating a Matrix for the current enviroment of the player
     * @return
     */
    private Matrix createMapMatrix(){

        // Creating the instances for the method to use
        TileMap map = engine.getMap();
        Player player = (Player)map.getPlayer();
        int tilePlayerX = TileMapDrawer.pixelsToTiles(player.getX());
        int tilePlayerY = TileMapDrawer.pixelsToTiles(player.getY());

        // Creating the matrix to return
        Matrix mapMatrix = new Matrix(25, 1);

        // Casting the map into the vector with the nested for loop
        int counter = 0;
        for (int y=Math.round(tilePlayerY) - 2; y <  Math.round(tilePlayerY) + 2; y++) {
            for (int x=Math.round(tilePlayerX) - 2; x <= Math.round(tilePlayerX) + 2; x++) {

                Image image = map.getTile(x, y);

                if (image != null) {
                    mapMatrix.getMatrix()[counter][0] = 0;
                } else {
                    mapMatrix.getMatrix()[counter][0] = 1;
                }

                if(y == tilePlayerY && x == tilePlayerX)
                    mapMatrix.getMatrix()[counter][0] = -1;

            }
        }
        return mapMatrix;
    }

    /**
     * Checking if the player is in front of a block
     * @return True if the player is in front of a block, false if otherwise
     */
    public boolean inFrontOfBlock(){

        // Creating the instances for the method to use
        Player player = (Player)engine.getMap().getPlayer();
        int tilePlayerX = TileMapDrawer.pixelsToTiles(player.getX());
        int tilePlayerY = TileMapDrawer.pixelsToTiles(player.getY());

        // Checking if the player is in front of a block
        if(engine.getMap().getTile(tilePlayerX - 1, tilePlayerY) != null ||
                engine.getMap().getTile(tilePlayerX + 1, tilePlayerY) != null)
            return true;
        return false;
    }
}
