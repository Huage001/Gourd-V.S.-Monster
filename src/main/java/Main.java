import frame.app.Game;
import gourd.view.AboutView;
import gourd.view.HelpView;
import gourd.view.HomeView;
import gourd.view.PlayView;

import static frame.Framework.app;
import static gourd.Scene.WindowX;
import static gourd.Scene.WindowY;


public class Main extends Game {

    public static void main(String[] args){
        launch(args);
    }

    public void onLaunch(){
        //System.out.println("Launch!");
        app.setTitle("妖精大战葫芦娃");
        app.setWidth(WindowX);
        app.setHeight(WindowY);

        app.regView("Home",new HomeView());
        app.regView("Play",new PlayView());
        app.regView("Help",new HelpView());
        app.regView("About",new AboutView());
        app.gotoView("Home");
    }
}
