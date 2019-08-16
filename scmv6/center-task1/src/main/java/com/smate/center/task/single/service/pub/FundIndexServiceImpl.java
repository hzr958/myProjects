package com.smate.center.task.single.service.pub;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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

import com.smate.center.task.dao.fund.rcmd.ConstFundAgencyDao;
import com.smate.center.task.dao.fund.rcmd.ConstFundCategoryDao;
import com.smate.center.task.dao.fund.rcmd.ConstFundCategoryDisDao;
import com.smate.center.task.dao.fund.rcmd.ConstFundCategoryKeywordsDao;
import com.smate.center.task.dao.fund.rcmd.ConstFundCategoryRegionDao;
import com.smate.center.task.dao.fund.rcmd.ConstFundPositionDao;
import com.smate.center.task.dao.pdwh.quartz.BaseJnlCategoryRankDao;
import com.smate.center.task.dao.pdwh.quartz.BaseJournalCategoryDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubXmlDao;
import com.smate.center.task.dao.sns.quartz.CategoryMapBaseDao;
import com.smate.center.task.dao.sns.quartz.ConstRegionDao;
import com.smate.center.task.model.fund.rcmd.ConstFundAgency;
import com.smate.center.task.model.fund.rcmd.ConstFundCategory;
import com.smate.center.task.model.fund.rcmd.ConstFundCategoryKeywords;
import com.smate.center.task.model.fund.rcmd.ConstFundCategoryRegion;
import com.smate.center.task.model.sns.pub.CategoryMapBase;
import com.smate.center.task.model.sns.pub.ConstRegion;
import com.smate.core.base.utils.cache.CacheService;

@Service("fundIndexService")
@Transactional(rollbackFor = Exception.class)
public class FundIndexServiceImpl implements FundIndexService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${solr.server.url}")
  private String serverUrl;
  private String runEnv = System.getenv("RUN_ENV");
  public static String INDEX_TYPE_PUB = "publication_index";
  public static String INDEX_TYPE_PAT = "patent_index";
  public static String INDEX_TYPE_PSN = "person_index";
  public static String INDEX_TYPE_FUND = "fund_index";
  public static String INDEX_TYPE_KW = "keywords_index";
  @Autowired
  private CacheService cacheService;
  @Autowired
  private ConstFundCategoryDao constFundCategoryDao;
  @Autowired
  private ConstFundCategoryDisDao constFundCategoryDisDao;
  @Autowired
  private ConstFundCategoryKeywordsDao constFundCategoryKeywordsDao;
  @Autowired
  private ConstFundCategoryRegionDao constFundCategoryRegionDao;
  @Autowired
  private ConstFundAgencyDao constFundAgencyDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private ConstFundPositionDao constFundPositionDao;
  @Autowired
  private CategoryMapBaseDao categoryMapBaseDao;

  @Autowired
  private PdwhPubXmlDao pdwhPubXmlDao;
  @Autowired
  private BaseJournalCategoryDao baseJournalCategoryDao;
  @Autowired
  private BaseJnlCategoryRankDao baseJnlCategoryRankDao;

  @Override
  public void fundIndex(Integer size) {
    SolrServer server = initializeSolrServer();
    Long lastId = (Long) cacheService.get(INDEX_TYPE_FUND, "last_fund_id");
    if (lastId == null) {
      lastId = 0L;
    }
    List<ConstFundCategory> fundList = constFundCategoryDao.getFundList(lastId, size);
    if (CollectionUtils.isEmpty(fundList)) {
      logger.info("==========fundList为空，index终止===========, time = " + new Date());
      return;
    }
    Integer lastIndex = fundList.size() - 1;
    Long lastFundId = fundList.get(lastIndex).getId();
    this.cacheService.put(INDEX_TYPE_FUND, 60 * 60 * 24, "last_fund_id", lastFundId);
    ArrayList<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
    for (ConstFundCategory fund : fundList) {
      if (fund == null || fund.getId() == null) {
        continue;
      }
      try {
        SolrInputDocument doc = new SolrInputDocument();
        doc = this.fillFundInfo(doc, fund);
        docList.add(doc);
      } catch (Exception e) {
        logger.error("获取基金相关信息出错，fundId=" + fund.getId() + "! ", e);
      }
      lastId = fund.getId();
    }

    if (CollectionUtils.isNotEmpty(docList)) {
      try {
        Date start = new Date();
        server.add(docList);
        server.commit();
        Date end = new Date();
        logger.info("fund索引创建完成，end = " + new Date() + " , 总共耗费时间(s)：" + (end.getTime() - start.getTime()) / 1000);
      } catch (SolrServerException | IOException e) {
        e.printStackTrace();
        logger.info("index索引创建出错，end = " + new Date());
      }
    }

  }

  private SolrServer initializeSolrServer() {
    SolrServer server = new HttpSolrServer(serverUrl);
    return server;
  }

  public SolrInputDocument fillFundInfo(SolrInputDocument doc, ConstFundCategory fund) {
    // 必须字段设定schema.xml配置
    Long fundId = fund.getId();
    Long agencyId = fund.getAgencyId();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    Date startDate = fund.getStartDate();
    Date endDate = fund.getEndDate();

    doc.setField("businessType", INDEX_TYPE_FUND);
    Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
    doc.setField("env", runEnv);
    doc.setField("id", generateIdForIndex(fundId, INDEX_TYPE_FUND));
    doc.setField("fundId", fundId);
    doc.setField("fundAgencyId", agencyId);
    doc.setField("fundNameZh", fund.getNameZh());
    doc.setField("fundNameEn", fund.getNameEn());
    if (fund.getCreateDate() != null) {
      doc.setField("fundCreateDate", sdf.format(fund.getCreateDate()));
    }
    if (fund.getUpdateDate() != null) {
      doc.setField("fundUpdateDate", sdf.format(fund.getUpdateDate()));
    }
    if (startDate != null) {
      doc.setField("fundStartDate", sdf.format(startDate));
    }
    if (endDate != null) {
      doc.setField("fundEndDate", sdf.format(endDate));
    }
    // 基金资助机构
    ConstFundAgency agency = constFundAgencyDao.get(agencyId);
    if (agency != null) {
      doc.setField("fundAgencyNameZh", agency.getNameZh());
      doc.setField("fundAgencyNameEn", agency.getNameEn());
    }
    doc.setField("fundDescription", fund.getDescription());
    // 基金所属地区
    doc.setField("fundRegionStrZh", this.getFundRegionStr(fundId, "zh"));
    doc.setField("fundRegionStrEn", this.getFundRegionStr(fundId, "en"));
    // 基金关键词
    doc.setField("fundKwString", this.getFundKeywordStr(fundId));
    // 获取基金职称，学位要求
    doc.setField("fundDegreeAndTitleRequire1",
        this.getMinimumRequirement(fund.getDegreeRequire1(), fund.getTitleRequire1()));
    doc.setField("fundDegreeAndTitleRequire2",
        this.getMinimumRequirement(fund.getDegreeRequire2(), fund.getTitleRequire2()));
    // 需要更新至scm新分类
    List<Long> discodeList = constFundCategoryDisDao.getFundDiscIdList(fundId);
    doc.setField("fundCategoryStrZh", this.getCategoryStr(discodeList, "zh"));
    doc.setField("fundCategoryStrEn", this.getCategoryStr(discodeList, "en"));
    doc.setField("fundCategoryIds", discodeList.toArray());
    String insType = fund.getInsType();
    if (StringUtils.isNotBlank(insType)) {
      if (insType.indexOf("1") < 0) {
        doc.setField("fundInsTypeEnterprise", 0);
      } else {
        doc.setField("fundInsTypeEnterprise", 1);
      }

      if (insType.indexOf("2") < 0) {
        doc.setField("fundInsTypeResearchIns", 0);
      } else {
        doc.setField("fundInsTypeResearchIns", 1);
      }
    }
    return doc;
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
    } else if (INDEX_TYPE_FUND.equalsIgnoreCase(type)) {
      // fund前缀为900000
      String idString = String.valueOf(id);
      return Long.parseLong("900000" + idString);
    } else if (INDEX_TYPE_KW.equalsIgnoreCase(type)) {
      String idString = String.valueOf(id);
      return Long.parseLong("210000" + idString);
    } else {
      return id;
    }
  }

  private String getFundKeywordStr(Long fundId) {
    String kwString = "";
    List<ConstFundCategoryKeywords> keywordsList = constFundCategoryKeywordsDao.findFundKeywordByCategoryId(fundId);
    StringBuilder sb = new StringBuilder();
    for (ConstFundCategoryKeywords kw : keywordsList) {

      if (StringUtils.isNotEmpty(kw.getKeywordText())) {
        sb.append(kw.getKeywordText());
        sb.append(", ");
      }
    }
    if (StringUtils.isNotEmpty(sb.toString())) {
      kwString = StringUtils.trimToEmpty(sb.substring(0, sb.length() - 2).toString());
    }
    return kwString;
  }

  private String getFundRegionStr(Long fundId, String language) {
    String regionStr = "";
    List<ConstFundCategoryRegion> regions = constFundCategoryRegionDao.findFundRegionByCategoryId(fundId);
    StringBuffer sb = new StringBuffer();
    for (ConstFundCategoryRegion region : regions) {
      ConstRegion cr = constRegionDao.getConstRegionById(region.getRegId());
      if (cr != null) {
        // 英文地区
        if ("en".equals(language)) {
          if (StringUtils.isNotEmpty(cr.getEnName())) {
            sb.append(cr.getEnName());
            sb.append(", ");
          }
          // 中文地区
        } else if ("zh".equals(language)) {
          if (StringUtils.isNotEmpty(cr.getZhName())) {
            sb.append(cr.getZhName());
            sb.append(", ");
          }
        }
      }
    }
    if (StringUtils.isNotEmpty(sb.toString())) {
      regionStr = StringUtils.trimToEmpty(sb.substring(0, sb.length() - 2).toString());
    }
    return regionStr;
  }

  private Integer getMinimumRequirement(String require1, String require2) {
    List<Long> requireIdList1 = this.getSplitRequireIdList(require1);
    List<Long> requireIdList2 = this.getSplitRequireIdList(require2);
    // 学位如果是所有则为0
    Integer minValue1 = this.constFundPositionDao.getLowestRequirementById(requireIdList1);
    // 职称
    Integer minValue2 = this.constFundPositionDao.getLowestRequirementById(requireIdList2);
    if (minValue1 != 0 && minValue2 != 0) {
      return minValue1 > minValue2 ? minValue2 : minValue1;
    } else {// 其中有一个为-1时
      return minValue1 == 0 ? minValue2 : minValue1;
    }
  }

  private String getCategoryStr(List<Long> discodeList, String type) {
    String CategoryStr = "";
    if (CollectionUtils.isNotEmpty(discodeList)) {
      List<CategoryMapBase> list = this.categoryMapBaseDao.findCategoryMapBases(discodeList);
      StringBuffer sb = new StringBuffer();
      for (CategoryMapBase category : list) {
        if ("zh".equals(type)) {
          if (StringUtils.isNotEmpty(category.getCategoryZh())) {
            sb.append(category.getCategoryZh());
            sb.append(", ");
          }
        } else if ("en".equals(type)) {
          if (StringUtils.isNotEmpty(category.getCategoryEn())) {
            sb.append(category.getCategoryEn());
            sb.append(", ");
          }
        }
      }
      if (StringUtils.isNotEmpty(sb.toString())) {
        CategoryStr = StringUtils.trimToEmpty(sb.substring(0, sb.length() - 2).toString());
      }
    }
    return CategoryStr;
  }

  /**
   * 获取解析职称或学位要求列表.
   * 
   * @param requireStr
   * @return
   */
  private List<Long> getSplitRequireIdList(String requireStr) {
    List<Long> requireIdList = new ArrayList<Long>();
    if (StringUtils.isNotBlank(requireStr)) {
      String[] requireArr = requireStr.split(",");
      for (int i = 0; i < requireArr.length; i++) {
        requireIdList.add(Long.valueOf(requireArr[i]));
      }
    }
    return requireIdList;
  }
}
