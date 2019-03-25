package com.twq.zk.usage.config;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 将配置发布到zk集群中的节点/config/app/dbcfg下
 * 现在zk中创建节点：
 * create /config ""
 * create /config/app ""
 */
public class ConfigPublisher implements Watcher {

    public static String ROOT_PATH = "/config";

    public static String APP_ZK_PATH = "/app";

    public static String DB_CFG_PATH = ROOT_PATH + APP_ZK_PATH +"/dbcfg";

    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {

        zooKeeper = new ZooKeeper("master:2181", 5000, new ConfigPublisher());
        countDownLatch.await();

        String config = "driverClassName=com.mysql.jdbc.Driver&" +
                "url=jdbc:mysql://192.168.100.133:3306/mydatabase&" +
                "username=root&password=root";

        if (zooKeeper.exists(DB_CFG_PATH, false) == null) {
            zooKeeper.create(DB_CFG_PATH, config.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } else {
            zooKeeper.setData(DB_CFG_PATH, config.getBytes(), -1);
        }
        System.out.println("发布db配置成功");
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getState() == Event.KeeperState.SyncConnected) {
            countDownLatch.countDown();
        }
    }


}
