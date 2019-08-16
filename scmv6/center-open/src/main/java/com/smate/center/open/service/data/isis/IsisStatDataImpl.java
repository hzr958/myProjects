package com.smate.center.open.service.data.isis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.exception.OpenIsisStatDataException;
import com.smate.center.open.isis.model.data.isis.NsfcPrjPubReport;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 向基金委提供项目结题数据-接口
 * 
 * @author hp
 * @date 2015-10-21
 */

@Transactional(rollbackFor = Exception.class)
public class IsisStatDataImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private NsfcStatService nsfcStatService;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object data = paramet.get(OpenConsts.MAP_DATA);
    if (data == null || StringUtils.isEmpty(data.toString())) {
      logger.error("Open系统-接收接口-输入数据不能为空,服务码:5fc82b4c");
      temp = super.errorMap("Open系统-接收接口-输入数据不能为空", paramet, "Open系统-接收接口-输入数据不能为空,服务码:5fc82b4c");
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
  @SuppressWarnings("deprecation")
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    try {
      Map<String, Object> temp = new HashMap<String, Object>();
      Object data = paramet.get(OpenConsts.MAP_DATA);
      @SuppressWarnings("unchecked")
      Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
      Object o = dataMap.get("rpt_year");
      Long rptYear = Long.valueOf(o.toString());
      List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
      List<NsfcPrjPubReport> nsfcPrjPubReportList =
          nsfcStatService.getPrjPubReportList(ObjectUtils.toString(dataMap.get("ins_name")), rptYear);
      for (NsfcPrjPubReport n : nsfcPrjPubReportList) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("prj_id", n.getNsfcPrjId());
        map.put("pub_id", n.getNsfcPrjPubReportPK().getPubId());
        map.put("ssci", n.getSsci());
        map.put("sci", n.getSci());
        map.put("istp", n.getIstp());
        map.put("ei", n.getEi());
        map.put("hxj", n.getHxj());
        map.put("pub_type", n.getPubType());
        map.put("tag", n.getIsTag());
        map.put("open",
            StringUtils.equals("Blue", n.getRomeoColour()) || StringUtils.equals("Yellow", n.getRomeoColour())
                || StringUtils.equals("Green", n.getRomeoColour()) ? 1 : 0);
        dataList.add(map);
      }

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
