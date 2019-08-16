package com.smate.web.management.dao.mail.bpo;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.BpoHibernateDao;
import com.smate.web.management.model.mail.bpo.IrisEmailAddr;
import com.smate.web.management.model.mail.bpo.IrisszMail;

@Repository
public class IrisszMailDao extends BpoHibernateDao<IrisszMail, Integer> {
  public List<IrisEmailAddr> getIrisEmails() {
    String hql = "from IrisEmailAddr where status=1";
    return super.find(hql);
  }

}
