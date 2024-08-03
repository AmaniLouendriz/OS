// Amani Louendriz

import java.util.*;

import java.util.concurrent.*;

public class Main{
    public static void main (String[] args){
        try{
            if(args.length == 1){
                int nbreStudents = Integer.parseInt(args[0]);

                QueueSemaphore chairs = new QueueSemaphore(1);// semaphore for the chairs in the hallway; accessing a chair is critical too
                // and should be done by one student at a time. It's a custom semaphore that has a queue for the processes that should wait.
                Semaphore notifyTa = new Semaphore(1);// when a student comes, he should first start by looking how many students are in 
                //waiting queue. This action is critical and should be done by one thread at  time; this semaphore is used to control this.
                boolean isNotified = false;// when a student notifies a TA; it changes this to true.
                Semaphore waitTA = new Semaphore(0);

                System.out.println("Starting "+nbreStudents+" students and one TA.");

                Thread[] studentThreads = new Thread[nbreStudents];

                TA worker = new TA(chairs,notifyTa,isNotified,waitTA);
                Thread taThread = new Thread(worker);
                taThread.start();



                for (int i = 0; i<nbreStudents;i++){
                    Student runner = new Student(i,chairs,worker,notifyTa,isNotified,waitTA);
                    Thread stThread = new Thread(runner);
                    studentThreads[i] = stThread;
                    stThread.start();
                }

            }else{
                System.out.println("Please enter the number of students you would like to create at args[0].");
            }

        }
        catch(Exception e){
             System.out.println("An  exception occured");
         }
        
    
    }
}