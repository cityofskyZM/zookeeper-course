package com.twq.zk.watch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ExsitTest implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper("master:2181", 5000, new ExsitTest());
        countDownLatch.await();

        Stat stat = zooKeeper.exists("/app5", true);

        System.out.println(stat);

        if (stat != null) {
            System.out.println(stat.getCzxid());
        }

        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            if (watchedEvent.getType() == Event.EventType.None && watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                countDownLatch.countDown();
            } else if (watchedEvent.getType() == Event.EventType.NodeCreated) {
                System.out.println("Node(" + watchedEvent.getPath() + ")Created");
                zooKeeper.exists(watchedEvent.getPath(), true);
            } else if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                System.out.println("Node(" + watchedEvent.getPath() + ")Deleted");
                zooKeeper.exists(watchedEvent.getPath(), true);
            } else if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
                System.out.println("Node(" + watchedEvent.getPath() + ")DataChanged");
                zooKeeper.exists(watchedEvent.getPath(), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
