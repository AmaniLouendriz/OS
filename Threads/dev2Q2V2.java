
//@Amani Louendriz  300218319

public class dev2Q2V2{

    private static int userNumber;

    public static void main(String[] args){
        try{
            if (args.length == 1){
                userNumber = Integer.parseInt(args[0]); 
                if(userNumber<0){
                    System.out.println("Please provide a positive number.");
                }else{
                    int originStart = 0;
                    int originEnd = userNumber-1;
                    int interval = originEnd - originStart + 1;
                    int n = computeRequiredThreads(interval);// number of threads
                    int subInterval = interval / n;
                    Thread[] poolOfThreads = new Thread[n];

                    for(int i = 0; i<n;i++){
                        int start = originStart + (i*subInterval);
                        int end = start + subInterval - 1;
                        if(i == n - 1){
                            end = originEnd;
                        }
                        dev2Q2V2Worker runner = new dev2Q2V2Worker(start,end);
                        Thread thread = new Thread(runner);
                        poolOfThreads[i] = thread;
                        thread.start();

                    }

                    for(int i = 0;i<poolOfThreads.length;i++){
                        poolOfThreads[i].join();
                    }
                    display(dev2Q2V2Worker.getList());

                }
            }

        }catch(NumberFormatException e){
            System.out.println("An error occured during conversion from string to int.");
        }catch(InterruptedException e){
            System.out.println("There was an interruption");
        }
       
    }


    public static void display(int[] list){
        for (int i = 0; i< list.length;i++){
            String s = list[i] + "\t";
            System.out.print(s);
        }
    }

    public static int computeRequiredThreads(int value){

        int i = 10;

        return value/i + 1;
    }


    public static int getUserNumber(){
        return userNumber;
    }






}