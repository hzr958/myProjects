package com.smate.web.management.dao.institution.bpo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.BpoHibernateDao;
import com.smate.web.management.model.institution.bpo.InsAliasStatus;

@Repository
public class InsAliasStatusDao extends BpoHibernateDao<InsAliasStatus, Serializable> {
  /**
   * 获取单位检索式，别名状态.
   * 
   * @param insId
   * @return
   */
  public InsAliasStatus getInsAliasStatus(Long insId) {

    InsAliasStatus status = super.get(insId);
    if (status == null) {
      status = new InsAliasStatus();
      status.setCgDate(new Date());
      status.setInsId(insId);
      this.save(status);
    }
    return status;
  }

}
