package com.smate.center.open.service.data.iris;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 合肥工业大学获取会议、期刊 被收录统计WS服务
 * 
 * @author zll
 *
 */
@Transactional(rollbackFor = Exception.class)
public class HfutGetPubListStatsImpl extends ThirdDataTypeBase {
  @Autowired
  HfutPubListStatsService hfutPubListStatsService;

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object data = paramet.get(OpenConsts.MAP_DATA);
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    if (data == null || StringUtils.isEmpty(data.toString())) {
      logger.error("Open系统-接收接口-输入数据不能为空");
      temp = super.errorMap("Open系统-接收接口-输入数据不能为空", paramet, "Open系统-接收接口-输入数据不能为空");
      return temp;
    }
    Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
    if (dataMap == null) {
      logger.error("合肥工业大学获取会议、期刊 被收录统计WS服务 参数 格式不正确 不能转换成map");
      temp = super.errorMap("合肥工业大学获取会议、期刊 被收录统计WS服务 参数 格式不正确 不能转换成map", paramet, "");
      return temp;
    }
    if (dataMap.get("typeId") == null || !NumberUtils.isDigits(dataMap.get("typeId").toString())
        || (Integer.valueOf((dataMap.get("typeId").toString())) != 3
            && Integer.valueOf((dataMap.get("typeId").toString())) != 4)) {
      logger.error("合肥工业大学获取会议、期刊 被收录统计WS服务 参数成果类型(typeId)不能为空或非数字或类型不对");
      temp = super.errorMap("合肥工业大学获取会议、期刊 被收录统计WS服务 参数成果类型(typeId)不能为空或非数字或类型不对", paramet, "");
      return temp;
    }
    if (dataMap.get("startDate") == null) {
      logger.error("合肥工业大学获取会议、期刊 被收录统计WS服务 参数开始发表日期(startDate)不能为空");
      temp = super.errorMap("合肥工业大学获取会议、期刊 被收录统计WS服务 参数开始发表日期(startDate)不能为空", paramet, "");
      return temp;
    } else {
      try {
        format.parse(dataMap.get("startDate").toString());
      } catch (Exception e) {
        logger.error("合肥工业大学获取会议、期刊 被收录统计WS服务 参数开始发表日期(startDate)日期格式错误");
        temp = super.errorMap("合肥工业大学获取会议、期刊 被收录统计WS服务 参数开始发表日期(startDate)日期格式错误", paramet, "");
        return temp;
      }
    }
    if (dataMap.get("endDate") == null) {
      logger.error("合肥工业大学获取会议、期刊 被收录统计WS服务 参数开始发表日期(endDate)不能为空");
      temp = super.errorMap("合肥工业大学获取会议、期刊 被收录统计WS服务 参数开始发表日期(endDate)不能为空", paramet, "");
      return temp;
    } else {
      try {
        format.parse(dataMap.get("endDate").toString());
      } catch (Exception e) {
        logger.error("合肥工业大学获取会议、期刊 被收录统计WS服务 参数结束发表日期(endDate)日期格式错误");
        temp = super.errorMap("合肥工业大学获取会议、期刊 被收录统计WS服务 参数结束发表日期(endDate)日期格式错误", paramet, "");
        return temp;
      }
    }
    try {
      if (format.parse(dataMap.get("startDate").toString()).getTime() > format.parse(dataMap.get("endDate").toString())
          .getTime()) {
        logger.error("合肥工业大学获取会议、期刊 被收录统计WS服务 参数开始发表日期(startDate)不能大于结束发表日期(endDate)");
        temp = super.errorMap("合肥工业大学获取会议、期刊 被收录统计WS服务 参数开始发表日期(startDate)不能大于结束发表日期(endDate)", paramet, "");
        return temp;
      }
    } catch (ParseException e) {
      logger.error("合肥工业大学获取会议、期刊 被收录统计WS服务 参数日期比较出错");
      temp = super.errorMap("合肥工业大学获取会议、期刊 被收录统计WS服务 参数日期比较出错", paramet, "");
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {

    Map<String, Object> temp = new HashMap<String, Object>();
    Object data = paramet.get(OpenConsts.MAP_DATA);
    Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
    Map<String, Object> pubListStats = new HashMap<String, Object>();
    pubListStats = hfutPubListStatsService.getPubListStats(pubListStats, dataMap);
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Document document = DocumentHelper.createDocument();
    Element publicationList = document.addElement("publicationList");
    if (pubListStats != null) {
      publicationList.addElement("type_id").addText(ObjectUtils.toString(dataMap.get("typeId")));
      publicationList.addElement("count_ei").addText(ObjectUtils.toString(pubListStats.get("totalCountEi")));
      publicationList.addElement("count_sci").addText(ObjectUtils.toString(pubListStats.get("totalCountSci")));
      publicationList.addElement("count_ssci").addText(ObjectUtils.toString(pubListStats.get("totalCountSsci")));
      publicationList.addElement("count_istp").addText(ObjectUtils.toString(pubListStats.get("totalCountIstp")));
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("data", document.asXML());
    dataList.add(map);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    temp.put(OpenConsts.RESULT_MSG, "scm-0000");// 响应成功
    temp.put(OpenConsts.RESULT_DATA, dataList);
    return temp;
  }

}
