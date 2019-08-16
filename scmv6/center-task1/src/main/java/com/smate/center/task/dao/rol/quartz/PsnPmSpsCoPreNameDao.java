package com.smate.center.task.dao.rol.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PsnPmSpsCoPreName;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class PsnPmSpsCoPreNameDao extends RolHibernateDao<PsnPmSpsCoPreName, Long> {

  public void savePsnPmSpsCoPreName(PsnPmSpsCoPreName pcn) {
    super.getSession().save(pcn);

  }

}
