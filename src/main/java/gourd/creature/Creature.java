package gourd.creature;

import gourd.Choice;
import gourd.Records;
import gourd.action.BasicMove;
import gourd.action.PhysicalAttack;
import gourd.action.Technique;
import gourd.view.PlayView;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static gourd.Scene.*;
import static gourd.view.PlayView.*;
import static java.lang.Thread.*;


public class Creature implements Runnable {

    public static final Point2D[] defaultArrange = new Point2D[11];
    public static final double barHeight = 10;
    public static final double barWidth = 80;
    public static final double barIntervalX = 140;
    public static final double barIntervalY = 100;
    public static final double barBaseX = -400;
    public static final double barBaseY = -310;
    public static final double progressionX = 400;
    public static final double progressionY = 8;
    public static final double progressionBaseX = 310;
    public static final double progressionBaseY = -315;
    public static final double headRealWidth = 40;
    public static final double headRealHeight = 40;
    public static final double miniRealWidth = 16;
    public static final double miniRealHeight = 16;

    static final long TimePerFrame = 90;//every frame would need 30ms

    static {
        defaultArrange[0] = new Point2D(4, 0);
        defaultArrange[1] = new Point2D(5, 1);
        defaultArrange[2] = new Point2D(4, 1);
        defaultArrange[3] = new Point2D(4, 2);
        defaultArrange[4] = new Point2D(5, 2);
        defaultArrange[5] = new Point2D(4, 3);
        defaultArrange[6] = new Point2D(6, 3);
        defaultArrange[7] = new Point2D(2, 1);
        defaultArrange[8] = new Point2D(1, 1);
        defaultArrange[9] = new Point2D(2, 2);
        defaultArrange[10] = new Point2D(1, 2);
    }

    //Some attributes that stay the same for most cases
    int order;
    int health;
    int maxHealth;
    int vigour;
    int maxVigour;
    double speed;
    int mapPositionX;

    public synchronized void setMapPositionX(int mapPositionX) {
        this.mapPositionX = mapPositionX;
    }

    public synchronized void setMapPositionY(int mapPositionY) {
        this.mapPositionY = mapPositionY;
    }

    int mapPositionY;
    int physicalAttack;
    int magicalAttack;
    int physicalDefence;
    int magicalDefence;
    Image model;
    Image head;
    double modelWidth;
    double modelHeight;
    double positionBiasX;
    double positionBiasY;
    double headWidth;
    double headHeight;
    double miniWidth;
    double miniHeight;
    boolean isEnemy;
    boolean isAI;
    double initProgress;
    Thread thread;
    List<Technique> techniques;
    PhysicalAttack attack;
    BasicMove basicMove;

    //Some attributes that can only be changed in class
    double progress;

    //Some attributes that view module need to set
    ImageView imageView;
    ImageView headView;
    ImageView miniView;
    Rectangle red;
    Rectangle blue;
    Rectangle blankRed;
    Rectangle blankBlue;
    volatile State state;
    volatile Choice choice;
    volatile int index;//only work in magic or thing choice
    volatile Point2D targetPoint;
    volatile boolean suspended;
    volatile Point2D realPosition;

    public synchronized int getIndex(){
        return index;
    }

    public synchronized void setIndex(int index){
        this.index=index;
    }

    public synchronized Choice getChoice(){
        return choice;
    }

    public synchronized void setChoice(Choice choice){
        this.choice=choice;
    }

    public synchronized Point2D getTargetPoint(){
        return targetPoint;
    }

    public synchronized void setTargetPoint(Point2D targetPoint){
        this.targetPoint=targetPoint;
    }

    public synchronized Thread getThread() {
        return thread;
    }

    public synchronized State getState(){
        return state;
    }

    public synchronized void setState(State state){
        this.state=state;
    }

    public Rectangle getRed() {
        return red;
    }

    public Rectangle getBlue() {
        return blue;
    }

    public Rectangle getBlankRed() {
        return blankRed;
    }

    public Rectangle getBlankBlue() {
        return blankBlue;
    }

    public Image getHead() {
        return head;
    }

    public ImageView getHeadView() {
        return headView;
    }

    public synchronized ImageView getMiniView() {
        return miniView;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public synchronized void setIsAI(boolean isAI){
        this.isAI=isAI;
    }

    public double getModelWidth() {
        return modelWidth;
    }

    public double getModelHeight() {
        return modelHeight;
    }

    public synchronized int getHealth() {
        return health;
    }

    public synchronized int getMaxHealth() {
        return maxHealth;
    }

    public synchronized void subtractHealth(int delta){
        this.health=Math.max(this.health-delta,0);
        if(this.health<=0){
            die();
        }
        this.addVigour(5);
    }

    public synchronized void addHealth(int delta){
        this.health=Math.min(this.maxHealth,this.health+delta);
    }

    public synchronized void subtractVigour(int delta){
        this.vigour-=delta;
    }

    public synchronized void addVigour(int delta){
        this.vigour=Math.min(this.maxVigour,this.vigour+delta);
    }

    public int getVigour() {
        return vigour;
    }

    public int getMaxVigour() {
        return maxVigour;
    }

    public double getSpeed() {
        return speed;
    }

    public synchronized int getMapPositionX() {
        return mapPositionX;
    }

    public synchronized int getMapPositionY() {
        return mapPositionY;
    }

    public synchronized Point2D getRealPosition() {
        return realPosition;
    }

    public synchronized void setRealPosition(Point2D point){
        Point2D bias=new Point2D(modelWidth/2,-modelHeight/2);
        this.realPosition=point.add(bias);
    }

    public long lastGoBack;

    public synchronized void goBack(){
        if(getState()==State.PROGRESSION) {
//            if (this.mapPositionX < 4) {
//                this.realPosition.add(-20, 0);
//            } else {
//                this.realPosition.add(20, 0);
//            }
            lastGoBack = System.currentTimeMillis();
        }
    }

    public synchronized void setRedAndBlue(){
        double healthPercent=(double)getHealth()/getMaxHealth();
        double vigourPercent=(double)getVigour()/getMaxVigour();
        red.widthProperty().setValue(healthPercent*barWidth);
        blue.widthProperty().setValue(vigourPercent*barWidth);
        red.translateXProperty().setValue(barBaseX+(healthPercent-1)*barWidth/2 + order * barIntervalX);
        blue.translateXProperty().setValue(barBaseX+(vigourPercent-1)*barWidth/2 + order * barIntervalX);
        blankRed.widthProperty().setValue((1-healthPercent)*barWidth);
        blankBlue.widthProperty().setValue((1-vigourPercent)*barWidth);
        blankRed.translateXProperty().setValue(barBaseX+healthPercent*barWidth/2+order*barIntervalX);
        blankBlue.translateXProperty().setValue(barBaseX+vigourPercent*barWidth/2+order*barIntervalX);
    }

    public Image getModel() {
        return model;
    }

    public synchronized int getPhysicalAttack() {
        return physicalAttack;
    }

    public synchronized int getMagicalAttack() {
        return magicalAttack;
    }

    public synchronized int getPhysicalDefence() {
        return physicalDefence;
    }

    public synchronized int getMagicalDefence() {
        return magicalDefence;
    }

    public double getProgress() {
        return progress;
    }

    public synchronized ImageView getImageView() {
        return imageView;
    }

    public double getHeadWidth() {
        return headWidth;
    }

    public double getHeadHeight() {
        return headHeight;
    }

    public double getMiniWidth() {
        return miniWidth;
    }

    public double getMiniHeight() {
        return miniHeight;
    }

    public double getInitProgress() {
        return initProgress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public synchronized void setMiniView() {
        if(thread.isAlive()) {
            miniView.translateXProperty().setValue(getInitProgress() + Creature.progressionX * getProgress() / 100);
        }
    }

    public synchronized void setImageView(){
        if(thread.isAlive()){
            Point2D currentPoint=getRealPosition();
            imageView.translateXProperty().setValue(currentPoint.getX()+positionBiasX);
            imageView.translateYProperty().setValue(currentPoint.getY()+positionBiasY);
        }
    }

    public PhysicalAttack getAttack() {
        return attack;
    }

    public BasicMove getBasicMove(){
        return basicMove;
    }

    public long getLastGoBack(){
        return lastGoBack;
    }

    public void resetLastGoBack(){
        lastGoBack=-1;
    }

    String fileName;
    Records records;
    Thread executeThread;

    public Creature(int mapX, int mapY, boolean isEnemy, int order) {
        this.order=order;
        state = State.PROGRESSION;
        this.isEnemy = isEnemy;
        isAI = isEnemy;
        lastGoBack=-1;

        mapPositionX = mapX;
        mapPositionY = mapY;

        if(isPlayBack) {
            this.fileName = "./save/" + PlayView.fileName.substring(0, PlayView.fileName.lastIndexOf('.'))
                    + "/" + PlayView.fileName.substring(0, PlayView.fileName.lastIndexOf('.'))
                    + "_" + isEnemy + "_" + order;
        }else{
            this.fileName="./save/" + PlayView.fileName+ "/" + PlayView.fileName+ "_" + isEnemy + "_" + order;
        }
        if(isPlayBack){
            records=new Records(this.fileName);
        }else{
            records=new Records();
        }

        progress = 0;

        red = new Rectangle(barWidth, barHeight, Color.RED);
        red.setStroke(Color.YELLOW);
        red.translateXProperty().setValue(barBaseX + order * barIntervalX);
        red.translateYProperty().setValue(isEnemy ? barBaseY + barIntervalY : barBaseY - barHeight / 2);
        blankRed = new Rectangle(0, barHeight, Color.WHITE);
        blankRed.setStroke(Color.YELLOW);
        blankRed.translateXProperty().setValue(barBaseX+barWidth/2 + order * barIntervalX);
        blankRed.translateYProperty().setValue(isEnemy ? barBaseY + barIntervalY : barBaseY - barHeight / 2);

        blue = new Rectangle(barWidth, barHeight, Color.BLUE);
        blue.setStroke(Color.YELLOW);
        blue.translateXProperty().setValue(barBaseX + order * barIntervalX);
        blue.translateYProperty().setValue(isEnemy ? barBaseY + 2 * barHeight + barIntervalY : barBaseY + 2 * barHeight);
        blankBlue = new Rectangle(0, barHeight, Color.WHITE);
        blankBlue.setStroke(Color.YELLOW);
        blankBlue.translateXProperty().setValue(barBaseX+barWidth/2 + order * barIntervalX);
        blankBlue.translateYProperty().setValue(isEnemy ? barBaseY + 2 * barHeight + barIntervalY : barBaseY + 2 * barHeight);


        techniques = Collections.synchronizedList(new ArrayList<Technique>());
        attack = new PhysicalAttack(this);
        basicMove=new BasicMove(this);
        techniques.add(attack);

        thread = new Thread(this);
        suspended = true;

        choice=Choice.ATTACK;
        index=-1;
        targetPoint=new Point2D(0,0);
    }

    public synchronized List<Technique> getTechniques() {
        return techniques;
    }

    public synchronized boolean isSuspended() {
        return suspended;
    }

    public synchronized void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public boolean isAI() {
        return isAI;
    }

    public synchronized void die(){
        if(getThread().isAlive()) {
            //bulletinBoard.addBulletin("Die!");
            health=0;
            getThread().interrupt();
            synchronized (board) {
                board[getMapPositionX()][getMapPositionY()] = null;
            }
        }
    }

    public void save(){
        if(!isPlayBack){
            records.writeFile(this.fileName);
        }
    }

    public void run() {

        while(getHealth()>0) {

            while(isSuspended()){ }

            long startTime = System.currentTimeMillis();
            boolean finish = false;

            if(getState()==State.PROGRESSION) {
                setProgress(getProgress() + getSpeed() * TimePerFrame / 1000);
                if (getProgress() >= 100) {
                    finish = true;
                    setProgress(0);
                }
            }

            if(finish){
                if(isPlayBack){
                    Records.Record record=records.getNext();
                    if(record==null){
                        break;
                    }
                    setChoice(Choice.find(record.choice));
                    if(getChoice()==Choice.MAGIC){
                        subtractVigour(techniques.get(record.index).blue);
                    }
                    setTargetPoint(new Point2D(record.x,record.y));
                    setIndex(record.index);
                    try {
                        sleep(record.time);
                    }catch (InterruptedException e){
                        break;
                    }
                }else {
                    long timeStart=System.currentTimeMillis();
                    if (!isAI()) {
                        setState(State.INTERACTION);

                        //wait for user control
                        try {
                            semaphore.acquire();
                        } catch (InterruptedException e) {
                            break;
                        }

                        setCurrentCreature(this);

                        //wait for user selection
                        synchronized (this) {
                            try {
                                wait();
                                semaphore.release();
                            } catch (InterruptedException e) {
                                semaphore.release();
                                break;
                            }
                        }
                    } else {
                        setChoice(Choice.MAGIC);
                        boolean isHurt=true;
                        if(techniques.size()<=1||getVigour()<techniques.get(1).blue){
                            setIndex(0);
                        }else{
                            setIndex(1);
                            subtractVigour(techniques.get(1).blue);
                            if(techniques.get(1).hurt<0){
                                isHurt=false;
                            }
                        }
                        int left;
                        if (isEnemy^isHurt) {
                            left = 4;
                        } else {
                            left = 0;
                        }
                        boolean canFind = false;
                        for (int i = 0; i < sceneX / 2; i++) {
                            for (int j = 0; j < sceneY; j++) {
                                if (board[left + i][j] != null) {
                                    setTargetPoint(new Point2D(i + left, j));
                                    canFind = true;
                                    break;
                                }
                            }
                            if (canFind) {
                                break;
                            }
                        }
                        if (!canFind) {
                            setTargetPoint(new Point2D(left, 0));
                        }
                    }

                    records.addRecord(System.currentTimeMillis()-timeStart,
                            getChoice(),getIndex(),getTargetPoint());
                }

                state=State.EXECUTION;
                if(this.health>0) {
                    execute();
                }
                state=State.PROGRESSION;

            }else {

                long sleepTime = TimePerFrame + startTime - System.currentTimeMillis();
                try {
                    yield();
                    sleep(Math.max(20, sleepTime));
                } catch (InterruptedException e) {
                    break;
                }
            }
        }

        die();

    }

    protected void execute() {
        switch(getChoice()){
            case AI:
                setIsAI(true);
                break;
            case KILL:
                subtractHealth(32767);
                break;
            case SKIP:
                break;
            case ATTACK:
                executeThread=new Thread(getAttack());
                executeThread.start();
                try {
                    executeThread.join();
                }catch(InterruptedException e){
                    return;
                }
                break;
            case MAGIC:
                executeThread=new Thread(techniques.get(getIndex()));
                executeThread.start();
                try {
                    executeThread.join();
                }catch(InterruptedException e){
                    return;
                }
                break;
            case CHANGE_POSITION:
                executeThread=new Thread(getBasicMove());
                executeThread.start();
                try{
                    executeThread.join();
                }catch(InterruptedException e){
                    return;
                }
                break;
            case THING:
                things.get(getIndex()).imageView.setVisible(true);
                things.get(getIndex()).time=System.currentTimeMillis();
                switch(getIndex()){
                    case 0:
                        bulletinBoard.addBulletin("可口可乐！");
                        bulletinBoard.addBulletin("真气+15");
                        addVigour(15);
                        break;
                    case 1:
                        bulletinBoard.addBulletin("红牛！");
                        bulletinBoard.addBulletin("物理攻击提升！");
                        physicalAttack=30;
                        break;
                    case 2:
                        bulletinBoard.addBulletin("仙葫芦！");
                        bulletinBoard.addBulletin("法术攻击提升！");
                        magicalAttack=30;
                        break;
                    case 3:
                        bulletinBoard.addBulletin("羽绒服！");
                        bulletinBoard.addBulletin("物理防御提升！");
                        physicalDefence=20;
                        break;
                    case 4:
                        bulletinBoard.addBulletin("魔女斗篷！");
                        bulletinBoard.addBulletin("法术防御提升！");
                        magicalDefence=20;
                        break;
                }
                break;
        }
    }
}