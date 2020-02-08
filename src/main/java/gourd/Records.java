package gourd;

import javafx.geometry.Point2D;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Records {

    public static class Record implements Serializable{
        public long time;
        public String choice;
        public int x,y;
        public int index;
        public Record(long time,Choice choice,int index,Point2D target){
            this.time=time;
            this.choice=choice.name();
            if(target==null){
                this.x=-1;
                this.y=-1;
            }else {
                this.x = (int) target.getX();
                this.y = (int) target.getY();
            }
            this.index=index;
        }
    }

    List<Record> recordList;
    int round;

    public Records() {
        recordList = new ArrayList<>();
        round=0;
    }

    public Records(String fileName){
        recordList=new ArrayList<>();
        readFile(fileName);
        round=0;
    }

    public void addRecord(long time,Choice choice,int index,Point2D target){
        recordList.add(new Record(time,choice,index,target));
    }

    public Record getNext(){
        if(round<recordList.size()) {
            Record record = recordList.get(round);
            round++;
            return record;
        }else{
            return null;
        }
    }

    public void writeFile(String fileName){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(recordList);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    void readFile(String fileName){
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);
            recordList=(List<Record>)objectInputStream.readObject();
            fileInputStream.close();
            objectInputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
