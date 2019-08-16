package com.smate.web.v8pub.service.pubquery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.DicLibrary;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.solr.model.QueryFields;
import com.smate.core.base.solr.service.SolrIndexSerivceImpl;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.vo.PubListResult;

public class PubPaperQueryInSolrServiceImpl extends AbstractPubQueryService {

  public static Long[] pubType = new Long[] {(long) PublicationTypeEnum.CONFERENCE_PAPER,
      (long) PublicationTypeEnum.JOURNAL_ARTICLE, (long) PublicationTypeEnum.THESIS};// 成果分类参考PublicationTypeEnum中的分类
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SolrIndexService solrIndexService;

  @Override
  public Map<String, Object> checkData(PubQueryDTO pubQueryDTO) {
    return null;
  }

  @Override
  public void queryPubs(PubQueryDTO pubQueryDTO) {
    QueryFields queryFields = this.buildQueryFields(pubQueryDTO);
    try {


      Map<String, Object> rsMap =
          solrIndexService.queryPubs(pubQueryDTO.getPageNo(), pubQueryDTO.getPageSize(), queryFields);


      /*
       * Map<String, Object> rsMap = solrIndexService.queryPubsNew(pubQueryDTO.getPageNo(),
       * pubQueryDTO.getPageSize(), queryFields);
       */
      pubQueryDTO.setPubInfoMap(rsMap);
    } catch (SolrServerException e) {
      logger.error("从solr获取推荐成果出错 ： psnId:" + pubQueryDTO.getSearchPsnId(), e);
      e.printStackTrace();
    }
  }

  private QueryFields buildQueryFields(PubQueryDTO pubQueryDTO) {
    QueryFields queryFields = new QueryFields();
    if (XmlUtil.containZhChar(pubQueryDTO.getSearchString())) {
      queryFields.setLanguage(queryFields.LANGUAGE_ZH);
    } else {
      queryFields.setLanguage(queryFields.LANGUAGE_EN);
    }
    queryFields.setSeqQuery(pubQueryDTO.getSeqQuery());
    // 检索的字符串
    queryFields.setSearchString(pubQueryDTO.getSearchString());
    // 发表年份（单个）
    queryFields.setPubYear(
        CommonUtils.compareIntegerValue(pubQueryDTO.getSearchPubYear(), 0) ? null : pubQueryDTO.getSearchPubYear());
    // 发表年份（多个，逗号隔开或空格隔开）
    queryFields.setYears(pubQueryDTO.getPublishYear());
    // 成果类型（单个）
    queryFields.setPubTypeId(pubQueryDTO.getSearchPubTypeId());
    // 成果类型（多个，逗号隔开或空格隔开）
    queryFields.setPubTypeIdStr(pubQueryDTO.getSearchPubType());
    // 排序
    queryFields.setOrderBy(pubQueryDTO.getOrderBy());
    // 收录情况
    queryFields.setPubDBIds(pubQueryDTO.getIncludeType());
    /*
     * queryFields.setAddr((String)
     * this.extractRegisterIns(pubQueryDTO.getSearchString()).get("rsStr"));
     * queryFields.setName((String)
     * this.extractRegisterUserName(pubQueryDTO.getSearchString()).get("rsStr"));
     * queryFields.setInsNames((Set<String>)
     * this.extractRegisterIns(pubQueryDTO.getSearchString()).get("rsCollection")); queryFields
     * .setUseNames((Set<String>)
     * (this.extractRegisterUserName(pubQueryDTO.getSearchString()).get("rsCollection")));
     */
    return queryFields;
  }

  private Map<String, Object> extractRegisterIns(String kws) {
    Map<String, Object> rsMap = new HashMap<>();
    if (StringUtils.isEmpty(kws)) {
      return rsMap;
    }
    kws = kws.toLowerCase().replaceAll("\\s+", "空格");
    Result kwRs = DicAnalysis.parse(kws);
    if (kwRs == null || kwRs.getTerms() == null || kwRs.getTerms().size() <= 0) {
      return null;
    }
    Set<String> kwStrings = new TreeSet<String>();
    StringBuilder sb = new StringBuilder();
    for (Term t : kwRs.getTerms()) {
      if (t == null) {
        continue;
      }
      if ("scm_ins_name".equals(t.getNatureStr())) {
        if (StringUtils.isNotEmpty(t.getName())) {
          String str = t.getName().replaceAll("空格", " ").replaceAll("\\s+", " ").trim();
          if (StringUtils.isEmpty(str)) {
            continue;
          }
          kwStrings.add(str);
          sb.append("\"");
          sb.append(str);
          sb.append("\"");
          sb.append(" OR");
        }
      }
    }
    String str = StringUtils.trimToEmpty(sb.toString());
    if (StringUtils.isNotEmpty(str)) {
      rsMap.put("rsStr", str.substring(0, str.length() - 2));
    }
    rsMap.put("rsCollection", kwStrings);
    return rsMap;
  }

  private Map<String, Object> extractRegisterUserName(String kws) {
    Map<String, Object> rsMap = new HashMap<>();
    if (StringUtils.isEmpty(kws)) {
      return rsMap;
    }
    kws = kws.toLowerCase().replaceAll("\\s+", "空格");
    Result kwRs = DicAnalysis.parse(kws);
    if (kwRs == null || kwRs.getTerms() == null || kwRs.getTerms().size() <= 0) {
      return null;
    }
    Set<String> kwStrings = new TreeSet<String>();
    StringBuilder sb = new StringBuilder();
    for (Term t : kwRs.getTerms()) {
      if (t == null) {
        continue;
      }
      // scm_user_name scm_ins_name
      if ("scm_user_name".equals(t.getNatureStr())) {
        if (StringUtils.isNotEmpty(t.getName())) {
          String str = t.getName().replaceAll("空格", " ").replaceAll("\\s+", " ").trim();
          if (StringUtils.isEmpty(str)) {
            continue;
          }
          kwStrings.add(str);
          sb.append("\"");
          sb.append(str);
          sb.append("\"");
          sb.append("OR");
        }
      }
    }
    String str = StringUtils.trimToEmpty(sb.toString());
    if (StringUtils.isNotEmpty(str)) {
      rsMap.put("rsStr", str.substring(0, str.length() - 2));
    }
    rsMap.put("rsCollection", kwStrings);
    return rsMap;
  }

  @Override
  public PubListResult assembleData(PubQueryDTO pubQueryDTO) {
    PubListResult listResult = new PubListResult();
    if (PubQueryDTO.SEQ_2.equals(pubQueryDTO.getSeqQuery())) {
      return assembleResultData(assembleFacetData(listResult, pubQueryDTO), pubQueryDTO);// 对结果列表和分组列表都进行组装
    } else {
      return assembleResultData(listResult, pubQueryDTO);// 只组装结果列表
    }
  }

  /**
   * 组装分组数据
   * 
   * @param pubQueryDTO
   * @return
   */
  private PubListResult assembleFacetData(PubListResult listResult, PubQueryDTO pubQueryDTO) {
    List<FacetField> facetList = (List<FacetField>) pubQueryDTO.getPubInfoMap().get(SolrIndexSerivceImpl.RESULT_FACET);
    Map<String, Object> resultData = new HashMap<String, Object>();
    if (facetList != null && facetList.size() > 0) {
      buildYearAndPubType(facetList, resultData);
      listResult.setResultData(resultData);
    }
    return listResult;
  }

  /**
   * 组装返回的结果数据
   * 
   * @param listResult
   * @param pubQueryDTO
   * @return
   */
  private PubListResult assembleResultData(PubListResult listResult, PubQueryDTO pubQueryDTO) {
    String count = (String) pubQueryDTO.getPubInfoMap().get(SolrIndexSerivceImpl.RESULT_NUMFOUND);
    String items = (String) pubQueryDTO.getPubInfoMap().get(SolrIndexSerivceImpl.RESULT_ITEMS);
    String highlight = (String) pubQueryDTO.getPubInfoMap().get(SolrIndexSerivceImpl.RESULT_HIGHLIGHT);
    if (StringUtils.isNotBlank(count)) {
      long num = NumberUtils.toLong(count);
      long totalPages = num / pubQueryDTO.getPageSize() + 1;
      pubQueryDTO.setTotalCount(num);
      pubQueryDTO.setTotalPages(totalPages);
    }
    // 构建论文信息
    boolean ishighlight = JacksonUtils.isJsonString(highlight);
    List<Map<String, Object>> listItems = JacksonUtils.jsonToList(items);
    Map<String, Map<String, List<String>>> highligh = JacksonUtils.jsonToMap(highlight);
    if (CollectionUtils.isNotEmpty(listItems) && ishighlight) {
      List<PubInfo> infoList = new ArrayList<PubInfo>();
      for (Map<String, Object> item : listItems) {
        PubInfo pubInfo = new PubInfo();
        Long pubId = NumberUtils.toLong(ObjectUtils.toString(item.get("pubId")));
        if (pubIsDelete(pubId)) {
          solrIndexService.deletePaperInsolr(pubId);
          int delCount = NumberUtils.toInt(count);
          count = String.valueOf(--delCount >= 0 ? delCount : 0);
          continue;
        }
        pubInfo.setPubId(pubId);
        pubInfo.setDes3PubId(Des3Utils.encodeToDes3(pubId.toString()));
        // 弃用HtmlUtils.htmlEscape,因当标题中存在&mdash;其无法正确转码 SCM-24713
        pubInfo.setTitle(StringEscapeUtils.unescapeHtml4(ObjectUtils.toString(item.get("pubTitle"))));
        pubInfo.setBriefDesc(ObjectUtils.toString(item.get("pubBrief")));
        pubInfo.setAuthorNames(ObjectUtils.toString(item.get("authors")));
        pubInfo.setPublishYear(NumberUtils.toInt(Objects.toString(item.get("pubYear"))));
        pubInfo.setCitations(NumberUtils.toInt(ObjectUtils.toString(item.get("pubCitations"))));
        pubInfo.setDbid(NumberUtils.toInt(ObjectUtils.toString(item.get("pubDbId"))));
        // 查询统计数
        builPdwhPubStatistics(pubInfo, pubQueryDTO.getSearchPsnId());
        // 全文
        buildPdwhPubFulltext(pubInfo, pubQueryDTO.getSearchPsnId());
        // 短地址
        buildPdwhPubIndexUrl(pubInfo, pubQueryDTO.getSearchPsnId());
        infoList.add(pubInfo);
      }
      listResult.setResultList(infoList);
    }
    listResult.setTotalCount(NumberUtils.toInt(count));
    return listResult;
  }


  private void buildYearAndPubType(List<FacetField> facetList, Map<String, Object> resultData) {
    for (FacetField f : facetList) {
      Map<Long, Long> map = new TreeMap<Long, Long>(new Comparator<Long>() {
        public int compare(Long obj1, Long obj2) {
          return obj2.compareTo(obj1);
        }
      });
      String name = f.getName();
      if ("pubYear".equals(name)) {
        int curYear = Calendar.getInstance().get(Calendar.YEAR);
        List<Count> countList = f.getValues();
        if (countList != null && countList.size() > 0) {
          for (int i = 0; i < countList.size(); i++) {
            if (countList.get(i).getName() != null && NumberUtils.isNumber(countList.get(i).getName()))
              if (NumberUtils.toInt(countList.get(i).getName()) <= curYear)
                map.put(NumberUtils.toLong(countList.get(i).getName()), countList.get(i).getCount());
          }
          resultData.put("pubYear", map);
        }
      } else if ("pubTypeId".equals(name)) {
        List<Count> countList = f.getValues();
        Map<Long, Long> temp = new LinkedHashMap<Long, Long>();
        for (Long type : pubType) {// 初始化保持排序，不能省
          temp.put(type, 0L);
        }
        Long otherTypeNum = 0L;// 那些不在页面上存在对应分类的成果{pubTyID=1,2,10},将放在"其他"栏SCM-24123
        if (CollectionUtils.isNotEmpty(countList)) {
          for (Count count : countList) {
            Long typeId = NumberUtils.toLong(count.getName());
            if (ArrayUtils.contains(pubType, typeId)) {// 是否在已存在分类中
              temp.put(typeId, count.getCount());
            } else {
              otherTypeNum += count.getCount();
            }
          }
        }
        temp.put((long) PublicationTypeEnum.OTHERS, otherTypeNum);// 将那些不包含在页面上显示的论文作为“其他”进行显示
        for (Long num : pubType) {
          if (!temp.containsKey(num)) {// 将没有数据的那些设置为0
            temp.put(num, 0L);
          }
        }
        resultData.put("pubType", temp);
      } else if ("language".equals(name)) {
        List<Count> countList = f.getValues();
        Map<String, Long> temp = new LinkedHashMap<String, Long>();
        temp.put("ZH_CN", 0L);
        temp.put("EN", 0L);
        if (countList != null && countList.size() > 0) {
          for (int i = 0; i < countList.size(); i++) {
            if (countList.get(i).getName() != null)
              temp.put(countList.get(i).getName(), countList.get(i).getCount());
          }
        }
        resultData.put("languageType", temp);
      }
    }
  }

  public static void main(String[] args) {
    String pathIns = "addr/";
    File fileIns = new File(pathIns);
    FileInputStream ris = null;
    InputStreamReader isr = null;
    BufferedReader br = null;
    try {
      ris = new FileInputStream(fileIns);
      isr = new InputStreamReader(ris, "UTF-8");
      br = new BufferedReader(isr);
      String fileStr = null;
      while (StringUtils.isNotBlank(fileStr = br.readLine())) {
        DicLibrary.insert(DicLibrary.DEFAULT, fileStr.toLowerCase().trim(), "scm_user_name", DicLibrary.DEFAULT_FREQ);
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
