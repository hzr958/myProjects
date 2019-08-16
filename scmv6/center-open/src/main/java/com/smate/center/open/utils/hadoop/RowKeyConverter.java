package com.smate.center.open.utils.hadoop;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * Hbase pdwh:pub_keywords表rowkey生成工具类
 * 
 * @author houchuanjie
 *
 */
public class RowKeyConverter {
  private static final int ROW_KEY_LENGTH = 40;

  /**
   * 生成Hbase pdwh:pub_keywords表 rowkey
   * 
   * @param key
   * @return
   */
  public static byte[] generateRowKey(String key) {
    byte[] rowKey = new byte[ROW_KEY_LENGTH];
    Bytes.putBytes(rowKey, 0, key.getBytes(), 0, key.length());
    return rowKey;
  }
}
