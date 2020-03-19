package lock;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * @author enlighten 2020-03-19
 * 实现一把锁，但是偷个懒使用queue
 */
public class EntLock implements Lock {
    public volatile AtomicReference<Thread> owner = new AtomicReference<>();

    public volatile LinkedBlockingQueue<Thread> waiter = new LinkedBlockingQueue<>();
    @Override
    public void lock() {
        boolean flag = true;
        while(!tryLock()){
            if(flag){
                // 塞到等待锁的集合中
                waiter.offer(Thread.currentThread());
                flag = false;
            }else{
                // 挂起这个线程
                LockSupport.park();
            }
        }
        waiter.remove(Thread.currentThread());
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        //通过CAS进行比较
        return owner.compareAndSet(null,Thread.currentThread());
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        // cas 修改 owner 拥有者
        if(owner.compareAndSet(Thread.currentThread(),null)) {
            for (Thread thread : waiter) {
                LockSupport.unpark(thread); // 唤醒线程继续 抢锁
            }
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
