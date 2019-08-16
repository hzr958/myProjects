package com.smate.sie.center.open.service.dept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.model.publication.PubXmlDocument;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.dao.consts.Sie6ConstDictionaryDao;
import com.smate.core.base.utils.dao.consts.SieConstPatTypeDao;
import com.smate.core.base.utils.dao.security.role.Sie6InsPortalDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.sie.center.open.dao.dept.SiePatentInfoDao;
import com.smate.sie.core.base.utils.dao.pub.SiePatXmlDao;
import com.smate.sie.core.base.utils.model.pub.SiePatXml;
import com.smate.sie.core.base.utils.model.pub.SiePatent;

/**
 * 专供广东阳光政务平台换用的新接口 （sieptatn）
 */
@SuppressWarnings("deprecation")
@Transactional(rollbackFor = Exception.class)
public class GetPatentInfoByInsServiceImpl extends ThirdDataTypeBase {

  @Autowired
  SiePatentInfoDao siePatentInfoDao;
  @Autowired
  SiePatXmlDao siePatXmlDao;
  @Autowired
  SieConstPatTypeDao sieConstPatTypeDao;
  @Autowired
  Sie6ConstDictionaryDao sie6ConstDictionaryDao;
  @Autowired
  Sie6InsPortalDao sieInsPortalDao;

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

    if (dataMap.get("insId") == null || !NumberUtils.isDigits(dataMap.get("insId").toString())) {
      logger.error(" 参数 insId 不能为空或非数字");
      temp = super.errorMap(" 参数 insId 不能为空或非数字", paramet, "");
      return temp;
    }

    if (dataMap.get("pageNo") != null && !NumberUtils.isDigits(dataMap.get("pageNo").toString())) {
      logger.error(" 参数 pageNo 不能为非数字");
      temp = super.errorMap(" 参数 pageNo 不能为非数字", paramet, "");
      return temp;
    }
    if (dataMap.get("pageSize") != null && !NumberUtils.isDigits(dataMap.get("pageSize").toString())) {
      logger.error(" 参数 pageSize 不能为非数字");
      temp = super.errorMap(" 参数 pageSize 不能为非数字", paramet, "");
      return temp;
    }
    if (!StringUtils.isBlank(ObjectUtils.toString(dataMap.get("orderType")))
        && (!"desc".equalsIgnoreCase(dataMap.get("orderType").toString()))
        && (!"asc".equals(dataMap.get("orderType").toString()))) {
      logger.error(" 参数 排序方式(orderType)错误，正确排序方式为'asc'或'desc'");
      temp = super.errorMap(" 参数  排序方式(orderType)错误，正确排序方式为'asc'或'desc'", paramet, "");
      return temp;
    }

    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @SuppressWarnings("unused")
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object mapData = paramet.get(OpenConsts.MAP_DATA);
    Map<String, Object> data = JacksonUtils.jsonToMap(mapData.toString());
    Page<SiePatent> page = new Page<SiePatent>();
    Integer pageNo = org.springframework.util.ObjectUtils.isEmpty(data.get("pageNo")) ? 1
        : NumberUtils.createInteger(data.get("pageNo").toString());
    Integer pageSize = org.springframework.util.ObjectUtils.isEmpty(data.get("pageSize")) ? 10
        : NumberUtils.createInteger(data.get("pageSize").toString());
    page.setPageNo(pageNo);
    page.setPageSize(pageSize);
    // 查询数据
    page = siePatentInfoDao.getPatsByMap(data, page);
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    // 获取分页参数
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("page_no", ObjectUtils.toString(page.getPageNo()));
    map.put("page_size", ObjectUtils.toString(page.getPageSize()));
    map.put("total_pages", ObjectUtils.toString(page.getTotalPages()));
    map.put("total_count", ObjectUtils.toString(page.getTotalCount()));
    dataList.add(map);
    if (!CollectionUtils.isEmpty(page.getResult())) {
      for (SiePatent pat : page.getResult()) {
        String applyTime = "";
        String grantTime = "";
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        PubXmlDocument pubXmlDoc = this.getSiePatXml(pat.getPatId());
        if (pubXmlDoc == null) {
          continue;
        }
        String patentType = null;
        String legalStatus = null;
        String patentNo = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_no");
        String patentOpenNo = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_open_no");
        String zh_abstract = StringEscapeUtils
            .escapeHtml4("" != pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_abstract")
                ? pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_abstract")
                : "");
        String zh_keywords = StringEscapeUtils
            .escapeHtml4("" != pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords")
                ? pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords")
                : "");
        String inventor = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names");
        // *专利类别：发明专利51 实用新型 52 外观专利53 国际专利54
        String patType = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_type");
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
        // *专利状态：申请0 授权1
        String legalStatusStr = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_status");
        if ("1".equals(legalStatusStr)) {
          legalStatus = "授权";
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
        if (null != pat.getApplyYear()) {
          applyTime = pat.getApplyYear() + "";
          if (null != pat.getApplyMonth()) {
            applyTime = applyTime + "/" + pat.getApplyMonth();
            if (null != pat.getApplyDay()) {
              applyTime = applyTime + "/" + pat.getApplyDay();
            }
          }
        }
        // 授权时，才显示
        if ("1".equals(legalStatusStr)) {
          String tempYear = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "start_year");
          String tempMonth = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "start_month");
          String tempDay = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "start_day");
          if (StringUtils.isNotEmpty(tempYear)) {
            grantTime = tempYear;
            if (StringUtils.isNotEmpty(tempMonth)) {
              grantTime = grantTime + "/" + tempMonth;
              if (StringUtils.isNotEmpty(tempDay)) {
                grantTime = grantTime + "/" + tempDay;
              }
            }
          }
        }
        dataMap.put("pat_id", pat.getPatId());
        dataMap.put("zh_title", pat.getZhTitle());
        dataMap.put("patent_type", patentType);//
        dataMap.put("legal_status", legalStatus);// 法律状态
        dataMap.put("apply_time", applyTime);// 申请日期
        dataMap.put("grant_time", grantTime);// 授权日期
        dataMap.put("zh_abstract", zh_abstract);// 专利描述
        dataMap.put("zh_keywords", zh_keywords);// 专利关键词
        dataMap.put("apply_no", patentNo);// 申请号
        dataMap.put("grant_no", pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "auth_no"));// 授权号
        dataMap.put("view_url", getPatAddress(pat.getPatId(), pat.getInsId()));
        dataMap.put("inventor", inventor);// 发明人
        dataMap.put("ipc_no", pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "ipc"));// 主分类号
        dataMap.put("cpc_no", pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cpc"));// 主分类号
        dataList.add(dataMap);
      }
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    temp.put(OpenConsts.RESULT_MSG, "scm-0000");// 响应成功
    temp.put(OpenConsts.RESULT_DATA, dataList);
    return temp;
  }

  @SuppressWarnings("rawtypes")
  private String getPatentee(PubXmlDocument pubXmlDoc) {
    // 获取专利权人的节点
    List appliers = pubXmlDoc.getNodes(PubXmlConstants.PUB_APPLIERS_APPLIER_XPATH);
    StringBuilder applierNames = new StringBuilder();
    for (int i = 0; i < appliers.size(); i++) {
      Element ele = (Element) appliers.get(i);
      String applierName = org.apache.commons.lang.StringUtils.trimToEmpty(ele.attributeValue("applier_name"));
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

  /**
   * 获取单位成果XML.
   * 
   * @param patId
   * @return
   */
  private PubXmlDocument getSiePatXml(Long patId) {
    PubXmlDocument newPubXmlDocument = null;
    try {
      SiePatXml siePatXml = siePatXmlDao.get(patId);
      if (siePatXml != null) {
        newPubXmlDocument = new PubXmlDocument(siePatXml.getPatXml());
      } else {
        newPubXmlDocument =
            new PubXmlDocument("<?xml version=\"1.0\" encoding=\"UTF-8\"?><data><publication /></data>");
      }
    } catch (Exception e) {
      logger.error("获取单位专利XML出现异常了喔,patId={}", patId, e);
    }
    return newPubXmlDocument;
  }

}
