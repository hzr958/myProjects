package com.smate.center.task.single.dao.solr;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.PdwhIndexPublication;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PdwhIndexPublicationDao extends PdwhHibernateDao<PdwhIndexPublication, Long> {

  public Long getCount(String code) {
    String Hql = "";
    Long count = null;
    if ("other".equals(code)) {
      Hql =
          "select count(t.pubId) from  PdwhIndexPublication t where ascii(substr(t.shortTitle, 0, 1)) < 65  or (ascii(substr(t.shortTitle, 0, 1)) > 90 and  ascii(substr(t.shortTitle, 0, 1)) < 97)    or ascii(substr(t.shortTitle, 0, 1)) > 122";
      count = (Long) super.createQuery(Hql).uniqueResult();
    } else {
      Hql = "select count(t.pubId) from  PdwhIndexPublication t where upper(t.shortTitle) like :code ";
      count = (Long) super.createQuery(Hql).setParameter("code", code + "%").uniqueResult();
    }

    return count;

  }

  @SuppressWarnings("unchecked")
  public List<PdwhIndexPublication> getPubByIndexCode(String code, int startIndex, int maxCount) {
    Query q = null;
    String queryHql = "select new PdwhIndexPublication(pubId,pubTitle,shortTitle) from PdwhIndexPublication t ";
    String orderBy = " order by substr(t.shortTitle,2,3),t.pubId";
    if ("other".equals(code)) {// 查询那些不是字母开头的
      queryHql +=
          " where ascii(substr(t.shortTitle, 0, 1)) < 65 " + "    or (ascii(substr(t.shortTitle, 0, 1)) > 90 and "
              + "        ascii(substr(t.shortTitle, 0, 1)) < 97) " + "    or ascii(substr(t.shortTitle, 0, 1)) > 122";
      q = super.createQuery(queryHql + orderBy);
    } else {
      queryHql += " where instr(substr(upper(t.shortTitle),0,1),?) >0 ";
      q = super.createQuery(queryHql + orderBy, code);
    }
    q.setFirstResult(startIndex);
    q.setMaxResults(maxCount);
    return q.list();
  }

  @SuppressWarnings("unchecked")
  public List<PdwhIndexPublication> getZhPubByIndexCode(String code, int startIndex, int maxCount) {
    Query q = null;
    String queryHql = "select new PdwhIndexPublication(pubId,dbId,zhTitle,enTitle) from PdwhIndexPublication t ";
    String orderBy = " order by substr(t.zhTitleShort,2,3),t.pubId";
    if ("other".equals(code)) {// 查询那些首字母不是字母开头的
      queryHql += " where ascii(substr(t.zhTitleShort, 0, 1)) < 65 "
          + "    or (ascii(substr(t.zhTitleShort, 0, 1)) > 90 and "
          + "        ascii(substr(t.zhTitleShort, 0, 1)) < 97) " + "    or ascii(substr(t.zhTitleShort, 0, 1)) > 122";
      q = super.createQuery(queryHql + orderBy);
    } else {
      queryHql += " where instr(substr(upper(t.zhTitleShort),0,1),?) >0 ";
      q = super.createQuery(queryHql + orderBy, code);
    }
    q.setFirstResult(startIndex);
    if (maxCount != 0) {
      q.setMaxResults(maxCount);
    }
    return q.list();
  }

  @SuppressWarnings("unchecked")
  public List<PdwhIndexPublication> getNeedCleanData(Long lastId, int size) {
    String hql =
        "select new PdwhIndexPublication(pubId,shortTitle) from  PdwhIndexPublication t where LENGTH(t.shortTitle) <> LENGTHB(t.shortTitle) and t.pubId > :lastId order by t.pubId";
    return super.createQuery(hql).setParameter("lastId", lastId).setMaxResults(size).list();
  }

  public void updatePdwhIndexPubTitle(Long pubId, String result) {
    String hql = "update PdwhIndexPublication t set t.shortTitle = :result where t.pubId = :pubId";
    super.createQuery(hql).setParameter("result", result).setParameter("pubId", pubId).executeUpdate();
  }

  public void deleteNotExist() {
    String hql =
        "delete from PdwhIndexPublication t where exists (select 1 from PubPdwhPO f where t.pubId=f.pubId and f.status=1) ";
    super.createQuery(hql).executeUpdate();
  }


}
