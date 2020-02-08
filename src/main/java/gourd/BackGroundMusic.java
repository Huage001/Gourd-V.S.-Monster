package gourd;

import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class BackGroundMusic implements Runnable {

    public Player mediaPlayer;
    @Override
    public void run() {
        try {
            File file=new File("./src/main/resource/bgm.mp3");
            BufferedInputStream buffer=new BufferedInputStream(new FileInputStream(file));
            mediaPlayer = new Player(buffer);
            mediaPlayer.play();
        } catch (Exception ignored) {
        }
    }
}
