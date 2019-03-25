package com.twq.zk;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZNodeCreationPersistent implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("master:2181", 5000, new ZNodeCreationPersistent());

        countDownLatch.await();

        //创建持久节点
       /** String path1 = zooKeeper.create("/app3",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);

        System.out.println("succ create node : " + path1);**/

        //创建一个持久有顺序的节点
        String path2 = zooKeeper.create("/app3/p_1_lock_",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT_SEQUENTIAL);

        System.out.println("succ create node : " + path2);

        String path3 = zooKeeper.create("/app3/p_1_lock_",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT_SEQUENTIAL);

        System.out.println("succ create node : " + path3);



    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == Watcher.Event.EventType.None && Watcher.Event.KeeperState.SyncConnected == event.getState()) {
            countDownLatch.countDown();
        }
    }
}
