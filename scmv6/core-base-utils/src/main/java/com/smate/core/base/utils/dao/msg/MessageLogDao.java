package com.smate.core.base.utils.dao.msg;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.msg.MessageLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageLogDao extends SnsHibernateDao<MessageLog, Long> {

  public int getSendCount(String phonenumber) {
    String sql = "select count(1)from Message_Log t where t.sms_To=? and t.send_Time>trunc(sysdate)";

    return super.queryForInt(sql, new Object[] {phonenumber});


  }

  /**
   * 当天发送的数量
   * @param phonenumber
   * @param type
   * @return
   */
  public int getSendCount(String phonenumber , Long type) {
    String sql = "select count(1)from Message_Log t where t.sms_To=? and t.sms_type=? and t.send_Time>trunc(sysdate)";

    return super.queryForInt(sql, new Object[] {phonenumber ,type});


  }
  /**
   *  查找最新的一条数据
   * @param phonenumber
   * @param type
   * @return
   */
  public MessageLog  findMessageLogNew(String phonenumber , Long type) {
    String sql = "from MessageLog t where t.smsTo=:phonenumber and t.smsType=:type ";

    List list =
        this.createQuery(sql).setParameter("phonenumber", phonenumber).setParameter("type", type).setMaxResults(1)
            .list();
    if(list != null && list.size() >0){
      return  (MessageLog)list.get(0);
    }
    return  null ;

  }

  /**
   * 当天发送的数量
   * @param smsType
   * @return
   */
  public MessageLog getMessageLog(Long  produceLogPsnId , Long smsType) {
    String hql = "from MessageLog t where t.produceLogPsnId =:produceLogPsnId and t.smsType =:smsType order by t.logId desc";
    Object obj = this.createQuery(hql).setParameter("produceLogPsnId", produceLogPsnId).setParameter("smsType", smsType)
        .setMaxResults(1).uniqueResult();
    if(obj != null){
      return (MessageLog)obj;
    }
    return null;


  }

}
