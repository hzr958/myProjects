package com.smate.center.batch.chain.pubassign.task;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignScoreDetail;
import com.smate.center.batch.model.rol.pub.PubAssignScoreMap;
import com.smate.center.batch.service.rol.pub.PubAssignSpsMatchService;

/**
 * ISI成果匹配-成果指派匹配用户名称前缀匹配任务，序号为1.
 * 
 * @author liqinghua
 * 
 */
public class PubAssignMatchSpsPrefixNameTask implements IPubAssignMatchTask {

  /**
   * 
   */
  private static final long serialVersionUID = 265987084444043027L;
  private final String name = "PubAssignMatchSpsPrefixNameTask";
  @Autowired
  private PubAssignSpsMatchService pubAssignSpsMatchService;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean can(PubAssginMatchContext context) {
    return context.isScopusImport();
  }

  @Override
  public boolean run(PubAssginMatchContext context) throws Exception {
    // 获取用户名称匹配上的用户列表
    List<Object[]> matchedPsns =
        pubAssignSpsMatchService.getSpsPrefixNameMatchPubAuthor(context.getPubId(), context.getInsId());
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
      String auname = (String) matchedPsn[3];
      Integer nameType = (Integer) matchedPsn[4];
      // 一个作者可能匹配上几个人，一个人可能匹配上几个作者
      PubAssignScoreMap scoreMap = pubAssignScoreMap.get(seqNo);
      if (scoreMap == null) {
        scoreMap = new PubAssignScoreMap(seqNo, context.getPubId(), context.getInsId(), auname, nameType);
        pubAssignScoreMap.put(seqNo, scoreMap);
      }
      // 匹配上的人员
      PubAssignScoreDetail scoreDetail = scoreMap.getDetailMap().get(psnId);
      if (scoreDetail == null) {
        scoreDetail = new PubAssignScoreDetail(psnId, context.getInsId(), context.getPubId(), seqNo);
        scoreMap.getDetailMap().put(psnId, scoreDetail);
      }
      // 是否是本机构，不是的话(作者的单位地址为空)，减分
      if (context.getInsId().equals(insId)) {
        scoreDetail.setInst(0f);
      } else {
        scoreDetail.setInst(context.getSettingPubAssignMatchScore().getNoMatchInst());
      }
    }
    return true;
  }

}
