package me.CowGoesMOOOO.main;

import com.TETOSOFT.tilegame.GameEngine;
import com.TETOSOFT.tilegame.TileMap;
import com.TETOSOFT.tilegame.sprites.Player;

public class Organizer implements Runnable {

    private NeuralNet dummyNet;
    private NeuralNet network;
    private NeuralNet 

    public Organizer(){
        dummyNet = new NeuralNet(new int[]{6,12,24,12,6});
        network = new NeuralNet(new int[]{6,12,48,36,6});
    }

    @Override
    public void run() {
        GameEngine engine = GameEngine.getInstance();

        while(engine.isRunning()){
            TileMap map = engine.getMap();
            Player player = (Player)map.getPlayer();

            float playerX = player.getX();
            float playerY = player.getY();


        }
    }
}
