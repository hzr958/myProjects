package com.smate.core.base.solr.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.gson.Gson;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
// import com.hankcs.hanlp.HanLP;
// import com.hankcs.hanlp.seg.Segment;
// import com.hankcs.hanlp.seg.common.Term;
import com.smate.core.base.solr.model.QueryFields;
import com.smate.core.base.utils.common.HashUtils;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;

@Service("solrIndexService")
@Transactional(rollbackFor = Exception.class)
public class SolrIndexSerivceImpl implements SolrIndexService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${solr.server.url}")
  private String serverUrl;
  private String runEnv = System.getenv("RUN_ENV");
  public static String INDEX_TYPE_PDWH_PAPER = "pdwh_paper_index";
  public static String INDEX_TYPE_PDWH_PAT = "pdwh_pat_index";
  public static String INDEX_TYPE_PUB = "publication_index";
  public static String INDEX_TYPE_PAT = "patent_index";
  public static String INDEX_TYPE_PSN = "person_index";
  public static String INDEX_TYPE_FUND = "fund_index";
  public static String INDEX_TYPE_KW = "keywords_index";
  public static String INDEX_TYPE_SNS_PUB = "simple_sns_pub_index";
  public static String INDEX_PRP_CODE = "prp_code_index";
  public static String RESULT_FACET = "facet"; // 查询分类统计
  public static String RESULT_NUMFOUND = "numFound"; // 查询结果总数
  public static String RESULT_ITEMS = "items"; // 查询结果
  public static String RESULT_HIGHLIGHT = "highlight"; // 高亮结果显示
  public static String NSFC_TF = "nsfc_tf";
  public static String NSFC_COTF = "nsfc_cotf";
  public static String INDEX_PDWH_SEARCH_SUGGEST = "pdwh_suggest_search_index"; // 基准库检索提示

  /*
   * 查询包含特定关键词的成果数
   */
  @Override
  public Long queryPubCount(String kw) throws SolrServerException {
    Assert.isTrue(StringUtils.isNotEmpty(kw), "查询关键词不能为空");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/pubbrowse");
    query.setQuery(kw);
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    return rsCount;
  }

  /*
   * 查询包含特定关键词的成果数
   */
  @Override
  public Long queryPubCount(String kw, Integer year) throws SolrServerException {
    Assert.isTrue(StringUtils.isNotEmpty(kw), "查询关键词不能为空");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/pubbrowse");
    query.setQuery(kw);
    query.addFilterQuery("pubYear:" + year);
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    return rsCount;
  }

  /*
   * 查询Publication:pubType=3,4,7 AND articleType = 1
   * 
   * QueryFields 包含String searchString, Integer pubYear, Integer pubType
   */
  @Override
  public Map<String, Object> queryPubs(Integer pageCount, Integer size, QueryFields queryFields)
      throws SolrServerException {
    Assert.notNull(queryFields, "queryFields不能为空！");
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");

    Integer pubYear = queryFields.getPubYear();
    Integer pubType = queryFields.getPubTypeId();
    String years = queryFields.getYears();
    String language = queryFields.getLanguage();
    // String language = queryFields.getLanguage();

    boolean isDoi = queryFields.getIsDoi();

    SolrServer server = new HttpSolrServer(serverUrl);
    String queryString = queryFields.getSearchString();
    SolrQuery query = new SolrQuery();
    Integer start = 0;
    if (pageCount > 0) {
      start = (pageCount - 1) * size;
    }
    if (isDoi) {
      query.setRequestHandler("/pubbrowsebydoi");
      query.addHighlightField("doi");
    } else {
      String location = this.getLocationStr(queryString);
      if (StringUtils.isNotEmpty(location) && queryFields.LANGUAGE_ZH.equals(language)) {
        String bqQuery =
            "journalGrade:1^2500 journalGrade:2^2200 journalGrade:3^2000 journalGrade:4^1500 journalGrade:5^500 journalGrade:6^250 journalGrade:7^200 journalGrade:8^100 fullText:1^3300";
        bqQuery = bqQuery + " pubOrganization:" + location + "^100";
        query.setParam("bp", bqQuery);
      }

      if (queryFields.LANGUAGE_ZH.equals(language)) {
        // 判断是否是库里边存在的中文名
        query.setRequestHandler("/pubbrowsezh");
        if (isSearchByNameZh(queryString, server, runEnv)) {
          query.setQuery("\"" + queryString + "\"");
        } else {
          query.setQuery(queryString);// 这里要进行对特殊字符（如空格等）进行转义操作
        }
      } else {
        query.setRequestHandler("/pubbrowseen");
        if (isSearchByNameEn(queryString, server, runEnv)) {
          query.setQuery("\"" + queryString + "\"");
        } else {
          query.setQuery(queryString);
        }
      }
      query.addHighlightField("title authors keywords");
    }

    List<String> fqList = new ArrayList<String>();
    if (pubYear != null) {
      fqList.add("pubYear:" + pubYear);
    } else if (StringUtils.isNotBlank(years)) {
      fqList.add("pubYear:" + "(" + years.trim().replaceAll(",", " ") + ")");
    }
    // 优先使用list查询
    if (queryFields.getPubTypeIdList() != null && queryFields.getPubTypeIdList().size() != 0) {
      StringBuffer sb = new StringBuffer();
      for (Integer pubTypeId : queryFields.getPubTypeIdList()) {
        sb.append(String.valueOf(pubTypeId));
        sb.append(" ");
      }
      String pubTypeIdsStr = sb.toString().trim();
      pubTypeIdsStr = "(" + pubTypeIdsStr + ")";
      fqList.add("pubTypeId:" + pubTypeIdsStr);
    } else {
      if (pubType != null) {
        if (pubType == 7) {
          fqList.add("pubTypeId:(7 1 2 5 10 12 13)");
        } else {
          fqList.add("pubTypeId:" + pubType);
        }
      } else if (StringUtils.isNotBlank(queryFields.getPubTypeIdStr())) {
        fqList.add("pubTypeId:" + "(" + queryFields.getPubTypeIdStr().trim().replaceAll(",", " ") + ")");
      }
    }

    if (StringUtils.isNotBlank(queryFields.getTitle())) {
      fqList.add("pubTitle:" + "\"" + queryFields.getTitle() + "\"");
    }
    if (StringUtils.isNotBlank(queryFields.getKeywords())) {
      fqList.add("keywords:" + "\"" + queryFields.getKeywords() + "\"");
    }
    if (StringUtils.isNotBlank(queryFields.getAuthors())) {
      fqList.add("authors:" + "\"" + queryFields.getAuthors() + "\"");
    }

    if (queryFields.getExcludedPubIds() != null && queryFields.getExcludedPubIds().size() != 0) {
      StringBuffer sb = new StringBuffer();
      for (Long pubId : queryFields.getExcludedPubIds()) {
        sb.append(String.valueOf(pubId));
        sb.append(" ");
      }
      String pubIdsStr = sb.toString().trim();
      pubIdsStr = "(" + pubIdsStr + ")";
      fqList.add("-pubId:" + pubIdsStr);
    }

    // 根据收录（dbId）过滤
    if (StringUtils.isNotBlank(queryFields.getPubDBIds())) {
      String pubDBIds = queryFields.getPubDBIds().trim().replaceAll(",", " ");
      fqList.add("(pubDbIdList:(" + pubDBIds + ") OR pubDbId:(" + pubDBIds + "))");
    }
    // 取消成果语言类型的判断
    /*
     * if (StringUtils.isNotEmpty(language) && !QueryFields.LANGUAGE_ALL.equals(language)) {
     * fqList.add("language:" + language); }
     */
    fqList.add("env:" + runEnv);
    fqList.add("businessType:" + INDEX_TYPE_PDWH_PAPER);
    String[] str = fqList.toArray(new String[fqList.size()]);
    query.addFilterQuery(str);
    query.setStart(start);
    query.setRows(size);
    query.setParam("fl", "*, score");
    query.setFacet(true);
    query.setFacetMinCount(1);
    query.setHighlightSimplePre("<font style = 'color:#de5f0d;font-weight:bold'>");
    query.setHighlightSimplePost("</font>");
    if (QueryFields.ORDER_YEAR.equals(queryFields.getOrderBy())) {
      query.addSort("pubYear", ORDER.desc);
      query.addSort("pubMonth", ORDER.desc);
      query.addSort("pubDay", ORDER.desc);
      query.addSort("fullText", ORDER.desc);
    } else {
      query.addSort("score", ORDER.desc);
      query.addSort("fullText", ORDER.desc);
    }
    // 在第二次查询的时候才会加入统计，填充leftmenu
    if (QueryFields.SEQ_2.equals(queryFields.getSeqQuery())) {
      String[] fFields = {"pubYear", "pubTypeId", "language"};
      query.addFacetField(fFields);
    }
    QueryResponse qrs = server.query(query, METHOD.POST);
    Long rsCount = qrs.getResults().getNumFound();
    // String resultStr = qrs.getResults().toString();
    Gson gs = new Gson();
    String resultStr = gs.toJson(qrs.getResults());
    String hlStr = gs.toJson(qrs.getHighlighting());
    Map<String, Object> rsMap = new HashMap<String, Object>();
    // 在第二次查询的时候才会加入统计，填充leftmenu
    if (QueryFields.SEQ_2.equals(queryFields.getSeqQuery())) {
      List<FacetField> facetList = qrs.getFacetFields();
      rsMap.put(RESULT_FACET, facetList);
    }
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    rsMap.put(RESULT_HIGHLIGHT, hlStr);
    return rsMap;
  }

  @Override
  public Map<String, Object> mobileFindPubs(Integer pageCount, Integer size, QueryFields queryFields)
      throws SolrServerException {
    Assert.notNull(queryFields, "queryFields不能为空！");
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    SolrQuery query = new SolrQuery();
    List<String> fqList = new ArrayList<String>();
    /*
     * if (queryFields.LANGUAGE_ZH.equals(queryFields.getLanguage())) { // 判断是否是库里边存在的中文名
     * query.setRequestHandler("/pubbrowsezh"); } else if
     * (queryFields.LANGUAGE_EN.equals(queryFields.getLanguage())) {
     * query.setRequestHandler("/pubbrowseen"); } else { }
     */
    query.setRequestHandler("/pubbrowse");

    SolrServer server = new HttpSolrServer(serverUrl);
    Integer start = 0;
    if (pageCount > 0) {
      start = (pageCount - 1) * size;
    }
    // 根据检索内容
    String searchString = queryFields.getSearchString();
    if (StringUtils.isNotBlank(searchString)) {
      query.setQuery(searchString);
    }
    // 根据科技领域过滤
    if (StringUtils.isNotBlank(queryFields.getScienceAreaIds())) {
      fqList.add("pubCategory:" + "(" + queryFields.getScienceAreaIds().replaceAll(",", " ") + ")");
    }
    // 根据成果类型过滤
    if (StringUtils.isNotBlank(queryFields.getPubTypeIdStr())) {
      fqList.add("pubTypeId:" + "(" + queryFields.getPubTypeIdStr().replaceAll(",", " ") + ")");
    }
    // 根据发表年份过滤
    if (StringUtils.isNotBlank(queryFields.getPubYearStr())) {
      fqList.add("pubYear:" + "(" + queryFields.getPubYearStr().replaceAll(",", " ") + ")");
    }
    // 根据收录（dbId）过滤
    if (StringUtils.isNotBlank(queryFields.getPubDBIds())) {
      fqList.add("pubDbIdList:(" + queryFields.getPubDBIds().trim().replaceAll(",", " ") + ")");
    }
    // 隐私
    fqList.add("isPrivate:0");
    fqList.add("env:" + runEnv);
    fqList.add("businessType:" + INDEX_TYPE_PDWH_PAPER);
    String[] str = fqList.toArray(new String[fqList.size()]);
    query.addFilterQuery(str);
    query.setStart(start);
    query.setRows(size);
    query.setParam("fl", "*, score");

    /*
     * if (QueryFields.ORDER_YEAR.equals(queryFields.getOrderBy())) { query.addSort("pubYear",
     * ORDER.desc); query.addSort("fullText", ORDER.desc); } else { query.addSort("score", ORDER.desc);
     * query.addSort("fullText", ORDER.desc); }
     */

    // 排序start
    List<String> sort = new ArrayList<String>();
    if (StringUtils.isNotBlank(searchString)) {
      sort.add("score");
    }
    if (StringUtils.isNotBlank(queryFields.getOrderBy())) {
      if (!"DEFAULT".equals(queryFields.getOrderBy())) {
        sort.add(queryFields.getOrderBy());
      }
    }
    buildSort(sort);// 创建排序顺序
    for (String orderStr : sort) {
      query.addSort(orderStr, ORDER.desc);
    }

    // 排序end
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    Gson gs = new Gson();
    String resultStr = gs.toJson(qrs.getResults());
    String hlStr = gs.toJson(qrs.getHighlighting());
    Map<String, Object> rsMap = new HashMap<String, Object>();
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    rsMap.put(RESULT_HIGHLIGHT, hlStr);
    return rsMap;

  }

  private void buildSort(List<String> sort) {
    sort = Optional.ofNullable(sort).orElse(new ArrayList<String>());
    List<String> defaultSort = Arrays.asList("readCount", "score", "fullText", "downLoadCount", "pubCitations",
        "pubYear", "pubMonth", "pubDay");
    if (sort.contains("pubYear")) {
      sort.add("pubMonth");
      sort.add("pubDay");
    }
    for (String orderStr : defaultSort) {
      if (!sort.contains(orderStr)) {
        sort.add(orderStr);
      }
    }
  }

  private boolean isSearchByNameZh(String str, SolrServer server, String runEnv) throws SolrServerException {
    if (StringUtils.isEmpty(str)) {
      return false;
    }
    if (str.length() <= 2) {
      return true;
    } else if (str.length() < 5) {
      SolrQuery query = new SolrQuery();
      query.setRequestHandler("/username");
      String[] fq = {"env:" + runEnv, "businessType:" + INDEX_TYPE_PSN};
      query.addFilterQuery(fq);
      query.setQuery("\"" + str + "\"");
      QueryResponse qrs = server.query(query);
      Long rsCount = qrs.getResults().getNumFound();
      if (rsCount > 0) {
        return true;
      }
    }
    return false;
  }

  private boolean isSearchByNameEn(String str, SolrServer server, String runEnv) throws SolrServerException {
    if (StringUtils.isEmpty(str)) {
      return false;
    }
    String[] splitStr = str.split(" ");
    if (splitStr.length <= 2) {
      return true;
    } else if (splitStr.length < 4) {
      SolrQuery query = new SolrQuery();
      query.setRequestHandler("/username");
      String[] fq = {"env:" + runEnv, "businessType:" + INDEX_TYPE_PSN};
      query.addFilterQuery(fq);
      query.setQuery("\"" + str + "\"");
      QueryResponse qrs = server.query(query);
      Long rsCount = qrs.getResults().getNumFound();
      if (rsCount > 0) {
        return true;
      }
    }
    return false;
  }

  private String getPubQueryString(String searchString, boolean isDoi) {
    StringBuilder sbQueryString = new StringBuilder();
    if (StringUtils.isNotEmpty(searchString)) {
      if (isDoi) {
        sbQueryString.append("doi:").append(searchString);
      } else {
        searchString = searchString.toLowerCase().trim();
        sbQueryString.append("(zhTitle:").append(searchString);
        sbQueryString.append(" OR enTitle:").append(searchString);
        sbQueryString.append(" OR authors:").append(searchString);
        sbQueryString.append(" OR zhAbstract:").append(searchString);
        sbQueryString.append(" OR enAbstract:").append(searchString);
        sbQueryString.append(" OR zhKeywords:").append(searchString);
        sbQueryString.append(" OR enKeywords:").append(searchString);
        sbQueryString.append(")");
      }
    }
    String queryString = sbQueryString.toString();
    return queryString;
  }

  /*
   * 查询Patent:pubType=5
   * 
   * QueryFields 包含String searchString, Integer pubYear
   */
  @Override
  public Map<String, Object> queryPatents(Integer pageCount, Integer size, QueryFields queryFields)
      throws SolrServerException {
    Assert.notNull(queryFields, "queryFields不能为空！");
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");

    Integer patYear = queryFields.getPatYear();
    Integer patType = queryFields.getPatTypeId();
    String language = queryFields.getLanguage();
    String years = queryFields.getYears();

    SolrServer server = new HttpSolrServer(serverUrl);
    String queryString = queryFields.getSearchString();

    SolrQuery query = new SolrQuery();
    String location = this.getLocationStr(queryString);
    if (StringUtils.isNotEmpty(location) && queryFields.LANGUAGE_ZH.equals(language)) {
      String bqQuery = "organization:" + location + "^100";
      query.setParam("bp", bqQuery);
    }

    Integer start = 0;
    if (pageCount > 0) {
      start = (pageCount - 1) * size;
    }

    if (queryFields.LANGUAGE_ZH.equals(language)) {
      // 判断是否是库里边存在的中文名
      query.setRequestHandler("/patentbrowsezh");
      if (isSearchByNameZh(queryString, server, runEnv)) {
        query.setQuery("\"" + queryString + "\"");
      } else {
        query.setQuery(queryString);
      }
    } else {
      query.setRequestHandler("/patentbrowseen");
      if (isSearchByNameEn(queryString, server, runEnv)) {
        query.setQuery("\"" + queryString + "\"");
      } else {
        query.setQuery(queryString);
      }
    }

    query.setQuery(queryString);
    List<String> fqList = new ArrayList<String>();
    if (patYear != null) {
      fqList.add("patYear:" + patYear);
    } else if (StringUtils.isNotBlank(years)) {
      fqList.add("patYear:" + "(" + years.replaceAll(",", " ").trim() + ")");
    }
    if (patType != null) {
      if (patType == 7) {
        fqList.add("-patTypeId:(51 52 53)");
      } else {
        fqList.add("patTypeId:" + patType);
      }
    } else if (StringUtils.isNotBlank(queryFields.getPatTypeIdStr())) {
      fqList.add("patTypeId:" + "(" + queryFields.getPatTypeIdStr().replaceAll(",", " ").trim() + ")");
    }
    if (queryFields.getExcludedPubIds() != null && queryFields.getExcludedPubIds().size() != 0) {
      StringBuffer sb = new StringBuffer();
      for (Long pubId : queryFields.getExcludedPubIds()) {
        sb.append(String.valueOf(pubId));
        sb.append(" ");
      }
      String pubIdsStr = sb.toString().trim();
      pubIdsStr = "(" + pubIdsStr + ")";
      fqList.add("-patId:" + pubIdsStr);
    }

    // 根据收录（dbId）过滤
    if (StringUtils.isNotBlank(queryFields.getPubDBIds())) {
      String pubDBIds = queryFields.getPubDBIds().trim().replaceAll(",", " ");
      fqList.add("(pubDbIdList:(" + pubDBIds + ") OR pubDbId:(" + pubDBIds + "))");
    }
    /*
     * if (StringUtils.isNotEmpty(language) && !QueryFields.LANGUAGE_ALL.equals(language)) {
     * fqList.add("patLanguage:" + language); }
     */
    fqList.add("env:" + runEnv);
    fqList.add("businessType:" + INDEX_TYPE_PDWH_PAT);
    String[] str = fqList.toArray(new String[fqList.size()]);

    query.addFilterQuery(str);
    query.setRows(size);
    query.setStart(start);
    query.setParam("fl", "*, score");
    query.setFacet(true);
    query.setFacetMinCount(1);
    if (QueryFields.ORDER_YEAR.equals(queryFields.getOrderBy())) {
      query.setSort("patYear", ORDER.desc);
    }

    // 在第二次查询的时候才会加入统计，填充leftmenu
    if (QueryFields.SEQ_2.equals(queryFields.getSeqQuery())) {
      String[] fFields = {"patYear", "patTypeId", "patLanguage"};
      query.addFacetField(fFields);
    }
    query.addHighlightField("patTitle patAuthors");
    query.setHighlightSimplePre("<font style = 'color:#de5f0d;font-weight:bold'>");
    query.setHighlightSimplePost("</font>");
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    // String resultStr = qrs.getResults().toString();
    Gson gs = new Gson();
    String resultStr = gs.toJson(qrs.getResults());
    String hlStr = gs.toJson(qrs.getHighlighting());
    Map<String, Object> rsMap = new HashMap<String, Object>();
    // 在第二次查询的时候才会加入统计，填充leftmenu
    if (QueryFields.SEQ_2.equals(queryFields.getSeqQuery())) {
      List<FacetField> facetList = qrs.getFacetFields();
      rsMap.put(RESULT_FACET, facetList);
    }
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    rsMap.put(RESULT_HIGHLIGHT, hlStr);
    return rsMap;
  }

  private String getPatQueryString(String searchString) {
    StringBuilder sbQueryString = new StringBuilder();

    if (StringUtils.isNotEmpty(searchString)) {
      searchString = searchString.toLowerCase().trim();
      sbQueryString.append(" (zhPatTitle:").append(searchString);
      sbQueryString.append(" OR enPatTitle:").append(searchString);
      sbQueryString.append(" OR patAuthors:").append(searchString);
      // sbQueryString.append(" OR patTypeId:").append(searchString);
      sbQueryString.append(" OR organization:").append(searchString);
      sbQueryString.append(" OR enPatBrief:").append(searchString);
      sbQueryString.append(" OR zhPatBrief:").append(searchString);
      sbQueryString.append(")");
    }
    String queryString = sbQueryString.toString();
    return queryString;
  }

  /*
   * 查询用户(不包含在隐私列表中的用户)
   * 
   * QueryFields 包含String searchString
   */
  @Override
  public Map<String, Object> queryPersons(Integer pageCount, Integer size, QueryFields queryFields)
      throws SolrServerException {
    Assert.notNull(queryFields, "queryFields不能为空！");
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    SolrServer server = new HttpSolrServer(serverUrl);
    String queryString = queryFields.getSearchString();
    StringBuffer qString = new StringBuffer();// 抽取完人名机构名的到的q语句
    List<String> fqList = new ArrayList<String>();
    // 抽取出来的人名现在不放在fq中，而是使用(字段名:"值")的形式放在q中进行查询，放在fq中会导致检索出来的结果无法按相似度进行排序
    queryString = extractUserNameAndInsName(queryString, qString, queryFields.getUseNames(), queryFields.getInsNames());
    SolrQuery query = new SolrQuery();
    Integer start = 0;
    if (pageCount > 0) {
      start = (pageCount - 1) * size;
    }
    queryString = StringUtils.trimToEmpty(queryString);
    if (XmlUtil.containZhChar(queryString)) {
      query.setRequestHandler("/userbrowsezh");
    } else {
      query.setRequestHandler("/userbrowseen");
    }
    query.setQuery(StringUtils.isNotBlank(qString) ? qString.toString()
        : (StringUtils.isNotBlank(queryString) ? queryString : "*"));
    fqList.add("env:" + runEnv);
    fqList.add("businessType:" + INDEX_TYPE_PSN);
    // 添加隐私名单
    fqList.add("isPrivate:0");
    if (StringUtils.isNotEmpty(queryString) && NumberUtils.isDigits(queryString) && queryString.length() == 8) {
      fqList.add("openId:" + queryString);
    }
    // 移动端-排除好友psnId
    if ("mobileSearchFriend".equals(queryFields.getFromPage()) || "searchPersons".equals(queryFields.getFromPage())) {
      List<Long> psnIdsList = queryFields.getPsnIdList();
      if (CollectionUtils.isNotEmpty(psnIdsList)) {
        String strPsnId = "";
        for (Long psnId : psnIdsList) {
          strPsnId = strPsnId + psnId + " ";
        }
        strPsnId = strPsnId.trim();
        strPsnId = "(" + strPsnId + ")";
        fqList.add("-psnId:" + strPsnId);
      }
    }
    List<String> insNameList = queryFields.getInsNameList();
    if (CollectionUtils.isNotEmpty(insNameList)) {
      StringBuffer insNames = new StringBuffer();
      for (String insName : insNameList) {
        insNames.append("\"");
        insNames.append(insName);
        insNames.append("\" ");
      }
      fqList.add("-zhInsName:" + "(" + insNames.toString().trim() + ")");
      fqList.add("-enInsName:" + "(" + insNames.toString().trim() + ")");
    }

    List<Long> psnRegionIdList = queryFields.getPsnRegionIds();
    if (CollectionUtils.isNotEmpty(psnRegionIdList)) {
      String psnRegionIds = "";
      for (Long psnRegionId : psnRegionIdList) {
        psnRegionIds = psnRegionIds + psnRegionId + " ";
      }
      psnRegionIds = psnRegionIds.trim();
      psnRegionIds = "(" + psnRegionIds + ")";
      fqList.add("psnRegionId:" + psnRegionIds);
    }
    List<Integer> psnScienceAreaIdList = queryFields.getPsnScienceAreaIds();
    if (CollectionUtils.isNotEmpty(psnScienceAreaIdList)) {
      String psnScienceAreaIds = "";
      for (Integer psnScienceAreaId : psnScienceAreaIdList) {
        psnScienceAreaIds = psnScienceAreaIds + psnScienceAreaId + " ";
      }
      psnScienceAreaIds = psnScienceAreaIds.trim();
      psnScienceAreaIds = "(" + psnScienceAreaIds + ")";
      fqList.add("psnScienceArea:" + psnScienceAreaIds);
    }
    String[] str = fqList.toArray(new String[fqList.size()]);
    query.addFilterQuery(str);
    query.setStart(start);
    query.setRows(size);
    query.setParam("fl", "*, score");
    query.addHighlightField("psnName enPsnName title zhInsName enInsName zhUnit enUnit");
    query.setHighlightSimplePre("<font style = 'color:#de5f0d;font-weight:bold'>");
    query.setHighlightSimplePost("</font>");
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    // String resultStr = qrs.getResults().toString();
    Gson gs = new Gson();
    String resultStr = gs.toJson(qrs.getResults());
    String hlStr = gs.toJson(qrs.getHighlighting());
    Map<String, Object> rsMap = new HashMap<String, Object>();
    // 人员查询暂时没有统计数
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    rsMap.put(RESULT_HIGHLIGHT, hlStr);
    return rsMap;
  }

  /**
   * 抽取人名和机构名
   * 
   * @param qString
   * @return
   */
  private String extractUserNameAndInsName(String queryString, StringBuffer qString, Set<String> userNames,
      Set<String> insNames) {
    if (StringUtils.isNotBlank(queryString)) {
      queryString = queryString.toLowerCase();
      // 人名提取
      if (CollectionUtils.isNotEmpty(userNames)) {
        StringBuffer enNames = new StringBuffer();
        StringBuffer zhNames = new StringBuffer();
        for (String userName : userNames) {// 将人名从查询字符中提取出来
          if (XmlUtil.containZhChar(userName)) {
            zhNames.append("\"").append(userName).append("\" ");
          } else if (StringUtils.isNotEmpty(userName)) {
            enNames.append("\"").append(userName).append("\" ");
          }
          // 去除被提取的字符 --会导致高亮不准确-注释
          if (StringUtils.isNotBlank(userName)) {// 防止userName为""，导致将所有的单词被拆分为一个个字母
            queryString = queryString.replace(userName, " ");
          }
        }
        if (StringUtils.isNotEmpty(zhNames.toString().trim()) && StringUtils.isEmpty(enNames.toString().trim())) {
          qString.append("psnName:(").append(zhNames.toString().trim()).append(")^10000");
        } else if (StringUtils.isNotEmpty(enNames.toString().trim())
            && StringUtils.isEmpty(zhNames.toString().trim())) {
          qString.append("enPsnName:(").append(enNames.toString().trim()).append(")^10000");
        } else if (StringUtils.isNotEmpty(enNames.toString().trim())
            && StringUtils.isNotEmpty(zhNames.toString().trim())) {
          qString.append("(psnName:(").append(zhNames.toString().trim()).append(")^10000 OR enPsnName:(")
              .append(enNames.toString().trim()).append(")^10000)");
        }
      }

      // 机构名提取
      if (CollectionUtils.isNotEmpty(insNames)) {
        StringBuffer enNames = new StringBuffer();
        StringBuffer zhNames = new StringBuffer();
        for (String insName : insNames) {
          if (XmlUtil.containZhChar(insName)) {
            zhNames.append("\"").append(insName).append("\" ");
          } else if (StringUtils.isNotEmpty(insName)) {
            enNames.append("\"").append(insName).append("\" ");
          }
          // 去除被提取的字符--会导致高亮不准确-注释
          if (StringUtils.isNotBlank(insName)) {
            queryString = queryString.replace(insName, " ");
          }
        }
        if (StringUtils.isNotBlank(qString)) {
          qString.append(" AND ");
        }
        if (StringUtils.isNotEmpty(zhNames.toString().trim()) && StringUtils.isEmpty(enNames.toString().trim())) {
          qString.append("zhInsName:(").append(zhNames.toString().trim()).append(")^1000");
        } else if (StringUtils.isNotEmpty(enNames.toString().trim())
            && StringUtils.isEmpty(zhNames.toString().trim())) {
          qString.append("enInsName:(").append(enNames.toString().trim()).append(")^1000");
        } else if (StringUtils.isNotEmpty(enNames.toString().trim())
            && StringUtils.isNotEmpty(zhNames.toString().trim())) {
          qString.append("(zhInsName:(").append(zhNames.toString().trim()).append(")^1000 OR enPsnName:(")
              .append(enNames.toString().trim()).append(")^1000)");
        }
      }
      if (StringUtils.isNotBlank(queryString)) {
        if (StringUtils.isNotBlank(qString)) {
          qString.append(" AND ");
        }
        qString.append(queryString);
      }
      return queryString;
    }
    return "";
  }

  private String getUserQueryString(String searchString) {
    StringBuilder sbQueryString = new StringBuilder();
    if (StringUtils.isNotEmpty(searchString)) {
      searchString = searchString.toLowerCase().trim();
      sbQueryString.append("(psnName:").append(searchString);
      sbQueryString.append(" OR enPsnName:").append(searchString);
      sbQueryString.append(" OR title:").append(searchString);
      sbQueryString.append(" OR zhInsName:").append(searchString);
      sbQueryString.append(" OR enInsName:").append(searchString);
      sbQueryString.append(" OR zhUnit:").append(searchString);
      sbQueryString.append(" OR enUnit:").append(searchString);
      sbQueryString.append(")");
    }
    String queryString = sbQueryString.toString();
    return queryString;
  }

  @Override
  public Map<String, Object> getRcmdPatents(Integer pageCount, Integer size, String queryKwString, String categoryStr)
      throws SolrServerException {

    Assert.notNull(queryKwString, "queryKwString不能为空！");
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");

    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    Integer start = 0;
    if (pageCount > 0) {
      start = (pageCount - 1) * size;
    }
    query.setRequestHandler("/patentRcmd");
    query.setQuery(queryKwString);
    List<String> fqList = new ArrayList<String>();

    if (StringUtils.isNotEmpty(categoryStr)) {
      fqList.add("patCategoryRcmd:" + categoryStr);
    }
    fqList.add("env:" + runEnv);

    String[] str = fqList.toArray(new String[fqList.size()]);

    query.addFilterQuery(str);
    query.setRows(size);
    query.setStart(start);
    // query.setParam("fl", "*, score");
    query.setFacet(true);
    query.setFacetMinCount(1);
    query.setSort("patYearRcmd", ORDER.desc);

    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();

    Gson gs = new Gson();
    String resultStr = gs.toJson(qrs.getResults());
    Map<String, Object> rsMap = new HashMap<String, Object>();
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    return rsMap;

  }

  @Override
  public Map<String, Object> getRequestRcmdFromPatent(Integer pageCount, Integer size, String queryKwString,
      String categoryStr) throws SolrServerException {

    Assert.notNull(queryKwString, "queryKwString不能为空！");
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");

    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    Integer start = 0;
    if (pageCount > 0) {
      start = (pageCount - 1) * size;
    }
    query.setRequestHandler("/patentRequestRcmd");
    query.setQuery(queryKwString);
    List<String> fqList = new ArrayList<String>();

    if (StringUtils.isNotEmpty(categoryStr)) {
      fqList.add("patCategoryRcmd:" + categoryStr);
    }
    fqList.add("env:" + runEnv);

    String[] str = fqList.toArray(new String[fqList.size()]);

    query.addFilterQuery(str);
    query.setRows(size);
    query.setStart(start);
    // query.setParam("fl", "*, score");
    query.setFacet(true);
    query.setFacetMinCount(1);

    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();

    Gson gs = new Gson();
    String resultStr = gs.toJson(qrs.getResults());
    Map<String, Object> rsMap = new HashMap<String, Object>();
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    return rsMap;

  }

  @Override
  public Map<String, Object> queryPersonsYouMayKnow(Integer pageCount, Integer size, QueryFields queryFields)
      throws SolrServerException {
    Assert.notNull(queryFields, "queryFields不能为空！");
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");

    SolrServer server = new HttpSolrServer(serverUrl);
    String queryString = queryFields.getSearchString();
    SolrQuery query = new SolrQuery();
    Integer start = 0;
    if (pageCount > 0) {
      start = (pageCount - 1) * size;
    }
    query.setRequestHandler("/personsYouMayKnow");
    query.setQuery(queryString);
    List<String> fqList = new ArrayList<String>();
    fqList.add("env:" + runEnv);
    // 添加隐私名单
    fqList.add("isPrivate:0");
    // 排除好友psnId
    List<Long> psnIdsList = queryFields.getPsnIdList();
    if (CollectionUtils.isNotEmpty(psnIdsList)) {
      String strPsnId = "";
      for (Long psnId : psnIdsList) {
        strPsnId = strPsnId + psnId + " ";
      }
      strPsnId = strPsnId.trim();
      strPsnId = "(" + strPsnId + ")";
      fqList.add("-psnId:" + strPsnId);
    }

    String[] str = fqList.toArray(new String[fqList.size()]);
    query.addFilterQuery(str);
    query.setStart(start);
    query.setRows(size);
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    // String resultStr = qrs.getResults().toString();
    Gson gs = new Gson();
    String resultStr = gs.toJson(qrs.getResults());
    String hlStr = gs.toJson(qrs.getHighlighting());
    Map<String, Object> rsMap = new HashMap<String, Object>();
    // 人员查询暂时没有统计数
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    rsMap.put(RESULT_HIGHLIGHT, hlStr);
    return rsMap;
  }

  @Override
  public Map<String, Object> getSearchSuggestion(String searchString) throws SolrServerException {
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/suggest");
    query.setQuery(searchString);
    String[] str = {"env:" + runEnv};
    query.setFilterQueries(str);
    QueryResponse qrs = server.query(query);
    Integer rsCount = 0;
    String resultStr = "";// gs.toJson(qrs.getResults());
    ArrayList<String> suggestList = new ArrayList<String>();
    Map rsMap = (LinkedHashMap) qrs.getResponse().get("suggest");
    if (rsMap != null) {
      if (rsMap.get("keywordSuggester") != null) {
        SimpleOrderedMap simpleOrderedMap = (SimpleOrderedMap) rsMap.get("keywordSuggester");
        if (simpleOrderedMap != null) {
          NamedList list = (NamedList) simpleOrderedMap.getVal(0);
          rsCount = (Integer) list.getVal(0);
          ArrayList<NamedList> rsList = (ArrayList<NamedList>) list.getVal(1);
          for (NamedList som : rsList) {
            String term = (String) som.getVal(0);
            if (StringUtils.isNotEmpty(term)) {
              suggestList.add(term);
            }
          }
        }
      }
    }
    Gson gs = new Gson();
    Map<String, Object> rs = new HashMap<String, Object>();
    rs.put(RESULT_NUMFOUND, rsCount);
    rs.put(RESULT_ITEMS, suggestList);
    return rs;
  }

  /*
   * 在index时区分唯一id
   */
  public Long generateIdForIndex(Long id, String type) {
    // pub前缀为100000
    if (INDEX_TYPE_PDWH_PAPER.equalsIgnoreCase(type)) {
      String idString = String.valueOf(id);
      return Long.parseLong("100000" + idString);
    } else if (INDEX_TYPE_PSN.equalsIgnoreCase(type)) {
      // psn前缀为50
      String idString = String.valueOf(id);
      return Long.parseLong("50" + idString);
    } else if (INDEX_TYPE_PDWH_PAT.equalsIgnoreCase(type)) {
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

  @Override
  public Map<String, Object> getFundRecommend(Integer pageCount, Integer size, String regionEn, String regionZh,
      String categoryEn, String categoryZh, Integer qualification, Integer timeGap, Integer fundInsTypeEnterprise,
      Integer fundInsTypeResearchIns, Integer sortOrder) throws SolrServerException {
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/fundRecommend");
    List<String> fqList = new ArrayList<String>();
    fqList.add("env:" + runEnv);
    if (StringUtils.isNotEmpty(categoryEn)) {
      fqList.add("fundCategoryStrEn:" + this.constructFqStringForFund(categoryEn)
          + " OR (*:* NOT fundCategoryStrEn:[* TO *])");
    }
    if (StringUtils.isNotEmpty(categoryZh)) {
      fqList.add("fundCategoryStrZh:" + this.constructFqStringForFund(categoryZh)
          + " OR (*:* NOT fundCategoryStrZh:[* TO *])");
    }
    if (StringUtils.isNotEmpty(regionEn)) {// 国家级基金信息地区要求为空
      fqList
          .add("fundRegionStrEn:" + this.constructFqStringForFund(regionEn) + " OR (*:* NOT fundRegionStrEn:[* TO *])");
    }
    if (StringUtils.isNotEmpty(regionZh)) {
      fqList
          .add("fundRegionStrZh:" + this.constructFqStringForFund(regionZh) + " OR (*:* NOT fundRegionStrZh:[* TO *])");
    }
    if (qualification != null && qualification != -1) { // const_position中对应grades；
      fqList.add("fundDegreeAndTitleRequire1:[" + qualification + " TO *] OR (fundDegreeAndTitleRequire1:\"0\")");
    }
    if (fundInsTypeEnterprise != null) {
      fqList.add("fundInsTypeEnterprise:" + fundInsTypeEnterprise);
    }
    if (fundInsTypeResearchIns != null) {
      fqList.add("fundInsTypeResearchIns:" + fundInsTypeResearchIns);
    }

    if (timeGap != null && timeGap != 0) {
      switch (timeGap) {
        case 1: // 一周以内
          fqList.add("fundEndDate:{NOW/DAY-1DAY TO NOW/DAY+7DAY]");
          break;
        case 2: // 1个月
          fqList.add("fundEndDate:{NOW/DAY-1DAY TO NOW+1MONTH]");
          break;
        case 3: // 3个月
          fqList.add("fundEndDate:{NOW/DAY-1DAY TO NOW+3MONTH]");
          break;
        default:
          break;
      }
    } else {
      fqList.add("fundEndDate:{NOW/DAY-1DAY TO *]");
    }
    // fqList.add("fl:*");
    String[] fq = fqList.toArray(new String[fqList.size()]);
    // 添加按照截止日期从近到远排序
    if (sortOrder != null && sortOrder == 1) {
      query.addSort("fundEndDate", SolrQuery.ORDER.asc);
      query.addSort("fundId", SolrQuery.ORDER.desc);
    }
    query.setQuery("*:*");
    query.addFilterQuery(fq);
    /*
     * // 添加按照截止日期从近到远排序 if (sortOrder != null && sortOrder == 1) { query.addSort("fundEndDate",
     * SolrQuery.ORDER.asc); }
     */
    // query.setQuery("*");
    Integer start = 0;
    if (pageCount > 0) {
      start = (pageCount - 1) * size;
    }
    query.setStart(start);
    query.setRows(size);
    query.set("fl", "*,score");
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    Gson gs = new Gson();
    String resultStr = gs.toJson(qrs.getResults());
    Map<String, Object> rsMap = new HashMap<String, Object>();
    // 人员查询暂时没有统计数
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    return rsMap;
  }

  @Override
  public Map<String, Object> queryFundDetail(Long fundId) throws SolrServerException {
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    Map<String, Object> result = new HashMap<String, Object>();
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/fundRecommend");// 设置在哪个查询器下查询
    List<String> fqList = new ArrayList<String>();
    fqList.add("env:" + runEnv);
    fqList.add("fundId:" + fundId);
    String[] fq = fqList.toArray(new String[fqList.size()]);
    query.setQuery("*:*");
    query.addFilterQuery(fq);
    query.set("fl", "*");// 返回所有字段,默认返回分数score
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    Gson gs = new Gson();
    String resultStr = gs.toJson(qrs.getResults());
    result.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    result.put(RESULT_ITEMS, resultStr);
    return result;
  }

  @Override
  public Map<String, Object> getRecommendFundRecommend(Integer pageCount, Integer size, String agencyIds, String areaId,
      Integer qualification, Integer timeGap, Integer fundInsTypeEnterprise, Integer fundInsTypeResearchIns,
      Integer sortOrder, List<Long> excludeFundIds) throws SolrServerException {
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/fundRecommend");
    List<String> fqList = new ArrayList<String>();
    fqList.add("env:" + runEnv);
    if (StringUtils.isNotEmpty(areaId)) {
      fqList.add("fundCategoryIds:(" + areaId.trim() + ") OR (*:* NOT fundCategoryIds:[* TO *])");
    }
    if (StringUtils.isNotEmpty(agencyIds)) {// 关注的资助机构不为空
      fqList.add("fundAgencyId:(" + agencyIds.replace(",", " ") + ")");
    }
    if (qualification != null && qualification != -1) { // const_position中对应grades；
      fqList.add("fundDegreeAndTitleRequire1:[" + qualification + " TO *] OR (fundDegreeAndTitleRequire1:\"0\")");
    }
    if (fundInsTypeEnterprise != null) {
      fqList.add("fundInsTypeEnterprise:" + fundInsTypeEnterprise);
    }
    if (fundInsTypeResearchIns != null) {
      fqList.add("fundInsTypeResearchIns:" + fundInsTypeResearchIns);
    }

    if (timeGap != null && timeGap != 0) {
      switch (timeGap) {
        case 1: // 一周以内
          fqList.add("fundEndDate:{NOW/DAY-1DAY TO NOW/DAY+7DAY]");
          break;
        case 2: // 1个月
          fqList.add("fundEndDate:{NOW/DAY-1DAY TO NOW+1MONTH]");
          break;
        case 3: // 3个月
          fqList.add("fundEndDate:{NOW/DAY-1DAY TO NOW+3MONTH]");
          break;
        default:
          break;
      }
    } else {
      fqList.add("fundEndDate:{NOW/DAY-1DAY TO *]");
    }
    if (CollectionUtils.isNotEmpty(excludeFundIds)) {
      String strFundId = "";
      for (Long fundId : excludeFundIds) {
        strFundId = strFundId + fundId + " ";
      }
      strFundId = strFundId.trim();
      strFundId = "(" + strFundId + ")";
      fqList.add("-fundId:" + strFundId);
    }
    // fqList.add("fl:*");
    String[] fq = fqList.toArray(new String[fqList.size()]);
    // 添加按照截止日期从近到远排序
    if (sortOrder != null && sortOrder == 1) {
      query.addSort("fundEndDate", SolrQuery.ORDER.asc);
      query.addSort("fundId", SolrQuery.ORDER.desc);
    }
    query.setQuery("*:*");
    query.addFilterQuery(fq);
    /*
     * // 添加按照截止日期从近到远排序 if (sortOrder != null && sortOrder == 1) { query.addSort("fundEndDate",
     * SolrQuery.ORDER.asc); }
     */
    // query.setQuery("*");
    Integer start = 0;
    if (NumberUtils.isNotNullOrZero(pageCount)) {
      start = (pageCount - 1) * size;
    }
    query.setStart(start);
    query.setRows(size);
    query.set("fl", "*,score");
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    Gson gs = new Gson();
    String resultStr = gs.toJson(qrs.getResults());
    Map<String, Object> rsMap = new HashMap<String, Object>();
    // 人员查询暂时没有统计数
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    return rsMap;
  }

  // 为基金拼接fq中字段，str用逗号隔开
  private String constructFqStringForFund(String queryString) {
    String[] strs = StringUtils.split(queryString, ",");
    if (strs == null) {
      return null;
    }

    Integer length = strs.length;
    if (length == 1) {
      return "(\"" + strs[0] + "\")";
    } else if (length > 1) {
      StringBuilder sb = new StringBuilder();
      sb.append("(");
      for (Integer i = 0; i < length; i++) {
        sb.append("\"" + StringUtils.trim(strs[i]) + "\"");
        if (i < length - 1) {
          sb.append(" OR ");
        }
      }
      sb.append(")");
      return sb.toString();
    }
    return null;
  }

  @Override
  public Map<String, Object> getFundFind(Integer pageCount, Integer size, String searchKey, String regionEn,
      String regionZh, String categoryEn, String categoryZh, Integer fundInsTypeEnterprise,
      Integer fundInsTypeResearchIns, Integer sortOrder) throws SolrServerException {
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/fundRecommend");
    List<String> fqList = new ArrayList<String>();
    fqList.add("env:" + runEnv);
    if (StringUtils.isNotEmpty(categoryEn)) {
      fqList.add("fundCategoryStrEn:" + this.constructFqStringForFund(categoryEn)
          + " OR (*:* NOT fundCategoryStrEn:[* TO *])");
    }
    if (StringUtils.isNotEmpty(categoryZh)) {
      fqList.add("fundCategoryStrZh:" + this.constructFqStringForFund(categoryZh)
          + " OR (*:* NOT fundCategoryStrZh:[* TO *])");
    }
    if (StringUtils.isNotEmpty(regionEn)) {// 国家级基金信息地区要求为空
      String fql = "fundRegionStrEn:" + this.constructFqStringForFund(regionEn);
      if (regionEn.contains("Country")) {
        fql += " OR (*:* NOT fundRegionStrEn:[* TO *])";
      }
      fqList.add(fql);
    }
    if (StringUtils.isNotEmpty(regionZh)) {
      String fql = "fundRegionStrZh:" + this.constructFqStringForFund(regionZh);
      if (regionZh.contains("国家")) {
        fql += " OR (*:* NOT fundRegionStrZh:[* TO *])";
      }
      fqList.add(fql);
    }
    if (fundInsTypeEnterprise != null) {
      fqList.add("fundInsTypeEnterprise:" + fundInsTypeEnterprise);
    }
    if (fundInsTypeResearchIns != null) {
      fqList.add("fundInsTypeResearchIns:" + fundInsTypeResearchIns);
    }
    String[] fq = fqList.toArray(new String[fqList.size()]);
    // 添加按照截止日期从近到远排序
    if (sortOrder != null && sortOrder == 1) {
      query.addSort("fundEndDate", SolrQuery.ORDER.asc);
      query.addSort("fundId", SolrQuery.ORDER.desc);
    }
    if (StringUtils.isNotEmpty(searchKey)) {
      query.setQuery(searchKey);
    } else {
      query.setQuery("*:*");
    }
    query.addFilterQuery(fq);
    Integer start = 0;
    if (pageCount > 0) {
      start = (pageCount - 1) * size;
    }
    query.setStart(start);
    query.setRows(size);
    query.set("fl", "*,score");
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    Gson gs = new Gson();
    String resultStr = gs.toJson(qrs.getResults());
    Map<String, Object> rsMap = new HashMap<String, Object>();
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    return rsMap;
  }

  @Override
  public Map<String, Object> getInfoForPubInput(String pubTitle, Integer pubType) throws SolrServerException {
    Assert.notNull(pubTitle, "pubTitle不能为空！");
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");

    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    List<String> fqList = new ArrayList<String>();
    if (pubType == 3 || pubType == 4) {
      query.setRequestHandler("/pubTitleQueryForInput");
      fqList.add("pubTypeId:" + pubType);
      fqList.add("businessType:" + INDEX_TYPE_PDWH_PAPER);
    } else if (pubType == 5) {
      query.setRequestHandler("/patentTitleQueryForInput");
      fqList.add("businessType:" + INDEX_TYPE_PDWH_PAT);
    } else {
      query.setRequestHandler("/pubTitleQueryForInput");
      fqList.add("pubTypeId:" + pubType);
      fqList.add("businessType:" + INDEX_TYPE_PDWH_PAPER);
    }
    query.setQuery(pubTitle);
    fqList.add("env:" + runEnv);
    // fqList.add("env:run");
    String[] str = fqList.toArray(new String[fqList.size()]);
    query.addFilterQuery(str);
    query.setStart(0);
    query.setRows(5);
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    SolrDocumentList resultStr = qrs.getResults();
    Map<String, Object> rsMap = new HashMap<String, Object>();
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    return rsMap;
  }

  @Override
  public Map<String, Object> getPsnRecommendForFollow(String psnInsName, String psnScienceArea, String psnKeywords,
      Integer size, List<Long> exludedPsnIds, Long currentUserId) throws SolrServerException {
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/psnRecommendForFollowing");
    ArrayList<String> fqList = new ArrayList<String>();
    fqList.add("env:" + runEnv);
    fqList.add("businessType:person_index");
    fqList.add("isPrivate:0");
    if (StringUtils.isNotEmpty(psnInsName)) {
      fqList.add("zhInsName:\"" + psnInsName + "\" OR enInsName:\"" + psnInsName + "\"");
    }
    if (StringUtils.isNotEmpty(psnScienceArea)) {
      fqList.add("psnScienceArea:(" + psnScienceArea + ")");
    }
    // 用来补充人员不够时，排除已经有的psnId
    if (currentUserId != null) {
      StringBuffer psnIds = new StringBuffer();
      psnIds.append(String.valueOf(currentUserId));
      psnIds.append(" ");
      if (exludedPsnIds != null && exludedPsnIds.size() != 0) {
        for (Long psnId : exludedPsnIds) {
          psnIds.append(String.valueOf(psnId));
          psnIds.append(" ");
        }
      }
      fqList.add("-psnId:(" + StringUtils.trim(psnIds.toString()) + ")");
    }
    if (fqList.size() > 0) {
      query.setFilterQueries(fqList.toArray(new String[fqList.size()]));
    }
    if (StringUtils.isNotEmpty(psnKeywords)) {
      query.setQuery(psnKeywords);
    } else {
      query.setQuery("*");
    }
    query.setStart(0);
    if (size == null || size == 0) {
      query.setRows(50);
    } else {
      query.setRows(size.intValue());
    }
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    SolrDocumentList resultStr = qrs.getResults();
    // Gson gs = new Gson();
    // String resultStr = gs.toJson(qrs.getResults());
    Map<String, Object> rsMap = new HashMap<String, Object>();
    rsMap.put(RESULT_NUMFOUND, rsCount);
    rsMap.put(RESULT_ITEMS, resultStr);
    return rsMap;
  }

  @Override
  public Map<String, Object> getRecommendPubs(Integer pageCount, Integer size, QueryFields queryFields)
      throws SolrServerException {
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/pubRecommend");
    List<String> fqList = new ArrayList<String>();
    fqList.add("env:" + runEnv);
    fqList.add("businessType:" + INDEX_TYPE_PDWH_PAPER);

    String keywords = this.filterStr(queryFields.getSearchPsnKey());
    if (StringUtils.isNotEmpty(queryFields.getScienceAreaIds())) {
      fqList.add("pubCategory:(" + queryFields.getScienceAreaIds().trim() + ")");
    }
    if (StringUtils.isNotEmpty(keywords)) {
      fqList.add("pubTitle:" + keywords);
    }

    if (queryFields.getTimeGap() != null) {
      Calendar cal = Calendar.getInstance();
      Integer year = cal.get(Calendar.YEAR);
      switch (queryFields.getTimeGap()) {
        case 1: // 当年至今
          fqList.add("pubYear:[" + year + " TO *]");
          break;
        case 2: // 当年-2 至今
          fqList.add("pubYear:[" + (year - 2) + " TO *]");
          break;
        case 3: // 当年-5 至今
          fqList.add("pubYear:[" + (year - 4) + " TO *]");
          break;
        default:
          break;
      }
    }

    if (StringUtils.isNotEmpty(queryFields.getPubTypeIdStr())) {
      // pubType需用空格
      fqList.add("pubTypeId:(" + queryFields.getPubTypeIdStr().replace(",", " ") + ")");
    }

    // 排除pubId
    List<Long> pubIdsList = queryFields.getExcludedPubIds();
    if (CollectionUtils.isNotEmpty(pubIdsList)) {
      String strPubId = "";
      for (Long pubId : pubIdsList) {
        strPubId = strPubId + pubId + " ";
      }
      strPubId = strPubId.trim();
      strPubId = "(" + strPubId + ")";
      fqList.add("-pubId:" + strPubId);
    }

    String[] fq = fqList.toArray(new String[fqList.size()]);
    query.addFilterQuery(fq);
    // boost query，对不同词采取不同权重
    String bqQuery = "";
    String[] kws = StringUtils.split(queryFields.getSearchPsnKey(), ",");
    if (kws != null && kws.length >= 1) {
      bqQuery =
          "journalGrade:1^2500 journalGrade:2^2200 journalGrade:3^2000 journalGrade:4^1500 journalGrade:5^500 journalGrade:6^250 journalGrade:7^200 journalGrade:8^100 fullText:1^3300";
      Integer baseScore = 500;
      Integer diminishGap = 500 / kws.length;
      for (Integer i = 0; i < kws.length; i++) {
        String weight = String.valueOf(baseScore - i * diminishGap);
        // 查询前先去除所有标点符号
        bqQuery = bqQuery + " pubTitle:\"" + kws[i].replaceAll("[\\pP‘’“”]", "") + "\"^" + weight;
      }
    }
    if (StringUtils.isNotEmpty(bqQuery)) {
      query.setParam("bq", bqQuery);
    }

    query.setQuery("*:*");
    Integer start = 0;
    if (pageCount > 0) {
      start = (pageCount - 1) * size;
    }
    query.setStart(start);
    query.setRows(size);
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    Gson gs = new Gson();
    String resultStr = gs.toJson(qrs.getResults());
    Map<String, Object> rsMap = new HashMap<String, Object>();
    // 人员查询暂时没有统计数
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    return rsMap;
  }

  private String filterStr(String searchPsnKey) {
    if (StringUtils.isEmpty(searchPsnKey)) {
      return null;
    }
    String[] strCo = StringUtils.split(searchPsnKey, ",");
    Integer length = strCo.length;
    if (length == 1) {
      return "(\"" + strCo[0] + "\")";
    } else if (length > 1) {
      StringBuilder sb = new StringBuilder();
      sb.append("(");
      for (Integer i = 0; i < length; i++) {
        sb.append("\"" + StringUtils.trim(strCo[i]) + "\"");
        if (i < length - 1) {
          sb.append(" OR ");
        }
      }
      sb.append(")");
      return sb.toString();
    } else {
      return null;
    }
  }

  private String filterStrByLanguage(String str, String language) {
    if (StringUtils.isEmpty(str) || StringUtils.isEmpty(language)) {
      return null;
    }

    String[] strCo = StringUtils.split(str, ",");
    ArrayList<String> filterList = new ArrayList<String>();
    for (String subStr : strCo) {
      if ("Zh".equals(language)) {
        if (XmlUtil.containZhChar(subStr)) {
          filterList.add(subStr);
        }
      } else if ("En".equals(language)) {
        if (!XmlUtil.containZhChar(subStr)) {
          filterList.add(subStr);
        }
      }
    }

    Integer length = filterList.size();
    if (length == 1) {
      return "(\"" + filterList.get(0) + "\")";
    } else if (length > 1) {
      StringBuilder sb = new StringBuilder();
      sb.append("(");
      for (Integer i = 0; i < length; i++) {
        sb.append("\"" + StringUtils.trim(filterList.get(i)) + "\"");
        if (i < length - 1) {
          sb.append(" OR ");
        }
      }
      sb.append(")");
      return sb.toString();
    } else {
      return null;
    }
  }

  @Override
  public Long queryPubCountByKeywords(String keywords, Integer year) throws SolrServerException {
    Assert.isTrue(StringUtils.isNotEmpty(keywords), "查询关键词不能为空");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/pubbrowse");
    query.setQuery(keywords);
    query.addFilterQuery("pubYear:" + year);
    query.addFilterQuery("businessType:" + INDEX_TYPE_PDWH_PAPER);
    query.addFilterQuery("env:" + runEnv);
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    return rsCount;
  }

  @Override
  public Long queryPrjCountByKeywords(String keywords, Integer year) throws SolrServerException {
    Assert.isTrue(StringUtils.isNotEmpty(keywords), "查询关键词不能为空");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/jxprojectbrowse");
    query.setQuery(keywords);
    query.addFilterQuery("jxStatYear:" + year);
    query.addFilterQuery("businessType:" + INDEX_PRP_CODE);
    query.addFilterQuery("env:" + runEnv);
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    return rsCount;
  }

  @Override
  public Long queryPatCountByKeywords(String keywords, Integer year) throws SolrServerException {
    Assert.isTrue(StringUtils.isNotEmpty(keywords), "查询关键词不能为空");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/patentbrowse");
    query.setQuery(keywords);
    query.addFilterQuery("patYear:" + year);
    query.addFilterQuery("businessType:" + INDEX_TYPE_PDWH_PAT);
    query.addFilterQuery("env:" + runEnv);
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    return rsCount;
  }

  @Override
  public Long queryCiteCountByKeywords(String keywords, Integer year) throws SolrServerException {
    return null;
  }

  @Override
  public Long queryTechCountByKeywords(String keywords, Integer year) throws SolrServerException {
    return null;
  }

  @Override
  public Map<String, Object> queryProject(Integer pageCount, Integer size, QueryFields queryFields)
      throws SolrServerException {
    Assert.notNull(queryFields, "queryFields不能为空！");
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");

    String years = queryFields.getYears();

    SolrServer server = new HttpSolrServer(serverUrl);
    String queryString = queryFields.getSearchString();
    SolrQuery query = new SolrQuery();
    Integer start = 0;
    if (pageCount > 0) {
      start = (pageCount - 1) * size;
    }
    query.setRequestHandler("/jxprojectbrowse");
    query.setQuery(queryString);
    List<String> fqList = new ArrayList<String>();
    fqList.add("env:" + runEnv);
    if (years != null) {
      fqList.add("jxStatYear:" + years);
    }
    String[] str = fqList.toArray(new String[fqList.size()]);

    query.addFilterQuery(str);
    query.setRows(size);
    query.setStart(start);
    query.setParam("fl", "*, score");
    query.setFacet(true);
    query.setFacetMinCount(1);
    if (QueryFields.ORDER_YEAR.equals(queryFields.getOrderBy())) {
      query.setSort("jxStatYear", ORDER.desc);
    }

    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    // String resultStr = qrs.getResults().toString();
    Gson gs = new Gson();
    String resultStr = gs.toJson(qrs.getResults());
    String hlStr = gs.toJson(qrs.getHighlighting());
    Map<String, Object> rsMap = new HashMap<String, Object>();
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    rsMap.put(RESULT_HIGHLIGHT, hlStr);
    return rsMap;
  }

  @Override
  public Map<String, Long> queryPubCountByRegionId(String keywords, Long regionId) throws SolrServerException {
    Assert.isTrue(StringUtils.isNotEmpty(keywords), "查询关键词不能为空");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/pubbrowse");
    query.setQuery(keywords);
    query.addFilterQuery("provinceId:" + regionId);
    query.addFilterQuery("businessType:" + INDEX_TYPE_PDWH_PAPER);
    query.addFilterQuery("env:" + runEnv);
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    Map<String, Long> rsMap = new HashMap<>();
    rsMap.put("rsCount", rsCount);
    rsMap.put("provinceId", regionId);
    return rsMap;
  }

  @Override
  public Map<String, Long> queryPatCountByRegionId(String keywords, Long regionId) throws SolrServerException {
    Assert.isTrue(StringUtils.isNotEmpty(keywords), "查询关键词不能为空");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/patentbrowse");
    query.setQuery(keywords);
    query.addFilterQuery("provinceId:" + regionId);
    query.addFilterQuery("businessType:" + INDEX_TYPE_PDWH_PAT);
    query.addFilterQuery("env:" + runEnv);
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    Map<String, Long> rsMap = new HashMap<String, Long>();
    rsMap.put("rsCount", rsCount);
    rsMap.put("provinceId", regionId);
    return rsMap;
  }

  @Override
  public Map<String, Long> queryPrjCountByRegionId(String keywords, Long regionId) throws SolrServerException {
    Assert.isTrue(StringUtils.isNotEmpty(keywords), "查询关键词不能为空");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/jxprojectbrowse");
    query.setQuery(keywords);
    query.addFilterQuery("jxProvinceId:" + regionId);
    query.addFilterQuery("businessType:" + INDEX_PRP_CODE);
    query.addFilterQuery("env:" + runEnv);
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    Map<String, Long> rsMap = new HashMap<String, Long>();
    rsMap.put("rsCount", rsCount);
    rsMap.put("provinceId", regionId);
    return rsMap;
  }

  @Override
  public Map<String, Long> queryTechCountByRegionId(String keywords, Long regionId) throws SolrServerException {
    return null;

  }

  @Override
  public Long queryCountPub(String keywords, Integer year, long provinceId) throws SolrServerException {
    Assert.isTrue(StringUtils.isNotEmpty(keywords), "查询关键词不能为空");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/pubbrowse");
    query.setQuery(keywords);
    query.addFilterQuery("provinceId:" + provinceId);
    query.addFilterQuery("pubYear:" + year);
    query.addFilterQuery("businessType:" + INDEX_TYPE_PDWH_PAPER);
    query.addFilterQuery("env:" + runEnv);
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    return rsCount;
  }

  @Override
  public Long queryCountPat(String keywords, Integer year, long provinceId) throws SolrServerException {
    Assert.isTrue(StringUtils.isNotEmpty(keywords), "查询关键词不能为空");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/patentbrowse");
    query.setQuery(keywords);
    query.addFilterQuery("provinceId:" + provinceId);
    query.addFilterQuery("patYear:" + year);
    query.addFilterQuery("businessType:" + INDEX_TYPE_PDWH_PAT);
    query.addFilterQuery("env:" + runEnv);
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    return rsCount;
  }

  @Override
  public Long queryCountPrj(String keywords, Integer year, long provinceId) throws SolrServerException {
    Assert.isTrue(StringUtils.isNotEmpty(keywords), "查询关键词不能为空");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/jxprojectbrowse");
    query.setQuery(keywords);
    query.addFilterQuery("jxProvinceId:" + provinceId);
    query.addFilterQuery("jxStatYear:" + year);
    query.addFilterQuery("businessType:" + INDEX_PRP_CODE);
    query.addFilterQuery("env:" + runEnv);
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    return rsCount;
  }

  @Override
  public Long queryCountTech(String keywords, Integer year, long provinceId) throws SolrServerException {
    // TODO Auto-generated method stub
    return null;
  }

  private String getLocationStr(String str) {
    String location = "";
    try {
      Segment seg1 = HanLP.newSegment().enableOrganizationRecognize(true);
      List<Term> oList = seg1.seg(str);
      for (Term t : oList) {
        if (t.nature != null) {
          String nature = t.nature.name();
          if ("ns".equals(nature) || "nt".equals(nature)
              || "nt".equalsIgnoreCase(StringUtils.substring(nature, 0, 2))) {
            location = location + t.word;
          }
        }
      }
    } catch (Exception e) {
      // 吃掉异常，当传递数字时seg1.seg(str);会报异常
      logger.error("getLocationStr出错", e);
    }
    return location;
  }

  @Override
  public Map<String, Object> queryPersonsDemo(Integer pageCount, Integer size, QueryFields queryFields)
      throws SolrServerException {
    Assert.notNull(queryFields, "queryFields不能为空！");
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");

    SolrServer server = new HttpSolrServer(serverUrl);
    String queryString = queryFields.getSearchString();
    SolrQuery query = new SolrQuery();
    Integer start = 0;
    if (pageCount > 0) {
      start = (pageCount - 1) * size;
    }
    query.setRequestHandler("/demouserbrowse");
    query.setQuery(queryString);
    List<String> fqList = new ArrayList<String>();
    fqList.add("env:" + runEnv);
    // 添加隐私名单
    fqList.add("isPrivate:0");
    // 移动端-排除好友psnId
    if ("mobileSearchFriend".equals(queryFields.getFromPage()) || "searchPersons".equals(queryFields.getFromPage())) {
      List<Long> psnIdsList = queryFields.getPsnIdList();
      if (CollectionUtils.isNotEmpty(psnIdsList)) {
        String strPsnId = "";
        for (Long psnId : psnIdsList) {
          strPsnId = strPsnId + psnId + " ";
        }
        strPsnId = strPsnId.trim();
        strPsnId = "(" + strPsnId + ")";
        fqList.add("-psnId:" + strPsnId);
      }
    }
    List<String> insNameList = queryFields.getInsNameList();
    if (CollectionUtils.isNotEmpty(insNameList)) {
      StringBuffer insNames = new StringBuffer();
      for (String insName : insNameList) {
        insNames.append("\"");
        insNames.append(insName);
        insNames.append("\" ");

      }
      fqList.add("-zhInsName:" + "(" + insNames.toString().trim() + ")");
      fqList.add("-enInsName:" + "(" + insNames.toString().trim() + ")");
    }

    List<Long> psnRegionIdList = queryFields.getPsnRegionIds();
    if (CollectionUtils.isNotEmpty(psnRegionIdList)) {
      String psnRegionIds = "";
      for (Long psnRegionId : psnRegionIdList) {
        psnRegionIds = psnRegionIds + psnRegionId + " ";
      }
      psnRegionIds = psnRegionIds.trim();
      psnRegionIds = "(" + psnRegionIds + ")";
      fqList.add("psnRegionId:" + psnRegionIds);
    }
    List<Integer> psnScienceAreaIdList = queryFields.getPsnScienceAreaIds();
    if (CollectionUtils.isNotEmpty(psnScienceAreaIdList)) {
      String psnScienceAreaIds = "";
      for (Integer psnScienceAreaId : psnScienceAreaIdList) {
        psnScienceAreaIds = psnScienceAreaIds + psnScienceAreaId + " ";
      }
      psnScienceAreaIds = psnScienceAreaIds.trim();
      psnScienceAreaIds = "(" + psnScienceAreaIds + ")";
      fqList.add("psnScienceArea:" + psnScienceAreaIds);
    }
    String[] str = fqList.toArray(new String[fqList.size()]);
    query.addFilterQuery(str);
    query.setStart(start);
    query.setRows(size);
    query.setParam("fl", "*, score");
    query.addHighlightField("psnName enPsnName title zhInsName enInsName zhUnit enUnit");
    query.setHighlightSimplePre("<font style = 'color:#de5f0d;font-weight:bold'>");
    query.setHighlightSimplePost("</font>");
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    // String resultStr = qrs.getResults().toString();
    Gson gs = new Gson();
    String resultStr = gs.toJson(qrs.getResults());
    String hlStr = gs.toJson(qrs.getHighlighting());
    Map<String, Object> rsMap = new HashMap<String, Object>();
    // 人员查询暂时没有统计数
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    rsMap.put(RESULT_HIGHLIGHT, hlStr);
    return rsMap;
  }

  @Deprecated
  @Override
  public Map<String, Object> findPubs(Integer pageCount, Integer size, QueryFields queryFields)
      throws SolrServerException {
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/pubRecommend");
    List<String> fqList = new ArrayList<String>();
    fqList.add("env:" + runEnv);
    fqList.add("businessType:" + INDEX_TYPE_PDWH_PAPER);
    // 科技领域
    if (StringUtils.isNotEmpty(queryFields.getScienceAreaIds())) {
      fqList.add("pubCategory:(" + queryFields.getScienceAreaIds().trim() + ")");
    }
    // 检索字符串
    String searchStringEn = this.filterStrByLanguage(queryFields.getSearchString(), "En");
    String searchStringZh = this.filterStrByLanguage(queryFields.getSearchString(), "Zh");
    if (StringUtils.isNotEmpty(searchStringEn) && StringUtils.isEmpty(searchStringZh)) {
      fqList.add("enTitle:" + searchStringEn);
    }
    if (StringUtils.isNotEmpty(searchStringZh) && StringUtils.isEmpty(searchStringEn)) {
      fqList.add("zhTitle:" + searchStringZh);
    }
    if (StringUtils.isNotEmpty(searchStringZh) && StringUtils.isNotEmpty(searchStringEn)) {
      fqList.add("enTitle:" + searchStringEn + " OR zhTitle:" + searchStringZh);
    }
    // 年份
    if (queryFields.getTimeGap() != null) {
      Calendar cal = Calendar.getInstance();
      Integer year = cal.get(Calendar.YEAR);
      switch (queryFields.getTimeGap()) {
        case 1: // 当年至今
          fqList.add("pubYear:[" + year + " TO *]");
          break;
        case 2: // 当年-2 至今
          fqList.add("pubYear:[" + (year - 2) + " TO *]");
          break;
        case 3: // 当年-5 至今
          fqList.add("pubYear:[" + (year - 4) + " TO *]");
          break;
        default:
          break;
      }
    }
    // 成果类型
    if (StringUtils.isNotEmpty(queryFields.getPubTypes())) {
      // pubType需用空格
      fqList.add("pubTypeId:(" + queryFields.getPubTypes().trim() + ")");
    }
    // 收录类别
    if (StringUtils.isNotBlank(queryFields.getPubDBIds())) {
      fqList.add("pubDbIdList:(" + queryFields.getPubDBIds().trim() + ")");
    }
    String[] fq = fqList.toArray(new String[fqList.size()]);
    query.addFilterQuery(fq);
    query.setQuery("*:*");
    Integer start = 0;
    if (pageCount > 0) {
      start = (pageCount - 1) * size;
    }
    query.setStart(start);
    query.setRows(size);
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    Gson gs = new Gson();
    String resultStr = gs.toJson(qrs.getResults());
    Map<String, Object> rsMap = new HashMap<String, Object>();
    // 人员查询暂时没有统计数
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    return rsMap;
  }

  @Override
  public QueryResponse query(SolrQuery query, List<String> fqList) throws SolrServerException {
    Assert.notNull(query, "query不能为空！");
    fqList.add("env:" + runEnv);
    String[] str = fqList.toArray(new String[fqList.size()]);
    query.addFilterQuery(str);
    SolrServer server = new HttpSolrServer(serverUrl);
    QueryResponse qrs = server.query(query);
    return qrs;
  }

  /**
   * 
   * 查询相关关键词co-tf，使用hash值，准确查询
   * 
   * @throws SolrServerException
   * 
   */
  @Override
  public Map<String, Object> queryRelatedKwsByKwHash(String kw, String discode, String queryType)
      throws SolrServerException {
    // runEnv = "run";
    if (StringUtils.isEmpty(kw)) {
      return null;
    }
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    String serverKwUrl = serverUrl + "collection_tf_cotf";
    SolrServer server = new HttpSolrServer(serverKwUrl);
    SolrQuery query = new SolrQuery();
    // query.setRequestHandler("/kwsCoTfByNsfcPrj");
    query.setRequestHandler("/kwsCoTfByNsfcPrjByHash");
    List<String> fqList = new ArrayList<String>();
    fqList.add("env:" + runEnv);
    fqList.add("businessType:" + NSFC_COTF);
    if (StringUtils.isNotEmpty(discode)) {
      if ("1".equals(queryType)) {
        Integer length = discode.length();
        switch (length) {
          case 3:
            fqList.add("nsfcDisCode_1:" + discode);
            break;
          case 5:
            fqList.add("nsfcDisCode_2:" + discode);
            break;
          case 7:
            fqList.add("nsfcDisCode_3:" + discode);
            break;
          default:
            fqList.add("nsfcDisCode:" + discode);
            break;
        }
      } else {
        fqList.add("nsfcDisCode:" + discode);
      }
    }
    Long kwHash = HashUtils.getStrHashCode(kw);
    // query.setQuery("\""+kw+"\"");
    query.setQuery(String.valueOf(kwHash));
    query.addFilterQuery(fqList.toArray(new String[fqList.size()]));
    query.addFacetField("secondKw");
    query.setFacetLimit(2000);
    query.setFacetMinCount(2);
    query.setStart(0);
    query.setRows(500);
    QueryResponse qrs = server.query(query);
    SolrDocumentList sdl = qrs.getResults();
    Map<String, Object> rsMap = new HashMap<String, Object>();
    if (sdl != null && sdl.size() != 0) {
      Set<String> relatedKwSet = new HashSet<String>();
      for (SolrDocument sd : sdl) {
        String rekw = String.valueOf(sd.getFieldValue("secondKw"));
        if (StringUtils.isNotBlank(rekw)) {
          relatedKwSet.add(rekw);
        }
      }
      Map<String, Long> fMap = new HashMap<String, Long>();
      FacetField ff = qrs.getFacetField("secondKw");
      if (ff != null) {
        for (Count c : ff.getValues()) {
          fMap.put(c.getName(), c.getCount());
        }
      }
      rsMap.put(RESULT_NUMFOUND, String.valueOf(relatedKwSet.size()));
      rsMap.put(RESULT_ITEMS, relatedKwSet);
      rsMap.put(RESULT_FACET, fMap);
    }
    return rsMap;
  }

  /**
   * 
   * 查询相关键词的cotf,只采用字符本身查询，范围更大
   * 
   * @throws SolrServerException
   * 
   */
  @Override
  public Map<String, Object> queryRelatedKws(String kw, String discode, String queryType) throws SolrServerException {
    // runEnv = "run";
    if (StringUtils.isEmpty(kw)) {
      return null;
    }
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    String serverKwUrl = serverUrl + "collection_tf_cotf";
    SolrServer server = new HttpSolrServer(serverKwUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/kwsCoTfByNsfcPrj");
    List<String> fqList = new ArrayList<String>();
    fqList.add("env:" + runEnv);
    fqList.add("businessType:" + NSFC_COTF);
    if (StringUtils.isNotEmpty(discode)) {
      if ("1".equals(queryType)) {
        Integer length = discode.length();
        switch (length) {
          case 3:
            fqList.add("nsfcDisCode_1:" + discode);
            break;
          case 5:
            fqList.add("nsfcDisCode_2:" + discode);
            break;
          case 7:
            fqList.add("nsfcDisCode_3:" + discode);
            break;
          default:
            fqList.add("nsfcDisCode:" + discode);
            break;
        }
      } else {
        fqList.add("nsfcDisCode:" + discode);
      }
    }
    query.setQuery("\"" + kw + "\"");
    query.addFilterQuery(fqList.toArray(new String[fqList.size()]));
    query.addFacetField("secondKw");
    query.setFacetLimit(2000);
    query.setFacetMinCount(2);
    query.setStart(0);
    query.setRows(500);
    QueryResponse qrs = server.query(query);
    SolrDocumentList sdl = qrs.getResults();
    Map<String, Object> rsMap = new HashMap<String, Object>();
    if (sdl != null && sdl.size() != 0) {
      Set<String> relatedKwSet = new HashSet<String>();
      for (SolrDocument sd : sdl) {
        String rekw = String.valueOf(sd.getFieldValue("secondKw"));
        if (StringUtils.isNotBlank(rekw)) {
          relatedKwSet.add(rekw);
        }
      }
      Map<String, Long> fMap = new HashMap<String, Long>();
      FacetField ff = qrs.getFacetField("secondKw");
      if (ff != null) {
        for (Count c : ff.getValues()) {
          fMap.put(c.getName(), c.getCount());
        }
      }
      rsMap.put(RESULT_NUMFOUND, String.valueOf(relatedKwSet.size()));
      rsMap.put(RESULT_ITEMS, relatedKwSet);
      rsMap.put(RESULT_FACET, fMap);
    }
    return rsMap;
  }

  @Override
  public Long queryKwTf(String kw, String discode, String queryType) throws SolrServerException {
    // runEnv = "run";
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    String serverKwUrl = serverUrl + "collection_tf_cotf";
    SolrServer server = new HttpSolrServer(serverKwUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/kwTfByNsfcPrj");
    List<String> fqList = new ArrayList<String>();
    fqList.add("env:" + runEnv);
    fqList.add("businessType:" + "");
    if (StringUtils.isNotEmpty(discode)) {
      if ("1".equals(queryType)) {
        Integer length = discode.length();
        switch (length) {
          case 3:
            fqList.add("nsfcDisCode_1:" + discode);
            break;
          case 5:
            fqList.add("nsfcDisCode_2:" + discode);
            break;
          case 7:
            fqList.add("nsfcDisCode_3:" + discode);
            break;
          default:
            fqList.add("nsfcDisCode:" + discode);
            break;
        }
      } else {
        fqList.add("nsfcDisCode:" + discode);
      }
    }
    query.setQuery("\"" + kw + "\"");
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    return rsCount;
  }

  public static void main(String[] args) {
    SolrIndexSerivceImpl sisi = new SolrIndexSerivceImpl();
    // sisi.serverKwUrl =
    // "http://192.168.15.192:28080/solr/collection_tf_cotf/";
    String str = "马建 香港城市大学";
    String name = "马建";
    str = str.replace(name, " ");
    System.out.println(str);

    sisi.runEnv = "development";
    String kw = "响应";
    try {
      Map<String, Object> rs = sisi.queryRelatedKws(kw, null, null);
      Integer count = Integer.parseInt((String) rs.get(RESULT_NUMFOUND));
      Set<String> rSet = (Set<String>) rs.get(RESULT_ITEMS);
      System.out.println(count);
      System.out.println(rSet);
    } catch (SolrServerException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * 根据人名检索成果
   * 
   * @param server
   * @param psnName
   * @throws SolrServerException
   * @author LIJUN
   * @date 2018年5月14日
   */
  public List<Map<String, Object>> searchPubByAuthorName(String md5Name, int page) throws SolrServerException {
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/pubbrowse");
    Integer start = 0;
    if (page > 0) {
      start = (page - 1) * 100;
    }
    query.setStart(start);
    query.set("q", "*:*");
    // 该参数就是控制条数
    query.set("rows", 100);
    List<String> fqList = new ArrayList<String>();
    fqList.add("env:" + runEnv);
    fqList.add("businessType:" + INDEX_TYPE_PDWH_PAPER);
    fqList.add("md5Authors:" + "\"" + md5Name + "\"");
    query.addField("pubId,cleanAuthors,pubOrganization,pubYear");
    String[] fq = fqList.toArray(new String[fqList.size()]);
    query.addFilterQuery(fq);
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    if (rsCount == null || rsCount == 0L) {
      return null;
    }
    String resultStr = JacksonUtils.jsonObjectSerializer(qrs.getResults());
    List<Map<String, Object>> jsonToList = JacksonUtils.jsonToList(resultStr);
    List<Map<String, Object>> publist = new ArrayList<>();
    for (Map<String, Object> object : jsonToList) {
      Map<String, Object> pubinfo = new HashMap<>();
      pubinfo.put("pubId", object.get("pubId"));
      pubinfo.put("authors", object.get("cleanAuthors"));
      pubinfo.put("organization", object.get("pubOrganization"));
      pubinfo.put("md5Authors", object.get("md5Authors"));
      pubinfo.put("pubYear", object.get("pubYear"));
      publist.add(pubinfo);
    }
    return publist;

  }

  /**
   * 通过作者名，检索专利
   * 
   * @param server
   * @param psnName
   * @return
   * @throws SolrServerException
   * @author LIJUN
   * @date 2018年5月23日
   */

  @Override
  public List<Map<String, Object>> searchPatentByAuthorName(String psnName, int page) throws SolrServerException {
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/patentbrowse");
    Integer start = 0;
    if (page > 0) {
      start = (page - 1) * 10;
    }
    query.setStart(start);
    query.set("q", "*:*");
    // 该参数就是控制条数
    query.set("rows", 100);
    List<String> fqList = new ArrayList<String>();
    fqList.add("env:" + runEnv);
    fqList.add("businessType:" + INDEX_TYPE_PDWH_PAT);
    fqList.add("cleanPatAuthors:" + "\"" + psnName + "\"");
    query.addField("patId,cleanPatAuthors,organization,pubYear");
    String[] fq = fqList.toArray(new String[fqList.size()]);
    query.addFilterQuery(fq);
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    if (rsCount == null || rsCount == 0L) {
      return null;
    }
    String resultStr = JacksonUtils.jsonObjectSerializer(qrs.getResults());
    List<Map<String, Object>> jsonToList = JacksonUtils.jsonToList(resultStr);
    List<Map<String, Object>> publist = new ArrayList<>();
    for (Map<String, Object> object : jsonToList) {
      Map<String, Object> pubinfo = new HashMap<>();
      pubinfo.put("pubId", object.get("pubId"));
      pubinfo.put("pubYear", object.get("pubYear"));
      pubinfo.put("authors", object.get("cleanPatAuthors"));
      pubinfo.put("organization", object.get("organization"));
      pubinfo.put("md5Authors", object.get("md5PatAuthors"));
      publist.add(pubinfo);
    }
    return publist;
  }

  @Override
  public Map<String, Object> queryPersonsByPsnKw(Integer pageCount, Integer size, QueryFields queryFields)
      throws SolrServerException {
    Assert.notNull(queryFields, "queryFields不能为空！");
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    SolrServer server = new HttpSolrServer(serverUrl);
    String queryString = queryFields.getSearchString();
    List<String> fqList = new ArrayList<String>();

    SolrQuery query = new SolrQuery();
    Integer start = 0;
    if (pageCount > 0) {
      start = (pageCount - 1) * size;
    }
    queryString = StringUtils.trimToEmpty(queryString);
    query.setRequestHandler("/userbrowse");
    query.setQuery("*");
    if (StringUtils.isNotBlank(queryFields.getKeywords())) {
      fqList.add("psnKeywords:" + queryFields.getKeywords());
    }
    fqList.add("env:" + runEnv);
    fqList.add("businessType:" + INDEX_TYPE_PSN);
    // 添加隐私名单
    fqList.add("isPrivate:0");
    String[] str = fqList.toArray(new String[fqList.size()]);
    query.addFilterQuery(str);
    query.setStart(start);
    query.setRows(size);
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    Gson gs = new Gson();
    String resultStr = gs.toJson(qrs.getResults());
    Map<String, Object> rsMap = new HashMap<String, Object>();
    // 人员查询暂时没有统计数
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    return rsMap;
  }

  @Override
  public Map<String, Object> queryPubsNew(Integer pageCount, Integer size, QueryFields queryFields)
      throws SolrServerException {
    Assert.notNull(queryFields, "queryFields不能为空！");
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    Integer pubYear = queryFields.getPubYear();
    Integer pubType = queryFields.getPubTypeId();
    String years = queryFields.getYears();
    String addr = queryFields.getAddr();
    String authorName = queryFields.getName();
    boolean isDoi = queryFields.getIsDoi();
    SolrServer server = new HttpSolrServer(serverUrl);
    String queryString = queryFields.getSearchString();
    Set<String> userNameSet = queryFields.getUseNames();
    Set<String> insNameSet = queryFields.getInsNames();
    SolrQuery query = new SolrQuery();
    List<String> fqList = new ArrayList<String>();
    Integer start = 0;
    if (pageCount > 0) {
      start = (pageCount - 1) * size;
    }
    if (isDoi) {
      query.setRequestHandler("/pubbrowsebydoi");
      query.addHighlightField("doi");
    } else {
      String bqQuery =
          "journalGrade:1^2500 journalGrade:2^2200 journalGrade:3^2000 journalGrade:4^1500 journalGrade:5^500 journalGrade:6^250 journalGrade:7^200 journalGrade:8^100 fullText:1^3300";
      query.setParam("bp", bqQuery);
      query.setRequestHandler("/pubbrowse");
      query.addHighlightField("title authors keywords");
      queryString = StringUtils.trimToEmpty(queryString);
      // 构造查询语句
      if (StringUtils.isNotEmpty(addr)) {
        // 提取到的
        fqList.add("pubOrganization:(" + addr + ")");
        if (insNameSet != null && insNameSet.size() > 0 && StringUtils.isNotEmpty(queryString)) {
          for (String addrStr : insNameSet) {
            queryString = queryString.replace(addrStr, "");
          }
          queryString = StringUtils.trimToEmpty(queryString);
        }
      }
      if (StringUtils.isNotEmpty(queryFields.getMd5Author())) {
        fqList.add("md5Authors:(" + queryFields.getMd5Author() + ")");
      } else if (StringUtils.isNotEmpty(authorName)) {
        fqList.add("authors:(" + authorName + ")");
      }

      if (userNameSet != null && userNameSet.size() > 0 && StringUtils.isNotEmpty(queryString)) {
        for (String userStr : userNameSet) {
          queryString = queryString.replace(userStr, "");
        }
        queryString = StringUtils.trimToEmpty(queryString);
      }

      if (StringUtils.isNotEmpty(queryString)) {
        query.setQuery(queryString);
      }
    }

    if (pubYear != null) {
      fqList.add("pubYear:" + pubYear);
    } else if (StringUtils.isNotBlank(years)) {
      fqList.add("pubYear:" + "(" + years.trim().replaceAll(",", " ") + ")");
    }

    // 优先使用list查询
    if (queryFields.getPubTypeIdList() != null && queryFields.getPubTypeIdList().size() != 0) {
      StringBuffer sb = new StringBuffer();
      for (Integer pubTypeId : queryFields.getPubTypeIdList()) {
        sb.append(String.valueOf(pubTypeId));
        sb.append(" ");
      }
      String pubTypeIdsStr = sb.toString().trim();
      pubTypeIdsStr = "(" + pubTypeIdsStr + ")";
      fqList.add("pubTypeId:" + pubTypeIdsStr);
    } else {
      if (pubType != null) {
        if (pubType == 7) {
          fqList.add("pubTypeId:(7 1 2 5 10 12 13)");
        } else {
          fqList.add("pubTypeId:" + pubType);
        }
      } else if (StringUtils.isNotBlank(queryFields.getPubTypeIdStr())) {
        fqList.add("pubTypeId:" + "(" + queryFields.getPubTypeIdStr().trim().replaceAll(",", " ") + ")");
      }
    }

    if (queryFields.getExcludedPubIds() != null && queryFields.getExcludedPubIds().size() != 0) {
      StringBuffer sb = new StringBuffer();
      for (Long pubId : queryFields.getExcludedPubIds()) {
        sb.append(String.valueOf(pubId));
        sb.append(" ");
      }
      String pubIdsStr = sb.toString().trim();
      pubIdsStr = "(" + pubIdsStr + ")";
      fqList.add("-pubId:" + pubIdsStr);
    }

    // 根据收录（dbId）过滤
    if (StringUtils.isNotBlank(queryFields.getPubDBIds())) {
      String pubDBIds = queryFields.getPubDBIds().trim().replaceAll(",", " ");
      fqList.add("(pubDbIdList:(" + pubDBIds + ") OR pubDbId:(" + pubDBIds + "))");
    }

    fqList.add("env:" + runEnv);
    // fqList.add("env:run");
    fqList.add("businessType:" + INDEX_TYPE_PDWH_PAPER);
    String[] str = fqList.toArray(new String[fqList.size()]);
    query.addFilterQuery(str);
    query.setStart(start);
    query.setRows(size);
    query.setParam("fl", "*, score");
    query.setFacet(true);
    query.setFacetMinCount(1);
    query.setParam("df", "pubTitle");
    query.setHighlightSimplePre("<font style = 'color:#de5f0d;font-weight:bold'>");
    query.setHighlightSimplePost("</font>");
    if (QueryFields.ORDER_YEAR.equals(queryFields.getOrderBy())) {
      query.addSort("pubYear", ORDER.desc);
      query.addSort("fullText", ORDER.desc);
    } else {
      query.addSort("score", ORDER.desc);
      query.addSort("fullText", ORDER.desc);
    }
    // 在第二次查询的时候才会加入统计，填充leftmenu
    if (QueryFields.SEQ_2.equals(queryFields.getSeqQuery())) {
      String[] fFields = {"pubYear", "pubTypeId", "language"};
      query.addFacetField(fFields);
    }
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    // String resultStr = qrs.getResults().toString();
    Gson gs = new Gson();
    String resultStr = gs.toJson(qrs.getResults());
    String hlStr = gs.toJson(qrs.getHighlighting());
    Map<String, Object> rsMap = new HashMap<String, Object>();
    // 在第二次查询的时候才会加入统计，填充leftmenu
    if (QueryFields.SEQ_2.equals(queryFields.getSeqQuery())) {
      List<FacetField> facetList = qrs.getFacetFields();
      rsMap.put(RESULT_FACET, facetList);
    }
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    rsMap.put(RESULT_HIGHLIGHT, hlStr);
    return rsMap;
  }

  @Override
  public Map<String, Object> queryPubsNewIfNull(Integer pageCount, Integer size, QueryFields queryFields)
      throws SolrServerException {
    Assert.notNull(queryFields, "queryFields不能为空！");
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");

    Integer pubYear = queryFields.getPubYear();
    Integer pubType = queryFields.getPubTypeId();
    String years = queryFields.getYears();
    String addr = queryFields.getAddr();
    String authorName = queryFields.getName();
    boolean isDoi = queryFields.getIsDoi();
    SolrServer server = new HttpSolrServer(serverUrl);
    String queryString = queryFields.getSearchString();
    SolrQuery query = new SolrQuery();
    List<String> fqList = new ArrayList<String>();
    Integer start = 0;
    if (pageCount > 0) {
      start = (pageCount - 1) * size;
    }
    if (isDoi) {
      query.setRequestHandler("/pubbrowsebydoi");
      query.addHighlightField("doi");
    } else {
      String bqQuery =
          "journalGrade:1^2500 journalGrade:2^2200 journalGrade:3^2000 journalGrade:4^1500 journalGrade:5^500 journalGrade:6^250 journalGrade:7^200 journalGrade:8^100 fullText:1^3300";
      query.setParam("bp", bqQuery);
      query.setRequestHandler("/pubbrowse");
      query.addHighlightField("title authors keywords");
      queryString = StringUtils.trimToEmpty(queryString);
      if (StringUtils.isNotEmpty(queryString)) {
        query.setQuery(queryString);
      }
    }

    if (pubYear != null) {
      fqList.add("pubYear:" + pubYear);
    } else if (StringUtils.isNotBlank(years)) {
      fqList.add("pubYear:" + "(" + years.trim().replaceAll(",", " ") + ")");
    }

    // 优先使用list查询
    if (queryFields.getPubTypeIdList() != null && queryFields.getPubTypeIdList().size() != 0) {
      StringBuffer sb = new StringBuffer();
      for (Integer pubTypeId : queryFields.getPubTypeIdList()) {
        sb.append(String.valueOf(pubTypeId));
        sb.append(" ");
      }
      String pubTypeIdsStr = sb.toString().trim();
      pubTypeIdsStr = "(" + pubTypeIdsStr + ")";
      fqList.add("pubTypeId:" + pubTypeIdsStr);
    } else {
      if (pubType != null) {
        fqList.add("pubTypeId:" + pubType);
      } else if (StringUtils.isNotBlank(queryFields.getPubTypeIdStr())) {
        fqList.add("pubTypeId:" + "(" + queryFields.getPubTypeIdStr().trim().replaceAll(",", " ") + ")");
      }
    }

    // 根据收录（dbId）过滤
    if (StringUtils.isNotBlank(queryFields.getPubDBIds())) {
      String pubDBIds = queryFields.getPubDBIds().trim().replaceAll(",", " ");
      fqList.add("(pubDbIdList:(" + pubDBIds + ") OR pubDbId:(" + pubDBIds + "))");
    }

    // fqList.add("env:" + runEnv);
    fqList.add("env:run");
    fqList.add("businessType:" + INDEX_TYPE_PDWH_PAPER);
    String[] str = fqList.toArray(new String[fqList.size()]);
    query.addFilterQuery(str);
    query.setStart(start);
    query.setRows(size);
    query.setParam("fl", "*, score");
    query.setFacet(true);
    query.setFacetMinCount(1);
    query.setParam("df", "pubTitle");
    query.setHighlightSimplePre("<font style = 'color:#de5f0d;font-weight:bold'>");
    query.setHighlightSimplePost("</font>");
    if (QueryFields.ORDER_YEAR.equals(queryFields.getOrderBy())) {
      query.addSort("pubYear", ORDER.desc);
      query.addSort("fullText", ORDER.desc);
    } else {
      query.addSort("score", ORDER.desc);
      query.addSort("fullText", ORDER.desc);
    }
    // 在第二次查询的时候才会加入统计，填充leftmenu
    if (QueryFields.SEQ_2.equals(queryFields.getSeqQuery())) {
      String[] fFields = {"pubYear", "pubTypeId", "language"};
      query.addFacetField(fFields);
    }
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    // String resultStr = qrs.getResults().toString();
    Gson gs = new Gson();
    String resultStr = gs.toJson(qrs.getResults());
    String hlStr = gs.toJson(qrs.getHighlighting());
    Map<String, Object> rsMap = new HashMap<String, Object>();
    // 在第二次查询的时候才会加入统计，填充leftmenu
    if (QueryFields.SEQ_2.equals(queryFields.getSeqQuery())) {
      List<FacetField> facetList = qrs.getFacetFields();
      rsMap.put(RESULT_FACET, facetList);
    }
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    rsMap.put(RESULT_HIGHLIGHT, hlStr);
    return rsMap;
  }

  @Override
  public void deletePatInsolr(Long pubId) {
    SolrServer server = new HttpSolrServer(serverUrl);
    // 删除论文索引
    try {
      server.deleteById(String.valueOf(generateIdForIndex(pubId, INDEX_TYPE_PDWH_PAT)));
    } catch (Exception e) {
      logger.error("删除solr索引失败==================pubId:" + pubId, e);
    }

  }

  @Override
  public void deletePaperInsolr(Long pubId) {
    SolrServer server = new HttpSolrServer(serverUrl);
    // 删除专利索引
    try {
      server.deleteById(String.valueOf(generateIdForIndex(pubId, INDEX_TYPE_PDWH_PAPER)));
    } catch (Exception e) {
      logger.error("删除solr索引失败==================pubId:" + pubId, e);
    }
  }

  @Override
  public Map<String, Object> getPdwhSearchSuggestStr(String searchKey, Integer type) throws SolrServerException {
    Assert.notNull(searchKey, "pubTitle不能为空！");
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    List<String> fqList = new ArrayList<String>();
    query.setRequestHandler("/pdwhSearchSuggest");
    fqList.add("businessType:" + INDEX_PDWH_SEARCH_SUGGEST);
    query.setQuery(searchKey);
    String[] str = fqList.toArray(new String[fqList.size()]);
    query.addFilterQuery(str);
    query.setStart(0);
    query.setRows(10);
    // 添加排序限制
    List<SortClause> sorts = new ArrayList<SortClause>();
    // 通过type：1成果--优先提示机构，人员；3人员--优先提示人员，机构
    if (type == 1) {
      sorts.add(new SortClause("suggestStrLevel", ORDER.desc));
      sorts.add(new SortClause("score", ORDER.desc));
      sorts.add(new SortClause("suggestStrScore", ORDER.desc));
      query.setSorts(sorts);
    } else {
      sorts.add(new SortClause("suggestStrLevel", ORDER.asc));
      sorts.add(new SortClause("score", ORDER.desc));
      sorts.add(new SortClause("suggestStrScore", ORDER.desc));
      query.setSorts(sorts);
    }
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    SolrDocumentList resultStr = qrs.getResults();
    Map<String, Object> rsMap = new HashMap<String, Object>();
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    return rsMap;
  }

  @Override
  public Map<String, Object> getPdwhInsSearchSuggestStr(String searchKey, Integer type) throws SolrServerException {
    Assert.notNull(searchKey, "pubTitle不能为空！");
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    List<String> fqList = new ArrayList<String>();
    query.setRequestHandler("/pdwhSearchInsSuggest");
    fqList.add("businessType:" + INDEX_PDWH_SEARCH_SUGGEST);
    query.setQuery(searchKey);
    String[] str = fqList.toArray(new String[fqList.size()]);
    query.addFilterQuery(str);
    query.setStart(0);
    query.setRows(10);
    // 添加排序限制
    List<SortClause> sorts = new ArrayList<SortClause>();
    sorts.add(new SortClause("score", ORDER.desc));
    sorts.add(new SortClause("suggestStrScore", ORDER.desc));
    query.setSorts(sorts);
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    SolrDocumentList resultStr = qrs.getResults();
    Map<String, Object> rsMap = new HashMap<String, Object>();
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    return rsMap;
  }

  @Override
  public Map<String, Object> getPdwhPsnSearchSuggestStr(String searchKey, Integer type) throws SolrServerException {
    Assert.notNull(searchKey, "pubTitle不能为空！");
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    List<String> fqList = new ArrayList<String>();
    query.setRequestHandler("/pdwhSearchPsnSuggest");
    fqList.add("businessType:" + INDEX_PDWH_SEARCH_SUGGEST);
    query.setQuery(searchKey);
    String[] str = fqList.toArray(new String[fqList.size()]);
    query.addFilterQuery(str);
    query.setStart(0);
    query.setRows(10);
    // 添加排序限制
    List<SortClause> sorts = new ArrayList<SortClause>();
    sorts.add(new SortClause("score", ORDER.desc));
    sorts.add(new SortClause("suggestStrScore", ORDER.desc));
    query.setSorts(sorts);
    QueryResponse qrs = server.query(query);
    Long rsCount = qrs.getResults().getNumFound();
    SolrDocumentList resultStr = qrs.getResults();
    Map<String, Object> rsMap = new HashMap<String, Object>();
    rsMap.put(RESULT_NUMFOUND, String.valueOf(rsCount));
    rsMap.put(RESULT_ITEMS, resultStr);
    return rsMap;
  }

  @Override
  public Map<String, Object> restfulQueryPubs(Integer pageNo, Integer pageSize, QueryFields queryFields)
      throws SolrServerException {
    Assert.notNull(queryFields, "queryFields不能为空！");
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    String queryString = queryFields.getSearchString();
    SolrServer server = new HttpSolrServer(serverUrl);
    SolrQuery query = new SolrQuery();
    List<String> fqList = new ArrayList<String>();
    fqList.add("env:" + runEnv);
    fqList.add("businessType:" + INDEX_TYPE_PDWH_PAPER);
    Integer start = 0;
    if (pageNo > 0) {
      start = (pageNo - 1) * pageSize;
    }
    query.setRequestHandler("/pubbrowse");
    queryString = StringUtils.trimToEmpty(queryString);
    if (StringUtils.isNotEmpty(queryString)) {
      query.setQuery(queryString);
    }
    if (queryFields.getRegionCode() != null) {
      fqList.add("regionCode:" + queryFields.getRegionCode());
    }
    if (queryFields.getPubYear() != null) {// 成果年份过滤
      fqList.add("pubYear:" + queryFields.getPubYear());
    } else if (StringUtils.isNotBlank(queryFields.getYears())) {// 多个年份过滤
      fqList.add("pubYear:" + "(" + queryFields.getYears().trim().replaceAll(",", " ") + ")");
    }

    if (queryFields.getPubTypeId() != null) {// 成果类型过滤
      if (queryFields.getPubTypeId() == 7) {
        fqList.add("pubTypeId:(7 1 2 5 10 12 13)");
      } else {
        fqList.add("pubTypeId:" + queryFields.getPubTypeId());
      }
    } else if (StringUtils.isNotBlank(queryFields.getPubTypeIdStr())) {// 多个类型过滤
      fqList.add("pubTypeId:" + "(" + queryFields.getPubTypeIdStr().trim().replaceAll(",", " ") + ")");
    }

    if (queryFields.getPubSuperCatgory() != null) {// 成果领域过滤
      fqList.add("pubSuperCategory:" + queryFields.getPubSuperCatgory());
    } else if (StringUtils.isNotBlank(queryFields.getPubSuperCategorys())) {
      fqList.add("pubSuperCategory:" + "(" + queryFields.getPubSuperCategorys().replaceAll(",", " ") + ")");
    }

    String[] str = fqList.toArray(new String[fqList.size()]);
    query.addFilterQuery(str);
    query.setStart(start);
    query.setRows(pageSize);
    query.setParam("fl", "*, score");
    query.setFacet(true);
    query.setFacetMinCount(1);
    query.setParam("df", "pubTitle");
    // 排序
    query.addSort("score", ORDER.desc);
    query.addSort("fullText", ORDER.desc);
    if (queryFields.getFacetField() != null) {// 分类统计
      query.addFacetField(queryFields.getFacetField());
    }
    QueryResponse qrs = server.query(query);// 查询

    Gson gs = new Gson();
    String resultStr = gs.toJson(qrs.getResults());
    Map<String, Object> rsMap = new HashMap<String, Object>();

    if (queryFields.getFacetField() != null) {
      List<FacetField> facetList = qrs.getFacetFields();
      rsMap.put(RESULT_FACET, facetList);// 分类统计结果
    }
    rsMap.put(RESULT_NUMFOUND, qrs.getResults().getNumFound());
    rsMap.put(RESULT_ITEMS, resultStr);
    return rsMap;

  }

}
