package com.smate.center.batch.quartz;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.pdwh.prj.NsfcKeywordsService;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 通过拆分NSFC项目与申请书中英文关键词获取对应的翻译，出现次数大于1，则默认为正确
 * 
 */
public class NsfcKwsTranslateBySeqTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final static Integer SIZE = 2000; // 每次刷新获取的个数

    @Autowired
    private NsfcKeywordsService nsfcKeywordsService;

    public void run() throws BatchTaskException {
        logger.debug("====================================NsfcKwsTranslateBySeqTask===开始运行");
        if (isRun() == false) {
            logger.debug("====================================NsfcKwsTranslateBySeqTask===开关关闭");
            return;
        } else {
            try {
                doRun();
            } catch (BatchTaskException e) {
                logger.error("NsfcKwsTranslateBySeqTask,运行异常", e);
                throw new BatchTaskException(e);
            }
        }
    }

    public void doRun() throws BatchTaskException {
        List<BigDecimal> pdwhPubIdList = nsfcKeywordsService.getSubsetsToHandleKwList(SIZE, 3);
        if (pdwhPubIdList == null || pdwhPubIdList.size() == 0) {
            logger.info("====================================NsfcKwsTranslateBySeqTask===运行完毕");
            return;
        }
        for (BigDecimal id : pdwhPubIdList) {
            Long prjId = id.longValue();
            Integer rs = 0;
            try {
                rs = this.nsfcKeywordsService.getTranslateNsfcPrjKwsBySeq(prjId);
                this.nsfcKeywordsService.updateScmPubSubsetsStatus(prjId, 3, rs, 1L);
            } catch (Exception e) {
                logger.error("NSFC项目与申请书中英文关键词获取对应的翻译,prjId:" + prjId, e);
                this.nsfcKeywordsService.updateScmPubSubsetsStatus(prjId, 3, 3, 1L);
            }
        }

    }

    public boolean isRun() throws BatchTaskException {
        return true;
    }
}
