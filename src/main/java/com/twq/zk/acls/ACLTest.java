package com.twq.zk.acls;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ACLTest implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper("master:2181", 5000, new ACLTest());
        countDownLatch.await();

        zooKeeper.addAuthInfo("digest", "foo:true".getBytes());

        zooKeeper.create("/app5", "".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);

        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getState() == Event.KeeperState.SyncConnected && event.getType() == Event.EventType.None) {
            countDownLatch.countDown();
        }
    }
}
