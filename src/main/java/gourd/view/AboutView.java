package gourd.view;

import frame.app.View;
import javafx.scene.control.Button;

import static frame.Framework.app;

public class AboutView extends View {
    private Button homeBtn;

    @Override
    public void onLaunch() {
        homeBtn=new Button("参见Readme.md");
        homeBtn.setOnAction((event)->{
            app.gotoView("Home");
        });
        getChildren().add(homeBtn);
    }

    public void onEnter(){

    }
}
