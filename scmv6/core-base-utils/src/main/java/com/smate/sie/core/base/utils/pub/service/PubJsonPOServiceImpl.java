package com.smate.sie.core.base.utils.pub.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.sie.core.base.utils.dao.pub.PubJsonPODao;
import com.smate.sie.core.base.utils.date.DateUtils;
import com.smate.sie.core.base.utils.model.pub.PubJsonPO;
import com.smate.sie.core.base.utils.pub.dom.AwardsInfoBean;
import com.smate.sie.core.base.utils.pub.dom.BookInfoBean;
import com.smate.sie.core.base.utils.pub.dom.ConferencePaperBean;
import com.smate.sie.core.base.utils.pub.dom.JournalInfoBean;
import com.smate.sie.core.base.utils.pub.dom.MemberInsBean;
import com.smate.sie.core.base.utils.pub.dom.OtherInfoBean;
import com.smate.sie.core.base.utils.pub.dom.PatentInfoBean;
import com.smate.sie.core.base.utils.pub.dom.PubDetailDOM;
import com.smate.sie.core.base.utils.pub.dom.PubMemberBean;
import com.smate.sie.core.base.utils.pub.dom.SiePubSituationBean;
import com.smate.sie.core.base.utils.pub.dom.ThesisInfoBean;
import com.smate.sie.core.base.utils.pub.dto.MemberInsDTO;
import com.smate.sie.core.base.utils.pub.dto.PubMemberDTO;
import com.smate.sie.core.base.utils.pub.dto.PubSituationDTO;
import com.smate.sie.core.base.utils.pub.exception.ServiceException;

@Service("pubJsonPOService")
@Transactional(rollbackFor = Exception.class)
public class PubJsonPOServiceImpl implements PubJsonPOService {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private PubJsonPODao pubJsonPODao;

  @SuppressWarnings("rawtypes")
  @Override
  public PubDetailDOM getDOMByIdAndType(Long pubId, Integer pubType) throws ServiceException {
    PubDetailDOM pubDetail = new PubDetailDOM();
    try {
      PubJsonPO pubJsonPO = pubJsonPODao.get(pubId);
      String json = pubJsonPO.getPubJson();
      switch (pubType) {
        case 1: // 奖励
          pubDetail =
              (PubDetailDOM) JacksonUtils.jsonObject(json, new TypeReference<PubDetailDOM<AwardsInfoBean>>() {});
          break;
        case 2:// 书籍
          pubDetail = (PubDetailDOM) JacksonUtils.jsonObject(json, new TypeReference<PubDetailDOM<BookInfoBean>>() {});
          break;
        case 3:// 会议论文
          pubDetail =
              (PubDetailDOM) JacksonUtils.jsonObject(json, new TypeReference<PubDetailDOM<ConferencePaperBean>>() {});
          break;
        case 4:// 期刊论文
          pubDetail =
              (PubDetailDOM) JacksonUtils.jsonObject(json, new TypeReference<PubDetailDOM<JournalInfoBean>>() {});
          break;
        case 5:// 专利
          pubDetail =
              (PubDetailDOM) JacksonUtils.jsonObject(json, new TypeReference<PubDetailDOM<PatentInfoBean>>() {});
          break;
        case 7:// 其他
          pubDetail = (PubDetailDOM) JacksonUtils.jsonObject(json, new TypeReference<PubDetailDOM<OtherInfoBean>>() {});
          break;
        case 8:// 学位论文
          pubDetail =
              (PubDetailDOM) JacksonUtils.jsonObject(json, new TypeReference<PubDetailDOM<ThesisInfoBean>>() {});
          break;
        case 10:// 书籍章节
          pubDetail = (PubDetailDOM) JacksonUtils.jsonObject(json, new TypeReference<PubDetailDOM<BookInfoBean>>() {});
          break;
      }
      return pubDetail;
    } catch (Exception e) {
      logger.error("查询成果详情时出错！pubId={}", pubId);
      throw new ServiceException("查询成果详情时出错！pubId=" + pubId, e);
    }
  }

  @Override
  public void savePubJson(PubJsonDTO pubJson) throws ServiceException {
    try {
      PubDetailDOM detail = pubDetailHandle(pubJson);
      PubJsonPO jsonPO = pubJsonPODao.get(pubJson.pubId);
      if (jsonPO == null) {
        jsonPO = new PubJsonPO();
        jsonPO.setPubId(pubJson.pubId);
        jsonPO.setPubJson(JacksonUtils.jsonObjectSerializer(detail));
      } else {
        jsonPO.setPubJson(JacksonUtils.jsonObjectSerializer(detail));
      }
      pubJsonPODao.save(jsonPO);
    } catch (Exception e) {
      logger.error("保存成果详情时出错！pubId={}", pubJson.pubId);
      throw new ServiceException("保存成果详情时出错！pubId=" + pubJson.pubId, e);
    }
  }

  public PubDetailDOM pubDetailHandle(PubJsonDTO pub) throws ServiceException {
    // 构造成果详情对象
    PubDetailDOM pubDetail = null;
    try {
      pubDetail = buildPubDetail(pub);
    } catch (Exception e) {
      logger.error("sie库成果详情服务：保存成果详情出错！PubDetailDOM={}", pubDetail, e);
      throw new ServiceException(this.getClass().getSimpleName() + "保存或更新成果详情出错!", e);
    }
    return pubDetail;
  }

  /**
   * 构造标准json对象
   * 
   * @param sourceMap
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  private PubDetailDOM buildPubDetail(PubJsonDTO pub) {
    PubDetailDOM pubDetail = new PubDetailDOM();
    pubDetail.setpubId(pub.pubId);
    pubDetail.setInsId(pub.insId);
    pubDetail.setTitle(pub.title);
    pubDetail.setSummary(pub.summary);
    String keywords = pub.keywords;
    keywords = StringUtils.isEmpty(keywords) ? "" : keywords;
    keywords = HtmlUtils.htmlEscape(keywords);
    pubDetail.setKeywords(keywords);
    pubDetail.setCitations(ObjectUtils.toString(pub.citations));
    pubDetail.setCitationsUpdateTime(pub.citationsUpdateTime);
    pubDetail.setBriefDesc(pub.briefDesc);
    pubDetail.setAuthorNames(pub.authorNames);
    pubDetail.setDoi(pub.doi);
    pubDetail.setOrganization(pub.organization);
    pubDetail.setFundInfo(pub.fundInfo);
    pubDetail.setDisciplineCode(pub.disciplineCode);
    pubDetail.setDisciplineName(pub.disciplineName);
    pubDetail.setIsPublicCode(pub.isPublicCode);
    pubDetail.setIsPublicName(pub.isPublicName);
    pubDetail.setSituations(buildSituations(pub));// []
    pubDetail.setMembers(buildMembers(pub));// []
    pubDetail.setPubAttachments(null);// []
    pubDetail.setPubTypeCode(pub.pubTypeCode);
    pubDetail.setPubTypeName(pub.pubTypeName);
    pubDetail.setTypeInfo(pub.pubTypeInfoBean);
    pubDetail.setFulltextId(pub.fulltextId);
    pubDetail.setFulltexName(pub.fulltextName);
    pubDetail.setSrcFulltextUrl(pub.srcFulltextUrl);
    pubDetail.setSrcDbId(pub.srcDbId);
    pubDetail.setSourceId(pub.sourceId);
    pubDetail.setSourceUrl(pub.sourceUrl);
    pubDetail.setPublishDate(pub.publishDate);
    pubDetail.setDataFrom(pub.dataFrom);
    pubDetail.setCitedUrl(pub.citedUrl);
    pubDetail.setUpdateTime(DateUtils.dateToStr2(new Date()));
    pubDetail.setHCP(pub.HCP == 1);
    pubDetail.setHP(pub.HP == 1);
    pubDetail.setOA(pub.OA);
    return pubDetail;
  }

  @SuppressWarnings("unchecked")
  private Set<SiePubSituationBean> buildSituations(PubJsonDTO pub) {
    Set<SiePubSituationBean> situationSet = new HashSet<SiePubSituationBean>();
    if (pub.situations != null) {
      List<PubSituationDTO> sitList =
          JacksonUtils.jsonToCollection(pub.situations.toJSONString(), List.class, PubSituationDTO.class);
      if (sitList != null && sitList.size() > 0) {
        SiePubSituationBean siePubSituationBean = null;
        for (PubSituationDTO p : sitList) {
          String libraryName = p.getLibraryName();
          if (StringUtils.isBlank(libraryName)) {
            continue;
          }
          siePubSituationBean = new SiePubSituationBean();
          siePubSituationBean.setSrcId(p.getSrcId());
          siePubSituationBean.setSrcDbId(p.getSrcDbId());
          siePubSituationBean.setSitStatus(p.isSitStatus());
          siePubSituationBean.setLibraryName(libraryName);
          siePubSituationBean.setSrcUrl(p.getSrcUrl());
          siePubSituationBean.setSitOriginStatus(p.isSitOriginStatus());
          situationSet.add(siePubSituationBean);
        }
      }
    }
    return situationSet;
  }

  @SuppressWarnings("unchecked")
  private List<PubMemberBean> buildMembers(PubJsonDTO pub) {
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
          pubMemberBean.setPmId(p.getPmId());
          pubMemberBean.setName(name);
          pubMemberBean.setPsnId(p.getPsnId());
          pubMemberBean.setCommunicable(p.isCommunicable());
          pubMemberBean.setEmail(p.getEmail());
          pubMemberBean.setFirstAuthor(p.isFirstAuthor());
          // pubMemberBean.setInsName(p.getInsName());
          pubMemberBean.setInsNames(buildInsNames(p.getInsNames()));
          pubMemberBean.setInsId(p.getInsId());
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

  // 合并单位查重用
  @Override
  public PubJsonDTO getPubJsonDTOByIdAndType(Long pubId, Integer pubType) throws ServiceException {
    PubDetailDOM pubDetail = getDOMByIdAndType(pubId, pubType);
    PubJsonDTO pubJson = new PubJsonDTO();
    pubJson.authorNames = pubDetail.getAuthorNames();
    pubJson.briefDesc = "";
    pubJson.citations = NumberUtils.toInt(pubDetail.getCitations());
    pubJson.citationsUpdateTime = "";
    pubJson.citedUrl = "";
    pubJson.dataFrom = pubDetail.getDataFrom();
    pubJson.doi = pubDetail.getDoi();
    pubJson.fundInfo = pubDetail.getFundInfo();
    pubJson.insId = pubDetail.getInsId();
    pubJson.keywords = pubDetail.getKeywords();
    pubJson.pubId = pubDetail.getpubId();
    pubJson.publishDate = pubDetail.getPublishDate();
    pubJson.pubTypeCode = pubDetail.getPubTypeCode();
    pubJson.srcDbId = pubDetail.getSrcDbId();
    pubJson.pubTypeInfoBean = pubDetail.getTypeInfo();
    pubJson.title = pubDetail.getTitle();
    return pubJson;
  }

}
