package com.smate.web.management.service.analysis;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.management.dao.analysis.sns.PersonTaughtDao;
import com.smate.web.management.dao.analysis.sns.PsnInsDetailDao;
import com.smate.web.management.dao.analysis.sns.PsnJournalDao;
import com.smate.web.management.model.analysis.sns.PsnInsDetail;
import com.smate.web.management.model.analysis.sns.RecommendScore;

/**
 * 
 * 基金、论文合作者推荐相关度：∑（关键词/同义词/翻译词+院系名称+发表期刊+所教课程）相同次数.
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCooperatorRelevance")
@Transactional(rollbackFor = Exception.class)
public class PsnCooperatorRelevance implements PsnCooperator {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  // 基金、论文合作者推荐质量
  @Resource(name = "psnCooperatorQuality")
  private PsnCooperator next;
  @Autowired
  private PsnInsDetailDao psnInsDetailDao;
  @Autowired
  private PsnJournalDao psnJournalDao;
  @Autowired
  private PersonTaughtDao personTaughtDao;

  @Override
  public void handler(RecommendScore rs) throws Exception {
    this.deptCompare(rs);
    this.jnlCompare(rs);
    this.taughtCompare(rs);
    next.handler(rs);
  }

  // 院系名称比较,表：schoalr2.psn_ins_detail(psn_id,dept_zhhash,dept_enhash)
  private void deptCompare(RecommendScore rs) throws Exception {
    try {
      PsnInsDetail pid = psnInsDetailDao.get(rs.getPsnId());
      PsnInsDetail coPid = psnInsDetailDao.get(rs.getCoPsnId());
      if (pid != null && coPid != null) {
        Long deptEn = pid.getDeptEnHash();
        Long coDeptEn = coPid.getDeptEnHash();
        // 英文院系名称相同，如英文相同，不需要计算中文，仅算１分
        if (deptEn != null && coDeptEn != null && deptEn.longValue() == coDeptEn.longValue()) {
          rs.getRelevanceScore().setDeptScore(1);
        } else {
          // 中文院系名称相同
          Long deptZh = pid.getDeptZhHash();
          Long coDeptZh = coPid.getDeptZhHash();
          if (deptZh != null && coDeptZh != null && deptZh.longValue() == coDeptZh.longValue()) {
            rs.getRelevanceScore().setDeptScore(1);
          }
        }
      }
    } catch (Exception e) {

      logger.error("基金、论文合作者推荐相关度计算失败，psn_ins_detail:psnId={},coPsnId={}", rs.getPsnId(), rs.getCoPsnId(), e);
      throw new Exception(e);
    }
  }

  // 发表期刊比较,表：scholar2.psn_journal(psn_id,issn)
  private void jnlCompare(RecommendScore rs) throws Exception {
    try {
      Long jnlScore = psnJournalDao.psnJournalEqualCount(rs.getPsnId(), rs.getCoPsnId());
      rs.getRelevanceScore().setJnlScore(jnlScore.intValue());
    } catch (Exception e) {

      logger.error("基金、论文合作者推荐相关度计算失败，psn_journal:psnId={},coPsnId={}", rs.getPsnId(), rs.getCoPsnId(), e);
      throw new Exception(e);
    }
  }

  // 所教课程比较，表：scholar2.person_taught_hash(psn_id,taught_hash)
  private void taughtCompare(RecommendScore rs) throws Exception {
    try {
      Long taughtScore = personTaughtDao.personTaughtEqualCount(rs.getPsnId(), rs.getCoPsnId());
      rs.getRelevanceScore().setTaughtScore(taughtScore.intValue());
    } catch (Exception e) {

      logger.error("基金、论文合作者推荐相关度计算失败，person_taught_hash:psnId={},coPsnId={}", rs.getPsnId(), rs.getCoPsnId(), e);
      throw new Exception(e);
    }
  }

}
