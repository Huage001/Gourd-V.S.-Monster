package gourd.creature;

import gourd.Scene;
import gourd.action.HorizontalMagic;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class SecondMonster extends Creature{

    public SecondMonster(int mapX, int mapY, boolean isEnemy, int order){
        super(mapX,mapY,isEnemy,order);

        health=250;
        maxHealth=250;
        vigour=100;
        maxVigour=100;
        speed=15;
        physicalAttack=20;
        magicalAttack=20;
        physicalDefence=10;
        magicalDefence=10;

        modelHeight=80;
        modelWidth=70;
        headWidth=60;
        headHeight=60;
        miniWidth=35;
        miniHeight=35;

        realPosition=new Point2D(Scene.realCoordinate[mapX][mapY].getX()+modelWidth/2,
                Scene.realCoordinate[mapX][mapY].getY()-modelHeight/2);

        imageView=new ImageView();
        imageView.setFitHeight(modelHeight);
        imageView.setFitWidth(modelWidth);
        positionBiasX=0;
        positionBiasY=0;
        imageView.translateXProperty().setValue(realPosition.getX()+positionBiasX);
        imageView.translateYProperty().setValue(realPosition.getY()+positionBiasY);

        headView=new ImageView();
        headView.translateXProperty().setValue(barBaseX-barWidth/2-headRealWidth/2+barIntervalX*order);
        double headHeightBias=0;
        headView.translateYProperty().setValue(isEnemy?barBaseY+barHeight+barIntervalY+headHeightBias:
                barBaseY+barHeight+headHeightBias);

        miniView=new ImageView();
        initProgress =progressionBaseX-progressionX/2;
        miniView.translateXProperty().setValue(progressionBaseX-progressionX/2);
        double miniHeightBias=10;
        miniView.translateYProperty().setValue(isEnemy?progressionBaseY-progressionY/2-miniRealHeight/2+miniHeightBias:
                progressionBaseY+progressionY/2+miniRealHeight/2+miniHeightBias);

        headView.setFitWidth(headWidth);
        headView.setFitHeight(headHeight);
        miniView.setFitWidth(miniWidth);
        miniView.setFitHeight(miniHeight);

        model=new Image("file:./src/main/resource/monster2.png");
        imageView.setImage(model);

        head=new Image("file:./src/main/resource/head9.png");
        headView.setImage(head);

        miniView.setImage(head);

        HorizontalMagic myMagic1=new HorizontalMagic(new Image("file:./src/main/resource/sword.png"),
                "剑来",this,20,30,1,0);
        techniques.add(myMagic1);
    }

}
