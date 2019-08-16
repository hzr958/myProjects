package com.smate.center.batch.chain.pubassign.task;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignScoreDetail;
import com.smate.center.batch.model.rol.pub.PubAssignScoreMap;
import com.smate.center.batch.service.rol.pub.PubAssignCniprMatchService;

/**
 * Cnipr成果匹配机构用户名.
 * 
 * @author liqinghua
 * 
 */
public class PubAssignMatchCniprAuthorTask implements IPubAssignMatchTask {

  /**
   * 
   */
  private static final long serialVersionUID = 3539525923266785260L;
  private final String name = "PubAssignMatchCniprAuthorTask";
  @Autowired
  private PubAssignCniprMatchService pubAssignCniprMatchService;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean can(PubAssginMatchContext context) {
    return true;
  }

  /**
   * <pre>
   * 一、用户名匹配包括两部分：
   * 1、用户自己填写的中文名，如果匹配上需要加分.
   * 2、用户确认的作者名，如果匹配上，可以作为已经匹配上该人员，但是不加分.
   * 
   * 二、匹配上的作者机构ID是当前结构ID，加分.
   * </pre>
   */
  @Override
  public boolean run(PubAssginMatchContext context) throws Exception {

    // 获取用户名称匹配上的用户列表
    List<Object[]> matchedPsns =
        pubAssignCniprMatchService.getCniprNameMatchPubAuthor(context.getPubId(), context.getInsId());
    // 如果未匹配到一个用户，直接返回，匹配终止
    if (matchedPsns == null || matchedPsns.size() == 0) {
      return false;
    }

    // 存储匹配结果
    Map<Integer, PubAssignScoreMap> pubAssignScoreMap = context.getPubAssignScoreMap();
    for (Object[] matchedPsn : matchedPsns) {
      Long psnId = (Long) matchedPsn[0];
      Integer seqNo = (Integer) matchedPsn[1];
      Long insId = (Long) matchedPsn[2];
      PubAssignScoreMap scoreMap = pubAssignScoreMap.get(seqNo);
      if (scoreMap == null) {
        scoreMap = new PubAssignScoreMap(seqNo, context.getPubId(), context.getInsId());
        pubAssignScoreMap.put(seqNo, scoreMap);
      }
      PubAssignScoreDetail scoreDetail = scoreMap.getDetailMap().get(psnId);
      if (scoreDetail == null) {
        scoreDetail = new PubAssignScoreDetail(psnId, context.getInsId(), context.getPubId(), seqNo);
        scoreMap.getDetailMap().put(psnId, scoreDetail);
      }
      scoreDetail.setName(context.getSettingPubAssignMatchScore().getName());
      // 是否是本机构，不是的话，减分
      if (context.getInsId().equals(insId)) {
        scoreDetail.setInst(0f);
      } else {
        scoreDetail.setInst(context.getSettingPubAssignMatchScore().getNoMatchInst());
      }
    }
    return true;
  }

}
