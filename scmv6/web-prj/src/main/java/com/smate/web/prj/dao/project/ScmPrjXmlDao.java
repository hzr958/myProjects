package com.smate.web.prj.dao.project;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.prj.model.common.ScmPrjXml;

/**
 * 项目XML
 * 
 * @author houchuanjie
 * @date 2018年3月15日 下午4:45:17
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
