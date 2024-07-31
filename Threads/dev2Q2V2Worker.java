public class dev2Q2V2Worker implements Runnable{

    private int start;
    private int end;
    private int id;

    private static int[] fibonnaci = new int[dev2Q2V2.getUserNumber()];// shared array by all threads

    private static int counter = 0;


    public dev2Q2V2Worker(int start,int end){
        this.start = start;
        this.end = end;
        counter++;// used to calculate the id
        this.id = counter;       
    }

    public synchronized static void fibonnacci(int start,int end,int id ){
        //System.out.println("I am a worker "+id + "starts from: "+start );


         for(int i = start;i<=end;i++){
                if(i == 0){
                    fibonnaci[i] = 0;
                }else if(i == 1){
                    fibonnaci[i] = 1;
                }else{
                    fibonnaci[i] = fibonnaci[i-1] + fibonnaci[i-2];
                }
                System.out.println("fib["+ i+ "] "+ fibonnaci[i]);
            }

        //System.out.println("I am a worker "+id + "end "+ end );


    }

   
    
    public void run(){

        if (this.start != 0 && this.start != 1){// we arent in the case of the first 2 elemts being calculated
            for (int i = 2; i< this.start;i++){
                if (fibonnaci[i] == 0){
                    try{
                        Thread.sleep(1000);// wait for previous values to be calculated
                    }catch(InterruptedException e){
                        System.out.println("Interrupted Exception");
                    }

                }
            }
        }
        fibonnacci(this.start,this.end,this.id);  
    }

    public static int[] getList(){
        return fibonnaci;
    }



}