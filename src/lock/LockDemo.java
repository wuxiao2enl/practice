package lock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 *
 * @author enlighten 2020-03-19
 */
public class LockDemo {

    int i = 0;
    //同步代码块
    public void add(){
        synchronized (this){
            i++;
        }
    }

    //锁
    Lock  lock = new ReentrantLock();
    public void add1(){
        lock.lock();
        try{
            i++;
        }finally {
            lock.unlock();
        }
    }

    //原子操作API
    AtomicInteger integer = new AtomicInteger(0);
    public void add2(){
        integer.incrementAndGet();
    }

    public static void main(String[] args) throws InterruptedException {
        LockDemo ld = new LockDemo();

        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    ld.add();
                }
            }).start();
        }
        Thread.sleep(2000L);
        System.out.println(ld.i);
    }
}
