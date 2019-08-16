package com.smate.center.batch.service.pdwh.pubimport;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.batch.base.TaskJobTypeConstants;
import com.smate.center.batch.connector.dao.exception.CreateBatchJobException;
import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.enums.BatchWeightEnum;
import com.smate.center.batch.connector.factory.BatchJobsFactory;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.batch.connector.util.BatchJobUtil;
import com.smate.center.batch.constant.ImportPubXmlConstants;
import com.smate.center.batch.dao.pdwh.prj.NsfcPrjKeywordsDao;
import com.smate.center.batch.dao.pdwh.prj.NsfcPrjKwByAuthorDao;
import com.smate.center.batch.dao.pdwh.prj.NsfcPrjPubRelationDao;
import com.smate.center.batch.dao.pdwh.pub.BaseJournalDao;
import com.smate.center.batch.dao.pdwh.pub.BaseJournalTitleDao;
import com.smate.center.batch.dao.pdwh.pub.PdwhCitedRelationDao;
import com.smate.center.batch.dao.pdwh.pub.PdwhMemberInsNameDAO;
import com.smate.center.batch.dao.pdwh.pub.PdwhPubCitedTimesDao;
import com.smate.center.batch.dao.pdwh.pub.PubPdwhDAO;
import com.smate.center.batch.dao.pdwh.pub.PubPdwhDetailDAO;
import com.smate.center.batch.dao.pdwh.pub.TmpTaskInfoRecordDao;
import com.smate.center.batch.dao.pdwh.pub.cnipr.CniprPublicationDao;
import com.smate.center.batch.dao.pdwh.pub.cnki.CnkiPublicationDao;
import com.smate.center.batch.dao.pdwh.pub.cnkipat.CnkiPatPublicationDao;
import com.smate.center.batch.dao.pdwh.pub.ei.EiPublicationDao;
import com.smate.center.batch.dao.pdwh.pub.isi.IsiPublicationDao;
import com.smate.center.batch.dao.pdwh.pub.sps.SpsPublicationDao;
import com.smate.center.batch.dao.pdwh.pub.wanfang.WfPublicationDao;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPubAddrDao;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPubDupDao;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPubKeywordsDao;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPubKeywordsSplitDao;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPubSourceDbDao;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPubXmlDao;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPubXmlToHandleDao;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPublicationDao;
import com.smate.center.batch.dao.pdwh.pubimport.TmpPdwhPubAddrCleanDao;
import com.smate.center.batch.dao.pdwh.pubimport.TmpPdwhPubAddrDao;
import com.smate.center.batch.dao.pdwh.pubimport.TmpPublicationAllDao;
import com.smate.center.batch.dao.sns.pub.ScmPubXmlDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.prj.NsfcPrjKeywords;
import com.smate.center.batch.model.pdwh.prj.NsfcPrjKwByAuthor;
import com.smate.center.batch.model.pdwh.pub.BaseJournal2;
import com.smate.center.batch.model.pdwh.pub.PdwhPubCitedTimes;
import com.smate.center.batch.model.pdwh.pub.PubPdwhPO;
import com.smate.center.batch.model.pdwh.pub.TmpTaskInfoRecord;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubAddr;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubDup;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubImportContext;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubKeywords;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubKeywordsSplit;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubSourceDb;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubXml;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;
import com.smate.center.batch.model.pdwh.pubimport.TmpPdwhPubAddr;
import com.smate.center.batch.model.pdwh.pubimport.TmpPdwhPubAddrClean;
import com.smate.center.batch.model.pdwh.pubimport.TmpPublicationAll;
import com.smate.center.batch.model.sns.pub.ConstRefDb;
import com.smate.center.batch.model.sns.pub.ScmPubXml;
import com.smate.center.batch.oldXml.pub.ImportPubXmlDocument;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pdwh.isipub.IsiPublicationService;
import com.smate.center.batch.service.pdwh.pub.CnkiPatPublicationService;
import com.smate.center.batch.service.pdwh.pub.CnkiPublicationService;
import com.smate.center.batch.service.pdwh.pub.EiPublicationService;
import com.smate.center.batch.service.pdwh.pub.OalibPublicationService;
import com.smate.center.batch.service.pdwh.pub.RainPatPublicationService;
import com.smate.center.batch.service.pdwh.pub.SpsPublicationService;
import com.smate.center.batch.service.pdwh.pub.WfPublicationService;
import com.smate.center.batch.service.pub.ConstRefDbService;
import com.smate.center.batch.util.pub.PdwhBriefUtils;
import com.smate.center.batch.util.pub.PubXmlDbUtils;
import com.smate.core.base.utils.common.HashUtils;
import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

@Service("pdwhPublicationService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPublicationServiceImpl implements PdwhPublicationService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MatchPubAddrsService matchPubAddrsService;
  @Autowired
  private IsiPublicationService isiPublicationService;
  @Autowired
  private EiPublicationService eiPublicationService;
  @Autowired
  private CnkiPublicationService cnkiPublicationService;
  @Autowired
  private CnkiPatPublicationService cnkiPatPublicationService;
  @Autowired
  private WfPublicationService wfPublicationService;
  @Autowired
  private RainPatPublicationService rainPatPublicationService;
  @Autowired
  private OalibPublicationService oalibPublicationService;
  @Autowired
  private SpsPublicationService spsPublicationService;
  @Autowired
  private BatchJobsService batchJobsService;
  @Autowired
  private ConstRefDbService constRefDbService;
  @Autowired
  private PdwhPubKeywordsDao pdwhPubKeywordsDao;
  @Autowired
  private PdwhPubKeywordsSplitDao pdwhPubKeywordsSplitDao;
  @Autowired
  private PdwhPubSourceDbDao pdwhPubSourceDbDao;
  @Autowired
  private PdwhPubAddrDao pdwhPubAddrDao;
  @Autowired
  private PdwhPubXmlDao pdwhPubXmlDao;
  @Autowired
  private PdwhPublicationDao pdwhPublicationDao;
  @Autowired
  private PdwhPubDupDao pdwhPubDupDao;
  @Autowired
  private IsiPublicationDao isiPublicationDao;
  @Autowired
  private EiPublicationDao eiPublicationDao;
  @Autowired
  private CnkiPublicationDao cnkiPublicationDao;
  @Autowired
  private CnkiPatPublicationDao cnkiPatPublicationDao;
  @Autowired
  private BaseJournalTitleDao baseJournalTitleDao;
  @Autowired
  private WfPublicationDao wfPublicationDao;
  @Autowired
  private CniprPublicationDao cniprPublicationDao;
  @Autowired
  private SpsPublicationDao spsPublicationDao;
  @Autowired
  private TmpPublicationAllDao tmpPublicationAllDao;
  @Autowired
  private PdwhPubXmlToHandleDao pdwhPubXmlToHandleDao;
  @Autowired
  private PdwhCitedRelationDao pdwhCitedRelationDao;
  @Autowired
  private PubShortUrlSaveService pubShortUrlSaveService;
  @Autowired
  PdwhPubCitedTimesDao pdwhPubCitedTimesDao;
  @Resource(name = "batchJobsContextFactory")
  private BatchJobsFactory batchJobsFactory;
  @Autowired
  private TmpPdwhPubAddrDao tmpPdwhPubAddrDao;
  @Autowired
  private TmpPdwhPubAddrCleanDao tmpPdwhPubAddrCleanDao;
  @Autowired
  private BaseJournalDao baseJournalDao;
  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDAO;
  @Autowired
  private NsfcPrjKwByAuthorDao nsfcPrjKwByAuthorDao;
  @Autowired
  private NsfcPrjKeywordsDao nsfcPrjKeywordsDao;
  @Autowired
  private NsfcPrjPubRelationDao nsfcPrjPubRelationDao;
  @Autowired
  private ScmPubXmlDao scmPubXmlDao;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;

  @Autowired
  private PdwhMemberInsNameDAO pdwhMemberInsNameDAO;
  @Autowired
  private TmpTaskInfoRecordDao tmpTaskInfoRecordDao;

  @Override
  public Map<String, Object> praseXmlData(String xmlData) {
    try {
      ImportPubXmlDocument document = new ImportPubXmlDocument(xmlData);
      Map<String, Object> map = document.prasePdwhImportPubDataNew();
      String sourceDbCode = (String) map.get("source_db_code");
      if (StringUtils.isNotBlank(sourceDbCode)) {
        ConstRefDb sourceDb = constRefDbService.getConstRefDbByCode(sourceDbCode);
        if (sourceDb != null) {
          Integer dbId = sourceDb.getId().intValue();
          map.put("dbId", dbId);
        }
      }
      String issn = StringUtils.substring((String) map.get("issn"), 0, 40);
      String original = StringUtils.substring((String) map.get("original"), 0, 200);
      Long jnlId = this.getPubAllByJnlId(original, issn);
      map.put("jnlId", jnlId);
      String journalNameZh = "";
      String journalNameEn = "";
      if (jnlId != null && jnlId != 0L) {
        BaseJournal2 bs2 = this.baseJournalDao.getBaseJournal2Title(jnlId);
        if (bs2 != null) {
          journalNameZh = bs2.getTitleXx();
          journalNameEn = bs2.getTitleEn();
        }
      }
      String zhBrief = this.generateBrief(xmlData, "zh", journalNameZh, journalNameEn);
      String enBrief = this.generateBrief(xmlData, "en", journalNameZh, journalNameEn);
      map.put("zhBrief", zhBrief);
      map.put("enBrief", enBrief);
      return map;
    } catch (Exception e) {
      logger.error("解析成果XML", e);
      throw new ServiceException("解析成果XML", e);
    }
  }

  @Override
  public PdwhPublication constructPdwhPub(Map<String, Object> xmlMap) {
    PdwhPublication pdwhPub = new PdwhPublication();
    pdwhPub.setArticleNo(StringUtils.substring((String) xmlMap.get("article_number"), 0, 20));
    pdwhPub.setAuthorName(XmlUtil
        .formatPubAuthorKws(XmlUtil.cleanAuthorsAddr(XmlUtil.subStr500char((String) xmlMap.get("author_names")))));
    pdwhPub.setAuthorNameSpec(XmlUtil.formatPubAuthorKws(
        XmlUtil.cleanAuthorsAddr(XmlUtil.subStr500char((String) xmlMap.get("authors_names_spec")))));
    pdwhPub.setConfName(StringUtils.substring((String) xmlMap.get("proceeding_title"), 0, 200));
    pdwhPub.setDbId((Integer) xmlMap.get("dbId"));
    pdwhPub.setDoi(StringUtils.substring((String) xmlMap.get("doi"), 0, 100));
    pdwhPub.setZhBriefDesc(StringUtils.substring((String) xmlMap.get("zhBrief"), 0, 500));
    pdwhPub.setEnBriefDesc(StringUtils.substring((String) xmlMap.get("enBrief"), 0, 500));
    pdwhPub.setStartPage(StringUtils.substring((String) xmlMap.get("start_page"), 0, 50));
    pdwhPub.setEndPage(StringUtils.substring((String) xmlMap.get("end_page"), 0, 50));
    pdwhPub.setZhTitle(HtmlUtils.Html2Text(StringUtils.substring((String) xmlMap.get("ctitle"), 0, 500)));
    pdwhPub.setEnTitle(HtmlUtils.Html2Text(StringUtils.substring((String) xmlMap.get("etitle"), 0, 500)));
    pdwhPub.setIsbn(StringUtils.substring((String) xmlMap.get("isbn"), 0, 40));
    pdwhPub.setIssn(StringUtils.substring((String) xmlMap.get("issn"), 0, 40));
    pdwhPub.setIssue(StringUtils.substring((String) xmlMap.get("issue"), 0, 20));
    pdwhPub.setVolume(StringUtils.substring((String) xmlMap.get("volume"), 0, 20));
    pdwhPub.setArticleNo(StringUtils.substring((String) xmlMap.get("article_number"), 0, 50));
    pdwhPub.setPatentNo(StringUtils.substring((String) xmlMap.get("patent_no"), 0, 100));
    pdwhPub.setPatentOpenNo(StringUtils.substring((String) xmlMap.get("patent_open_no"), 0, 100));
    pdwhPub.setPubType((Integer) xmlMap.get("pub_type"));
    pdwhPub.setPubYear((Integer) xmlMap.get("pubyear"));
    pdwhPub.setSourceId(StringUtils.substring((String) xmlMap.get("source_id"), 0, 100));
    pdwhPub.setOriginal(StringUtils.substring((String) xmlMap.get("original"), 0, 200));

    // SCM-17436
    /**
     * 关键词长度达到500字符截断时，需要找到保留中的最后一个分号，只保留分号前面的词，分号后多余的字符不需要保留。 如：……XX;yy;y500x;xxx;yyy…… 则保留的词是……xx和yy
     */
    pdwhPub.setZhKeywords(XmlUtil.formatPubAuthorKws(XmlUtil.subStr500char((String) xmlMap.get("ckeywords"))));
    pdwhPub.setEnKeywords(XmlUtil.formatPubAuthorKws(XmlUtil.subStr500char((String) xmlMap.get("ekeywords"))));
    pdwhPub.setOrganization((String) xmlMap.get("organization"));// SCM-18043,保存到pdwh_pub_addr前不截取
    pdwhPub.setEnAbstract(StringUtils.substring((String) xmlMap.get("eabstract"), 0, 1000));
    pdwhPub.setZhAbstract(StringUtils.substring((String) xmlMap.get("cabstract"), 0, 1000));
    pdwhPub.setJnlId((Long) xmlMap.get("jnlId"));
    pdwhPub.setPatentCategory((Integer) xmlMap.get("patent_category"));
    pdwhPub.setFundInfo(StringUtils.substring((String) xmlMap.get("fundinfo"), 0, 1000));
    this.fillHashValue(pdwhPub);
    return pdwhPub;
  }

  private void fillHashValue(PdwhPublication pdwhPub) {
    if (pdwhPub == null) {
      return;
    }

    String zhTitle = PubHashUtils.cleanTitle(pdwhPub.getZhTitle());
    String enTitle = PubHashUtils.cleanTitle(pdwhPub.getEnTitle());
    String pubYear = String.valueOf(pdwhPub.getPubYear());
    String pubType = String.valueOf(pdwhPub.getPubType());
    String[] unionValues = new String[] {zhTitle, enTitle, pubYear, pubType};
    String[] titleValues = new String[] {zhTitle, enTitle};

    Long unionHashValue = PubHashUtils.fingerPrint(unionValues) == null ? 0L : PubHashUtils.fingerPrint(unionValues);
    Long titleHashValue = PubHashUtils.fingerPrint(titleValues) == null ? 0L : PubHashUtils.fingerPrint(titleValues);
    Long enTitleHash = HashUtils.getStrHashCode(enTitle);
    Long zhTitleHash = HashUtils.getStrHashCode(zhTitle);
    Long doiHash = PubHashUtils.cleanDoiHash(pdwhPub.getDoi());
    Long sourceIdHash = HashUtils.getStrHashCode(XmlUtil.getTrimBlankLower(pdwhPub.getSourceId()));
    Long patentNoHash = PubHashUtils.cleanPatentNoHash(pdwhPub.getPatentNo());
    Long patentOpenNoHash = PubHashUtils.cleanPatentNoHash(pdwhPub.getPatentOpenNo());
    Long unitHashValue =
        PubHashUtils.getEnPubUnitFingerPrint(pdwhPub.getPubYear(), pdwhPub.getOriginal(), pdwhPub.getAuthorName());

    pdwhPub.setEnTitleHash(enTitleHash);
    pdwhPub.setZhTitleHash(zhTitleHash);
    pdwhPub.setDoiHash(doiHash);
    pdwhPub.setSourceIdHash(sourceIdHash);
    pdwhPub.setPatentNoHash(patentNoHash);
    pdwhPub.setPatentOpenNoHash(patentOpenNoHash);
    pdwhPub.setUnionHashValue(unionHashValue); // 中英文，出版年份，成果类型的hash值
    pdwhPub.setTitleHashValue(titleHashValue);// 中英文标题hash值
    pdwhPub.setUnitHash(unitHashValue);// 老基准库查重计算hash

  }

  /**
   * 保存成果关键字
   * 
   * @param pdwhPub
   * 
   */
  @Override
  public void saveKeywords(PdwhPublication pdwhPub, PdwhPubImportContext context) {
    // 获取正确的中英文关键字
    Map<String, Object> keywordMap = getKeywords(pdwhPub.getZhKeywords(), pdwhPub.getEnKeywords());
    String zhKeywords = keywordMap.get("zhKeywords") == null ? "" : (String) keywordMap.get("zhKeywords");
    String enKeyWords = keywordMap.get("enKeywords") == null ? "" : (String) keywordMap.get("enKeywords");
    Integer priorityFlag = context.getPriorityFlag() == null ? 0 : context.getPriorityFlag();
    Integer replaceFlag = context.getReplaceFlag() == null ? 0 : context.getReplaceFlag();

    if (StringUtils.isNotBlank(zhKeywords)) {
      // 关键字处理并保存 2代表中文关键字
      this.handlePubKeywords(pdwhPub.getPubId(), zhKeywords, 2, priorityFlag, replaceFlag);
      // 关键字拆分处理并保存，2代表中文关键字
      this.handlePubKeywordsSplit(pdwhPub.getPubId(), zhKeywords, 2, priorityFlag, replaceFlag);
    } else {
      if (context.getReplaceFlag() == 1) {
        pdwhPubKeywordsDao.deletePubKeywords(pdwhPub.getPubId(), 2);
        pdwhPubKeywordsSplitDao.deletePubKeywordsSplit(pdwhPub.getPubId(), 2);
      }
    }
    if (StringUtils.isNotBlank(enKeyWords)) {
      // //关键字处理并保存 1代表英文关键字
      this.handlePubKeywords(pdwhPub.getPubId(), enKeyWords, 1, priorityFlag, replaceFlag);
      // 关键字拆分处理并保存，1代表英文关键字
      this.handlePubKeywordsSplit(pdwhPub.getPubId(), enKeyWords, 1, priorityFlag, replaceFlag);
    } else {
      if (context.getReplaceFlag() == 1) {
        pdwhPubKeywordsDao.deletePubKeywords(pdwhPub.getPubId(), 1);
        pdwhPubKeywordsSplitDao.deletePubKeywordsSplit(pdwhPub.getPubId(), 1);
      }
    }
  }

  /**
   * 成果地址匹配单位
   *
   * @param pdwhPub
   *
   */
  @Override
  public void pubAddrMatch(PdwhPublication pdwhPub, PdwhPubImportContext context) {
    // 匹配单位
    Integer match = matchPubAddrsService.pubAddrMatchCache(context.getInsId(), pdwhPub.getPubId(),
        pdwhPub.getPubAddrs(), pdwhPub.getDbId());
    // 保存至BatchJobs
    if (match == 1) {
      this.saveToBatchJobs(pdwhPub.getPubId(), context.getInsId(), context.getDbId());
    }
  }

  @Override
  public ImportPubXmlDocument getPubXmlDocById(Long pubId) {
    PdwhPubXml xml = pdwhPubXmlDao.getpdwhPubXmlPubId(pubId);
    String xmlData = xml.getXml();
    if (org.apache.commons.lang.StringUtils.isBlank(xmlData)) {
      return null;
    }
    ImportPubXmlDocument document = null;
    try {
      document = new ImportPubXmlDocument(xmlData);
    } catch (Exception e) {
      logger.error("从成果xml中解析信息出错，pub_id:" + pubId, e);
      return null;
    }
    return document;
  }

  /**
   * 处理并保存成果关键字
   * 
   * @param pubId
   * @param Keywords
   * @param type
   */
  public void handlePubKeywords(Long pubId, String Keywords, int type, int priorityFlag, int replaceFlag) {
    try {
      if (pdwhPubKeywordsDao.getpubKeywords(pubId, type) != null) {
        if (priorityFlag == 1 || replaceFlag == 1) { // 如果优先级高priorityFlag，或者全部替代标志位replaceFlag==1，则替换新值
          pdwhPubKeywordsDao.deletePubKeywords(pubId, type);
          pdwhPubKeywordsDao.save(new PdwhPubKeywords(pubId, Keywords, type));
        }
      } else {
        // 保存关键字
        pdwhPubKeywordsDao.save(new PdwhPubKeywords(pubId, Keywords, type));
      }
    } catch (Exception e) {
      logger.error("handlePubKeywords处理保存关键词出错", e);
      throw new ServiceException("handlePubKeywords处理保存关键词出错", e);
    }

  }

  /**
   * 处理并保存成果拆分关键字
   * 
   * @param pubId
   * @param Keywords
   * @param type
   */
  public void handlePubKeywordsSplit(Long pubId, String Keywords, int type, int priorityFlag, int replaceFlag) {
    try {
      if (pdwhPubKeywordsSplitDao.getPubKeywordsSplit(pubId, type) != null) {
        if (priorityFlag == 1 || replaceFlag == 1) {
          pdwhPubKeywordsSplitDao.deletePubKeywordsSplit(pubId, type);
          this.savePdwhPubKeywordsSplit(pubId, Keywords, type);
        }
      } else {
        this.savePdwhPubKeywordsSplit(pubId, Keywords, type);
      }

    } catch (Exception e) {
      logger.error("handlePubKeywordsSplit处理保存关键词拆分出错", e);
      throw new ServiceException("handlePubKeywordsSplit处理保存保存关键词拆分出错", e);
    }
  }

  private void savePdwhPubKeywordsSplit(Long pubId, String Keywords, int type) {
    // 拆分关键字
    Set<String> keywords = XmlUtil.splitKeywords(Keywords);
    if (CollectionUtils.isEmpty(keywords)) {
      return;
    }
    for (String keyword : keywords) {
      keyword = StringUtils.substring(keyword, 0, 200);
      pdwhPubKeywordsSplitDao.save(
          new PdwhPubKeywordsSplit(pubId, keyword, keyword.toLowerCase(), PubHashUtils.getKeywordHash(keyword), type));
    }
  }

  public Map<String, Object> getKeywords(String zhKeywords, String enKeywords) {
    Map<String, Object> keywordsMap = new HashMap<String, Object>();
    // 英文关键字中包含中文且中文关键字不包含中文，交换，其他情况保留之前的
    if (XmlUtil.containZhChar(enKeywords) && !XmlUtil.containZhChar(zhKeywords)) {
      keywordsMap.put("zhKeywords", enKeywords);
      keywordsMap.put("enKeywords", zhKeywords);
    } else {
      keywordsMap.put("zhKeywords", zhKeywords);
      keywordsMap.put("enKeywords", enKeywords);
    }
    return keywordsMap;
  }

  /**
   * 判断中英文作者名是否放错了位置，放错了位置的话调换
   * 
   * @param enAuthorNames
   * @param zhAuthorNames
   * @return
   */
  public Map<String, String> swapAuthorNames(String authorNames, String authorsNamesSpec) {
    Map<String, String> authorMap = new HashMap<String, String>();
    if (StringUtils.isNotEmpty(authorNames) && StringUtils.isNotEmpty(authorsNamesSpec)) {
      // authorNames里面包含中文字符，并且authorsNamesSpec 只有英文字符
      if (XmlUtil.containZhChar(authorNames) && !XmlUtil.containZhChar(authorsNamesSpec)) {
        authorMap.put("zhAuthorNames", authorNames);
        authorMap.put("enAuthorNames", authorsNamesSpec);
      } else {
        authorMap.put("zhAuthorNames", authorsNamesSpec);
        authorMap.put("enAuthorNames", authorNames);
        // 都为中文,以authorsNamesSpec为准
        if (XmlUtil.containZhChar(authorNames) && XmlUtil.containZhChar(authorsNamesSpec)) {
          authorMap.put("zhAuthorNames", authorsNamesSpec);
          authorMap.put("enAuthorNames", "");
        }
        // 都为英文，以authorNames为准
        if (!XmlUtil.containZhChar(authorNames) && !XmlUtil.containZhChar(authorsNamesSpec)) {
          authorMap.put("zhAuthorNames", "");
          authorMap.put("enAuthorNames", authorNames);
        }
      }

    } else {
      if (XmlUtil.containZhChar(authorNames)) {
        authorMap.put("zhAuthorNames", authorNames);
        authorMap.put("enAuthorNames", authorsNamesSpec);
      } else if (StringUtils.isNotEmpty(authorNames)) {
        authorMap.put("zhAuthorNames", authorsNamesSpec);
        authorMap.put("enAuthorNames", authorNames);
      } else if (XmlUtil.containZhChar(authorsNamesSpec)) {
        authorMap.put("zhAuthorNames", authorsNamesSpec);
        authorMap.put("enAuthorNames", authorNames);
      } else if (StringUtils.isNotBlank(authorsNamesSpec)) {
        authorMap.put("zhAuthorNames", authorNames);
        authorMap.put("enAuthorNames", authorsNamesSpec);
      }
    }
    return authorMap;
  }

  /**
   * 新增保存基准库收录情况
   */
  @Override
  public PdwhPubSourceDb saveCitedInfo(PdwhPublication pdwhPub) throws ServiceException {
    Long pubId = pdwhPub.getPubId();
    PdwhPubXml pdwhPubXml = pdwhPubXmlDao.get(pubId);
    if (pdwhPubXml == null) {
      pdwhPubXml = new PdwhPubXml();
      pdwhPubXml.setPubId(pubId);
      pdwhPubXml.setXml(pdwhPub.getXmlString());
      pdwhPubXmlDao.save(pdwhPubXml);
    }
    PdwhPubSourceDb pubSourceDb = pdwhPubSourceDbDao.getPubSourceDb(pubId);
    if (pubSourceDb == null) {
      pubSourceDb = new PdwhPubSourceDb();
      pubSourceDb.setPubId(pubId);
    }
    return pubSourceDb;

  }

  /**
   * SCM-13118 更新引用计数
   * 
   * @param pubId
   */
  @Override
  public void handlePdwhCiteTimes(PdwhPublication pdwhPub) {
    Integer sysiteTimes = null;
    try {
      sysiteTimes = this.getSyscitedTimes(pdwhPub);
    } catch (Exception e) {
      logger.error("获取xml引用记录次数出错，pubid" + pdwhPub, e);
    }
    if (sysiteTimes == null) {
      sysiteTimes = 0;
    }

    this.saveCitedTimes(pdwhPub, sysiteTimes);
  }

  /**
   * 获取xml中引用值
   * 
   * @param currentPubId
   * @return
   * @throws Exception
   */
  public Integer getSyscitedTimes(PdwhPublication pdwhPub) throws Exception {
    String xmlString = pdwhPub.getXmlString();
    if (StringUtils.isEmpty(xmlString)) {
      logger.error("获取到的xml为空，pubId:" + pdwhPub.getPubId());
      return 0;
    }
    Integer dbId = pdwhPub.getDbId();
    // 清理XML数据
    Integer sysiteTimes = null;
    try {
      xmlString = xmlString.replaceAll("[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]", "");
      ImportPubXmlDocument doc = new ImportPubXmlDocument(xmlString);
      // 获取xml中引用次数，首先获取isi对应的引用次数,ei,cnki
      // 如果成果dbId = (15,16,17),32
      // 来自sci,ssci,istp,oalib;获取 cite_times
      if (dbId == 15 || dbId == 16 || dbId == 17 || dbId == 32) {
        sysiteTimes = doc.getCiteTimes();
      }
      // 如果成果dbId =4:cnki;获取 cnki_cite_time
      if (dbId == 4) {
        sysiteTimes = doc.getCnkiCiteTimes();
      }
      // 如果成果 dbId =21:cnkiPat;dbid=31:rainpat 获取cnipr_cite_times

      if (dbId == 21 || dbId == 31) {
        sysiteTimes = doc.getCniprCiteTimes();
      }
      // 如果成果dbId =14, EI; 获取 ei_cite_time
      if (dbId == 14) {
        sysiteTimes = doc.getEiCiteTimes();
      }
    } catch (NullPointerException e) {
      logger.error("获取到xml引用记录节点为空，PubId：" + pdwhPub.getPubId());
    }
    return sysiteTimes;

  }

  /**
   * 保存引用计数
   * 
   * @param currentPubId
   * @param sysiteTimes
   */
  public void saveCitedTimes(PdwhPublication pdwhPub, int sysiteTimes) {
    Integer dbId = pdwhPub.getDbId();
    // 引用计数表中 isi，dbId = (15,16,17)对应dbid统一记录为99，cnkipat,rainpat，dbId =
    // (21,31) 统一记录为98
    if (dbId == 15 || dbId == 16 || dbId == 17 || dbId == 32) {
      dbId = 99;
    }
    if (dbId == 21 || dbId == 31) {
      dbId = 98;
    }
    PdwhPubCitedTimes pdwhPubCitedTimes = pdwhPubCitedTimesDao.getcitesByPubDBId(pdwhPub.getPubId(), dbId);
    // 如果PdwhPubCitedTimes没有记录，则新增
    if (pdwhPubCitedTimes == null) {
      PdwhPubCitedTimes citedtimes = new PdwhPubCitedTimes();
      citedtimes.setPdwhPubId(pdwhPub.getPubId());
      citedtimes.setDbId(dbId);
      citedtimes.setCitedTimes(sysiteTimes);
      citedtimes.setUpdateDate(new Date());
      citedtimes.setType(0);// 后台更新
      pdwhPubCitedTimesDao.save(citedtimes);

    } else {
      // 存在记录，查询relation表记录数，如果小于则不更新
      Long pdwhCitedCount = 0L;
      Long count = pdwhCitedRelationDao.getPdwhCitedCount(pdwhPub.getPubId());
      if (count != null) {
        pdwhCitedCount = count;
      }
      if (sysiteTimes > pdwhCitedCount.intValue()) {
        pdwhPubCitedTimes.setCitedTimes(sysiteTimes);
        pdwhPubCitedTimes.setUpdateDate(new Date());
        pdwhPubCitedTimesDao.save(pdwhPubCitedTimes);
      }

    }

  }

  /**
   * 更新基准库收录情况
   */
  @Override
  public void updateCitedInfo(Integer DbId, PdwhPublication pdwhPub, PdwhPubXml pdwhPubXml) {
    /**
     * 如果成果dbId = (15,16,17),32, 来自sci, ssci, istp,oalib;更新cite_record_url, cite_times 如果成果dbId =
     * 4:cnki;更新cnki_cite_record_url, cnki_cite_time 如果成果 dbId = 21:cnkiPat; dbId = 31：rainpat
     * 更新cnipr_cite_record_url, cnipr_cite_times 如果成果dbId = 14, EI;更新ei_cite_record_url, ei_cite_time
     **/

    try {
      ImportPubXmlDocument newdocument = new ImportPubXmlDocument(pdwhPub.getXmlString());
      ImportPubXmlDocument olddocument = new ImportPubXmlDocument(pdwhPubXml.getXml());
      ImportPubXmlDocument document = null;
      if (DbId == 15 || DbId == 16 || DbId == 17 || DbId == 32) {
        document = this.updateXmlNode(newdocument, olddocument, "cite_times", "cite_record_url");
      }
      if (DbId == 4) {
        document = this.updateXmlNode(newdocument, olddocument, "cnki_cite_times", "cnki_cite_record_url");
      }
      if (DbId == 14) {
        document = this.updateXmlNode(newdocument, olddocument, "ei_cite_times", "ei_cite_record_url");
      }
      if (DbId == 21 || DbId == 31) {
        document = this.updateXmlNode(newdocument, olddocument, "cnipr_cite_times", "cnipr_cite_record_url");
        // cnipr_cite_record_url
      }

      if (document != null) {
        pdwhPubXml.setXml(document.getXmlString());
        pdwhPubXmlDao.save(pdwhPubXml);
      }

    } catch (Exception e) {
      logger.error("更新基准库收录情况出错", e);
    }

  }

  /**
   * 更新xml节点
   * 
   * @param newdocument新xml
   *        <p>
   * @param olddocument原始xml
   *        <p>
   * @param xmlNode1需要更新的节点1
   *        <p>
   * @param xmlNode2需要更新的节点2
   *        <p>
   * @return
   */

  public ImportPubXmlDocument updateXmlNode(ImportPubXmlDocument newdocument, ImportPubXmlDocument olddocument,
      String xmlNode1, String xmlNode2) {
    String newXmlNode1 = newdocument.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, xmlNode1);
    String newXmlNode2 = newdocument.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, xmlNode2);
    olddocument.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, xmlNode1, newXmlNode1);
    olddocument.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, xmlNode2, newXmlNode2);
    return olddocument;
  }

  @Override
  public void savePubSourceDb(PdwhPubSourceDb pubSourceDb) {
    pdwhPubSourceDbDao.save(pubSourceDb);
  }

  @Override
  public void savePubAddress(PdwhPublication pdwhPublication, PdwhPubImportContext context) throws ServiceException {
    if (pdwhPublication.getOrganization() != null) {
      /**
       * 原有成果地址orgnanization是中文，当前导入成果优先级高，且是英文则 保留原有的中文地址并移动到xml
       * orgnanization_spec节点下，英文放到orgnanization节点。 地址表pdwh_pub_addr同样保留原有的中文记录 ;
       * 
       * 如果在此基础上在导入一篇中文的地址成果 ,且优先级高 则只会更新orgnanization_spec中的地址信息，同时地址表中中文地址也会更新
       **/
      if (context.getPriorityFlag() == 1 && pdwhPublication.getOrganizationSpec() != null) {
        Map<String, String> addrMap = getPubAddress(pdwhPublication.getOrganizationSpec());
        handlePubAddress(pdwhPublication, pdwhPublication.getDbId(), addrMap);// 保存替换更新后的spec地址

        Map<String, String> srcAddrMap = getPubAddress(pdwhPublication.getOrganization());
        handlePubAddress(pdwhPublication, pdwhPublication.getDbId(), srcAddrMap);// 保存交换位置后的新的org节点英文地址

      } else {
        Map<String, String> addrMap = getPubAddress(pdwhPublication.getOrganization());
        handlePubAddress(pdwhPublication, pdwhPublication.getDbId(), addrMap);
      }

    }
    // 基准库成果新增更新任务结束，插入记录跑成果地址和作者匹配任务
    if (StringUtils.isBlank(pdwhPublication.getOrganization())
        && StringUtils.isBlank(pdwhPublication.getOrganizationSpec())) {
      logger.info("成果地址信息为空，不进行处理地址作者信息匹配任务，pubId:" + pdwhPublication.getPubId());
      return;
    }
    try {
      BatchJobs job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.MATCH_PDWHPUB_ADRR_AUTHOR,
          BatchJobUtil.getPdwhAddrAuthMatchContext(pdwhPublication.getPubId() + "", context.getOperation()),
          BatchWeightEnum.C.toString());
      batchJobsService.saveJob(job);
    } catch (CreateBatchJobException e) {
      throw new ServiceException(e);
    }

  }

  /**
   * 处理成果地址
   * 
   * @param pubId
   * @param dbId
   * @param map
   */
  void handlePubAddress(PdwhPublication pdwhPublication, Integer dbId, Map<String, String> map) {
    Set<String> pubAddrSet = new HashSet<String>();
    int language = 1;
    if (dbId == 4 || dbId == 14 || dbId == 21 || dbId == 11) {
      // cnki || ei || cnkipat的成果用这个方法去解析
      pubAddrSet = XmlUtil.parseCnkiPubAddrs(map.get("organization"));
    } else if (dbId == 8 || dbId == 15 || dbId == 16 || dbId == 17 || dbId == 10) {
      // isi的成果用这个方法去解析
      pubAddrSet = XmlUtil.parseIsiPubAddrs(map.get("organization"));
    } else {
      // 其他没有定义的都暂时用isi的拆分
      pubAddrSet = XmlUtil.parseIsiPubAddrs(map.get("organization"));
    }
    if (CollectionUtils.isEmpty(pubAddrSet)) {
      return;
    }
    if ("zh".equals(map.get("language"))) {
      // 中文为2
      language = 2;
      pdwhPubAddrDao.deletePdwhPubAddr(pdwhPublication.getPubId(), 2);

    } else {
      // 英文为1
      language = 1;
      pdwhPubAddrDao.deletePdwhPubAddr(pdwhPublication.getPubId(), 1);
    }
    List<PdwhPubAddr> pubAddrs = new ArrayList<PdwhPubAddr>();
    for (String pubAddr : pubAddrSet) {
      PdwhPubAddr pdwhPubAddr = new PdwhPubAddr(pdwhPublication.getPubId(), StringUtils.substring(pubAddr, 0, 500),
          PubHashUtils.cleanPubAddrHash(pubAddr), language);
      // 成果拆分地址不可为空
      if (pdwhPubAddr.getAddress() != null && pdwhPubAddr.getAddrHash() != null) {
        pdwhPubAddrDao.save(pdwhPubAddr);
        pubAddrs.add(pdwhPubAddr);
      }
    }
    pdwhPublication.setPubAddrs(pubAddrs);

  }

  Map<String, String> getPubAddress(String organization) {
    Map<String, String> addrMap = new HashMap<String, String>();
    if (XmlUtil.containZhChar(organization)) {
      addrMap.put("language", "zh");
      addrMap.put("organization", organization);
    } else {
      addrMap.put("language", "en");
      addrMap.put("organization", organization);
    }
    return addrMap;

  }

  @Override
  public void updateXml(PdwhPublication newPub, PdwhPubImportContext context) throws Exception {
    PdwhPubXml pdwhPubXml = pdwhPubXmlDao.get(context.getDupPubId());
    if (pdwhPubXml == null) {
      return;
    }
    if (StringUtils.isBlank(pdwhPubXml.getXml()) || StringUtils.isBlank(newPub.getXmlString())) {
      logger.info("新导入的成果的xml为空或者查重的成果xml为空，pubId=" + newPub.getPubId() + ",dupId=" + context.getDupPubId());
      return;
    }
    ImportPubXmlDocument olddocument = new ImportPubXmlDocument(pdwhPubXml.getXml());
    ImportPubXmlDocument newdocument = new ImportPubXmlDocument(newPub.getXmlString());
    if (context.getReplaceFlag() == 1) {// 完全替换
      pdwhPubXml.setXml(newPub.getXmlString());
      pdwhPubXmlDao.save(pdwhPubXml);
      return;
    } else {
      updateCommonXmlInfo(olddocument, newdocument, context);

      if (context.getPriorityFlag().equals(1)) {
        // updateOtherXml(olddocument, newdocument, context);
        // addXmlInfo(olddocument, newdocument);
        updatePubAuthor(olddocument, newdocument);
        /**
         * 原有成果地址orgnanization是中文，当前导入成果优先级高，且是英文则 保留原有的中文地址并移动到xml
         * orgnanization_spec节点下，英文放到orgnanization节点。 地址表pdwh_pub_addr同样保留原有的中文记录 ;
         * 
         * 如果在此基础上在导入一篇中文的地址成果 ,且优先级高 则只会更新orgnanization_spec中的地址信息，同时地址表中中文地址也会更新
         */

        // 1.当原有成果地址orgnanization是中文，导入为英文时
        if (!XmlUtil.containZhChar(newPub.getOrganization()) && XmlUtil.containZhChar(olddocument.getOrgnization())) {

          olddocument.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "orgnanization_spec",
              olddocument.getOrgnization());
          olddocument.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "organization",
              newPub.getOrganization());
        }

        // 2当存在orgspec节点 且导入为中文地址时
        else if (XmlUtil.containZhChar(newPub.getOrganization())
            && XmlUtil.containZhChar(olddocument.getOrgnizationSpec())) {
          olddocument.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "organization_spec",
              newPub.getOrganization());
        }
        // 其他情况替换不变
        else {
          String newNodeValue =
              newdocument.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "orgnanization");
          if (StringUtils.isNotBlank(newNodeValue)) {
            olddocument.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "organization", newNodeValue);
          }
        }

      }
      pdwhPubXml.setXml(olddocument.getXmlString());
      pdwhPubXmlDao.save(pdwhPubXml);
    }

  }

  void updateOtherXml(ImportPubXmlDocument olddocument, ImportPubXmlDocument newdocument,
      PdwhPubImportContext context) {
    for (String node : olddocument.getOtherXmlList()) {
      String newNodeValue = newdocument.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, node);
      olddocument.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, node, newNodeValue);
    }
  }

  /**
   * 更新作者
   * 
   * @param olddocument
   * @param newdocument
   */
  void updatePubAuthor(ImportPubXmlDocument olddocument, ImportPubXmlDocument newdocument) {
    olddocument.removeNode("/pub_authors");
    // 更新所有作者信息
    List authorList = newdocument.getPubAuthorList();
    if (authorList != null && authorList.size() > 0) {
      olddocument.removeNode("/pub_authors");
      Element authorEles = olddocument.createElement("/pub_authors");
      for (int i = 0; i < authorList.size(); i++) {
        Element newAuthor = authorEles.addElement("author");
        Element author = (Element) authorList.get(i);
        olddocument.copyPubElement(newAuthor, author);
      }
    }
  }

  /**
   * 更新普通节点
   * 
   * @param olddocument
   * @param newdocument
   * @param context
   */
  void updateCommonXmlInfo(ImportPubXmlDocument olddocument, ImportPubXmlDocument newdocument,
      PdwhPubImportContext context) {
    for (String commonNode : olddocument.getCommonXmlList()) {
      String oldNodeValue = olddocument.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, commonNode);
      String newNodeValue = newdocument.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, commonNode);
      if (StringUtils.isBlank(oldNodeValue)) {
        olddocument.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, commonNode, newNodeValue);
      } else if (context.getPriorityFlag().equals(1) && StringUtils.isNotBlank(newNodeValue)) {
        if (commonNode.equals("organization")) {
          continue; // 单位替换移动到后续操作
        }
        olddocument.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, commonNode, newNodeValue);// 旧的不为空，新导入的为空的时候，保留旧值
      }
    }
  }

  @Override
  public void saveToBatchJobs(Long pubId, Long insId, Integer dbid) throws ServiceException {
    try {

      BatchJobs job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.PUB_ASSIGN_FOR_ROL,
          BatchJobUtil.getDbSourceContext(pubId + "", insId + "", dbid + ""), BatchWeightEnum.D.toString());
      batchJobsService.saveJob(job);
    } catch (CreateBatchJobException e) {
      throw new ServiceException(e);
    }

  }

  /**
   * 更新其他的节点
   * 
   * @param olddocument
   * @param newdocument
   */
  void addXmlInfo(ImportPubXmlDocument olddocument, ImportPubXmlDocument newdocument) {
    for (String xmlNode : olddocument.getXmlAttrSpecList()) {
      String newNodeValue = newdocument.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, xmlNode);
      if (StringUtils.isNotBlank(newNodeValue)) {
        olddocument.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, xmlNode, newNodeValue);
      }

    }

  }

  private String generateBrief(String xmlString, String language) {
    String brief = "";
    try {
      PubXmlDocument doc = new PubXmlDocument(xmlString);
      brief = PdwhBriefUtils.getBrief(doc, language);

    } catch (DocumentException e) {
      logger.error("pdwhPubImport 生成成果brief错误，" + e);
    }

    return brief;
  }

  private String generateBrief(String xmlString, String language, String baseJournalNameZh, String baseJournalNameEn) {
    String brief = "";
    try {
      PubXmlDocument doc = new PubXmlDocument(xmlString);
      brief = PdwhBriefUtils.getBrief(doc, language, baseJournalNameZh, baseJournalNameEn);

    } catch (DocumentException e) {
      logger.error("pdwhPubImport 生成成果brief错误，" + e);
    }

    return brief;
  }

  @Override
  public void updateAuthorInfo(PdwhPublication pdwhPub, PdwhPubImportContext context) throws Exception {
    PdwhPubXml pdwhPubXml = pdwhPubXmlDao.get(context.getDupPubId());
    if (pdwhPubXml == null) {
      return;
    }
    String oldXml = pdwhPubXml.getXml();
    String newXml = pdwhPub.getXmlString();
    if (StringUtils.isEmpty(newXml) || StringUtils.isEmpty(oldXml)) {
      return;
    }
    ImportPubXmlDocument oldDoc = new ImportPubXmlDocument(oldXml);
    ImportPubXmlDocument newDoc = new ImportPubXmlDocument(newXml);
    String authorNames = newDoc.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "author_names");
    String authorsNamesSpec = newDoc.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "authors_names_spec");
    String oldAuthorNames = oldDoc.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "author_names");
    String oldAuthorNamesSpec =
        oldDoc.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "authors_names_spec");
    Map<String, String> authorMap = swapAuthorNames(authorNames, authorsNamesSpec);
    if (context.getReplaceFlag() == 1) {// 完全替换重复的成果
      pdwhPubXml.setXml(pdwhPub.getXmlString());
      pdwhPubXmlDao.save(pdwhPubXml);
      return;
    }
    // 如果不覆盖，并且dbId是4,也要判断新导入的中文作者名是否为空，不为空的话需要把中文作者加到xml中的authors_names_spec节点
    if (context.getPriorityFlag() == 0) {
      if (StringUtils.isBlank(oldAuthorNames) && StringUtils.isBlank(oldAuthorNamesSpec)) {
        if (StringUtils.isNotEmpty(authorMap.get("enAuthorNames"))) {
          oldDoc.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "authors_names_spec",
              authorMap.get("zhAuthorNames"));
          oldDoc.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "author_names",
              authorMap.get("enAuthorNames"));
        } else {
          oldDoc.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "author_names",
              authorMap.get("zhAuthorNames"));
          oldDoc.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "authors_names_spec",
              authorMap.get("enAuthorNames"));
        }
      }

      if (StringUtils.isBlank(oldAuthorNames) && StringUtils.isNotBlank(oldAuthorNamesSpec)) {
        if (XmlUtil.containZhChar(oldAuthorNamesSpec)) {
          if (StringUtils.isNotEmpty(authorMap.get("enAuthorNames"))) {
            oldDoc.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "author_names",
                authorMap.get("enAuthorNames"));
          } else {
            oldDoc.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "author_names", oldAuthorNamesSpec);
            oldDoc.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "authors_names_spec", "");
          }
        } else {
          oldDoc.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "author_names", oldAuthorNamesSpec);
          oldDoc.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "authors_names_spec",
              authorMap.get("zhAuthorNames"));
        }
      }

      if (StringUtils.isBlank(oldAuthorNamesSpec) && StringUtils.isNotBlank(oldAuthorNames)) {

        if (XmlUtil.containZhChar(oldAuthorNames)) {
          if (StringUtils.isNotEmpty(authorMap.get("enAuthorNames"))) {
            oldDoc.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "author_names",
                authorMap.get("enAuthorNames"));
            oldDoc.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "authors_names_spec", oldAuthorNames);
          }
        } else {
          oldDoc.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "authors_names_spec",
              authorMap.get("zhAuthorNames"));
        }

      }

    }

    /**
     * 用新导入的xml中对应的中英文作者替换重复节点中author_names(放作者的英文名称),authors_names_spec( 放作者的中文名称 )的值，
     * 如果重复成果中的author_names为中文的话需要调换放到authors_names_spec中，具体做法如下
     */

    if (context.getPriorityFlag() == 1) {
      if (StringUtils.isBlank(authorMap.get("enAuthorNames")) && StringUtils.isNotBlank(authorMap.get("zhAuthorNames"))
          && (StringUtils.isBlank(oldAuthorNames) || XmlUtil.containZhChar(oldAuthorNames))
          && StringUtils.isBlank(oldAuthorNamesSpec)) {
        // 只有对新老xml中author_names为中文，authors_names_spec为空的情况，则只更新中文信息，不对调；
        oldDoc.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "author_names",
            authorMap.get("zhAuthorNames"));
      } else {
        // 其他情况，如下处理
        if (StringUtils.isNotBlank(authorMap.get("enAuthorNames"))) {
          oldDoc.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "author_names",
              authorMap.get("enAuthorNames"));
          if (!XmlUtil.containZhChar(oldAuthorNamesSpec)) {
            oldDoc.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "authors_names_spec", "");
          }
        }

        if (StringUtils.isNotBlank(oldAuthorNames) && XmlUtil.containZhChar(oldAuthorNames)) {
          oldDoc.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "authors_names_spec", oldAuthorNames);
        }

        if (StringUtils.isNotBlank(authorMap.get("zhAuthorNames"))) {
          oldDoc.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "authors_names_spec",
              authorMap.get("zhAuthorNames"));
          if (StringUtils
              .isBlank(oldDoc.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "author_names"))) {// 如果英文为空，则调换位置
            oldDoc.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "author_names",
                authorMap.get("zhAuthorNames"));
            oldDoc.setXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "authors_names_spec", "");
          }
        }
      }
    }
    pdwhPubXml.setXml(oldDoc.getXmlString());
    // 更新pdwh_publication表的author_names,authors_names_spec
    PdwhPublication oldPdwhPub = pdwhPublicationDao.get(pdwhPub.getPubId());
    if (oldPdwhPub != null) {
      oldPdwhPub.setAuthorName(XmlUtil.formatPubAuthorKws(XmlUtil.cleanAuthorsAddr(
          XmlUtil.subStr500char(oldDoc.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "author_names")))));
      oldPdwhPub.setAuthorNameSpec(XmlUtil.formatPubAuthorKws(XmlUtil.cleanAuthorsAddr(XmlUtil
          .subStr500char(oldDoc.getXmlNodeAttribute(ImportPubXmlConstants.PUBLICATION_XPATH, "authors_names_spec")))));
      this.pdwhPublicationDao.save(oldPdwhPub);
    }
    pdwhPubXmlDao.save(pdwhPubXml);

  }

  @Override
  public void savePdwhPublication(PdwhPublication pdwhPub) throws ServiceException {
    pdwhPub.setUpdateMark(1);
    this.pdwhPublicationDao.save(pdwhPub);
    String xmlString = pdwhPub.getXmlString();
    if (xmlString == null) {
      xmlString = "";
    }
    // 增加xml的申明
    String xmltop = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    if (StringUtils.indexOf(xmlString, xmltop) < 0) {
      xmlString = xmltop + xmlString;
    }
    PdwhPubXml pdwhPubXml = new PdwhPubXml();
    pdwhPubXml.setXml(xmlString);
    pdwhPubXml.setPubId(pdwhPub.getPubId());
    this.pdwhPubXmlDao.save(pdwhPubXml);
    Long pubId = pdwhPub.getPubId();
    // SCM-12850 生成短地址,基准库成果类型为S
    try {
      pubShortUrlSaveService.producePubShortUrl(pubId, ShortUrlConst.S_TYPE);
    } catch (Exception e) {
      logger.error("保存成果短地址出错！pubId:" + pubId, e);
    }
    Assert.notNull(pubId, "成果保存失败，pubId为空");
    try {
      this.savePdwhPubDup(pdwhPub);
    } catch (Exception e) {
      logger.error("Dao操作出现异常，pdwhPubID:" + pdwhPub.getPubId());
      // 回滚事务
      throw new ServiceException("Dao操作出现异常，新增操作出错：pdwhPubID:" + pdwhPub.getPubId(), e);
    }

  }

  public void savePdwhPubDup(PdwhPublication pdwhPub) throws DaoException {
    try {
      PdwhPubDup dupPub = this.warpDupPubInfo(pdwhPub);
      this.pdwhPubDupDao.save(dupPub);
    } catch (Exception e) {
      throw new DaoException();
    }

  }

  private PdwhPubDup warpDupPubInfo(PdwhPublication pdwhPub) {
    Integer dbId = pdwhPub.getDbId();
    Integer pubType = pdwhPub.getPubType();

    PdwhPubDup dupPub = new PdwhPubDup();

    if (PubXmlDbUtils.isCnkiDb(dbId)) {
      dupPub.setCnkiDoi(pdwhPub.getDoi());
      dupPub.setCnkiDoiHash(pdwhPub.getDoiHash());
    }

    if (PubXmlDbUtils.isIsiDb(dbId)) {
      dupPub.setDoi(pdwhPub.getDoi());
      dupPub.setDoiHash(pdwhPub.getDoiHash());
      dupPub.setIsiSourceId(pdwhPub.getSourceId());
      dupPub.setIsiSourceIdHash(pdwhPub.getSourceIdHash());
    }

    if (PubXmlDbUtils.isEiDb(dbId)) {
      dupPub.setDoi(pdwhPub.getDoi());
      dupPub.setDoiHash(pdwhPub.getDoiHash());
      dupPub.setEiSourceId(pdwhPub.getSourceId());
      dupPub.setEiSourceIdHash(pdwhPub.getSourceIdHash());
    }
    // SCM-13338
    if (PubXmlDbUtils.isRainPatDb(dbId)) {
      dupPub.setDoi(pdwhPub.getDoi());
      dupPub.setDoiHash(pdwhPub.getDoiHash());
    }
    // SCM-16309
    if (PubXmlDbUtils.isOalibDb(dbId)) {
      dupPub.setDoi(pdwhPub.getDoi());
      dupPub.setDoiHash(pdwhPub.getDoiHash());
    }

    dupPub.setPubType(pubType);
    dupPub.setPubYear(pdwhPub.getPubYear());
    dupPub.setPatentNoHash(pdwhPub.getPatentNoHash());
    dupPub.setPatentOpenNoHash(pdwhPub.getPatentOpenNoHash());
    dupPub.setTitleHash(pdwhPub.getTitleHashValue());
    dupPub.setUnionHashKey(pdwhPub.getUnionHashValue());
    dupPub.setZhTitleHash(pdwhPub.getZhTitleHash());
    dupPub.setEnTitleHash(pdwhPub.getEnTitleHash());
    dupPub.setPubId(pdwhPub.getPubId());
    return dupPub;
  }

  @Override
  public void updatePdwhPubInfo(PdwhPublication newPdwhPub, PdwhPubImportContext context) throws Exception {
    Integer priorityFlag = context.getPriorityFlag();
    Long oldPubId = context.getDupPubId();
    PdwhPublication oldPdwhPub = this.pdwhPublicationDao.get(oldPubId);
    if (oldPdwhPub == null) {
      newPdwhPub.setUpdateMark(1);
      newPdwhPub.setPubId(oldPubId);
      newPdwhPub.setCreateDate(context.getNow());
      newPdwhPub.setUpdateDate(context.getNow());
      newPdwhPub.setCreatePsnId(context.getCurrentPsnId());
      newPdwhPub.setUpdatePsnId(context.getCurrentPsnId());
      this.pdwhPublicationDao.save(newPdwhPub);
    } else {
      oldPdwhPub.setUpdateMark(1);
      // this.updatePdwhPublication(oldPdwhPub, newPdwhPub, priorityFlag);
      this.updatePdwhPub(oldPdwhPub, newPdwhPub, priorityFlag);
      oldPdwhPub.setUpdateDate(context.getNow());
      oldPdwhPub.setUpdatePsnId(context.getCurrentPsnId());
      this.pdwhPublicationDao.save(oldPdwhPub);
      // 为更新时为成果填充重复成果id
      newPdwhPub.setPubId(oldPubId);
    }
  }

  @Override
  public void updatePdwhDupPubInfo(PdwhPublication newPdwhPub, PdwhPubImportContext context) throws Exception {
    Integer priorityFlag = context.getPriorityFlag();
    Long oldPubId = context.getDupPubId();
    PdwhPubDup oldDupPub = this.pdwhPubDupDao.get(oldPubId);
    if (oldDupPub == null) {
      PdwhPubDup newPdwhPubDup = this.warpDupPubInfo(newPdwhPub);
      newPdwhPubDup.setPubId(oldPubId);
      this.pdwhPubDupDao.save(newPdwhPubDup);
    } else {
      this.updatePdwhPubDup(oldDupPub, newPdwhPub, priorityFlag);
    }
  }

  private void updatePdwhPub(PdwhPublication oldPdwhPub, PdwhPublication newPdwhPub, Integer priorityFlag)
      throws Exception {
    // 需要更新的值
    String[] includePropertyNames = {"dbId", "authorName", "authorNameSpec", "zhTitle", "enTitle", "zhBriefDesc",
        "enBriefDesc", "zhKeywords", "enKeywords", "jnlId", "pubType", "pubYear", "organization", "isbn", "issn",
        "issue", "volume", "startPage", "endPage", "articleNo", "patentNo", "patentOpenNo", "confName"};

    for (String propertyName : includePropertyNames) {
      PropertyDescriptor pdwhPubProperty = BeanUtils.getPropertyDescriptor(oldPdwhPub.getClass(), propertyName);
      Method oldWriteMethod = pdwhPubProperty.getWriteMethod();
      Method oldReadMethod = pdwhPubProperty.getReadMethod();

      if (oldReadMethod == null || oldWriteMethod == null) {
        continue;
      }

      Object value = oldReadMethod.invoke(oldPdwhPub);
      // 更新值为空的时候
      if (value == null || String.valueOf(value) == "" || String.valueOf(value) == "0") {
        PropertyDescriptor newPdwhPubProperty = BeanUtils.getPropertyDescriptor(newPdwhPub.getClass(), propertyName);
        Method newReadMethod = newPdwhPubProperty.getReadMethod();
        if (newReadMethod == null) {
          continue;
        }
        Object newValue = newReadMethod.invoke(newPdwhPub);
        oldWriteMethod.invoke(oldPdwhPub, newValue);
      } else if (priorityFlag == 1) {// 替换操作
        PropertyDescriptor newPdwhPubProperty = BeanUtils.getPropertyDescriptor(newPdwhPub.getClass(), propertyName);
        Method newReadMethod = newPdwhPubProperty.getReadMethod();
        if (newReadMethod == null) {
          continue;
        }
        Object newValue = newReadMethod.invoke(newPdwhPub);
        if (newValue != null && String.valueOf(newValue) != "" && String.valueOf(newValue) != "0") {
          oldWriteMethod.invoke(oldPdwhPub, newValue);
        }
      }
    }
  }

  private void updatePdwhPubDup(PdwhPubDup oldDupPub, PdwhPublication newPdwhPub, Integer priorityFlag)
      throws Exception {
    PdwhPubDup newDupPub = this.warpDupPubInfo(newPdwhPub);
    Long pubId = oldDupPub.getPubId();
    PropertyDescriptor[] pdwhPubproperties = BeanUtils.getPropertyDescriptors(oldDupPub.getClass());
    for (PropertyDescriptor property : pdwhPubproperties) {
      Method readMethod = property.getReadMethod();
      Method writeMethod = property.getWriteMethod();
      if (readMethod == null || writeMethod == null) {
        continue;
      }
      Object value = readMethod.invoke(oldDupPub);

      // 如果旧值为空，则把新值补充至旧值中
      if (value == null || String.valueOf(value) == "" || String.valueOf(value) == "0") {
        // 获取对应新值中的属性的值，补充
        PropertyDescriptor newProperty = BeanUtils.getPropertyDescriptor(newDupPub.getClass(), property.getName());
        Method newReadMethod = newProperty.getReadMethod();
        if (newReadMethod == null) {
          continue;
        }
        Object newValue = newReadMethod.invoke(newDupPub);
        writeMethod.invoke(oldDupPub, newValue);
      } else if (priorityFlag == 1) {
        PropertyDescriptor newProperty = BeanUtils.getPropertyDescriptor(newDupPub.getClass(), property.getName());
        Method newReadMethod = newProperty.getReadMethod();
        if (newReadMethod == null) {
          continue;
        }
        Object newValue = newReadMethod.invoke(newDupPub);
        // 对priorityFlag为1的，用不为空的新值覆盖对应旧值
        if (newValue != null && String.valueOf(value) != "" && String.valueOf(value) != "0") {
          writeMethod.invoke(oldDupPub, newValue);
        }
      }
    }

    // 重新赋值下，以防被替换
    oldDupPub.setPubId(pubId);
    this.pdwhPubDupDao.save(oldDupPub);

  }

  @Override
  public void handleInfo(PdwhPublication pdwhPub) {
    Long sourceIdHash = pdwhPub.getSourceIdHash();
    Long titleHashValue = pdwhPub.getTitleHashValue(); // 原基准库导入任务，cnki，wangfang，cnkipat的titleHash与此值一致（由entitle与zhtitle生成）
    Long enTitleHashValue = pdwhPub.getEnTitleHash();// 原基准库导入任务中，isi，ei,scopus
    // 分库的titleHash实际是enTitleHash;
    Long unitHashValue = pdwhPub.getUnitHash();
    Long dupId;
    Assert.isTrue(StringUtils.isNotBlank(pdwhPub.getXmlString()), "分库保存时xml不应为空！");

    if (PubXmlDbUtils.isIsiDb(pdwhPub.getDbId())) {
      dupId = isiPublicationService.getDupPub(sourceIdHash, enTitleHashValue, unitHashValue);
      if (dupId == null) {
        // 保存查重信息
        isiPublicationDao.saveIsiPubDup(pdwhPub.getPubId(), sourceIdHash, enTitleHashValue, unitHashValue);
        // 分库保存xml
        isiPublicationDao.saveIsiPubExtend(pdwhPub.getPubId(), pdwhPub.getXmlString());
      } else {
        // 保存新的xml
        isiPublicationDao.saveIsiPubExtend(dupId, pdwhPub.getXmlString());
      }
      return;
    }
    if (PubXmlDbUtils.isEiDb(pdwhPub.getDbId())) {
      dupId = eiPublicationService.getDupPub(sourceIdHash, enTitleHashValue, unitHashValue);
      if (dupId == null) {
        // 保存查重信息
        eiPublicationDao.saveEiPubDup(pdwhPub.getPubId(), sourceIdHash, enTitleHashValue, unitHashValue);
        // 分库保存xml
        eiPublicationDao.saveEiPubExtend(pdwhPub.getPubId(), pdwhPub.getXmlString());
      } else {
        // 保存新的xml
        eiPublicationDao.saveEiPubExtend(dupId, pdwhPub.getXmlString());
      }
      return;
    }
    if (PubXmlDbUtils.isCnkiDb(pdwhPub.getDbId())) {
      dupId = cnkiPublicationService.getDupPub(titleHashValue, unitHashValue);
      if (dupId == null) {
        // 保存查重信息
        cnkiPublicationDao.saveCnkiPubDup(pdwhPub.getPubId(), titleHashValue, unitHashValue);
        // 分库保存xml
        cnkiPublicationDao.saveCnkiPubExtend(pdwhPub.getPubId(), pdwhPub.getXmlString());
      } else {
        // 保存新的xml
        cnkiPublicationDao.saveCnkiPubExtend(dupId, pdwhPub.getXmlString());
      }
      return;
    }

    // cnkipat与cnipr统一至cnkipat表
    if (PubXmlDbUtils.isCnkipatDb(pdwhPub.getDbId()) || PubXmlDbUtils.isCNIPRDb(pdwhPub.getDbId())) {
      dupId =
          cnkiPatPublicationService.getDupPub(titleHashValue, pdwhPub.getPatentNoHash(), pdwhPub.getPatentOpenNoHash());
      if (dupId == null) {
        // 保存查重信息
        cnkiPatPublicationDao.saveCnkiPatPubDup(pdwhPub.getPubId(), titleHashValue, pdwhPub.getPatentNoHash(),
            pdwhPub.getPatentOpenNoHash());
        // 分库保存xml
        cnkiPatPublicationDao.saveCnkiPatPubExtend(pdwhPub.getPubId(), pdwhPub.getXmlString());
      } else {
        // 保存新的xml
        cnkiPatPublicationDao.saveCnkiPatPubExtend(dupId, pdwhPub.getXmlString());
      }
      return;
    }
    if (PubXmlDbUtils.isWanFangDb(pdwhPub.getDbId())) {
      dupId = wfPublicationService.getDupPub(titleHashValue, unitHashValue);
      if (dupId == null) {
        // 保存查重信息
        wfPublicationDao.saveWfPubDup(pdwhPub.getPubId(), titleHashValue, unitHashValue);
        // 分库保存xml
        wfPublicationDao.saveWfPubExtend(pdwhPub.getPubId(), pdwhPub.getXmlString());
      } else {
        // 保存新的xml
        wfPublicationDao.saveWfPubExtend(dupId, pdwhPub.getXmlString());
      }
      return;
    }
    if (PubXmlDbUtils.isScopusDb(pdwhPub.getDbId())) {
      dupId = spsPublicationService.getDupPub(sourceIdHash, enTitleHashValue, unitHashValue);
      if (dupId == null) {
        // 保存查重信息
        spsPublicationDao.saveSpsPubDup(pdwhPub.getPubId(), sourceIdHash, enTitleHashValue, unitHashValue);
        // 分库保存xml
        spsPublicationDao.saveSpsPubExtend(pdwhPub.getPubId(), pdwhPub.getXmlString());
      } else {
        // 保存新的xml
        spsPublicationDao.saveSpsPubExtend(dupId, pdwhPub.getXmlString());
      }
      return;
    }

    // SCM-13338
    if (PubXmlDbUtils.isRainPatDb(pdwhPub.getDbId())) {
      dupId =
          rainPatPublicationService.getDupPub(titleHashValue, pdwhPub.getPatentNoHash(), pdwhPub.getPatentOpenNoHash());
      if (dupId == null) {
        // 保存查重信息
        rainPatPublicationService.saveRainPatPubDup(pdwhPub.getPubId(), titleHashValue, pdwhPub.getPatentNoHash(),
            pdwhPub.getPatentOpenNoHash());
        // 分库保存xml
        rainPatPublicationService.saveRainPatPubExtend(pdwhPub.getPubId(), pdwhPub.getXmlString());
      } else {
        // 保存新的xml
        rainPatPublicationService.saveRainPatPubExtend(dupId, pdwhPub.getXmlString());
      }
      return;
    }
    // SCM-16309

    if (PubXmlDbUtils.isOalibDb(pdwhPub.getDbId())) {
      dupId = oalibPublicationService.getDupPub(titleHashValue, pdwhPub.getUnionHashValue(), pdwhPub.getSourceIdHash());
      if (dupId == null) {
        // 保存查重信息
        oalibPublicationService.saveOalibPubDup(pdwhPub.getPubId(), titleHashValue, pdwhPub.getUnionHashValue(),
            pdwhPub.getSourceIdHash());
        // 分库保存xml
        oalibPublicationService.saveOalibPubExtend(pdwhPub.getPubId(), pdwhPub.getXmlString());
      } else {
        // 保存新的xml
        oalibPublicationService.saveOalibPubExtend(dupId, pdwhPub.getXmlString());
      }
      return;
    }
  }

  /**
   * 获取pdwh_publication中期刊数据对应匹配的基准期刊id.
   * 
   * @param original ,issn
   * @return
   * @throws ServiceException
   */
  private Long getPubAllByJnlId(String original, String issn) {
    Long baseJnlId = null;
    try {
      // 获取匹配的基础期刊Id
      String jissn = XmlUtil.buildStandardIssn(issn);
      original = XmlUtil.changeSBCChar(original);
      if (StringUtils.isNotBlank(original) && StringUtils.isNotBlank(jissn)) {
        baseJnlId = baseJournalTitleDao.PdwhPubAllJournalMatchBaseJnlId(original, jissn);
      }
    } catch (Exception e) {
      logger.error("获取pdwh_publication表对应期刊数据相匹配的基准库期刊id出错", e);
    }
    return baseJnlId;
  }

  @Override
  public List<TmpPublicationAll> getTmpPublicationAllList(Integer size) {
    List<TmpPublicationAll> pdwhPubList = this.tmpPublicationAllDao.getToHandleList(size);
    return pdwhPubList;
  }

  @Override
  public PdwhPublication getPdwhPubById(Long pubId) {
    return pdwhPublicationDao.getPdwhPubById(pubId);
  }

  @Override
  public List<TmpPdwhPubAddr> getToHandleList(Long minPubId, Long maxPubId) {
    return tmpPdwhPubAddrDao.getToHandleList(minPubId, maxPubId);
  }

  @Override
  public void saveTmpPdwhPubAddr(TmpPdwhPubAddr tmpPdwhPubAddr) {
    this.tmpPdwhPubAddrDao.save(tmpPdwhPubAddr);
  }

  // 前边必须包含空格
  String[] level0Ins = {" hospital", " hosp"};
  String[] level1Ins = {" univ", "univ ", " university", "university ", " coll", "coll ", " college", "college ",
      " acad", "acad ", " academy", "academy ", " inst", " institute", "inst ", "institute ", " group", "group ",
      " co.", " co. ltd", " co ltd", " company", " hospital", " hosp", "hospital ", "hosp ", " research center",
      " research centre", "center", "centre", " lab", " laboratory", " administration"};
  String[] level2Ins = {" inst", " institute", " group", " co.", " co. ltd", " co ltd"};
  String[] level3Ins = {" research center", " lab", " laboratory", " administration"};

  // 1完成获取地址；2
  @Override
  public Integer extractInstitutionAddr(TmpPdwhPubAddr pubAddr) throws Exception {
    String addrStr = pubAddr.getAddress();
    Integer language = pubAddr.getLanguage();
    Long oldAddrId = pubAddr.getAddrId();
    Integer rsStatus = 2;
    if (StringUtils.isEmpty(addrStr)) {
      return rsStatus;
    }
    if (tmpPdwhPubAddrCleanDao.getTmpPdwhPubAddrCleanCount(oldAddrId) > 0) {
      this.tmpPdwhPubAddrCleanDao.deleteByAddrId(oldAddrId);
    }
    if (language == 1) {
      String[] addrs = addrStr.split(";");
      for (String addr : addrs) {
        if (StringUtils.isEmpty(addr) || addr.length() < 5) {
          continue;
        } else {
          Map<String, String> addrMap = this.splitEnAddr(addr);
          // 存储解压后的数据;2获取的地址为空，1成功获取到地址
          Integer rs = this.saveNewAddr(pubAddr, StringUtils.trimToEmpty(addrMap.get("address")),
              StringUtils.trimToEmpty(addrMap.get("address1")), StringUtils.trimToEmpty(addrMap.get("address2")));
          if (rs == 1) {
            rsStatus = 1;
          }
        }
      }

    } else {
      String[] addrs = addrStr.split(";");
      for (String addr : addrs) {
        if (StringUtils.isEmpty(addr) || addr.length() < 3) {
          continue;
        } else {
          // 存储解压后的数据
          String[] zhAddrs = addr.split(",");
          for (String zhAddr : zhAddrs) {
            Integer rs = this.saveNewAddr(pubAddr, StringUtils.trimToEmpty(zhAddr), null, null);
            if (rs == 1) {
              rsStatus = 1;
            }
          }
        }
      }
    }
    return rsStatus;
  }

  private Integer saveNewAddr(TmpPdwhPubAddr pubAddrOld, String addr, String addr1, String addr2) {
    if (StringUtils.isEmpty(addr)) {// 如果核心地址为空，返回
      return 2;
    }
    addr = addr.replaceAll("\\.", "");
    if (StringUtils.isEmpty(addr)) {// 如果核心地址为空，返回
      return 2;
    }
    TmpPdwhPubAddrClean newAddr = new TmpPdwhPubAddrClean();
    newAddr.setAddrId(pubAddrOld.getAddrId());
    newAddr.setPubId(pubAddrOld.getPubId());
    newAddr.setAddress(addr);
    if (StringUtils.isNotEmpty(addr1)) {
      newAddr.setAddress1(addr1);
    }
    if (StringUtils.isNotEmpty(addr2)) {
      newAddr.setAddress2(addr2);
    }
    newAddr.setAddrHash(PubHashUtils.cleanPubAddrHash(addr));
    this.tmpPdwhPubAddrCleanDao.save(newAddr);
    return 1;
  }

  // 大多数情况addr2为国家，addr1为省份或者所在城市，addr为具体单位名字
  private Map<String, String> splitEnAddr(String addr) {
    String[] addrs = addr.split(",");
    Map rsMap = new HashMap<String, String>();
    if (addrs.length == 3) {
      rsMap.put("address", addrs[0]);
      String addr1 = StringUtils.trimToEmpty(addrs[1]);
      if (StringUtils.isNotEmpty(addr1)) {
        rsMap.put("address1", this.removeAddrNum(addr1));
      }
      String addr2 = StringUtils.trimToEmpty(addrs[2]);
      if (StringUtils.isNotEmpty(addr2)) {
        rsMap.put("address2", this.removeAddrNum(addr2));
      }
    } else if (addrs.length == 2) {
      rsMap.put("address", addrs[0]);
      String addr2 = StringUtils.trimToEmpty(addrs[1]);
      if (StringUtils.isNotEmpty(addr2)) {
        rsMap.put("address2", this.removeAddrNum(addr2));
      }
    } else if (addrs.length == 1) {
      rsMap.put("address", addrs[0]);
    } else {
      // 拆分出多个地址的处理
      String addr1 = StringUtils.trimToEmpty(addrs[addrs.length - 2]);
      if (this.isNumeric(addr1)) {
        addr1 = StringUtils.trimToEmpty(addrs[addrs.length - 3]);
      }
      if (StringUtils.isNotEmpty(addr1)) {
        rsMap.put("address1", this.removeAddrNum(addr1));
      }
      String addr2 = StringUtils.trimToEmpty(addrs[addrs.length - 1]);
      if (StringUtils.isNotEmpty(addr2)) {
        rsMap.put("address2", this.removeAddrNum(addr2));
      }
      String addr0 = this.getInstitutionName(addrs);
      if (StringUtils.isNotEmpty(addr0)) {
        rsMap.put("address", addr0);
      }
    }
    return rsMap;
  }

  public boolean isNumeric(String str) {
    if (StringUtils.isEmpty(str)) {
      return false;
    }
    Pattern pattern = Pattern.compile("[0-9]*");
    Matcher isNum = pattern.matcher(str);
    if (!isNum.matches()) {
      return false;
    }
    return true;
  }

  private String getInstitutionName(String[] addrs) {
    for (Integer i = 0; i < addrs.length; i++) {
      if (StringUtils.isEmpty(addrs[i])) {
        continue;
      }
      // 大学附属医院，算入大学中，方便提取地址
      /*
       * for (String hp : level0Ins) { if (addrs[i].indexOf(hp) > 0) { String hpAddr = addrs[i]; return
       * addrs[i]; } }
       */
      // 如果是大学，优先提取;
      for (String univ : level1Ins) {
        if (addrs[i].indexOf(univ) >= 0) {
          return addrs[i];
        }
      }
    }
    return null;
  }

  private String removeAddrNum(String addr) {
    // 去除数字
    addr = addr.replaceAll("\\d", "");
    if (StringUtils.isNotEmpty(addr)) {
      return addr;
    } else {
      return null;
    }
  }

  @Override
  public PubPdwhDetailDOM getFullPdwhPubInfoById(Long pubId) {
    return pubPdwhDetailDAO.findById(pubId);
  }

  /**
   * 获取pdwh_publication中期刊数据对应匹配的基准期刊id.
   * 
   * @param original ,issn
   * @return
   * @throws ServiceException
   */
  @Override
  public Long getJnlIdByJournalNameOrIssn(String original, String issn) {
    Long baseJnlId = null;
    try {
      // 获取匹配的基础期刊Id
      String jissn = XmlUtil.buildStandardIssn(issn);
      original = XmlUtil.changeSBCChar(original);
      if (StringUtils.isNotBlank(original) || StringUtils.isNotBlank(jissn)) {
        baseJnlId = baseJournalTitleDao.PdwhPubAllJournalMatchBaseJnlIdByNameOrIssn(original, jissn);
      }
    } catch (Exception e) {
      logger.error("获取pdwh_publication表对应期刊数据相匹配的基准库期刊id出错", e);
    }
    return baseJnlId;
  }

  @Override
  public List<BigDecimal> getPrjIdList(Integer size) {
    return this.tmpPdwhPubAddrDao.getPrjIdList(size);
  }

  @Override
  public void updatePrjStatus(Long prjId, Integer status) {
    this.tmpPdwhPubAddrDao.updatePrjStatus(prjId, status);
  }

  @Override
  public Integer extractPrjKws(Long prjId) throws Exception {
    NsfcPrjKwByAuthor npka = this.nsfcPrjKwByAuthorDao.get(prjId);
    if (npka == null) {
      return 4;
    }
    String zhKw = npka.getKwStrZh();
    String enKw = npka.getKwStrEn();
    Set<String> kwSetByAuthors = new HashSet<String>();
    this.getKwStr(zhKw, kwSetByAuthors);
    this.getKwStr(enKw, kwSetByAuthors);
    for (String str : kwSetByAuthors) {
      saveNsfcPrjKeywords(str, 1, npka.getNsfcCategroy(), npka.getYear(), npka.getPrjId());
    }
    Set<String> kwSetFromPubs = new HashSet<String>();
    List<Long> pubIds = this.nsfcPrjPubRelationDao.getPubIdList(npka.getPrjId());
    // 处理成果方面
    if (pubIds != null && pubIds.size() != 0) {
      for (Long pubId : pubIds) {
        if (pubId == null) {
          continue;
        }
        ScmPubXml xml = this.scmPubXmlDao.get(pubId);
        if (xml == null || StringUtils.isEmpty(xml.getPubXml())) {
          continue;
        }
        try {
          PubXmlDocument pubXml = new PubXmlDocument(xml.getPubXml());
          String zhPubKw = pubXml.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords");
          String enPubKw = pubXml.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_keywords");
          this.getKwStr(zhPubKw, kwSetFromPubs);
          this.getKwStr(enPubKw, kwSetFromPubs);
        } catch (Exception e) {
          logger.error("从nsfc项目成果中获取关键词出错", e);
        }
      }
      for (String str : kwSetFromPubs) {
        saveNsfcPrjKeywords(str, 2, npka.getNsfcCategroy(), npka.getYear(), npka.getPrjId());
      }
    }
    if (CollectionUtils.isEmpty(kwSetFromPubs)) {
      return 4;// 未提取到成果关键词
    }
    if (CollectionUtils.isEmpty(kwSetFromPubs) && CollectionUtils.isEmpty(kwSetByAuthors)) {
      return 2;
    }
    return 1;
  }

  private void saveNsfcPrjKeywords(String kw, Integer type, String category, Integer year, Long prjId) {
    if (StringUtils.isEmpty(kw)) {
      return;
    }
    NsfcPrjKeywords npk = new NsfcPrjKeywords();
    npk.setKwStr(kw);
    npk.setKwHashValue(HashUtils.getStrHashCode(kw));
    npk.setKwType(type);
    npk.setNsfcCategroy(category);
    npk.setPrjId(prjId);
    npk.setYear(year);
    if (XmlUtil.containZhChar(kw)) {
      npk.setLanguage(2);
    } else {
      npk.setLanguage(1);
    }
    this.nsfcPrjKeywordsDao.save(npk);
  }

  private void getKwStr(String kwStr, Set<String> strSet) {
    if (StringUtils.isEmpty(kwStr)) {
      return;
    }
    // 转换大小字符
    kwStr = XmlUtil.cToe(kwStr);
    String[] strC = kwStr.split(";");
    if (strC == null || strC.length == 0) {
      strC = kwStr.split(",");
    }
    if (strC == null || strC.length == 0) {
      return;
    }
    for (String str : strC) {
      if (!StringUtils.isEmpty(str)) {
        str = str.toLowerCase().trim();
        strSet.add(str);
      }
    }

  }

  @Override
  public PubPdwhPO getNewPdwhPubById(Long pubId) {
    return pubPdwhDAO.get(pubId);
  }

  @Override
  public List<Map<String, Object>> getPubMemberInfoList(Long pubId) {
    return pdwhMemberInsNameDAO.getPubMemberInfoList(pubId);
  }

  @Override
  public void saveTmpTaskInfoRecord(Long pubId) {
    // 同时要实时指派到人，即pub_assign_log表要有记录
    TmpTaskInfoRecord tmpTaskInfoRecord =
        tmpTaskInfoRecordDao.getJobByhandleId(pubId, TaskJobTypeConstants.pdwhPubAssignInsTask);
    if (tmpTaskInfoRecord != null) {
      tmpTaskInfoRecord.setStatus(0);
    } else {
      tmpTaskInfoRecord = new TmpTaskInfoRecord(pubId, TaskJobTypeConstants.pdwhPubAssignInsTask, new Date());
    }
    tmpTaskInfoRecordDao.saveOrUpdate(tmpTaskInfoRecord);
  }
}
