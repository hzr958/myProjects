package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.PubFundinfo;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果基金标注DAO.
 * 
 * @author xys
 * 
 */
@Repository
public class PubFundinfoDao extends SnsHibernateDao<PubFundinfo, Long> {

  /**
   * 删除成果基金标注.
   * 
   * @param pubId
   * @throws DaoException
   */
  public void deletePubFundinfo(Long pubId) throws DaoException {
    super.createQuery("delete from PubFundinfo t where t.pubId=?", pubId).executeUpdate();
  }

  /**
   * 根据人员id与基金标注匹配查找项目相关成果id.
   * 
   * @param psnId
   * @param fundinfo
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPrjRelatedPubIds(Long psnId, String fundinfo) throws DaoException {
    String hql = "select t.pubId from PubFundinfo t where t.psnId=? and instr(upper(t.fundinfo),?)>0";
    return super.createQuery(hql, psnId, fundinfo.toUpperCase().trim()).list();
  }
}
