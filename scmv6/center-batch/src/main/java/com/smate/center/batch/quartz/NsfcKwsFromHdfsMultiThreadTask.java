package com.smate.center.batch.quartz;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.smate.center.batch.chain.pub.MultiWriteToDbTask;
import com.smate.center.batch.service.TaskMarkerService;
import com.smate.center.batch.service.pdwh.prj.NsfcKeywordsService;
import com.smate.core.base.utils.exception.BatchTaskException;

// hadoop算完后的结果写入数据库中
public class NsfcKwsFromHdfsMultiThreadTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final static Integer SIZE = 5000; // 每次刷新获取的个数

    @Autowired
    private TaskMarkerService taskMarkerService;
    @Autowired
    private NsfcKeywordsService nsfcKeywordsService;

    @Value("${hadoop.hdfs.uri}")
    private String hdsfPath;
    private String localFilePath = "";

    public void run() throws BatchTaskException {
        logger.debug("====================================NsfcKwsFromHdfsTask===开始运行");
        if (isRun() == false) {
            logger.debug("====================================NsfcKwsFromHdfsTask===开关关闭");
            return;
        } else {
            try {
                taskMarkerService.closeQuartzApplication("NsfcKwsFromHdfsMultiThreadTask");
                doRun();
            } catch (Exception e) {
                logger.error("NsfcKwsFromHdfsTask,运行异常", e);
            }
        }
    }

    public void doRun() throws Exception {
        Configuration cfg = new Configuration();
        URI uri = new URI(hdsfPath);
        FileSystem fs = FileSystem.get(uri, cfg);
        FileStatus[] status = fs.listStatus(new Path("/output/subset2/"));
        ExecutorService executorService = Executors.newFixedThreadPool(15);
        if (status != null) {
            for (FileStatus fsy : status) {
                if (fsy.isFile()) {
                    System.out.println(fsy.getPath().getName() + ", 其路径是：" + fsy.getPath());
                    if (fsy.getPath().getName().indexOf("part-r-0") >= 0) { // 目前是
                        try {
                            String[] pathNames = fsy.getPath().toString().split(":");
                            String subPath = pathNames[2].substring(pathNames[2].indexOf("/"), pathNames[2].length());
                            Integer num = Integer.parseInt(subPath.substring(subPath.length() - 3, subPath.length()));
                            Integer taskCount = this.nsfcKeywordsService.getTaskStatusByDataSection(subPath);
                            if (taskCount > 0 || num > 560 || num < 200) {
                                continue;
                            }
                            executorService.execute(new MultiWriteToDbTask(subPath, subPath, fs));
                            // System.out.println("总共读取关键词组cotf" + i + "行");
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                        }
                    }
                }
            }
        }
        executorService.shutdown();
    }

    public boolean isRun() throws BatchTaskException {
        // 任务开关控制逻辑
        return taskMarkerService.getApplicationQuartzSettingValue("NsfcKwsFromHdfsMultiThreadTask") == 1;
    }

    public static void main(String[] args) throws Exception {
        String hdsfPath = "hdfs://192.168.15.190:9000/";
        // String hdsfPath = "hdfs://121.201.57.134:9000/output/subset/part-r-00006";
        // String hdsfPath = "hdfs://121.201.57.134:9000/";
        Configuration cfg = new Configuration();
        // cfg.set("dfs.client.use.datanode.hostname", "true");
        // URI uri = new URI(hdsfPath + "/subset/part-r-00000");
        FileSystem fs = FileSystem.get(new URI(hdsfPath), cfg);
        FileStatus[] status = fs.listStatus(new Path("/output/subset2/"));
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        if (status != null) {
            System.out.println("executor.start();");
            for (FileStatus fsy : status) {
                if (fsy.isFile()) {
                    fsy.getBlockSize();
                    fsy.getLen();
                    System.out.println(fsy.getPath().getName() + ", 其路径是：" + fsy.getPath());
                    if (fsy.getPath().getName().indexOf("part-r-00") >= 0) {
                        String[] pathNames = fsy.getPath().toString().split(":");
                        String subPath = pathNames[2].substring(pathNames[2].indexOf("/"), pathNames[2].length());
                        /*
                         * Pattern p = Pattern.compile("[1-9]{2}"); Matcher m = p.matcher(subPath); if (m.find()) {
                         * System.out.println("first number: " + m.group() + ", index: " + m.start()); }
                         */
                        Integer num = Integer.parseInt(subPath.substring(subPath.length() - 3, subPath.length()));
                        System.out.println(num);
                        // executorService.execute(new MultiWriteToDbTask(subPath, subPath, fs));
                    }
                }
                if (fsy.isDirectory()) {
                    System.out.println("这个是文件夹：" + fsy.getPath());
                }
            }
        }
        System.out.println("executor.shutdown();");
        executorService.shutdown();
    }

}

