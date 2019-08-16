package com.smate.center.batch.quartz;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.smate.center.batch.service.TaskMarkerService;
import com.smate.center.batch.service.pdwh.prj.NsfcKeywordsService;
import com.smate.core.base.utils.exception.BatchTaskException;

// hadoop算完后的结果写入数据库中
public class NsfcKwsFromHdfsTask {
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
                taskMarkerService.closeQuartzApplication("NsfcKwsFromHdfsTask");
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
        if (status != null) {
            for (FileStatus fsy : status) {
                if (fsy.isFile()) {
                    System.out.println(fsy.getPath().getName() + ", 其路径是：" + fsy.getPath());
                    if (fsy.getPath().getName().indexOf("part-r-0") >= 0) { // 目前是
                        BufferedReader br = null;
                        FSDataInputStream fsd = null;
                        try {
                            String[] pathNames = fsy.getPath().toString().split(":");
                            String subPath = pathNames[2].substring(pathNames[2].indexOf("/"), pathNames[2].length());
                            Integer num = Integer.parseInt(subPath.substring(subPath.length() - 3, subPath.length()));
                            if (num < 127 || num > 498) {
                                continue;
                            }
                            fsd = fs.open(new Path(subPath));
                            br = new BufferedReader(new InputStreamReader(fsd));
                            String brStr;
                            Integer i = 0;
                            while ((brStr = br.readLine()) != null) {
                                // System.out.println("读取文件第" + i + "行，关键词为：" + brStr);
                                try {
                                    this.nsfcKeywordsService.saveNsfcKwsCotf(brStr);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                i++;
                            }
                            System.out.println("总共读取关键词组cotf" + i + "行");
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            fsd.close();
                            br.close();
                        }
                    }
                }
            }
        }
    }

    public boolean isRun() throws BatchTaskException {
        // 任务开关控制逻辑
        return taskMarkerService.getApplicationQuartzSettingValue("NsfcKwsFromHdfsTask") == 1;
    }

    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        System.out.println(time);

        String hdsfPath = "hdfs://192.168.15.190:9000/";
        // String hdsfPath = "hdfs://121.201.57.134:9000/output/subset/part-r-00006";
        // String hdsfPath = "hdfs://121.201.57.134:9000/";
        Configuration cfg = new Configuration();
        // cfg.set("dfs.client.use.datanode.hostname", "true");
        // URI uri = new URI(hdsfPath + "/subset/part-r-00000");
        FileSystem fs = FileSystem.get(new URI(hdsfPath), cfg);
        FileStatus[] status = fs.listStatus(new Path("/output/subset2/"));
        if (status != null) {
            for (FileStatus fsy : status) {
                if (fsy.isFile()) {
                    fsy.getBlockSize();
                    fsy.getLen();
                    System.out.println(fsy.getPath().getName() + ", 其路径是：" + fsy.getPath());
                    if (fsy.getPath().getName().indexOf("part-r-0000") >= 0) {
                        BufferedReader br2 = null;
                        FSDataInputStream fsd2 = null;
                        try {
                            String[] pathNames = fsy.getPath().toString().split(":");
                            String subPath = pathNames[2].substring(pathNames[2].indexOf("/"), pathNames[2].length());
                            fsd2 = fs.open(new Path(subPath));
                            br2 = new BufferedReader(new InputStreamReader(fsd2));
                            String brStr;
                            while ((brStr = br2.readLine()) != null) {
                                System.out.println(brStr);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            fsd2.close();
                            br2.close();
                        }
                    }
                }
            }

            BufferedReader br1 = null;
            FSDataInputStream fsd1 = null;
            try {
                // fsd = fs.open(fsy.getPath());
                // fsd1 = fs.open(new Path("hdfs://192.168.15.190:9000/output/subset/part-r-00000"));
                // fsd1 = fs.open(new Path("/output/subset/part-r-00000"));
                fsd1 = fs.open(new Path("/output/subset/"));

                br1 = new BufferedReader(new InputStreamReader(fsd1));
                String readLine = br1.readLine();
                System.out.println(readLine);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                fsd1.close();
                br1.close();
            }

            for (FileStatus fsy : status) {
                if (fsy.isFile()) {
                    System.out.println(fsy.getPath().getName() + "是文件, 其路径是：" + fsy.getPath());
                    BufferedReader br = null;
                    FSDataInputStream fsd = null;
                    try {
                        // fsd = fs.open(fsy.getPath());
                        fsd = fs.open(new Path("/output/subset/part-r-00000"));
                        br = new BufferedReader(new InputStreamReader(fsd));
                        String readLine = br.readLine();
                        System.out.println(readLine);
                        int i = 0;
                        while ((readLine = br.readLine()) != null) {
                            if (i > 3) {
                                break;
                            }
                            System.out.println(readLine);
                            i++;
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    } finally {
                        fsd.close();
                        br.close();
                    }
                }
                if (fsy.isDirectory()) {
                    System.out.println("这个是文件夹：" + fsy.getPath());
                }
            }
        }
    }

}
