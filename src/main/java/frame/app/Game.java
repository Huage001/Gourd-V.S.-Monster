package frame.app;

import javafx.application.Application;
import javafx.stage.Stage;

public abstract class Game extends Application {

    private App app;

    public abstract void onLaunch();

    public void onFinish(){
        //Do something in subclass when game finish
    }

    public boolean onExit(){
        //Do something in subclass when game tends to exit
        return true;
    }

    @Override
    public final void start(Stage stage) throws Exception {
        app=new App(stage);
        app.onLaunch=this::onLaunch;
        app.onFinish=this::onFinish;
        app.onExit=this::onExit;

        app.launch();
    }

    public final void stop(){
        app.getCurrentView().onLeave();
        app.finish();
    }
}