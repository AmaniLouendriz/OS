// Amani Louendriz

import java.util.concurrent.*;
import java.util.Random;

public class Student implements Runnable{
    private int id;
    private QueueSemaphore chairs;
    private TA ta;
    private Semaphore notifyTa;
    private boolean isNotified;
    private Semaphore waitTA;


    public Student(int id,QueueSemaphore chairs,TA ta,Semaphore notifyTa,boolean isNotified,Semaphore waitTA){
        this.chairs = chairs;
        this.id = id;
        this.ta = ta;
        this.notifyTa = notifyTa;
        this.isNotified = isNotified;
        this.waitTA = waitTA;
    }

    @Override

    public void run(){

        try{
            while(true){
                // Simuler le temps de programmation de l'etudiant
                System.out.println("Student " + this.id + " is programming.");
                Thread.sleep(new Random().nextInt(3000) + 1000);// duration of programming
                System.out.println("Student " + this.id + " needs help!!");

                this.notifyTa.acquire();
                

                if(chairs.getQueueLength() >= 1 && chairs.getQueueLength() < 3){// students are already waiting outside, and a at least one place is available
                    System.out.println("Student " + this.id + " sits and waits.");
                    this.notifyTa.release();
                    this.chairs.acquire();

                    System.out.println("Student " + this.id + " goes to see the TA now.");// this never got executed

                }else if(chairs.getQueueLength() == 3){
                    System.out.println("Student " + this.id + " will come back later.");// no place is available
                    this.notifyTa.release();
                    Thread.sleep(new Random().nextInt(3000) + 1000);
                }else if(chairs.getQueueLength() == 0 && ta.getIsNotified() == false){
                    this.notifyTa.release();
                    this.chairs.acquire();// this one will access directly
                    System.out.println("Student " + this.id + " wakes up the TA.");// this is a critical section; that's why it's surrounded by aquire and signal()
                    synchronized(ta){
                        ta.notify();
                        ta.setNotified(true);
                    }
                    //it shouldn't continue executing before the TA responds
                    this.waitTA.acquire();
                    // Reset the semaphore to 0
                    this.waitTA.drainPermits();
                }else if(chairs.getQueueLength() == 0 && ta.getIsNotified() == true){
                    System.out.println("Student " + this.id + " sits and waits.");
                    this.notifyTa.release();
                    this.chairs.acquire();// this one will be put in the queue

                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }


}