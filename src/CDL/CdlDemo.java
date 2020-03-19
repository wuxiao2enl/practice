package CDL;

import aqs.AQSDemo;
import aqs.EntAqs;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author enlighten 2020-03-19
 * Cdl的实现以及测试案例
 * 	    await：方法等计数器变为0，在这之前，线程进入等待状态
 * 	    countdown：计数器值减一，直到为0
 * 	 可以联系线程通信中的wait/notify方法
 */
public class CdlDemo {
    AQSDemo aqsDemo = new AQSDemo(){


        @Override
        public int tryAcquireShared() {
            return this.getState().get() ==0?1:-1;
        }

        @Override
        public boolean tryReleaseShared() {
            return this.getState().decrementAndGet()==0;
        }
    };

    public CdlDemo(int count) {
        aqsDemo.setState(new AtomicInteger(count));
    }

    public void await() {
        aqsDemo.acquireShared();
    }

    public void countDown() {
        aqsDemo.releaseShared();
    }

    public static void main(String[] args) {
        CdlDemo cdlDemo = new CdlDemo(10);
        //启动十个线程先
        for(int i=0;i<10;i++){
            int finalI = i;
            new Thread(()->{
                try{
                    Thread.sleep(6000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("我是" + Thread.currentThread() + ".我执行接口-" + finalI +"调用了");
                cdlDemo.countDown();
            }).start();
        }
        cdlDemo.await();//
        System.out.println("出来吧神龙");
    }
}
