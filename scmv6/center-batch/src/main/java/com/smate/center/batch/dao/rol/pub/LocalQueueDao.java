package com.smate.center.batch.dao.rol.pub;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.LocalQueueError;
import com.smate.center.batch.model.rol.pub.LocalQueueMessage;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 本地消息DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class LocalQueueDao extends RolHibernateDao<LocalQueueMessage, Long> {

  /**
   * 获取新的消息ID.
   * 
   * @return
   */
  public Long generalMsgId() {

    BigDecimal msgId = (BigDecimal) super.getSession()
        .createSQLQuery("select SEQ_LOCAL_QUEUE_MESSAGE.nextval from dual ").uniqueResult();
    return msgId.longValue();
  }

  /**
   * 删除消息.
   * 
   * @param msgId
   */
  public void removeMsg(Long msgId) {

    super.createQuery("delete from LocalQueueError where msgId = ? ", msgId).executeUpdate();
    super.createQuery("delete from LocalQueueMessage where id = ? ", msgId).executeUpdate();
  }

  /**
   * 保存消息错误信息.
   * 
   * @param error
   */
  public void saveLocalQueueError(LocalQueueError error) {
    super.createQuery("delete from LocalQueueError where msgId = ? ", error.getMsgId()).executeUpdate();
    super.getSession().save(error);
  }

  /**
   * 标记状态.
   * 
   * @param msgId
   */
  public void updateLocalQueneState(Long msgId, int state) {
    super.createQuery("update LocalQueueMessage set state = ?,opAt = ?  where id = ? ", state, new Date(), msgId)
        .executeUpdate();
  }

  /**
   * 加载需要发送的消息分发出去.
   * 
   * @param batchSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<LocalQueueMessage> loadSendMessage(int batchSize) {

    // 先查找状态 = 0的
    String hql =
        "from LocalQueueMessage where state = 0 and createAt < ? and errorNum < 3 order by errorNum asc,priority desc,id asc ";
    // 10秒之前
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.SECOND, -10);
    Date date = cal.getTime();
    Query queryResult = super.createQuery(hql, date);
    queryResult.setMaxResults(batchSize);
    List<LocalQueueMessage> listResult = queryResult.list();
    if (CollectionUtils.isNotEmpty(listResult)) {
      return listResult;
    }
    // 再查找状态=2的，5分钟前的
    hql =
        "from LocalQueueMessage where state = 2 and opAt < ? and errorNum < 3 order by errorNum asc,priority desc,id asc ";
    // 5分钟前的
    cal = Calendar.getInstance();
    cal.add(Calendar.MINUTE, -5);
    date = cal.getTime();
    queryResult = super.createQuery(hql, date);
    queryResult.setMaxResults(batchSize);
    listResult = queryResult.list();
    if (CollectionUtils.isNotEmpty(listResult)) {
      return listResult;
    }

    // 再查找状态=9的，错误次数<3的
    hql =
        "from LocalQueueMessage where state = 9 and opAt < ? and errorNum < 3 order by errorNum asc,priority desc,id asc ";
    // 5分钟前的
    cal = Calendar.getInstance();
    cal.add(Calendar.MINUTE, -5);
    date = cal.getTime();
    queryResult = super.createQuery(hql, date);
    queryResult.setMaxResults(batchSize);
    listResult = queryResult.list();
    return listResult;
  }
}
