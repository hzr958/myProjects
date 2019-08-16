package com.smate.center.batch.chain.pub;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.context.ApplicationContext;

import com.smate.center.batch.service.pdwh.prj.NsfcKeywordsService;
import com.smate.center.batch.util.pub.BatchApplicationContext;

public class MultiWriteToDbTask implements Runnable {

    private String hdsfPath; // 从hdfs根目录开始
    private String fileSection;
    private FileSystem fileSystem;

    public MultiWriteToDbTask() {
        super();
    }

    public MultiWriteToDbTask(String hdsfPath, String fileSection, FileSystem fileSystem) {
        super();
        this.hdsfPath = hdsfPath;
        this.fileSection = fileSection;
        this.fileSystem = fileSystem;
    }

    public String getHdsfPath() {
        return hdsfPath;
    }

    public void setHdsfPath(String hdsfPath) {
        this.hdsfPath = hdsfPath;
    }

    public String getFileSection() {
        return fileSection;
    }

    public void setFileSection(String fileSection) {
        this.fileSection = fileSection;
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public void setFileSystem(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }



    @Override
    public void run() {
        ApplicationContext applicationContext = BatchApplicationContext.getApplicationContext();
        NsfcKeywordsService nsfcKeywordsService = applicationContext.getBean(NsfcKeywordsService.class);

        // 目前是
        BufferedReader br = null;
        FSDataInputStream fsd = null;
        try {
            Long taskId = nsfcKeywordsService.getTaskId();
            nsfcKeywordsService.insertIntoNsfcTaskStatus(taskId, fileSection, new Date());
            fsd = fileSystem.open(new Path(hdsfPath));
            br = new BufferedReader(new InputStreamReader(fsd));
            String brStr;
            Integer i = 0;
            System.out.println("处理的文件名为：" + this.fileSection + " end ID:" + Thread.currentThread().getId());
            while ((brStr = br.readLine()) != null) {
                try {
                    nsfcKeywordsService.saveNsfcKwsCotf(brStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i++;
            }
            nsfcKeywordsService.updateNsfcTaskStatus(taskId, new Date());
            System.out.println("处理的文件名为：" + this.fileSection + " end ID:" + Thread.currentThread().getId()
                    + "总共读取关键词组cotf" + i + "行");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fsd.close();
                br.close();
                /*
                 * System.out.println("处理的文件名为：" + this.fileSection + " end ID:" + Thread.currentThread().getId() +
                 * "fsd.close()- br.close();");
                 */
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
