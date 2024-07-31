import java.util.*;

// Amani Louendriz
// 300218319

public class dev2Q1V2{
    
    public static void main(String[] args){

        try{

            if(args.length == 1){
                int userNumber = Integer.parseInt(args[0]);

                // dividing the interval

                int originStart = 2;
                int originEnd = userNumber;
                int originIntervalSize = userNumber - originStart + 1;
                int nbreThread = computeRequiredThreads(originIntervalSize);
                int subIntervalSize = originIntervalSize / nbreThread;

                System.out.println("Starting "+nbreThread+" threads with the value: "+userNumber);




                // pool of threads

                Thread[] poolOfThreads = new Thread[nbreThread];

                for (int i = 0; i<poolOfThreads.length;i++){

                    int start = originStart + (i*subIntervalSize);

                    int end = start + subIntervalSize - 1; 

                    if (i == nbreThread - 1){// the case of the last thread
                        end = originEnd;
                    }

                    //System.out.println(i);

                    //System.out.println(start);
                    //System.out.println(end);

                    dev2Q1V2Worker runner = new dev2Q1V2Worker(start,end);

                    Thread thread = new Thread(runner);

                    // lancer le thread
                    poolOfThreads[i] = thread;

                    thread.start();
                }

                for (int i = 0;i<poolOfThreads.length;i++){
                    poolOfThreads[i].join();
                }
                List<Integer> collectionResult = dev2Q1V2Worker.getResult();


                System.out.println("before being sorted");
                System.out.println(collectionResult);

                System.out.println("after being sorted");
                Collections.sort(collectionResult);
                System.out.println(collectionResult);
            }else{
                System.out.println("Please input an argument at args[0]");
            }
          
        }catch(NumberFormatException e){
            System.out.println("An error occured at input.");
        }catch(InterruptedException e){
            System.out.println("there was an interruption");
        }
    }


    public static int computeRequiredThreads(int value){
        int i = 50; // the maximum interval size to be processed by each thread.

        return value/i + 1;
    }
}