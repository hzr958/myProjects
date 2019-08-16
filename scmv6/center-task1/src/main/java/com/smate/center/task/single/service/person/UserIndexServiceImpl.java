package com.smate.center.task.single.service.person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.task.dao.open.OpenUserUnionDao;
import com.smate.center.task.dao.sns.psn.PsnDisciplineKeyDao;
import com.smate.center.task.dao.sns.psn.PsnScienceAreaDao;
import com.smate.center.task.dao.sns.quartz.InstitutionDao;
import com.smate.center.task.dao.sns.quartz.PublicationDao;
import com.smate.center.task.dao.sns.quartz.ScmPubXmlDao;
import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.sns.quartz.Institution;
import com.smate.center.task.model.sns.quartz.ScmPubXml;
import com.smate.center.task.single.dao.rol.psn.RolPsnInsDao;
import com.smate.center.task.single.dao.rol.pub.InsUnitDao;
import com.smate.center.task.single.model.rol.psn.RolPsnIns;
import com.smate.center.task.single.model.rol.pub.InsUnit;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.core.base.project.dao.ProjectDao;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.dao.security.InsPortalDao;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.PsnPrivateDao;
import com.smate.core.base.utils.model.InsPortal;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.string.IrisStringUtils;

@Service("userIndexService")
@Transactional(rollbackFor = Exception.class)
public class UserIndexServiceImpl implements UserIndexService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${solr.server.url}")
  private String serverUrl;
  private String runEnv = System.getenv("RUN_ENV");
  public static String INDEX_TYPE_PUB = "publication_index";
  public static String INDEX_TYPE_PAT = "patent_index";
  public static String INDEX_TYPE_PSN = "person_index";
  public static String INDEX_TYPE_PAT_RCMD = "patent_rcmd_index";
  public static String INDEX_TYPE_PAT_OWNER_RCMD = "patent_rcmd_owner_index";
  public static String INDEX_TYPE_PAT_REQUEST_RCMD = "patent_rcmd_request_index";

  @Autowired
  private PersonDao personDao;
  @Autowired
  private PsnPrivateDao psnPrivateDao;
  @Autowired
  private InsPortalDao insPortalDao;
  @Autowired
  private RolPsnInsDao rolPsnInsDao;
  @Autowired
  private InsUnitDao insUnitDao;
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private ScmPubXmlDao scmPubXmlDao;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private ProjectDao projectDao;
  @Autowired
  private PsnScienceAreaDao psnScienceAreaDao;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private PsnDisciplineKeyDao psnDisciplineKeyDao;

  private Integer batchSize = 2400;
  @Autowired
  private CacheService cacheService;

  @Override
  public void indexUser() {

    SolrServer server = initializeSolrServer();

    Long lastId = (Long) cacheService.get(INDEX_TYPE_PSN, "last_psn_id");
    if (lastId == null) {
      lastId = 0L;
    }
    ArrayList<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
    List<Person> psnList = personDao.findUserByBatchSize(lastId, batchSize);
    if (CollectionUtils.isEmpty(psnList)) {
      return;
    }
    Integer lastIndex = psnList.size() - 1;
    Long lastPsnId = psnList.get(lastIndex).getPersonId();
    this.cacheService.put(INDEX_TYPE_PSN, 60 * 60 * 24, "last_psn_id", lastPsnId);

    for (Person psn : psnList) {

      try {
        if (psn == null) {
          continue;
        }
        SolrInputDocument doc = new SolrInputDocument();
        // 必须字段设定schema.xml配置,配置id时需将其与其他id区分开，以5开头
        doc.setField("businessType", INDEX_TYPE_PSN);
        Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
        doc.setField("env", runEnv);
        doc.setField("id", generateIdForIndex(psn.getPersonId(), INDEX_TYPE_PSN));
        doc.setField("psnId", psn.getPersonId());
        doc.setField("psnName", psn.getName());
        doc.setField("enPsnName", psn.getEname());
        // 人员信息完整度
        doc.setField("psnInfoIntegrity", psn.getComplete());
        // 判断是否是isis注册过的用户
        boolean isIsisRegistered = openUserUnionDao.ifRegisteredInTheIns(psn.getPersonId(), "2bcca485");
        if (isIsisRegistered) {
          doc.setField("psnIsisReg", 1);
        } else {
          doc.setField("psnIsisReg", 0);
        }

        if (StringUtils.isBlank(psn.getPosition()) || StringUtils.isBlank(psn.getTitolo())) {
          doc.setField("title", (StringUtils.isBlank(psn.getPosition()) ? "" : psn.getPosition())
              + (StringUtils.isBlank(psn.getTitolo()) ? "" : psn.getTitolo()));
        } else {
          doc.setField("title", psn.getPosition() + ", " + psn.getTitolo());
        }

        // 添加人员科技领域
        List<Long> psnScienceAreaList = this.psnScienceAreaDao.findPsnScienceAreaIds(psn.getPersonId());
        if (psnScienceAreaList != null && psnScienceAreaList.size() > 0) {
          doc.setField("psnScienceArea", psnScienceAreaList.toArray());
        }

        // 获取个人成果中的关键词 与个人成果数
        Map<String, Object> rsMap = this.getPsnKwsByPubKws(psn.getPersonId());
        if (rsMap != null) {
          String kwsStr = (String) rsMap.get("kwsStr");
          doc.setField("psnKeywords", kwsStr);
          doc.setField("psnPatKeywords", rsMap.get("patKwsStr"));
          Integer pubCounts = (Integer) rsMap.get("pubCounts");
          doc.setField("psnPubCount", pubCounts);
        }
        // 人员自填关键词
        List<String> keywords = psnDisciplineKeyDao.getPsnKeywords(psn.getPersonId());
        String psnDisciplineKeyStr = "";
        if (CollectionUtils.isNotEmpty(keywords)) {
          psnDisciplineKeyStr = StringUtils.lowerCase(keywords.toString());
        }
        doc.setField("psnDisciplineKey", psnDisciplineKeyStr);
        // 获取人员的专利数
        Integer patentCounts = psnStatisticsDao.getPatentNumByPsnId(psn.getPersonId());
        doc.setField("psnPatentCount", patentCounts);
        // 人员项目数
        Long prjCounts = this.getPsnPrjCounts(psn.getPersonId());
        doc.setField("psnPrjCount", prjCounts);

        // 人员是否在隐私列表中
        if (this.psnPrivateDao.existsPsnPrivate(psn.getPersonId())) {
          doc.setField("isPrivate", 1);
        } else {
          doc.setField("isPrivate", 0);
        }
        if (psn.getInsId() != null) {
          Institution ins = institutionDao.get(psn.getInsId());
          if (ins != null) {
            doc.setField("zhInsName", StringUtils.isNotBlank(ins.getZhName()) ? ins.getZhName() : ins.getEnName());
            doc.setField("enInsName", StringUtils.isNotBlank(ins.getEnName()) ? ins.getEnName() : ins.getZhName());
          }
        } else if (StringUtils.isNotEmpty(psn.getInsName())) {
          doc.setField("zhInsName", psn.getInsName());
        }
        doc.setField("psnRegionId", psn.getRegionId());
        doc.setField("zhUnit", psn.getDepartment());
        docList.add(doc);
        lastId = psn.getPersonId();
      } catch (Exception e) {
        logger.error("人员索引创建出错，psnID:" + psn.getPersonId(), e);
      }
    }

    if (CollectionUtils.isNotEmpty(docList)) {
      try {
        Date start = new Date();
        server.add(docList);
        server.commit();
        Date end = new Date();
        logger.info("User索引创建完成，end = " + new Date() + " , 总共耗费时间(s)：" + (end.getTime() - start.getTime()) / 1000);
      } catch (SolrServerException | IOException e) {
        e.printStackTrace();
        logger.info("User索引创建出错，end = " + new Date());
      }
    }
  }

  private SolrServer initializeSolrServer() {
    SolrServer server = new HttpSolrServer(serverUrl);
    return server;
  }

  /*
   * 在index时区分唯一id
   */
  public Long generateIdForIndex(Long id, String type) {
    // pub前缀为100000
    if (INDEX_TYPE_PUB.equalsIgnoreCase(type)) {
      String idString = String.valueOf(id);
      return Long.parseLong("100000" + idString);
    } else if (INDEX_TYPE_PSN.equalsIgnoreCase(type)) {
      // psn前缀为50
      String idString = String.valueOf(id);
      return Long.parseLong("50" + idString);
    } else if (INDEX_TYPE_PAT.equalsIgnoreCase(type)) {
      // pat前缀为700000
      String idString = String.valueOf(id);
      return Long.parseLong("700000" + idString);
    } else if (INDEX_TYPE_PAT_RCMD.equalsIgnoreCase(type)) {
      // patRcmd前缀为770000
      String idString = String.valueOf(id);
      return Long.parseLong("770000" + idString);
    } else if (INDEX_TYPE_PAT_OWNER_RCMD.equalsIgnoreCase(type)) {
      // patOwner前缀为770000
      String idString = String.valueOf(id);
      return Long.parseLong("780000" + idString);
    } else if (INDEX_TYPE_PAT_REQUEST_RCMD.equalsIgnoreCase(type)) {
      // patRequestRcmd前缀为790000
      String idString = String.valueOf(id);
      return Long.parseLong("790000" + idString);
    } else {
      return id;
    }
  }

  /**
   * 获取个人所属的所有成果关键词，与成果数
   */
  public Map<String, Object> getPsnKwsByPubKws(Long psnId) {
    List<Long> pubIdList = this.publicationDao.getPubIdListByPsnId(psnId);
    Integer pubCounts = 0;
    StringBuilder keywords = new StringBuilder();
    StringBuilder patKeywords = new StringBuilder();
    if (CollectionUtils.isNotEmpty(pubIdList)) {
      pubCounts = pubIdList.size();
      for (Long pubId : pubIdList) {
        ScmPubXml scmPubXml = scmPubXmlDao.get(pubId);
        if (scmPubXml != null) {
          String pubXml = scmPubXml.getPubXml();

          if (StringUtils.isNotEmpty(pubXml)) {
            try {
              PubXmlDocument xmlDoc = new PubXmlDocument(pubXml);
              if (5 == xmlDoc.getPubTypeId()) {
                String zhAbstract = StringUtils
                    .trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_abstract"));
                String enAbstract = StringUtils
                    .trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_abstract"));
                String zhTitle = IrisStringUtils.filterSupplementaryChars(
                    StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title")));
                String enTitle = IrisStringUtils.filterSupplementaryChars(
                    StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title")));
                patKeywords.append(zhAbstract);
                patKeywords.append(enAbstract);
                patKeywords.append(zhTitle);
                patKeywords.append(enTitle);
              }

              String enKws =
                  StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_keywords"));
              String zhKws =
                  StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords"));
              if (StringUtils.isNoneEmpty(enKws)) {
                keywords.append(enKws);
              }
              if (StringUtils.isNoneEmpty(zhKws)) {
                keywords.append(zhKws);
              }
            } catch (Exception e) {
              logger.error("获取xml相关属性错误, pubId = " + pubId);
            }
          }
        }
      }
    }

    String kwsStr = keywords.toString();
    Map rsMap = new HashMap<String, Object>();
    rsMap.put("pubCounts", pubCounts);
    rsMap.put("kwsStr", kwsStr);
    rsMap.put("patKwsStr", patKeywords.toString());
    return rsMap;
  }

  public Long getPsnPrjCounts(Long psnId) {
    Long count = this.projectDao.getPsnPrjCounts(psnId);

    if (count == null) {
      return 0L;
    }
    return count;
  }

  public String getAllPatContentString(Long psnId) {

    List<Long> pubIdList = this.publicationDao.getPubIdListByPsnId(psnId);
    StringBuilder content = new StringBuilder();
    if (CollectionUtils.isNotEmpty(pubIdList)) {
      for (Long pubId : pubIdList) {
        ScmPubXml scmPubXml = scmPubXmlDao.get(pubId);
        if (scmPubXml != null) {
          String pubXml = scmPubXml.getPubXml();

          if (StringUtils.isNotEmpty(pubXml)) {
            try {
              PubXmlDocument xmlDoc = new PubXmlDocument(pubXml);
              String enTitle =
                  StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title"));
              String zhTitle =
                  StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title"));
              String enKws =
                  StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_abstract"));
              String zhKws =
                  StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_abstract"));
              if (StringUtils.isNoneEmpty(enTitle)) {
                content.append(enTitle);
              }
              if (StringUtils.isNoneEmpty(zhTitle)) {
                content.append(zhTitle);
              }
              if (StringUtils.isNoneEmpty(enKws)) {
                content.append(enKws);
              }
              if (StringUtils.isNoneEmpty(zhKws)) {
                content.append(zhKws);
              }
            } catch (Exception e) {
              logger.error("获取xml相关属性错误, pubId = " + pubId);
            }
          }
        }
      }
    }

    String contentStr = content.toString();
    return contentStr;

  }

  @Override
  public void indexPatentOwner() {

    SolrServer server = initializeSolrServer();

    Long lastId = (Long) cacheService.get(INDEX_TYPE_PAT_OWNER_RCMD, "last_psn_id");
    if (lastId == null) {
      lastId = 0L;
    }
    ArrayList<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
    List<Person> psnList = personDao.findUserByBatchSize(lastId, batchSize);
    if (CollectionUtils.isEmpty(psnList)) {
      return;
    }
    Integer lastIndex = psnList.size() - 1;
    Long lastPsnId = psnList.get(lastIndex).getPersonId();
    this.cacheService.put(INDEX_TYPE_PAT_OWNER_RCMD, 60 * 60 * 24, "last_psn_id", lastPsnId);

    for (Person psn : psnList) {
      if (psn == null) {
        continue;
      }
      SolrInputDocument doc = new SolrInputDocument();
      // 必须字段设定schema.xml配置,配置id时需将其与其他id区分开，以5开头
      doc.setField("businessType", INDEX_TYPE_PAT_OWNER_RCMD);
      Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
      doc.setField("env", runEnv);
      doc.setField("id", generateIdForIndex(psn.getPersonId(), INDEX_TYPE_PAT_OWNER_RCMD));
      doc.setField("psnIdPatRcmd", psn.getPersonId());
      doc.setField("psnNamePatRcmd", psn.getName());
      doc.setField("enPsnNamePatRcmd", psn.getEname());
      doc.setField("titlePatRcmd", psn.getPosition());

      // 获取个人成果中的关键词
      Map<String, Object> rsMap = this.getPsnKwsByPubKws(psn.getPersonId());
      if (rsMap != null) {
        String kwsStr = (String) rsMap.get("kwsStr");
        doc.setField("psnKeywordsPatRcmd", kwsStr);
      }
      // 人员是否在隐私列表中
      if (this.psnPrivateDao.existsPsnPrivate(psn.getPersonId())) {
        doc.setField("isPrivatePatRcmd", 1);
      } else {
        doc.setField("isPrivatePatRcmd", 0);
      }

      if (psn.getInsId() != null) {

        InsPortal ins = insPortalDao.get(psn.getInsId());
        if (ins != null) {
          doc.setField("zhInsNamePatRcmd", ins.getZhTitle());
          doc.setField("enInsNamePatRcmd", ins.getEnTitle());
        }
        try {
          RolPsnIns psnIns = rolPsnInsDao.findPsnIns(psn.getPersonId(), psn.getInsId());
          if (psnIns != null && psnIns.getUnitId() != null) {
            InsUnit unit = insUnitDao.get(psnIns.getUnitId());
            if (unit != null) {
              doc.setField("zhUnitPatRcmd", unit.getZhName());
              doc.setField("enUnitPatRcmd", unit.getEnName());
            }
          }
        } catch (DaoException e) {
          logger.info("获取psnIns相关属性错误, psnId= " + psn.getPersonId(), e);
        }
      }
      docList.add(doc);
      lastId = psn.getPersonId();
    }

    if (CollectionUtils.isNotEmpty(docList)) {
      try {
        Date start = new Date();
        server.add(docList);
        server.commit();
        Date end = new Date();
        logger.info("User索引创建完成，end = " + new Date() + " , 总共耗费时间(s)：" + (end.getTime() - start.getTime()) / 1000);
      } catch (SolrServerException | IOException e) {
        e.printStackTrace();
        logger.info("User索引创建出错，end = " + new Date());
      }
    }
  }

}
