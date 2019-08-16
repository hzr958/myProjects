package com.smate.center.data.hbase.pubkeywords;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * hbase pdwh:pub_keywords表查询类
 * 
 * @author houchuanjie
 */
public class KeywordsQuery {
  public static final byte[] COLUMN_FAMILY_INFO = Bytes.toBytes("info");
  public static final byte[] QUALIFIER_COUNT = Bytes.toBytes("count");
}
