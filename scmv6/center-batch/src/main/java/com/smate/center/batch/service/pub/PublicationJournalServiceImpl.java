package com.smate.center.batch.service.pub;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.psn.PsnJournalDao;
import com.smate.center.batch.dao.sns.psn.PsnJournalGradeDao;
import com.smate.center.batch.dao.sns.pub.JournalGradeDao;
import com.smate.center.batch.dao.sns.pub.PublicationJournalDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PsnJournal;
import com.smate.center.batch.model.sns.pub.PublicationJournal;

/**
 * 成果期刊服务.
 * 
 * @author liqinghua
 * 
 */
@Service("publicationJournalService")
@Transactional(rollbackFor = Exception.class)
public class PublicationJournalServiceImpl implements PublicationJournalService {

  private static final long serialVersionUID = 4487807291808310474L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PublicationXmlService publicationXmlService;
  @Autowired
  private PublicationJournalDao pubJournalDao;
  @Autowired
  private JournalGradeDao journalGradeDao;
  @Autowired
  private PsnJournalDao psnJournalDao;
  @Autowired
  private PsnJournalGradeDao psnJournalGradeDao;

  @Override
  public void savePubJournal(Long pubId, Long psnId, String issn, String jname, Long jnlId, Integer pubYear)
      throws ServiceException {

    try {
      if (StringUtils.isBlank(issn) && StringUtils.isBlank(jname)) {
        this.delPubJournal(pubId);
        return;
      }
      issn = StringUtils.substring(issn, 0, 100);
      this.pubJournalDao.savePubJournal(pubId, psnId, issn, jname, jnlId, pubYear);
    } catch (Exception e) {
      logger.error("保存成果期刊pubId:" + pubId, e);
      throw new ServiceException("保存成果期刊pubId:" + pubId, e);
    }
  }

  @Override
  public void delPubJournal(Long pubId) throws ServiceException {

    try {
      this.pubJournalDao.delPubJournal(pubId);
    } catch (Exception e) {
      logger.error("删除成果期刊pubId:" + pubId, e);
      throw new ServiceException("删除成果期刊pubId:" + pubId, e);
    }
  }

  @Override
  public boolean isPubHxj(Long pubId) throws ServiceException {

    try {
      String issn = pubJournalDao.getPubJIssn(pubId);
      return journalGradeDao.isHxJ(issn);
    } catch (Exception e) {
      logger.error("判断成果期刊是否核心期刊pubId:" + pubId, e);
      throw new ServiceException("判断成果期刊是否核心期刊pubId:" + pubId, e);
    }
  }

  @Override
  public void savePsnPubJournal(Long psnId) throws ServiceException {

    try {
      List<Object[]> psnIssnList = this.pubJournalDao.getPsnOwnerPubIssn(psnId);
      if (CollectionUtils.isEmpty(psnIssnList)) {
        this.psnJournalDao.delPsnJournal(psnId);
        psnJournalGradeDao.delPsnJGrade(psnId);
        return;
      }
      List<PsnJournal> pjList = this.psnJournalDao.getPsnAllJournal(psnId);
      int maxGrade = 4;
      // 查重，去掉删除差异期刊列表
      outer_loop: for (PsnJournal pj : pjList) {
        for (int i = 0; i < psnIssnList.size(); i++) {
          Object[] psnIssn = psnIssnList.get(i);
          String issn = (String) psnIssn[0];
          Long tf = (Long) psnIssn[1];
          // 找到成果存在的期刊ISSN
          if (issn.equalsIgnoreCase(pj.getIssn())) {
            // 获取最高等级
            if (pj.getGrade() < maxGrade) {
              maxGrade = pj.getGrade();
            }
            psnIssnList.remove(i);
            // 重新设置TF
            pj.setTf(tf.intValue());
            this.psnJournalDao.save(pj);
            continue outer_loop;
          }
        }
        // 没找到
        this.psnJournalDao.delete(pj.getId());
      }
      if (CollectionUtils.isNotEmpty(psnIssnList)) {
        for (int i = 0; i < psnIssnList.size(); i++) {
          Object[] psnIssn = psnIssnList.get(i);
          String issn = (String) psnIssn[0];
          Long tf = (Long) psnIssn[1];
          int hxj = 0;
          int grade = this.journalGradeDao.getJournalGrade(issn);
          if (grade <= 3) {
            hxj = 1;
          }
          PsnJournal pj = new PsnJournal(psnId, issn.toUpperCase(), issn.toLowerCase(), grade, hxj);
          pj.setTf(tf.intValue());
          this.psnJournalDao.save(pj);
          // 获取最高等级
          if (grade < maxGrade) {
            maxGrade = grade;
          }
        }
      }
      this.psnJournalGradeDao.savePsnJGrade(psnId, maxGrade);
    } catch (Exception e) {
      logger.error("保存人员成果期刊psnId:" + psnId, e);
      throw new ServiceException("保存人员成果期刊pubId:" + psnId, e);
    }
  }

  @Override
  public PublicationJournal getPubJournal(Long pubId) throws ServiceException {
    try {
      return this.pubJournalDao.get(pubId);
    } catch (Exception e) {
      logger.error("获取文献期刊信息pubId:" + pubId, e);
      throw new ServiceException("获取文献期刊信息pubId:" + pubId, e);
    }
  }

}
