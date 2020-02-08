package test.view;

import frame.app.View;
import javafx.scene.control.Button;

import static frame.Framework.*;
import static frame.Framework.app;

public class PlayView extends View {

    private Button homeBtn;

    @Override
    public void onLaunch() {
        homeBtn=new Button("Home");
        homeBtn.setOnAction((event)->{
            app.gotoView("Home");
        });
        getChildren().add(homeBtn);

    }
}
