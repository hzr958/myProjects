package com.smate.center.job.test;

import com.smate.center.job.framework.support.JobConfig;
import java.util.Objects;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.smate.center.job.framework.zookeeper.leader.LeaderCompetitor;
import com.smate.center.job.framework.zookeeper.service.ZKClientService;

/**
 * LeaderDispatch服务测试类
 *
 * @author houchuanjie
 * @date 2018/04/03 11:04
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class LeaderCompetitorTest {
    @Autowired
    private LeaderCompetitor leaderDispatch;
    @Autowired
    private ZKClientService clientService;
    @Test
    public void test() throws InterruptedException {
        clientService.blockUntilConnected();
        assert Objects.nonNull(leaderDispatch.getCurrentServerID()) : "current server id cannot be null.";
        assert Objects.nonNull(JobConfig.getCurrentServerNode()) : "current server node cannot be null.";
        System.out.println("当前服务器节点：" + JobConfig.getCurrentServerNode());
        Thread.sleep(60000);
    }

}
