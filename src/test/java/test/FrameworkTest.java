package test;

import org.junit.Test;

import frame.app.Game;
import test.view.HomeView;
import test.view.PlayView;
import javafx.geometry.Point2D;

import static frame.Framework.app;

public class FrameworkTest extends Game {

    public static void setTestPoint(Point2D point,Integer b){
        point=point.add(1,1);
        b=b+1;
        System.out.println(point.getX()+" , "+point.getY());
        System.out.println(b);
    }

    @Test
    public void main(){
        String[] args = new String[0];
        launch(args);
    }

    public void onLaunch(){
        System.out.println("Launch!");
        app.setTitle("Test Game");
        app.setWidth(800.0);
        app.setHeight(600.0);

        app.regView("Home",new HomeView());
        app.regView("Play",new PlayView());
        app.gotoView("Home");
    }

    public void onFinish(){
        System.out.println("Finish!");
    }

    public boolean onExit(){
        System.out.println("Exit!");
        return true;
    }
}