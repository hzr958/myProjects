package com.smate.web.group.dao.grp.pub;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.group.model.grp.pub.GrpPubRcmd;

/**
 * 群组成果推荐实体
 * 
 * @author tsz
 *
 */
@Repository
public class GrpPubRcmdDao extends SnsHibernateDao<GrpPubRcmd, Long> {
  /**
   * 获取群组待确认的成果数量
   * 
   * @return
   */
  public Long getPendingConfirmedCount(Long grpId) {
    // TODO 待确认的条件没有加
    String hql = "select count(t.id) from GrpPubRcmd t where  t.grpId=:grpId and t.status=0";
    Object object = this.createQuery(hql).setParameter("grpId", grpId).uniqueResult();
    if (object == null) {
      return 0L;
    } else {
      return (Long) object;
    }
  }

  /**
   * 获取这个群组的一条带确认数据
   * 
   * @param grpId
   * @return
   * @throws Exception
   */
  public Long getNextGrpPubRcmd(Long grpId) throws Exception {
    String hql = "select t.pubId from GrpPubRcmd t where t.grpId=:grpId and status=0 order by t.createDate desc ";
    Object obj = super.createQuery(hql).setParameter("grpId", grpId).setMaxResults(1).uniqueResult();
    if (obj != null) {
      return (Long) obj;
    }
    return null;
  }

  /**
   * 根据群组id 成果id查询 成果推荐记录
   * 
   * @param pubId
   * @param grpId
   * @return
   * @throws Exception
   */
  public GrpPubRcmd getGrpPubRcmd(Long pubId, Long grpId) throws Exception {
    String hql = "from GrpPubRcmd t where t.grpId=:grpId and t.pubId=:pubId and t.status=0";
    Object obj = super.createQuery(hql).setMaxResults(1).setParameter("grpId", grpId).setParameter("pubId", pubId)
        .uniqueResult();
    if (obj != null) {
      return (GrpPubRcmd) obj;
    }
    return null;
  }

  public List<GrpPubRcmd> findGrpPubsRcmdByGroupId(Long grpId, Page page) {
    Long count = (Long) super.createQuery("select count(t.id) from GrpPubRcmd t where t.grpId =:grpId  and t.status =0")
        .setParameter("grpId", grpId).uniqueResult();
    if (count == 0L) {
      return null;
    }
    String hql =
        "from  GrpPubRcmd t  where t.grpId =:grpId  and t.status =0 order by t.publishYear desc, t.createDate desc";
    page.setPageSize(Integer.parseInt((String.valueOf(count))));
    page.setTotalCount(NumberUtils.toLong(ObjectUtils.toString(count)));
    List<GrpPubRcmd> list = super.createQuery(hql).setParameter("grpId", grpId).setFirstResult(page.getFirst() - 1)
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
