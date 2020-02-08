package gourd.creature;

import gourd.Scene;
import gourd.action.HorizontalMagic;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class SixthGourd extends Creature{

    public SixthGourd(int mapX, int mapY, boolean isEnemy, int order){
        super(mapX,mapY,isEnemy,order);

        health=150;
        maxHealth=150;
        vigour=100;
        maxVigour=100;
        speed=8;
        physicalAttack=20;
        magicalAttack=20;
        physicalDefence=10;
        magicalDefence=10;

        modelHeight=80;
        modelWidth=70;
        headWidth=110;
        headHeight=140;
        miniWidth=70;
        miniHeight=70;

        realPosition=new Point2D(Scene.realCoordinate[mapX][mapY].getX()+modelWidth/2,
                Scene.realCoordinate[mapX][mapY].getY()-modelHeight/2);

        imageView=new ImageView();
        imageView.setFitHeight(modelHeight);
        imageView.setFitWidth(modelWidth);
        positionBiasX=-30;
        positionBiasY=20;
        imageView.translateXProperty().setValue(realPosition.getX()+positionBiasX);
        imageView.translateYProperty().setValue(realPosition.getY()+positionBiasY);

        headView=new ImageView();
        headView.translateXProperty().setValue(barBaseX-barWidth/2-headRealWidth/2+barIntervalX*order);
        double headHeightBias=35;
        headView.translateYProperty().setValue(isEnemy?barBaseY+barHeight+barIntervalY+headHeightBias:
                barBaseY+barHeight+headHeightBias);

        miniView=new ImageView();
        initProgress =progressionBaseX-progressionX/2;
        miniView.translateXProperty().setValue(progressionBaseX-progressionX/2);
        double miniHeightBias=15;
        miniView.translateYProperty().setValue(isEnemy?progressionBaseY-progressionY/2-miniRealHeight/2+miniHeightBias:
                progressionBaseY+progressionY/2+miniRealHeight/2+miniHeightBias);

        headView.setFitWidth(headWidth);
        headView.setFitHeight(headHeight);
        miniView.setFitWidth(miniWidth);
        miniView.setFitHeight(miniHeight);

        model=new Image("file:./src/main/resource/gourd6.png");
        imageView.setImage(model);

        head=new Image("file:./src/main/resource/head6.png");
        headView.setImage(head);

        miniView.setImage(head);

        HorizontalMagic myMagic1=new HorizontalMagic(new Image("file:./src/main/resource/milk.png"),
                "奶啊",this,-15,30,2,1);
        techniques.add(myMagic1);

    }

}
