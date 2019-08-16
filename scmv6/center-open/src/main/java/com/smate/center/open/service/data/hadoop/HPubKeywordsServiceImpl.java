package com.smate.center.open.service.data.hadoop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.util.Bytes;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.model.hadoop.HKeywords;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.utils.hadoop.HbaseUtils;
import com.smate.center.open.utils.hadoop.MessageDigestUtils;
import com.smate.center.open.utils.hadoop.PubKeywordsQuery;
import com.smate.center.open.utils.hadoop.RowKeyConverter;
import com.smate.core.base.utils.constant.KeywordConstants;

/**
 * 获取成果关键词出现次数服务
 * 
 * @author lhd
 *
 */
public class HPubKeywordsServiceImpl extends ThirdDataTypeBase {

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();

    // 校验服务参数 serviceType
    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }
    Object pubDataObj = serviceData.get(KeywordConstants.KEYWORDS);
    if (pubDataObj == null) {
      logger.error("服务参数格式不正确 ,keywordList 为空或不是json格式");
      temp.putAll(errorMap(OpenMsgCodeConsts.SCM_723, paramet, ""));
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
      return temp;
    }
    paramet.putAll(serviceData);
    paramet.put(KeywordConstants.KEYWORDS, pubDataObj);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    String keywords = paramet.get(KeywordConstants.KEYWORDS).toString();
    List<String> list = new ArrayList<String>();
    String[] split = keywords.split(";");
    for (String key : split) {
      list.add(key);
    }
    List<HKeywords> keyList = new ArrayList<HKeywords>();
    Map<String, String> mdmap = new HashMap<String, String>();
    if (CollectionUtils.isNotEmpty(list)) {
      keyList = handlePubKeywords(list, mdmap);
      for (int i = 1; i <= 7; i++) {
        buildVal(i, keyList, mdmap, resultMap);
      }
    }
    resultMap = super.successMap(OpenMsgCodeConsts.SCM_000, resultMap);
    return resultMap;
  }

  private void buildVal(int cId, List<HKeywords> keyList, Map<String, String> mdmap, Map<String, Object> resultMap) {
    if (CollectionUtils.isNotEmpty(keyList)) {
      for (HKeywords hk : keyList) {
        if (mdmap.size() == 1) {
          String key1 = mdmap.get(hk.getKey1());
          int key1Value = buildRowKey(cId + hk.getKey1());
          buildResultMap(resultMap, key1, key1Value);
        } else {
          String key1 = mdmap.get(hk.getKey1());
          String key2 = mdmap.get(hk.getKey2());
          int key1Value = buildRowKey(cId + hk.getKey1());
          buildResultMap(resultMap, key1, key1Value);
          int key2Value = buildRowKey(cId + hk.getKey2());
          buildResultMap(resultMap, key2, key2Value);
          int key12Value = buildRowKey(cId + hk.getKey1() + hk.getKey2());
          buildResultMap(resultMap, key1 + ";" + key2, key12Value);
        }
      }
    }
  }

  private void buildResultMap(Map<String, Object> resultMap, String key, int value) {
    Object obj = resultMap.get(key);
    if (obj == null) {
      resultMap.put(key, value);
    } else {
      resultMap.put(key, NumberUtils.toInt(obj.toString()) + value);
    }
  }

  private int buildRowKey(String str) {
    byte[] b = RowKeyConverter.generateRowKey(MessageDigestUtils.messageDigest(str));
    String rowKey = new String(b);
    List<Cell> cells =
        HbaseUtils.queryOne(PubKeywordsQuery.TableNameStr, rowKey, PubKeywordsQuery.FAMILY, PubKeywordsQuery.QUALIFIER);
    if (CollectionUtils.isNotEmpty(cells)) {
      for (Cell cell : cells) {
        String value = Bytes.toString(CellUtil.cloneValue(cell)); // 取到值
        return NumberUtils.toInt(value);
      }
    }
    return 0;
  }

  private List<HKeywords> handlePubKeywords(List<String> list, Map<String, String> mdmap) {
    List<HKeywords> keyList = new ArrayList<HKeywords>();
    TreeSet<String> treeSet = new TreeSet<String>();
    for (String keyword : list) {
      String kw = keyword.replaceAll("\\s+", " ").trim();
      treeSet.add(MessageDigestUtils.messageDigest(kw.toLowerCase()));
      mdmap.put(MessageDigestUtils.messageDigest(kw.toLowerCase()), kw);
    }
    if (CollectionUtils.isNotEmpty(treeSet)) {
      List<String> keywordsList = new ArrayList<>(treeSet);
      if (keywordsList.size() == 1) {
        for (String str : keywordsList) {
          HKeywords hKeywords = new HKeywords();
          hKeywords.setKey1(str);
          keyList.add(hKeywords);
        }
      } else {
        for (int i = 0; i < keywordsList.size() - 1; i++) {
          for (int j = i + 1; j < keywordsList.size(); j++) {
            HKeywords hKeywords = new HKeywords();
            hKeywords.setKey1(keywordsList.get(i));
            hKeywords.setKey2(keywordsList.get(j));
            keyList.add(hKeywords);
          }
        }
      }
    }
    return keyList;
  }

}
