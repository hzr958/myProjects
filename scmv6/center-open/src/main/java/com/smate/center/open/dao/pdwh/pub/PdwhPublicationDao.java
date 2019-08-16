package com.smate.center.open.dao.pdwh.pub;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.pdwh.pub.PdwhPublication;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库成果dao
 * 
 * @author zjh
 *
 */
@Repository
public class PdwhPublicationDao extends PdwhHibernateDao<PdwhPublication, Long> {

  /**
   * 查询基准库成果的总数
   * 
   * @param searchKey
   * @return
   */
  public Long findPdwhPubCountBySearchKey(String searchKey) {
    if (StringUtils.isBlank(searchKey)) {
      return 0L;
    }
    searchKey = searchKey.trim();
    StringBuilder hql = new StringBuilder();
    hql.append("select count(1)  from  PdwhPublication  t  where   ");
    hql.append(" ( ");
    hql.append(" instr(upper(t.zhTitle),:searchKey)>0");// 中文标题
    hql.append(" or instr(upper(t.enTitle), :searchKey )>0");// 英文标题
    hql.append(" or instr(upper(t.zhKeywords),  :searchKey )>0");// 中文关键词
    hql.append(" or instr(upper(t.enKeywords),  :searchKey )>0");// 英文关键词
    hql.append(" or instr(upper(t.authorName),  :searchKey)>0");// 作者
    hql.append(" or instr(upper(t.authorNameSpec),  :searchKey)>0");// 作者 英文
    hql.append(" ) ");

    Object obj = this.createQuery(hql.toString()).setParameter("searchKey", searchKey).uniqueResult();
    if (obj != null) {
      return (Long) obj;
    }
    return null;
  }

  public List<Long> findPdwhPubIdListBySearchKey(String searchKey, Integer pageNo, Integer pageSize) {
    if (StringUtils.isBlank(searchKey)) {
      return null;
    }
    searchKey = searchKey.trim();
    StringBuilder hql = new StringBuilder();
    hql.append("select   t.pubId from  PdwhPublication  t  where   ");
    hql.append(" ( ");
    hql.append(" instr(upper(t.zhTitle),:searchKey)>0");// 中文标题
    hql.append(" or instr(upper(t.enTitle), :searchKey )>0");// 英文标题
    hql.append(" or instr(upper(t.zhKeywords),  :searchKey )>0");// 中文关键词
    hql.append(" or instr(upper(t.enKeywords),  :searchKey )>0");// 英文关键词
    hql.append(" or instr(upper(t.authorName),  :searchKey)>0");// 作者
    hql.append(" or instr(upper(t.authorNameSpec),  :searchKey)>0");// 作者 英文
    hql.append(" ) ");
    Object obj = this.createQuery(hql.toString()).setParameter("searchKey", searchKey)
        .setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize).list();
    if (obj != null) {
      return (List<Long>) obj;
    }
    return null;
  }

}
