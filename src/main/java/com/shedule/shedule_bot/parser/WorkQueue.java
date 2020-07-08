package com.shedule.shedule_bot.parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WorkQueue {
    private final int nThreads;
    private final PoolWorker[] threads;
    private final LinkedList queue;
    // пока список не пуст, значит задачи выполняются
    public List<Integer> isExecuting = new ArrayList<>();

    public boolean isQueueEmpty() {
        return queue.isEmpty() & (isExecuting.size() == 0);
    }


    public void stopThreads(){
        for (PoolWorker thread : threads) {
            thread.killThread();
        }
    }

    public WorkQueue(int nThreads) {
        this.nThreads = nThreads;
        queue = new LinkedList();
        threads = new PoolWorker[nThreads];

        for (int i = 0; i < nThreads; i++) {
            threads[i] = new PoolWorker();
            threads[i].start();
        }
    }

    public void execute(Runnable r) {
        synchronized (queue) {
            queue.addLast(r);
            queue.notify();
        }
    }

    private class PoolWorker extends Thread {
        public boolean alive = true;

        public void killThread() {
            synchronized(queue){
                alive = false;
                queue.notifyAll();
            }
        }

        public void run() {
            Runnable r;

            while (alive) {
                synchronized (queue) {
                    while (queue.isEmpty() && alive) {
                        try {
                            queue.wait();
                        } catch (InterruptedException ignored) {
                        }
                        if(!alive)
                            break;
                    }
                    if(!alive)
                        break;

                    r = (Runnable) queue.removeFirst();
                    // помечаем, что задача выполняется
                    isExecuting.add(1);
                }

                // If we don't catch RuntimeException,
                // the pool could leak threads
                try {
                    r.run();
                    // убираем из списка выполняемых задач одну из

                } catch (RuntimeException e) {
                    // You might want to log something here
                }
            }
        }
    }
}