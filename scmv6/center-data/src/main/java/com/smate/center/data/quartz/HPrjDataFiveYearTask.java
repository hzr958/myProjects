package com.smate.center.data.quartz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.data.hbase.pubkeywords.RowKeyConverter;
import com.smate.center.data.model.hadoop.pub.HKeywordsItem;
import com.smate.center.data.model.pub.ProjectDataFiveYear;
import com.smate.center.data.service.pub.ProjectDataFiveYearService;
import com.smate.center.data.service.task.TaskMarkerService;
import com.smate.center.data.utils.MessageDigestUtils;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 项目关键词频次任务
 * 
 * @author lhd
 *
 */
public class HPrjDataFiveYearTask {
  private final static Logger logger = LoggerFactory.getLogger(HPrjDataFiveYearTask.class);
  private final static Integer SIZE = 100; // 每次获取成果个数
  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private ProjectDataFiveYearService projectDataFiveYearService;

  private Long startId = 483587L;// 483588 - 1
  private Long endId = 732484L;
  private String taskName = "HPrjDataFiveYearTask";
  private static Configuration conf = null;
  private static Connection con = null;

  static {
    try {
      if (conf == null) {
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", System.getenv("HADOOP_NODES"));
      }
    } catch (Exception e) {
      logger.error("HBase Configuration Initialization failure !");
      throw new RuntimeException(e);
    }
  }

  /**
   * 获得链接
   * 
   * @return
   */
  public static synchronized Connection getConnection() {
    try {
      if (con == null || con.isClosed()) {
        con = ConnectionFactory.createConnection(conf);
      }
    } catch (IOException e) {
      logger.error("HBase 建立链接失败 ", e);
    }
    return con;
  }

  public void run() throws BatchTaskException {
    if (isRun() == false) {
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        throw new BatchTaskException(e);
      }
    }
  }

  private void doRun() throws BatchTaskException {
    for (Long i = startId; i < endId; i += SIZE) {
      try {
        long s1 = Calendar.getInstance().getTimeInMillis();
        List<ProjectDataFiveYear> prjKeywords = projectDataFiveYearService.getProjectDataList(SIZE, i, endId);
        if (CollectionUtils.isNotEmpty(prjKeywords)) {
          List<HKeywordsItem> allList = new ArrayList<HKeywordsItem>();
          con = getConnection();
          Table table = con.getTable(TableName.valueOf("sns", "prj_keywords_count"));
          for (ProjectDataFiveYear prj : prjKeywords) {
            try {
              List<HKeywordsItem> list = new ArrayList<HKeywordsItem>();
              String cId = prj.getApplicationCode();
              String zhKeywords = prj.getZhKeywords();// 中文关键词
              String enKeywords = prj.getEnKeywords();// 英文关键词
              if (StringUtils.isNotBlank(zhKeywords)) {
                handleKeywords(zhKeywords, list);
              }
              if (StringUtils.isNotBlank(enKeywords)) {
                handleKeywords(enKeywords, list);
              }
              allList.addAll(buildData(table, list, cId));
            } catch (Exception e) {
              logger.error(taskName + "出错", e);
            }
          }
          if (CollectionUtils.isNotEmpty(allList)) {
            saveData(allList);
          }
          long e1 = Calendar.getInstance().getTimeInMillis();
          logger.error(taskName + "===100条完成时间: " + (e1 - s1));
          logger.error(taskName + "中100条的最后一个id= " + prjKeywords.get(prjKeywords.size() - 1).getId());
        }
      } catch (Exception e) {
        try {
          Thread.sleep(300000);
          i -= SIZE;
        } catch (InterruptedException e1) {
          logger.error(taskName + "线程休眠==================", e1);
        }
        logger.error(taskName + "处理出错,休眠下==================", e);
      }
    }
    taskMarkerService.closeQuartzApplication(taskName);
    logger.error(taskName + "数据处理完毕====================");
  }

  private void saveData(List<HKeywordsItem> allList) throws Exception {
    projectDataFiveYearService.saveData(allList);
  }

  private List<HKeywordsItem> buildData(Table table, List<HKeywordsItem> list, String cId) {
    List<HKeywordsItem> resultList = new ArrayList<HKeywordsItem>();
    if (CollectionUtils.isNotEmpty(list)) {
      for (HKeywordsItem input : list) {
        byte[] key12 = buildRowKey(cId + input.getKey1Sha1() + input.getKey2Sha1());
        double key12Val = getKey(table, key12);
        if (key12Val > 0) {
          HKeywordsItem hi = new HKeywordsItem();
          byte[] key1 = buildRowKey(cId + input.getKey1Sha1());
          byte[] key2 = buildRowKey(cId + input.getKey2Sha1());
          double key1Val = getKey(table, key1);
          double key2Val = getKey(table, key2);
          hi.setKey1(input.getKey1());
          hi.setKey2(input.getKey2());
          hi.setKey1Val(key1Val);
          hi.setKey2Val(key2Val);
          hi.setKey12Val(key12Val);
          hi.setcId(cId);
          resultList.add(hi);
        }
      }
    }
    return resultList;
  }

  private byte[] buildRowKey(String str) {
    return RowKeyConverter.generateRowKey(MessageDigestUtils.messageDigest(str));
  }

  private double getKey(Table table, byte[] key) {
    try {
      Get get = new Get(key);
      Result result = table.get(get);
      byte[] vKey = result.getValue(Bytes.toBytes("info"), Bytes.toBytes("count"));
      // double dKey = Bytes.toDouble(vKey);//不能用这个,因为存的时候是字符串类型
      double dKey = Double.parseDouble(Bytes.toString(vKey) == null ? "0" : Bytes.toString(vKey));
      return dKey;
    } catch (Exception e) {
      logger.error(taskName + "table.get(get)报错:", e);
    }
    return 0;
  }

  private void handleKeywords(String keywords, List<HKeywordsItem> list) {
    if (StringUtils.isNotBlank(keywords)) {
      Map<String, String> mdmap = new HashMap<String, String>();
      TreeSet<String> treeSet = new TreeSet<String>();
      String[] keywordsStr = keywords.replace("；", ";").split(";");
      for (String keyword : keywordsStr) {
        String kw = keyword.replaceAll("\\s+", " ").trim();
        treeSet.add(MessageDigestUtils.messageDigest(kw.toLowerCase()));
        mdmap.put(MessageDigestUtils.messageDigest(kw.toLowerCase()), keyword);
      }
      if (CollectionUtils.isNotEmpty(treeSet)) {
        List<String> zhkeywordsList = new ArrayList<>(treeSet);
        for (int i = 0; i < zhkeywordsList.size() - 1; i++) {
          for (int j = i + 1; j < zhkeywordsList.size(); j++) {
            HKeywordsItem item = new HKeywordsItem();
            item.setKey1Sha1(zhkeywordsList.get(i));
            item.setKey2Sha1(zhkeywordsList.get(j));
            item.setKey1(mdmap.get(zhkeywordsList.get(i)));
            item.setKey2(mdmap.get(zhkeywordsList.get(j)));
            list.add(item);
          }
        }
      }
    }
  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    return taskMarkerService.getApplicationQuartzSettingValue(taskName) == 1;
  }

  public Long getStartId() {
    return startId;
  }

  public void setStartId(Long startId) {
    this.startId = startId;
  }

  public Long getEndId() {
    return endId;
  }

  public void setEndId(Long endId) {
    this.endId = endId;
  }

  public String getTaskName() {
    return taskName;
  }

  public void setTaskName(String taskName) {
    this.taskName = taskName;
  }

}
