package com.smate.center.batch.dao.rcmd.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rcmd.pub.PublicationConfirm;
import com.smate.center.batch.model.rcmd.pub.PublicationConfirmHi;
import com.smate.core.base.utils.data.RcmdHibernateDao;

@Repository
public class PublicationConfirmHiDao extends RcmdHibernateDao<PublicationConfirmHi, Long> {

  public void saveHistory(PublicationConfirm pc) {

    PublicationConfirmHi pch = new PublicationConfirmHi(pc);
    super.save(pch);

  }

}
