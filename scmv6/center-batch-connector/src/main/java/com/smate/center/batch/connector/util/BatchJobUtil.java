package com.smate.center.batch.connector.util;

import java.util.HashMap;
import java.util.Map;

import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 任务工具类
 * 
 * @author apache
 *
 */
public class BatchJobUtil {

  public static String getContext(String objId) {
    return "{\"msg_id\":" + objId + "}";
  }

  public static String getVersionContext(String objId, String version) {
    return "{\"msg_id\":" + objId + ",\"version\":" + version + "}";
  }

  public static String getVersionContext(String objId, String version, String groupId) {
    return "{\"msg_id\":" + objId + ",\"version\":" + version + ",\"groupId\":" + groupId + "}";
  }

  public static String getActionContext(String objId, String version, String actionType) {
    return "{\"msg_id\":" + objId + ",\"version\":" + version + ",\"actionType\":" + actionType + "}";
  }

  public static String getActionContext(String objId, String version, String actionType, String groupId) {
    return "{\"msg_id\":" + objId + ",\"version\":" + version + ",\"actionType\":" + actionType + ",\"groupId\":"
        + groupId + "}";
  }

  public static String getDbSourceContext(String pubid, String insId, String DbSource) {
    return "{\"msg_id\":" + pubid + ",\"ins_id\":" + insId + ",\"dbSource\":" + DbSource + "}";
  }

  public static String getPsnInfoContext(String pubId, String insId, String locale) {
    Map<String, String> map = new HashMap<String, String>();
    map.put("msg_id", pubId);
    map.put("ins_id", insId);
    map.put("locale", locale);
    return JacksonUtils.jsonMapSerializer(map);

  }

  public static String getGroupContext(String obj, Integer node_id) {
    Map<String, String> map = new HashMap<String, String>();
    map.put("msg_id", obj);
    map.put("node_id", node_id.toString());
    return JacksonUtils.jsonMapSerializer(map);
  }

  public static String getContext1(String obj) {
    Map<String, String> map = new HashMap<String, String>();
    map.put("msg_id", obj);
    return JacksonUtils.jsonMapSerializer(map);
  }

  public static String getGroupContext(String obj, Long psnId) {
    Map<String, String> map = new HashMap<String, String>();
    map.put("msg_id", obj);
    map.put("invite_psn_id", psnId.toString());
    return JacksonUtils.jsonMapSerializer(map);
  }

  /**
   * 关注动态
   * 
   * @param obj
   * @param psnId
   * @return
   */
  public static String getAttDynamicContext(String obj, Long psnId, Long attPsnId, Integer status) {
    Map<String, String> map = new HashMap<String, String>();
    map.put("msg_id", obj);
    map.put("psnId", psnId.toString());
    map.put("attPsnId", attPsnId.toString());
    map.put("status", status.toString());
    return JacksonUtils.jsonMapSerializer(map);
  }

  /**
   * 构造Context
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param objId
   * @return
   */
  public static String getContext(Long objId) {
    return "{\"msg_id\":" + objId + "}";
  }

  /**
   * 更新人员solr信息Context
   * 
   * @param obj
   * @param psnId
   * @return
   */
  public static String getPsnSolrRefreshContext(String obj, String opreateType) {
    Map<String, String> map = new HashMap<String, String>();
    map.put("msg_id", obj);
    map.put("operate_type", opreateType);
    return JacksonUtils.jsonMapSerializer(map);
  }

  /**
   * 基准库成果地址作者匹配context
   * 
   * @param obj
   * @param psnId
   * @return
   */
  public static String getPdwhAddrAuthMatchContext(String obj, String opreateType) {
    Map<String, String> map = new HashMap<String, String>();
    map.put("msg_id", obj);
    map.put("operate_type", opreateType);
    return JacksonUtils.jsonMapSerializer(map);
  }

  /**
   * crossref数据保存到基准库
   * 
   * @param originalId
   * @return
   */
  public static String getSavePdwhPubDataFromCrossref(String obj) {
    Map<String, String> map = new HashMap<String, String>();
    map.put("msg_id", obj);
    return JacksonUtils.jsonMapSerializer(map);
  }
}
