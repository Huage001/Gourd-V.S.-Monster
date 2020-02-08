package gourd.action;

import gourd.creature.Creature;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

import static gourd.Scene.realCoordinate;

public abstract class Technique implements Runnable{
    public Point2D range;
    public String name;
    public int hurt;
    public int blue;
    public ImageView imageView;
    public Timeline timeline;
    protected Point2D start;
    protected Point2D target;
    protected Creature creature;
    protected Point2D currentPoint;
    protected void setStart(){
        this.start=new Point2D(creature.getMapPositionX(),creature.getMapPositionY());
    }
    protected void setTarget(){
        this.target=this.creature.getTargetPoint();
    }
    protected void resetCurrentPoint(boolean isBack){
        if(isBack){
            this.currentPoint=realCoordinate[(int) target.getX()][(int) target.getY()];
        }else {
            this.currentPoint=realCoordinate[(int) start.getX()][(int) start.getY()];
        }
    }
    Technique(String name,Point2D range,Creature creature,int hurt,int blue){
        this.name=name;
        this.range=range;
        this.creature=creature;
        this.hurt=hurt;
        this.blue=blue;
    }
}
