package com.smate.center.task.dao.email;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.email.PubConfirmPromotePsn;
import com.smate.center.task.service.email.PubConfirmPromoteService;
import com.smate.core.base.utils.data.EmailSrvHibernateDao;

/**
 * 成果认领推广邮件 dao
 * 
 * @author zk
 * 
 **/
@Repository
public class PubConfirmPromotePsnDao extends EmailSrvHibernateDao<PubConfirmPromotePsn, Long> {

  /**
   * 批量获取人员
   */
  @SuppressWarnings({"unchecked"})
  public List<PubConfirmPromotePsn> getPubConfirmPromotePsn(Integer size) throws DaoException {

    List<Integer> statuList = new ArrayList<Integer>();
    // 未处理数据
    statuList.add(PubConfirmPromoteService.IS_NOT_SEND);
    // 完成指派动作
    statuList.add(PubConfirmPromoteService.IS_SEND);

    // 再次发送
    statuList.add(PubConfirmPromoteService.RE_SEND);

    String hql =
        "from PubConfirmPromotePsn p2 where p2.status in (:status) and p2.dealDt < sysdate-2/24 order by p2.id";
    return super.createQuery(hql).setParameterList("status", statuList).setMaxResults(100).setFirstResult(100 * size)
        .list();
  }

  /**
   * 统计是否还有未发送人员
   * 
   * @return
   * @throws DaoException
   */
  public Long countNotSend() throws DaoException {

    // 未处理数据
    // 完成指派动作
    String hql = "select count(p2.id) from PubConfirmPromotePsn p2 where p2.status in (?,?)";
    return (Long) super.countHqlResult(hql, PubConfirmPromoteService.IS_NOT_SEND, PubConfirmPromoteService.IS_SEND);
  }
}
