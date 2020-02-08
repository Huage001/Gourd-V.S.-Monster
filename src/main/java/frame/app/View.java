package frame.app;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public abstract class View {

    public Pane getPane() {
        return pane;
    }

    private final Pane pane;

    public ObservableList<Node> getChildren(){
        return pane.getChildren();
    }

    public View(){
        pane=new StackPane();
        pane.setBackground(Background.EMPTY);
    }

    public abstract void onLaunch();

    public void onEnter() {
        //Do something in subclass when the game enter this view
    }

    public void onStart(){
        //Do something in subclass when initialize engining
        //When window gets the focus, it would call onStart
    }

    public void onUpdate(double time) {
        //Do something in subclass when the game update its frame
        //calling frequency depends on fps
    }

    public void onStop(){
        //Do something
        //When window lose the focus, it would call onStop
    }

    public void onLeave(){
        //Do something in subclass when the game leaves this view
    }

    public void onFinish(){
        //Do something in subclass when this view is going to exit
    }
}
