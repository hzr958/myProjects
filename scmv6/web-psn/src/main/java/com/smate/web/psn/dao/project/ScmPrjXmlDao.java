package com.smate.web.psn.dao.project;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.project.ScmPrjXml;

/**
 * @author zx 项目详情Dao
 */
@Repository
public class ScmPrjXmlDao extends SnsHibernateDao<ScmPrjXml, Long> {

  /**
   * 批量获取ScmPrjXml
   */
  @SuppressWarnings("unchecked")
  public List<ScmPrjXml> getBatchScmPrjXml(List<Long> prjIds) {

    String hql = "from ScmPrjXml t where t.prjId in (:prjIds)";
    return super.createQuery(hql).setParameterList("prjIds", prjIds).list();
  }
}
