// Amani Louendriz

import java.util.concurrent.*;

import java.util.Random;




public class TA implements Runnable{
    private QueueSemaphore chairs;
    private Semaphore wakeUpTA;
    private Semaphore notifyTa;
    private boolean isNotified;
    private Semaphore waitTA;


    public TA(QueueSemaphore chairs,Semaphore notifyTa,boolean isNotified,Semaphore waitTA){
        this.chairs = chairs;
        this.notifyTa = notifyTa;
        this.isNotified = isNotified;
        this.waitTA = waitTA;

    }


    public void setNotified(boolean v){
        this.isNotified = v;
    }

    public boolean getIsNotified(){
        return this.isNotified;
    }

    public void run(){

        try{
            while(true){
                if(chairs.getQueueLength() == 0 && this.isNotified == false){
                    System.out.println("No student is waiting. TA takes a nap. zzz");
                    synchronized(this){
                        this.wait();// TA sleeps until a student wakes him up. 
                    }
                }
                if(chairs.getQueueLength()>=1 ){
                    this.chairs.release();
                    System.out.println("The TA is helping a student.");
                    Thread.sleep(new Random().nextInt(2000) + 1000); // Duration of help
                    System.out.println("The TA finished helping.");
                    this.isNotified = false;
                }
                if(this.isNotified == true){
                    System.out.println("The TA is helping a student.");
                    Thread.sleep(new Random().nextInt(3000) + 1000); // Duration of help
                    System.out.println("The TA finished helping.");
                    this.waitTA.release();
                    this.isNotified = false;
                }
            }

        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}