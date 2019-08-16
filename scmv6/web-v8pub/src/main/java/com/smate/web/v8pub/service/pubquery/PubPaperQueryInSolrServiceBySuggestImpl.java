package com.smate.web.v8pub.service.pubquery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.solr.model.QueryFields;
import com.smate.core.base.solr.service.SolrIndexSerivceImpl;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.v8pub.service.pdwh.PubPdwhDetailService;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.utils.MessageDigestUtils;
import com.smate.web.v8pub.vo.PubListResult;

public class PubPaperQueryInSolrServiceBySuggestImpl extends AbstractPubQueryService {

  public static Long[] pubType = new Long[] {(long) PublicationTypeEnum.CONFERENCE_PAPER,
      (long) PublicationTypeEnum.JOURNAL_ARTICLE, (long) PublicationTypeEnum.THESIS};// 成果分类参考PublicationTypeEnum中的分类
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SolrIndexService solrIndexService;
  @Autowired
  private PubPdwhDetailService pubPdwhDetailService;

  @Override
  public Map<String, Object> checkData(PubQueryDTO pubQueryDTO) {
    return null;
  }

  @Override
  public void queryPubs(PubQueryDTO pubQueryDTO) {
    QueryFields queryFields = this.buildQueryFields(pubQueryDTO);
    try {
      Map<String, Object> rsMap =
          solrIndexService.queryPubsNew(pubQueryDTO.getPageNo(), pubQueryDTO.getPageSize(), queryFields);
      pubQueryDTO.setPubInfoMap(rsMap);
    } catch (SolrServerException e) {
      logger.error("从solr获取推荐成果出错 :" + pubQueryDTO.getSuggestKw() + "; ", e);
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
    // 检索的字符串(这边转换为小写字母，因为在进行人名地名提取时，都使用的是统一的小写)
    queryFields.setSearchString(pubQueryDTO.getSearchString().toLowerCase());
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
    this.constructNameAndInsStr(queryFields, pubQueryDTO);
    return queryFields;
  }

  private void constructNameAndInsStr(QueryFields queryFields, PubQueryDTO pubQueryDTO) {
    TreeSet<String> addrSet = new TreeSet<String>();
    String addr = StringUtils.trimToEmpty(pubQueryDTO.getSuggestInsName());
    String addrInsId = StringUtils.trimToEmpty(pubQueryDTO.getSuggestInsId());
    if (StringUtils.isNotEmpty(addrInsId)) {
      try {
        Long insId = Long.parseLong(ServiceUtil.decodeFromDes3(addrInsId));
        List<String> addrs = this.pubPdwhDetailService.getInsDetailsById(insId);
        if (addrs != null) {
          for (String ppair : addrs) {
            String str = StringUtils.trimToEmpty(ppair);
            if (StringUtils.isNotEmpty(str)) {
              addrSet.add(str.toLowerCase());
            }
          }
        }
      } catch (Exception e) {
        logger.error("通过InsId获取单位别名出错，insId=" + addrInsId, e);
      }
    }
    if (StringUtils.isNotEmpty(addr)) {
      addrSet.add(addr.toLowerCase());
    }
    StringBuilder sb = new StringBuilder();
    for (String str : addrSet) {
      sb.append("\"");
      sb.append(str);
      sb.append("\"");
      sb.append(",");
    }
    String addStr = StringUtils.trimToEmpty(sb.toString());
    if (StringUtils.isNotEmpty(addStr)) {
      queryFields.setAddr(addStr.substring(0, addStr.length() - 1));
    }
    queryFields.setInsNames(addrSet);

    String name = StringUtils.trimToEmpty(pubQueryDTO.getSuggestPsnName());
    String psnIdStr = StringUtils.trimToEmpty(pubQueryDTO.getSuggestPsnId());
    TreeSet<String> usrSet = new TreeSet<String>();
    // 0，代表人员为空，排除掉
    if (StringUtils.isNotEmpty(name) && !"0".equals(name)) {
      usrSet.add(name.toLowerCase());
    }
    List<String> usrNames = new ArrayList<String>();
    if (StringUtils.isNotEmpty(psnIdStr) && !"0".equals(psnIdStr)) {
      try {
        Long psnId = Long.parseLong(ServiceUtil.decodeFromDes3(psnIdStr));
        usrNames = this.pubPdwhDetailService.getAllUserName(psnId);
      } catch (Exception e) {
        logger.error("通过psnIdStr获取用户别名出错，psnId=" + psnIdStr, e);
      }
    }
    if (usrNames != null && usrNames.size() > 0) {
      for (String str : usrNames) {
        str = StringUtils.trimToEmpty(str);
        if (StringUtils.isNotEmpty(str)) {
          usrSet.add(str.toLowerCase());
        }
      }
    }
    StringBuilder sbUsr = new StringBuilder();
    StringBuilder sbUsrMd5 = new StringBuilder();
    for (String usr : usrSet) {
      try {
        String cleanAuthors = XmlUtil.cleanXMLAuthorChars(usr);
        sbUsr.append("\"");
        sbUsr.append(cleanAuthors);
        sbUsr.append("\"");
        sbUsr.append(",");
        String md5 = MessageDigestUtils.messageDigest(cleanAuthors);
        sbUsrMd5.append("\"");
        sbUsrMd5.append(md5);
        sbUsrMd5.append("\"");
        sbUsrMd5.append(",");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    String usrStr = StringUtils.trimToEmpty(sbUsr.toString());
    if (StringUtils.isNotEmpty(usrStr)) {
      queryFields.setName(usrStr.substring(0, usrStr.length() - 1));
    }
    String usrStrMd5 = StringUtils.trimToEmpty(sbUsrMd5.toString());
    if (StringUtils.isNotEmpty(usrStrMd5)) {
      queryFields.setMd5Author(usrStrMd5.substring(0, usrStrMd5.length() - 1));
    }
    queryFields.setUseNames(usrSet);
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
   * 拼装组数据
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public PubListResult assembleFacetData(PubListResult listResult, PubQueryDTO pubQueryDTO) {
    List<FacetField> facetList = (List<FacetField>) pubQueryDTO.getPubInfoMap().get(SolrIndexSerivceImpl.RESULT_FACET);
    Map<String, Object> resultData = new HashMap<String, Object>();
    if (facetList != null && facetList.size() > 0) {
      buildYearAndPubType(facetList, resultData);
      listResult.setResultData(resultData);
    }
    return listResult;
  }

  public PubListResult assembleResultData(PubListResult listResult, PubQueryDTO pubQueryDTO) {
    String count = (String) pubQueryDTO.getPubInfoMap().get(SolrIndexSerivceImpl.RESULT_NUMFOUND);
    String items = (String) pubQueryDTO.getPubInfoMap().get(SolrIndexSerivceImpl.RESULT_ITEMS);
    String highlight = (String) pubQueryDTO.getPubInfoMap().get(SolrIndexSerivceImpl.RESULT_HIGHLIGHT);
    if (StringUtils.isNotBlank(count)) {
      long num = NumberUtils.toLong(count);
      // 构建论文信息
      boolean ishighlight = JacksonUtils.isJsonString(highlight);
      List<Map<String, Object>> listItems = JacksonUtils.jsonToList(items);
      Map<String, Map<String, List<String>>> highligh = JacksonUtils.jsonToMap(highlight);
      if (CollectionUtils.isNotEmpty(listItems) && ishighlight) {
        List<PubInfo> infoList = new ArrayList<PubInfo>();
        for (Map<String, Object> item : listItems) {
          PubInfo pubInfo = new PubInfo();
          Long pubId = NumberUtils.toLong(ObjectUtils.toString(item.get("pubId")));

          /*
           * if (pubIsDelete(pubId)) { solrIndexService.deletePaperInsolr(pubId); num--; continue; }
           */

          pubInfo.setPubId(pubId);
          pubInfo.setDes3PubId(Des3Utils.encodeToDes3(pubId.toString()));
          pubInfo.setTitle(HtmlUtils.htmlEscape(ObjectUtils.toString(item.get("pubTitle"))));
          pubInfo.setBriefDesc(ObjectUtils.toString(item.get("pubBrief")));
          pubInfo.setAuthorNames(ObjectUtils.toString(item.get("authors")));
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
      long totalPages = num / pubQueryDTO.getPageSize() + 1;
      pubQueryDTO.setTotalCount(num);
      pubQueryDTO.setTotalPages(totalPages);
      listResult.setTotalCount(NumberUtils.toInt(String.valueOf(num)));
    }
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
