package com.smate.web.v8pub.service.handler.assembly.construct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.alibaba.fastjson.JSONArray;
import com.smate.core.base.pub.enums.PubLibraryEnum;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.pub.util.CleanCopyrightUtils;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.date.DateStringSplitFormateUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.pubxml.ImportPubXmlUtils;
import com.smate.web.v8pub.dom.AwardsInfoBean;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.ThesisInfoBean;
import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.authorname.PubAuthorNameService;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubSituationService;

/**
 * 所有参数的重新构建：主要是参数长度的限制 <br/>
 * 关于校验 <br/>
 * 1.标题 处理 标题如果超过数据库的长度就截取长度 页面不对标题长度做控制<br/>
 * 2.至于其它的字段 需要注意 验证的时候需要 扩大倍数 长度要是页面长度的两倍<br/>
 * 
 * @author YJ 2018年8月3日
 */
@Transactional(rollbackFor = Exception.class)
public class ASConstructPubParamsImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubAuthorNameService pubAuthorNameService;
  @Autowired
  private PubSituationService pubSituationService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    /**
     * 关键词长度达到500字符截断时，需要找到保留中的最后一个分号，只保留分号前面的词，分号后多余的字符不需要保留。 如：……XX;yy;y500x;xxx;yyy…… 则保留的词是……xx和yy
     */
    try {
      pub.title = constructTitle(pub.title);
      pub.summary = constructSummary(pub.summary);
      // 格式化发表日期调整
      pub.publishDate = contructPublishDate(pub);
      pub.authorNames = constructAuthorNames(pub.authorNames, pub.members);
      pub.citations = buildCitations(pub.citations);
      pub.awardCount = buildCitations(pub.awardCount);
      pub.commentCount = buildCitations(pub.commentCount);
      pub.shareCount = buildCitations(pub.shareCount);
      pub.keywords = buildKeywords(pub.keywords);
      pub.doi = buildDOI(pub.doi);
      pub.briefDesc = buildBriefDesc(pub.briefDesc);
      // SCM-22703 去除organization字段的长度控制，个人库与基准库都不进行控制，因为只存储在mongodb中
      // pub.organization = buildOrganization(pub.organization);
      pub.fundInfo = buildFunInfo(pub.fundInfo);
      pub.countryId = buildCountryId(pub.countryId);
      if (pub.pubHandlerName.toLowerCase().indexOf("pdwh") == -1) {// 只有当不是基准库成果时才会去重新构建收录情况
        pub.situations = buildSuations(pub.situations);
      }
      logger.debug("sns库成果参数预处理，长度调整成功");
    } catch (Exception e) {
      logger.error("成果参数处理出错！", e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "成果参数预处理出错！", e);
    }

    return null;
  }

  /**
   * 重新构建收录情况，只保留一个OTHER的文献库，且优先保存有srcId或者srcUrl
   * 
   * @param situations
   * @return
   */
  @SuppressWarnings("unchecked")
  private JSONArray buildSuations(JSONArray situations) {
    if (situations == null) {
      return situations;
    }
    PubSituationDTO pubSituationDTO = null;
    List<PubSituationDTO> sitList =
        JacksonUtils.jsonToCollection(situations.toJSONString(), List.class, PubSituationDTO.class);
    int flag = -1; // 记录传入的OTHER文献库的srcId或者srcUrl有值 并在sitList的位置
    List<Integer> indexs = new ArrayList<>(); // 标记需要保留下来的 文献库
    if (sitList != null && sitList.size() > 0) {
      for (int i = 0; i < sitList.size(); i++) {
        pubSituationDTO = sitList.get(i);
        String libraryName = pubSituationDTO.getLibraryName();
        libraryName = PubLibraryEnum.parse(libraryName).desc;
        if ("OTHER".equalsIgnoreCase(libraryName)) {
          if (flag == -1) {
            // 先保留第一个OTHER的文献库
            flag = i;
          }
          if (StringUtils.isNotBlank(pubSituationDTO.getSrcId())
              || StringUtils.isNotBlank(pubSituationDTO.getSrcUrl())) {
            flag = i;
          }
          pubSituationDTO.setLibraryName(libraryName);
        } else {
          indexs.add(i);
        }
      }
      if (flag != -1) {
        // OTHER中有数据，也要保存
        indexs.add(flag);
      }
    }
    List<PubSituationDTO> saveList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(indexs)) {
      for (int saveIndex : indexs) {
        // 需要保存下来的文献库
        if (saveIndex < sitList.size()) {
          saveList.add(sitList.get(saveIndex));
        }
      }
    }
    // 补全完善srcDbId为空的数据
    pubSituationService.perfectSrcDbId(saveList);
    return JSONArray.parseArray(JacksonUtils.listToJsonStr(saveList));
  }

  private Long buildCountryId(Long countryId) {
    if (NumberUtils.isNullOrZero(countryId)) {
      // 为0也是返回null
      return null;
    }
    return countryId;
  }

  private String buildFunInfo(String fundInfo) {
    fundInfo = StringUtils.trimToEmpty(fundInfo);
    fundInfo = StringUtils.substring(fundInfo, 0, 1000);
    return fundInfo;
  }

  @SuppressWarnings("unused")
  private String buildOrganization(String organization) {
    organization = StringUtils.trimToEmpty(organization);
    organization = StringUtils.substring(organization, 0, 500);
    return organization;
  }

  private String buildBriefDesc(String briefDesc) {
    briefDesc = StringUtils.trimToEmpty(briefDesc);
    briefDesc = StringUtils.substring(briefDesc, 0, 500);
    return briefDesc;
  }

  private String buildDOI(String doi) {
    doi = StringUtils.trimToEmpty(doi);
    doi = StringUtils.substring(doi, 0, 100);
    return doi;
  }

  private String buildKeywords(String keywords) {
    keywords = HtmlUtils.htmlUnescape(keywords);
    keywords = StringUtils.trimToEmpty(keywords);
    if (StringUtils.isNotBlank(keywords)) {
      List<String> kwList = ImportPubXmlUtils.parsePubKeywords2(keywords);
      if (CollectionUtils.isNotEmpty(kwList)) {
        keywords = kwList.stream().map(keyword -> StringUtils.trimToEmpty(keyword)).collect(Collectors.joining("; "));
      }
    }
    keywords = XmlUtil.subStr500char(keywords);
    return keywords;
  }

  private Integer buildCitations(Integer citations) {
    if (citations == null) {
      return 0;
    }
    return citations;
  }

  private String constructAuthorNames(String authorNames, JSONArray members) {
    authorNames = HtmlUtils.htmlUnescape(authorNames);
    authorNames = pubAuthorNameService.disposeAuthorName(members, authorNames);
    return authorNames;
  }

  private String contructPublishDate(PubDTO pub) {
    String publishDate = pub.publishDate;
    publishDate = StringUtils.trimToEmpty(publishDate);
    // 学位取：答辩日期字段
    if (pub.pubType.intValue() == PublicationTypeEnum.THESIS) {
      ThesisInfoBean thesisInfoBean = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), ThesisInfoBean.class);
      if (thesisInfoBean != null) {
        publishDate = thesisInfoBean.getDefenseDate();
      }
    }

    // 专利取：申请日期字段
    if (pub.pubType.intValue() == PublicationTypeEnum.PATENT) {
      PatentInfoBean patentInfoBean = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), PatentInfoBean.class);
      if (patentInfoBean != null) {
        Integer status = patentInfoBean.getStatus();
        if (status != null && status == 1) {
          // 授权状态
          publishDate = patentInfoBean.getStartDate();
        } else {
          // 申请状态
          publishDate = patentInfoBean.getApplicationDate();
        }
      }
    }

    // 奖励取：授奖日期字段
    if (pub.pubType.intValue() == PublicationTypeEnum.AWARD) {
      AwardsInfoBean awardsInfoBean = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), AwardsInfoBean.class);
      if (awardsInfoBean != null) {
        publishDate = awardsInfoBean.getAwardDate();
      }
    }
    if (StringUtils.isBlank(publishDate)) {
      return "";
    }

    Map<String, String> publishDateMap = DateStringSplitFormateUtil.split(publishDate);
    if (publishDateMap.get("year") != null) {
      pub.publishYear = Integer.valueOf(publishDateMap.get("year"));
    }
    if (publishDateMap.get("month") != null) {
      pub.publishMonth = Integer.valueOf(publishDateMap.get("month"));
    }
    if (publishDateMap.get("day") != null) {
      pub.publishDay = Integer.valueOf(publishDateMap.get("day"));
    }
    pub.publishDate = publishDateMap.get("fomate_date");
    return pub.publishDate;
  }

  private String constructSummary(String summary) {
    summary = StringUtils.trimToEmpty(summary);
    summary = StringUtils.substring(summary, 0, 20000);
    // 处理摘要的版权信息
    summary = CleanCopyrightUtils.cleanSummary(summary);
    // 处理<br>
    summary = summary.replace("&lt;br&gt;", "\n");
    return summary;
  }

  private String constructTitle(String title) {
    title = StringUtils.trimToEmpty(title);
    title = StringUtils.substring(title, 0, 2000);
    return title;
  }

}
