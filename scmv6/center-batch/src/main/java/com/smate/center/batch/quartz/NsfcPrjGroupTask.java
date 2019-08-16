package com.smate.center.batch.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.pdwh.prj.NsfcApplicationAutoAssignService;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 按照谱聚类算法对申请书分组
 * 
 */
public class NsfcPrjGroupTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NsfcApplicationAutoAssignService nsfcApplicationAutoAssignService;

    public void run() throws BatchTaskException {
        logger.debug("====================================NsfcPrjGroupTask===开始运行");
        if (isRun() == false) {
            logger.debug("====================================NsfcPrjGroupTask===开关关闭");
            return;
        } else {
            try {
                doRun();
            } catch (BatchTaskException e) {
                logger.error("KGPubKwsExtractTask,运行异常", e);
                throw new BatchTaskException(e);
            }
        }
    }

    public void doRun() throws BatchTaskException {
        try {
            this.nsfcApplicationAutoAssignService.spetralClustering();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public boolean isRun() throws BatchTaskException {
        // 任务开关控制逻辑
        return true;
    }

}
