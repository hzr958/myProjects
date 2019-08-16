package com.smate.center.merge.dao.person;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.merge.model.sns.person.SysMergeUserHis;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员合并记录表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class SysMergeUserHistoryDao extends SnsHibernateDao<SysMergeUserHis, Long> {
  /**
   * 更新被合并人员记录的合并状态
   * 
   * @param psnId
   * @param mergeStatus
   */
  public void changePsnMergeStatus(Long psnId, Integer mergeStatus) {
    String hql = "update SysMergeUserHis t set t.mergeStatus=?,t.dealTime=sysdate where t.delPsnId=? ";
    super.createQuery(hql, mergeStatus, psnId).executeUpdate();
  }

  /**
   * 获取未发送邮件的记录日志
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SysMergeUserHis> getHisNotSendByPsnId(Long psnId) {

    String hql = "from SysMergeUserHis t where t.psnId=? and t.mailStatus=0";
    List<SysMergeUserHis> queryList = super.createQuery(hql, psnId).list();
    if (CollectionUtils.isEmpty(queryList)) {
      return null;
    }
    return queryList;
  }

  public void updateMailStatus(Long psnId, Integer mailStatus) {
    String hql = "update  SysMergeUserHis t set t.mailStatus=? where t.psnId = ? and t.mailStatus=0";
    super.createQuery(hql, mailStatus, psnId).executeUpdate();
  }
}
