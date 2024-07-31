import java.util.*;

public class dev2Q1V2Worker implements Runnable{

    private int start;
    private int end;

    private static List<Integer> result = new ArrayList<Integer>();


    public dev2Q1V2Worker(int start,int end){

        this.start = start;
        this.end = end;

    }


    public void run(){
        //System.out.println("I am a worker thread with");
        //System.out.println(start);
        //System.out.println(end);

        for(int i = this.start;i<=this.end;i++){
            if(isPrime(i) == true){
                synchronized(result){
                    result.add(i);
                }
                //System.out.println(i);

                try{
                    Thread.sleep(new Random().nextInt(200) + 100);
                }catch(InterruptedException e){
                    System.out.println("Interrupted exception");
                }
            }
        }
    }


    public boolean isPrime(int value){

        for(int i = 2;i<=Math.sqrt(value);i++){
            if(value % i == 0){
                return false;
            }
        }
        return true;
    }

    public static List<Integer> getResult(){
        return result;
    }

}