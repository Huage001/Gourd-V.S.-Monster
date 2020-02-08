package frame.input;

import javafx.scene.input.MouseButton;

public enum Mouse {

    LEFT(MouseButton.PRIMARY),
    MIDDLE(MouseButton.MIDDLE),
    RIGHT(MouseButton.SECONDARY);

    private final MouseButton button;

    Mouse(MouseButton button){
        this.button=button;
    }

    public MouseButton getButton(){
        return button;
    }

    public static Mouse find(MouseButton button){
        for(Mouse m:values()){
            if(m.button!=null&&m.button==button){
                return m;
            }
        }
        return null;
    }
}
