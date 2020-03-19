package semaphore;

import aqs.EntAqs;

/**
 * @author enlighten 2020-03-19
 */
public class EntSemaphore {
    EntAqs aqs = new EntAqs(){
        @Override
        public int tryAcquireShared() {
            //不能直接减，可能会变成负数
            //return getState().decrementAndGet();
            for (; ; ) {
                int count = getState().get();
                int n = count - 1;
                if (count <= 0 || n < 0) {
                    return -1;
                }
                if (getState().compareAndSet(count, n)) {
                    return 1;
                }
            }
        }

        @Override
        public boolean tryReleaseShared() {
            return getState().incrementAndGet()>=0;
        }
    };

    public EntSemaphore(int count) {
        aqs.getState().set(count); // 设置资源的状态
    }

    public void acquire() {
        aqs.acquireShared();
    } // 获取令牌

    public void release() {
        aqs.releaseShared();
    } // 释放令牌
}
