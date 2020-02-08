package gourd.action;

import gourd.creature.Creature;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import static frame.Framework.app;
import static gourd.Scene.realCoordinate;
import static gourd.view.PlayView.*;
import static java.lang.Thread.sleep;

public class HorizontalMagic extends Technique{
    Image image;
    int cnt;
    int range;//0：打一行 1：打一列 2：AOE
    int imageHeight;
    int imageWidth;

    public HorizontalMagic(Image image,String name, Creature creature,
                           int hurt, int blue,int cnt,int width,int height,int range) {
        super(name,new Point2D(1,1),creature,hurt,blue);
        this.image=image;
        this.cnt=cnt;
        imageView=new ImageView(image);
        imageView.setVisible(false);
        app.getCurrentView().getChildren().add(imageView);
        this.imageHeight=height;
        this.imageWidth=width;
        this.range=range;
    }
    public HorizontalMagic(Image image,String name, Creature creature, int hurt, int blue,int cnt,int range){
        this(image,name,creature,hurt,blue,cnt,100,100,range);
    }

    static final long time=1000;

    private void doHurt(int x,int y){
        Creature targetCreature = board[x][y];
        if (targetCreature != null) {
            if(hurt>=0) {
                int realHurt = hurt + creature.getMagicalAttack() - targetCreature.getMagicalDefence();
                bulletinBoard.addBulletin("伤害：" + realHurt);
                targetCreature.subtractHealth(realHurt);
                targetCreature.goBack();
            }else{
                int recover=-this.hurt;
                bulletinBoard.addBulletin("回血："+recover);
                targetCreature.addHealth(recover);
            }
        }
    }

    private void doAttack(){
        int base=0;
        if((int)target.getX()>=4){
            base=4;
        }
        if(range==0) {
            for (int i = base; i < base + 4; i++) {
                doHurt(i,(int)target.getY());
            }
        }else if(range==1){
            for(int i=0;i<4;i++){
                doHurt((int)target.getX(),i);
            }
        }else{
            for(int i=base;i<base+4;i++){
                for(int j=0;j<4;j++){
                    doHurt(i,j);
                }
            }
        }
    }

    @Override
    public void run() {
        bulletinBoard.addBulletin(name+"！");
        setStart();
        setTarget();
        resetCurrentPoint(false);

        KeyValue xValueEnd,yValueEnd,xValueStart,yValueStart;

        imageView.opacityProperty().setValue(0.7);
        imageView.setFitHeight(imageHeight);
        imageView.setFitWidth(imageWidth);
        if(range==0) {
            imageView.translateXProperty().setValue(realCoordinate[(int) start.getX()][(int) start.getY()].getX());
            imageView.translateYProperty().setValue(realCoordinate[(int) start.getX()][(int) start.getY()].getY());
            xValueEnd=new KeyValue(imageView.translateXProperty(),
                    realCoordinate[(int)target.getX()][(int)target.getY()].getX());
            yValueEnd=new KeyValue(imageView.translateYProperty(),
                    realCoordinate[(int)target.getX()][(int)target.getY()].getY());
            xValueStart=new KeyValue(imageView.translateXProperty(),
                    realCoordinate[(int)start.getX()][(int)start.getY()].getX());
            yValueStart=new KeyValue(imageView.translateYProperty(),
                    realCoordinate[(int)start.getX()][(int)start.getY()].getY());
        }else if(range==1){
            imageView.translateXProperty().setValue(realCoordinate[(int)target.getX()][1].getX());
            imageView.translateYProperty().setValue(-200);
            xValueEnd=new KeyValue(imageView.translateXProperty(),
                    realCoordinate[(int)target.getX()][1].getX());
            yValueEnd=new KeyValue(imageView.translateYProperty(), 250);
            xValueStart=new KeyValue(imageView.translateXProperty(),
                    realCoordinate[(int)target.getX()][(int)target.getY()].getX());
            yValueStart=new KeyValue(imageView.translateYProperty(),-200);
        }else{
            if(target.getX()>=4) {
                imageView.translateXProperty().setValue(250);
                xValueStart=new KeyValue(imageView.translateXProperty(),250);
                xValueEnd=new KeyValue(imageView.translateXProperty(),250);
            }else{
                imageView.translateXProperty().setValue(-250);
                xValueStart=new KeyValue(imageView.translateXProperty(),-250);
                xValueEnd=new KeyValue(imageView.translateXProperty(),-250);
            }
            imageView.translateYProperty().setValue(-200);
            yValueEnd=new KeyValue(imageView.translateYProperty(), 250);
            yValueStart=new KeyValue(imageView.translateYProperty(), -200);
        }

        timeline=new Timeline();
        timeline.setCycleCount(cnt);
        timeline.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, xValueStart,yValueStart),
                new KeyFrame(Duration.millis(time), actionEvent -> doAttack(),xValueEnd,yValueEnd));
        timeline.onFinishedProperty().setValue(actionEvent -> imageView.setVisible(false));
        addTask(this);
        try {
            sleep(200);
        } catch (InterruptedException ignored) {}
    }

}
