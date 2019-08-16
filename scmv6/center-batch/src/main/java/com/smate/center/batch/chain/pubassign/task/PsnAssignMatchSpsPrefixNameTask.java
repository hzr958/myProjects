package com.smate.center.batch.chain.pubassign.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignScoreDetail;
import com.smate.center.batch.model.rol.pub.PubAssignScoreMap;
import com.smate.center.batch.model.rol.pub.SettingPubAssignScoreWrap;
import com.smate.center.batch.service.rol.pub.PubAssignSpsMatchService;

/**
 * scopus成果匹配-成果指派匹配用户名称前缀匹配任务，序号为1.
 * 
 * @author liqinghua
 * 
 */
public class PsnAssignMatchSpsPrefixNameTask implements IPubAssignMatchTask {

  /**
   * 
   */
  private static final long serialVersionUID = -7414028362402183667L;
  private final String name = "PsnAssignMatchSpsPrefixNameTask";
  @Autowired
  private PubAssignSpsMatchService pubAssignSpsMatchService;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean can(PubAssginMatchContext context) {
    return true;
  }

  @Override
  public boolean run(PubAssginMatchContext context) throws Exception {
    // 获取用户名称匹配上的成果列表
    List<Object[]> matchedPubs =
        pubAssignSpsMatchService.getSpsPubAuthorMatchPsnPrefixName(context.getPsnId(), context.getInsId());
    // 如果未匹配到一条成果，直接返回
    if (matchedPubs == null || matchedPubs.size() == 0) {
      return true;
    }
    // 存储成果ID
    Map<Long, Map<Integer, PubAssignScoreMap>> psnAssignScoreMap = context.getPsnAssignScoreMap();
    for (Object[] matchedPub : matchedPubs) {
      Long pubId = (Long) matchedPub[0];
      Integer dbId = (Integer) matchedPub[1];
      Integer seqNo = (Integer) matchedPub[2];
      Long authorInsId = (Long) matchedPub[3];
      String auname = (String) matchedPub[4];
      Integer nametype = (Integer) matchedPub[5];

      Map<Integer, PubAssignScoreMap> pubAssignScoreMap = psnAssignScoreMap.get(pubId);
      if (pubAssignScoreMap == null) {
        pubAssignScoreMap = new HashMap<Integer, PubAssignScoreMap>();
        psnAssignScoreMap.put(pubId, pubAssignScoreMap);
      }
      PubAssignScoreMap scoreMap = pubAssignScoreMap.get(seqNo);
      if (scoreMap == null) {
        scoreMap = new PubAssignScoreMap(seqNo, pubId, context.getInsId(), auname, nametype);
        pubAssignScoreMap.put(seqNo, scoreMap);
      }
      PubAssignScoreDetail detail = new PubAssignScoreDetail(context.getPsnId(), context.getInsId(), pubId, seqNo);
      // 是否是本机构，如果不是，则减分
      if (!context.getInsId().equals(authorInsId)) {
        detail.setInst(getSettingPubAssignScore(Long.valueOf(dbId)).getNoMatchInst());
      } else if (seqNo != null) {
        detail.setInst(0F);
      }
      scoreMap.getDetailMap().put(context.getPsnId(), detail);
    }
    context.setPsnAssignScoreMap(psnAssignScoreMap);
    return true;
  }

  public SettingPubAssignScoreWrap getSettingPubAssignScore(Long dbId) throws ServiceException {

    SettingPubAssignScoreWrap setting = this.pubAssignSpsMatchService.getSettingPubAssignScore(dbId);
    if (setting == null) {
      throw new ServiceException("请设置DBID=" + dbId + "的SETTING_PUBASSIGN_SCORE数据");
    }
    return setting;
  }

}
