package com.smate.center.batch.dao.pdwh.pub.isi;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.isi.IsiPubExpandLog;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * ISI成果拆分定时器任务数据出错日志表操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class IsiPubExpandLogDao extends PdwhHibernateDao<IsiPubExpandLog, Long> {

  /**
   * 获取ISI成果拆分任务.
   * 
   * @param number
   * @return
   */
  @SuppressWarnings({"unchecked"})
  public List<Long> loadIsiNeedExpandPubId(Integer number) {
    String sql = "select t.pubId from IsiPubExpandLog t where t.status='0' ";
    return super.getSession().createQuery(sql).setMaxResults(number).list();
  }

  /**
   * 更新成果拆分记录状态.
   * 
   * @param pubId
   * @param status
   */
  public void updateIsiSplitDataStatus(Long pubId, Integer status) {
    String sql = "update IsiPubExpandLog t set t.status=:status where t.pubId=:pubId ";
    super.getSession().createQuery(sql).setParameter("status", status).setParameter("pubId", pubId).executeUpdate();
  }

  /**
   * 记录ISI成果拆分日志记录.
   * 
   * @param pubId
   * @param splitResultFailed
   * @param errorMsg
   */
  public void writeIsiSplitLog(Long pubId, Integer status, String errorMsg) {
    String sql = "update IsiPubExpandLog t set t.status=:status,t.errLog=:errLog where t.pubId=:pubId ";
    super.getSession().createQuery(sql).setParameter("status", status).setParameter("errLog", errorMsg)
        .setParameter("pubId", pubId).executeUpdate();
  }

  /**
   * 获取ISI成果拆分日志记录.
   * 
   * @param pubId
   * @return
   */
  public IsiPubExpandLog getIsiPubExpandLog(Long pubId) {
    String hql = "from IsiPubExpandLog t where t.pubId=? ";
    return super.findUnique(hql, pubId);
  }

  /**
   * 保存成果拆分日志记录.
   * 
   * @param pubLog
   */
  public void saveIsiPubExpandLog(IsiPubExpandLog pubLog) {
    IsiPubExpandLog mLog = this.getIsiPubExpandLog(pubLog.getPubId());
    if (mLog != null) {
      mLog.setStatus(pubLog.getStatus());
    } else {
      super.save(pubLog);
    }
  }

}
