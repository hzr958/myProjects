package com.smate.center.task.service.pdwh.quartz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.task.dao.pdwh.quartz.OriginalPdwhPubRelationDao;
import com.smate.center.task.dao.pdwh.quartz.PubReferenceDao;
import com.smate.center.task.dao.sns.quartz.ConstRefDbDao;
import com.smate.center.task.dao.sns.quartz.ConstRegionDao;
import com.smate.center.task.model.pdwh.pub.OriginalPdwhPubRelation;
import com.smate.center.task.model.sns.pub.ConstRegion;
import com.smate.center.task.model.sns.quartz.ConstRefDb;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubDuplicateDAO;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDetailDAO;
import com.smate.center.task.v8pub.dao.sns.PubSnsDAO;
import com.smate.core.base.pub.consts.RestTemplateUtils;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.enums.PubBookTypeEnum;
import com.smate.core.base.pub.enums.PubConferencePaperTypeEnum;
import com.smate.core.base.pub.enums.PubThesisDegreeEnum;
import com.smate.core.base.pub.util.PdwhPubXMLToJsonStrUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.xss.XssUtils;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.dto.BookInfoDTO;
import com.smate.web.v8pub.dto.ConferencePaperDTO;
import com.smate.web.v8pub.dto.JournalInfoDTO;
import com.smate.web.v8pub.dto.MemberInsDTO;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.dto.ThesisInfoDTO;

@Service("handpdwhPubXmlService")
@Transactional(rollbackFor = Exception.class)
public class HandlePdwhPubXmlServiceImpl implements HandlePdwhPubXmlService {
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String scmDomain;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private ConstRefDbDao constRefDbDao;
  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDAO;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private OriginalPdwhPubRelationDao originalPdwhPubRelationDao;
  @Autowired
  private PubReferenceDao pubReferenceDao;
  @Autowired
  private PdwhPubDuplicateDAO pdwhPubDuplicateDAO;

  @Override
  public Map<String, Object> XmlToJsonData(String pubData) {
    // 调用接口将xml转成json
    Map<String, Object> map = PdwhPubXMLToJsonStrUtils.dealWithXMLToMap(pubData);
    // 城市id 构造
    buildCountryId(map);
    // srcDbId 构造
    buildSrcDbId(map);
    return map;
  }

  @SuppressWarnings("unchecked")
  @Override
  public String getPubDupucheckStatus(Map map) {
    Map<String, Object> dupMap = new HashMap<String, Object>();
    dupMap.put("pubGener", 3);// 成果大类别，1代表个人成果 2代表群组成果 3代表基准库成果
    dupMap.put("title", map.get("title"));
    String publishDate = (String) map.get("publishDate");
    Integer pubYear = NumberUtils.toInt(StringUtils.substring(publishDate, 0, 4));
    dupMap.put("pubYear", pubYear);
    Integer pubType = (Integer) map.get("pubType");
    dupMap.put("pubType", pubType);
    dupMap.put("doi", map.get("doi"));
    dupMap.put("srcDbId", map.get("srcDbId"));
    dupMap.put("sourceId", map.get("sourceId"));
    if (pubType != null && pubType == 5) {
      Map<String, Object> typeInfo = (Map<String, Object>) map.get("pubTypeInfo");
      if (typeInfo != null) {
        dupMap.put("applicationNo", typeInfo.get("applicationNo"));
        dupMap.put("publicationOpenNo", typeInfo.get("publicationOpenNo"));
      }
    }
    if (pubType != null && pubType == 12) {
      Map<String, Object> typeInfo = (Map<String, Object>) map.get("pubTypeInfo");
      if (typeInfo != null) {
        dupMap.put("standardNo", typeInfo.get("standardNo"));
      }
    }
    if (pubType != null && pubType == 13) {
      Map<String, Object> typeInfo = (Map<String, Object>) map.get("pubTypeInfo");
      if (typeInfo != null) {
        dupMap.put("registerNo", typeInfo.get("registerNo"));
      }
    }
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(dupMap), headers);
    String dupUrl = scmDomain + V8pubQueryPubConst.PUB_DUPCHECK_URL;
    String result = restTemplate.postForObject(dupUrl, entity, String.class);
    return result;
  }

  @Override
  public String saveNewPdwhPub(Map<String, Object> map, Long insId, Long PsnId) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    String url = scmDomain + V8pubQueryPubConst.PUBHANDLER_URL;
    map.put("pubHandlerName", "savePdwhPubHandler");
    map.put("recordFrom", 3);
    map.put("des3PsnId", Des3Utils.encodeToDes3(String.valueOf(PsnId)));
    map.put("insId", insId);
    String dataJson = JacksonUtils.mapToJsonStr(map);
    // 进行xss的过滤
    dataJson = XssUtils.transferJson(dataJson);
    // 先对json数据进行多个空格的替换
    dataJson = dataJson.replaceAll("\\s+", " ");
    HttpEntity<String> entity1 = new HttpEntity<String>(dataJson, headers);
    return restTemplate.postForObject(url, entity1, String.class);
  }

  @Override
  public String updatePdwhPub(Map<String, Object> map, Long pdwhPubId, Long insId, Long PsnId) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    String url = scmDomain + V8pubQueryPubConst.PUBHANDLER_URL;
    map.put("pubHandlerName", "updatePdwhPubHandler");
    map.put("recordFrom", 3);
    map.put("des3PsnId", Des3Utils.encodeToDes3(String.valueOf(2L)));
    map.put("insId", insId);
    map.put("des3PubId", Des3Utils.encodeToDes3(pdwhPubId.toString()));
    String dataJson = JacksonUtils.mapToJsonStr(map);
    // 进行xss的过滤
    dataJson = XssUtils.transferJson(dataJson);
    // 先对json数据进行多个空格的替换
    dataJson = dataJson.replaceAll("\\s+", " ");
    HttpEntity<String> entity1 = new HttpEntity<String>(dataJson, headers);
    return restTemplate.postForObject(url, entity1, String.class);
  }

  @Override
  public String updateCitedTimes(Long currentPubId, int fileciteTimes) {
    Map<String, Object> map = new HashMap<String, Object>();
    PubPdwhDetailDOM pubDetail = pubPdwhDetailDAO.findById(currentPubId);
    map.put("des3PubId", Des3Utils.encodeToDes3(currentPubId.toString()));
    map.put("citations", fileciteTimes);
    map.put("srcDbId", pubDetail.getSrcDbId());
    map.put("citedType", 0);
    String url = scmDomain + V8pubQueryPubConst.PUBHANDLER_URL;
    map.put("pubHandlerName", "updatePdwhCitationsHandler");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> entity1 = new HttpEntity<String>(JacksonUtils.mapToJsonStr(map), headers);
    return restTemplate.postForObject(url, entity1, String.class);
  }

  @Override
  public void updateSnsPubCitedTimes(Long pubId, int fileciteTimes) {
    Map<String, Object> params = new HashMap<String, Object>();
    Long ownerPsnId = pubSnsDAO.getCreatePsnId(pubId);
    if (ownerPsnId == null) {
      return;
    }
    params.put("pubHandlerName", "updateSnsCitationsHandler");
    params.put("des3PubId", Des3Utils.encodeToDes3(pubId.toString()));
    params.put("citations", fileciteTimes);
    params.put("des3PsnId", Des3Utils.encodeToDes3(ownerPsnId.toString()));
    params.put("srcDbId", null);
    params.put("citedType", 0);
    RestTemplateUtils.post(restTemplate, scmDomain + V8pubQueryPubConst.PUBHANDLER_URL,
        JacksonUtils.mapToJsonStr(params));
  }

  private void buildCountryId(Map<String, Object> map) {
    Long countryId = null;
    List<ConstRegion> regionList = constRegionDao.findAll();
    String country = (String) map.get("country");
    String city = (String) map.get("city");
    if (StringUtils.isNotEmpty(country)) {
      countryId = matchCountryId(regionList, country);
      if (countryId == null && StringUtils.isNotEmpty(country)) {
        countryId = matchCountryId(regionList, city);
      }
    }
    map.remove("city");
    map.remove("country");
    map.put("countryId", countryId);
  }

  public Long matchCountryId(List<ConstRegion> regionList, String countryName) {
    if (StringUtils.isEmpty(countryName)) {
      return null;
    }
    List<Long> countryIds = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(regionList)) {
      for (ConstRegion constRegion : regionList) {
        // 先处理地区名，全部转为小写
        String zhRegionName = cleanCountryName(constRegion.getZhName());
        String enRegionName = cleanCountryName(constRegion.getEnName());
        countryName = cleanCountryName(countryName);
        // 匹配
        boolean isMatch = (countryName.contains(zhRegionName) && StringUtils.isNotEmpty(zhRegionName))
            || (countryName.contains(enRegionName) && StringUtils.isNotEmpty(enRegionName));
        if (isMatch) {
          countryIds.add(constRegion.getId());
        }
      }
    }
    if (CollectionUtils.isNotEmpty(countryIds)) {
      // 取最大的地区id
      Long countryId = countryIds.stream().mapToLong(regionId -> regionId).summaryStatistics().getMax();
      return countryId;
    }
    return null;
  }

  private String cleanCountryName(String countryName) {
    if (StringUtils.isEmpty(countryName)) {
      return "";
    }
    countryName = countryName.replace("市", "");
    countryName = countryName.replace("省", "");
    countryName = countryName.replace("自治区", "");
    countryName = countryName.replace("自治州", "");
    countryName = countryName.replace("地区", "");
    countryName = countryName.replace("盟", "");
    countryName = countryName.replace("林区", "");
    countryName = countryName.replace("的岛礁及其海域", "");
    countryName = countryName.toLowerCase();
    return countryName;
  }

  @SuppressWarnings("unchecked")
  private void buildSrcDbId(Map<String, Object> map) {
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

  @Override
  public Long saveOriginalPdwhPubRelation() {
    OriginalPdwhPubRelation originalPdwhPub = new OriginalPdwhPubRelation();
    originalPdwhPub.setRecordFrom(4);
    originalPdwhPub.setStatus(2);
    originalPdwhPubRelationDao.save(originalPdwhPub);
    return originalPdwhPub.getId();
  }

  @Override
  public void updateOriginalPdwhPubRelation(Long id, Long pdwhPubId, int status) {
    OriginalPdwhPubRelation originalPdwhPub = originalPdwhPubRelationDao.get(id);
    if (originalPdwhPub != null) {
      originalPdwhPub.setPdwhPubId(pdwhPubId);
      originalPdwhPub.setStatus(status);
      originalPdwhPubRelationDao.saveOrUpdate(originalPdwhPub);
    }


  }

  @Override
  public Map<String, Object> CrossrefToJsonData(Map<String, Object> dataMap) {
    Map<String, Object> result = new HashMap<String, Object>();

    // 成果基本信息
    dealPubBase(dataMap, result);
    // 成员信息
    dealPubMembers(dataMap, result);
    // 不同类型对应的额外信息
    buildPubTypeInfo(dataMap, result);
    // 城市id 构造
    buildCountryId(result);
    List<PubSituationDTO> situations = new ArrayList<>();
    result.put("situations", situations);
    return result;
  }


  @SuppressWarnings("unchecked")
  private void dealPubBase(Map<String, Object> pubData, Map<String, Object> result) {
    // 标题
    List<String> titleList = (List<String>) pubData.get("title");
    result.put("title", StringUtils.trimToEmpty(titleList.get(0)));
    // 摘要
    /*
     * if (pubData.get("abstract") != null) { result.put("summary", pubData.get("abstract").toString());
     * } else { result.put("summary", ""); }
     */
    result.put("summary", pubData.get("abstract"));
    // 关键词
    result.put("keywords", "");
    result.put("country", "");
    result.put("city", "");
    // doi
    if (pubData.get("DOI") != null) {
      result.put("doi", pubData.get("DOI").toString());
    } else {
      result.put("doi", "");
    }
    // 基金标注
    List<Map<Object, Object>> funderList = (List<Map<Object, Object>>) pubData.get("funder");
    StringBuilder fundInfo = new StringBuilder();
    String fundInfoString = "";
    if (funderList != null) {
      for (Map<Object, Object> funderMap : funderList) {
        fundInfo.append(funderMap.get("name"));
        List<String> awards = (List<String>) funderMap.get("award");
        if (awards != null && awards.size() > 0) {
          if (StringUtils.isNotBlank(awards.get(0))) {
            fundInfo.append(awards.toString());
          }
        }
        fundInfo.append(";");
      }
      fundInfoString =
          StringUtils.trimToEmpty(StringUtils.defaultString(fundInfo.toString().substring(0, fundInfo.length() - 1)));
    }
    result.put("fundInfo", fundInfoString);

    StringBuilder fulltextUrls = new StringBuilder();
    String srcFulltextUrl = "";
    List<Map<Object, Object>> linkList = (List<Map<Object, Object>>) pubData.get("link");
    if (linkList != null) {
      for (Map<Object, Object> linkMap : linkList) {
        if (linkMap.get("URL") != null) {
          if ("similarity-checking".equals(linkMap.get("intended-application"))) {
            srcFulltextUrl = StringUtils.trimToEmpty(linkMap.get("URL").toString());
          }
          fulltextUrls.append(linkMap.get("URL"));
          fulltextUrls.append(";");
        }
      }
      if (StringUtils.isBlank(srcFulltextUrl)) {
        srcFulltextUrl = StringUtils.trimToEmpty(fulltextUrls.toString().substring(0, fulltextUrls.indexOf(";")));
      }

    }
    // 来源全文路径
    result.put("srcFulltextUrl", srcFulltextUrl);

    result.put("fulltextUrls", fulltextUrls);

    // citedUrl
    result.put("citedUrl", "");
    // sourceUrl
    result.put("sourceUrl", "");

    // 成果类型
    String type = pubData.get("type").toString();
    String bookChapter = "book-section,book-track,book-part,book-chapter";// 书籍章节
    String book = "book,edited-book,reference-book,monograph";// 书/著作
    String journal = "journal-article";// 期刊
    String conference = "proceedings,proceedings-article";// 会议
    String thesis = "dissertation";// 学位论文
    int pubType = 7;
    if (bookChapter.indexOf(type) > -1) {
      pubType = 10;
    } else if (book.indexOf(type) > -1) {
      pubType = 2;
    } else if (journal.indexOf(type) > -1) {
      pubType = 4;
    } else if (conference.indexOf(type) > -1) {
      pubType = 3;
    } else if (thesis.indexOf(type) > -1) {
      pubType = 8;
    }
    if ("book".equals(type)) {
      pubType = 2;
    }

    if ("proceedings".equals(type)) {
      pubType = 3;
    }
    result.put("pubType", pubType);
    // srcDbId,crossref
    result.put("srcDbId", 36);

    // citations 引用次数
    if (pubData.get("is-referenced-by-count") != null) {
      result.put("citations", String.valueOf(pubData.get("is-referenced-by-count")));
    } else {
      result.put("citations", 0);
    }
    // crossref中数据更新时间
    StringBuilder depositedDate = new StringBuilder();
    if (pubData.get("deposited") != null) {
      Map<Object, Object> deposited = (Map<Object, Object>) pubData.get("deposited");
      if (deposited.get("date-parts") != null) {
        List<List<Integer>> dateParts = (List<List<Integer>>) deposited.get("date-parts");
        List<Integer> date = dateParts.get(0);
        for (Integer s : date) {
          depositedDate.append(s);
          depositedDate.append("-");
        }
      }
    }
    result.put("deposited", depositedDate.substring(0, depositedDate.length() - 1));

    Map<Object, Object> datemap = new HashMap<Object, Object>();
    if (pubData.get("published-print") != null) {
      datemap = (Map<Object, Object>) pubData.get("published-print");

    } else if (pubData.get("published-online") != null) {
      datemap = (Map<Object, Object>) pubData.get("published-online");
    } else if (pubData.get("approved") != null) {// 学位论文发表时间
      datemap = (Map<Object, Object>) pubData.get("approved");
    }
    StringBuilder publishDate = new StringBuilder();
    if (datemap.get("date-parts") != null) {
      List<List<Integer>> dateParts = (List<List<Integer>>) datemap.get("date-parts");
      List<Integer> date = dateParts.get(0);
      for (Integer s : date) {
        publishDate.append(s);
        publishDate.append("-");
      }
    }
    String pubYear = "";
    // publishDate
    if (StringUtils.isNotBlank(publishDate)) {
      pubYear = publishDate.substring(0, publishDate.indexOf("-"));
      result.put("publishDate", publishDate.substring(0, publishDate.length() - 1));
    } else {
      result.put("publishDate", "");
    }
    result.put("pubYear", pubYear);
    // HCP 高被引字段
    result.put("HCP", 0);
    // HP 热门文章
    result.put("HP", 0);
  }

  @SuppressWarnings("unchecked")
  private void dealPubMembers(Map<String, Object> pubData, Map<String, Object> result) {
    List<PubMemberDTO> memberList = new ArrayList<PubMemberDTO>();
    result.put("crossref_member_id", pubData.get("member"));
    // 记录成果作者的单位信息
    Set<String> organizations = new HashSet<String>();
    // 成果作者名拼接字段
    StringBuilder authorNames = new StringBuilder();
    List<Map<String, Object>> authorList = (List<Map<String, Object>>) pubData.get("author");
    if (CollectionUtils.isNotEmpty(authorList)) {
      PubMemberDTO pubMemberDTO = null;
      Integer seqNo = 0;
      for (Map<String, Object> author : authorList) {
        if (author != null) {
          String authorName = "";
          Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
          if (author.get("given") != null
              && pattern.matcher(author.get("given").toString().trim().replace(" ", "")).matches()
              || author.get("given").toString().equals(author.get("family").toString())) {// 中文名字
            if (author.get("family").toString().length() == 1) {
              authorName = author.get("family") + "" + author.get("given");
            } else {
              if (pattern.matcher(author.get("given").toString().trim().replace(" ", "")).matches()) {
                authorName = author.get("given").toString();
              } else {
                authorName = author.get("family").toString();
              }
            }
          } else {
            // 作者名
            if (author.get("given") != null && author.get("family") != null) {
              authorName = author.get("given") + ", " + author.get("family");
            } else if (author.get("given") != null && author.get("family") == null) {
              authorName = (String) author.get("given");
            } else if (author.get("given") == null && author.get("family") != null) {
              authorName = (String) author.get("family");
            }
          }
          // 第一作者
          boolean firstAuthor = "first".equals(String.valueOf(author.get("sequence")));
          // 作者有多个部门，一个部门一个作者进行拆分，暂时按照此规则进行构建数据，后面结构改动会进行改正此部分逻辑
          List<Map<String, Object>> deptList = (List<Map<String, Object>>) author.get("affiliation");
          if (CollectionUtils.isNotEmpty(deptList)) {
            for (Map<String, Object> dept : deptList) {
              if (dept != null && dept.get("name") != null) {
                String deptName = String.valueOf(dept.get("name"));
                pubMemberDTO = new PubMemberDTO();
                pubMemberDTO.setName(authorName);
                pubMemberDTO.setSeqNo(++seqNo);
                pubMemberDTO.setDept(deptName);
                pubMemberDTO.setFirstAuthor(firstAuthor);
                // 默认值
                pubMemberDTO.setEmail("");
                pubMemberDTO.setOwner(false);
                pubMemberDTO.setCommunicable(false);
                pubMemberDTO.setInsNames(new ArrayList<MemberInsDTO>());
                memberList.add(pubMemberDTO);

                // 处理organizations信息
                organizations.add(deptName);
              }
            }
          } else {
            // 不存在一个部门信息的作者
            pubMemberDTO = new PubMemberDTO();
            pubMemberDTO.setName(authorName);
            pubMemberDTO.setSeqNo(++seqNo);
            pubMemberDTO.setDept("");
            pubMemberDTO.setFirstAuthor(firstAuthor);
            // 默认值
            pubMemberDTO.setEmail("");
            pubMemberDTO.setOwner(false);
            pubMemberDTO.setCommunicable(false);
            pubMemberDTO.setInsNames(new ArrayList<MemberInsDTO>());
            memberList.add(pubMemberDTO);
          }

          // 处理作者名信息
          authorNames.append(authorName + ";");
        }
      }
    }

    // "organization": "单位地址信息",
    if (organizations.size() > 0) {
      StringBuilder depts = new StringBuilder();
      for (String data : organizations) {
        depts.append(data);
        depts.append(".");
      }
      result.put("organization", depts.substring(0, depts.length() - 1));
    } else {
      result.put("organization", "");
    }

    // 作者
    if (StringUtils.isNotBlank(authorNames)) {
      result.put("authorNames", authorNames);
    } else {
      result.put("authorNames", "");
    }
    result.put("members", memberList);
  }

  private void buildPubTypeInfo(Map<String, Object> pubData, Map<String, Object> result) {
    int pubType = Integer.parseInt(result.get("pubType").toString());
    Map<String, Object> typeInfo = new HashMap<>();
    switch (pubType) {
      case 2:// 书/著作
      case 10:// 书籍章节
        typeInfo = dealPubBook(pubData);
        break;
      case 3:// 会议
        typeInfo = dealPubConfPaper(pubData, result);
        break;
      case 4:// 期刊
        Map<String, Object> journalInfo = dealPubJournal(pubData);
        typeInfo = JacksonUtils.json2HashMap(journalInfo.get("typeInfo").toString());
        if (journalInfo.get("eissn") != null) {
          result.put("eissn", journalInfo.get("eissn"));
        }
        break;
      case 8:// 学位
        typeInfo = dealPubThesis(pubData, result);
        break;
      case 7:
        typeInfo = dealpubOther(pubData);
        break;
      default:
        typeInfo = dealpubOther(pubData);
        break;
    }
    result.put("pubTypeInfo", typeInfo);
  }


  private Map<String, Object> dealpubOther(Map<String, Object> pubData) {
    return null;
  }

  private Map<String, Object> dealPubBook(Map<String, Object> pubData) {
    BookInfoDTO bookInfoDTO = new BookInfoDTO();
    // 书名
    if (pubData.get("container-title") != null) {
      List<String> titleList = (List<String>) pubData.get("container-title");
      bookInfoDTO.setName(titleList.get(0));
    } else {
      bookInfoDTO.setName("");
    }
    // 书籍类型 no
    String type = String.valueOf(pubData.get("type"));
    bookInfoDTO.setType(buildBookType(type));
    // 出版社
    bookInfoDTO.setPublisher(String.valueOf(pubData.get("publisher")));
    // 页数 no
    bookInfoDTO.setTotalPages(0);
    // 总字数 no
    bookInfoDTO.setTotalWords(0);
    // 丛书名
    bookInfoDTO.setSeriesName("");
    StringBuilder editors = new StringBuilder();
    List<Map<Object, Object>> editorList = (List<Map<Object, Object>>) pubData.get("editor");
    if (editorList != null) {
      for (Map<Object, Object> editorMap : editorList) {
        String authorName = editorMap.get("given") + " " + editorMap.get("family");
        editors.append(authorName);
        editors.append(";");
      }
    }
    // 书籍编辑
    if (StringUtils.isNotBlank(editors)) {
      bookInfoDTO.setEditors(editors.substring(0, editors.length() - 1));
    } else {
      bookInfoDTO.setEditors("");
    }
    // 章节号码
    bookInfoDTO.setChapterNo("");
    // 开始结束页码，文章号合并
    String pageNumber = null;
    if (pubData.get("page") != null && StringUtils.isNotBlank(String.valueOf(pubData.get("page")))) {
      pageNumber = String.valueOf(pubData.get("page"));
    }
    if (pageNumber == null && pubData.get("article-number") != null) {
      pageNumber = String.valueOf(pubData.get("article-number"));
    }

    bookInfoDTO.setPageNumber(StringUtils.trimToEmpty(pageNumber));
    List<String> ISBNList = (List<String>) pubData.get("ISBN");
    // 书籍isbn
    if (ISBNList != null && ISBNList.size() > 0) {
      bookInfoDTO.setISBN(ISBNList.get(0));
    } else {
      bookInfoDTO.setISBN("");
    }
    // 语种
    bookInfoDTO
        .setLanguage(String.valueOf(pubData.get("language")) == "null" ? "" : String.valueOf(pubData.get("language")));
    String jsonStr = JacksonUtils.jsonObjectSerializer(bookInfoDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }

  /**
   * crossref数据专用
   * 
   * @param type
   * @return
   */
  private PubBookTypeEnum buildBookType(String type) {

    if (StringUtils.isBlank(type)) {
      return PubBookTypeEnum.NULL;
    }
    if ("monograph".equalsIgnoreCase(type)) {
      return PubBookTypeEnum.MONOGRAPH;
    }
    return PubBookTypeEnum.NULL;
  }

  private Map<String, Object> dealPubConfPaper(Map<String, Object> pubData, Map<String, Object> result) {
    ConferencePaperDTO conferencePaperDTO = new ConferencePaperDTO();
    // 论文集名
    if (pubData.get("container-title") != null) {
      List<String> titleList = (List<String>) pubData.get("container-title");
      conferencePaperDTO.setPapers(titleList.get(0));
    } else {
      conferencePaperDTO.setPapers("");
    }
    String pageNumber = "";
    String name = "";
    String organizer = "";
    String location = "";
    StringBuilder startDate = new StringBuilder();
    StringBuilder endDate = new StringBuilder();
    // 论文类别
    String paperType = pubData.get("type").toString();
    conferencePaperDTO.setPaperType(buildPaperType(paperType));
    if (pubData.get("event") != null) {
      Map<Object, Object> event = (Map<Object, Object>) pubData.get("event");
      name = event.get("name").toString();
      if (event.get("location") != null) {
        location = event.get("location").toString();
      }
      List<String> organizers = (List<String>) event.get("sponsor");
      organizer = organizers != null && organizers.size() > 0 ? organizers.get(0) : "";
      Map<Object, Object> start = (Map<Object, Object>) event.get("start");
      Map<Object, Object> end = (Map<Object, Object>) event.get("end");
      if (start != null) {
        List<List<Integer>> startLists = (List<List<Integer>>) start.get("date-parts");
        List<Integer> startList = startLists.get(0);
        for (Integer date : startList) {
          startDate.append(date);
          startDate.append("-");
        }
      }
      if (end != null) {
        List<List<Integer>> endLists = (List<List<Integer>>) end.get("date-parts");
        List<Integer> endList = endLists.get(0);
        for (Integer date : endList) {
          endDate.append(date);
          endDate.append("-");
        }
      }
    }
    result.put("country", location);
    // 会议名称
    conferencePaperDTO.setName(name);
    // 会议组织者
    conferencePaperDTO.setOrganizer(organizer);
    // 开始日期
    if (StringUtils.isNotEmpty(startDate.toString())) {
      conferencePaperDTO.setStartDate(startDate.toString().substring(0, startDate.length() - 1));
    } else {
      conferencePaperDTO.setStartDate("");
    }
    // 结束日期
    if (StringUtils.isNotEmpty(endDate.toString())) {
      conferencePaperDTO.setEndDate(endDate.toString().substring(0, endDate.length() - 1));
    } else {
      conferencePaperDTO.setEndDate("");
    }
    // 页码
    conferencePaperDTO.setPageNumber(pageNumber);
    String jsonStr = JacksonUtils.jsonObjectSerializer(conferencePaperDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }

  private PubConferencePaperTypeEnum buildPaperType(String paperType) {
    return PubConferencePaperTypeEnum.NULL;
  }

  private Map<String, Object> dealPubJournal(Map<String, Object> pubData) {
    JournalInfoDTO journalInfoDTO = new JournalInfoDTO();
    // 期刊ID
    journalInfoDTO.setJid(null);
    if (pubData.get("container-title") != null) {
      List<String> titleList = (List<String>) pubData.get("container-title");
      journalInfoDTO.setName(titleList.get(0));
    } else {
      journalInfoDTO.setName("");
    }
    // 发表状态(P已发表/A已接收),默认已发表
    journalInfoDTO.setPublishStatus("P");
    // 期号
    if (pubData.get("volume") != null) {
      journalInfoDTO.setVolumeNo(pubData.get("volume").toString());
    } else {
      journalInfoDTO.setVolumeNo("");
    }
    // 卷号
    if (pubData.get("issue") != null) {
      journalInfoDTO.setIssue(pubData.get("issue").toString());
    } else {
      journalInfoDTO.setIssue("");
    }
    // 开始结束页码，文章号合并
    String pageNumber = null;
    if (pubData.get("page") != null && StringUtils.isNotBlank(pubData.get("page").toString())) {
      pageNumber = pubData.get("page").toString();
    }
    if (pageNumber == null && pubData.get("article-number") != null) {
      pageNumber = pubData.get("article-number").toString();
    }
    journalInfoDTO.setPageNumber(StringUtils.trimToEmpty(pageNumber));
    // issn
    if (pubData.get("ISSN") != null) {
      List<String> ISSNList = (List<String>) pubData.get("ISSN");
      if (ISSNList.size() > 1) {
        // 有两个issn的情况取pissn ，eissn存至crossref_other_info
        if (pubData.get("issn-type") != null) {
          List<Map<String, String>> DataList = (List<Map<String, String>>) pubData.get("issn-type");
          for (Map<String, String> map : DataList) {
            if ("print".equals(map.get("type"))) {
              journalInfoDTO.setISSN(map.get("value"));
            } else if ("electronic".equals(map.get("type"))) {
              pubData.put("eissn", map.get("value"));
            }
          }
        }
      } else if (ISSNList.size() == 1) {
        journalInfoDTO.setISSN(ISSNList.get(0));
      }
    } else {
      journalInfoDTO.setISSN("");
    }
    String jsonStr = JacksonUtils.jsonObjectSerializer(journalInfoDTO);
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("typeInfo", jsonStr);
    if (pubData.get("eissn") != null) {
      map.put("eissn", pubData.get("eissn"));
    }
    return map;
  }

  private Map<String, Object> dealPubThesis(Map<String, Object> pubData, Map<String, Object> result) {
    ThesisInfoDTO thesisInfoDTO = new ThesisInfoDTO();
    // 学位
    if (pubData.get("degree") != null) {
      List<String> degreeList = (List<String>) pubData.get("degree");
      String degree = degreeList.get(0);
      if (degree.contains("D")) {
        thesisInfoDTO.setDegree(PubThesisDegreeEnum.DOCTOR);
      } else if (degree.contains("M")) {
        thesisInfoDTO.setDegree(PubThesisDegreeEnum.MASTER);
      }
    } else {
      thesisInfoDTO.setDegree(PubThesisDegreeEnum.OTHER);
    }
    if (pubData.get("institution") != null) {
      Map<String, Object> DataMap = (Map<String, Object>) pubData.get("institution");
      if (DataMap.get("name") != null) {
        thesisInfoDTO.setIssuingAuthority(DataMap.get("name").toString());
      } else {
        thesisInfoDTO.setIssuingAuthority("");
      }
      if (DataMap.get("place") != null) {
        result.put("country", DataMap.get("place").toString());
      } else {
        result.put("country", "");
      }
      if (DataMap.get("department") != null) {
        List<String> deptList = (List<String>) DataMap.get("department");
        thesisInfoDTO.setDepartment(deptList.get(0));
      } else {
        thesisInfoDTO.setDepartment("");
      }
    }
    List<String> ISBNList = (List<String>) pubData.get("ISBN");
    // 书籍isbn
    if (ISBNList != null && ISBNList.size() > 0) {
      thesisInfoDTO.setISBN(ISBNList.get(0));
    } else {
      thesisInfoDTO.setISBN("");
    }
    thesisInfoDTO.setDefenseDate(result.get("publishDate").toString());
    String jsonStr = JacksonUtils.jsonObjectSerializer(thesisInfoDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }

  @Override
  public void updatePubInfoInSolr(Long currentPubId) {
    // 访问V8pub系统接口更新sorl索引
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("des3PubId", Des3Utils.encodeToDes3(currentPubId.toString()));
    String SERVER_URL = scmDomain + V8pubQueryPubConst.PDWHUPDATESORL_URL;
    restTemplate.postForObject(SERVER_URL, params, String.class);
  }


}
