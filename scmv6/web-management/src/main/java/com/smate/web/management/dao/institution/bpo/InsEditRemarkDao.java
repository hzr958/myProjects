package com.smate.web.management.dao.institution.bpo;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.BpoHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.management.model.analysis.DaoException;
import com.smate.web.management.model.institution.bpo.InsEditRemark;

/**
 * 修改单位备注信息Dao
 * 
 * @author zjh
 *
 */
@Repository
public class InsEditRemarkDao extends BpoHibernateDao<InsEditRemark, Serializable> {
  /**
   * 查询修改某个单位备注信息总数
   * 
   * @param insId
   * @return
   * @throws DaoException
   */
  public Long queryInsEditRemarkCountByInsId(Long insId) throws DaoException {
    String hql = "select count(ier.id) from InsEditRemark ier where ier.insId=?";
    return this.findUnique(hql, insId);
  }

  public Page queryInsEditRemarkByPage(Long insId, Page page) {
    String hql = "from InsEditRemark ier where ier.insId=?";

    Long totalCount = super.findUnique("select count(ier.id)" + hql, insId);
    page.setTotalCount(totalCount);

    List<InsEditRemark> list = super.createQuery(hql + " order by ier.createDate desc", insId)
        .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
    page.setResult(list);

    return page;
  }

}
