package com.smate.sie.center.open.dao.dept;

import org.springframework.stereotype.Repository;
import com.smate.center.open.model.sie.publication.PublicationRol;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class SieTheisNumDao extends RolHibernateDao<PublicationRol, Long> {

  public Long getNums(Long insId) {
    // 单位论文数
    Long theisNum = super.findUnique(
        "select count(*) from PublicationRol p where  p.typeId in ('3','4')  and p.status = 2 and p.insId = ?", insId);
    return theisNum;

  }
}
