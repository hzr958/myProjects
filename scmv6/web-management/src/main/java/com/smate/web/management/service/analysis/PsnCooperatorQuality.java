package com.smate.web.management.service.analysis;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.web.management.model.analysis.sns.RecommendScore;

/**
 * 基金、论文合作者推荐质量: H-index.
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCooperatorQuality")
@Transactional(rollbackFor = Exception.class)
public class PsnCooperatorQuality implements PsnCooperator {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  // 基金合作者推荐合作度
  @Resource(name = "psnCooperatorDegree")
  private PsnCooperator next;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;

  // 表：scholar2.psn_statistics(psn_id,hindex)
  @Override
  public void handler(RecommendScore rs) throws Exception {
    try {
      // 人员信息统计表
      PsnStatistics ps = psnStatisticsDao.get(rs.getCoPsnId());
      if (ps != null) {
        rs.setQualityScore(ps.getHindex());
      }
    } catch (Exception e) {
      logger.error("基金、论文合作者推荐质量计算失败，psn_statistics:psnId={}", rs.getPsnId(), e);
      throw new Exception(e);
    }
    next.handler(rs);
  }
}
