package com.smate.web.management.dao.pub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.management.model.pub.PubPdwhPO;

/**
 * 
 * @author yhx
 *
 */
@Repository
public class PubPdwhDAO extends PdwhHibernateDao<PubPdwhPO, Long> {
  @SuppressWarnings("unchecked")
  public List<PubPdwhPO> findPubList(PubPdwhPO form, Page<PubPdwhPO> page) {
    String count = "select count(1) ";
    String hql = "from PubPdwhPO t where t.status=0 ";
    String order = "order by t.pubId desc";
    StringBuffer sb = new StringBuffer();
    List<Object> params = new ArrayList<Object>();
    if (form.getPubId() != null && !form.getPubId().equals("")) {
      sb.append(" and t.pubId=? ");
      params.add(form.getPubId());
    }
    if (StringUtils.isNotBlank(form.getTitle())) {
      sb.append(" and t.title like ? ");
      params.add("%" + form.getTitle() + "%");
    }
    Long totalCount = super.findUnique(count + hql + sb.toString(), params.toArray());
    page.setTotalCount(totalCount);
    List<PubPdwhPO> pubList = super.createQuery(hql + sb.toString() + order, params.toArray())
        .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
    page.setResult(pubList);
    return pubList;
  }
}
