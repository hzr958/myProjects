package com.smate.web.v8pub.service.searchimport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import com.alibaba.fastjson.JSONArray;
import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.consts.dao.InstitutionDao;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.privacy.dao.PrivacySettingsDao;
import com.smate.core.base.psn.consts.PsnPrivacySettingConst;
import com.smate.core.base.psn.dao.EducationHistoryDao;
import com.smate.core.base.psn.dao.WorkAndEduHistoryDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.enums.PubLibraryEnum;
import com.smate.core.base.pub.enums.PubSnsRecordFromEnum;
import com.smate.core.base.pub.util.PdwhPubXMLToJsonStrUtils;
import com.smate.core.base.pub.util.PubDetailVoUtil;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.pub.vo.Accessory;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.cache.SnsCacheService;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.common.LocaleTextUtils;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.PsnPrivateDao;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.IrisStringUtils;
import com.smate.core.base.utils.xss.XssUtils;
import com.smate.web.v8pub.consts.PubImportConstants;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dao.pdwh.DbCachePfetchDao;
import com.smate.web.v8pub.dao.pdwh.PubPdwhDetailDAO;
import com.smate.web.v8pub.dao.searchimport.InsAliasDao;
import com.smate.web.v8pub.dao.sns.ConstRefDbDao;
import com.smate.web.v8pub.dao.sns.FriendDao;
import com.smate.web.v8pub.dao.sns.InsRefDbDao;
import com.smate.web.v8pub.dao.sns.PubKnowDao;
import com.smate.web.v8pub.dao.sns.PubSituationDAO;
import com.smate.web.v8pub.dao.sns.group.GroupMemberDao;
import com.smate.web.v8pub.dao.sns.psn.FriendTempDao;
import com.smate.web.v8pub.dao.sns.psn.FriendTempSysDao;
import com.smate.web.v8pub.dao.sns.pubtype.ConstPubTypeDao;
import com.smate.web.v8pub.dom.AwardsInfoBean;
import com.smate.web.v8pub.dom.BookInfoBean;
import com.smate.web.v8pub.dom.ConferencePaperBean;
import com.smate.web.v8pub.dom.JournalInfoBean;
import com.smate.web.v8pub.dom.OtherInfoBean;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.PubSituationBean;
import com.smate.web.v8pub.dom.PubTypeInfoBean;
import com.smate.web.v8pub.dom.SoftwareCopyrightBean;
import com.smate.web.v8pub.dom.StandardInfoBean;
import com.smate.web.v8pub.dom.ThesisInfoBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.dto.JournalInfoDTO;
import com.smate.web.v8pub.dto.MemberInsDTO;
import com.smate.web.v8pub.dto.PatentInfoDTO;
import com.smate.web.v8pub.dto.PubFulltextDTO;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.dto.PubTypeInfoDTO;
import com.smate.web.v8pub.dto.SoftwareCopyrightDTO;
import com.smate.web.v8pub.dto.StandardInfoDTO;
import com.smate.web.v8pub.enums.PubGenreConstants;
import com.smate.web.v8pub.enums.PubHandlerEnum;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.DbCachePfetch;
import com.smate.web.v8pub.po.pubType.ConstPubType;
import com.smate.web.v8pub.po.searchimport.InsAlias;
import com.smate.web.v8pub.po.sns.PubSituationPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.match.PubAuthorMatchService;
import com.smate.web.v8pub.service.pdwh.PubPdwhDetailService;
import com.smate.web.v8pub.service.pdwh.indexurl.PdwhPubIndexUrlService;
import com.smate.web.v8pub.service.pubdetailquery.PubDetailHandleService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.utils.PubFileParamsUtils;
import com.smate.web.v8pub.utils.PubLocaleUtils;
import com.smate.web.v8pub.utils.RestTemplateUtils;
import com.smate.web.v8pub.vo.PendingImportPubVO;
import com.smate.web.v8pub.vo.psn.PsnInfo;
import com.smate.web.v8pub.vo.searchimport.ConstRefDbVO;
import com.smate.web.v8pub.vo.searchimport.PubImportVO;
import com.smate.web.v8pub.vo.sns.ConstRefDb;
import com.smate.web.v8pub.vo.sns.InsRefDb;

/**
 * 检索导入成果服务实现
 * 
 * @author wsn
 * @date 2018年8月9日
 */
@Service("searchImportService")
@Transactional(rollbackFor = Exception.class)
public class PubImportServiceImpl implements PubImportService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainScm;
  @Value("${pub.saveorupdate.url}")
  private String SAVEORUPDATE;
  @Autowired
  private ConstRefDbDao constRefDbDao;
  @Autowired
  private InsRefDbDao insRefDbDao;
  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private EducationHistoryDao educationHistoryDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private PubImportInfoDriverFactory pubImportInfoDriverFactory;
  @Autowired
  private WorkAndEduHistoryDao workAndEduHistoryDao;
  @Autowired
  private PubDetailHandleService pubDetailHandleService;
  @Autowired
  private GroupMemberDao groupMemberDao;
  @Autowired
  private PubSituationDAO pubSituationDao;
  @Autowired
  private SnsCacheService cacheService;
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private FriendTempSysDao friendTempSysDao;
  @Autowired
  private FriendTempDao friendTempDao;
  @Autowired
  private PubKnowDao pubKnowDao;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private InsAliasDao insAliasDao;
  @Autowired
  private DbCachePfetchDao dbCachePfetchDao;
  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDAO;
  @Autowired
  private PdwhPubIndexUrlService pdwhPubIndexUrlService;
  @Autowired
  private ConstPubTypeDao constPubTypeDao;
  @Autowired
  private PubAuthorMatchService pubAuthorMatchService;
  @Autowired
  private PsnPrivateDao psnPrivateDao;
  @Autowired
  private PrivacySettingsDao privacySettingsDao;
  @Autowired
  private PubSnsService pubSnsService;

  /**
   * 初始化进入联邦检索页面所需信息
   */
  @Override
  public void initSearchInfo(PubImportVO importVo) throws ServiceException {
    // 1.获取人员姓名信息
    Person psn = personDao.getPersonName(importVo.getPsnId());
    importVo.setZhName(psn.getName());
    importVo.setFirstName(psn.getFirstName());
    importVo.setLastName(psn.getLastName());
    // 2.获取人员单位信息,优先查找首要单位，没有再找结束时间是至今的，没有再找结束时间和insId不为空的（按toYear+toMonth降序排）
    List<Long> insIdList = this.findPsnInsIdsByPsnId(importVo.getPsnId());
    // 3.获取可查找的文献库列表
    List<ConstRefDbVO> dbList = this.getSearchDBList(importVo.getDbType(), insIdList);
    importVo.setConstRefDBList(dbList);
    // 4.将可查找文献库列表信息，转成json格式的字符串
    importVo.setJsonDBInfo(this.buildDBInfoToJson(dbList));
  }

  @Override
  public List<ConstRefDbVO> getSearchDBList(String dbType, List<Long> insIdList) throws ServiceException {
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
   * @param locale：当前语言环境
   * @param dbType：文献库类型（成果、项目...）
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
    StringBuilder sb = new StringBuilder("{'default':''");
    if (dbList != null && dbList.size() > 0) {
      int dbListLen = dbList.size();
      for (int i = 0; i < dbListLen; i++) {
        sb.append(",'" + dbList.get(i).getCode() + "':");
        sb.append(JacksonUtils.jsonObjectSerializer(dbList.get(i)).toString());
      }
    }
    sb.append("}");
    return sb.toString();
  }

  /**
   * 查找人员单位ID
   * 
   * @return
   * @throws ServiceException
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
      if (CollectionUtils.isEmpty(result)) {// 按最近日期
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

  @SuppressWarnings("rawtypes")
  public void buildPendingImportPubByXml(PubImportVO importVo) throws ServiceException {
    try {
      String inputXml = importVo.getInputXml();
      List<PendingImportPubVO> pubVOs = new ArrayList<PendingImportPubVO>();
      if (StringUtils.isNotBlank(inputXml)) {
        // 针对万方期刊中的&符号 和"·"没有转换
        inputXml = inputXml.replace("&amp;amp;amp;", "&amp;").replaceAll("&#183;", "·");
        // 处理&：如果&后面跟随的不是如下6个字符串（&lt; &gt; &amp; &apos;
        // &quot;），则将&变为&amp;
        inputXml = IrisStringUtils.filterSupplementaryCharsChina(inputXml);
        // 针对isi库的记录进行查重过滤，如果source_id相同，则认为两条记录是相同，删除其中一条（移到后面步骤做）
        Document doc = DocumentHelper.parseText(inputXml);
        deleteDupNode(doc);
        // 检索导入时存一份XML到pdwh库
        this.cachePubXmlToPdwh(importVo.getPsnId(), null, inputXml, 1);
        List nodes = doc.selectNodes("/scholarWorks/data");
        for (Object node : nodes) {
          Node pubNode = (Node) node;
          // 针对万方的时间处理
          rebuildWanFangDate(pubNode);
          // 将xml转成json
          Map<String, Object> pubMap = PdwhPubXMLToJsonStrUtils.dealWithXMLToMap(pubNode.asXML(),
              LocaleContextHolder.getLocale().equals(Locale.CHINA));
          // 重新构造srcDbId
          buildSrcDbId(pubMap);
          // 重新构造countryId
          buildCountryId(pubMap);
          String pubJson = JacksonUtils.mapToJsonStr(pubMap);
          // 先对json数据进行多个空格的替换
          pubJson = PubFileParamsUtils.replaceMultipleSpace(pubJson);
          // xss 过滤以及内容转义
          pubJson = XssUtils.transferJson(pubJson);
          // 将json转成PendingImportPubVO对象
          int pubType = NumberUtils.toInt(Objects.toString(pubMap.get("pubType"), "7"));
          PendingImportPubVO VO = pubImportInfoDriverFactory.getDriver(pubType).buildPendingImportPubVoByJson(pubJson);
          if (VO != null) {
            VO.setPubXml(pubNode.asXML());
            VO.setJsonPub(pubJson);
            // 从xml中取一些额外的信息
            rebuildImportPubInfo((Element) pubNode, VO);
            pubVOs.add(VO);
          }
        }
      }
      importVo.setPendingImportPubs(pubVOs);
    } catch (Exception e) {
      logger.error("将成果xml转成PendingImportPubVO对象出错", e);
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public void initImportPubInfo(PubImportVO importVo) throws ServiceException {
    try {
      Long psnId = importVo.getPsnId();
      boolean isNotGroupPub = importVo.getGrpId() == null || CommonUtils.compareLongValue(0L, importVo.getGrpId());
      List<PendingImportPubVO> pubVos = importVo.getPendingImportPubs();
      Integer errorCount = 0; // 数据有问题的成果数量
      String sourceDBs = ""; // 有问题的成果所在的文献库名称
      Map<String, Integer> errMap = new HashMap<String, Integer>(); // 存放错误信息的Map
      Map<Integer, PendingImportPubVO> cacheMap = new HashMap<Integer, PendingImportPubVO>();
      int i = 0;
      // 遍历成果信息进行进一步处理
      List<PendingImportPubVO> dupPubVos = new ArrayList<PendingImportPubVO>();
      for (PendingImportPubVO vo : pubVos) {
        // title为空的数据不做处理，但要在页面提示
        if (this.checkErrorData(vo, sourceDBs, errorCount, errMap) == 1) {
          errorCount++;
          continue;
        }
        /**
         * SCM-20168 去除对格式化标题、作者名、期刊名大小写的逻辑
         * 之前逻辑：格式化标题、作者名、期刊名大小写，只有全部是大写时才格式化,重构AuthorNames分割符为分号（现在应该都是英文分号加空格的了）
         * this.rebuildTitleAuthJnlName(vo);
         */
        // 文件导入时，从PDWH_PUB_SOURCE_DB表查询数据，有对应成果引用情况数据的话更新导入的xml中引用情况数据
        matchPubAthor(vo, psnId);
        // 处理成果描述字段
        this.rebuildPubBriefDesc(vo);
        // 导入到群组的成果不查重
        if (isNotGroupPub) {
          // 对成果进行简单查重
          Long dupPubId = this.checkDupPub(vo, importVo.getDes3PsnId());
          // 利用查重后的成果收录信息初始化待导入成果的收录信息, 或用待导入成果的收录信息更新查重的成果的收录情况
          this.rebuildPubSituation(vo, dupPubId);
          dupPubVos.add(vo);
        } else {
          // 群组成果不查重，但是可以将有的收录情况进行补充显示
          this.rebuildPubSituation(vo, null);
        }
        cacheMap.put(i, vo);
        i++;
        vo.setIndex(i);
      }
      if (dupPubVos.size() > 0) {
        pubVos.removeAll(dupPubVos);
        dupPubVos.addAll(pubVos);
        importVo.setPendingImportPubs(dupPubVos);
      }
      // i值表示为导入成果的数量
      if (errorCount < i) {
        String key = getTimeStamp() + psnId;
        cacheService.put("searchImportPubCache", 60 * 60, key, (Serializable) cacheMap);
        importVo.setCacheKey(key);
        importVo.setValidPub(true);
      } else {
        importVo.setValidPub(false);
      }
    } catch (Exception e) {
      logger.error("重构待导入成果信息出错", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 格式化标题、作者名、期刊名大小写，只有全部是大写时才格式化
   * 
   * @param pubvo
   */
  @SuppressWarnings("rawtypes")
  protected void rebuildTitleAuthJnlName(PendingImportPubVO pubvo) {
    String title = XmlUtil.formatTitle(Objects.toString(pubvo.getTitle(), ""));
    String original = XmlUtil.formatJnlTitle(Objects.toString(pubvo.getOriginal(), ""));
    String authorNames = XmlUtil.formatAuthors(Objects.toString(pubvo.getAuthorNames(), ""));
    String authorNamesSpec = XmlUtil.formatAuthors(Objects.toString(pubvo.getAuthorsNamesSpec(), ""));
    authorNames = StringUtils.isBlank(authorNames) ? authorNamesSpec : authorNames;
    authorNames = HtmlUtils.htmlUnescape(authorNames);
    pubvo.setTitle(title);
    pubvo.setAuthorNames(authorNames);
    pubvo.setOriginal(original);
  }

  /**
   * 重新构建成果描述字段
   * 
   * @param pubvo
   */
  @SuppressWarnings("rawtypes")
  protected void rebuildPubBriefDesc(PendingImportPubVO pubvo) {
    try {
      int pubType = pubvo.getPubType();
      String briefDesc = pubImportInfoDriverFactory.getDriver(pubType)
          .getBriefDescData(PubLocaleUtils.getLocale(pubvo.getTitle()), pubvo);
      pubvo.setBriefDesc(briefDesc);
    } catch (Exception e) {
      logger.error("构建成果描述briefDesc字段出错", e);
    }
  }

  // 针对万方日期处理
  protected void rebuildWanFangDate(Node item) {
    Element ele = (Element) item.selectSingleNode("publication");
    String startDate = ele.attributeValue("start_date");
    if (StringUtils.isNotBlank(startDate)) {
      startDate = startDate.replace("年", "-").replace("月", "-").replace("日", "").trim();
      ele.addAttribute("start_date", startDate);
    }
    String endDate = ele.attributeValue("end_date");
    if (StringUtils.isNotBlank(endDate)) {
      endDate = endDate.replace("年", "-").replace("月", "-").replace("日", "").trim();
      ele.addAttribute("end_date", endDate);
    }
  }

  // 将当前人员姓名和待导入成果作者进行匹配
  protected void matchPubAthor(PendingImportPubVO<PubTypeInfoDTO> pubvo, Long psnId) {
    Integer matchFlag = pubAuthorMatchService.isMatch(pubvo.getAuthorNames(), psnId);
    pubvo.setAuthorMatch(matchFlag);
  }

  /**
   * 从xml中获取一些其他信息填充到pubvo中
   * 
   * @param item
   * @param pubvo
   */
  @SuppressWarnings("rawtypes")
  protected void rebuildImportPubInfo(Element item, PendingImportPubVO pubvo) {
    try {
      Element ele = (Element) item.selectSingleNode("publication");
      // sourceDBCode
      String sourceDbCode = ele.attributeValue("source_db_code");
      pubvo.setSourceDbCode(sourceDbCode);
      // 作者名称
      String authorsNamesSpec = ele.attributeValue("authors_names_spec");
      pubvo.setAuthorsNamesSpec(authorsNamesSpec);
      // 成果年份
      String pubYear = ele.attributeValue("pubyear");
      if (StringUtils.isBlank(pubvo.getPubYear()) && StringUtils.isNotBlank(pubYear)) {
        if (pubYear.indexOf("-") > -1) {
          pubYear = pubYear.substring(0, pubYear.indexOf("-"));
        }
        // scienceDirect年份处理
        if (pubYear.indexOf("/") > -1) {
          pubYear = pubYear.substring(0, pubYear.indexOf("/"));
        }
        if (pubYear.length() > 4) {
          pubYear = pubYear.substring(0, 4);
        }
        pubvo.setPubYear(pubYear);
      }
      // tmpsource_url
      String tmpSourceUrl = ele.attributeValue("tmpsource_url");
      pubvo.setTmpSourceUrl(tmpSourceUrl);
      // sourceUrl
      String sourceUrl = ele.attributeValue("source_url");
      pubvo.setSourceUrl(sourceUrl);
    } catch (Exception e) {
      logger.error("从xml中获取额外信息出错", e);
    }
  }
  /* 在显示成果列表时，在xml中针对isi库的记录进行查重过滤，如果source_id相同，则认为两条记录是相同，删除其中一条 */

  @SuppressWarnings("rawtypes")
  public void deleteDupNode(Document doc) {
    List nodes = doc.selectNodes("/scholarWorks/data");
    Integer recCount = nodes.size();
    HashMap<Integer, String> hm1 = new HashMap<Integer, String>();
    // 先将所有isi的source_id取出来放入map以方便后比较
    for (int i = 0; i < recCount; i++) {
      Node pitem = (Node) nodes.get(i);
      Node item = pitem.selectSingleNode("publication");
      String source_id = item.valueOf("@source_id");
      source_id = source_id.trim();
      hm1.put(i, source_id);
    }
    Element root = doc.getRootElement();
    int hm1Size = hm1.size();
    for (int j = 0; j < hm1Size - 1; j++) {
      String source_id = hm1.get(j);
      if ("".equals(source_id)) {
        continue;
      }
      for (int k = j + 1; k < hm1Size; k++) {
        if (source_id.equals(hm1.get(k))) {
          Element remainEle = (Element) nodes.get(j);
          Element deleteEle = (Element) nodes.get(k);
          if (root.remove(deleteEle)) {
            Element remainEle_C = (Element) remainEle.selectSingleNode("publication");
            Element deleteEle_C = (Element) deleteEle.selectSingleNode("publication");
            remainEle_C.addAttribute("citation_index",
                remainEle_C.attributeValue("citation_index") + "," + deleteEle_C.attributeValue("citation_index"));
          }
        }
      }
    }
  }

  /**
   * 是否有title为空的数据，有的话记录下，页面上提示用
   * 
   * @param pubvo
   * @param sourceDBs
   * @param errorCount
   * @param errMap
   * @return 0：title不为空，1：title为空
   */
  @SuppressWarnings("rawtypes")
  protected int checkErrorData(PendingImportPubVO pubvo, String sourceDBs, Integer errorCount,
      Map<String, Integer> errMap) {
    int result = 0;
    if (StringUtils.isBlank(pubvo.getTitle())) {
      String dbCode = Objects.toString(pubvo.getSourceDbCode());
      if (sourceDBs.indexOf(dbCode) < 0) {
        // sourceDBs += dbCode + ",";
        errorCount = 0;
      }
      errorCount++;
      errMap.put(dbCode, errorCount);
      result = 1;
    }
    return result;
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

  @SuppressWarnings({"rawtypes", "unchecked", "unused"})
  private void removeDuplicateWithOrder(List<WorkHistory> workHistories) {
    List<WorkHistory> wList = new ArrayList<WorkHistory>();
    Set set = new HashSet();
    for (WorkHistory workHistory : workHistories) {
      String element = workHistory.getInsName();
      if (set.add(element)) {
        wList.add(workHistory);
      }
    }
    workHistories.clear();
    workHistories.addAll(wList);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public void saveImportPub(PubImportVO importVo) throws ServiceException {
    try {
      boolean isGroupMember = this.isGroupMember(importVo);
      // 获取缓存好的成果数据
      Map<Integer, PendingImportPubVO> pendingPub = null;
      Object cacheObj = cacheService.get("searchImportPubCache", importVo.getCacheKey());
      if (cacheObj != null) {
        pendingPub = (Map<Integer, PendingImportPubVO>) cacheObj;
      }
      // 先获取要排除掉的人员Id
      List<Long> removePsnIds = this.findAllRemovePsnId(importVo.getPsnId());
      Set<PsnInfo> psnInfoSet = new HashSet<PsnInfo>();
      Set<Long> psnIdSet = new HashSet<Long>();
      // 先获取构建好的成果json参数数据
      JSONArray jsonList = importVo.getPubJsonList();
      // 成功导入的成果数量
      int importSuccessNum = 0;
      if (isGroupMember && CollectionUtils.isNotEmpty(jsonList) && pendingPub != null) {
        int pubSize = jsonList.size();
        // 遍历待导入成果信息
        for (int i = 0; i < pubSize; i++) {
          // 获取成果保存操作所需参数
          Map<String, Object> pubParams = JacksonUtils.jsonToMap(jsonList.get(i).toString());
          PendingImportPubVO pubImport = pendingPub.get(i);
          // 检查是否勾选了复选框（是否要导入该条成果），不导入的就跳过, 1：选中了要保存，0：没勾选不保存
          // int needSave = NumberUtils.toInt(Objects.toString(pubParams.get("pub_save"), "0"));
          // 获取重复成果选中的操作
          String dupPubOpt = this.checkDupPubOpt(pubParams);
          if (pubImport != null && !"skip".equals(dupPubOpt)) {
            // 重新构建成果类型（因为用户可能改变成果类型,默认为期刊论文类型）
            int pubType = NumberUtils.toInt(Objects.toString(pubParams.get("pub_type"), "0"));
            if (!CommonUtils.compareIntegerValue(pubType, pubImport.getPubType())) {
              List<PubMemberDTO> memberList = pubImport.getMembers();
              // pubImport.setPubTypeInfo(pubImportInfoDriverFactory.getDriver(pubType).getNewPubTypeInfo(pubType));
              PubImportInfoDriver pubImportInfoDriver = pubImportInfoDriverFactory.getDriver(pubType);
              // 重新构造pubTypeInfo属性
              Map<String, Object> pubTypeInfo = PdwhPubXMLToJsonStrUtils.buildPubTypeInfo(pubImport.getPubXml(),
                  pubType, null, LocaleContextHolder.getLocale().equals(Locale.CHINA));
              String typeInfoJson = "";
              if (pubTypeInfo != null) {
                typeInfoJson = JacksonUtils.mapToJsonStr(pubTypeInfo);
              }
              if (StringUtils.isNotEmpty(typeInfoJson)) {
                typeInfoJson = XssUtils.transferJson(typeInfoJson);
              }

              pubImport = pubImportInfoDriver.buildPendingImportPubVoByJson(pubImport.getJsonPub());
              pubImport.setPubTypeInfo(pubImportInfoDriver.buildTypeInfo(typeInfoJson));

              pubImport.setMembers(memberList);
              pubImport.setPubType(pubType);
              this.rebuildPubBriefDesc(pubImport);
            }

            // 重新构建作者信息
            this.rebuildPubMembers(pubImport);
            // 重新构建收录情况
            this.rebuildPubSourceInfo(pubImport, pubParams);
            // 更新，替换的时候全文、附件、作者这些不替换，也就是把重复的成果的全文、附件、作者、成果ID放到新的要导入的成果json中，然后做更新操作
            if ("refresh".equals(dupPubOpt)) {
              this.updateDupPubJson(Objects.toString(pubParams.get("dup_des3_pub_id")).trim(), pubImport);
            } else {
              // 新增成果数据
              pubImport.setPubHandlerName(PubHandlerEnum.SAVE_SNS.name);
            }

            // 处理权限等其他信息
            this.dealPendingImportPubOtherInfo(pubParams, pubImport, importVo);
            // 清空pubxml数据，不需要传入成果保存业务中，xml数据过长会导致转出json失败
            pubImport.setPubXml("");
            // 保存成果数据
            boolean saveSuccess = this.saveOrUpdatePubJson(JacksonUtils.jsonObjectSerializerNoNull(pubImport));
            // 更新基准库-个人库成果对应关系表
            if (saveSuccess) {
              importSuccessNum++;
            }
            // 查找有相同成果的人员Id
            psnIdSet = findOwnerSamePubPsnIds(removePsnIds, pubImport, psnIdSet);
          }
        }
      }
      // 查找有相同成果的人员信息推荐给操作者为好友
      this.findOwnerSamePubPsnInfo(psnInfoSet, psnIdSet, LocaleContextHolder.getLocale());
      // 成功导入的成果数量
      importVo.setImportSuccessSize(importSuccessNum);
      importVo.setRecommendPsn(psnInfoSet);
      cacheService.remove("searchImportPubCache", importVo.getCacheKey());
    } catch (Exception e) {
      logger.error("保存导入的成果出错", e);
      cacheService.remove("searchImportPubCache", importVo.getCacheKey());
      throw new ServiceException(e);
    }
  }

  /**
   * 重新构建成果的作者信息，主要是对authorNames拆分出来的作者信息进行补充完善的操作
   * 
   * @param pubImport
   */
  private void rebuildPubMembers(PendingImportPubVO<PubTypeInfoDTO> pubImport) {
    List<PubMemberDTO> memberList =
        pubAuthorMatchService.perfectMembers(pubImport.getAuthorNames(), pubImport.getMembers());
    pubImport.setMembers(memberList);
  }

  /**
   * 调用接口更新或保存成果json
   */
  protected boolean saveOrUpdatePubJson(String pubJson) throws ServiceException {
    String result = "";
    boolean saveSuccess = false;
    try {
      result = RestTemplateUtils.post(restTemplate, SAVEORUPDATE, pubJson);
      Map<String, String> resultMap = JacksonUtils.jsonToMap(result);
      if (resultMap != null && "SUCCESS".equals(resultMap.get("status"))) {
        saveSuccess = true;
      }
    } catch (Exception e) {
      logger.error("保存成果json出错 pubJson={}", pubJson, e);
      throw e;
    }
    return saveSuccess;
  }

  // 更新重复的成果json数据
  @SuppressWarnings({"rawtypes", "unchecked", "deprecation"})
  protected void updateDupPubJson(String des3PubId, PendingImportPubVO pub) throws Exception {
    Map<String, Object> detailParams = new HashMap<>();
    PubDetailVO pubDetailVO = new PubDetailVO();
    detailParams.put(V8pubConst.DESC_PUB_ID, des3PubId);
    detailParams.put("serviceType", "snsPub");
    pubDetailVO = pubDetailHandleService.queryPubDetail(detailParams);
    if (pubDetailVO != null) {
      pub.setAuthorNames(pubDetailVO.getAuthorName());
      pub.setPubId(pubDetailVO.getPubId());
      pub.setDes3PubId(Des3Utils.encodeToDes3(pubDetailVO.getPubId().toString()));
      PubFulltextDTO fulltext = pubDetailVO.getFullText();
      PubFulltextDTO fulltextDTO = new PubFulltextDTO();
      if (fulltext != null) {
        fulltextDTO.setDes3fileId(fulltext.getDes3fileId());
        fulltextDTO.setFileName(fulltext.getFileName());
        fulltextDTO.setPermission(fulltext.getPermission());
      }
      pub.setFullText(fulltextDTO);
      List<PubMemberDTO> members = pubDetailVO.getMembers();
      List<PubMemberDTO> memDTOs = new ArrayList<PubMemberDTO>();
      if (CollectionUtils.isNotEmpty(members)) {
        for (PubMemberDTO mem : members) {
          PubMemberDTO dto = new PubMemberDTO();
          dto.setCommunicable(mem.isCommunicable());
          dto.setEmail(mem.getEmail());
          dto.setFirstAuthor(mem.isFirstAuthor());
          dto.setName(mem.getName());
          dto.setOwner(mem.isOwner());
          dto.setPsnId(mem.getPsnId());
          dto.setSeqNo(mem.getSeqNo());
          dto.setInsNames(mem.getInsNames());
          if (CollectionUtils.isEmpty(dto.getInsNames())) {
            dto.setInsNames(new ArrayList<MemberInsDTO>());
          }
          memDTOs.add(dto);
        }
      }
      // SCM-20931 对有全文链接的有值不进行更新
      if (StringUtils.isNotBlank(pubDetailVO.getSrcFulltextUrl())) {
        pub.setSrcFulltextUrl(pubDetailVO.getSrcFulltextUrl());
      }
      // SCM-20933 期刊论文的期号卷号有值不进行更新
      if (pubDetailVO.getPubType() != null && pubDetailVO.getPubType() == 4
          && pubDetailVO.getPubType().equals(pub.getPubType())) {
        JournalInfoDTO old = (JournalInfoDTO) pubDetailVO.getPubTypeInfo();
        JournalInfoDTO now = (JournalInfoDTO) pub.getPubTypeInfo();
        if (old != null) {
          // 期号有值不更新
          String issue = StringUtils.isNotBlank(old.getIssue()) ? old.getIssue() : now.getIssue();
          // 期号有值不更新
          String volumeNo = StringUtils.isNotBlank(old.getVolumeNo()) ? old.getVolumeNo() : now.getVolumeNo();
          if (now == null) {
            now = new JournalInfoDTO();
            now.setISSN(old.getISSN());
            now.setName(old.getName());
            now.setJid(old.getJid());
            now.setPublishStatus(old.getPublishStatus());
            now.setPageNumber(old.getPageNumber());
          }
          now.setIssue(issue);
          now.setVolumeNo(volumeNo);
        }
      }
      // SCM-20928 附件不能丢
      List<Accessory> accessorys = pubDetailVO.getAccessorys();
      if (CollectionUtils.isNotEmpty(accessorys)) {
        pub.setAccessorys(accessorys);
      }
      // 查重到的成果要带updateMark至接口进行保存
      pub.setUpdateMark(pubDetailVO.getUpdateMark());
      pub.setMembers(memDTOs);
      pub.setPubHandlerName(PubHandlerEnum.UPDATE_SNS.name);
    } else {
      pub.setPubHandlerName(PubHandlerEnum.SAVE_SNS.name);
    }
  }

  // 重构成果收入情况
  @SuppressWarnings({"unchecked", "rawtypes"})
  protected void rebuildPubSourceInfo(PendingImportPubVO pub, Map<String, Object> pubParams) {
    PubSituationDTO dto = null;
    String sourceInfo = Objects.toString(pubParams.get("situation_info"), "").trim();
    if (StringUtils.isNotBlank(sourceInfo)) {
      // 收录情况用","分割
      String[] sourceArr = sourceInfo.split(",");
      List<PubSituationDTO> importSituations = pub.getSituations();
      String xmlSitLibrary = "";
      if (CollectionUtils.isNotEmpty(importSituations)) {
        xmlSitLibrary = importSituations.stream().map(PubSituationDTO::getLibraryName).collect(Collectors.joining(","));
        xmlSitLibrary = xmlSitLibrary.replace("SCI", "SCIE");
      }
      for (String source : sourceArr) {
        source = source.toUpperCase();
        if (StringUtils.isNotBlank(xmlSitLibrary) && xmlSitLibrary.contains(source)) {
          continue;
        }
        dto = new PubSituationDTO();
        dto.setLibraryName(PubLibraryEnum.parse(source).desc);
        dto.setPubId(pub.getPubId());
        dto.setSrcDbId(PubParamUtils.buildDbId(source) + "");
        dto.setSitOriginStatus(false);
        dto.setSitStatus(true);
        importSituations.add(dto);
      }
      pub.setSituations(importSituations);
    }
  }

  /**
   * 当前登录人员是否是群组成员
   * 
   * @param importVo
   * @return
   */
  protected boolean isGroupMember(PubImportVO importVo) {
    Long groupId = importVo.getGrpId();
    Long psnId = importVo.getPsnId();
    boolean isGroupMember = true;
    if (groupId != null && psnId != null && groupId > 0L) {
      isGroupMember = groupMemberDao.isExistGrpMember(groupId, psnId);
    }
    return isGroupMember;
  }

  /**
   * 获取重复成果选中的操作
   * 
   * @param pubParams
   * @return skip：跳过, refresh：更新, add:新增, no_dup:没有重复
   */
  protected String checkDupPubOpt(Map<String, Object> pubParams) {
    String result = "no_dup";
    // 成果是否重复，重复的成果选取的操作是什么（跳过、新增、更新）, 1:重复了，0：没重复，默认没重复
    String dupDes3PubId = Objects.toString(pubParams.get("dup_des3_pub_id"), "").trim();
    if (StringUtils.isNotBlank(dupDes3PubId)) {
      // 获取重复成果选择的操作
      result = Objects.toString(pubParams.get("dup_opt"), "refresh");
    }
    return result;
  }

  /**
   * 处理一些其他的信息
   * 
   * @param pubParams
   * @param pubImport
   * @param importVo
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  protected void dealPendingImportPubOtherInfo(Map<String, Object> pubParams, PendingImportPubVO pubImport,
      PubImportVO importVo) {
    try {
      pubImport.setRecordFrom(PubSnsRecordFromEnum.IMPORT_FROM_DATABASE);
      pubImport.setJsonPub("");
      // 是个人成果还是群组成果
      int pubGenre = 1;
      if (importVo.getGrpId() != null && importVo.getGrpId() > 0) {
        pubImport.setDes3GrpId(Des3Utils.encodeToDes3(importVo.getGrpId().toString()));
        pubGenre = 2;
        // 是保存为成果还是文献
        if (CommonUtils.compareIntegerValue(importVo.getSavePubType(), 2)) {
          pubImport.setIsProjectPub(0);
        } else {
          pubImport.setIsProjectPub(1);
        }
      }
      pubImport.setDes3PsnId(Des3Utils.encodeToDes3(importVo.getPsnId().toString()));
      pubImport.setPubGenre(pubGenre);
      // 为了能正常转换成PubDTO对象
      if (pubImport.getCitations() == null) {
        pubImport.setCitations(0);
      }
      List<PubMemberDTO> memList = pubImport.getMembers();
      if (CollectionUtils.isNotEmpty(memList)) {
        for (PubMemberDTO mem : memList) {
          if (CollectionUtils.isEmpty(mem.getInsNames())) {
            mem.setInsNames(new ArrayList<MemberInsDTO>());
          }
        }
      }
      // pubTypeInfo为空的话就给个空的对象
      if (pubImport.getPubTypeInfo() == null) {
        int pubType = pubImport.getPubType();
        pubImport.setPubTypeInfo(pubImportInfoDriverFactory.getDriver(pubType).getNewPubTypeInfo(pubType));
      }
    } catch (Exception e) {
      logger.error("保存导入成果，构建其他一些信息出错", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 成果查重
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  private Long checkDupPub(PendingImportPubVO<PubTypeInfoDTO> pubImport, String des3PsnId) {
    Long pubId = 0L;
    Map<String, String> result = new HashMap<String, String>();
    try {
      String publishDate = pubImport.getPublishDate();
      String url = domainScm + V8pubQueryPubConst.PUB_DUPCHECK_URL;
      Map<String, Object> checkMap = new HashMap<String, Object>();
      checkMap.put("pubGener", pubImport.getPubGenre());
      checkMap.put("des3PsnId", des3PsnId);
      checkMap.put("title", pubImport.getTitle());
      checkMap.put("pubType", pubImport.getPubType());
      checkMap.put("doi", pubImport.getDoi());
      checkMap.put("srcDbId", pubImport.getSrcDbId());
      checkMap.put("sourceId", pubImport.getSourceId());
      if (5 == pubImport.getPubType()) {
        PatentInfoDTO infoDto = (PatentInfoDTO) pubImport.getPubTypeInfo();
        if (infoDto != null) {
          checkMap.put("applicationNo", infoDto.getApplicationNo());
          checkMap.put("publicationOpenNo", infoDto.getPublicationOpenNo());
          Integer status = infoDto.getStatus();
          if (status != null && status == 1) {
            // 授权状态
            publishDate = infoDto.getStartDate();
          } else {
            // 申请状态
            publishDate = infoDto.getApplicationDate();
          }
        }
      }
      if (12 == pubImport.getPubType()) {
        StandardInfoDTO infoDto = (StandardInfoDTO) pubImport.getPubTypeInfo();
        if (infoDto != null) {
          checkMap.put("standardNo", infoDto.getStandardNo());
        }
      }
      if (13 == pubImport.getPubType()) {
        SoftwareCopyrightDTO infoDto = (SoftwareCopyrightDTO) pubImport.getPubTypeInfo();
        if (infoDto != null) {
          checkMap.put("registerNo", infoDto.getRegisterNo());
        }
      }
      checkMap.put("pubYear", PubDetailVoUtil.parseDateForYear(publishDate));
      HttpHeaders headers = new HttpHeaders();
      MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
      headers.setContentType(type);
      HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(checkMap), headers);
      result = restTemplate.postForObject(url, entity, Map.class);
      if (result != null && "SUCCESS".equalsIgnoreCase(result.get("status"))) {
        String dupPubId = result.get("msg");
        if (StringUtils.isNotBlank(dupPubId)) {
          pubId = Long.valueOf(dupPubId);
        } else {
          pubId = null;
        }
      }
    } catch (Exception e) {
      logger.error("导入成果，成果查重出错", e);
      throw new ServiceException(e);
    }
    return pubId;
  }

  // 用查重到的成果的收录情况更新待导入成果的收录情况
  protected void rebuildPubSituation(PendingImportPubVO<PubTypeInfoDTO> vo, Long dupPubId) {
    List<PubSituationDTO> importSituations = vo.getSituations();
    if (!NumberUtils.isNullOrZero(dupPubId)) {
      List<PubSituationPO> situations = pubSituationDao.findPubSituationsByPubId(dupPubId);
      if (CollectionUtils.isNotEmpty(situations)) {
        // 查重到的成果，合并xml中的收录情况信息，进行界面展示和导入
        String newLibrarys =
            importSituations.stream().map(PubSituationDTO::getLibraryName).collect(Collectors.joining(","));
        newLibrarys = newLibrarys.toUpperCase();
        for (PubSituationPO sit : situations) {
          String libraryName = sit.getLibraryName().toUpperCase();
          if (!newLibrarys.contains(libraryName)) {
            PubSituationDTO pubSituationDTO = new PubSituationDTO();
            pubSituationDTO.setLibraryName(sit.getLibraryName());
            pubSituationDTO.setSrcDbId(sit.getSrcDbId());
            pubSituationDTO.setSrcId(sit.getSrcId());
            pubSituationDTO.setSrcUrl(sit.getSrcUrl());
            pubSituationDTO.setSitStatus(sit.getSitStatus() == 1);
            pubSituationDTO.setSitOriginStatus(sit.getSitOriginStatus() == 1);
            importSituations.add(pubSituationDTO);
          }
        }

      }
      vo.setSituations(importSituations);
    }
    vo.setDupPubId(dupPubId);
    vo.setDes3PubId(Des3Utils.encodeToDes3(Objects.toString(dupPubId)));
    this.checkPubIncluded(vo);
  }

  // 设置收录情况
  protected void checkPubIncluded(PendingImportPubVO<PubTypeInfoDTO> vo) {
    List<PubSituationDTO> importSituations = vo.getSituations();
    if (CollectionUtils.isNotEmpty(importSituations)) {
      for (PubSituationDTO sitDTO : importSituations) {
        String desc = sitDTO.getLibraryName();
        if ("EI".equalsIgnoreCase(desc)) {
          vo.setEIIncluded(sitDTO.isSitStatus());
          continue;
        }
        if ("ISTP".equalsIgnoreCase(desc)) {
          vo.setISTPIncluded(sitDTO.isSitStatus());
          continue;
        }
        if ("SCIE".equalsIgnoreCase(desc) || "SCI".equalsIgnoreCase(desc)) {
          vo.setSCIEIncluded(sitDTO.isSitStatus());
          continue;
        }
        if ("SSCI".equalsIgnoreCase(desc)) {
          vo.setSSCIIncluded(sitDTO.isSitStatus());
          continue;
        }
      }
    }
  }

  // 获取时间戳
  private String getTimeStamp() {
    long time = System.currentTimeMillis();
    String timeStamp = String.valueOf(time / 1000);
    return timeStamp;
  }

  @Override
  public List<Long> findAllRemovePsnId(Long psnId) throws ServiceException {
    List<Long> psnIds = new ArrayList<Long>();
    try {
      // 所有好友
      List<Long> fList = friendDao.findFriend(psnId);
      psnIds.addAll(fList);
      // 所有已经忽略的人员
      List<Long> sysList = friendTempSysDao.findFriendTempSys(psnId);
      psnIds.addAll(sysList);
      // 已经发送过请求，对方仍然没有处理的人员
      List<Long> reqList = friendTempDao.findReqTempPsnId(psnId);
      psnIds.addAll(reqList);
      // 排除自己
      psnIds.add(psnId);
    } catch (Exception e) {
      logger.error("成果导入，获取好友推荐时需要排除掉的人员出错， psnId = " + psnId, e);
      throw new ServiceException();
    }
    return psnIds;
  }

  /**
   * 获取可能的合作者人员ID
   * 
   * @param removePsnIds
   * @param vo
   * @param psnIdSet
   * @return
   */
  @SuppressWarnings("rawtypes")
  protected Set<Long> findOwnerSamePubPsnIds(List<Long> removePsnIds, PendingImportPubVO vo, Set<Long> psnIdSet) {
    if (psnIdSet == null) {
      psnIdSet = new HashSet<Long>();
    }
    if (CollectionUtils.isEmpty(removePsnIds)) {
      removePsnIds.add(0L);
    }
    int size = psnIdSet.size();
    // 若小于9，继续查找
    if (size < 9) {
      List<Long> psnIdList = pubKnowDao.findSamePubByPsn(PubImportConstants.OUTPUT,
          PubHashUtils.cleanTitleHash(vo.getTitle()), removePsnIds);
      if (CollectionUtils.isNotEmpty(psnIdList)) {
        psnIdSet.addAll(psnIdList);
        // 排除掉隐私用户和设置了不让加好友的用户
        List<Long> privateIds = psnPrivateDao.isPsnPrivate(psnIdList);
        psnIdSet.removeAll(privateIds);
        List<Long> rejectAddFrdIds = privacySettingsDao.getPsnIdsByPrivacySetting(psnIdList,
            PsnPrivacySettingConst.reqAddFrd, PsnPrivacySettingConst.PERMISSION_SELF);
        psnIdSet.removeAll(rejectAddFrdIds);
      }
    }
    return psnIdSet;
  }

  /**
   * 查找可能的合作者信息
   * 
   * @param psnInfoSet
   * @param psnIdSet
   * @param locale
   * @return
   * @throws ServiceException
   */
  protected Set<PsnInfo> findOwnerSamePubPsnInfo(Set<PsnInfo> psnInfoSet, Set<Long> psnIdSet, Locale locale)
      throws ServiceException {
    if (CollectionUtils.isNotEmpty(psnIdSet)) {
      List<Long> psnIds = new ArrayList<Long>();
      psnIds.addAll(psnIdSet);
      // 查询出有共同成果的人员的信息，最多9个
      List<Person> psnList = personDao.findBatchPsnByIds(psnIds, 9);
      if (CollectionUtils.isNotEmpty(psnList)) {
        for (Person psn : psnList) {
          PsnInfo info = new PsnInfo();
          info.setPsnId(psn.getPersonId());
          info.setAvatarUrl(psn.getAvatars());
          info.setName(LocaleTextUtils.getStrByLocale(locale.toString(), psn.getName(), psn.getEname()));
          this.buildPsnTitoloInfo(info, psn);
          psnInfoSet.add(info);
        }
      }
    }
    return psnInfoSet;
  }

  /**
   * 成果标题HashCode.
   * 
   * @param title 标题
   * @return int
   */
  protected static Integer cleanTitleHash(String title) {
    if (title == null) {
      return null;
    } else {
      // html解码
      title = HtmlUtils.htmlUnescape(title);
      title = XmlUtil.trimAllHtml(title);
      title = XmlUtil.filterForCompare(title);
    }
    title = title.replaceAll("\\s+", "").trim().toLowerCase();
    if ("".equals(title)) {
      return null;
    }
    return title.hashCode();
  }

  /**
   * 构建人员单位+部门 职称+头衔信息
   * 
   * @param psnInfo
   * @param psn
   * @return
   */
  private void buildPsnTitoloInfo(PsnInfo psnInfo, Person psn) {
    String insName = psn.getInsName();
    String department = psn.getDepartment();
    String position = psn.getPosition();
    String titolo = psn.getTitolo();
    // 构建单位+部门信息
    if (StringUtils.isBlank(insName) || StringUtils.isBlank(department)) {
      psnInfo.setInsAndDep(
          (StringUtils.isBlank(insName) ? "" : insName) + (StringUtils.isBlank(department) ? "" : department));
    } else {
      psnInfo.setInsAndDep(insName + ", " + department);
    }
    // 构建职称+头衔信息
    if (StringUtils.isBlank(position) || StringUtils.isBlank(titolo)) {
      psnInfo.setPosAndTitolo(
          (StringUtils.isBlank(position) ? "" : position) + (StringUtils.isBlank(titolo) ? "" : titolo));
    } else {
      psnInfo.setPosAndTitolo(position + ", " + titolo);
    }
  }

  @Override
  public String checkDuplicatePub(PubPdwhDetailDOM detail) throws ServiceException {
    Map<String, Object> dupMap = new HashMap<String, Object>();
    dupMap.put("pubGener", 1);// 成果大类别，1代表个人成果 2代表群组成果 3代表基准库成果
    dupMap.put("title", detail.getTitle());
    dupMap.put("pubYear", PubDetailVoUtil.parseDateForYear(detail.getPublishDate()));
    dupMap.put("pubType", detail.getPubType());
    dupMap.put("doi", detail.getDoi());
    dupMap.put("srcDbId", detail.getSrcDbId());
    dupMap.put("sourceId", detail.getSourceId());
    if (5 == detail.getPubType()) {
      PatentInfoBean infoBean = (PatentInfoBean) detail.getTypeInfo();
      if (infoBean != null) {
        dupMap.put("applicationNo", infoBean.getApplicationNo());
        dupMap.put("publicationOpenNo", infoBean.getPublicationOpenNo());
      }
    }
    if (12 == detail.getPubType()) {
      StandardInfoBean infoBean = (StandardInfoBean) detail.getTypeInfo();
      if (infoBean != null) {
        dupMap.put("standardNo", infoBean.getStandardNo());
      }
    }
    if (13 == detail.getPubType()) {
      SoftwareCopyrightBean infoBean = (SoftwareCopyrightBean) detail.getTypeInfo();
      if (infoBean != null) {
        dupMap.put("registerNo", infoBean.getRegisterNo());
      }
    }
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(dupMap), headers);
    String dupUrl = domainScm + V8pubQueryPubConst.PUB_DUPCHECK_URL;
    String result = restTemplate.postForObject(dupUrl, entity, String.class);
    return result;
  }

  @Override
  public String getOrgNameAlias(String orgName, String dbCodes) {
    Long insId = 0L;
    Long dbId = 0L;
    try {
      Institution ins = institutionDao.lookUpByName(orgName);
      if (null != ins) {
        insId = ins.getId();
      }
      if (StringUtils.isBlank(dbCodes)) {
        return null;
      }
      String[] dbCodeArr = dbCodes.split(":");
      Map<String, String> map = new HashMap<String, String>();
      for (int i = 0; i < dbCodeArr.length; i++) {
        if (insId == 0L) {
          map.put(dbCodeArr[i], orgName);
        } else {
          ConstRefDb constRefDb = constRefDbDao.getConstRefDbByCode(dbCodeArr[i]);
          if (null == constRefDb) {
            map.put(dbCodeArr[i], orgName);
          } else {
            dbId = constRefDb.getId();
            InsAlias insAlias = insAliasDao.getAliasName(insId, dbId);
            map.put(dbCodeArr[i], null != insAlias ? insAlias.getName() : orgName);
          }
        }
      }
      return JacksonUtils.mapToJsonStr(map).toString();
    } catch (Exception e) {
      logger.error("获取单位别名时出错", e);
    }
    return null;
  }

  /**
   * 检索导入成果的时候，将拉取的成果xml存一份到pdwh库
   * 
   * @param psnId
   * @param insId
   * @param xmlData
   * @param webType
   */
  private void cachePubXmlToPdwh(Long psnId, Long insId, String xmlData, Integer webType) {
    DbCachePfetch data = new DbCachePfetch(psnId, insId, xmlData, webType, new Date());
    this.dbCachePfetchDao.save(data);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void buildPdwhPubInfo(PubInfo pubInfo, Long psnId) {
    PubPdwhDetailDOM detail = pubPdwhDetailDAO.findById(pubInfo.getPubId());
    pubInfo.setTitle(detail.getTitle());
    pubInfo.setAuthorNames(detail.getAuthorNames());
    pubInfo.setBriefDesc(detail.getBriefDesc());
    Set<PubSituationBean> situations = detail.getSituations();
    for (PubSituationBean pubSituationBean : situations) {
      if (pubSituationBean.isSitStatus() && "EI".equals(pubSituationBean.getLibraryName())) {
        pubInfo.setListEi(1);
      } else if (pubSituationBean.isSitStatus() && "SCIE".equals(pubSituationBean.getLibraryName())) {
        pubInfo.setListSci(1);
      } else if (pubSituationBean.isSitStatus() && "SCI".equals(pubSituationBean.getLibraryName())) {
        pubInfo.setListSci(1);
      } else if (pubSituationBean.isSitStatus() && "SSCI".equals(pubSituationBean.getLibraryName())) {
        pubInfo.setListSsci(1);
      } else if (pubSituationBean.isSitStatus() && "ISTP".equals(pubSituationBean.getLibraryName())) {
        pubInfo.setListIstp(1);
      }
    }
    String indexUrl = pdwhPubIndexUrlService.getIndexUrlByPubId(pubInfo.getPubId());
    if (StringUtils.isNotBlank(indexUrl)) {
      pubInfo.setPubIndexUrl(domainScm + "/" + ShortUrlConst.S_TYPE + "/" + indexUrl);
    } else {
      pubInfo.setPubIndexUrl(
          domainScm + "/pub/details/pdwh?des3PubId=" + Des3Utils.encodeToDes3(pubInfo.getPubId().toString()));
    }
    pubInfo.setDbid(detail.getSrcDbId());
    pubInfo.setPubType(detail.getPubType());
    rebuildAutherMatch(pubInfo, psnId);
  }

  private void rebuildAutherMatch(PubInfo pubInfo, Long psnId) {
    Integer matchFlag = pubAuthorMatchService.isMatch(pubInfo.getAuthorNames(), psnId);
    pubInfo.setAuthermatch(matchFlag);
  }

  @Override
  public List<ConstPubType> getAllPubType() {
    return constPubTypeDao.getAllTypes(LocaleContextHolder.getLocale());
  }

  @Autowired
  private PubPdwhDetailService pubPdwhDetailService;

  @SuppressWarnings("unchecked")
  @Override
  public Set<PubSituationBean> buildSitationList(List<String> sitationList, Long pubId) {
    Set<PubSituationBean> sitList =
        Optional.ofNullable(sitationList).orElse(new ArrayList<String>()).stream().map(s -> {
          PubSituationBean sit = new PubSituationBean();
          sit.setLibraryName(PubLibraryEnum.parse(s).desc);
          sit.setSitStatus(true);
          sit.setSitOriginStatus(false);
          return sit;
        }).collect(Collectors.toSet());

    // 取成果原数据的收录情况信息进行填充至用户选中的收录情况中
    if (NumberUtils.isNotNullOrZero(pubId)) {
      PubPdwhDetailDOM pdwhDetail = pubPdwhDetailService.get(pubId);
      Set<PubSituationBean> situations = pdwhDetail.getSituations();
      rebuildSituation(situations);
      sitList.addAll(pdwhDetail.getSituations());
      initSitations(sitList, situations);
    }
    return sitList;
  }

  /**
   * 解决历史数据中SCI的问题
   * 
   * @param situations
   */
  private void rebuildSituation(Set<PubSituationBean> situations) {
    if (CollectionUtils.isNotEmpty(situations)) {
      for (PubSituationBean bean : situations) {
        bean.setLibraryName(PubLibraryEnum.parse(bean.getLibraryName()).desc);
      }
    }
  }

  private void initSitations(Set<PubSituationBean> sitList, Set<PubSituationBean> situations) {
    if (CollectionUtils.isNotEmpty(situations) && CollectionUtils.isNotEmpty(sitList)) {
      for (PubSituationBean bean : situations) {
        for (PubSituationBean dto : sitList) {
          if (bean != null && dto != null) {
            if (bean.getLibraryName().equalsIgnoreCase(dto.getLibraryName())) {
              dto.setSrcDbId(bean.getSrcDbId());
              dto.setSrcId(bean.getSrcId());
              dto.setSrcUrl(bean.getSrcUrl());
            }
          }
        }
      }
    }
  }

  @SuppressWarnings("rawtypes")
  @Override
  public String updateSnsPub(Long dupPubId, Long psnId, Long pdwhPubId, Integer pubType,
      Set<PubSituationBean> situationList) {
    Map<String, Object> map = new HashMap<>();
    PubPdwhDetailDOM pdwhDetail = pubPdwhDetailDAO.findById(pdwhPubId);
    if (pdwhDetail == null) {
      return null;
    }
    // 更新重复的成果json数据。作者、全文、附件不更新,所以要先把个人成果的信息查出来
    Map<String, Object> detailParams = new HashMap<>();
    detailParams.put(V8pubConst.DESC_PUB_ID, Des3Utils.encodeToDes3(dupPubId.toString()));
    detailParams.put("serviceType", "snsPub");
    PubDetailVO pubDetailVO = pubDetailHandleService.queryPubDetail(detailParams);
    this.buildPubInfo(pdwhDetail, pubDetailVO, map);
    map.put("des3PsnId", Des3Utils.encodeToDes3(String.valueOf(psnId)));
    map.put("pubType", pubType == null ? pdwhDetail.getPubType() : pubType);
    map.put("pubTypeInfo", buildPubTypeInfo(pdwhDetail, pubType));
    map.put("situations", situationList);
    map.put("updateMark", buildUpdateMark(dupPubId));
    // 调用保存个人成果接口
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    String pubJson = JacksonUtils.mapToJsonStr(map);
    pubJson = XssUtils.transferJson(pubJson);
    HttpEntity<String> entity = new HttpEntity<String>(pubJson, headers);
    String saveUrl = domainScm + V8pubQueryPubConst.PUBHANDLER_URL;
    String result = restTemplate.postForObject(saveUrl, entity, String.class);
    return result;
  }

  private PubTypeInfoBean buildPubTypeInfo(PubPdwhDetailDOM pdwhDetail, Integer pubType) {
    if (pubType != null && !pubType.equals(pdwhDetail.getPubType())) {
      switch (pubType) {
        case 1:
          return new AwardsInfoBean();
        case 2:
          return new BookInfoBean();
        case 3:
          return new ConferencePaperBean();
        case 4:
          return new JournalInfoBean();
        case 5:
          return new PatentInfoBean();
        case 7:
          return new OtherInfoBean();
        case 8:
          return new ThesisInfoBean();
        case 10:
          return new BookInfoBean();
      }
    }
    return pdwhDetail.getTypeInfo();
  }

  private Integer buildUpdateMark(Long dupPubId) {
    if (NumberUtils.isNotNullOrZero(dupPubId)) {
      PubSnsPO pubSnsPO = pubSnsService.get(dupPubId);
      if (pubSnsPO != null) {
        return pubSnsPO.getUpdateMark();
      }
    }
    return null;
  }

  @SuppressWarnings("rawtypes")
  private void buildPubInfo(PubPdwhDetailDOM pdwhDetail, PubDetailVO pubDetailVO, Map<String, Object> map) {
    if (pubDetailVO.getFullText() != null) {
      PubFulltextDTO fulltext = pubDetailVO.getFullText();
      fulltext.setDes3fileId(fulltext.getDes3fileId());
      PubFulltextDTO fulltextDTO = new PubFulltextDTO();
      if (fulltext != null) {
        fulltextDTO.setDes3fileId(fulltext.getDes3fileId());
        fulltextDTO.setFileName(fulltext.getFileName());
        fulltextDTO.setPermission(fulltext.getPermission());
      }
      map.put("fullText", fulltextDTO);
    }
    if (pubDetailVO.getAccessorys() != null) {
      map.put("accessorys", pubDetailVO.getAccessorys());
    }
    map.put("pubHandlerName", PubHandlerEnum.UPDATE_SNS.getName());
    map.put("des3PubId", pubDetailVO.getDes3PubId());
    map.put("des3PdwhPubId", Des3Utils.encodeToDes3(pdwhDetail.getPubId().toString()));
    map.put("pubGenre", PubGenreConstants.PDWH_SNS_PUB);
    map.put("title", pdwhDetail.getTitle());
    map.put("publishDate", pdwhDetail.getPublishDate());
    map.put("countryId", pdwhDetail.getCountryId());
    map.put("fundInfo", pdwhDetail.getFundInfo());
    map.put("citations", pdwhDetail.getCitations());
    map.put("doi", pdwhDetail.getDoi());
    map.put("summary", pdwhDetail.getSummary());
    map.put("keywords", pdwhDetail.getKeywords());
    map.put("srcFulltextUrl", pdwhDetail.getSrcFulltextUrl());
    map.put("recordFrom", PubSnsRecordFromEnum.IMPORT_FROM_PDWH);
    map.put("organization", pdwhDetail.getOrganization());
    map.put("sourceUrl", pdwhDetail.getSourceUrl());
    map.put("citedUrl", pdwhDetail.getCitedUrl());
    map.put("permission", 7);
    map.put("sourceId", pdwhDetail.getSourceId());
    map.put("srcDbId", pdwhDetail.getSrcDbId());
    map.put("dbId", pdwhDetail.getSrcDbId());
    map.put("members", pubDetailVO.getMembers());
    map.put("authorNames", pubDetailVO.getAuthorNames());
  }

  public void constructData(Map<String, Object> dataMap) {
    // 重新构造srcDbId
    buildSrcDbId(dataMap);
    // 重新构造countryId
    buildCountryId(dataMap);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public void buildSrcDbId(Map<String, Object> map) {
    Long srcDbId = null;
    if (map.get("srcDbId") != null) {
      ConstRefDb constRefDb = constRefDbDao.getConstRefDbByCode(map.get("srcDbId").toString());
      if (constRefDb != null) {
        srcDbId = constRefDb.getId();
        map.remove("srcDbId");
        map.put("srcDbId", srcDbId);
      }
    }
    // 填充引用情况记录对象中的srcDbId
    if (map.get("situations") != null) {
      String sitJson = JacksonUtils.listToJsonStr((List) map.get("situations"));
      if (StringUtils.isNotBlank(sitJson) && !"null".equalsIgnoreCase(sitJson)) {
        List<PubSituationDTO> situations = JacksonUtils.jsonToCollection(sitJson, List.class, PubSituationDTO.class);
        if (situations != null && situations.size() > 0) {
          for (PubSituationDTO pubSituationDTO : situations) {
            String dbCode = pubSituationDTO.getLibraryName();
            if (StringUtils.isNotBlank(dbCode)) {
              ConstRefDb constRefDb = constRefDbDao.getConstRefDbByCode(dbCode);
              if (constRefDb != null) {
                pubSituationDTO.setSrcDbId(constRefDb.getId() + "");
              }
            }
          }
          map.remove("situations");
          map.put("situations", situations);
        }
      }
    }
  }

  public void buildCountryId(Map<String, Object> map) {
    Long countryId = null;
    List<ConstRegion> regionList = constRegionDao.findAll();
    String country = (String) map.get("country");
    String city = (String) map.get("city");
    if (StringUtils.isNotEmpty(country)) {
      countryId = PubParamUtils.matchCountryId(regionList, country);
      if (countryId == null && StringUtils.isNotEmpty(country)) {
        countryId = PubParamUtils.matchCountryId(regionList, city);
      }
    }
    map.remove("city");
    map.remove("country");
    map.put("countryId", countryId);
  }
}
