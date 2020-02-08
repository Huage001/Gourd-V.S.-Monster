package gourd;

import javafx.scene.control.TextArea;

import java.util.LinkedList;
import java.util.Queue;

import static java.lang.Thread.sleep;

public class BulletinBoard implements Runnable{

    static final int maxSize=5;
    static final long timeWait=30;
    static final long bulletinHoldMax=3000;
    static final double posX=0.0;
    static final double posY=-100.0;
    static final double width=175.0;
    static final double height=135.0;
    static class Bulletin{
        String msg;
        long time;
        Bulletin(String msg,long time){
            this.msg=msg;
            this.time=time;
        }
    }
    Queue<Bulletin> bulletins;
    public TextArea textArea;

    public BulletinBoard(){
        bulletins=new LinkedList<>();
        textArea=new TextArea();
        textArea.setVisible(false);
        textArea.setTranslateX(posX);
        textArea.setTranslateY(posY);
        textArea.setMaxSize(width,height);
        textArea.setStyle("-fx-background-color:orange; -fx-border-color:pink; -fx-text-fill:black;" +
                "-fx-font-size: 20; -fx-font-weight:bold; -fx-text-alignment:center;");
    }

    public synchronized void setBulletinBoard(){
        synchronized (bulletins){
            if(!bulletins.isEmpty()) {
                String temp= "";
                textArea.setVisible(true);
                for (Bulletin bulletin : bulletins) {
                    temp = temp.concat(bulletin.msg + "\n");
                }
                textArea.setText(temp);
            }else{
                textArea.setVisible(false);
            }
        }
    }

    public synchronized void addBulletin(String msg){
        synchronized (bulletins) {
            while (bulletins.size() >= maxSize) {
                bulletins.poll();
            }
            Bulletin bulletin = new Bulletin(msg, System.currentTimeMillis());
            bulletins.offer(bulletin);
        }
    }

    @Override
    public void run() {
        while(true){
            synchronized (bulletins) {
                while (!bulletins.isEmpty()) {
                    if (System.currentTimeMillis() - bulletins.element().time > bulletinHoldMax) {
                        bulletins.poll();
                    }else{
                        break;
                    }
                }
            }
            try {
                sleep(timeWait);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
