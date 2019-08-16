package com.smate.center.job.test;

import com.smate.center.job.framework.zookeeper.config.ZKConfig;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.smate.center.job.framework.zookeeper.listener.NodeIDSharedCountListener;

/**
 * 测试共享计数器
 *
 * @author houchuanjie
 * @date 2018/04/02 14:23
 */
public class SharedCountTest {
    private static final int CLIENT_QTY = 10;
    private static final String PATH = "/examples/counter";

    @Test
    public void test() throws InterruptedException {
        List<SharedCount> counterList = Lists.newArrayList();
        List<CuratorFramework> curatorList = Lists.newArrayList();
        for (int i = 0; i < CLIENT_QTY; i++) {

        }
        ExecutorService service = Executors.newFixedThreadPool(CLIENT_QTY);
        for (int i = 0; i < CLIENT_QTY; ++i) {
            CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
            client.start();
            curatorList.add(client);
            SharedCount counter = new SharedCount(client, PATH, ZKConfig.NODE_COUNTER_INITIAL_VALUE);
            counter.addListener(new NodeIDSharedCountListener());
            counterList.add(counter);
            Callable<Void> task = new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    counter.start();
                    Thread.sleep(new Random().nextInt(10000));
                    System.out.print("增长前的值：" + counter.getCount());
                    System.out.print("\tIncrement: " + counter.trySetCount(counter.getVersionedValue(), counter.getCount() + 1));
                    System.out.println("\t增长后的值：" + counter.getCount());
                    counter.close();
                    return null;
                }
            };
            service.submit(task);

        }
        service.shutdown();
        service.awaitTermination(10, TimeUnit.MINUTES);
        for (int i = 0; i < CLIENT_QTY; i++) {
            curatorList.get(i).close();
        }
    }
}
