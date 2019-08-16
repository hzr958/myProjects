package com.smate.sie.web.application.dao.consts;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.web.application.model.consts.ConstDisciplineNsfc;

@Repository
public class ConstDisciplineNsfcDao extends SieHibernateDao<ConstDisciplineNsfc, Long> {

  public ConstDisciplineNsfc findConstBySub3Dis(String sub3Dis) {
    return (ConstDisciplineNsfc) super.findUnique("from ConstDisciplineNsfc t where t.disCode = ? ",
        new Object[] {sub3Dis});
  }
}
