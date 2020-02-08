package gourd.view;

import frame.app.View;
import gourd.*;
import gourd.action.Technique;
import gourd.creature.*;
import frame.input.Mouse;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import java.applet.AudioClip;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import static frame.Framework.app;
import static frame.Framework.mouseInput;
import static gourd.Scene.*;
import static java.applet.Applet.newAudioClip;

public class PlayView extends View {

    Scene scene;
    AudioClip soundPlayer;
    Thread musicThread;
    BackGroundMusic backGroundMusic;
    public static Creature[] creatures;
    public static Creature[][] board;
    public static boolean isRunning;
    public static State state;
    public static Creature currentCreature;
    public static Choice choice;
    public static Point2D target;
    public static int index=-1;
    public static Semaphore semaphore;
    public static Button physicalAttackBtn;
    public static Button defendBtn;
    public static Button magicAttackBtn;
    public static Button thingBtn;
    public static Button changePositionBtn;
    public static Button pauseBtn;
    public static Button quitBtn;
    public static Button killBtn;
    public static Button aiBtn;
    public static VBox box;
    public static long finishTime;

    public static Ellipse choiceCircle;
    public static Ellipse targetCircle;

    public static BulletinBoard bulletinBoard;
    static Thread bulletinThread;

    public static boolean isPlayBack;
    public static String fileName;

    public static Queue<Technique> taskList;

    public static List<Thing> things;
    public ImageView resultImageView1,resultImageView2;

    static{
        semaphore=new Semaphore(1);
        
        physicalAttackBtn=new Button("A");
        physicalAttackBtn.setOnAction((event) -> {
            choice = Choice.ATTACK;
            resetBoard();
            setState(State.SELECTION);
        });
        physicalAttackBtn.setTranslateX(boardX);
        physicalAttackBtn.setTranslateY(boardY);
        physicalAttackBtn.setStyle(defaultStyleMajor);
        physicalAttackBtn.setMinSize(buttonMajorR*2,buttonMajorR*2);

        magicAttackBtn=new Button("M");
        magicAttackBtn.setOnAction((event) -> {
            choice = Choice.MAGIC;
            resetBoard();
            setList();
            setState(State.LIST);
        });
        magicAttackBtn.setTranslateX(boardX-2*buttonMajorR-buttonInterval);
        magicAttackBtn.setTranslateY(boardY);
        magicAttackBtn.setStyle(defaultStyleMajor);
        magicAttackBtn.setMinSize(buttonMajorR*2,buttonMajorR*2);

        defendBtn=new Button("S");
        defendBtn.setOnAction((event) -> {
            choice = Choice.SKIP;
            resetBoard();
            setState(State.EXECUTION);
        });
        defendBtn.setTranslateX(boardX+2*buttonMajorR+buttonInterval);
        defendBtn.setTranslateY(boardY);
        defendBtn.setStyle(defaultStyleMajor);
        defendBtn.setMinSize(buttonMajorR*2,buttonMajorR*2);

        thingBtn=new Button("T");
        thingBtn.setOnAction((event) -> {
            choice = Choice.THING;
            resetBoard();
            setThingsList();
            setState(State.LIST);
        });
        thingBtn.setTranslateX(boardX);
        thingBtn.setTranslateY(boardY-2*buttonMajorR-buttonInterval);
        thingBtn.setStyle(defaultStyleMajor);
        thingBtn.setMinSize(buttonMajorR*2,buttonMajorR*2);

        changePositionBtn=new Button("C");
        changePositionBtn.setOnAction((event) -> {
            choice = Choice.CHANGE_POSITION;
            resetBoard();
            setState(State.SELECTION);
        });
        changePositionBtn.setTranslateX(boardX);
        changePositionBtn.setTranslateY(boardY+2*buttonMajorR+buttonInterval);
        changePositionBtn.setStyle(defaultStyleMajor);
        changePositionBtn.setMinSize(buttonMajorR*2,buttonMajorR*2);

        aiBtn=new Button("I");
        aiBtn.setOnAction((event) -> {
            choice = Choice.AI;
            resetBoard();
            setState(State.EXECUTION);
        });
        aiBtn.setTranslateX(boardX-bias);
        aiBtn.setTranslateY(boardY-bias);
        aiBtn.setStyle(defaultStyleMinor);
        aiBtn.setMinSize(buttonMinorR*2,buttonMinorR*2);

        killBtn=new Button("K");
        killBtn.setOnAction((event) -> {
            choice = Choice.KILL;
            resetBoard();
            setState(State.EXECUTION);
        });
        killBtn.setTranslateX(boardX+bias);
        killBtn.setTranslateY(boardY-bias);
        killBtn.setStyle(defaultStyleMinor);
        killBtn.setMinSize(buttonMinorR*2,buttonMinorR*2);

        pauseBtn=new Button("P");
        pauseBtn.setOnAction((event) -> {
            choice = Choice.PAUSE;
            resetBoard();
            setState(State.EXECUTION);
        });
        pauseBtn.setTranslateX(boardX-bias);
        pauseBtn.setTranslateY(boardY+bias);
        pauseBtn.setStyle(defaultStyleMinor);
        pauseBtn.setMinSize(buttonMinorR*2,buttonMinorR*2);

        quitBtn=new Button("Q");
        quitBtn.setOnAction((event) -> {
            choice = Choice.QUIT;
            resetBoard();
            setState(State.EXECUTION);
        });
        quitBtn.setTranslateX(boardX+bias);
        quitBtn.setTranslateY(boardY+bias);
        quitBtn.setStyle(defaultStyleMinor);
        quitBtn.setMinSize(buttonMinorR*2,buttonMinorR*2);

        choiceCircle=new Ellipse();
        targetCircle=new Ellipse();

        box=new VBox();

        board=new Creature[sceneX][sceneY];

        bulletinBoard=new BulletinBoard();

        taskList=new LinkedList<>();

        things=new ArrayList<>();
        things.add(new Thing("可口可乐","file:./src/main/resource/coke.png"));
        things.add(new Thing("红牛","file:./src/main/resource/redBull.png"));
        things.add(new Thing("仙葫芦","file:./src/main/resource/magicGourd.png"));
        things.add(new Thing("羽绒服","file:./src/main/resource/coat.png"));
        things.add(new Thing("魔女斗篷","file:./src/main/resource/magicCoat.png"));
    }

    public static synchronized void addTask(Technique technique){
        taskList.add(technique);
    }

    public static synchronized void tryToGetPosition(Point2D position,Creature creature){
        Creature originalCreature=board[(int) position.getX()][(int) position.getY()];
        if(originalCreature==null){
            synchronized (board) {
                board[(int) position.getX()][(int) position.getY()] = creature;
            }
            creature.setMapPositionX((int) position.getX());
            creature.setMapPositionY((int) position.getY());
        }else{
            if(creature.getPhysicalDefence()>originalCreature.getPhysicalDefence()){
                originalCreature.die();
                synchronized (board) {
                    board[(int) position.getX()][(int) position.getY()] = creature;
                }
                creature.setMapPositionX((int) position.getX());
                creature.setMapPositionY((int) position.getY());
                bulletinBoard.addBulletin("撞死人啦");
            }else{
                creature.die();
                bulletinBoard.addBulletin("被撞死拉");
            }
        }
    }

    @Override
    public void onLaunch() {
        isRunning=false;
    }

    @Override
    public synchronized void onEnter() {
        File file;
        try {
            backGroundMusic=new BackGroundMusic();
            musicThread=new Thread(backGroundMusic);
            musicThread.start();
            file=new File("./src/main/resource/sound.wav");
            soundPlayer=newAudioClip(file.toURL());
        }catch (Exception e){
            e.printStackTrace();
        }

        isRunning=true;
        currentCreature=null;
        state=State.find("PROGRESSION");
        scene=new Scene();
        creatures=new Creature[11];

        getChildren().add(scene.getSceneImageView());
        getChildren().add(scene.getProgression());

        Creature creature;

        creature=new FirstGourd((int) Creature.defaultArrange[0].getX(),
                (int) Creature.defaultArrange[0].getY(),
                true,0);
        creatures[0]=creature;
        creature=new SecondGourd((int) Creature.defaultArrange[1].getX(),
                (int) Creature.defaultArrange[1].getY(),
                true,1);
        creatures[1]=creature;
        creature=new ThirdGourd((int) Creature.defaultArrange[2].getX(),
                (int) Creature.defaultArrange[2].getY(),
                true,2);
        creatures[2]=creature;
        creature=new FourthGourd((int) Creature.defaultArrange[3].getX(),
                (int) Creature.defaultArrange[3].getY(),
                true,3);
        creatures[3]=creature;
        creature=new FifthGourd((int) Creature.defaultArrange[4].getX(),
                (int) Creature.defaultArrange[4].getY(),
                true,4);
        creatures[4]=creature;
        creature=new SixthGourd((int) Creature.defaultArrange[5].getX(),
                (int) Creature.defaultArrange[5].getY(),
                true,5);
        creatures[5]=creature;
        creature=new SeventhGourd((int) Creature.defaultArrange[6].getX(),
                (int) Creature.defaultArrange[6].getY(),
                true,6);
        creatures[6]=creature;

        creature=new FirstMonster((int) Creature.defaultArrange[7].getX(),
                (int) Creature.defaultArrange[7].getY(),
                false,0);
        creatures[7]=creature;
        creature=new SecondMonster((int) Creature.defaultArrange[8].getX(),
                (int) Creature.defaultArrange[8].getY(),
                false,1);
        creatures[8]=creature;
        creature=new ThirdMonster((int) Creature.defaultArrange[9].getX(),
                (int) Creature.defaultArrange[9].getY(),
                false,2);
        creatures[9]=creature;
        creature=new FourthMonster((int) Creature.defaultArrange[10].getX(),
                (int) Creature.defaultArrange[10].getY(),
                false,3);
        creatures[10]=creature;

        for (Creature value : creatures) {
            getChildren().add(value.getImageView());
            getChildren().add(value.getHeadView());
            getChildren().add(value.getRed());
            getChildren().add(value.getBlue());
            getChildren().add(value.getBlankRed());
            getChildren().add(value.getBlankBlue());
            getChildren().add(value.getMiniView());
            board[value.getMapPositionX()][value.getMapPositionY()]=value;
        }

        for (Creature value : creatures) {
            value.setSuspended(false);
            value.getThread().start();
        }

        getChildren().add(bulletinBoard.textArea);
        bulletinThread=new Thread(bulletinBoard);
        bulletinThread.start();

        for(Thing thing:things){
            getChildren().add(thing.imageView);
        }
    }

    public void onStart(){
        if(isRunning) {
            for (Creature creature : creatures) {
                synchronized (creatures) {
                    creature.setSuspended(false);
                }
            }
            try {
                bulletinBoard.notifyAll();
            }catch(Exception ignored){
            }
        }
    }

    public void onStop(){
        if(isRunning) {
            for (Creature creature : creatures) {
                synchronized (creatures) {
                    creature.setSuspended(true);
                }
            }
            try {
                bulletinBoard.wait();
            }catch(Exception ignored){
            }
        }
    }

    public void onLeave(){
        for (Creature creature : creatures) {
            creature.getThread().interrupt();
        }
        bulletinThread.interrupt();
        resetBoard();
        resetList();
        getChildren().remove(bulletinBoard.textArea);
        backGroundMusic.mediaPlayer.close();
        for(Thing thing:things){
            getChildren().remove(thing.imageView);
        }
        app.gotoView("Home");
    }

    public synchronized void onUpdate(double time){
        long now=System.currentTimeMillis();
        if(state==State.OVER){
            if(now-finishTime>3500){
                getChildren().remove(resultImageView1);
                if(resultImageView2!=null){
                    getChildren().remove((resultImageView2));
                }
                onLeave();
            }
            return;
        }
        if(state==State.SELECTION) {
            View view= app.getCurrentView();
            view.getChildren().remove(targetCircle);
            target=matchNearestPosition(mouseInput.getPointX(),mouseInput.getPointY());
            targetCircle.setTranslateX(realCoordinate[(int) target.getX()][(int) target.getY()].getX());
            targetCircle.setTranslateY(realCoordinate[(int) target.getX()][(int) target.getY()].getY());
            targetCircle.setRadiusX(boardRadius[(int) target.getX()][(int) target.getY()]*2);
            targetCircle.setRadiusY(boardRadius[(int) target.getX()][(int) target.getY()]);
            targetCircle.setFill(Color.YELLOW);
            targetCircle.setOpacity(0.8);
            view.getChildren().add(targetCircle);

            if(mouseInput.isClicked(Mouse.LEFT)) {
                setState(State.EXECUTION);
            }
        }
        if(state==State.EXECUTION){
            View view=app.getCurrentView();
            view.getChildren().remove(targetCircle);
            currentCreature.setChoice(choice);
            currentCreature.setTargetPoint(target);
            currentCreature.setIndex(index);
            synchronized (currentCreature) {
                if(currentCreature.getThread().isAlive()) {
                    currentCreature.notifyAll();
                }
            }
            setState(State.PROGRESSION);

            if(choice==Choice.QUIT){
                view.onLeave();
            }else if(choice==Choice.PAUSE){
                view.onStop();
            }else if(choice==Choice.KILL){
                view.getChildren().remove(currentCreature.getMiniView());
            }
        }
        if(state==State.PROGRESSION&&semaphore.availablePermits()<1){
            setState(State.INTERACTION);
            setBoard();
        }
        if(semaphore.availablePermits()>0){
            setState(State.PROGRESSION);
            resetBoard();
        }
        int enemyLeft=7,playerLeft=4;
        for(Creature creature:creatures){
            creature.setRedAndBlue();
            if(creature.getThread().isAlive()) {
                creature.setMiniView();
                if (creature.getState() == State.EXECUTION) {
                    creature.setImageView();
                } else if(creature.getState()==State.PROGRESSION) {
                    if (creature.getLastGoBack() > 0) {
                        int x=creature.getMapPositionX(),y=creature.getMapPositionY();
                        if(now-creature.getLastGoBack()<500) {
                            if(x<4) {
                                creature.setRealPosition(realCoordinate[x][y].add(-20, 0));
                            }else{
                                creature.setRealPosition(realCoordinate[x][y].add(20, 0));
                            }
                            creature.setImageView();
                        }else{
                            creature.setRealPosition(realCoordinate[x][y]);
                            creature.setImageView();
                            creature.resetLastGoBack();
                        }
                    }
                }
            }else{
                creature.getImageView().rotateProperty().setValue(-90);
                creature.getMiniView().visibleProperty().setValue(false);
                creature.getImageView().opacityProperty().setValue(
                        Math.max(creature.getImageView().opacityProperty().get()-0.05,0));
                if(creature.isEnemy()){
                    enemyLeft--;
                }else{
                    playerLeft--;
                }
            }
        }
        if(playerLeft==0||enemyLeft==0) {
            if (playerLeft == 0) {
                bulletinBoard.addBulletin("葫芦娃胜利！");
                battleFail();
            }
            else{
                bulletinBoard.addBulletin("妖精胜利！");
                battleSuccess();
            }
            finishTime=now;
            setState(State.OVER);
            for(Creature creature:creatures){
                creature.getThread().interrupt();
                creature.save();
            }
            if(!isPlayBack){
                File file=new File("./save/",fileName+".game");
                if(!file.exists()){
                    try{
                        file.createNewFile();
                    }catch(Exception ignored){}
                }
            }
        }

        while(!taskList.isEmpty()){
            Technique currentTask=taskList.poll();
            currentTask.imageView.setVisible(true);
            currentTask.timeline.play();
            soundPlayer.play();
        }
        bulletinBoard.setBulletinBoard();

        for(Thing thing:things){
            if(thing.time>0&&now-thing.time>=1000){
                thing.imageView.setVisible(false);
                thing.time=-1;
            }
        }
    }

    private void battleSuccess() {
        Image successImage1=new Image("file:./src/main/resource/success1.gif");
        resultImageView1=new ImageView(successImage1);
        Image successImage2=new Image("file:./src/main/resource/success2.gif");
        resultImageView2=new ImageView(successImage2);
        resultImageView1.translateYProperty().setValue(-250);
        resultImageView2.translateYProperty().setValue(120);
        getChildren().add(resultImageView1);
        getChildren().add(resultImageView2);
    }

    private void battleFail() {
        Image failImage=new Image("file:./src/main/resource/fail.png");
        resultImageView1=new ImageView(failImage);
        getChildren().add(resultImageView1);
    }

    public static synchronized void setState(State state){
        PlayView.state=state;
    }

    public static synchronized void setCurrentCreature(Creature currentCreature){
        PlayView.currentCreature=currentCreature;
    }

    public static void setBoard(){
        View view= app.getCurrentView();
        view.getChildren().add(physicalAttackBtn);
        view.getChildren().add(magicAttackBtn);
        view.getChildren().add(defendBtn);
        view.getChildren().add(thingBtn);
        view.getChildren().add(changePositionBtn);
        view.getChildren().add(aiBtn);
        view.getChildren().add(killBtn);
        view.getChildren().add(pauseBtn);
        view.getChildren().add(quitBtn);
        choice=Choice.ATTACK;
    }
    
    public static void resetBoard(){
        View view= app.getCurrentView();
        view.getChildren().remove(physicalAttackBtn);
        view.getChildren().remove(magicAttackBtn);
        view.getChildren().remove(defendBtn);
        view.getChildren().remove(thingBtn);
        view.getChildren().remove(changePositionBtn);
        view.getChildren().remove(aiBtn);
        view.getChildren().remove(killBtn);
        view.getChildren().remove(pauseBtn);
        view.getChildren().remove(quitBtn);
    }

    public static void setList(){
        View view= app.getCurrentView();
        List<Technique> techniques=currentCreature.getTechniques();

        for(Technique technique:techniques){
            Button button=new Button(technique.name);
            button.setOnAction((event)->{
                int currentBlue=currentCreature.getVigour();
                if(currentBlue>=technique.blue){
                    choice = Choice.MAGIC;
                    index=techniques.indexOf(technique);
                    resetList();
                    setState(State.SELECTION);
                    currentCreature.subtractVigour(technique.blue);
                }
            });
            box.getChildren().add(button);
        }
        box.setAlignment(Pos.CENTER);
        box.setTranslateX(boardX);
        box.setTranslateY(boardY);
        box.setSpacing(0);
        view.getChildren().add(box);
    }

    public static void resetList(){
        View view= app.getCurrentView();
        box.getChildren().clear();
        view.getChildren().remove(box);
    }

    public static void setThingsList(){
        View view= app.getCurrentView();

        for(Thing thing:things){
            Button button=new Button(thing.name);
            button.setOnAction((event)->{
                choice = Choice.THING;
                index=things.indexOf(thing);
                resetList();
                setState(State.EXECUTION);
            });
            box.getChildren().add(button);
        }
        box.setAlignment(Pos.CENTER);
        box.setTranslateX(boardX);
        box.setTranslateY(boardY);
        box.setSpacing(0);
        view.getChildren().add(box);
    }

    public static Point2D matchNearestPosition(double x,double y){
        x-=WindowX/2;
        y-=WindowY/2;
        int optimalX=0,optimalY=0;
        double nearestDistance=maxDistance;
        for(int i=0;i<sceneX;i++){
            for(int j=0;j<sceneY;j++){
                double distance=realCoordinate[i][j].distance(x,y);
                if(distance<nearestDistance){
                    nearestDistance=distance;
                    optimalX=i;
                    optimalY=j;
                }
            }
        }
        return new Point2D(optimalX,optimalY);
    }

}
