package com.smate.center.open.utils.hadoop;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * hbase pdwh:pub_keywords表查询类
 * 
 * @author houchuanjie
 *
 */
public class PubKeywordsQuery {
  public static final TableName TABLE_NAME = TableName.valueOf("pdwh", "pub_keywords_count");
  public static final String TableNameStr = TABLE_NAME.getNameWithNamespaceInclAsString();
  public static final byte[] COLUMN_FAMILY_INFO = Bytes.toBytes("info");
  public static final byte[] QUALIFIER_COUNT = Bytes.toBytes("count");
  public static final String FAMILY = "info";
  public static final String QUALIFIER = "count";
}
