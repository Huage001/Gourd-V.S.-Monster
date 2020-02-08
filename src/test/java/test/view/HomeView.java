package test.view;

import frame.app.View;
import frame.input.Key;
import frame.input.Mouse;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import static frame.Framework.*;

public class HomeView extends View {

    private Button playBtn;
    private Button exitBtn;

    @Override
    public void onLaunch() {
        playBtn=new Button("Play");
        playBtn.setOnAction((event)->{
            app.gotoView("Play");
        });
        exitBtn=new Button("Exit");
        exitBtn.setOnAction((event)->{
            app.exit();
        });

        VBox box=new VBox(playBtn,exitBtn);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(20);

        getChildren().add(box);
    }

    public void onStart(){
        System.out.println("Start!");
    }

    public void onUpdate(double time) {
        //System.out.println("Current time is "+time);
        if(keyInput.isPressed(Key.A)){
            System.out.println("Pressed A");
        }
        if(keyInput.isReleased(Key.A)){
            System.out.println("Released A");
        }
        if(keyInput.isHeld(Key.L)){
            System.out.println("Holding L");
        }
        if(keyInput.isTyped(Key.C)){
            System.out.println("Typed C: "+keyInput.getTypeCount(Key.C));
        }

        //System.out.println(mouseInput.getPointX()+" , "+mouseInput.getPointY());

        if(mouseInput.isPressed(Mouse.LEFT)){
            System.out.println("Pressed Left");
        }

        if(mouseInput.isReleased(Mouse.LEFT)){
            System.out.println("Released Left");
        }

        if(mouseInput.isHeld(Mouse.RIGHT)){
            System.out.println("Holding Right");
        }

        if(mouseInput.isDragged(Mouse.LEFT)){
            System.out.println("Dragged Left: "+mouseInput.getDragX(Mouse.LEFT)+" , "+mouseInput.getDragY(Mouse.LEFT));
        }

        if(mouseInput.isClicked(Mouse.MIDDLE)){
            System.out.println("Clicked Middle: "+mouseInput.getClickCount(Mouse.MIDDLE));
            System.out.println(mouseInput.getPointX()+" , "+mouseInput.getPointY());
        }

        if(mouseInput.isScrolled()){
            System.out.println("Scrolled: "+mouseInput.getScrollValue());
        }
    }

    public void onStop(){
        System.out.println("Stop!");
    }

    public void onEnter(){
        System.out.println("Enter Home!");
    }

    public void onLeave(){
        System.out.println("Leave Home!");
    }
}
