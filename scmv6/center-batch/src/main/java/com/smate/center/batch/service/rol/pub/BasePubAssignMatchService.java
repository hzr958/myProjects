package com.smate.center.batch.service.rol.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.psn.PsnPmJournalRol;
import com.smate.center.batch.model.rol.pub.PsnPmConference;
import com.smate.center.batch.model.rol.pub.PsnPmKeyWordRol;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.center.batch.model.rol.pub.SettingPubAssignScoreWrap;

/**
 * 成果匹配超类.
 * 
 * @author liqinghua
 * 
 */
public abstract class BasePubAssignMatchService {

  /**
   * 构造按成果指派人员PubAssginMatchContext.
   * 
   * @param pub
   * @param insId
   * @return
   * @throws ServiceException
   */
  public PubAssginMatchContext buildPubAssginMatchContext(PublicationRol pub, Long insId,
      SettingPubAssignScoreWrap settingScore) throws ServiceException {

    PubAssginMatchContext context = new PubAssginMatchContext();
    context.setDbId(Long.valueOf(pub.getSourceDbId()));
    context.setInsId(insId);
    context.setPub(pub);
    context.setPubId(pub.getId());
    context.setSettingPubAssignMatchScore(settingScore);
    return context;
  }

  /**
   * 构造按人员指派成果PubAssginMatchContext.
   * 
   * @param psnId
   * @param insId
   * @return
   * @throws ServiceException
   */
  public PubAssginMatchContext buildPsnAssginMatchContext(Long psnId, Long insId, int type) throws ServiceException {

    PubAssginMatchContext context = new PubAssginMatchContext();
    context.setInsId(insId);
    context.setPsnId(psnId);
    context.setPsnMatchType(type);
    return context;
  }

  /**
   * 构造匹配上成果关键词的指定人员ID与关键词列表映射.
   * 
   * @param list
   * @return
   */
  public Map<Long, List<PsnPmKeyWordRol>> buildKwMatchPubKwMap(List<PsnPmKeyWordRol> list) {

    if (list == null || list.size() == 0) {
      return null;
    }
    Map<Long, List<PsnPmKeyWordRol>> result = new HashMap<Long, List<PsnPmKeyWordRol>>();
    for (PsnPmKeyWordRol kw : list) {
      Long psnId = kw.getPsnId();
      if (result.get(psnId) == null) {
        List<PsnPmKeyWordRol> pkwList = new ArrayList<PsnPmKeyWordRol>();
        pkwList.add(kw);
        result.put(psnId, pkwList);
      } else {
        result.get(psnId).add(kw);
      }
    }
    return result;
  }

  /**
   * 构造匹配上成果关键词的指定人员ID与关键词列表映射.
   * 
   * @param list
   * @return
   */
  public Map<Long, PsnPmJournalRol> buildPJMatchPubPJMap(List<PsnPmJournalRol> list) {

    if (list == null || list.size() == 0) {
      return null;
    }
    Map<Long, PsnPmJournalRol> result = new HashMap<Long, PsnPmJournalRol>();
    for (PsnPmJournalRol pj : list) {
      result.put(pj.getPsnId(), pj);
    }
    return result;
  }

  /**
   * 构造匹配上成果期刊的指定人员ID与期刊列表映射.
   * 
   * @param list
   * @return
   * @throws ServiceException
   */
  public Map<Long, PsnPmConference> buildPCMatchPubPcMap(List<PsnPmConference> list) throws ServiceException {

    if (list == null || list.size() == 0) {
      return null;
    }
    Map<Long, PsnPmConference> result = new HashMap<Long, PsnPmConference>();
    for (PsnPmConference pc : list) {
      result.put(pc.getPsnId(), pc);
    }
    return result;
  }

  /**
   * 构造匹配上成果作者的指定用户的合作者个数映射.
   * 
   * @param list
   * @return
   */
  public Map<Long, Long> buildIsiCoMatchPubAuthMap(List<Object[]> list) {

    if (list == null || list.size() == 0) {
      return null;
    }
    Map<Long, Long> result = new HashMap<Long, Long>();
    for (Object[] objs : list) {
      Long psnId = (Long) objs[0];
      Long count = (Long) objs[1];
      result.put(psnId, count);
    }
    return result;
  }
}
