package cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author enlighten 2020-03-19
 */
public class CyclicBarrierDemo {
    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingQueue<String > sqls = new LinkedBlockingQueue<>();

        CyclicBarrier barrier = new CyclicBarrier(4,()->{
            System.out.println("有四个线程了，开始插入："+Thread.currentThread());
            for (int i = 0;i<4;++i){
                System.out.println(sqls.poll());
            }
        });

        for(int i =0;i<12;++i){
            new Thread(()->{
                try{
                    sqls.add("data - "+ Thread.currentThread());
                    Thread.sleep(1000L);
                    barrier.await();
                    System.out.println(Thread.currentThread()+ "插入完毕");
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        Thread.sleep(2000L);
    }
}
