package com.smate.center.task.dao.sns.quartz;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.PubConfirmRecord;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PubConfirmRecordDao extends SnsHibernateDao<PubConfirmRecord, Serializable> {

  /**
   * 保存成果确认记录.
   * 
   * @param psnId
   * @param insId
   * @param rolPubId
   * @param snsPubId
   * @return
   */
  public Long savePubConfirmRecord(Long psnId, Long insId, Long rolPubId, Long snsPubId) {

    PubConfirmRecord pc = new PubConfirmRecord(psnId, insId, rolPubId, snsPubId);
    super.save(pc);
    return pc.getId();
  }

  /**
   * 设置同步推荐系统状态.
   * 
   * @param id
   * @param status
   */
  public void setSyncRcmdStatus(Long id, Integer syncRcmd) {
    String hql = "update PubConfirmRecord t set t.syncRcmd = ? where t.id = ? ";
    super.createQuery(hql, syncRcmd, id).executeUpdate();
  }

}
