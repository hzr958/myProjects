package com.smate.web.group.dao.grp.pub;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.group.model.group.pub.GroupPubPO;

@Repository
public class GroupPubsDAO extends SnsHibernateDao<GroupPubPO, Long> {

  public GroupPubPO findByPubId(Long pubId) {
    String hql = " from  GroupPubPO t  where t.pubId =:pubId and t.ownerPsnId is not null order by t.createDate desc";
    List list = super.createQuery(hql).setParameter("pubId", pubId).list();
    if (list != null && list.size() > 0) {
      return (GroupPubPO) list.get(0);
    }
    return null;
  }

  public List<GroupPubPO> findGrpPubsByGroupId(Long grpId, Page page) {
    String hql = "from  GroupPubPO t  where t.grpId =:grpId  and t.status =0 order by t.createDate desc";
    Long count = (Long) super.createQuery("select count(t.id) from GroupPubPO t where t.grpId =:grpId  and t.status =0")
        .setParameter("grpId", grpId).uniqueResult();
    page.setTotalCount(NumberUtils.toLong(ObjectUtils.toString(count)));
    List<GroupPubPO> list = super.createQuery(hql).setParameter("grpId", grpId).setFirstResult(page.getFirst() - 1)
        .setMaxResults(page.getPageSize()).list();
    return list;
  }

  public List<Map<String, Object>> findGrpPubInfoByPubId(Long pubId) {
    String sql =
        "select t.pub_id,t.title,t.author_names,t.brief_desc,p1.pub_index_url,p.thumbnail_path, p.file_id from v_pub_sns t inner join v_pub_index_url p1 on t.pub_id = p1.pub_id left join v_pub_fulltext p on p.pub_id = t.pub_id where t.status = 0 and t.pub_id =:pubId order by p.file_id desc";
    return super.getSession().createSQLQuery(sql).setParameter("pubId", pubId)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
  }
}
