package com.smate.center.batch.chain.pubassign.task;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignScoreDetail;
import com.smate.center.batch.model.rol.pub.PubAssignScoreMap;
import com.smate.center.batch.service.rol.pub.PubAssignEiMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignPubMedMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignSpsMatchService;

/**
 * ISI成果匹配-成果指派匹配用户名称任务，序号为2.
 * 
 * @author liqinghua
 * 
 */
public class PubAssignMatchEmailTask implements IPubAssignMatchTask {

  /**
   * 
   */
  private static final long serialVersionUID = 8482519230286513277L;
  private final String name = "PubAssignMatchEmailTask";
  @Autowired
  private PubAssignMatchService pubAssignMatchService;
  @Autowired
  private PubAssignSpsMatchService pubAssignSpsMatchService;
  @Autowired
  private PubAssignPubMedMatchService pubAssignPubMedMatchService;
  @Autowired
  private PubAssignEiMatchService pubAssignEiMatchService;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean can(PubAssginMatchContext context) {
    return context.isIsiImport() || context.isScopusImport() || context.isPubMedImport() || context.isEiImport();
  }

  @Override
  public boolean run(PubAssginMatchContext context) throws Exception {

    List<Object[]> matchedPsns = getMatchedPsns(context);

    // 如果未匹配到一个用户，直接返回
    if (CollectionUtils.isEmpty(matchedPsns)) {
      return true;
    }
    // 存储匹配结果
    Map<Integer, PubAssignScoreMap> pubAssignScoreMap = context.getPubAssignScoreMap();
    for (Object[] matchedPsn : matchedPsns) {
      Integer seqNo = (Integer) matchedPsn[0];
      Long psnId = (Long) matchedPsn[1];
      Long insId = (Long) matchedPsn[2];

      // 有些成果作者EMAIL的seqno为空
      if (seqNo == null) {
        PubAssignScoreDetail psnScoreDetail = createEmailScoreDetail(context, psnId, insId, seqNo);
        context.getNullSeqScoreMap().put(psnId, psnScoreDetail);
      } else {
        PubAssignScoreMap scoreMap = pubAssignScoreMap.get(seqNo);
        if (scoreMap == null) {
          scoreMap = new PubAssignScoreMap(seqNo, context.getPubId(), context.getInsId());
          pubAssignScoreMap.put(seqNo, scoreMap);
        }
        // 记录seqno的email分数
        PubAssignScoreDetail scoreDetail = scoreMap.getDetailMap().get(psnId);
        if (scoreDetail == null) {
          scoreDetail = createEmailScoreDetail(context, psnId, insId, seqNo);
          scoreMap.getDetailMap().put(psnId, scoreDetail);
        } else {
          scoreDetail.setEmail(context.getSettingPubAssignMatchScore().getEmail());
        }
      }
    }

    return true;
  }

  /**
   * 获取匹配上的人员列表.
   * 
   * @param context
   * @return
   * @throws ServiceException
   */
  private List<Object[]> getMatchedPsns(PubAssginMatchContext context) throws ServiceException {
    // 获取用户email匹配上的用户列表
    List<Object[]> matchedPsns = null;
    if (context.isIsiImport()) {
      matchedPsns = pubAssignMatchService.getEmailMatchPubEmail(context.getPubId(), context.getInsId());
    } else if (context.isScopusImport()) {
      matchedPsns = pubAssignSpsMatchService.getEmailMatchPubEmail(context.getPubId(), context.getInsId());
    } else if (context.isPubMedImport()) {
      matchedPsns = pubAssignPubMedMatchService.getEmailMatchPubEmail(context.getPubId(), context.getInsId());
    } else if (context.isEiImport()) {
      matchedPsns = pubAssignEiMatchService.getEmailMatchPubEmail(context.getPubId(), context.getInsId());
    }
    return matchedPsns;
  }

  /**
   * 创建EMAIL匹配上的scoredetail.
   * 
   * @param context
   * @param matchPsnId
   * @param matchInsId
   * @return
   */
  private PubAssignScoreDetail createEmailScoreDetail(PubAssginMatchContext context, Long matchPsnId, Long matchInsId,
      Integer seqNo) {
    PubAssignScoreDetail psnScoreDetail = new PubAssignScoreDetail(matchPsnId, context.getInsId(), context.getPubId(),
        seqNo, context.getSettingPubAssignMatchScore().getEmail());
    // 是否是本机构，不是的话，减分
    if (context.getInsId().equals(matchInsId)) {
      psnScoreDetail.setInst(0f);
    } else {
      psnScoreDetail.setInst(context.getSettingPubAssignMatchScore().getNoMatchInst());
    }
    return psnScoreDetail;
  }
}
