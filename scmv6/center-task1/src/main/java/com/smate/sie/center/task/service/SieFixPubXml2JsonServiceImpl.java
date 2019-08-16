package com.smate.sie.center.task.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.sie.center.task.dao.SieDataPubXml2JsonRefreshDao;
import com.smate.sie.center.task.model.SieDataPubXml2JsonRefresh;
import com.smate.sie.core.base.utils.dao.pub.PatJsonPODao;
import com.smate.sie.core.base.utils.dao.pub.PubJsonPODao;
import com.smate.sie.core.base.utils.dao.pub.PubXmlDao;
import com.smate.sie.core.base.utils.dao.pub.SiePatXmlDao;
import com.smate.sie.core.base.utils.model.pub.PatJsonPO;
import com.smate.sie.core.base.utils.model.pub.PubJsonPO;
import com.smate.sie.core.base.utils.model.pub.SiePatXml;
import com.smate.sie.core.base.utils.model.pub.SiePubXml;
import com.smate.sie.core.base.utils.pub.dom.AwardsInfoBean;
import com.smate.sie.core.base.utils.pub.dom.BookInfoBean;
import com.smate.sie.core.base.utils.pub.dom.ConferencePaperBean;
import com.smate.sie.core.base.utils.pub.dom.JournalInfoBean;
import com.smate.sie.core.base.utils.pub.dom.MemberInsBean;
import com.smate.sie.core.base.utils.pub.dom.PatMemberBean;
import com.smate.sie.core.base.utils.pub.dom.PatentInfoBean;
import com.smate.sie.core.base.utils.pub.dom.PubAttachmentsBean;
import com.smate.sie.core.base.utils.pub.dom.PubDetailDOM;
import com.smate.sie.core.base.utils.pub.dom.PubMemberBean;
import com.smate.sie.core.base.utils.pub.dom.PubTypeInfoBean;
import com.smate.sie.core.base.utils.pub.dom.SiePubSituationBean;
import com.smate.sie.core.base.utils.pub.dom.ThesisInfoBean;
import com.smate.sie.core.base.utils.pub.dto.MemberInsDTO;
import com.smate.sie.core.base.utils.pub.dto.PatMemberDTO;
import com.smate.sie.core.base.utils.pub.dto.PubAttachmentsDTO;
import com.smate.sie.core.base.utils.pub.dto.PubMemberDTO;
import com.smate.sie.core.base.utils.pub.dto.PubSituationDTO;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

/***
 * 成果专利xml转json服务实现层
 * 
 * @author lijianming
 * @Date 20190325
 */
@Service("sieFixPubXml2JsonService")
@Transactional(rollbackFor = Exception.class)
public class SieFixPubXml2JsonServiceImpl implements SieFixPubXml2JsonService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SieDataPubXml2JsonRefreshDao sieDataPubXml2JsonRefreshDao;
  @Autowired
  private PubXmlDao pubXmlDao;
  @Autowired
  private PubJsonPODao pubJsonPODao;
  @Autowired
  private PatJsonPODao patJsonPODao;
  @Autowired
  private SiePatXmlDao siePatXmlDao;

  @Override
  public List<SieDataPubXml2JsonRefresh> getNeedRefreshData(int size) {
    try {
      return this.sieDataPubXml2JsonRefreshDao.queryNeedRefresh(size);
    } catch (Exception e) {
      logger.error("获取需要转化为Json数据的成果或专利失败", e);
      throw new ServiceException("", e);
    }
  }

  @Override
  public void saveHandleFailRefresh(SieDataPubXml2JsonRefresh refresh) {
    refresh.setStatus(9);
    sieDataPubXml2JsonRefreshDao.save(refresh);
  }

  @Override
  public SiePubXml checkPubXmlAndJsonIsExist(Long pubId) throws Exception {
    try {
      // 检查pubId的pub_xml数据是否存在
      SiePubXml pubXml = pubXmlDao.getXmlByPubId(pubId);
      if (pubXml != null) {
        PubJsonPO pubJson = pubJsonPODao.get(pubId);
        if (pubJson == null) { // 若没有pubJson数据，则返回pubXml数据做进一步转化
          return pubXml;
        } else {
          return null;
        }
      } else {
        return null;
      }
    } catch (Exception e) {
      logger.error("查询成果的xml与json数据失败", e);
      throw new ServiceException("", e);
    }
  }

  @Override
  public SiePatXml checkPatXmlAndJsonIsExist(Long pubId) throws Exception {
    try {
      // 检查pubId的pub_xml数据是否存在
      SiePatXml patXml = siePatXmlDao.getXmlByPatId(pubId);
      if (patXml != null) {
        PatJsonPO patJsonPo = patJsonPODao.get(pubId);
        if (patJsonPo == null) {
          return patXml;
        } else {
          return null;
        }
      }
    } catch (Exception e) {
      logger.error("查询专利的xml与json数据失败", e);
      throw new ServiceException("", e);
    }
    return null;
  }

  @Override
  public void savePubXml2JsonRefresh(SieDataPubXml2JsonRefresh pubXml2JsonRefresh) throws Exception {
    try {
      sieDataPubXml2JsonRefreshDao.save(pubXml2JsonRefresh);
    } catch (Exception e) {
      logger.error("成果xml数据转json数据任务：根据处理结果更新DATA_PUBXML_TO_JSON_REFRESH表状态出错。pubId: " + pubXml2JsonRefresh.getPubId()
          + ",status: " + pubXml2JsonRefresh.getStatus(), e);
      throw new Exception(e);
    }
  }

  @Override
  public void savePubJsonData(PubJsonDTO pubJson, Long pubId) throws Exception {
    try {
      PubDetailDOM detail = pubDetailHandle(pubJson);
      PubJsonPO pubJsonPo = new PubJsonPO();
      if (pubJson.pubId != null) {
        pubJsonPo.setPubId(pubJson.pubId);
      } else {
        pubJsonPo.setPubId(pubId);
      }
      pubJsonPo.setPubJson(JacksonUtils.jsonObjectSerializer(detail));
      pubJsonPODao.save(pubJsonPo);
    } catch (Exception e) {
      logger.error("成果xml数据转json数据任务：保存json数据到pub_Json表出错。pubId: " + pubJson.pubId, e);
      throw new Exception(e);
    }
  }

  @SuppressWarnings("rawtypes")
  @Override
  public void savePatJsonData(PubJsonDTO patJson, Long pubId) throws Exception {
    try {
      PubDetailDOM detail = pubDetailHandle(patJson);
      PatJsonPO patJsonPo = new PatJsonPO();
      if (patJson.pubId != null) {
        patJsonPo.setPatId(patJson.pubId);
      } else {
        patJsonPo.setPatId(pubId);
      }
      patJsonPo.setPatJson(JacksonUtils.jsonObjectSerializer(detail));
      patJsonPODao.save(patJsonPo);
    } catch (Exception e) {
      logger.error("专利xml数据转json数据任务：保存json数据到pat_Json表出错。pubId: " + patJson.pubId, e);
      throw new Exception(e);
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
    pubDetail.setCitations((pub.citations == 0) ? "" : pub.citations.toString());
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
    if (pub.pubTypeCode != 5) {
      pubDetail.setMembers(buildMembers(pub));
    } else {
      pubDetail.setMembers(buildPatMembers(pub));
    }
    pubDetail.setPubAttachments(buildPubAttachments(pub));// []
    pubDetail.setPubTypeCode(pub.pubTypeCode);
    pubDetail.setPubTypeName(pub.pubTypeName);
    PubTypeInfoBean infoBean = transformInfoBean(pub);
    pubDetail.setTypeInfo(infoBean);// pub.pubTypeInfoBean
    pubDetail.setFulltextId(pub.fulltextId);
    pubDetail.setFulltexName(pub.fulltextName);
    pubDetail.setSrcFulltextUrl(pub.srcFulltextUrl);
    pubDetail.setSrcDbId(pub.srcDbId);
    pubDetail.setSourceId(pub.sourceId);
    pubDetail.setSourceUrl(pub.sourceUrl);
    pubDetail.setPublishDate(pub.publishDate);
    pubDetail.setDataFrom(pub.dataFrom);
    pubDetail.setCitedUrl(pub.citedUrl);
    pubDetail.setUpdateTime(pub.lastUpdateTime); // DateUtils.dateToStr2(new Date())
    pubDetail.setHCP(pub.HCP == 1);
    pubDetail.setHP(pub.HP == 1);
    pubDetail.setOA(pub.OA);
    return pubDetail;
  }

  private PubTypeInfoBean transformInfoBean(PubJsonDTO pub) {
    Integer pubType = pub.pubTypeCode;
    if (pub != null) {
      switch (pubType) {
        case 1: // 奖励
          AwardsInfoBean awards = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), AwardsInfoBean.class);
          return awards;
        case 2:// 书籍
          BookInfoBean bookInfoBean = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), BookInfoBean.class);
          return bookInfoBean;
        case 3:// 会议论文
          ConferencePaperBean conferencePaperBean =
              JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), ConferencePaperBean.class);
          return conferencePaperBean;
        case 4:// 期刊论文
          JournalInfoBean journal = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), JournalInfoBean.class);
          return journal;
        case 5:// 专利
          PatentInfoBean patBean = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), PatentInfoBean.class);
          return patBean;
        case 7:// 其他
          /*
           * OtherInfoBean otherInfoBean = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(),
           * OtherInfoBean.class);
           */
          break;
        case 8:// 学位论文
          ThesisInfoBean thesisInfoBean = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), ThesisInfoBean.class);
          return thesisInfoBean;
        case 10:// 书籍章节
          BookInfoBean bookInfoBean2 = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), BookInfoBean.class);
          return bookInfoBean2;
      }
    }
    return null;
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
          pubMemberBean.setFirstAuthor((seqNo != null && seqNo == 1));
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
  private List<PatMemberBean> buildPatMembers(PubJsonDTO pub) {
    List<PatMemberBean> list = new ArrayList<>();
    if (pub.members != null) {
      List<PatMemberDTO> memberList =
          JacksonUtils.jsonToCollection(pub.members.toJSONString(), List.class, PatMemberDTO.class);
      if (memberList != null && memberList.size() > 0) {
        PatMemberBean patMemberBean = null;
        for (PatMemberDTO p : memberList) {
          patMemberBean = new PatMemberBean();
          String name = p.getName();
          if (StringUtils.isBlank(name)) {
            continue;
          }
          Integer seqNo = p.getSeqNo();
          patMemberBean.setSeqNo(seqNo);
          patMemberBean.setPmId(p.getPmId());
          patMemberBean.setName(name);
          patMemberBean.setPsnId(p.getPsnId());
          patMemberBean.setCommunicable(p.isCommunicable());
          patMemberBean.setEmail(p.getEmail());
          patMemberBean.setFirstAuthor((seqNo != null && seqNo == 1));
          patMemberBean.setInsName(p.getInsName());
          patMemberBean.setInsId(p.getInsId());
          list.add(patMemberBean);
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
}
