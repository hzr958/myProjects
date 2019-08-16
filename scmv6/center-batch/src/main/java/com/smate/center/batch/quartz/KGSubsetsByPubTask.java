package com.smate.center.batch.quartz;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.smate.center.batch.chain.pub.MultiSubsetsTask;
import com.smate.center.batch.service.pdwh.prj.NsfcKeywordsService;
import com.smate.center.batch.service.pub.GenerateAddrPsnConstDicService;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 计算pdwh成果子集，写入txt文件用于hadoop任务
 * 
 */
public class KGSubsetsByPubTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final static Integer SIZE = 5000; // 每次刷新获取的个数
    @Value("${subset.multitask.filepath}")
    private String filePath;
    @Value("${subset.multitask.tasknum}")
    private Integer taskNum;
    @Value("${subset.multitask.filesize}")
    private Long fileSize;
    @Autowired
    private NsfcKeywordsService nsfcKeywordsService;
    @Autowired
    private GenerateAddrPsnConstDicService generateAddrPsnConstDicService;

    public void run() throws BatchTaskException {
        logger.debug("====================================KGSubsetsByPubTask===开始运行");
        if (isRun() == false) {
            logger.debug("====================================KGSubsetsByPubTask===开关关闭");
            return;
        } else {
            try {
                doRun();
            } catch (BatchTaskException e) {
                logger.error("KGSubsetsByPubTask,运行异常", e);
                throw new BatchTaskException(e);
            }
        }
    }

    public void doRun() throws BatchTaskException {
        // this.generateAddrPsnConstDicService.generateNsfcKwsDic();
        // type =1 成果
        List<BigDecimal> pdwhPubIdList = nsfcKeywordsService.getSubsetsToHandleKwList(SIZE, 1);
        if (pdwhPubIdList == null || pdwhPubIdList.size() == 0) {
            logger.info("====================================KGSubsetsByPubTask===运行完毕");
            return;
        }

        ThreadPoolExecutor tpe = new ThreadPoolExecutor(taskNum, taskNum, 1000L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(2000));
        Integer sum = pdwhPubIdList.size() / 1000;
        Double fSum = pdwhPubIdList.size() / 1000.00;
        Integer count = 1;
        if (!(String.valueOf(fSum).equals(String.valueOf(sum) + ".0"))) {
            sum++;
        }
        while (count <= sum) {
            StringBuffer sb = new StringBuffer();
            Integer batch = 1000;
            Integer start = 1000 * (count - 1);
            Integer latchSize = 1000;
            if (sum == count) {
                batch = pdwhPubIdList.size();
                latchSize = batch - start;
            } else {
                batch = 1000 * count;
            }
            CountDownLatch latch = new CountDownLatch(latchSize);
            System.out.println("latchSize=" + latchSize);
            System.out.println("for(int i=" + start + "; i<" + batch + "; i++)");
            for (Integer i = start; i < batch; i++) {
                // List<String> kwList = nsfcKeywordsService.getExtractKwsFromStr(pdwhPubIdList.get(i).longValue());
                MultiSubsetsTask mst = new MultiSubsetsTask(pdwhPubIdList.get(i).longValue(), sb, latch, null);
                tpe.execute(mst);
            }

            try {
                System.out.println(new Date() + "====1等待所有子集计算完成====");
                latch.await();
                System.out.println(new Date() + "====2开始写入txt文件====");
                File f = this.getFileName(filePath);
                if (!f.exists()) {
                    f.createNewFile();
                }
                BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
                try {
                    bw.write(sb.toString());
                    bw.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    bw.close();
                    System.out.println(new Date() + "====3开始写入txt完成====");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(new Date() + "第" + count + "次====");
            count++;
        }
        // System.out.println(new Date() + "任务执行完毕====" );
        tpe.shutdown();
        System.out.println(new Date() + "KGSubsetsByPubTask成功线程池关闭======");
    }

    public boolean isRun() throws BatchTaskException {
        // 任务开关控制逻辑
        return true;
        // return
        // taskMarkerService.getApplicationQuartzSettingValue("PubToPubsimpleTask2")
        // == 1;
    }

    private File getFileName(String filePath) {
        String prefix = "subset_pub_";
        String name = "";
        Long time = (new Date()).getTime();
        File file = new File(filePath);
        File[] fileList = file.listFiles();
        if (fileList != null && fileList.length > 0) {
            List<Long> nameNum = new ArrayList<Long>();
            for (File f : fileList) {
                try {
                    String fileName = f.getName();
                    if (fileName.indexOf(prefix) < 0) {
                        continue;
                    }
                    String[] strs = fileName.split("_");
                    if (strs == null || strs.length <= 1) {
                        continue;
                    }
                    String numStr = strs[2];
                    if (numStr != null) {
                        numStr = (numStr.split(".txt"))[0];
                        Long num = Long.parseLong(numStr);
                        if (num != null) {
                            nameNum.add(num);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (nameNum.size() == 1) {
                time = nameNum.get(0);
            } else if (nameNum.size() > 1) {
                time = Collections.max(nameNum);
            }
        }
        name = prefix + time + ".txt";
        File newFile = new File(filePath + name);
        if (newFile.length() > fileSize) {
            newFile = new File(filePath + prefix + (new Date()).getTime() + ".txt");
        }
        return newFile;
    }

    public static void main(String[] args) {
        KGSubsetsByPubTask ks = new KGSubsetsByPubTask();
        File file = ks.getFileName("C:/Users/Administrator/Desktop/20181015open/pubtestfile/");
        System.out.println(file.getName());
    }
}
