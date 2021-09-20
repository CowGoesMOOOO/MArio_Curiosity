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
    private boolean first = true;
    private float highestX;
    private int currentState;
    private GameEngine engine;
    public Organizer(){
        dummyNet = new NeuralNet(new int[]{25,120,70,60,25});
        network = new NeuralNet(new int[]{25,120,70,60,25});
        qMatrix = new QMatrix(3, 3, 0.97, 0.125);
    }

    @Override
    public void run() {
        engine = GameEngine.getInstance();
        highestX = engine.getMap().getPlayer().getX();
        while(engine.isRunning()){
            Matrix mapMatrix = createMapMatrix();

            // Neural Network stuff
            try{
                if(playerThreat()){
                    currentState = 2;
                }
                currentState = 1;

                if(first) {
                    first = false;
                    currentState = 0;
                }

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
                    System.out.println("random move");
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

                Matrix passThrough = dummyNet.predict(mapMatrix);
                Matrix predictedMatrix = network.predict(mapMatrix);
                network.trainBackprop(mapMatrix, passThrough);

                double ireward = 0;

                Matrix rewardMatrix = MatrixMath.matrixSub(passThrough, predictedMatrix);
                for(int i = 0; i < rewardMatrix.getRow(); i++) {
                    ireward += rewardMatrix.getMatrix()[i][0]*rewardMatrix.getMatrix()[i][0];
                }
                int nextState = playerThreat() == true ? 2 : 1;

                float playerX = engine.getMap().getPlayer().getX();
                double ereward = (playerX - highestX);
                if(playerX > highestX){
                    highestX = playerX;
                }

                qMatrix.train(currentState, nextState, a.getNumber(), ereward + ireward);
                System.out.println(Arrays.deepToString(qMatrix.getMatrix().getMatrix()));
                /* Matrix loss = MatrixMath.matrixAdd((MatrixMath.matrixAdd(reward, MatrixMath.multNumber(actionMatrix,phi))), qMatrix);                                         ;
                actionNet.trainBackprop(mapMatrix , loss); */
            }catch (DimensionMismatchException | AWTException e){
                e.printStackTrace();
             }
        }
    }

    private Matrix createMapMatrix(){
        TileMap map = engine.getMap();
        Player player = (Player)map.getPlayer();

        int tilePlayerX = TileMapDrawer.pixelsToTiles(player.getX());
        int tilePlayerY = TileMapDrawer.pixelsToTiles(player.getY());
        Matrix mapMatrix = new Matrix(25, 1);
        int counter = 0;

        for (int y=Math.round(tilePlayerY) - 2; y <  Math.round(tilePlayerY) + 2; y++) {
            for (int x=Math.round(tilePlayerX) - 2; x <= Math.round(tilePlayerX) + 2; x++) {

                Image image = map.getTile(x, y);

                if (image != null) {
                    mapMatrix.getMatrix()[counter][0] = 0;
                } else {
                    mapMatrix.getMatrix()[counter][0] = 1;
                }

                Iterator i = map.getSprites();
                while(i.hasNext()){
                    Sprite next = (Sprite)i.next();
                    if(Math.round(next.getX()) == x && Math.round(next.getY()) == y){
                        mapMatrix.getMatrix()[counter][0] = 0.5;
                    }
                }

                if(y == Math.round(tilePlayerY) && x == Math.round(tilePlayerX))
                    mapMatrix.getMatrix()[counter][0] = 2;

                counter++;
            }
        }
        return mapMatrix;
    }

    private boolean playerThreat(){
        Matrix map = createMapMatrix();
        for(int i = 0; i < map.getRow(); i++){
            if(map.getMatrix()[i][0] == 0.5)
                return true;
        }
        return false;
    }

}
