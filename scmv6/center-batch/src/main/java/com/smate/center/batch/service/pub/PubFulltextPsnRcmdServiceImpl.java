package com.smate.center.batch.service.pub;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.prj.PubFulltextPsnRcmdDao;
import com.smate.center.batch.model.sns.prj.PubFulltextPsnRcmd;
import com.smate.center.batch.service.psn.PsnStatisticsService;
import com.smate.center.batch.service.psn.PsnStatisticsUpdateService;
import com.smate.center.batch.util.pub.LogUtils;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 成果全文人员推荐ServiceImpl.
 * 
 * @author pwl
 * 
 */
@Service("pubFulltextPsnRcmdService")
@Transactional(rollbackFor = Exception.class)
public class PubFulltextPsnRcmdServiceImpl implements PubFulltextPsnRcmdService {

  /**
   * 
   */
  private static final long serialVersionUID = -6701471050304758099L;

  private static Logger logger = LoggerFactory.getLogger(PubFulltextPsnRcmdServiceImpl.class);

  @Autowired
  private PsnStatisticsUpdateService psnStatisticsUpdateService;

  @Autowired
  private PubFulltextPsnRcmdDao pubFulltextPsnRcmdDao;
  @Autowired
  private PsnStatisticsService psnStatisticsService;

  @Override
  public void updateStatusByPubId(Long pubId, Integer status) {

    try {
      this.pubFulltextPsnRcmdDao.updateStatusByPubId(pubId, status);
      // 计算人员的成果全文推荐总数并将其更新到数据库scholar2.psn_statistics.pub_fulltext_sum中_MJG_SCM-5991.
      this.updatePubFTextReSumList(pubId);
    } catch (Exception e) {
      LogUtils.error(logger, e, "更新成果pubId={}全文推荐记录状态出现异常", pubId);
      throw new RuntimeException(e);
    }
  }

  /**
   * 
   * 批量更改成果全文推荐统计数_MJG_SCM-5991.
   * 
   * @param pubId
   * @throws Exception
   */
  private void updatePubFTextReSumList(Long pubId) throws Exception {
    List<PubFulltextPsnRcmd> rePubList = pubFulltextPsnRcmdDao.queryFulltextRcmdByPubId(pubId);
    if (CollectionUtils.isNotEmpty(rePubList)) {
      for (PubFulltextPsnRcmd pubFText : rePubList) {
        this.updatePubFullTextReSum(pubFText.getPsnId());
      }
    }
  }

  /**
   * 计算人员的成果全文推荐总数并将其更新到数据库psn_statistics.pub_fulltext_sum中_MJG_SCM- 5991.
   * 
   * @param psnId
   * @throws Exception
   */
  private void updatePubFullTextReSum(Long psnId) throws Exception {
    Long pubFullTextReSum = pubFulltextPsnRcmdDao.queryRcmdFulltextCount(psnId);
    psnStatisticsUpdateService.updatePsnStatisByPubFull(psnId, Integer.valueOf(pubFullTextReSum.toString()));
  }

  @Override
  public Long getRcmdFulltextCount() {

    return this.getRcmdFulltextCount(SecurityUtils.getCurrentUserId());
  }

  @Override
  public Long getRcmdFulltextCount(Long psnId) {

    try {
      // 获取人员推荐成果全文数(数量为空则重新计算推荐)_MJG_SCM-5991.
      Integer reFTextSum = psnStatisticsService.getPubFulltextReSum(psnId);
      if (reFTextSum != null && reFTextSum.longValue() > 0) {
        return reFTextSum.longValue();
      }
      Long pubReFTextSum = this.pubFulltextPsnRcmdDao.queryRcmdFulltextCount(psnId);
      psnStatisticsUpdateService.updatePsnStatisByPubFull(psnId, Integer.valueOf(pubReFTextSum.toString()));
      return pubReFTextSum;
    } catch (Exception e) {
      LogUtils.error(logger, e, "获取人员psnId={}成果全文推荐总数", psnId);
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deletePubFulltextPsnRcmd(Long pubId) {

    try {

      List<Long> psnIds = pubFulltextPsnRcmdDao.queryOwnerPsnIdByPubId(pubId);
      this.pubFulltextPsnRcmdDao.deletePubFulltextPsnRcmd(pubId);
      // 计算人员的成果全文推荐总数并将其更新到数据库scholar2.psn_statistics.pub_fulltext_sum中_MJG_SCM-5991.
      for (Long psnId : psnIds) {
        this.updatePubFullTextReSum(psnId);
      }

    } catch (Exception e) {
      LogUtils.error(logger, e, "删除成果pubId={}对应的全文推荐记录出现异常", pubId);
      throw new RuntimeException(e);
    }
  }


}
