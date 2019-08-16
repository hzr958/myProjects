package com.smate.center.job;

import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.common.enums.JobWeightEnum;
import com.smate.center.job.common.exception.ServiceException;
import com.smate.center.job.common.po.OfflineJobPO;
import com.smate.center.job.common.po.OnlineJobConfigPO;
import com.smate.center.job.common.po.OnlineJobPO;
import com.smate.center.job.common.service.JobService;
import com.smate.center.job.common.support.JobCreator;
import com.smate.center.job.framework.service.OfflineJobService;
import com.smate.center.job.framework.service.OnlineJobConfigService;
import com.smate.center.job.framework.service.OnlineJobService;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.file.service.ArchiveFileService;
import com.smate.core.base.utils.constant.DBSessionEnum;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author houchuanjie
 * @date 2018/05/04 14:30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-test-job-curd.xml", "classpath:spring/applicationContext-zookeeper.xml" })
public class JobTest {
    //@Autowired
    private OfflineJobService offlineJobService;
    @Autowired
    private OnlineJobService onlineJobService;
    @Autowired
    private OnlineJobConfigService onlineJobConfigService;
    @Autowired
    private ArchiveFileService archiveFileService;
    @Autowired
    private JobService jobService;

    @Test
    public void addOfflineJob() throws ServiceException {
        OfflineJobPO offlineJobPO = new OfflineJobPO();
        offlineJobPO.setJobName("OfflineJobRunnerExample");
        offlineJobPO.setBegin(1L);
        offlineJobPO.setEnd(1000L);
        offlineJobPO.setThreadCount(10);
        offlineJobPO.setDbSessionEnum(DBSessionEnum.SNS);
        offlineJobPO.setTableName("PUBLICATION");
        offlineJobPO.setUniqueKey("PUB_ID");
        offlineJobPO.setEnable(true);
        offlineJobPO.setWeight(JobWeightEnum.A);
        offlineJobPO.setPriority(99);
        offlineJobPO.setStatus(JobStatusEnum.UNPROCESS);
        offlineJobService.saveOrUpdate(offlineJobPO);
    }

    @Test
    public void addOnlineJobs() {
        int count = 25000;
        List<OnlineJobPO> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            OnlineJobPO onlineJobPO = new OnlineJobPO();
            onlineJobPO.setJobName("HelloJobA");
            onlineJobPO.setStatus(JobStatusEnum.UNPROCESS);
            onlineJobPO.setPriority(99);
            list.add(onlineJobPO);
        }

        for (int i = 0; i < count; i++) {
            OnlineJobPO onlineJobPO = new OnlineJobPO();
            onlineJobPO.setJobName("HelloJobB");
            onlineJobPO.setStatus(JobStatusEnum.UNPROCESS);
            onlineJobPO.setPriority(99);
            list.add(onlineJobPO);
        }

        for (int i = 0; i < count; i++) {
            OnlineJobPO onlineJobPO = new OnlineJobPO();
            onlineJobPO.setJobName("HelloJobC");
            onlineJobPO.setStatus(JobStatusEnum.UNPROCESS);
            onlineJobPO.setPriority(99);
            list.add(onlineJobPO);
        }

        for (int i = 0; i < count; i++) {
            OnlineJobPO onlineJobPO = new OnlineJobPO();
            onlineJobPO.setJobName("HelloJobD");
            onlineJobPO.setStatus(JobStatusEnum.UNPROCESS);
            onlineJobPO.setPriority(99);
            list.add(onlineJobPO);
        }

        onlineJobService.batchSave(list);
    }

    @Test
    public void addOnlineJobConfig() throws ServiceException {
        OnlineJobConfigPO onlineJobConfigPO = new OnlineJobConfigPO();
        onlineJobConfigPO.setEnable(true);
        onlineJobConfigPO.setJobName("thumbnailJob");
        onlineJobConfigPO.setPriority(99);
        onlineJobConfigService.saveOrUpdate(onlineJobConfigPO);
    }

    @Test
    public void addOnlineJob() {
        ArchiveFile archiveFile = archiveFileService.getArchiveFileById(1000000008866L);
        SimpleEntry<String, Long> entry = new SimpleEntry<>("pub_id", 1200000038925L);
        for (int i = 0; i < 100; i++) {
            OnlineJobPO thumbnailJob = JobCreator.createThumbnailJob(archiveFile, FileTypeEnum.SNS_FULLTEXT, entry);
            jobService.addJob(thumbnailJob);
        }
    }
}
