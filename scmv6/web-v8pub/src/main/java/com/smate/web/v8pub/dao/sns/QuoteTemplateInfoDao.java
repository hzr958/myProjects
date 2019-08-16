package com.smate.web.v8pub.dao.sns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.QuoteTemplateInfo;

/**
 * 成果和项目的引用模板Dao
 * 
 * @author lhd
 */
@Repository
public class QuoteTemplateInfoDao extends SnsHibernateDao<QuoteTemplateInfo, Long> {

  /**
   * 根据类型查找模板
   * 
   * @param type
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<QuoteTemplateInfo> findByType(Integer type) {
    String hql = "from QuoteTemplateInfo t where t.type=:type and t.status = 1";
    return this.createQuery(hql).setParameter("type", type).list();
  }

  /**
   * 查找某个成果类型的模板
   * 
   * @param type
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<QuoteTemplateInfo> findPubTypeTemplate(Integer type, Integer pubType) {
    String hql =
        "from QuoteTemplateInfo t where t.type =:type and t.pubType =:pubType and t.status = 1 order by t.seqNo asc";
    return this.createQuery(hql).setParameter("type", type).setParameter("pubType", pubType).list();
  }

  /**
   * 查找某个类型的默认模板
   * 
   * @param type
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<QuoteTemplateInfo> findTypeTemplate(Integer type) {
    String hql =
        "from QuoteTemplateInfo t where t.type =:type and t.pubType is null and t.status = 1 order by t.seqNo asc";
    return this.createQuery(hql).setParameter("type", type).list();
  }
}
