package com.smate.web.data.utils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
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

/**
 * Hbase工具类
 * 
 * @author lhd
 *
 */
public class HbaseUtils {

  protected static Logger logger = LoggerFactory.getLogger(HbaseUtils.class);
  private static final Object lock = new Object();
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

  public static Connection getConnectionByPool() {
    if (null == con) {
      synchronized (lock) {
        if (null == con) {// 空的时候创建，不为空就直接返回；典型的单例模式
          Configuration conf = HBaseConfiguration.create();
          conf.set("hbase.zookeeper.quorum", "smate-web,smate-alpha,smate-yali221,isis");
          ExecutorService pool = Executors.newFixedThreadPool(10);// 建立一个数量为10的线程池
          try {
            con = ConnectionFactory.createConnection(conf, pool);// 用线程池创建connection
          } catch (IOException e) {
            logger.error("HBase用线程池创建connection failure !");
          }
        }
      }
    }
    return con;
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

  public static List<Cell> queryList(String tableStr, String rowKey) {
    Table table = null;
    List<Cell> cells = null;
    try {
      con = getConnection();
      TableName tableName = TableName.valueOf(tableStr);
      table = con.getTable(tableName);
      Get get = new Get(Bytes.toBytes(rowKey));
      Result result = table.get(get);
      cells = result.listCells();
    } catch (Exception e) {
      logger.error("getResult failure !", e);
    } finally {
      try {
        table.close();
      } catch (IOException e) {
        logger.error("table.close() failure !", e);
      }
      try {
        con.close();
      } catch (IOException e) {
        logger.error("Close failed in Hbase's connection!", e);
      }
    }
    return cells;
  }

}
