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
import com.smate.core.base.utils.string.StringUtils;
import com.smate.sie.core.base.utils.dao.pub.PatJsonPODao;
import com.smate.sie.core.base.utils.date.DateUtils;
import com.smate.sie.core.base.utils.model.pub.PatJsonPO;
import com.smate.sie.core.base.utils.pub.dom.MemberInsBean;
import com.smate.sie.core.base.utils.pub.dom.PatMemberBean;
import com.smate.sie.core.base.utils.pub.dom.PatentInfoBean;
import com.smate.sie.core.base.utils.pub.dom.PubAttachmentsBean;
import com.smate.sie.core.base.utils.pub.dom.PubDetailDOM;
import com.smate.sie.core.base.utils.pub.dom.SiePubSituationBean;
import com.smate.sie.core.base.utils.pub.dto.PubAttachmentsDTO;
import com.smate.sie.core.base.utils.pub.dto.PubMemberDTO;
import com.smate.sie.core.base.utils.pub.dto.PubSituationDTO;
import com.smate.sie.core.base.utils.pub.exception.ServiceException;

/**
 * patjson接口服务实现类
 * 
 * @author lijianming
 * @date 2019年3月14日
 */
@Service("patJsonPOService")
@Transactional(rollbackFor = Exception.class)
public class PatJsonPOServiceImpl implements PatJsonPOService {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private PatJsonPODao patJsonPODao;

  @SuppressWarnings("rawtypes")
  @Override
  public void savePatJson(PubJsonDTO patJson) throws ServiceException {
    try {
      PubDetailDOM detail = pubDetailHandle(patJson);
      PatJsonPO jsonPO = patJsonPODao.get(patJson.pubId);
      if (jsonPO == null) {
        jsonPO = new PatJsonPO();
        jsonPO.setPatId(patJson.pubId);
        jsonPO.setPatJson(JacksonUtils.jsonObjectSerializerNoNull(detail));
      } else {
        jsonPO.setPatJson(JacksonUtils.jsonObjectSerializerNoNull(detail));
      }
      patJsonPODao.save(jsonPO);
    } catch (Exception e) {
      logger.error("保存专利详情时出错！patId={}", patJson.pubId);
      throw new ServiceException("保存专利详情时出错！patId=" + patJson.pubId, e);
    }
  }

  @SuppressWarnings("rawtypes")
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
    pubDetail.setPubAttachments(buildPubAttachments(pub));// []
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
    Set<SiePubSituationBean> situationSet = new HashSet<>();
    if (pub.situations != null) {
      List<PubSituationDTO> sitList =
          JacksonUtils.jsonToCollection(pub.situations.toJSONString(), List.class, PubSituationDTO.class);
      if (sitList != null && sitList.size() > 0) {
        SiePubSituationBean pubSituationBean = null;
        for (PubSituationDTO p : sitList) {
          String libraryName = p.getLibraryName();
          if (StringUtils.isBlank(libraryName)) {
            continue;
          }
          pubSituationBean = new SiePubSituationBean();
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

  private List<PatMemberBean> buildMembers(PubJsonDTO pub) {
    List<PatMemberBean> list = new ArrayList<>();
    if (pub.members != null) {
      List<PubMemberDTO> memberList = pub.memberList;
      /* JacksonUtils.jsonToCollection(pub.members.toJSONString(), List.class, PatMemberDTO.class); */
      if (memberList != null && memberList.size() > 0) {
        PatMemberBean pubMemberBean = null;
        for (PubMemberDTO p : memberList) {
          pubMemberBean = new PatMemberBean();
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
          pubMemberBean.setFirstAuthor((seqNo != null && seqNo == 1));
          pubMemberBean.setInsName(p.getInsName());
          // pubMemberBean.setInsNames(buildInsNames());
          pubMemberBean.setInsId(p.getInsId());
          list.add(pubMemberBean);
        }
      }
    }
    return list;
  }

  @SuppressWarnings("unchecked")
  private List<MemberInsBean> buildInsNames() { // List<MemberInsDTO> insNames
    String insJson = JacksonUtils.jsonObjectSerializer(new ArrayList<MemberInsBean>());
    return JacksonUtils.jsonToCollection(insJson, List.class, MemberInsBean.class);
  }

  @SuppressWarnings("unchecked")
  private List<PubAttachmentsBean> buildPubAttachments(PubJsonDTO pub) {
    List<PubAttachmentsBean> list = new ArrayList<>();
    if (pub.pubAttachments != null) {
      List<PubAttachmentsDTO> memberList =
          JacksonUtils.jsonToCollection(pub.pubAttachments.toJSONString(), List.class, PubAttachmentsDTO.class);
      if (memberList != null && memberList.size() > 0) {
        PubAttachmentsBean pubAttachmentsBean = null;
        for (PubAttachmentsDTO p : memberList) {
          pubAttachmentsBean = new PubAttachmentsBean();
          Integer seqNo = p.getSeqNo();
          pubAttachmentsBean.setSeqNo(seqNo);
          pubAttachmentsBean.setFileId(p.getFileId());
          pubAttachmentsBean.setFileName(p.getFileName());
          pubAttachmentsBean.setFileSize(p.getFileSize());
          pubAttachmentsBean.setFileExt(p.getFileExt());
          pubAttachmentsBean.setUploadTime(p.getUploadTime());
          list.add(pubAttachmentsBean);
        }
      }
    }
    return list;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public PubDetailDOM getDOMByIdAndType(Long patId) throws ServiceException {
    PubDetailDOM pubDetail = null;
    try {
      PatJsonPO patJsonPO = patJsonPODao.get(patId);
      if (patJsonPO != null) {
        String json = patJsonPO.getPatJson();
        pubDetail = (PubDetailDOM) JacksonUtils.jsonObject(json, new TypeReference<PubDetailDOM<PatentInfoBean>>() {});
      }
      return pubDetail;
    } catch (Exception e) {
      logger.error("查询专利详情时出错！patId={}", patId);
      throw new ServiceException("查询专利情时出错！patId=" + patId, e);
    }
  }

}
