package com.smate.center.batch.chain.pubassign.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignScoreDetail;
import com.smate.center.batch.model.rol.pub.PubAssignScoreMap;
import com.smate.center.batch.model.rol.pub.SettingPubAssignScoreWrap;
import com.smate.center.batch.service.rol.pub.PubAssignMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignPubMedMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignSpsMatchService;
import com.smate.center.batch.util.pub.PubXmlDbUtils;

/**
 * 成果匹配-人员匹配成果用户email匹配.
 * 
 * @author liqinghua
 * 
 */
public class PsnAssignMatchEmailTask implements IPubAssignMatchTask {

  /**
   * 
   */
  private static final long serialVersionUID = -7961678761594714581L;

  private final String name = "PsnAssignMatchEmailTask";
  @Autowired
  private PubAssignMatchService pubAssignMatchService;
  @Autowired
  private PubAssignSpsMatchService pubAssignSpsMatchService;
  @Autowired
  private PubAssignPubMedMatchService pubAssignPubMedMatchService;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean can(PubAssginMatchContext context) {
    // 必须名称能够匹配上
    return MapUtils.isNotEmpty(context.getPsnAssignScoreMap());
  }

  @Override
  public boolean run(PubAssginMatchContext context) throws Exception {

    // 获取用户email匹配上的成果列表
    List<Object[]> pubDetailList = getMatchEmailDetails(context);

    // 如果未匹配到一条成果，直接返回
    if (pubDetailList == null || pubDetailList.size() == 0) {
      return true;
    }
    // 存储成果ID
    Map<Long, Map<Integer, PubAssignScoreMap>> psnAssignScoreMap = context.getPsnAssignScoreMap();
    for (Object[] pubDetail : pubDetailList) {
      Long pubId = (Long) pubDetail[0];
      Integer dbId = (Integer) pubDetail[1];
      Integer seqNo = (Integer) pubDetail[2];
      Long insId = (Long) pubDetail[3];
      Map<Integer, PubAssignScoreMap> pubAssignScoreMap = psnAssignScoreMap.get(pubId);
      if (pubAssignScoreMap == null) {
        pubAssignScoreMap = new HashMap<Integer, PubAssignScoreMap>();
        psnAssignScoreMap.put(pubId, pubAssignScoreMap);
      }
      if (seqNo == null) {
        // 创建
        PubAssignScoreDetail detail = createEmailScoreDetail(context, insId, pubId, seqNo, new Long(dbId));
        PubAssignScoreMap map = new PubAssignScoreMap(null, pubId, context.getInsId());
        map.getDetailMap().put(context.getPsnId(), detail);
        // 为空的记录seq设置为-1
        pubAssignScoreMap.put(-1, map);
      } else {
        PubAssignScoreMap map = pubAssignScoreMap.get(seqNo);
        if (map == null) {
          map = new PubAssignScoreMap(seqNo, pubId, context.getInsId());
          pubAssignScoreMap.put(seqNo, map);
        }
        PubAssignScoreDetail detail = map.getDetailMap().get(context.getPsnId());
        if (detail == null) {
          detail = createEmailScoreDetail(context, insId, pubId, seqNo, new Long(dbId));
          map.getDetailMap().put(context.getPsnId(), detail);
        } else {
          // email加分
          detail.setEmail(getSettingPubAssignScore(new Long(dbId)).getEmail());
        }
      }
    }
    return true;
  }

  private List<Object[]> getMatchEmailDetails(PubAssginMatchContext context) throws ServiceException {
    List<Object[]> pubDetailList = null;
    if (context.getPsnMatchType() == PsnMatchTypeConstants.ISI) {
      pubDetailList = pubAssignMatchService.getPsnEmailMatchIsiPubEmail(context.getPsnId(), context.getInsId());
    } else if (context.getPsnMatchType() == PsnMatchTypeConstants.SCOPUS) {
      pubDetailList = pubAssignSpsMatchService.getPsnEmailMatchSpsPubEmail(context.getPsnId(), context.getInsId());
    } else if (context.getPsnMatchType() == PsnMatchTypeConstants.PUBMED) {
      pubDetailList =
          pubAssignPubMedMatchService.getPsnEmailMatchPubMedPubEmail(context.getPsnId(), context.getInsId());
    }
    return pubDetailList;
  }

  /**
   * 创建EMAIL匹配上的scoredetail.
   * 
   * @param context
   * @param matchPsnId
   * @param matchInsId
   * @return
   */
  private PubAssignScoreDetail createEmailScoreDetail(PubAssginMatchContext context, Long matchInsId, Long pubId,
      Integer seqNo, Long dbId) throws ServiceException {
    SettingPubAssignScoreWrap setting = getSettingPubAssignScore(dbId);
    PubAssignScoreDetail psnScoreDetail =
        new PubAssignScoreDetail(context.getPsnId(), context.getInsId(), pubId, seqNo, setting.getEmail());
    // 是否是本机构，不是的话，减分
    if (context.getInsId().equals(matchInsId)) {
      psnScoreDetail.setInst(0f);
    } else {
      psnScoreDetail.setInst(setting.getNoMatchInst());
    }
    return psnScoreDetail;
  }

  public SettingPubAssignScoreWrap getSettingPubAssignScore(Long dbId) throws ServiceException {

    SettingPubAssignScoreWrap setting = null;
    if (PubXmlDbUtils.isIsiDb(dbId)) {
      setting = this.pubAssignMatchService.getSettingPubAssignScore(dbId);
    } else if (PubXmlDbUtils.isScopusDb(dbId)) {
      setting = this.pubAssignSpsMatchService.getSettingPubAssignScore(dbId);
    } else if (PubXmlDbUtils.isPubMedDb(dbId)) {
      setting = this.pubAssignPubMedMatchService.getSettingPubAssignScore(dbId);
    }
    if (setting == null) {
      throw new ServiceException("请设置DBID=" + dbId + "的SETTING_PUBASSIGN_SCORE数据");
    }
    return setting;
  }

}
