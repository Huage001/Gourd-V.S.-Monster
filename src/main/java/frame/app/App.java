package frame.app;

import frame.Framework;
import frame.input.KeyInput;
import frame.input.MouseInput;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.HashMap;

public class App {
    //Application Management

    private final Stage stage;
    private final Scene scene;
    private final Pane root;

    private final HashMap<String,View> viewMap;
    private final ObjectProperty<View> currentView;

    private final Engine engine;
    private final KeyInput keyInput;
    private final MouseInput mouseInput;

    OnLaunch onLaunch;
    OnFinish onFinish;
    OnExit onExit;

    public App(Stage stage){
        this.stage=stage;
        root=new StackPane();
        scene=new Scene(root);
        stage.setScene(scene);

        viewMap=new HashMap<>();
        currentView=new SimpleObjectProperty<>();

        engine=new Engine();
        keyInput=new KeyInput();
        mouseInput=new MouseInput();

        initFramework();
        initApp();
        initEngine();

        currentView.addListener((o,ov,nv)->{
            if(ov!=null){
                ov.onLeave();
                root.getChildren().remove(ov.getPane());
            }
            if(nv!=null){
                root.getChildren().add(nv.getPane());
                nv.onEnter();
            }
        });
    }

    private void initEngine(){
        scene.setFill(Color.WHITE);
        root.setBackground(Background.EMPTY);

        engine.onStart=()->{
            for(View view:viewMap.values()){
                if(view!=null){
                    view.onStart();
                }
            }
            keyInput.install(stage);
            mouseInput.install(stage);
        };

        engine.onUpdate=(time)->{
            View view=getCurrentView();
            if(view!=null){
                view.onUpdate(time);
            }

            keyInput.refresh();
            mouseInput.refresh();
        };

        engine.onStop=()->{
            keyInput.uninstall(stage);
            mouseInput.uninstall(stage);
            for(View view:viewMap.values()){
                if(view!=null){
                    view.onStop();
                }
            }
        };

        stage.focusedProperty().addListener((o,ov,nv)->{
            if(nv){
                engine.start();
            }else{
                engine.stop();
            }
        });
    }

    public Stage getStage(){
        return stage;
    }

    public Scene getScene(){
        return scene;
    }

    public String getTitle(){
        return stage.getTitle();
    }

    public void setTitle(String title){
        stage.setTitle(title);
    }

    public StringProperty titleProperty(){
        return stage.titleProperty();
    }

    public double getWidth(){
        return stage.getMinWidth();
    }

    public double getHeight(){
        return stage.getMinHeight();
    }

    public void setWidth(double width){
        root.setMinWidth(width);
    }

    public void setHeight(double height){
        root.setMinHeight(height);
    }

    public DoubleProperty widthProperty(){
        return root.minWidthProperty();
    }

    public DoubleProperty heightProperty(){
        return root.minHeightProperty();
    }

    public void regView(String name,View view){
        viewMap.put(name,view);
    }

    public void unregView(String name){
        View view=viewMap.remove(name);
        if(view!=null&&view==getCurrentView()){
            currentView.set(null);
        }
    }

    public View getView(String name) {
        return viewMap.get(name);
    }

    public View getCurrentView(){
        return currentView.get();
    }

    public ReadOnlyObjectProperty<View> currentViewProperty(){
        return currentView;
    }

    public void gotoView(String name){
        View view=viewMap.get(name);

        if(view!=null){
            currentView.set(view);
        }
    }

    void launch(){
        if(onLaunch!=null){
            onLaunch.handle();
        }

        for(View view:viewMap.values()){
            view.onLaunch();
        }

        stage.requestFocus();
        stage.show();
    }

    void finish(){
        for(View view:viewMap.values()){
            view.onFinish();
        }

        if(onFinish!=null){
            onFinish.handle();
        }
    }

    public void exit(){
        if(onExit.handle()) {
            Platform.exit();
        }
    }

    private void initFramework(){
        Framework.app=this;
        Framework.engine=engine;
        Framework.keyInput=keyInput;
        Framework.mouseInput=mouseInput;
    }

    private void initApp(){
        stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST,(event)->{
            if(onExit!=null&&!onExit.handle()){
                event.consume();
            }
        });
    }

    interface OnLaunch{
        void handle();
    }

    interface OnFinish{
        void handle();
    }

    interface OnExit{
        boolean handle();
    }
}
