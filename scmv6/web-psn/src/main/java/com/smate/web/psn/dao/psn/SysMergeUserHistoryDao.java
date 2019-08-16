package com.smate.web.psn.dao.psn;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.psninfo.SysMergeUserHis;
import com.smate.web.psn.model.psninfo.SysMergeUserInfo;

/**
 * 人员合并记录表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class SysMergeUserHistoryDao extends SnsHibernateDao<SysMergeUserHis, Long> {

  /**
   * 根据人员ID获取其正在合并的帐号记录.
   * 
   * @param psnId
   */
  @SuppressWarnings("unchecked")
  public List<Long> getMergeUserList(Long psnId, Integer status) {
    String hql = "select delPsnId from SysMergeUserHis t where t.mergeStatus=1 and t.psnId=? and t.status=? ";
    return super.createQuery(hql, psnId, status).list();
  }

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
   * 保存人员合并记录.
   * 
   * @param mergeUserInfo
   */
  public void saveMergeUserInfo(SysMergeUserInfo mergeUserInfo) {
    SysMergeUserInfo tempMergeUser = this.getMergeUserInfo(mergeUserInfo.getPsnId(), mergeUserInfo.getDelPsnId());
    if (tempMergeUser != null) {
      tempMergeUser.setDelDesPsnId(mergeUserInfo.getDelDesPsnId());
      tempMergeUser.setInsName(mergeUserInfo.getInsName());
      tempMergeUser.setLoginCount(mergeUserInfo.getLoginCount());
      tempMergeUser.setPsnAvatars(mergeUserInfo.getPsnAvatars());
      tempMergeUser.setPsnEmail(mergeUserInfo.getPsnEmail());
      tempMergeUser.setPsnEnName(mergeUserInfo.getPsnEnName());
      tempMergeUser.setPsnFirstName(mergeUserInfo.getPsnFirstName());
      tempMergeUser.setPsnLastName(mergeUserInfo.getPsnLastName());
      tempMergeUser.setPsnTitolo(mergeUserInfo.getPsnTitolo());
      tempMergeUser.setPsnZhName(mergeUserInfo.getPsnZhName());
      super.getSession().update(tempMergeUser);
    } else {
      super.getSession().save(mergeUserInfo);
    }
  }

  /**
   * 根据合并的人员ID查询合并记录.
   * 
   * @param loginName
   * @param status
   * @return
   */
  @SuppressWarnings("unchecked")
  public SysMergeUserInfo getMergeUserInfo(Long psnId, Long delPsnId) {
    StringBuilder hql = new StringBuilder();
    hql.append("from SysMergeUserInfo t where t.psnId=? and t.delPsnId=? ");
    List<Object> params = new ArrayList<Object>();
    params.add(psnId);
    params.add(delPsnId);
    List<SysMergeUserInfo> mergeUserList = super.createQuery(hql.toString(), params.toArray()).list();
    if (CollectionUtils.isNotEmpty(mergeUserList)) {
      return mergeUserList.get(0);
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
   * 根据登录帐号和状态查询被合并记录.
   * 
   * @param loginName
   * @param status
   * @return
   */
  public SysMergeUserHis getMergeUserHis(String loginName, Integer status, Long delPsnId) {
    StringBuilder hql = new StringBuilder();
    hql.append("from SysMergeUserHis t where t.status=? and t.loginName=?");
    List<Object> params = new ArrayList<Object>();
    params.add(status);
    params.add(loginName);
    if (delPsnId != null) {
      hql.append(" and t.delPsnId =?");
      params.add(delPsnId);
    }
    Object obj = super.createQuery(hql.toString(), params.toArray()).uniqueResult();
    if (obj != null) {
      return (SysMergeUserHis) obj;
    }
    return null;
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

  /**
   * 获取合并状态
   * 
   * @return
   */
  public Integer getMergeStatus(Long savePsnId, Long delPsnId) {
    String hql = "from SysMergeUserHis t where t.psnId=? and t.delPsnId=?";
    List<SysMergeUserHis> list = super.createQuery(hql, savePsnId, delPsnId).list();
    if (list != null && list.size() > 0) {
      return list.get(0).getMergeStatus();
    }
    return null;
  }

}
