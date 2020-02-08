package gourd;

public enum Choice {
    ATTACK(0,"ATTACK"),
    MAGIC(1,"MAGIC"),
    THING(2,"THING"),
    SKIP(3,"SKIP"),
    PAUSE(4,"PAUSE"),
    QUIT(5,"QUIT"),
    CHANGE_POSITION(6,"CHANGE_POSITION"),
    AI(7,"AI"),
    KILL(8,"KILL");

    private final int code;
    private final String text;
    Choice(int code,String text){
        this.code=code;
        this.text=text;
    }

    public static Choice find(int code){
        for(Choice k:values()){
            if(k.code==code){
                return k;
            }
        }
        return null;
    }

    public static Choice find(String text){
        for(Choice k:values()){
            if(k.text!=null&&k.text.equalsIgnoreCase(text)){
                return k;
            }
        }
        return null;
    }
}
