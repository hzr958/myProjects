package com.smate.web.psn.dao.privacy;



import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.privacy.PrivacyAttConfig;

/**
 * 隐私关注配置表dao
 * 
 * @author oyh
 * 
 */

@Repository
public class PrivacyAttConfigDao extends SnsHibernateDao<PrivacyAttConfig, Integer> {

  // 查询所有关注类别相互关联的隐私
  public List<String> getPrivacyKeys() throws Exception {

    StringBuilder sb = new StringBuilder();
    sb.append("select  t.privacyKey from  PrivacyAttConfig t ");

    return super.getSession().createQuery(sb.toString()).list();

  }

  public String findAttTypeBy(String privacyKey) throws Exception {

    StringBuilder sb = new StringBuilder();
    sb.append("select t.attId from PrivacyAttConfig t where t.privacyKey=?");
    return (String) super.createQuery(sb.toString(), privacyKey).uniqueResult();

  }
}
