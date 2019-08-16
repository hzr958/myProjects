package com.smate.web.psn.dao.file;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.psn.form.PsnFileInfo;
import com.smate.web.psn.model.file.PsnFileShareBase;

/**
 * 个人文件分享主表dao
 * 
 * @author Administrator
 *
 */
@Repository
public class PsnFileShareBaseDao extends SnsHibernateDao<PsnFileShareBase, Long> {

  /**
   * 得到主键
   * 
   * @return
   */
  public Long getId() {
    String sql = "select  seq_v_psn_file_share_base.nextval  from dual ";
    return this.queryForLong(sql);
  }

  @SuppressWarnings("unchecked")
  public List<PsnFileShareBase> findListByPsnId(Long psnId, Page<PsnFileInfo> page) {
    String counthql = "select count(1) ";
    String hql = "from PsnFileShareBase t where t.status=0 and t.sharerId=:psnId ";
    page.setTotalCount((Long) this.createQuery(counthql + hql).setParameter("psnId", psnId).uniqueResult());
    hql += " order by t.updateDate desc , t.id desc";
    return this.createQuery(hql).setParameter("psnId", psnId).setFirstResult(page.getFirst() - 1)
        .setMaxResults(page.getPageSize()).list();
  }

}
