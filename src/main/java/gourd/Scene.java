package gourd;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Scene {
    public static final double WindowX=1129.0;
    public static final double WindowY=750.0;
    public static final int sceneX=8;
    public static final int sceneY=4;
    public static final double maxDistance=2000.0;
    public static final Point2D[][] realCoordinate = new Point2D[sceneX][sceneY];
    public static final double[][] boardRadius=new double[sceneX][sceneY];
    public static final double progressionBaseX=310;//进度条的坐标
    public static final double progressionBaseY=-315;
    public static final double progressionX=400;//进度条的大小
    public static final double progressionY=8;
    public static final double boardX=0;
    public static final double boardY=250;
    public static final double buttonMajorR=25;
    public static final double buttonMinorR=15;
    public static final double buttonInterval=5;
    public static final double bias=30;
    public static final String defaultStyleMajor="-fx-background-color: " +
            "linear-gradient(to right,#00fffc,#fff600);"+
            "-fx-background-radius:25;"+
            "-fx-background-radius:25;"+
            "-fx-font-size:25;";
    public static final String defaultStyleMinor="-fx-background-color: " +
            "linear-gradient(to right,#00fffc,#fff600);"+
            "-fx-background-radius:15;"+
            "-fx-background-radius:15;"+
            "-fx-font-size:15;";
    static {
        realCoordinate[0][0]=new Point2D(-285+45,-20+210);
        realCoordinate[0][1]=new Point2D(-325+45,-20+220);
        realCoordinate[0][2]=new Point2D(-380+45,-20+240);
        realCoordinate[0][3]=new Point2D(-470+45,-20+270);
        realCoordinate[1][0]=new Point2D(-230+45,-20+210);
        realCoordinate[1][1]=new Point2D(-265+45,-20+225);
        realCoordinate[1][2]=new Point2D(-305+45,-20+245);
        realCoordinate[1][3]=new Point2D(-360+45,-20+270);
        realCoordinate[2][0]=new Point2D(-190+45,-20+210);
        realCoordinate[2][1]=new Point2D(-210+45,-20+230);
        realCoordinate[2][2]=new Point2D(-240+45,-20+250);
        realCoordinate[2][3]=new Point2D(-270+45,-20+280);
        realCoordinate[3][0]=new Point2D(-140+45,-20+215);
        realCoordinate[3][1]=new Point2D(-150+45,-20+230);
        realCoordinate[3][2]=new Point2D(-170+45,-20+250);
        realCoordinate[3][3]=new Point2D(-180+45,-20+280);
        realCoordinate[4][0]=new Point2D(55+45,-20+220);
        realCoordinate[4][1]=new Point2D(65+45,-20+235);
        realCoordinate[4][2]=new Point2D(80+45,-20+260);
        realCoordinate[4][3]=new Point2D(100+45,-20+285);
        realCoordinate[5][0]=new Point2D(110+45,-20+220);
        realCoordinate[5][1]=new Point2D(125+45,-20+235);
        realCoordinate[5][2]=new Point2D(145+45,-20+255);
        realCoordinate[5][3]=new Point2D(180+45,-20+290);
        realCoordinate[6][0]=new Point2D(160+45,-20+220);
        realCoordinate[6][1]=new Point2D(190+45,-20+235);
        realCoordinate[6][2]=new Point2D(225+45,-20+255);
        realCoordinate[6][3]=new Point2D(245+45,-20+285);
        realCoordinate[7][0]=new Point2D(205+45,-20+220);
        realCoordinate[7][1]=new Point2D(240+45,-20+235);
        realCoordinate[7][2]=new Point2D(290+45,-20+255);
        realCoordinate[7][3]=new Point2D(360+45,-20+280);

        boardRadius[0][0]=10.0;
        boardRadius[0][1]=10.26;
        boardRadius[0][2]=16.25;
        boardRadius[0][3]=20.0;
        boardRadius[1][0]=9.27;
        boardRadius[1][1]=11.5;
        boardRadius[1][2]=15.0;
        boardRadius[1][3]=19.5;
        boardRadius[2][0]=9.0;
        boardRadius[2][1]=10.35;
        boardRadius[2][2]=15.85;
        boardRadius[2][3]=20.25;
        boardRadius[3][0]=9.0;
        boardRadius[3][1]=11.5;
        boardRadius[3][2]=16.45;
        boardRadius[3][3]=18.5;
        boardRadius[4][0]=9.0;
        boardRadius[4][1]=11.5;
        boardRadius[4][2]=16.45;
        boardRadius[4][3]=18.5;
        boardRadius[5][0]=9.0;
        boardRadius[5][1]=10.35;
        boardRadius[5][2]=15.85;
        boardRadius[5][3]=20.25;
        boardRadius[6][0]=9.27;
        boardRadius[6][1]=11.5;
        boardRadius[6][2]=15.0;
        boardRadius[6][3]=19.5;
        boardRadius[7][0]=10.0;
        boardRadius[7][1]=10.26;
        boardRadius[7][2]=16.25;
        boardRadius[7][3]=20.0;
    }

    Image sceneImage;
    ImageView sceneImageView;
    Rectangle progression;

    public Image getSceneImage() {
        return sceneImage;
    }

    public ImageView getSceneImageView() {
        return sceneImageView;
    }

    public Rectangle getProgression(){
        return progression;
    }

    public Scene(){
        sceneImage=new Image("file:./src/main/resource/newScene.jpg");
        sceneImageView=new ImageView(sceneImage);

        progression=new Rectangle(progressionX,progressionY,Color.YELLOWGREEN);
        progression.setStroke(Color.PURPLE);
        progression.translateXProperty().setValue(progressionBaseX);
        progression.translateYProperty().setValue(progressionBaseY);
    }

}
