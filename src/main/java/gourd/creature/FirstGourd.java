package gourd.creature;

import gourd.Scene;
import gourd.action.HorizontalMagic;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class FirstGourd extends Creature{

    public FirstGourd(int mapX, int mapY, boolean isEnemy, int order){
        super(mapX,mapY,isEnemy,order);

        health=150;
        maxHealth=150;
        vigour=100;
        maxVigour=100;
        speed=10;
        physicalAttack=20;
        magicalAttack=20;
        physicalDefence=10;
        magicalDefence=10;

        modelHeight=130;
        modelWidth=120;
        headWidth=142;
        headHeight=175;
        miniWidth=100;
        miniHeight=100;

        realPosition=new Point2D(Scene.realCoordinate[mapX][mapY].getX()+modelWidth/2,
                Scene.realCoordinate[mapX][mapY].getY()-modelHeight/2);

        imageView=new ImageView();
        imageView.setFitHeight(modelHeight);
        imageView.setFitWidth(modelWidth);
        positionBiasX=-55;
        positionBiasY=20;
        imageView.translateXProperty().setValue(realPosition.getX()+positionBiasX);
        imageView.translateYProperty().setValue(realPosition.getY()+positionBiasY);

        headView=new ImageView();
        headView.translateXProperty().setValue(barBaseX-barWidth/2-headRealWidth/2+barIntervalX*order);
        double headHeightBias=15;
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

        model=new Image("file:./src/main/resource/gourd1.png");
        imageView.setImage(model);

        head=new Image("file:./src/main/resource/head1.png");
        headView.setImage(head);

        miniView.setImage(head);

        HorizontalMagic myMagic1=new HorizontalMagic(new Image("file:./src/main/resource/gourd.png"),
                "葫芦压顶",this,20,30,1,1);
        techniques.add(myMagic1);

    }

}
