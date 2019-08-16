package com.smate.center.batch.dao.pdwh.pub.cnki;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubExpandLog;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * CNKI成果拆分定时器任务数据出错日志表操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class CnkiPubExpandLogDao extends PdwhHibernateDao<CnkiPubExpandLog, Long> {

  /**
   * 获取CNKI成果拆分任务.
   * 
   * @param number
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> loadCnkiNeedExpandPubId(Integer number) {
    String sql = "select t.pubId from CnkiPubExpandLog t where t.status='0' ";
    return super.getSession().createQuery(sql).setMaxResults(number).list();
  }

  /**
   * 更新成果拆分记录状态.
   * 
   * @param pubId
   * @param status
   */
  public void updateCnkiSplitDataStatus(Long pubId, Integer status) {
    String sql = "update CnkiPubExpandLog t set t.status=:status where t.pubId=:pubId ";
    super.getSession().createQuery(sql).setParameter("status", status).setParameter("pubId", pubId).executeUpdate();
  }

  /**
   * 记录CNKI成果拆分日志记录.
   * 
   * @param pubId
   * @param splitResultFailed
   * @param errorMsg
   */
  public void writeIsiSplitLog(Long pubId, Integer status, String errorMsg) {
    String sql = "update CnkiPubExpandLog t set t.status=:status,t.errLog=:errLog where t.pubId=:pubId ";
    super.getSession().createQuery(sql).setParameter("status", status).setParameter("errLog", errorMsg)
        .setParameter("pubId", pubId).executeUpdate();
  }

  /**
   * 获取CNKI成果拆分日志记录.
   * 
   * @param pubId
   * @return
   */
  public CnkiPubExpandLog getCnkiPubExpandLog(Long pubId) {
    String hql = "from CnkiPubExpandLog t where t.pubId=? ";
    return super.findUnique(hql, pubId);
  }

  /**
   * 保存成果拆分日志记录.
   * 
   * @param pubLog
   */
  public void saveCnkiPubExpandLog(CnkiPubExpandLog pubLog) {
    CnkiPubExpandLog mLog = this.getCnkiPubExpandLog(pubLog.getPubId());
    if (mLog != null) {
      mLog.setStatus(pubLog.getStatus());
    } else {
      super.save(pubLog);
    }
  }

}
