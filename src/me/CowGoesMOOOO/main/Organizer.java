package me.CowGoesMOOOO.main;

import com.TETOSOFT.graphics.Sprite;
import com.TETOSOFT.tilegame.GameEngine;
import com.TETOSOFT.tilegame.TileMap;
import com.TETOSOFT.tilegame.TileMapDrawer;
import com.TETOSOFT.tilegame.sprites.Player;
import me.CowGoesMOOOO.helper.Matrix;
import me.CowGoesMOOOO.helper.exceptions.DimensionMismatchException;

import java.awt.*;
import java.util.Iterator;

public class Organizer implements Runnable {

    private NeuralNet dummyNet;
    private NeuralNet network;
    private NeuralNet actionNet;

    private int screenWidth;
    private int screenHeight;

    public Organizer(){
        dummyNet = new NeuralNet(new int[]{81,120,70,60,81});
        network = new NeuralNet(new int[]{81,130,150,130,81});
        actionNet = new NeuralNet(new int[]{81,60,40,20,10,3});
    }

    @Override
    public void run() {
        GameEngine engine = GameEngine.getInstance();
        screenWidth = engine.getScreen().getWidth();
        screenHeight = engine.getScreen().getHeight();

        while(engine.isRunning()){
            TileMap map = engine.getMap();
            Player player = (Player)map.getPlayer();

            float playerX = player.getX();
            float playerY = player.getY();

            int mapWidth = TileMapDrawer.tilesToPixels(map.getWidth());

            int offsetX = screenWidth / 2 -
                    Math.round(player.getX()) - 64;
            offsetX = Math.min(offsetX, 0);
            offsetX = Math.max(offsetX, screenWidth - mapWidth);
            int offsetY = screenHeight -
                    TileMapDrawer.tilesToPixels(map.getHeight());

            int firstTileX = TileMapDrawer.pixelsToTiles(-offsetX);
            int lastTileX = firstTileX +
                    TileMapDrawer.pixelsToTiles(screenWidth) + 1;

            //Initializing the map into a matrix
            Matrix mapMatrix = new Matrix(81, 1);

            for (int y=0; y<map.getHeight(); y++) {
                for (int x=firstTileX; x <= lastTileX; x++) {
                    Image image = map.getTile(x, y);
                    if (image != null) {
                        mapMatrix.getMatrix()[x+(y*9)][0] = 1;
                    } else {
                        mapMatrix.getMatrix()[x+(y*9)][0] = 0;
                    }
                }
            }

            //Initializing the sprites into the matrix
            Iterator iterator = map.getSprites();
            while(iterator.hasNext()){
                Sprite sprite = (Sprite)iterator.next();
                int x = Math.round(sprite.getX()) + offsetX;
                int y = Math.round(sprite.getY()) + offsetY;
                mapMatrix.getMatrix()[x+(y*9)][0] = -1;
            }

            try {
                Matrix passThrough = dummyNet.predict(mapMatrix);
                network.trainBackprop(network.predict(mapMatrix), passThrough);
                
            }catch (DimensionMismatchException e){
                e.printStackTrace();
            }


        }
    }
}
