package com.smate.center.oauth.dao.thirduser;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.oauth.model.thirduser.SysThirdUser;
import com.smate.core.base.utils.data.CasHibernateDao;

/**
 * 第三方登录
 * 
 * @author Scy
 * 
 */
@Repository
public class SysThirdUserDao extends CasHibernateDao<SysThirdUser, Long> {

  /**
   * 根据第三方ID获取psnId
   * 
   * @param type
   * @param thirdId
   * @return
   */
  public Long findPsnId(Integer type, String thirdId) {
    String hql = "select t.psnId from SysThirdUser t where t.type = ? and t.thirdId = ?";
    return (Long) this.createQuery(hql, type, thirdId).uniqueResult();
  }

  /**
   * 根据第三方UnionID获取psnId
   * 
   * @param type
   * @param unionId
   * @return
   */
  public Long findUnionID(Integer type, String unionId) {
    String hql = "select t.psnId from SysThirdUser t where t.type =:type and t.unionId =:unionId";
    return (Long) this.createQuery(hql).setParameter("type", type).setParameter("unionId", unionId).uniqueResult();
  }

  /**
   * 
   * @param type
   * @param psnId
   * @return
   */
  public SysThirdUser findByTypeAndPsnId(Integer type, Long psnId) {
    String hql = "from SysThirdUser t where t.type = ? and t.psnId = ?";
    return (SysThirdUser) this.createQuery(hql, type, psnId).uniqueResult();
  }

  /**
   * 根据第三方ID更新NickName
   * 
   * @param type
   * @param thirdId
   * @return
   */
  public Integer updateNickname(Integer type, String thirdId, String nickName) {
    String hql = "  update  SysThirdUser   t  set  t.nickName  =  ?  where t.type = ? and t.thirdId = ?";
    return (Integer) this.createQuery(hql, nickName, type, thirdId).executeUpdate();
  }

  public void deleteBy(Long psnId, String unionId, int type) {
    String hql = "delete  from SysThirdUser t where t.type =:type and t.unionId=:unionId and t.psnId =:psnId";
    this.createQuery(hql).setParameter("psnId", psnId).setParameter("unionId", unionId).setParameter("type", type)
        .executeUpdate();
  }

  // --------------------------
  /**
   * 根据第三方ID更新UnionID
   * 
   * @param type
   * @param thirdId
   * @return
   */
  public Integer updateUnionID(Integer type, String thirdId, String unionId) {
    String hql = "update SysThirdUser t set t.unionId  =:unionId  where t.type =:type and t.thirdId =:thirdId";
    return (Integer) this.createQuery(hql).setParameter("unionId", unionId).setParameter("type", type)
        .setParameter("thirdId", thirdId).executeUpdate();
  }

  /**
   * 据第三方类型查询所有第三方用户
   * 
   * @param type
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> findByType(Integer type) {
    String hql = "select t.thirdId from SysThirdUser t where t.type =:type and t.unionId is null";
    List<String> list = this.createQuery(hql).setParameter("type", type).list();
    return list;
  }

}
