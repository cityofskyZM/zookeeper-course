package com.twq.zk;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ZNodeCreationEphemeral implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("master:2181", 5000, new ZNodeCreationEphemeral());

        countDownLatch.await();

        //创建临时节点
        String path1 = zooKeeper.create("/app3/p_2",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);

        System.out.println("succ create node : " + path1);

        //创建临时有顺序节点
        String path2 = zooKeeper.create("/app3/p_3",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);

        System.out.println("succ create node : " + path2);

        String path3 = zooKeeper.create("/app3/p_3",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);

        System.out.println("succ create node : " + path3);

        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);

    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == Event.EventType.None && Event.KeeperState.SyncConnected == event.getState()) {
            countDownLatch.countDown();
        }
    }
}
