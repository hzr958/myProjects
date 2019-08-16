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
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.data.hbase.pubkeywords.KeywordsQuery;
import com.smate.center.data.hbase.pubkeywords.RowKeyConverter;
import com.smate.center.data.model.hadoop.pub.HKeywords;
import com.smate.center.data.model.pub.PdwhPubKeywordsCategory;
import com.smate.center.data.service.pub.PdwhPubKeywordsService;
import com.smate.center.data.service.task.TaskMarkerService;
import com.smate.center.data.utils.MessageDigestUtils;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 计算pdwh成果关键词组合频次任务
 * 
 * @author lhd
 */
public class HPdwhPubKeywordsTask {
  private final static Logger logger = LoggerFactory.getLogger(HPdwhPubKeywordsTask.class);
  private final static Integer SIZE = 2000; // 每次获取成果个数
  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private PdwhPubKeywordsService pdwhPubKeywordsService;
  private Long startId;
  private Long endId;
  private String taskName;
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
      logger.error("HBase 建立连接失败 ", e);
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
        logger.error(taskName + "运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  private void doRun() throws BatchTaskException {
    for (Long i = startId; i < endId; i += SIZE) {
      try {
        long s1 = Calendar.getInstance().getTimeInMillis();
        List<PdwhPubKeywordsCategory> pdwhPubKeywords = pdwhPubKeywordsService.getPdwhPubKeywords(SIZE, i, endId);
        if (CollectionUtils.isNotEmpty(pdwhPubKeywords)) {
          List<HKeywords> zhList = new ArrayList<HKeywords>();
          List<HKeywords> enList = new ArrayList<HKeywords>();
          con = getConnection();
          Table table = con.getTable(TableName.valueOf("pdwh:pub_keywords_count"));
          Table table2 = con.getTable(TableName.valueOf("pdwh:pub_keywords_strength"));
          for (PdwhPubKeywordsCategory pubKeywords : pdwhPubKeywords) {
            Map<String, List<HKeywords>> map = new HashMap<String, List<HKeywords>>();
            Map<String, String> mdmap = new HashMap<String, String>();
            try {
              Long pubId = pubKeywords.getPubId();
              Long categoryId = pubKeywords.getScmCategoryId();// 学部
              String zhKeywords = pubKeywords.getZhKeywords();// 中文关键词
              String enKeywords = pubKeywords.getEnKeywords();// 英文关键词
              if (StringUtils.isNotBlank(zhKeywords)) {
                zhList = handleKeywords(zhKeywords, mdmap);
                buildVal(categoryId, table, zhList, pubId);
                map.put("zh_CN", zhList);
              }
              if (StringUtils.isNotBlank(enKeywords)) {
                enList = handleKeywords(enKeywords, mdmap);
                buildVal(categoryId, table, enList, pubId);
                map.put("en_US", enList);
              }
              saveData(table2, con, map, pubId + "", mdmap, categoryId);
            } catch (Exception e) {
              pdwhPubKeywordsService.saveOpResult(pubKeywords.getId(), 9);
              logger.error(taskName + "出错,将status置为9===========", e);
            }
          }
          long e1 = Calendar.getInstance().getTimeInMillis();
          logger.error(taskName + "===" + SIZE + "条完成时间: " + (e1 - s1));
          logger.error(taskName + "中" + SIZE + "条最后一个id= " + pdwhPubKeywords.get(pdwhPubKeywords.size() - 1).getId());
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
    // finally {
    // try {
    // logger.error(taskName + "=====con.close ========");
    // con.close();
    // } catch (IOException e) {
    // logger.error(taskName + "con.close()Exception========", e);
    // }
    // }
  }

  private void saveData(Table table2, Connection con, Map<String, List<HKeywords>> map, String rowKey,
      Map<String, String> mdmap, Long categoryId) throws Exception {
    Put put = new Put(Bytes.toBytes(rowKey));
    for (Map.Entry<String, List<HKeywords>> entry : map.entrySet()) {
      List<HKeywords> values = entry.getValue();
      for (HKeywords hKeywords : values) {
        hKeywords.setKey1(mdmap.get(hKeywords.getKey1()));
        hKeywords.setKey2(mdmap.get(hKeywords.getKey2()));
      }
    }
    String json = JacksonUtils.mapToJsonStr(map);
    put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("count" + categoryId), Bytes.toBytes(json));
    table2.put(put);
  }

  private void buildVal(Long categoryId, Table table, List<HKeywords> list, Long pubId) throws Exception {
    if (CollectionUtils.isNotEmpty(list)) {
      for (HKeywords hk : list) {
        byte[] key1 = buildRowKey(categoryId + hk.getKey1());
        byte[] key2 = buildRowKey(categoryId + hk.getKey2());
        byte[] key12 = buildRowKey(categoryId + hk.getKey1() + hk.getKey2());
        double dKey1 = getKey(table, key1, pubId, categoryId);
        double dKey2 = getKey(table, key2, pubId, categoryId);
        double dKey12 = getKey(table, key12, pubId, categoryId);
        hk.setVal(dKey12 / (dKey1 + dKey2 - dKey12));
      }
    }
  }

  private byte[] buildRowKey(String str) {
    return RowKeyConverter.generateRowKey(MessageDigestUtils.messageDigest(str));
  }

  private double getKey(Table table, byte[] key, Long pubId, Long categoryId) throws Exception {
    Get get = new Get(key);
    Result result = table.get(get);
    byte[] vKey = result.getValue(KeywordsQuery.COLUMN_FAMILY_INFO, KeywordsQuery.QUALIFIER_COUNT);
    double dKey = Double.parseDouble(Bytes.toString(vKey) == null ? "0" : Bytes.toString(vKey));
    return dKey;
  }

  private List<HKeywords> handleKeywords(String keywords, Map<String, String> mdmap) {
    List<HKeywords> list = new ArrayList<HKeywords>();
    if (StringUtils.isNotBlank(keywords)) {
      TreeSet<String> treeSet = new TreeSet<String>();
      String[] keywordsString = keywords.split(";");
      for (String keyword : keywordsString) {
        String kw = keyword.replaceAll("\\s+", " ").trim();
        treeSet.add(MessageDigestUtils.messageDigest(kw.toLowerCase()));
        mdmap.put(MessageDigestUtils.messageDigest(kw.toLowerCase()), keyword);
      }
      if (CollectionUtils.isNotEmpty(treeSet)) {
        List<String> zhkeywordsList = new ArrayList<>(treeSet);
        for (int i = 0; i < zhkeywordsList.size() - 1; i++) {
          for (int j = i + 1; j < zhkeywordsList.size(); j++) {
            HKeywords hKeywords = new HKeywords();
            hKeywords.setKey1(zhkeywordsList.get(i));
            hKeywords.setKey2(zhkeywordsList.get(j));
            list.add(hKeywords);
          }
        }
      }
    }
    return list;
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
