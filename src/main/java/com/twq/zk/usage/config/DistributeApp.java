package com.twq.zk.usage.config;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * java -cp zookeeper-course-1.0-SNAPSHOT-jar-with-dependencies.jar com.twq.zk.usage.config.DistributeApp
 */
public class DistributeApp {

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        ConfigManager configManager = new ConfigManager(ConfigPublisher.DB_CFG_PATH);

        while (true) {
            TimeUnit.SECONDS.sleep(5);
            Map<String, String> configMap = configManager.getConfigMap();
            System.out.println("Do Some Thing With config : " + configMap);
        }

    }
}
