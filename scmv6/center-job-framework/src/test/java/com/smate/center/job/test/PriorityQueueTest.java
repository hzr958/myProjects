package com.smate.center.job.test;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

import org.apache.curator.framework.recipes.queue.DistributedPriorityQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.common.po.OfflineJobPO;

/**
 * zookeeper 优先级队列测试类
 *
 * @author houchuanjie
 * @date 2018/04/08 09:30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-zookeeper.xml" })
public class PriorityQueueTest {
    @Autowired
    private DistributedPriorityQueue<OfflineJobPO> unallocatedTaskPriorityQueue;

    @Test
    public void test() throws Exception {
        Random random = new Random(Instant.now().toEpochMilli());
        for (int i = 0; i < 10; i++) {
            OfflineJobPO jobInfo = new OfflineJobPO();
            jobInfo.setId(UUID.randomUUID().toString().replace("-", ""));
            jobInfo.setJobName("job-" + i);
            jobInfo.setStatus(JobStatusEnum.UNPROCESS);
            jobInfo.setPriority(random.nextInt(100));
            unallocatedTaskPriorityQueue.put(jobInfo, jobInfo.getPriority());
            Thread.sleep(random.nextInt(1000));
        }
        unallocatedTaskPriorityQueue.getLastMessageCount();
    }
}
