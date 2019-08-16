package com.smate.center.batch.chain.pubassign.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignScoreDetail;
import com.smate.center.batch.model.rol.pub.PubAssignScoreMap;
import com.smate.center.batch.model.rol.pub.SettingPubAssignScoreWrap;
import com.smate.center.batch.service.rol.pub.PubAssignCnkiMatchService;

/**
 * cnki成果匹配-人员匹配成果用户名称匹配.
 * 
 * @author liqinghua
 * 
 */
public class PsnAssignMatchCnkiAuthorTask implements IPubAssignMatchTask {

  /**
   * 
   */
  private static final long serialVersionUID = -1139032426634827957L;

  private final String name = "PsnAssignMatchCnkiAuthorTask";
  @Autowired
  private PubAssignCnkiMatchService pubAssignCnkiMatchService;

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
        pubAssignCnkiMatchService.getCnkiPubAuthorMatchPsn(context.getPsnId(), context.getInsId());
    // 如果未匹配到一条成果，直接返回
    if (CollectionUtils.isEmpty(matchedPubs)) {
      return false;
    }
    // 存储成果ID
    Map<Long, Map<Integer, PubAssignScoreMap>> psnAssignScoreMap = context.getPsnAssignScoreMap();
    for (Object[] matchedPub : matchedPubs) {
      Long pubId = (Long) matchedPub[0];
      Long dbId = Long.valueOf((Integer) matchedPub[1]);
      Integer seqNo = (Integer) matchedPub[2];
      Long insId = (Long) matchedPub[3];
      Map<Integer, PubAssignScoreMap> pubAssignScoreMap = psnAssignScoreMap.get(pubId);
      if (pubAssignScoreMap == null) {
        pubAssignScoreMap = new HashMap<Integer, PubAssignScoreMap>();
        psnAssignScoreMap.put(pubId, pubAssignScoreMap);
      }
      PubAssignScoreMap scoreMap = pubAssignScoreMap.get(seqNo);
      if (scoreMap == null) {
        scoreMap = new PubAssignScoreMap(seqNo, pubId, context.getInsId());
        pubAssignScoreMap.put(seqNo, scoreMap);
      }
      PubAssignScoreDetail detail = new PubAssignScoreDetail(context.getPsnId(), context.getInsId(), pubId, seqNo);
      // 用户名称分数
      detail.setName(getSettingPubAssignScore(dbId).getName());
      // 是否是本机构，如果不是，则减分
      if (!context.getInsId().equals(insId)) {
        detail.setInst(getSettingPubAssignScore(Long.valueOf(dbId)).getNoMatchInst());
      } else if (seqNo != null) {
        detail.setInst(0F);
      }
      scoreMap.getDetailMap().put(context.getPsnId(), detail);
    }
    return true;
  }

  /**
   * 获取分数配置.
   * 
   * @param dbId
   * @return
   * @throws ServiceException
   */
  public SettingPubAssignScoreWrap getSettingPubAssignScore(Long dbId) throws ServiceException {

    SettingPubAssignScoreWrap setting = this.pubAssignCnkiMatchService.getSettingPubAssignScore(dbId);
    if (setting == null) {
      throw new ServiceException("请设置DBID=" + dbId + "的SETTING_PUBASSIGN_CNKISCORE数据");
    }
    return setting;
  }

}
