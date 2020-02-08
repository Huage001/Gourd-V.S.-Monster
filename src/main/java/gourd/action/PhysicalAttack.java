package gourd.action;

import gourd.creature.Creature;
import javafx.geometry.Point2D;

import static gourd.Scene.*;
import static gourd.Scene.realCoordinate;
import static gourd.view.PlayView.*;
import static gourd.view.PlayView.board;
import static gourd.view.PlayView.bulletinBoard;
import static gourd.view.PlayView.tryToGetPosition;
import static java.lang.Thread.sleep;

public class PhysicalAttack extends Technique{

    public PhysicalAttack(Creature creature) {
        super("普通攻击", new Point2D(1,1),creature,0,0);
    }
    static final long time=1000;
    static final long interval=20;
    static final double height=-200;

    private double a,b,c,deltaX;

    @Override
    public void run() {
        setStart();
        setTarget();
        setArg();
        synchronized (board) {
            board[(int) start.getX()][(int) start.getY()] = null;
        }
        resetCurrentPoint(false);
        fly();
        try {
            sleep(200);
        } catch (InterruptedException e) {
            return;
        }
        doAttack();
        deltaX=-deltaX;
        resetCurrentPoint(true);
        fly();
        resetCurrentPoint(false);
        tryToGetPosition(start,creature);
    }

    private void setArg(){
        Point2D s=realCoordinate[(int) start.getX()][(int) start.getY()];
        Point2D t=realCoordinate[(int) target.getX()][(int) target.getY()];
        double p=Math.pow(s.getX()-t.getX(),2);
        a=-4*height/p;
        b=((s.getX()-t.getX())*(s.getY()-t.getY())+4*height*(s.getX()+t.getX()))/p;
        c=((s.getX()-t.getX())*(s.getX()*t.getY()-t.getX()*s.getY())-4*height*s.getX()*t.getX())/p;
        deltaX=(t.getX()-s.getX())*interval/time;
    }

    private void updateCurrentPoint(){
        double x=currentPoint.getX()+deltaX;
        double y=a*x*x+b*x+c;
        currentPoint=new Point2D(x,y);
    }

    private void fly(){
        if(start.equals(target)){
            try {
                sleep(time);
            } catch (InterruptedException e) {
                return;
            }
        }else{
            for(long t=0;t<time;t+=interval){
                long startTime=System.currentTimeMillis();
                updateCurrentPoint();
                creature.setRealPosition(currentPoint);
                long sleepTime=interval-System.currentTimeMillis()+startTime;
                if(sleepTime>0){
                    try {
                        sleep(sleepTime);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        }
    }

    private void doAttack(){
        Creature targetCreature=board[(int) target.getX()][(int) target.getY()];
        if(targetCreature!=null){
            int hurt=creature.getPhysicalAttack()-targetCreature.getPhysicalDefence();
            //System.out.println("Hurt: "+hurt);
            bulletinBoard.addBulletin("伤害："+hurt);
            targetCreature.subtractHealth(hurt);
        }else{
            //System.out.println("Miss!");
            bulletinBoard.addBulletin("Miss!");
        }
    }
}
