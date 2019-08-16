package com.smate.center.open.service.fund;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.open.dao.consts.ConstRegionDao;
import com.smate.center.open.dao.fund.ConstFundAgencyDao;
import com.smate.center.open.dao.fund.ConstFundCategoryDao;
import com.smate.center.open.dao.fund.ConstFundCategoryDisDao;
import com.smate.center.open.dao.fund.ConstFundCategoryKeywordsDao;
import com.smate.center.open.dao.fund.ConstFundCategoryRegionDao;
import com.smate.center.open.dao.fund.ConstFundPositionDao;
import com.smate.center.open.dao.publication.CategoryMapBaseDao;
import com.smate.center.open.model.consts.ConstRegion;
import com.smate.center.open.model.fund.ConstFundAgency;
import com.smate.center.open.model.fund.ConstFundCategory;
import com.smate.center.open.model.fund.ConstFundCategoryKeywords;
import com.smate.center.open.model.fund.ConstFundCategoryRegion;
import com.smate.center.open.model.publication.CategoryMapBase;

@Service("fundService")
@Transactional(rollbackFor = Exception.class)
public class FundServiceImpl implements FundService {
  public static String INDEX_TYPE_FUND = "fund_index";

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${solr.server.url}")
  private String serverUrl;
  private String runEnv = System.getenv("RUN_ENV");
  @Autowired
  private ConstFundCategoryDao constFundCategoryDao;
  @Autowired
  private ConstFundAgencyDao constFundAgencyDao;
  @Autowired
  private ConstFundCategoryDisDao constFundCategoryDisDao;
  @Autowired
  private ConstFundCategoryRegionDao constFundCategoryRegionDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private CategoryMapBaseDao categoryMapBaseDao;
  @Autowired
  private ConstFundPositionDao constFundPositionDao;
  @Autowired
  private ConstFundCategoryKeywordsDao constFundCategoryKeywordsDao;

  @Override
  public void deleteSolrFundInfo(Long fundId) throws Exception {
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    SolrServer server = new HttpSolrServer(serverUrl);
    if (fundId == null || fundId <= 0L) {
      logger.error("更新基金solr索引，psnId为空");
      return;
    }
    server.deleteById("900000" + fundId);
    server.commit();
    logger.info("Solr人员基金信息成功删除，fundId = " + fundId);
  }

  @Override
  public void updateSolrFundInfo(Long fundId) throws Exception {
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    SolrServer server = new HttpSolrServer(serverUrl);
    if (fundId == null || fundId <= 0L) {
      logger.error("更新基金solr索引，psnId为空");
      return;
    }
    server.deleteById("900000" + fundId);
    server.commit();

    SolrInputDocument doc = new SolrInputDocument();
    ConstFundCategory fund = constFundCategoryDao.getFundInfo(fundId);
    Long agencyId = fund.getAgencyId();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    Date startDate = fund.getStartDate();
    Date endDate = fund.getEndDate();

    doc.setField("businessType", INDEX_TYPE_FUND);
    Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
    doc.setField("env", runEnv);
    doc.setField("id", "900000" + fundId);
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
    server.add(doc);
    server.commit();
  }

  private Object getCategoryStr(List<Long> discodeList, String type) {
    String CategoryStr = "";
    if (CollectionUtils.isNotEmpty(discodeList)) {
      List<Integer> discodes = new ArrayList<Integer>();
      for (Long discode : discodeList) {
        discodes.add(discode.intValue());
      }
      List<CategoryMapBase> list = this.categoryMapBaseDao.findCategoryMapBases(discodes);
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

  private Object getMinimumRequirement(String require1, String require2) {
    List<Long> requireIdList1 = this.getSplitRequireIdList(require1);
    List<Long> requireIdList2 = this.getSplitRequireIdList(require2);
    // 学位如果是所有则为-1
    Integer minValue1 = this.constFundPositionDao.getLowestRequirementById(requireIdList1);
    // 职称
    Integer minValue2 = this.constFundPositionDao.getLowestRequirementById(requireIdList2);
    if (minValue1 != 0 && minValue2 != 0) {
      return minValue1 > minValue2 ? minValue2 : minValue1;
    } else {// 其中有一个为-1时
      return minValue1 == 0 ? minValue2 : minValue1;
    }
  }

  private Object getFundKeywordStr(Long fundId) {
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

  private Object getFundRegionStr(Long fundId, String language) throws Exception {
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
