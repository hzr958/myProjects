package com.smate.center.job.test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * @author houchuanjie
 * @date 2018/03/31 18:59
 */

public class LeaderLatchTest{

    private static final int CLIENT_QTY = 10;
    private static final String PATH = "/examples/leader";

    @Test
    public void leaderLatchCompete() throws Exception {
        List<LeaderLatch> leaderLatchList = Lists.newArrayList();
        List<CuratorFramework> curatorList = Lists.newArrayList();
        for (int i = 0; i < CLIENT_QTY; i++) {
            CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
            curatorList.add(client);
            LeaderLatch leaderLatch = new LeaderLatch(client, PATH, "Client #" + i);
            leaderLatchList.add(leaderLatch);
            client.start();
            leaderLatch.start();
        }
        Thread.sleep(20000);
        leaderLatchList.forEach(leader -> {
            if(leader.hasLeadership()){
                System.out.println("当前的leader是：" + leader.getId());
                System.out.println(leader.getId() + "已放弃leader权利！");
                try {
                    leader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        leaderLatchList.get(0).await(2, TimeUnit.SECONDS);
        System.out.println("Client #0 可能会被选举为leader，但也有可能不是即使它想成为leader。");
        System.out.println("新的leader是："+leaderLatchList.get(0).getLeader().getId());
    }
}
