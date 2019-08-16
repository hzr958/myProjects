package com.smate.center.open.service.data.isis;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.exception.OpenIsisStatDataException;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.json.JacksonUtils;


/**
 * 结题项目成果按 单位统计
 * 
 * @author hp
 * @date 2016-02-17
 */

@Transactional(rollbackFor = Exception.class)
public class IsisPubStatImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private NsfcStatService nsfcStatService;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object data = paramet.get(OpenConsts.MAP_DATA);
    if (data == null || StringUtils.isEmpty(data.toString())) {
      logger.error("Open系统-接收接口-输入数据不能为空");
      temp = super.errorMap("Open系统-接收接口-输入数据不能为空", paramet, "Open系统-接收接口-输入数据不能为空");
      return temp;
    }

    @SuppressWarnings("unchecked")
    Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
    if (dataMap == null) {
      logger.error("项目结题服务 参数 格式不正确 不能转换成map");
      temp = super.errorMap("项目结题服务 参数 格式不正确 不能转换成map", paramet, "");
      return temp;
    }
    if (dataMap.get("ins_name") == null) {
      logger.error("项目结题服务 参数 单位名称不能为空");
      temp = super.errorMap("项目结题服务 参数 单位名称不能为空", paramet, "");
      return temp;
    }
    if (dataMap.get("rpt_year") == null || !NumberUtils.isDigits(dataMap.get("rpt_year").toString())
        || StringUtils.length(dataMap.get("rpt_year").toString()) != 4) {
      logger.error("项目结题服务 参数 报告年度 格式不正确,正确格式为yyyy");
      temp = super.errorMap("项目结题服务 参数 报告年度 格式不正确,正确格式为yyyy", paramet, "");
      return temp;
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);

    return temp;
  }

  /**
   * 
   * 
   * @param ins_name rpt_year
   * @throws OpenIsisStatDataException
   */
  @SuppressWarnings({"unchecked", "deprecation"})
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    try {
      Map<String, Object> temp = new HashMap<String, Object>();
      Object data = paramet.get(OpenConsts.MAP_DATA);

      Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());


      List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

      // 获取 SnsNsfcPrjReport LIST
      // List<SnsNsfcPrjReport> nsfcPrjReportList = nsfcStatService.getNsfcPrjReportList(dataMap);
      // 根据 RPT_ID获取成果
      // Set<Long> rptIdSet = new HashSet<Long>();
      // for(SnsNsfcPrjReport npr:nsfcPrjReportList){
      // rptIdSet.add(npr.getRptId());
      // }
      // List<SnsNsfcPrjRptPub> nsfcPrjRptPubList = new ArrayList<SnsNsfcPrjRptPub>();
      // if(rptIdSet.size()>0){
      // nsfcPrjRptPubList = nsfcStatService.getPubsByRptIds(rptIdSet);
      // }
      // 根据成果获取统计数
      Map<String, Object> statData = nsfcStatService.getPubStatByIns(dataMap);

      Map<String, Object> map = new HashMap<String, Object>();
      Document document = DocumentHelper.createDocument();
      Element pubStat = document.addElement("pub_stat");
      for (String key : statData.keySet()) {
        Map<String, Object> subMap = (Map<String, Object>) statData.get(key);
        Element ele = pubStat.addElement(key);
        for (String subkey : subMap.keySet()) {
          ele.setAttributeValue(subkey, subMap.get(subkey) == null ? "0" : subMap.get(subkey).toString());
        }
      }
      map.put("data", document.asXML());
      dataList.add(map);
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, "scm-0000");// 响应成功
      temp.put(OpenConsts.RESULT_DATA, dataList);
      return temp;
    } catch (Exception e) {
      logger.error("获取项目结题数据服务异常 map=" + paramet.toString(), e);
      throw new OpenIsisStatDataException(e);
    }
  }

}
