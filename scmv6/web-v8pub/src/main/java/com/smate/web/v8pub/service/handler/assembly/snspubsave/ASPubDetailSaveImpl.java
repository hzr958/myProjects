package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.dom.MemberInsBean;
import com.smate.web.v8pub.dom.PubMemberBean;
import com.smate.web.v8pub.dom.PubSituationBean;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;
import com.smate.web.v8pub.dto.MemberInsDTO;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubSnsDetailService;

/**
 * 保存个人成果 详情
 * 
 * @author tsz
 *
 * @date 2018年7月13日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubDetailSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubSnsDetailService pubSnsDetailService;

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
    // 构造成果详情对象
    PubSnsDetailDOM pubDetail = null;
    try {
      pubDetail = build(pub);
      pubSnsDetailService.saveOrUpdate(pubDetail);
      pub.detailsJson = JacksonUtils.jsonObjectSerializer(pubDetail);
      logger.debug("保存或更新sns库成果详情成功");
    } catch (Exception e) {
      logger.error("个人库成果详情服务：保存成果详情出错！PubSnsDetailDOM={}", pubDetail, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "保存或更新sns库成果详情出错!", e);
    }
    return null;
  }

  /**
   * 构造标准json对象
   * 
   * @param sourceMap
   * @return
   */
  @SuppressWarnings("unchecked")
  private PubSnsDetailDOM build(PubDTO pub) {
    PubSnsDetailDOM pubDetail = null;
    pubDetail = pubSnsDetailService.get(pub.pubId);
    if (pubDetail != null) {
      pub.historyDetailsJson = JacksonUtils.jsonObjectSerializer(pubDetail);// 保存成果历史记录
    }
    if (pubDetail == null) {
      pubDetail = new PubSnsDetailDOM();
      pubDetail.setPubId(pub.pubId);
    }
    pubDetail.setAuthorNames(pub.authorNames);
    pubDetail.setBriefDesc(pub.briefDesc);
    pubDetail.setCountryId(pub.countryId);
    pubDetail.setDoi(pub.doi);
    pubDetail.setFulltextId(pub.fulltextId);
    pubDetail.setFundInfo(pub.fundInfo);
    String keywords = pub.keywords;
    keywords = StringUtils.isEmpty(keywords) ? "" : keywords;
    keywords = HtmlUtils.htmlEscape(keywords);
    pubDetail.setKeywords(keywords);
    pubDetail.setOrganization(pub.organization);
    pubDetail.setSrcFulltextUrl(pub.srcFulltextUrl);
    pubDetail.setSummary(pub.summary);
    pubDetail.setCitations(pub.citations);
    pubDetail.setTitle(pub.title);
    pubDetail.setSrcDbId(pub.srcDbId);
    pubDetail.setSourceId(pub.sourceId);
    pubDetail.setSourceUrl(pub.sourceUrl);
    pubDetail.setPubType(pub.pubType);
    pubDetail.setPublishDate(pub.publishDate);
    pubDetail.setTypeInfo(pub.pubTypeInfoBean);
    pubDetail.setMembers(buildMembers(pub));
    pubDetail.setSituations(buildSituations(pub));
    pubDetail.setHCP(pub.HCP == 1);
    pubDetail.setHP(pub.HP == 1);
    pubDetail.setOA(pub.OA);
    return pubDetail;
  }

  @SuppressWarnings("unchecked")
  private Set<PubSituationBean> buildSituations(PubDTO pub) {
    Set<PubSituationBean> situationSet = new HashSet<>();
    if (pub.situations != null) {
      List<PubSituationDTO> sitList =
          JacksonUtils.jsonToCollection(pub.situations.toJSONString(), List.class, PubSituationDTO.class);
      if (sitList != null && sitList.size() > 0) {
        PubSituationBean pubSituationBean = null;
        for (PubSituationDTO p : sitList) {
          String libraryName = p.getLibraryName();
          if (StringUtils.isBlank(libraryName)) {
            continue;
          }
          pubSituationBean = new PubSituationBean();
          pubSituationBean.setSrcId(p.getSrcId());
          pubSituationBean.setSrcDbId(p.getSrcDbId());
          pubSituationBean.setSitStatus(p.isSitStatus());
          pubSituationBean.setLibraryName(libraryName);
          pubSituationBean.setSrcUrl(p.getSrcUrl());
          pubSituationBean.setSitOriginStatus(p.isSitOriginStatus());
          situationSet.add(pubSituationBean);
        }
      }
    }
    return situationSet;
  }

  @SuppressWarnings("unchecked")
  private List<PubMemberBean> buildMembers(PubDTO pub) {
    List<PubMemberBean> list = new ArrayList<>();
    if (pub.members != null) {
      List<PubMemberDTO> memberList =
          JacksonUtils.jsonToCollection(pub.members.toJSONString(), List.class, PubMemberDTO.class);
      if (memberList != null && memberList.size() > 0) {
        PubMemberBean pubMemberBean = null;
        for (PubMemberDTO p : memberList) {
          pubMemberBean = new PubMemberBean();
          String name = p.getName();
          if (StringUtils.isBlank(name)) {
            continue;
          }
          Integer seqNo = p.getSeqNo();
          pubMemberBean.setSeqNo(seqNo);
          // name = HtmlUtils.htmlEscape(name);
          pubMemberBean.setName(name);
          pubMemberBean.setPsnId(p.getPsnId());
          pubMemberBean.setCommunicable(p.isCommunicable());
          pubMemberBean.setEmail(p.getEmail());
          pubMemberBean.setFirstAuthor((seqNo != null && seqNo == 1));
          pubMemberBean.setInsNames(buildInsNames(p.getInsNames()));
          pubMemberBean.setDept(p.getDept());
          pubMemberBean.setName(p.getName());
          pubMemberBean.setMemberId(p.getMemberId());
          list.add(pubMemberBean);
        }
      }
    }
    return list;
  }

  @SuppressWarnings("unchecked")
  private List<MemberInsBean> buildInsNames(List<MemberInsDTO> insNames) {
    String insJson = JacksonUtils.jsonObjectSerializer(insNames);
    return JacksonUtils.jsonToCollection(insJson, List.class, MemberInsBean.class);
  }

  /**
   * 获取成果引用url，根据什么去判断这条成果被谁引用，引用的根本标示 获取成果引用url的条件： 所更新的成果不为中文成果，必须为英文成果
   * 1.首选sourceId，有sourceId使用sourceId 2.没有sourceId，有doi使用doi 3.sourceId和doi均没有，默认为标题组装
   * 
   * @return
   */
  protected String buildCitedUrl(PubDTO pub) {
    String title = null;
    // 其次标题，如果标题都不符合要求的话，那么直接返回null
    if (checkTitleExact(pub.title)) {
      title = pub.title;
    }
    // 首选成果sourceId
    if (pub.srcDbId != null && pub.srcDbId != 0) {
      return "UT=" + pub.srcDbId;
    }
    // 次选成果doi，第一种情况：当doi中的值是以10.开头，直接设置值即可
    if (checkDOIExact(pub.doi, "10.*")) {
      return "DOI=(" + pub.doi + ")";
    }
    // 次选成果doi，第二种情况：当doi中的值是以DOI.开头，需要截取之后的数值进行设置
    if (checkDOIExact(pub.doi.toUpperCase(), "DOI.*")) {
      String doi = pub.doi.toUpperCase();
      doi = doi.substring("DOI.".length());
      return "DOI=(" + doi + ")";
    }
    // 次选成果doi，第三种情况：当doi中的值是以CNKI.开头时，标题不为null，那么设置标题
    if (checkDOIExact(pub.doi.toUpperCase(), "CNKI.*") && StringUtils.isNoneBlank(title)) {
      return "TI=(" + title + ")";
    }
    return null;
  }

  /**
   * 判断内容是否为符合条件数据 1.标题内容不能为null 2.标题必须英文的，中文的不予处理
   * 
   * @param title
   * @return
   */
  protected boolean checkTitleExact(String title) {
    // 不为null
    if (StringUtils.isBlank(title)) {
      return false;
    }
    // 不为中文
    Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
    Matcher m = p.matcher(title);
    if (m.find()) {
      return false;
    }
    return true;
  }

  protected boolean checkDOIExact(String doi, String pattern) {
    if (StringUtils.isBlank(doi)) {
      return false;
    }
    if (!Pattern.matches(pattern, doi)) {
      return false;
    }
    return true;
  }

}
