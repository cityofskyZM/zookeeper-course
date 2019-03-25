package com.twq.zk.usage;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

/**
 * java -cp zookeeper-course-1.0-SNAPSHOT-jar-with-dependencies.jar com.twq.zk.usage.LeaderSelectionApp
 */
public class LeaderSelectionApp {
    public static void main(String[] args) throws InterruptedException {
        CuratorFramework client =
                CuratorFrameworkFactory.newClient("master:2181",
                        new ExponentialBackoffRetry(1000, 3));

        client.start();
        String masterPath = "/master";
        LeaderSelector selector = new LeaderSelector(client, masterPath, new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                System.out.println("成为Master角色");
                TimeUnit.SECONDS.sleep(5);
                System.out.println("退出master角色");
            }
        });

        selector.autoRequeue();
        selector.start();
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }
}
