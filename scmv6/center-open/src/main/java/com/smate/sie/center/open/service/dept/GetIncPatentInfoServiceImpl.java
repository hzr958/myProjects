package com.smate.sie.center.open.service.dept;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.dao.data.OpenThirdRegDao;
import com.smate.center.open.dao.data.OpenTokenServiceConstDao;
import com.smate.center.open.model.OpenTokenServiceConst;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.dao.security.role.Sie6InsPortalDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.sie.center.open.dao.dept.SiePatentInfoDao;
import com.smate.sie.core.base.utils.date.DateUtils;
import com.smate.sie.core.base.utils.model.pub.SiePatent;
import com.smate.sie.core.base.utils.pub.dom.PatAppliersBean;
import com.smate.sie.core.base.utils.pub.dom.PatentInfoBean;
import com.smate.sie.core.base.utils.pub.dom.PubDetailDOM;
import com.smate.sie.core.base.utils.pub.service.PatJsonPOService;

/**
 * 增量获取按单位专利接口
 * 
 * @author lijianming
 *
 */
@SuppressWarnings("deprecation")
@Transactional(rollbackFor = Exception.class)
public class GetIncPatentInfoServiceImpl extends ThirdDataTypeBase {

  @Autowired
  private OpenTokenServiceConstDao openTokenServiceConstDao;
  @Autowired
  SiePatentInfoDao siePatentInfoDao;
  @Autowired
  private PatJsonPOService patJsonPOService;
  @Autowired
  Sie6InsPortalDao sieInsPortalDao;
  @Autowired
  OpenThirdRegDao openThirdRegDao;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object data = paramet.get(OpenConsts.MAP_DATA);
    if (data == null || StringUtils.isEmpty(data.toString())) {
      logger.error("Open系统-接收接口-输入数据不能为空");
      temp = super.errorMap("Open系统-接收接口-输入数据不能为空", paramet, "Open系统-接收接口-输入数据不能为空");
      return temp;
    }

    Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
    if (dataMap == null) {
      logger.error(" 参数 格式不正确 不能转换成map");
      temp = super.errorMap(" 参数 格式不正确 不能转换成map", paramet, "");
      return temp;
    }

    if (dataMap.get("pageNo") == null || !NumberUtils.isDigits(dataMap.get("pageNo").toString())) {
      logger.error(" 参数 pageNo 不能为空或非数字");
      temp = super.errorMap(" 参数 pageNo 不能为空或非数字", paramet, "");
      return temp;
    }
    if (dataMap.get("pageSize") == null || !NumberUtils.isDigits(dataMap.get("pageSize").toString())) {
      logger.error(" 参数 pageSize 不能为空或非数字");
      temp = super.errorMap(" 参数 pageSize 不能为空或非数字", paramet, "");
      return temp;
    }
    // 更新时间：开始时间
    if (dataMap.get("startUpdateDate") == null
        || DateUtils.parseStringToDate(dataMap.get("startUpdateDate").toString()) == null) {
      logger.error("参数startUpdateDate 不能为空或非时间格式");
      temp = super.errorMap("参数startUpdateDate 不能为空或非时间格式", paramet, "");
      return temp;
    }
    // 更新时间：结束时间
    if (StringUtils.isNotBlank(dataMap.get("endUpdateDate").toString())) {
      if (DateUtils.parseStringToDate(dataMap.get("endUpdateDate").toString()) == null) {
        logger.error("参数endUpdateDate 非时间格式");
        temp = super.errorMap("参数endUpdateDate 非时间格式", paramet, "");
        return temp;
      }
    }

    // 时间对比
    if (dataMap.get("startUpdateDate") != null && dataMap.get("endUpdateDate") != null) {
      Date startDate = DateUtils.parseStringToDate(dataMap.get("startUpdateDate").toString());
      Date endDate = DateUtils.parseStringToDate(dataMap.get("endUpdateDate").toString());
      if (startDate != null && endDate != null) {
        if (startDate.compareTo(endDate) > 0) {
          logger.error("参数startUpdateDate时间大于参数endUpdateDate时间");
          temp = super.errorMap("参数startUpdateDate时间大于参数endUpdateDate时间", paramet, "");
          return temp;
        }
      }
    }

    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object mapData = paramet.get(OpenConsts.MAP_DATA);
    Map<String, Object> data = JacksonUtils.jsonToMap(mapData.toString());
    Page<SiePatent> page = new Page<SiePatent>();
    String token = paramet.get(OpenConsts.MAP_TOKEN).toString();
    String serviceType = paramet.get(OpenConsts.MAP_TYPE).toString();
    OpenTokenServiceConst openTokenServiceConst =
        openTokenServiceConstDao.findObjBytokenAndServiceType(token, serviceType);
    if (openTokenServiceConst.getInsId() == null || openTokenServiceConst.getInsId() == 0) {
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, "单位id不能为空，请检查是否已配置单位id");
      return temp;
    }

    data.put("insId", openTokenServiceConst.getInsId());
    page.setPageNo(Integer.valueOf(data.get("pageNo").toString()));
    page.setPageSize(Integer.valueOf(data.get("pageSize").toString()));
    page = siePatentInfoDao.getPatsNoOrderByDate(data, page);
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("page_no", ObjectUtils.toString(page.getPageNo()));
    map.put("page_size", ObjectUtils.toString(page.getPageSize()));
    map.put("total_pages", ObjectUtils.toString(page.getTotalPages()));
    map.put("total_count", ObjectUtils.toString(page.getTotalCount()));
    dataList.add(map);

    if (!CollectionUtils.isEmpty(page.getResult())) {
      for (SiePatent pat : page.getResult()) {
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        // 获取json字符串，并且构造成PubDetailDOM对象
        PubDetailDOM detail = patJsonPOService.getDOMByIdAndType(pat.getPatId());
        if (detail == null) {
          continue;
        }
        PatentInfoBean bean = (PatentInfoBean) detail.getTypeInfo();
        String patentType = "";
        String legalStatus = "";
        String patentOpenNo = null != bean ? bean.getPublicationOpenNo() : "";
        String remark = null != detail ? StringEscapeUtils.escapeHtml4(detail.getSummary()) : "";
        String inventor = null != detail ? detail.getAuthorNames() : "";

        // *专利类别：发明专利51 实用新型 52 外观专利53 国际专利54
        String patType = null != bean ? bean.getTypeCode() : "";
        if ("51".equals(patType)) {
          patentType = "发明专利";
        } else if ("52".equals(patType)) {
          patentType = "实用新型";
        } else if ("53".equals(patType)) {
          patentType = "外观专利";
        } else if ("54".equals(patType)) {
          patentType = "国际专利";
        } else {
          patentType = "";
        }
        String patentNo = null != bean ? bean.getApplicationNo() : "";
        // *专利状态：申请0 授权1
        String legalStatusStr = null != bean ? bean.getPatentStatusCode() : "";
        if ("1".equals(legalStatusStr)) {
          legalStatus = "授权";
          patentNo = null != bean ? bean.getAuthNo() : "";
        } else if ("0".equals(legalStatusStr)) {
          legalStatus = "申请";
        } else if ("2".equals(legalStatusStr)) {
          legalStatus = "视撤";
        } else if ("3".equals(legalStatusStr)) {
          legalStatus = "有效";
        } else if ("4".equals(legalStatusStr)) {
          legalStatus = "失效";
        } else {
          legalStatus = "";
        }
        dataMap.put("pat_id", pat.getPatId());
        dataMap.put("zh_title", ObjectUtils.toString(null != pat.getZhTitle() ? pat.getZhTitle() : pat.getEnTitle()));
        dataMap.put("patent_type", patentType);
        dataMap.put("legal_status", legalStatus);
        dataMap.put("apply_no", patentNo);// 申请号 / 授权号 (授权阶段取授权号,否则取申请号)
        String authNo = null != bean ? bean.getAuthNo() : "";
        dataMap.put("grant_no", patentOpenNo);// 公开号
        dataMap.put("apply_time", detail.getPublishDate());// 发表时间
        dataMap.put("grant_time", null != bean ? bean.getAuthDate() : "");// 公开日期 即授权时间
        dataMap.put("remark", remark);// 专利描述
        dataMap.put("ipc_no", null != bean ? bean.getIPC() : "");// 主分类号
        dataMap.put("patentee", getPatentee(bean));// 专利权人
        dataMap.put("inventor", inventor);// 发明人

        dataMap.put("view_url", getPatAddress(pat.getPatId(), openTokenServiceConst.getInsId()));
        dataList.add(dataMap);
      }
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    temp.put(OpenConsts.RESULT_MSG, "scm-0000");// 响应成功
    temp.put(OpenConsts.RESULT_DATA, dataList);
    return temp;
  }

  private String getPatentee(PatentInfoBean bean) {
    // 获取专利权人的节点
    List<PatAppliersBean> appliers = bean.getAppliers();
    if (CollectionUtils.isEmpty(appliers) && appliers.size() == 0) {
      return "";
    }
    StringBuilder applierNames = new StringBuilder();
    for (int i = 0; i < appliers.size(); i++) {
      PatAppliersBean app = appliers.get(i);
      String applierName = StringUtils.trimToEmpty(app.getApplierName());
      if (i == 0) {
        applierNames.append(applierName);
      } else {
        applierNames.append(";");
        applierNames.append(applierName);
      }
    }
    return applierNames.toString();
  }

  private String getPatAddress(Long patId, Long insId) {
    StringBuilder sbl = new StringBuilder();
    sbl.append("https://");
    sbl.append(sieInsPortalDao.get(insId).getDomain());
    sbl.append("/pubweb/patent/view?des3Id=");
    sbl.append(ServiceUtil.encodeToDes3(patId + ""));
    return sbl.toString();
  }

}
