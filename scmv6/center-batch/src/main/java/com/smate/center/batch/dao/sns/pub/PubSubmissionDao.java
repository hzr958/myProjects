package com.smate.center.batch.dao.sns.pub;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.PublicationSubmmission;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * @author yamingd 成果提交状态管理DAO.
 */
@Repository
public class PubSubmissionDao extends SnsHibernateDao<PublicationSubmmission, Long> {

  /**
   * 获取已提交成果数.
   * 
   * @param insId
   * @param psnId
   * @return
   */
  public Long getSubmitTotal(Long insId, Long psnId) {
    String hql =
        "select count(*) from Publication p   where p.id in (select t.pubId from PublicationSubmmission t where (t.state = 1 or t.state = 2) and t.psnId=? and t.insId = ? ) and p.status=0   and p.psnId=? ";
    Long result = super.findUnique(hql, psnId, insId, psnId);
    return result;
  }

  /**
   * 唯一主键查询,读取提交状态.
   * 
   * @param insId
   * @param pubId insId+pubId唯一主键
   * @return
   * @throws DaoException
   */
  public PublicationSubmmission getByUqId(Long insId, Long pubId) throws DaoException {
    PublicationSubmmission item = super.findUnique("from PublicationSubmmission t where t.insId = ? and t.pubId = ?",
        new Object[] {insId, pubId});
    return item;
  }

  public void updatePubSubmissionState(String pubIds, Long insId, Long psnId, Integer state) {
    String hql =
        "update PublicationSubmmission t set t.state=? where t.insId=? and t.psnId=? and t.pubId in(" + pubIds + ")";
    super.createQuery(hql, new Object[] {state, insId, psnId}).executeUpdate();
  }

  /**
   * 添加一个提交状态记录.
   * 
   * @param insId
   * @param pubId
   * @param psnId
   * @param versionNo
   * @param state
   * @throws DaoException
   */
  public void addPubSubmission(Long insId, Long pubId, Long psnId, Integer versionNo, Integer state)
      throws DaoException {
    PublicationSubmmission item = new PublicationSubmmission();
    item.setCreateAt(new Date());
    item.setLastSync(new Date());
    item.setPsnId(psnId);
    item.setState(state);
    item.setVersionNo(versionNo);
    item.setInsId(insId);
    item.setPubId(pubId);
    super.save(item);
  }


  /**
   * 保存提交状态.
   * 
   * @param item
   * @throws DaoException
   */
  public void saveState(PublicationSubmmission item) throws DaoException {
    super.save(item);
  }
}
