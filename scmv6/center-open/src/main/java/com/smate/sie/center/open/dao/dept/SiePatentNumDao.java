package com.smate.sie.center.open.dao.dept;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.sie.publication.PublicationRol;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class SiePatentNumDao extends RolHibernateDao<PublicationRol, Long> {

  public Long getNums(Long insId) {
    // 单位专利数
    Long patentNum = super.findUnique(
        "select count(*) from PublicationRol p where  p.typeId = '5' and p.status = '2' and p.insId = ?", insId);
    return patentNum;

  }
}
