package com.twq.zk.usage.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

public class AppWithCurator {

    public static void main(String[] args) throws Exception {
        CuratorFramework client =
                CuratorFrameworkFactory.newClient("master:2181",
                        new ExponentialBackoffRetry(1000, 3));

        client.start();

        InterProcessMutex lock = new InterProcessMutex(client, "/locks");
        if (lock.acquire(30, TimeUnit.SECONDS)) {
            try {
                System.out.println("Do Some thing With DataBase");

                Long sleepDuration = Long.parseLong(args[0]);

                TimeUnit.SECONDS.sleep(sleepDuration);
            } finally {
                lock.release();
            }
        }
    }
}
