package com.twq.zk.watch;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ChildrenGetter implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper("master:2181", 5000, new ChildrenGetter());
        countDownLatch.await();

        String path = "/app4";
        
        zooKeeper.create(path + "/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        List<String> childrenList = zooKeeper.getChildren(path, true);
        System.out.println(childrenList);

        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);

    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.None && watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            countDownLatch.countDown();
        } else if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
            try {
                //这边一定要再次注册，要不然就不会再watch了
                System.out.println("Get Children : " + zooKeeper.getChildren(watchedEvent.getPath(), true));
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
