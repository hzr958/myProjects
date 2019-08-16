package com.smate.web.management.dao.psn;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.management.model.analysis.DaoException;
import com.smate.web.management.model.psn.SysMergeUserHis;


/**
 * 人员合并记录表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class SysMergeUserHistoryDao extends SnsHibernateDao<SysMergeUserHis, Long> {

  /**
   * 获取被删除的帐号记录
   * 
   * @param delPsnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SysMergeUserHis> getMergeByDelPsnId(Long delPsnId) throws DaoException {

    String hql = "from SysMergeUserHis t where t.delPsnId=? and t.status=99";
    return super.createQuery(hql, delPsnId).list();
  }

  /**
   * 获取被删除的帐号记录
   * 
   * @param delPsnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SysMergeUserHis> getMergeBy(Long psnId, Long delPsnId) throws DaoException {

    String hql = "from SysMergeUserHis t where t.psnId=? and t.delPsnId=? and t.status=99";
    return super.createQuery(hql, psnId, delPsnId).list();
  }

  public void updateMailStatus(Long psnId, Integer mailStatus) {
    String hql = "update  SysMergeUserHis t set t.mailStatus=? where t.psnId = ?";
    super.createQuery(hql, mailStatus, psnId).executeUpdate();
  }

  /**
   * 
   * 获取未发送邮件记录的psnId
   * 
   * @param StartId
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getNotSendEmailLog(Long startId, Integer size) {

    String hql =
        "select distinct t.psnId from SysMergeUserHis t where t.psnId>? and t.mailStatus=0 and t.mergeStatus in (2,3) order by t.psnId ";
    return super.createQuery(hql, startId).setMaxResults(size).list();
  }

  /**
   * 获取未发送邮件的记录日志
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SysMergeUserHis> getHisNotSendByPsnId(Long psnId) {

    SysMergeUserHis saveHis = getHisSaveLog(psnId);
    if (saveHis == null) {
      return null;
    }
    String hql = "from SysMergeUserHis t where t.psnId=? and t.mailStatus=0 ";
    List<SysMergeUserHis> queryList = super.createQuery(hql, psnId).list();
    if (CollectionUtils.isEmpty(queryList)) {
      return null;
    }
    if (!queryList.contains(saveHis)) {
      queryList.add(saveHis);
    }
    return queryList;
  }

  /**
   * 通过psnId获取保留人记录
   * 
   * @param psnId
   * @return
   */
  public SysMergeUserHis getHisSaveLog(Long psnId) {
    String hql = "from SysMergeUserHis t where t.psnId=? and t.status=1";

    return (SysMergeUserHis) super.createQuery(hql, psnId).setMaxResults(1).uniqueResult();
  }

  /**
   * 根据登录帐号和状态查询被合并记录.
   * 
   * @param loginName
   * @param status
   * @return
   */
  public SysMergeUserHis getMergeUserHis(String loginName, Integer status, Long delPsnId) {
    StringBuilder hql = new StringBuilder();
    hql.append("from SysMergeUserHis t where t.status=? and t.loginName=? and t.delPsnId =?");
    List<Object> params = new ArrayList<Object>();
    params.add(status);
    params.add(loginName);
    params.add(delPsnId);
    Object obj = super.createQuery(hql.toString(), params.toArray()).uniqueResult();
    if (obj != null) {
      return (SysMergeUserHis) obj;
    }
    return null;
  }

  /**
   * 保存人员合并记录.
   * 
   * @param mergeUserHis
   */
  public void saveMergeUserHis(SysMergeUserHis mergeUserHis) {
    SysMergeUserHis tempMergeUser =
        this.getMergeUserHis(mergeUserHis.getLoginName(), mergeUserHis.getStatus(), mergeUserHis.getDelPsnId());
    if (tempMergeUser == null) {
      super.getSession().save(mergeUserHis);
    }
  }

  /**
   * 根据人员ID获取其正在合并的帐号记录.
   * 
   * @param psnId
   */
  @SuppressWarnings("unchecked")
  public List<String> getMergeUserList(Long psnId, Integer status) {
    String hql = "select loginName from SysMergeUserHis t where t.mergeStatus=1 and t.psnId=? and t.status=? ";
    return super.createQuery(hql, psnId, status).list();
  }

  /**
   * 获取合并状态
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public Integer getMergeStatus(Long savePsnId, Long delPsnId) throws DaoException {

    String hql = "from SysMergeUserHis t where t.psnId = :savePsnId  and t.delPsnId = :delPsnId";
    List<SysMergeUserHis> list =
        super.createQuery(hql).setParameter("savePsnId", savePsnId).setParameter("delPsnId", delPsnId).list();
    if (list != null && list.size() > 0) {
      return list.get(0).getMergeStatus();
    }
    return null;
  }

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
}
