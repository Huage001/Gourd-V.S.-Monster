package frame.app;

import javafx.animation.AnimationTimer;

public class Engine{

    private final Timer timer;

    private double nowNanos;

    public double getLastNanos() {
        return lastNanos;
    }

    public double getLastMillis(){
        return lastNanos*1e-6;
    }

    public double getLastSecs(){
        return lastNanos*1e-9;
    }

    private double lastNanos;

    public double getNowNanos() {
        return nowNanos;
    }

    public double getNowMillis(){
        return nowNanos*1e-6;
    }

    public double getNowSecs(){
        return nowNanos*1e-9;
    }

    public double getDeltaMillis(){
        return deltaNanos*1e-6;
    }

    public double getDeltaSecs(){
        return deltaNanos*1e-9;
    }

    public double getDeltaNanos() {
        return deltaNanos;
    }

    private double deltaNanos;

    //Frames Per Second
    private double fps;

    //Nanos Per Frame
    private double npf;

    OnUpdate onUpdate;
    OnStart onStart;
    OnStop onStop;

    Engine(){
        timer=new Timer();
        setFPS(60.0);
    }

    Engine(double fps){
        timer=new Timer();
        setFPS(fps);
    }

    public double getFPS(){
        return fps;
    }

    public void setFPS(double fps){
        this.fps=fps;
        this.npf=1e9/fps;
    }

    void start(){
        timer.start();
    }

    void stop(){
        timer.stop();
    }

    private final class Timer extends AnimationTimer{

        public void start(){
            this.reset();
            super.start();
            if(onStart!=null){
                onStart.handle();
            }
        }

        public void stop(){
            if(onStop!=null){
                onStop.handle();
            }
            super.stop();
            this.reset();
        }

        @Override
        public void handle(long l) {
            nowNanos=l;
            if(lastNanos>0){
                deltaNanos+=nowNanos-lastNanos;
            }
            lastNanos=nowNanos;
            if(deltaNanos>=npf){
                if(onUpdate!=null){
                    onUpdate.handle(deltaNanos);
                }
                deltaNanos-=npf;
            }
        }

        public void reset(){
            deltaNanos=0.0;
            lastNanos=0.0;
            nowNanos=0.0;
        }
    }

    interface OnUpdate{
        void handle(double time);
    }

    interface OnStart{
        void handle();
    }

    interface OnStop{
        void handle();
    }
}
