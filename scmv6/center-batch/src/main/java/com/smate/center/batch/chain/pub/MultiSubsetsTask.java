package com.smate.center.batch.chain.pub;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.context.ApplicationContext;

import com.smate.center.batch.service.pdwh.prj.NsfcKeywordsService;
import com.smate.center.batch.service.pub.PdwhPubKeywordsService;
import com.smate.center.batch.util.pub.BatchApplicationContext;

public class MultiSubsetsTask implements Runnable {
    // private static ApplicationContext applicationContext;
    private Long pubId;
    private String nsfcCategory;
    private StringBuffer sb;
    private CountDownLatch latch;
    private volatile Integer taskNum;

    public MultiSubsetsTask(Long pubId, StringBuffer sb, CountDownLatch latch, String nsfcCategory) {
        this.pubId = pubId;
        this.sb = sb;
        this.latch = latch;
        this.nsfcCategory = nsfcCategory;
    }

    public MultiSubsetsTask(Long pubId, StringBuffer sb, CountDownLatch latch, String nsfcCategory, Integer taskNum) {
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
            List<String> kwList = nsfcKeywordsService.getExtractKwsFromStr(pubId);
            if (kwList == null || kwList.size() <= 0) {
                nsfcKeywordsService.updateScmPubSubsetsStatus(pubId, 1, 2, 0L);
                return;
            }
            if (kwList.size() == 1) {
                nsfcKeywordsService.updateScmPubSubsetsStatus(pubId, 1, 4, 0L);
                return;
            }
            /*
             * Date start = new Date(); if (kwList.size() > 15) { System.out.println(pubId + "关键词子集计算开始: " +
             * start); }
             */
            String discode = nsfcKeywordsService.getPubNsfcCategory(pubId);
            if (StringUtils.isNotBlank(discode) && discode.length() >= 3) {
                discode = discode.substring(0, 3);
            }
            Long subSetSize = 0L;
            Map<String, Object> rsMap = pdwhPubKeywordsService.getAllSubsetsString(kwList, discode, pubId);
            if (rsMap != null) {
                subSetSize = (Long) rsMap.get("subSetNum");
                if (rsMap.get("subSetString") != null && StringUtils.isNotEmpty((String) rsMap.get("subSetString"))) {
                    this.sb.append((String) rsMap.get("subSetString"));
                }
            }
            /*
             * Date middle = new Date(); if (kwList.size() > 15) { System.out.println(pubId + "关键词长度为" +
             * kwList.size() + ",其子集计算开始时间: " + start + ", 结束时间: " + middle + ", 计算用时: " + (middle.getTime() -
             * start.getTime()) + "，关键词子集数量为：" + subSetSize); }
             */
            // 需要分两次存储，方便以后计算：带discode与不带discode
            /*
             * Long slp = new Random().nextInt(10) * 1L; Thread.sleep(slp); System.out.println(name + "任务读取数据："
             * + readline + "====等待时间: " + slp);
             */
            // 不带discode
            /*
             * for (String kwSubset : subsetString) { sb.append(pubId.toString()); sb.append("-!");
             * sb.append(kwSubset); sb.append("\r\n"); }
             */
            // 带discode
            /*
             * if (StringUtils.isNotBlank(nsfcCategory)) { for (String kwSubset : subsetString) {
             * sb.append(pubId.toString()); sb.append("-!"); sb.append(nsfcCategory); sb.append("-!");
             * sb.append(kwSubset); sb.append("\r\n"); } }
             */
            /*
             * Date end = new Date(); if (kwList.size() > 15) { System.out.println(pubId + "关键词长度为" +
             * kwList.size() + ",其子集计算开始时间: " + start + ", 结束时间: " + middle + ", 计算用时: " + (middle.getTime() -
             * start.getTime()) + "，字符处理时间为: " + (end.getTime() - middle.getTime())); }
             */
            nsfcKeywordsService.updateScmPubSubsetsStatus(pubId, 1, 1, subSetSize);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(pubId + "任务发生错误=====");
            nsfcKeywordsService.updateScmPubSubsetsStatus(pubId, 1, 3, 0L);
        } finally {
            latch.countDown();
        }
    }
}
