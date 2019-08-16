package com.smate.web.management.service.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.management.dao.analysis.sns.FriendDao;
import com.smate.web.management.dao.analysis.sns.PsnKnowCopartnerDao;
import com.smate.web.management.model.analysis.DegreeScore;
import com.smate.web.management.model.analysis.sns.RecommendScore;

/**
 * 基金、论文合作者推荐合作度：好友（推荐论文合作者）=1.
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCooperatorDegree")
@Transactional(rollbackFor = Exception.class)
public class PsnCooperatorDegree implements PsnCooperator {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private PsnKnowCopartnerDao psnKnowCopartnerDao;

  // 表：scholar2.psn_friend(psn_id,friend_psn_id),scholar2.psn_know_copartner(psn_id,copartner_id)
  @Override
  public void handler(RecommendScore rs) throws Exception {
    DegreeScore ds = rs.getDegreeScore();

    try {
      Long friendScore = friendDao.isFriend(rs.getPsnId(), rs.getCoPsnId());
      if (friendScore > 0) {
        ds.setFriendScore(friendScore.intValue());
      } else {// 如果已经是好友，合作者不必计算，合作度仅１分
        Long cooperatorScore = psnKnowCopartnerDao.isCopartner(rs.getPsnId(), rs.getCoPsnId());
        ds.setCooperatorScore(cooperatorScore.intValue());
      }

    } catch (Exception e) {
      logger.error("基金、论文合作者推荐合作度计算失败，psn_friend,psn_know_copartner:psnId={},coPsnId={}", rs.getPsnId(),
          rs.getCoPsnId(), e);
      throw new Exception(e);
    }

  }

}
