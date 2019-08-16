package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.PsnPmKeyWordRol;
import com.smate.center.batch.model.rol.pub.ScmPubKeywordsSplit;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 用户确认过的成果的关键词或用户在研究领域中添加的关键词.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnPmKeyWordRolDao extends RolHibernateDao<PsnPmKeyWordRol, Long> {

  /**
   * 获取用户确认过的成果关键词.
   * 
   * @param kwhash
   * @param psnId
   * @return
   */
  public PsnPmKeyWordRol getPsnPmKeyWord(Integer kwhash, Long psnId) {

    String hql = "from PsnPmKeyWordRol where kwhash = ? and psnId = ? ";
    List<PsnPmKeyWordRol> list = super.find(hql, kwhash, psnId);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * ScmPubKeywordsSplit.
   * 
   * @param startId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ScmPubKeywordsSplit> loadScmPubKeywordsSplit(Long startId) {

    return super.createQuery("from ScmPubKeywordsSplit t where t.id > ? order by t.id asc ", startId).setMaxResults(100)
        .list();
  }

  /**
   * ScmPubKeywordsSplit.
   * 
   * @param split
   */
  public void saveScmPubKeywordsSplit(ScmPubKeywordsSplit split) {

    super.getSession().saveOrUpdate(split);
  }

  /**
   * 根据人员获取记录.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PsnPmKeyWordRol> getPsnPmKeyWordList(Long psnId) {

    String ql = "from PsnPmKeyWordRol where psnId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 根据人员ID删除记录.
   * 
   * @param psnId
   */
  public void deleteByPsnId(Long psnId) {
    String hql = "delete from PsnPmKeyWordRol where psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }
}
