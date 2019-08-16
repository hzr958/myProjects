package com.smate.center.task.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.psn.PsnAreaClassify;
import com.smate.core.base.utils.data.RcmdHibernateDao;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员领域大类，默认使用前两个项目大类，无项目情况下使用成果期刊大类.
 * 
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnAreaClassifyDao extends SnsHibernateDao<PsnAreaClassify, Long> {

  /**
   * 获取人员领域大类.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PsnAreaClassify> getPsnAreaClassify(Long psnId) {

    return super.createQuery("from PsnAreaClassify t where t.psnId = ? ", psnId).list();
  }

  /**
   * 获取人员领域大类.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> getPsnAreaClassifyStr(Long psnId) {

    String hql = "select t.classify from PsnAreaClassify t where t.psnId = :psnId  ";
    return super.createQuery(hql).setParameter("psnId", psnId).list();

    // return super.createQuery("select t.classify from PsnAreaClassify t where t.psnId = ? ",
    // psnId).list();
  }

  /**
   * 删除人员领域大类.
   * 
   * @param psnId
   */
  public void delPsnAreaClassify(Long psnId) {
    super.createQuery("delete from PsnAreaClassify t where t.psnId = ? ", psnId).executeUpdate();
  }

  /**
   * 删除人员领域大类.
   * 
   * @param psnId
   */
  public void delPsnAreaClassifyById(Long id) {
    super.createQuery("delete from PsnAreaClassify t where t.id = ? ", id).executeUpdate();
  }
}
