package com.twq.zk.usage.lock;

import java.util.concurrent.TimeUnit;

/**
 * java -cp zookeeper-course-1.0-SNAPSHOT-jar-with-dependencies.jar com.twq.zk.usage.lock.App
 */
public class App {

    public static void main(String[] args) throws InterruptedException {
        DistributedLock lock = new DistributedLock("master:2181", "app2");

        lock.lock();

        System.out.println("Do Some thing With DataBase");

        Long sleepDuration = Long.parseLong(args[0]);

        TimeUnit.SECONDS.sleep(sleepDuration);

        if (lock != null) {
            lock.unlock();
        }
    }
}
