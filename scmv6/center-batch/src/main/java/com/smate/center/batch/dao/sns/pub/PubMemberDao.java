package com.smate.center.batch.dao.sns.pub;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.PubMember;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;

@Repository
public class PubMemberDao extends SnsHibernateDao<PubMember, Long> {

  @SuppressWarnings("unchecked")
  public List<String> getSeventhCooperator(List<Long> pubIds, List<String> exclubeNames) throws DaoException {
    List<String> name;
    if (CollectionUtils.isNotEmpty(exclubeNames)) {
      name = super.createQuery(
          "select t.name from PubMember t where t.pubId in(:pubIds) and t.name not in(:name) group by t.name order by count(t.pubId) desc")
              .setParameterList("pubIds", pubIds).setParameterList("name", exclubeNames).list();
    } else {
      name = super.createQuery(
          "select t.name from PubMember t where t.pubId in(:pubIds) group by t.name order by count(t.pubId) desc")
              .setParameterList("pubIds", pubIds).list();
    }
    return name;
  }


  /**
   * 查询成果合作者邮箱
   * 
   * @param psnId
   * @return
   */
  public List<String> findCoEmail(Long psnId) {
    String hql =
        "select distinct t.email from PubMember t where t.email is not null and exists (select t1.pubId from PubOwnerMatch t1 where t1.psnId = ? and t.pubId = t1.pubId and t1.auSeq > 0)";
    return super.createQuery(hql, psnId).list();
  }

  @SuppressWarnings("unchecked")
  public List<String> queryPubMemberNameByPubId(Long pubId) throws DaoException {
    return super.createQuery("select t.name from PubMember t where t.pubId=? order by t.id desc", pubId).list();
  }

  /**
   * 获取成果的第一作者
   * 
   */
  public String getPubFirstAuthor(Long pubId) {
    String hql = "select t.name from PubMember t where t.pubId = ? order by t.seqNo asc";
    return (String) super.createQuery(hql, pubId).setMaxResults(1).uniqueResult();
  }

  /**
   * 获取成果作者列表.
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubMember> getPubMemberList(Long pubId) {

    String hql = "from PubMember t where t.pubId = ? order by t.seqNo asc";
    return super.createQuery(hql, pubId).list();
  }

  public List<Map<String, String>> findSnsPubMembers(Page page, Long pubId) throws DaoException {
    String sql =
        "select t.MEMBER_PSN_ID, t.MEMBER_NAME psn_name from pub_member t where t.pub_id = :pubId order by t.seq_no";
    Query query = super.getSession().createSQLQuery(sql);
    query.setParameter("pubId", pubId);
    query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    page.setTotalCount(query.list().size());
    query.setFirstResult(page.getFirst() - 1);
    query.setMaxResults(page.getPageSize());
    return query.list();
  }
}
