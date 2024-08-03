// Amani Louendriz

import java.util.LinkedList;
import java.util.List;



public class QueueSemaphore{
    private int s;// how many threads can access the critical section
    private LinkedList<Thread> waitingQueue;


    public QueueSemaphore(int s){
        this.s = s;
        this.waitingQueue = new LinkedList<>();
    }

    public void acquire() throws InterruptedException {
        synchronized (this) {
            s--;
            if(s<0){
                Thread currentThread = Thread.currentThread();
                this.waitingQueue.add(currentThread);
                this.wait();
            }

        }
    }

    public void release() {
        synchronized (this) {
            s++;

            if(s<=0){
                Thread nextThread = this.waitingQueue.poll();
                synchronized (nextThread) {
                    nextThread.notify();
                }

            }
        }
    }

    public int getQueueLength(){
        synchronized(this){
            return this.waitingQueue.size();
        }
    }
}