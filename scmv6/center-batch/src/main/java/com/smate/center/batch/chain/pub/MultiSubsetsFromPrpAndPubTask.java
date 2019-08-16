package com.smate.center.batch.chain.pub;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.context.ApplicationContext;

import com.smate.center.batch.service.pdwh.prj.NsfcKeywordsService;
import com.smate.center.batch.service.pub.PdwhPubKeywordsService;
import com.smate.center.batch.util.pub.BatchApplicationContext;

public class MultiSubsetsFromPrpAndPubTask implements Runnable {
    // private static ApplicationContext applicationContext;
    private Long pubId;
    private String nsfcCategory;
    private StringBuffer sb;
    private CountDownLatch latch;
    private volatile Integer taskNum;

    public MultiSubsetsFromPrpAndPubTask(Long pubId, StringBuffer sb, CountDownLatch latch, String nsfcCategory) {
        this.pubId = pubId;
        this.sb = sb;
        this.latch = latch;
        this.nsfcCategory = nsfcCategory;
    }

    public MultiSubsetsFromPrpAndPubTask(Long pubId, StringBuffer sb, CountDownLatch latch, String nsfcCategory,
            Integer taskNum) {
        super();
        this.pubId = pubId;
        this.nsfcCategory = nsfcCategory;
        this.sb = sb;
        this.latch = latch;
        this.taskNum = taskNum;
    }

    public Integer getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(Integer taskNum) {
        this.taskNum = taskNum;
    }



    public Long getPubId() {
        return pubId;
    }

    public void setPubId(Long pubId) {
        this.pubId = pubId;
    }


    public StringBuffer getSb() {
        return sb;
    }

    public void setSb(StringBuffer sb) {
        this.sb = sb;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    public String getNsfcCategory() {
        return nsfcCategory;
    }

    public void setNsfcCategory(String nsfcCategory) {
        this.nsfcCategory = nsfcCategory;
    }

    private synchronized void increaseCount() {
        if (this.taskNum == null) {
            return;
        }
        this.taskNum = this.taskNum + 1;
        // System.out.println(pubId + "任务完成=====, taskCounts=" + this.taskNum);
    }

    @Override
    public void run() {
        ApplicationContext applicationContext = BatchApplicationContext.getApplicationContext();
        NsfcKeywordsService nsfcKeywordsService = applicationContext.getBean(NsfcKeywordsService.class);
        PdwhPubKeywordsService pdwhPubKeywordsService = applicationContext.getBean(PdwhPubKeywordsService.class);
        try {
            Map<String, List<String>> kwListMap = nsfcKeywordsService.getExtractZhKwsFromPrjStr(pubId);
            if (kwListMap == null || kwListMap.size() <= 0) {
                nsfcKeywordsService.updateScmPubSubsetsStatus(pubId, 2, 2, 0L);
                return;
            }
            List<String> zhKws = kwListMap.get("zhKws");
            List<String> enKws = kwListMap.get("enKws");
            List<String> categoryStr = kwListMap.get("category");
            String discode = "";
            if (categoryStr != null && categoryStr.size() > 0) {
                discode = categoryStr.get(0);
            }
            Long subSetSize = 0L;
            if (zhKws != null && zhKws.size() > 1) {
                Map<String, Object> rsMap = pdwhPubKeywordsService.getAllSubsetsString(zhKws, discode, pubId);
                if (rsMap != null) {
                    subSetSize = (Long) rsMap.get("subSetNum") + subSetSize;
                    if (rsMap.get("subSetString") != null
                            && StringUtils.isNotEmpty((String) rsMap.get("subSetString"))) {
                        this.sb.append((String) rsMap.get("subSetString"));
                    }
                }
            }
            if (enKws != null && enKws.size() > 1) {
                Map<String, Object> rsMap = pdwhPubKeywordsService.getAllSubsetsString(enKws, discode, pubId);
                if (rsMap != null) {
                    subSetSize = (Long) rsMap.get("subSetNum") + subSetSize;
                    if (rsMap.get("subSetString") != null
                            && StringUtils.isNotEmpty((String) rsMap.get("subSetString"))) {
                        this.sb.append((String) rsMap.get("subSetString"));
                    }
                }
            }
            nsfcKeywordsService.updateScmPubSubsetsStatus(pubId, 2, 1, subSetSize);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(pubId + "任务发生错误=====");
            nsfcKeywordsService.updateScmPubSubsetsStatus(pubId, 2, 3, 0L);
        } finally {
            latch.countDown();
        }
    }
}
