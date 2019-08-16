package com.smate.web.v8pub.service.handler.assembly.pdwhpubsave;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.dom.MemberInsBean;
import com.smate.web.v8pub.dom.PubMemberBean;
import com.smate.web.v8pub.dom.PubSituationBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.dto.MemberInsDTO;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PubPdwhDetailService;

/**
 * 基准库成果详情
 * 
 * @author YJ
 *
 *         2018年7月26日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPdwhDetailSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPdwhDetailService pubPdwhDetailService;

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
    pubPdwhDetailService.saveOrUpdate(build(pub));
    return null;
  }

  @SuppressWarnings("unchecked")
  private PubPdwhDetailDOM build(PubDTO pub) {
    PubPdwhDetailDOM pdwhDetail = null;
    try {
      pdwhDetail = pubPdwhDetailService.get(pub.pubId);
      if (pdwhDetail == null) {
        pdwhDetail = new PubPdwhDetailDOM();
        pdwhDetail.setPubId(pub.pubId);
      }
      pdwhDetail.setAuthorNames(pub.authorNames);
      pdwhDetail.setBriefDesc(pub.briefDesc);
      pdwhDetail.setCountryId(pub.countryId);
      pdwhDetail.setDoi(pub.doi);
      pdwhDetail.setCitedUrl(pub.citedUrl);
      pdwhDetail.setSourceUrl(pub.sourceUrl);
      pdwhDetail.setFulltextId(pub.fulltextId);
      pdwhDetail.setFundInfo(pub.fundInfo);
      pdwhDetail.setKeywords(pub.keywords);
      pdwhDetail.setOrganization(pub.organization);
      pdwhDetail.setSrcFulltextUrl(pub.srcFulltextUrl);
      pdwhDetail.setSummary(pub.summary);
      Integer citations = PubParamUtils.resetCitation(pub.srcDbId, pub.citations);
      // 系统引用次数大则更新为系统的
      citations = PubParamUtils.maxCitations(pdwhDetail.getCitations(), citations);
      pdwhDetail.setCitations(citations);
      pdwhDetail.setTitle(pub.title);
      pdwhDetail.setInsId(pub.insId);
      pdwhDetail.setSrcDbId(pub.srcDbId);
      pdwhDetail.setSourceId(pub.sourceId);
      pdwhDetail.setPubType(pub.pubType);
      pdwhDetail.setPublishDate(pub.publishDate);
      pdwhDetail.setTypeInfo(pub.pubTypeInfoBean);
      pdwhDetail.setMembers(buildMembers(pub));
      pdwhDetail.setSituations(buildSituations(pub));
      pdwhDetail.setHCP(pub.HCP == 1);
      pdwhDetail.setHP(pub.HP == 1);
      pdwhDetail.setOA(pub.OA);
      return pdwhDetail;
    } catch (Exception e) {
      logger.error("基准库成果详情服务：保存成果详情出错！pdwhDetail={}", pdwhDetail, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "基准库保存成果详情出错！", e);
    }

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
          pubSituationBean = new PubSituationBean();
          pubSituationBean.setSrcId(p.getSrcId());
          pubSituationBean.setSrcDbId(p.getSrcDbId());
          pubSituationBean.setSitStatus(p.isSitStatus());
          pubSituationBean.setLibraryName(p.getLibraryName());
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
    if (pub.members != null && pub.members.size() > 0) {
      List<PubMemberDTO> memberList =
          JacksonUtils.jsonToCollection(pub.members.toJSONString(), List.class, PubMemberDTO.class);
      if (memberList != null && memberList.size() > 0) {
        PubMemberBean pubMemberBean = null;
        for (PubMemberDTO p : memberList) {
          pubMemberBean = new PubMemberBean();
          pubMemberBean.setPsnId(p.getPsnId());
          pubMemberBean.setCommunicable(p.isCommunicable());
          pubMemberBean.setEmail(p.getEmail());
          pubMemberBean.setFirstAuthor(p.isFirstAuthor());
          pubMemberBean.setInsNames(buildInsNames(p.getInsNames()));
          pubMemberBean.setName(p.getName());
          pubMemberBean.setSeqNo(p.getSeqNo());
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

}
