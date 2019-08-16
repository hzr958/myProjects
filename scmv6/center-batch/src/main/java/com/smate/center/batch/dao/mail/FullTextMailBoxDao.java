package com.smate.center.batch.dao.mail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.mail.FullTextMailBox;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;


/**
 * 全文请求发件箱Dao.
 * 
 * @author cxr
 * 
 */
@Repository
public class FullTextMailBoxDao extends SnsHibernateDao<FullTextMailBox, Long> {
  /**
   * 获取某人发件箱列表.
   * 
   * @param senderId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<FullTextMailBox> getMailBoxList(Long senderId) throws DaoException {
    String hql = "from FullTextMailBox t where t.status=? and t.senderId=?";
    return super.createQuery(hql, new Object[] {0, senderId}).list();
  }

  /**
   * 分页获取发件箱列表.
   * 
   * @param senderId
   * @param page
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public Page<FullTextMailBox> getMailBoxPage(Long senderId, String searchKey, Page<FullTextMailBox> page)
      throws DaoException {
    StringBuffer hql = new StringBuffer();
    List params = new ArrayList();
    hql.append("from FullTextMailBox t where t.status=? and t.senderId=?");
    params.add(0);
    params.add(senderId);
    if (StringUtils.isNotBlank(searchKey)) {
      hql.append(" and (t.mailTitle like ? or t.mailEnTitle like ? or t.paramJson like ?)");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");
      params.add("%" + searchKey + "%");
    }
    hql.append(" order by t.mailId desc");

    Object[] paramArray = params.toArray();

    // 查询总页数
    Query queryCt = super.createQuery("select count(t.mailId) " + hql.toString(), paramArray);
    Long count = (Long) queryCt.uniqueResult();
    page.setTotalCount(count.intValue());

    // 查询数据实体
    Query queryResult = super.createQuery(hql.toString(), paramArray);
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());
    return page;
  }

  /**
   * 更新邮件状态.
   * 
   * @param mailId
   * @param status
   * @throws DaoException
   */
  public void updateMailBoxStatus(Long mailId, int status) throws DaoException {
    String hql = "update FullTextMailBox t set t.status=? where t.mailId=?";
    super.createQuery(hql, new Object[] {status, mailId}).executeUpdate();
  }

  /**
   * 批量更新邮件状态.
   * 
   * @param mailIds
   * @param status
   * @throws DaoException
   */
  public void updateMailBoxStatusBatch(List<Long> mailIds, int status) throws DaoException {
    String hql = "update FullTextMailBox t set t.status=:status where t.mailId in(:mailIds)";
    super.createQuery(hql).setParameter("status", status).setParameterList("mailIds", mailIds).executeUpdate();
  }

  /**
   * 根据人员ID删除发件箱记录.
   * 
   * @param psnId
   */
  public void delMailBoxByPsnId(Long psnId) {
    String hql = "delete from FullTextMailBox t where senderId=?";
    super.createQuery(hql, new Object[] {psnId}).executeUpdate();
  }
}
