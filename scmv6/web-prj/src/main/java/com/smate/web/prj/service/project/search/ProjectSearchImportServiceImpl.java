package com.smate.web.prj.service.project.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.project.service.ProjectService;
import com.smate.core.base.project.vo.ProjectInfo;
import com.smate.core.base.psn.dao.EducationHistoryDao;
import com.smate.core.base.psn.dao.WorkAndEduHistoryDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.pub.util.AuthorNamesUtils;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.common.LocaleTextUtils;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.prj.consts.PrjXmlOperationEnum;
import com.smate.web.prj.dao.project.ScmPrjXmlDao;
import com.smate.web.prj.dao.project.SnsProjectQueryDao;
import com.smate.web.prj.dao.project.search.ConstRefDbDao;
import com.smate.web.prj.dao.project.search.InsRefDbDao;
import com.smate.web.prj.exception.PrjException;
import com.smate.web.prj.form.PrjImportForm;
import com.smate.web.prj.form.PrjXmlProcessContext;
import com.smate.web.prj.model.common.ScmPrjXml;
import com.smate.web.prj.model.search.ConstRefDb;
import com.smate.web.prj.model.search.InsRefDb;
import com.smate.web.prj.service.project.SnsPrjXmlService;
import com.smate.web.prj.util.ProjectHash;
import com.smate.web.prj.vo.ConstRefDbVO;
import com.smate.web.prj.xml.PrjXmlConstants;
import com.smate.web.prj.xml.PrjXmlDocument;
import com.smate.web.prj.xml.PrjXmlDocumentBuilder;
import com.smate.web.prj.xml.PrjXmlProcessor;

/**
 * .项目检索导入服务
 * 
 * @author wsn
 * 
 * @date Dec 14, 2018
 */
@Service("projectSearchImportService")
@Transactional(rollbackFor = Exception.class)
public class ProjectSearchImportServiceImpl implements ProjectSearchImportService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonDao personDao;
  @Autowired
  private ConstRefDbDao constRefDbDao;
  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private EducationHistoryDao educationHistoryDao;
  @Autowired
  private InsRefDbDao insRefDbDao;
  @Autowired
  private WorkAndEduHistoryDao workAndEduHistoryDao;
  @Autowired
  private SnsPrjXmlService snsPrjXmlService;
  private List<ImportPrjXmlDealService> xmlDealList;
  @Autowired
  private SnsProjectQueryDao snsProjectQueryDao;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private ProjectService projectService;
  @Autowired
  private ScmPrjXmlDao scmPrjXmlDao;

  @Override
  public void initSearchInfo(PrjImportForm form) throws ServiceException {
    // 1.获取人员姓名信息
    Person psn = personDao.getPersonName(form.getPsnId());
    form.setZhName(psn.getName());
    form.setFirstName(psn.getFirstName());
    form.setLastName(psn.getLastName());
    // 2.获取人员单位信息,优先查找首要单位，没有再找结束时间是至今的，没有再找结束时间和insId不为空的（按toYear+toMonth降序排）
    List<Long> insIdList = this.findPsnInsIdsByPsnId(form.getPsnId());
    // 3.获取可查找的文献库列表
    List<ConstRefDbVO> dbList = this.getSearchDBList(form.getDbType(), insIdList);
    form.setConstRefDBList(dbList);
    // 4.将可查找文献库列表信息，转成json格式的字符串
    form.setJsonDBInfo(this.buildDBInfoToJson(dbList));
  }


  @Override
  public List<WorkHistory> findPsnAllInsInfo(Long psnId) {
    List<WorkHistory> workHistories = new ArrayList<WorkHistory>();
    try {
      List<Map<String, Object>> mapList = workAndEduHistoryDao.findWorkAndEduHistoryByPsnId(psnId);
      for (Map<String, Object> map : mapList) {
        WorkHistory workHistory = new WorkHistory();
        workHistory.setInsName(map.get("INS_NAME") == null ? "" : map.get("INS_NAME").toString());
        workHistory.setToYear(map.get("TO_YEAR") == null ? null : Long.valueOf(map.get("TO_YEAR").toString()));
        workHistory.setFromYear(map.get("FROM_YEAR") == null ? null : Long.valueOf(map.get("FROM_YEAR").toString()));
        workHistory
            .setIsPrimary(map.get("IS_PRIMARY") == null ? 0L : Long.parseLong(map.get("IS_PRIMARY").toString().trim()));
        workHistory.setIsActive(map.get("IS_ACTIVE") == null ? null : Long.valueOf(map.get("IS_ACTIVE").toString()));
        workHistories.add(workHistory);
      }
      // 去掉工作经历和教育经历相同的机构
      // removeDuplicateWithOrder(workHistories);
    } catch (Exception e) {
      logger.error("获取人员所有的单位信息出错，psnId=" + psnId, e);
    }
    return workHistories;
  }

  protected List<ConstRefDbVO> getSearchDBList(String dbType, List<Long> insIdList) throws ServiceException {
    String allInsIds = ArrayUtils.toString(insIdList);
    logger.debug("====检索资源类型:{},人员所有单位:{}", dbType, allInsIds.substring(1, allInsIds.length() - 1));
    Locale locale = LocaleContextHolder.getLocale();
    // 通过文献库类型获取可查询的公用文献库列表
    List<ConstRefDbVO> result = this.getSearchDBListByDBType(locale, dbType);
    if (CollectionUtils.isNotEmpty(insIdList)) {
      // 通过insId查询单位可用文献库信息，来补充人员可查询文献库的信息
      result = this.getSearchDbListByInsIds(insIdList, result);
    }
    return result;
  }

  /**
   * 若人员单位ID为空，则通过文献库类型获取文献库列表
   * 
   * @param locale 当前语言环境 不能为{@code null}
   * @param dbType 文献库类型（成果、项目...）
   * @return
   */
  protected List<ConstRefDbVO> getSearchDBListByDBType(Locale locale, String dbType) {
    // 用dbType过滤，获取所有公用的文献库
    List<ConstRefDb> constList = constRefDbDao.getAllPublicRefDBByDBType(dbType, locale);
    logger.debug("====没有找到用户可用的单位insIdList，直接返回ConstRefDb表中的数据");
    return copyConstRefDbToVo(constList);
  }

  /**
   * 通过insId查询单位可用文献库信息，来补充人员可查询文献库的信息
   * 
   * @param constRefDBFromList：可查询文献库列表
   * @param insIdList：人员单位ID
   * @return
   */
  protected List<ConstRefDbVO> getSearchDbListByInsIds(List<Long> insIdList, List<ConstRefDbVO> constRefDBFromList) {
    // 查询单位可用文献库信息
    List<InsRefDb> insRefDbList = insRefDbDao.getDbByIns(insIdList);
    if (CollectionUtils.isNotEmpty(constRefDBFromList) && CollectionUtils.isNotEmpty(insRefDbList)) {
      int dbListLen = constRefDBFromList.size();
      int insDbListLen = insRefDbList.size();
      // 遍历所有根据文献库类型查询出来的公用文献库，和单位可用文献库信息，查找dbId匹配的数据，用来补充一些其他信息
      for (int i = 0; i < dbListLen; i++) {
        ConstRefDbVO from = constRefDBFromList.get(i);
        for (int j = 0; j < insDbListLen; j++) {
          InsRefDb insRefDb = insRefDbList.get(j);
          if (from.getId().equals(insRefDb.getInsRefDbId().getDbId())) {
            from.setLoginUrl(insRefDb.getLoginUrl() == null ? "" : insRefDb.getLoginUrl());// 用于校外登录的url
            if (StringUtils.isNotBlank(insRefDb.getActionUrl())) {
              from.setActionUrl(insRefDb.getActionUrl());// 用于校外查询的url
            }
            if (StringUtils.isNotBlank(insRefDb.getActionUrlInside())) {
              from.setActionUrlInside(insRefDb.getActionUrlInside());// 用于校内查询的url
            }
            if (StringUtils.isNotBlank(insRefDb.getLoginUrlInside())) {
              from.setLoginUrlInside(insRefDb.getLoginUrlInside());// 用户进行登录验证的URL
            }
            // TODO 校外全文url的域名
            if (StringUtils.isNotBlank(insRefDb.getFulltextUrlInside())) {
              from.setFulltextUrlInside(insRefDb.getFulltextUrlInside());// 校内全文url的域名
            }
            if (insRefDb.getEnabled() != null) {
              from.setIsPublic(insRefDb.getEnabled());// 是否公用
            }
            logger.debug("====根据用户单位配制的url对ConstRefDb进行重设,dbcode:{}", from.getCode());
            break;
          }
        }
      }
    }
    return constRefDBFromList;
  }

  /**
   * 将查询出的数据放入VO对象中
   * 
   * @param POList：查询出的PO对象list
   * @return
   */
  protected List<ConstRefDbVO> copyConstRefDbToVo(List<ConstRefDb> POList) {
    if (CollectionUtils.isEmpty(POList)) {
      return null;
    }
    List<ConstRefDbVO> constRefDBFromList = new ArrayList<ConstRefDbVO>();
    for (ConstRefDb constRefDb : POList) {
      ConstRefDbVO vo = new ConstRefDbVO();
      vo.setId(constRefDb.getId());
      vo.setActionUrl(constRefDb.getActionUrl() == null ? "" : constRefDb.getActionUrl());
      vo.setActionUrlInside(constRefDb.getActionUrlInside() == null ? "" : constRefDb.getActionUrlInside());
      vo.setCode(constRefDb.getCode());
      vo.setDbBitCode(constRefDb.getDbBitCode());
      vo.setEnSortKey(constRefDb.getEnSortKey());
      vo.setIsPublic(constRefDb.getIsPublic());
      vo.setDbType(constRefDb.getDbType() == null ? "" : constRefDb.getDbType());
      vo.setEnAbbrName(constRefDb.getEnAbbrName() == null ? "" : constRefDb.getEnAbbrName());
      vo.setEnUsName(constRefDb.getEnUsName() == null ? "" : constRefDb.getEnUsName());
      vo.setFulltextUrlInside(constRefDb.getFulltextUrlInside() == null ? "" : constRefDb.getFulltextUrlInside());
      vo.setLoginUrl(constRefDb.getLoginUrl() == null ? "" : constRefDb.getLoginUrl());
      vo.setLoginUrlInside(constRefDb.getLoginUrlInside() == null ? "" : constRefDb.getLoginUrlInside());
      vo.setSuportLang(constRefDb.getSuportLang() == null ? "" : constRefDb.getSuportLang());
      vo.setZhAbbrName(constRefDb.getZhAbbrName() == null ? "" : constRefDb.getZhAbbrName());
      vo.setZhCnName(constRefDb.getZhCnName() == null ? "" : constRefDb.getZhCnName());
      vo.setZhSortKey(constRefDb.getZhSortKey());
      vo.setBatchQuery(constRefDb.getBatchQuery() == null ? "" : constRefDb.getBatchQuery());
      constRefDBFromList.add(vo);
    }
    return constRefDBFromList;
  }

  /**
   * 将文献库列表信息构建成json格式字符串.
   */
  protected String buildDBInfoToJson(List<ConstRefDbVO> dbList) {
    Locale local = LocaleContextHolder.getLocale();
    if (CollectionUtils.isNotEmpty(dbList)) {
      for (ConstRefDbVO constRefDb : dbList) {
        if (local.equals(Locale.US)) {
          constRefDb.setNameDetails(constRefDb.getEnUsName());
          constRefDb.setDbName(constRefDb.getEnAbbrName());
        } else {
          constRefDb.setDbName(constRefDb.getZhAbbrName());
          constRefDb.setNameDetails(constRefDb.getZhCnName());
        }
      }
    }
    StringBuilder sb = new StringBuilder("{\"default\":\"\"");
    if (dbList != null && dbList.size() > 0) {
      int dbListLen = dbList.size();
      for (int i = 0; i < dbListLen; i++) {
        sb.append(",\"" + dbList.get(i).getCode() + "\":");
        sb.append(JacksonUtils.jsonObjectSerializer(dbList.get(i)).toString());
      }
    }
    sb.append("}");
    return sb.toString();
  }

  /**
   * .查找人员单位ID
   * 
   * @return
   */
  protected List<Long> findPsnInsIdsByPsnId(Long userPsnId) throws ServiceException {
    try {
      List<Long> result = null;
      result = workHistoryDao.findWorkByPrimary(userPsnId);// 如果work是首要
      if (CollectionUtils.isEmpty(result)) {
        result = educationHistoryDao.findPrimaryEdu(userPsnId);// 如果edu是首要
      }
      if (CollectionUtils.isEmpty(result)) {
        result = workHistoryDao.findWorkByPsnId(userPsnId);// 如果是当前
      }
      // 按最近日期
      if (CollectionUtils.isEmpty(result)) {
        result = new ArrayList<Long>();
        Long workInsId = workHistoryDao.getWorkInsIdByLastDate(userPsnId);
        if (workInsId != null && workInsId > 0) {
          result.add(workInsId);
        } else {
          Long eduInsId = educationHistoryDao.getEduInsIdByLastDate(userPsnId);
          if (eduInsId != null && eduInsId > 0) {
            result.add(eduInsId);
          }
        }
      }
      return result;
    } catch (Exception e) {
      logger.error("获取指定人员的所有工作单位ID出错", e);
      throw new ServiceException(e);
    }
  }


  @Override
  public void buildPendingImportPrjByXml(PrjImportForm form) throws ServiceException {
    try {
      Long psnId = form.getPsnId();
      Document doc = null;
      if (StringUtils.isNotBlank(form.getInputXml())) {
        Locale locale = LocaleContextHolder.getLocale();
        doc = DocumentHelper.parseText(form.getInputXml());
        List nodes = doc.selectNodes("/scholarWorks/data");
        // TODO 将里面重复isi类型的记录过滤
        Node rootNode = doc.selectSingleNode("/scholarWorks");
        Integer recCount = nodes.size();
        List<PrjXmlDocument> xmlDocs = new ArrayList<PrjXmlDocument>();
        for (int index = 0; index < recCount; index++) {
          Node pitem = (Node) nodes.get(index);
          Node item = pitem.selectSingleNode("publication");
          Element ele = (Element) item;
          // title为空的不做处理，TODO 这种数据页面要提示
          String ctitle = ele.attributeValue("ctitle");
          String etitle = ele.attributeValue("etitle");
          if (StringUtils.isNotBlank(ctitle) || StringUtils.isNotBlank(etitle)) {
            // 格式化标题、作者名、期刊名大小写，只有全部是大写时才格式化, 该逻辑应该不要了，参考成果的SCM-20168
            // 如果资金为0则清空
            // 根据成果中的作者是否pf匹配当前用户，给导入列表着色
            // ele.addAttribute("author_match", String.valueOf(isMatch(item.valueOf("@author_names"), psnId)));
            // 查重
            Long dupPrjId = snsProjectQueryDao.getDupPrjId(ProjectHash.cleanTitleHash(ctitle),
                ProjectHash.cleanTitleHash(etitle), psnId);
            ele.addAttribute("dup_value", Objects.toString(dupPrjId, ""));
            // 重新构建项目xml
            PrjXmlDocument xmlDocument = this.rebuildImportXml(pitem, psnId);
            // 构建待导入列表显示信息
            this.buildPrjInfo(xmlDocument, form, locale);
            xmlDocs.add(xmlDocument);
          }
        }
        // 缓存待导入项目xml
        if (CollectionUtils.isNotEmpty(xmlDocs)) {
          String key = getTimeStamp() + psnId;
          cacheService.put("searchImportPrjCache", 60 * 60, key, (Serializable) xmlDocs);
          form.setCacheKey(key);
        }
      }
    } catch (Exception e) {
      throw new ServiceException(e);
    }
  }


  /**
   * .将项目作者和当前登录人员匹配
   * 
   * @param authorNames
   * @param psnId
   * @return
   */
  private Integer isMatch(String authorNames, Long psnId) throws ServiceException {
    try {
      Integer isMatch = 0;
      if (StringUtils.isNotBlank(authorNames) && !NumberUtils.isNullOrZero(psnId)) {
        Person person = personDao.get(psnId);
        List<String> nameArray = AuthorNamesUtils.parsePubAuthorNames(authorNames);
        if (nameArray != null && person != null && nameArray.size() > 0) {
          for (String psnName : nameArray) {
            psnName = AuthorNamesUtils.cleanAuthorName(psnName);
            if (matchAuthor(psnName, person)) {
              isMatch = 1;
            }
          }
        }
      }
      return isMatch;
    } catch (Exception e) {
      logger.error("拆分authorName匹配当前人出错！authorName={},psnId={}", authorNames, psnId, e);
      throw new ServiceException(e);
    }
  }

  /**
   * .进行用户名的匹配
   * 
   * @param psnName
   * @param person
   * @return
   */
  private boolean matchAuthor(String psnName, Person person) {
    psnName = clearAuthorName(psnName);
    String name = clearAuthorName(person.getName());
    String lastName = clearAuthorName(person.getLastName());
    String fristName = clearAuthorName(person.getFirstName());
    if (psnName.equalsIgnoreCase(name) || psnName.equalsIgnoreCase(lastName + fristName)
        || psnName.equalsIgnoreCase(fristName + lastName)
        || psnName.equalsIgnoreCase(lastName + StringUtils.substring(fristName, 0, 1))) {
      return true;
    }
    return false;
  }

  /**
   * .先处理作者信息，去除所有空格，转为小写
   * 
   * @param authorName
   * @return
   */
  private String clearAuthorName(String authorName) {
    if (StringUtils.isBlank(authorName)) {
      return "";
    }
    authorName = authorName.replace("&amp;", "&");
    authorName = HtmlUtils.htmlUnescape(authorName);
    // 清除html标记
    authorName = XmlUtil.trimAllHtml(authorName);
    // 清除*
    authorName = authorName.replace("*", "");
    // 过滤作者中的特殊符号进行匹配
    authorName = XmlUtil.filterAuForCompare(authorName);
    authorName = authorName.replace(" ", "");
    authorName = StringUtils.trimToEmpty(authorName);
    authorName = authorName.toLowerCase();
    return authorName;
  }


  /**
   * .重新构建项目xml信息.
   * 
   * @param item
   */
  private PrjXmlDocument rebuildImportXml(Node pitem, Long psnId) throws Exception {
    PrjXmlProcessContext xmlContext = snsPrjXmlService.buildXmlProcessContext(psnId);
    xmlContext.setCurrentAction(PrjXmlOperationEnum.Import);
    PrjXmlDocument xmlDocument = new PrjXmlDocument(pitem.asXML());
    for (ImportPrjXmlDealService service : xmlDealList) {
      service.checkParameter(xmlContext);
      service.dealWithXml(xmlDocument, xmlContext);
    }
    new PrjXmlProcessor(xmlDocument).generateBrief();
    return xmlDocument;
  }

  /**
   * 构建页面待导入列表显示信息
   * 
   * @param xmlDocument
   * @param form
   * @param locale
   */
  private void buildPrjInfo(PrjXmlDocument xmlDocument, PrjImportForm form, Locale locale) {
    List<ProjectInfo> prjList = form.getPrjInfoList();
    if (prjList == null) {
      prjList = new ArrayList<ProjectInfo>();
    }
    ProjectInfo info = new ProjectInfo();
    info.setShowTitle(LocaleTextUtils.getLocaleText(locale,
        xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_title"),
        xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_title")));
    String authorNames = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "author_names");
    info.setShowAuthorNames(authorNames);
    info.setShowBriefDesc(LocaleTextUtils.getLocaleText(locale,
        xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "brief_desc"),
        xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "brief_desc_en")));
    info.setDes3DupPrjId(xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "dup_value"));
    /*
     * String match = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "author_match"); if
     * (NumberUtils.isCreatable(match)) { info.setAuthorMatch(NumberUtils.parseInt(match)); }
     */
    info.setTempSourceUrl(xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PUBLICATION_XPATH, "tmpsource_url"));
    info.setSourceDbCode(xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PUBLICATION_XPATH, "source_db_code"));
    prjList.add(info);
    form.setPrjInfoList(prjList);
  }


  public List<ImportPrjXmlDealService> getXmlDealList() {
    return xmlDealList;
  }


  public void setXmlDealList(List<ImportPrjXmlDealService> xmlDealList) {
    this.xmlDealList = xmlDealList;
  }


  @SuppressWarnings("unchecked")
  @Override
  public void savePendingImportPrjs(PrjImportForm form) throws ServiceException {
    // 导入成功的项目数量
    int importSuccessNum = 0;
    try {
      List<PrjXmlDocument> xmlDocs =
          (List<PrjXmlDocument>) cacheService.get("searchImportPrjCache", form.getCacheKey());
      if (xmlDocs != null && CollectionUtils.isNotEmpty(form.getPrjJsonList())) {
        // 遍历处理项目XML
        for (int i = 0; i < xmlDocs.size(); i++) {
          PrjXmlDocument doc = xmlDocs.get(i);
          String xml = doc.getXmlString();
          if (doc != null) {
            // 获取成果保存操作所需参数
            Map<String, Object> prjParams = JacksonUtils.jsonToMap(form.getPrjJsonList().get(i).toString());
            // 开始处理
            importSuccessNum = this.dealWithImportPrjXml(doc, prjParams, form.getPsnId(), importSuccessNum);
          }
        }
        // 生成导入项目动态
      }
    } catch (Exception e) {
      throw new ServiceException(e);
    }
    form.setImportSuccessNum(importSuccessNum);
    // 删除缓存的数据
    cacheService.remove("searchImportPrjCache", form.getCacheKey());
  }


  // 获取时间戳
  private String getTimeStamp() {
    long time = System.currentTimeMillis();
    String timeStamp = String.valueOf(time / 1000);
    return timeStamp;
  }

  /**
   * 构建PrjXmlProcessContext.
   * 
   * @param prjParams
   * @return prjId
   */
  protected PrjXmlProcessContext dealWithImportPrjXmlProcessContext(Long psnId, Map<String, Object> prjParams)
      throws PrjException {
    // 默认新增一个项目ID
    Long prjId = projectService.createPrjId();
    String dupPrjOpt = "no_dup";
    // 项目是否重复，重复的项目选取的操作是什么（跳过、新增、更新）, 1:重复了，0：没重复，默认没重复
    String dupPrjId = Des3Utils.decodeFromDes3(Objects.toString(prjParams.get("dup_des3_prj_id"), "").trim());
    if (StringUtils.isNotBlank(dupPrjId)) {
      // 获取重复项目选择的操作
      dupPrjOpt = Objects.toString(prjParams.get("dup_opt"), "refresh");
    }
    // 查重后更新
    if ("refresh".equals(dupPrjOpt)) {
      prjId = NumberUtils.isCreatable(dupPrjId) ? NumberUtils.parseLong(dupPrjId) : prjId;
      // 查重后跳过
    } else if ("skip".equals(dupPrjOpt)) {
      prjId = 0L;
    }
    // 构建PrjXmlProcessContext
    PrjXmlProcessContext context = snsPrjXmlService.buildXmlProcessContext(psnId);
    context.setCurrentAction(PrjXmlOperationEnum.Import);
    context.setCurrentPrjId(prjId);
    context.setDupOperation(dupPrjOpt);
    return context;
  }

  /**
   * 处理导入的项目xml及项目相关表数据
   * 
   * @param doc
   * @param prjParams
   * @param psnId
   * @param importSuccessNum
   * @return
   */
  protected Integer dealWithImportPrjXml(PrjXmlDocument doc, Map<String, Object> prjParams, Long psnId,
      int importSuccessNum) throws Exception {
    // 构建PrjXmlProcessContext.
    PrjXmlProcessContext context = this.dealWithImportPrjXmlProcessContext(psnId, prjParams);
    Long prjId = context.getCurrentPrjId();
    // 导入项目查到重后，选择更新操作要先合并xml
    if ("refresh".equals(context.getDupOperation())) {
      ScmPrjXml prjXml = scmPrjXmlDao.get(prjId);
      if (prjXml != null) {
        PrjXmlDocumentBuilder.mergeWhenImport(doc, prjXml.getPrjXml());
      }
    }
    // 处理XML TODO 通过pub_authors/author节点重构prj_members节点(待确认)
    if (NumberUtils.isNotNullOrZero(prjId)) {
      PrjXmlProcessor processor = new PrjXmlProcessor(doc);
      snsPrjXmlService.dealWithXmlByProcessor(context, doc, processor);
      importSuccessNum++;
    }
    return importSuccessNum;
  }
}
