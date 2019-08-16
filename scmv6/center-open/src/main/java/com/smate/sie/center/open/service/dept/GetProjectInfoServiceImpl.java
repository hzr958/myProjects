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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.dao.data.OpenTokenServiceConstDao;
import com.smate.center.open.model.OpenTokenServiceConst;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.dao.security.role.Sie6InsPortalDao;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.rol.Sie6InsPortal;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.sie.center.open.project.json.dao.PrjJsonPoDao;
import com.smate.sie.center.open.project.json.model.PrjDetailDOM;
import com.smate.sie.core.base.utils.dao.prj.SiePrjDisciplineDao;
import com.smate.sie.core.base.utils.dao.prj.SiePrjKewordDao;
import com.smate.sie.core.base.utils.dao.prj.SiePrjMemberDao;
import com.smate.sie.core.base.utils.dao.prj.SiePrjXmlDao;
import com.smate.sie.core.base.utils.dao.prj.SieProjectDao;
import com.smate.sie.core.base.utils.dao.psn.SieInsPersonDao;
import com.smate.sie.core.base.utils.model.prj.ProjectJsonPo;
import com.smate.sie.core.base.utils.model.prj.SiePrjDiscipline;
import com.smate.sie.core.base.utils.model.prj.SiePrjKeword;
import com.smate.sie.core.base.utils.model.prj.SiePrjMember;
import com.smate.sie.core.base.utils.model.prj.SieProject;
import com.smate.sie.core.base.utils.xml.SiePrjXmlConstants;
import com.smate.sie.core.base.utils.xml.SiePrjXmlDocument;

@SuppressWarnings("deprecation")
@Transactional(rollbackFor = Exception.class)
public class GetProjectInfoServiceImpl extends ThirdDataTypeBase {

  @Autowired
  private SieProjectDao sieProjectDao;
  @Autowired
  private OpenTokenServiceConstDao openTokenServiceConstDao;
  @Autowired
  private Sie6InsPortalDao sieInsPortalDao;
  @Autowired
  private SiePrjXmlDao siePrjXmlDao;
  @Autowired
  private SieInsPersonDao sieInsPersonDao;
  @Autowired
  private PrjJsonPoDao prjJsonPoDao;
  @Autowired
  private SiePrjMemberDao siePrjMemberDao;
  @Autowired
  private SiePrjKewordDao prjKewordDao;
  @Autowired
  private SiePrjDisciplineDao prjDisciplineDao;

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

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object mapData = paramet.get(OpenConsts.MAP_DATA);
    Map<String, Object> data = JacksonUtils.jsonToMap(mapData.toString());
    Page<SieProject> page = new Page<SieProject>();
    page.setPageNo(Integer.valueOf(data.get("pageNo").toString()));
    page.setPageSize(Integer.valueOf(data.get("pageSize").toString()));
    String token = paramet.get(OpenConsts.MAP_TOKEN).toString();
    String serviceType = paramet.get(OpenConsts.MAP_TYPE).toString();
    OpenTokenServiceConst openTokenServiceConst =
        openTokenServiceConstDao.findObjBytokenAndServiceType(token, serviceType);
    if (openTokenServiceConst.getInsId() == null || openTokenServiceConst.getInsId() == 0) {
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, "单位id不能为空，请检查是否已配置单位id");
      return temp;
    }
    Long insId = openTokenServiceConst.getInsId();
    data.put("insId", insId);
    page = getPage(data, page);
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("page_no", ObjectUtils.toString(page.getPageNo()));
    map.put("page_size", ObjectUtils.toString(page.getPageSize()));
    map.put("total_pages", ObjectUtils.toString(page.getTotalPages()));
    map.put("total_count", ObjectUtils.toString(page.getTotalCount()));
    dataList.add(map);
    if (!CollectionUtils.isEmpty(page.getResult())) {
      PrjDetailDOM detail = null;
      ProjectJsonPo projectJsonPo = null;
      for (SieProject prj : page.getResult()) {
        /*
         * SiePrjXml siePrjXml = siePrjXmlDao.get(prj.getId()); SiePrjXmlDocument xmlDocument = null; try {
         * if (siePrjXml == null) { siePrjXml = new SiePrjXml(); siePrjXml.setPrjId(prj.getId());
         * xmlDocument = new SiePrjXmlDocument(); } else { xmlDocument = new
         * SiePrjXmlDocument(siePrjXml.getPrjXml()); } } catch (DocumentException e) { e.printStackTrace();
         * }
         */
        detail = new PrjDetailDOM();
        projectJsonPo = new ProjectJsonPo();
        try {
          projectJsonPo = prjJsonPoDao.getJsonByPrjId(prj.getId());
          if (projectJsonPo != null) {
            String prjJson = projectJsonPo.getPrjJson();
            detail = JacksonUtils.jsonObject(prjJson, PrjDetailDOM.class);
          }
        } catch (Exception e) {
          logger.error("查询项目详情时出错！prjId={}", prj.getId());
          e.printStackTrace();
        }
        // 获取项目负责人，notifyAuthor：1
        List<SiePrjMember> prjMemberList = siePrjMemberDao.getMainMemberByPrjId(prj.getId());
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        dataMap.put("zh_title", ObjectUtils.toString(prj.getZhTitle()));
        // 项目来源
        dataMap.put("project_from", ObjectUtils.toString(prj.getPrjFromId()));// 原来是“国家自然科学基金”，现在是数字（5代表）
        // 资助类别
        dataMap.put("grant_name", ObjectUtils.toString(prj.getGrantZhName()));
        // 开始时间
        dataMap.put("start_date", getStartDate(prj));
        // 结束时间
        dataMap.put("end_date", ObjectUtils.toString(getEndDate(prj)));
        // 立项时间
        dataMap.put("stat_year", ObjectUtils.toString(prj.getStatYear()));
        dataMap.put("ins_name", ObjectUtils.toString(getInsName(prj)));
        // 立项批准号，项目编号，合同编号
        dataMap.put("approve_number", ObjectUtils.toString(prj.getExternalNo()));
        // 立项金额
        dataMap.put("total_amt", prj.getAmount());
        dataMap.put("prj_status", ObjectUtils.toString(prj.getPrjStatus()));
        dataMap.put("unit_name", getUnitName(prjMemberList));
        // 学科代码
        dataMap.put("dis_code", getDisCode(prj.getId())); // 原来是国家自然科学基金代码，现在是SIE代码
        // 摘要
        dataMap.put("summary", getSummaryByDom(detail));
        // 关键词
        dataMap.put("key_words", getKeyWords(prj.getInsId(), prj.getId()));
        dataMap.put("psn_name", getPsnName(prjMemberList));
        // 项目参与人
        dataMap.put("members", prj.getAuthorNames());// 原来是人名+邮箱，现在是只包含人名
        // 项目详情页
        dataMap.put("view_url", getPrjAddress(prj.getId(), prj.getInsId()));
        dataMap.put("prj_id", prj.getId());
        dataList.add(dataMap);
      }
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    temp.put(OpenConsts.RESULT_MSG, "scm-0000");// 响应成功
    temp.put(OpenConsts.RESULT_DATA, dataList);
    return temp;
  }

  @Deprecated
  private Object getKeyWords(SiePrjXmlDocument xmlDocument) {
    String keyWords = null;
    if (xmlDocument == null) {
      keyWords = "";
    } else {
      keyWords = xmlDocument.getXmlNodeAttribute(SiePrjXmlConstants.PROJECT_XPATH, "ckeywords");
      keyWords = XmlUtil.trimStartEndP(keyWords);
      keyWords = XmlUtil.trimThreateningHtml(keyWords);
      keyWords = keyWords.replaceAll("(&nbsp;)+", " ");
      if ("".equals(XmlUtil.trimAllHtml(keyWords))) {
        keyWords = "";
      }
    }
    return StringEscapeUtils.escapeHtml4(keyWords);
  }

  private Object getKeyWordsByDom(PrjDetailDOM detailDOM) {
    String keyWords = "";
    if (detailDOM != null) {
      keyWords = detailDOM.getKeywords();
      if (StringUtils.isNotBlank(keyWords)) {
        keyWords = XmlUtil.trimStartEndP(keyWords);
        keyWords = XmlUtil.trimThreateningHtml(keyWords);
        keyWords = keyWords.replaceAll("(&nbsp;)+", " ");
        if ("".equals(XmlUtil.trimAllHtml(keyWords))) {
          keyWords = "";
        }
      }
    }
    return keyWords;
  }

  private Object getKeyWords(Long insId, Long prjId) {
    List<SiePrjKeword> keywordList = prjKewordDao.getPrjKeyWords(insId, prjId);
    String keywords = "";
    if (CollectionUtils.isNotEmpty(keywordList)) {
      for (int i = 0; i < keywordList.size(); i++) {
        if (i != keywordList.size() - 1) {
          keywords += keywordList.get(i).getKeyword() + ";";
        } else {
          keywords += keywordList.get(i).getKeyword();
        }
      }
    }
    return keywords;
  }

  private Object getUnitName(List<SiePrjMember> prjMemberList) {
    if (CollectionUtils.isNotEmpty(prjMemberList)) {
      return prjMemberList.get(0).getUnitName();
    }
    return "";
  }

  private Object getPsnName(List<SiePrjMember> prjMemberList) {
    if (CollectionUtils.isNotEmpty(prjMemberList)) {
      return prjMemberList.get(0).getName();
    }
    return "";
  }

  /**
   * 检查接口是否需要判断负责人的信息
   */
  private Page<SieProject> getPage(Map<String, Object> data, Page<SieProject> page) {
    Page<SieProject> tempPage = sieProjectDao.getProjectInfo(data, page);
    String author = ObjectUtils.toString(data.get("psnName"));
    if (!StringUtils.isBlank(author)) {
      String psnIDs = getPsnIDs(author);
      List<SieProject> result = tempPage.getResult();
      List<SieProject> newresult = new ArrayList<>();
      for (SieProject sieProject : result) {
        if (psnIDs.matches(".*" + sieProject.getPsnId() + ".*")) {
          newresult.add(sieProject);
        }
      }
      tempPage.setResult(newresult);
    }
    return tempPage;
  }

  /**
   * 通过名字获取对应的PSNID
   */
  private String getPsnIDs(String author) {
    List<Long> psnIdByPsnName = sieInsPersonDao.getPsnIdByPsnName(author);
    StringBuilder sbl = new StringBuilder();
    for (Long temp : psnIdByPsnName) {
      sbl.append(temp.toString());
      sbl.append(",");
    }
    return sbl.toString();
  }

  @Deprecated
  private String getSummary(SiePrjXmlDocument xmlDocument) {
    String cabstract = null;
    if (xmlDocument == null) {
      cabstract = "";
    } else {
      cabstract = xmlDocument.getXmlNodeAttribute(SiePrjXmlConstants.PROJECT_XPATH, "cabstract");
      cabstract = XmlUtil.trimStartEndP(cabstract);
      cabstract = XmlUtil.trimThreateningHtml(cabstract);
      cabstract = cabstract.replaceAll("(&nbsp;)+", " ");
      if ("".equals(XmlUtil.trimAllHtml(cabstract))) {
        cabstract = "";
      }
    }
    return StringEscapeUtils.escapeHtml4(cabstract);
  }

  private String getSummaryByDom(PrjDetailDOM detailDOM) {
    String cabstract = "";
    if (detailDOM != null) {
      cabstract = detailDOM.getSummary();
      if (StringUtils.isNotBlank(cabstract)) {
        cabstract = XmlUtil.trimStartEndP(cabstract);
        cabstract = XmlUtil.trimThreateningHtml(cabstract);
        cabstract = cabstract.replaceAll("(&nbsp;)+", " ");
        if ("".equals(XmlUtil.trimAllHtml(cabstract))) {
          cabstract = "";
        }
      }
    }
    return cabstract;
  }

  @Deprecated
  private String getDisCode(SiePrjXmlDocument xmlDocument) {
    String disCode = null;
    if (xmlDocument == null) {
      disCode = "";
    } else {
      disCode = xmlDocument.getXmlNodeAttribute(SiePrjXmlConstants.PROJECT_XPATH, "discipline_code");
      disCode = XmlUtil.trimStartEndP(disCode);
      disCode = XmlUtil.trimThreateningHtml(disCode);
      disCode = disCode.replaceAll("(&nbsp;)+", " ");
      if ("".equals(XmlUtil.trimAllHtml(disCode))) {
        disCode = "";
      }
    }
    return disCode;
  }

  private String getDisCodeByDom(PrjDetailDOM detailDOM) {
    if (detailDOM != null) {
      if (StringUtils.isNotBlank(detailDOM.getDisciplineCode())) {
        return detailDOM.getDisciplineCode();
      }
    }
    return "";
  }

  private Object getDisCode(Long prjId) {
    List<SiePrjDiscipline> prjDisciplineList = prjDisciplineDao.getByPrjId(prjId);
    String disCodes = "";
    if (CollectionUtils.isNotEmpty(prjDisciplineList)) {
      for (int i = 0; i < prjDisciplineList.size(); i++) {
        if (i != prjDisciplineList.size() - 1) {
          disCodes += prjDisciplineList.get(i).getDisCode() + ";";
        } else {
          disCodes += prjDisciplineList.get(i).getDisCode();
        }
      }
    }
    return disCodes;
  }

  private String getInsName(SieProject prj) {
    String insName = "";
    Sie6InsPortal sieInsPortal = sieInsPortalDao.get(prj.getInsId());
    if (sieInsPortal != null) {
      insName = sieInsPortal.getZhTitle();
    }
    return insName;
  }

  private String getStartDate(SieProject prj) {
    StringBuilder sbl = new StringBuilder();
    if (prj != null && prj.getStartYear() != null && prj.getStartMonth() != null && prj.getStartDay() != null) {
      sbl.append(prj.getStartYear());
      sbl.append("/");
      sbl.append(prj.getStartMonth());
      sbl.append("/");
      sbl.append(prj.getStartDay());
    }
    return sbl.toString();
  }

  private String getEndDate(SieProject prj) {
    StringBuilder sbl = new StringBuilder();
    if (prj != null && prj.getEndYear() != null && prj.getEndMonth() != null && prj.getEndDay() != null) {
      sbl.append(prj.getEndYear());
      sbl.append("/");
      sbl.append(prj.getEndMonth());
      sbl.append("/");
      sbl.append(prj.getEndDay());
    }
    return sbl.toString();
  }

  /**
   * 获取项目的详情地址，现在只有我们系统的，还无法判断是基金委还是我们系统的。
   */
  private String getPrjAddress(Long prjId, Long insId) {
    StringBuilder sbl = new StringBuilder();
    Sie6InsPortal sie6InsPortal = sieInsPortalDao.get(insId);
    if (sie6InsPortal != null) {
      sbl.append("https://");
      sbl.append(sie6InsPortal.getDomain());
      sbl.append("/prjweb/project/view?des3Id=");
      sbl.append(ServiceUtil.encodeToDes3(prjId + ""));
    }
    return sbl.toString();
  }

}
