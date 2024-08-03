/**
 * 
 * Amani Louendriz
 */




public class Page{

    private int value;
    private int timeStamp;

    public Page(int value,int timeStamp){
        this.value = value;
        this.timeStamp = timeStamp;
    }

    public int getValue(){
        return this.value;
    }
    public void setValue(int value){
        this.value = value;
    }

    public int getTimeStamp(){
        return this.timeStamp;
    }

    public void setTimeStamp(int timeStamp){
        this.timeStamp = timeStamp;
    }



}