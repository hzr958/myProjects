package com.smate.center.data.quartz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;

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
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import com.smate.center.data.hbase.pubkeywords.KeywordsQuery;
import com.smate.center.data.hbase.pubkeywords.RowKeyConverter;
import com.smate.center.data.model.hadoop.pub.HKeywordsItem;
import com.smate.center.data.model.pub.PdwhPubKeywordsCategory;
import com.smate.center.data.service.pub.PdwhPubKeywordsService;
import com.smate.center.data.service.task.TaskMarkerService;
import com.smate.center.data.utils.MessageDigestUtils;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 成果关键词推荐任务
 * 
 * @author lhd
 */
public class HPubKeywordsRcmdTask {
  private final static Logger logger = LoggerFactory.getLogger(HPubKeywordsRcmdTask.class);
  private final static Integer SIZE = 100; // 每次获取成果个数
  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private PdwhPubKeywordsService pdwhPubKeywordsService;

  private Long startId;
  private Long endId;
  private String taskName;
  private static Configuration conf = null;
  private static Connection con = null;
  @Value("${solr.server.url}")
  private String serverUrl;
  private String runEnv = System.getenv("RUN_ENV");
  public static String INDEX_TYPE_HADOOP_PUB = "hadoop_pub_index";

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
        List<PdwhPubKeywordsCategory> pdwhPubKeywords = pdwhPubKeywordsService.getPdwhPubKeywords(SIZE, i, endId);
        if (CollectionUtils.isNotEmpty(pdwhPubKeywords)) {
          List<HKeywordsItem> allList = new ArrayList<HKeywordsItem>();
          con = getConnection();
          Table table = con.getTable(TableName.valueOf("pdwh:pub_keywords_count"));
          for (PdwhPubKeywordsCategory pubKeywords : pdwhPubKeywords) {
            try {
              List<HKeywordsItem> list = new ArrayList<HKeywordsItem>();
              Long pubId = pubKeywords.getPubId();
              String keywords = "";
              String zhKeywords = pubKeywords.getZhKeywords();// 中文关键词
              String enKeywords = pubKeywords.getEnKeywords();// 英文关键词
              if (StringUtils.isNotBlank(zhKeywords) && StringUtils.isNotBlank(enKeywords)) {
                zhKeywords = zhKeywords.trim();
                String str = zhKeywords.substring(zhKeywords.length() - 1);
                if (";".equals(str)) {
                  keywords = zhKeywords + enKeywords;
                } else {
                  keywords = zhKeywords + ";" + enKeywords;
                }
              } else if (StringUtils.isNotBlank(zhKeywords) && StringUtils.isBlank(enKeywords)) {
                keywords = zhKeywords;
              } else if (StringUtils.isBlank(zhKeywords) && StringUtils.isNotBlank(enKeywords)) {
                keywords = enKeywords;
              }
              handleKeywords(keywords, list);
              // if (StringUtils.isNotBlank(zhKeywords)) {
              // handleKeywords(zhKeywords, list);
              // }
              // if (StringUtils.isNotBlank(enKeywords)) {
              // handleKeywords(enKeywords, list);
              // }
              allList.addAll(buildVal(table, list, pubId));
            } catch (Exception e) {
              pdwhPubKeywordsService.saveOpResult(pubKeywords.getId(), 9);
              logger.error(taskName + "出错,将status置为9===========", e);
            }
          }
          if (CollectionUtils.isNotEmpty(allList)) {
            long s3 = Calendar.getInstance().getTimeInMillis();
            saveAtSolr(allList);
            long s4 = Calendar.getInstance().getTimeInMillis();
            logger.error(taskName + "===100条成果组合的关键词存到solr上的时间: " + (s4 - s3));

          }
          long e1 = Calendar.getInstance().getTimeInMillis();
          logger.error(taskName + "===100条完成时间: " + (e1 - s1));
          logger.error(taskName + "中100条的最后一个id= " + pdwhPubKeywords.get(pdwhPubKeywords.size() - 1).getId());
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
      // finally {
      // try {
      // logger.error(taskName + "=====con.close ========");
      // con.close();
      // } catch (IOException e) {
      // logger.error(taskName + "con.close()Exception========", e);
      // }
      // }
    }
    taskMarkerService.closeQuartzApplication(taskName);
    logger.error(taskName + "数据处理完毕====================");
  }

  private void saveAtSolr(List<HKeywordsItem> list) {
    try {
      SolrServer server = new HttpSolrServer(serverUrl + "collection2");
      List<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
      for (HKeywordsItem item : list) {
        SolrInputDocument doc = new SolrInputDocument();
        // 必须字段设定schema.xml配置
        doc.setField("businessType", INDEX_TYPE_HADOOP_PUB);
        Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
        doc.setField("env", runEnv);
        doc.setField("id", UUID.randomUUID());
        doc.setField("key1", item.getKey1());
        doc.setField("key1Val", item.getKey1Val());
        doc.setField("key1Sha1", item.getKey1Sha1());
        doc.setField("key2", item.getKey2());
        doc.setField("key2Val", item.getKey2Val());
        doc.setField("key2Sha1", item.getKey2Sha1());
        doc.setField("key12Sha1", item.getKey12Sha1());
        doc.setField("key12Val", item.getKey12Val());
        doc.setField("key12Jaccard", item.getKey12Jaccard());
        doc.setField("cId", item.getcId());
        docList.add(doc);
      }
      server.add(docList);
      server.commit();
    } catch (Exception e) {
      logger.error(taskName + "saveAtSolr出错", e);
    }
  }

  private List<HKeywordsItem> buildVal(Table table, List<HKeywordsItem> list, Long pubId) throws Exception {
    List<HKeywordsItem> resultList = new ArrayList<HKeywordsItem>();
    if (CollectionUtils.isNotEmpty(list)) {
      double key12Val = 0;
      for (HKeywordsItem input : list) {
        boolean check = pdwhPubKeywordsService
            .checkCombine(MessageDigestUtils.messageDigest(input.getKey1Sha1() + input.getKey2Sha1()));
        boolean check2 = pdwhPubKeywordsService
            .checkCombine(MessageDigestUtils.messageDigest(input.getKey2Sha1() + input.getKey1Sha1()));
        if ((!check) && (!check2)) {
          int it = 0;
          HKeywordsItem hi = new HKeywordsItem();
          HKeywordsItem hi2 = new HKeywordsItem();
          for (int i = 1; i <= 7; i++) {
            byte[] key12 = buildRowKey(i + input.getKey1Sha1() + input.getKey2Sha1());
            key12Val = getKey(table, key12, pubId, i);
            it += key12Val;
            if (key12Val > 0) {// pdwh:pub_keywords_count表上存在该组合
              byte[] key1 = buildRowKey(i + input.getKey1Sha1());
              byte[] key2 = buildRowKey(i + input.getKey2Sha1());
              double key1Val = getKey(table, key1, pubId, i);
              double key2Val = getKey(table, key2, pubId, i);
              hi.setKey1(input.getKey1());
              hi.setKey1Sha1(input.getKey1Sha1());
              hi.setKey2(input.getKey2());
              hi.setKey2Sha1(input.getKey2Sha1());
              hi.setKey12Sha1(input.getKey12Sha1());
              hi.setKey1Val(hi.getKey1Val() + key1Val);
              hi.setKey2Val(hi.getKey2Val() + key2Val);
              hi.setKey12Val(hi.getKey12Val() + key12Val);
              hi.setKey12Jaccard(hi.getKey12Jaccard() + key12Val / (key1Val + key2Val - key12Val));
              hi.setcId(i + ";" + hi.getcId());
            }
          }
          // =====存一份主为(sha1
          // B)的,所以hi2的key1相关的存的是hi的key2相关的,hi2的key2相关的存的是hi的key1相关的
          if (it > 0) {
            hi2.setKey1(hi.getKey2());
            hi2.setKey1Val(hi.getKey2Val());
            hi2.setKey1Sha1(hi.getKey2Sha1());
            hi2.setKey2(hi.getKey1());
            hi2.setKey2Val(hi.getKey1Val());
            hi2.setKey2Sha1(hi.getKey1Sha1());
            hi2.setKey12Sha1(MessageDigestUtils.messageDigest(hi2.getKey1Sha1() + hi2.getKey2Sha1()));
            hi2.setKey12Val(hi.getKey12Val());
            hi2.setKey12Jaccard(hi.getKey12Jaccard());
            hi2.setcId(hi.getcId());
            resultList.add(hi);
            resultList.add(hi2);
            pdwhPubKeywordsService
                .saveCombineTable(MessageDigestUtils.messageDigest(hi.getKey1Sha1() + hi.getKey2Sha1()));
            // hi2的key1相关的存的是hi的key2相关的,hi2的key2相关的存的是hi的key1相关的
            pdwhPubKeywordsService
                .saveCombineTable(MessageDigestUtils.messageDigest(hi2.getKey1Sha1() + hi2.getKey2Sha1()));
          }
        }
      }
    }
    return resultList;
  }

  private byte[] buildRowKey(String str) {
    return RowKeyConverter.generateRowKey(MessageDigestUtils.messageDigest(str));
  }

  private double getKey(Table table, byte[] key, Long pubId, int categoryId) {
    try {
      Get get = new Get(key);
      Result result = table.get(get);
      byte[] vKey = result.getValue(KeywordsQuery.COLUMN_FAMILY_INFO, KeywordsQuery.QUALIFIER_COUNT);
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
      String[] keywordsStr = keywords.split(";");
      for (String keyword : keywordsStr) {
        if (StringUtils.isNotBlank(keyword)) {
          String kw = keyword.replaceAll("\\s+", " ").trim();
          treeSet.add(MessageDigestUtils.messageDigest(kw.toLowerCase()));
          mdmap.put(MessageDigestUtils.messageDigest(kw.toLowerCase()), keyword);
        }
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
            item.setKey12Sha1(MessageDigestUtils.messageDigest(item.getKey1Sha1() + item.getKey2Sha1()));
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
