package com.smate.web.management.dao.psn;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.management.model.psn.SysMergeUserInfo;

/**
 * 被合并删除的人员信息表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class SysMergeUserInfoDao extends SnsHibernateDao<SysMergeUserInfo, Long> {

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
   * 根据人员ID获取其合并的帐号记录.
   * 
   * @param psnId
   * @param loginCountList
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SysMergeUserInfo> getMergeUserInfoList(Long psnId, List<String> loginCountList) {
    String hql = "from SysMergeUserInfo t where t.psnId=? and t.loginCount in (:loginCount) ";
    return super.createQuery(hql, psnId).setParameterList("loginCount", loginCountList).list();
  }
}
