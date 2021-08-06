package me.CowGoesMOOOO.main;

import com.TETOSOFT.graphics.Sprite;
import com.TETOSOFT.tilegame.GameEngine;
import com.TETOSOFT.tilegame.TileMap;
import com.TETOSOFT.tilegame.TileMapDrawer;
import com.TETOSOFT.tilegame.sprites.Player;
import me.CowGoesMOOOO.helper.Matrix;
import me.CowGoesMOOOO.helper.exceptions.DimensionMismatchException;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Random;

public class Organizer implements Runnable {

    private final NeuralNet dummyNet;
    private final NeuralNet network;
    private final NeuralNet actionNet;

    public Organizer(){
        dummyNet = new NeuralNet(new int[]{81,120,70,60,81});
        network = new NeuralNet(new int[]{81,130,150,130,81});
        actionNet = new NeuralNet(new int[]{81,60,40,20,10,3});
    }

    @Override
    public void run() {
        GameEngine engine = GameEngine.getInstance();

        while(engine.isRunning()){
            TileMap map = engine.getMap();
            Player player = (Player)map.getPlayer();

            int tilePlayerX = TileMapDrawer.pixelsToTiles(player.getX());
            int tilePlayerY = TileMapDrawer.pixelsToTiles(player.getY());

            //Initializing the map into a matrix
            Matrix mapMatrix = new Matrix(81, 1);
            int counter = 0;

            for (int y=Math.round(tilePlayerY) - 4; y <=  Math.round(tilePlayerY) + 4; y++) {
                for (int x=Math.round(tilePlayerX) - 4; x <= Math.round(tilePlayerX) + 4; x++) {

                    Image image = map.getTile(x, y);

                    if (image != null) {
                        mapMatrix.getMatrix()[counter][0] = 1;
                    } else {
                        mapMatrix.getMatrix()[counter][0] = 0;
                    }

                    Iterator i = map.getSprites();
                    while(i.hasNext()){
                        Sprite next = (Sprite)i.next();
                        if(next.getX() == x && next.getY() == y){
                            mapMatrix.getMatrix()[counter][0] =-0.5;
                        }
                    }

                    counter++;
                }
            }

            // Neural Network stuff
            try {
                Matrix actionMatrix = actionNet.predict(mapMatrix);
                double[][] actions = actionMatrix.getMatrix();
                Robot robot = new Robot();

                if(actions[0][0] > actions[1][0] && actions[0][0] > actions[2][0]){
                    robot.keyPress(KeyEvent.VK_RIGHT);
                    robot.keyRelease(KeyEvent.VK_RIGHT);
                } else if(actions[0][0] < actions[1][0] && actions[0][0] >= actions[2][0]){
                    robot.keyPress(KeyEvent.VK_LEFT);
                    robot.keyRelease(KeyEvent.VK_LEFT);
                } else if(actions[0][0] < actions[1][0] && actions[1][0] < actions[2][0]){
                    robot.keyPress(KeyEvent.VK_SPACE);
                    robot.keyRelease(KeyEvent.VK_SPACE);
                } else {
                    System.out.println("random move");
                    switch(new Random().nextInt(2)){
                        case 0:
                            robot.keyPress(KeyEvent.VK_RIGHT);
                            robot.keyRelease(KeyEvent.VK_RIGHT);
                            break;
                        case 1:
                            robot.keyPress(KeyEvent.VK_LEFT);
                            robot.keyRelease(KeyEvent.VK_LEFT);
                            break;
                        case 2:
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

                double difference = 0;

                for(int i = 0; i < passThrough.getRow(); i++){
                    double d = passThrough.getMatrix()[i][0] + predictedMatrix.getMatrix()[i][0];
                    if(d < 0){
                        d *= -1;
                    }
                    difference += d;
                }

                // todo: actionNet train with d

            }catch (DimensionMismatchException | AWTException e){
                e.printStackTrace();
            }


        }
    }
}
