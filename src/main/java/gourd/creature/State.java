package gourd.creature;

public enum State {
    PROGRESSION(0,"PROGRESSION"),
    INTERACTION(1,"INTERACTION"),
    EXECUTION(2,"EXECUTION"),
    SELECTION(3,"SELECTION"),
    LIST(4,"LIST"),
    OVER(5,"OVER");

    private final int code;
    private final String text;

    State(int code,String text){
        this.code=code;
        this.text=text;
    }

    public static State find(int code){
        for(State k:values()){
            if(k.code==code){
                return k;
            }
        }
        return null;
    }

    public static State find(String text){
        for(State k:values()){
            if(k.text!=null&&k.text.equalsIgnoreCase(text)){
                return k;
            }
        }
        return null;
    }
}
