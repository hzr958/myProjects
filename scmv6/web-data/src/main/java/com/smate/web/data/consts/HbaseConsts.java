package com.smate.web.data.consts;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * hbase相关常量
 * 
 * @author lhd
 *
 */
public class HbaseConsts {

  public static final TableName TB_PK_STRENGTH = TableName.valueOf("pdwh:pub_keywords_strength");
  public static final byte[] TB_PK_STRENGTH_FAMILY = Bytes.toBytes("info");
  public static final byte[] TB_PK_STRENGTH_QUALIFIER = Bytes.toBytes("count");
  public static final String TB_PK_STRENGTH_STR = "pdwh:pub_keywords_strength";
  public static final String TB_PK_STRENGTH_FAMILY_STR = "info";
  public static final String TB_PK_STRENGTH_QUALIFIER_STR = "count";
}
