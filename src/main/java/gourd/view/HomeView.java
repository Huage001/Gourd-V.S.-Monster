package gourd.view;

import frame.app.View;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

import static frame.Framework.*;
import static frame.Framework.app;
import static gourd.view.PlayView.fileName;
import static gourd.view.PlayView.isPlayBack;

public class HomeView extends View {

    private Button playOfflineBtn;
    private Button playOnlineBtn;
    private Button playBackBtn;
    private Button helpBtn;
    private Button aboutBtn;
    private Button exitBtn;

    @Override
    public void onLaunch() {
        playOfflineBtn=new Button("单人游戏");
        playOfflineBtn.setOnAction((event)->{
            isPlayBack=false;
            fileName="save_"+System.currentTimeMillis();
            String path ="./save/"+fileName;
            File f = new File(path);
            if(!f.exists()){
                f.mkdirs();
            }
            app.gotoView("Play");
        });
        playOfflineBtn.setMinSize(100,35);

        playOnlineBtn=new Button("网络对战");
        playOnlineBtn.setOnAction((event)->{
            isPlayBack=false;
            app.gotoView("Play");
        });
        playOnlineBtn.setMinSize(100,35);

        playBackBtn=new Button("观看战斗回放");
        playBackBtn.setOnAction((event)->{
            isPlayBack=true;
            FileChooser fileChooser=new FileChooser();
            fileChooser.setTitle("选择记录文件");
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("GAME files (*.game)", "*.game");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialDirectory(new File("./save"));
            File file= fileChooser.showOpenDialog(null);
            if(file!=null) {
                fileName=file.getName();
                app.gotoView("Play");
            }
        });
        playBackBtn.setMinSize(100,35);

        helpBtn=new Button("帮助");
        helpBtn.setOnAction((event)->{
            app.gotoView("Help");//TODO
        });
        helpBtn.setMinSize(100,35);

        aboutBtn=new Button("关于");
        aboutBtn.setOnAction((event)->{
            app.gotoView("About");//TODO
        });
        aboutBtn.setMinSize(100,35);

        exitBtn=new Button("退出");
        exitBtn.setOnAction((event)->{
            app.exit();
        });
        exitBtn.setMinSize(100,35);

        VBox box=new VBox(playOfflineBtn,playOnlineBtn,playBackBtn,helpBtn,aboutBtn,exitBtn);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(30);
        box.translateYProperty().setValue(100);
        box.opacityProperty().setValue(0.75);

        Image homeImage=new Image("file:./src/main/resource/home.png");
        ImageView homeImageView=new ImageView(homeImage);
        Image titleImage=new Image("file:./src/main/resource/title.gif");
        ImageView titleImageView=new ImageView(titleImage);
        titleImageView.translateXProperty().setValue(-200);
        titleImageView.translateYProperty().setValue(-250);

        getChildren().add(homeImageView);
        getChildren().add(titleImageView);
        getChildren().add(box);
    }
}
