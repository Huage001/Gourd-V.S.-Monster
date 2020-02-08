package gourd;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Thing {
    Image image;
    public ImageView imageView;
    public String name;
    public long time;
    public Thing(String name,String url){
        image=new Image(url);
        imageView=new ImageView(image);
        imageView.setVisible(false);
        imageView.translateXProperty().setValue(-400);
        imageView.translateYProperty().setValue(-100);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        this.name=name;
        this.time=-1;
    }
}
