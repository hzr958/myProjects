package com.smate.core.base.utils.dao.wechat;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.wechat.WeChatRelation;

/**
 * 
 * 微信openid与科研之友openid关联Dao
 * 
 * @author zk
 *
 */
@Repository
public class WeChatRelationDao extends SnsHibernateDao<WeChatRelation, Long> {

  // 根据微信 webChatOpenId获取人员psnId
  public Long queryPsnIdByWeChatOpenid(String webChatOpenId) {
    return super.findUnique(
        "select psnId from OpenUserUnion where token=? and openId=(select smateOpenId from WeChatRelation where webChatOpenId=?) ",
        "00000000", webChatOpenId);
  }

  // 根据微信 webChatOpenId获取人员psnId
  public Long queryPsnIdByWeChatUnionid(String weChatUnionId) {
    return super.findUnique(
        "select psnId from OpenUserUnion where token=? and openId=(select smateOpenId from WeChatRelation where weChatUnionId=?) ",
        "00000000", weChatUnionId);
  }

  /**
   * 取消绑定
   * 
   * @param wxOpenId
   */
  public void cancelBind(String wxOpenId) {
    String hql = "delete from WeChatRelation w where w.webChatOpenId=:wx";
    super.createQuery(hql).setParameter("wx", StringUtils.trim(wxOpenId)).executeUpdate();
  }

  /**
   * 根据wxUnionId 取消绑定
   * 
   * @param wxUnionId
   */
  public void cancelBindByUnionId(String wxUnionId) {
    String hql = "delete from WeChatRelation w where w.weChatUnionId=:wx";
    super.createQuery(hql).setParameter("wx", StringUtils.trim(wxUnionId)).executeUpdate();
  }

  /**
   * 根据smate的openId取消绑定
   */
  public void cancelBindBySmateId(Long smateOpenId) {
    String hql = "delete from WeChatRelation w where w.smateOpenId=:smateOpenId";
    super.createQuery(hql).setParameter("smateOpenId", smateOpenId).executeUpdate();
  }

  /**
   * 获取SmateOpenId.
   * 
   * @param weChatOpenId
   * @return
   */
  @SuppressWarnings("unchecked")
  public Long getSmateOpenId(String weChatOpenId) {
    String hql = "select t.smateOpenId from WeChatRelation t where t.webChatOpenId=?";
    List<Long> smateOpenIds = super.createQuery(hql, weChatOpenId).list();
    if (smateOpenIds != null && smateOpenIds.size() > 0) {
      return smateOpenIds.get(0);
    }
    return null;
  }

  /**
   * 根据SmateOpenId 取数据.
   * 
   * @param weChatOpenId
   * @return
   */
  public WeChatRelation getBySmateOpenId(Long smateOpenId) {
    String hql = "from WeChatRelation t where t.smateOpenId=?";
    List list = super.createQuery(hql, smateOpenId).list();
    if (list != null && list.size() > 0) {
      return (WeChatRelation) list.get(0);
    }
    return null;
  }

  /**
   * 根据wxOpenId 取数据.
   * 
   * @param weChatOpenId
   * @return
   */
  public WeChatRelation getByWxOpenId(String wxOpenId) {
    String hql = "from WeChatRelation t where t.webChatOpenId=?";
    Object temp = super.createQuery(hql, wxOpenId).uniqueResult();
    if (temp == null) {
      return null;
    }
    return (WeChatRelation) temp;
  }

  /**
   * 根据UnionId 取数据.
   * 
   * @param UnionId
   * @return
   */
  public WeChatRelation getByUnionId(String UnionId) {
    String hql = "from WeChatRelation w where w.weChatUnionId=?";
    Object temp = super.createQuery(hql, UnionId).uniqueResult();
    if (temp == null) {
      return null;
    }
    return (WeChatRelation) temp;
  }

  /**
   * 检查是否有wxOpenId记录
   * 
   * @param wxOpenId
   * @return
   */
  public boolean checkWxOpenId(String wxOpenId) {
    String hql = "select w.id  from WeChatRelation w where w.webChatOpenId=:wx";
    Object obj = super.createQuery(hql).setParameter("wx", StringUtils.trim(wxOpenId)).uniqueResult();
    if (obj == null) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * 查询wxOpenId对应的psnid记录
   * 
   * @param wxOpenId
   * @return
   */
  public Long findSmateOpenId(String wxOpenId) {
    String hql = "select w.smateOpenId from WeChatRelation w where w.webChatOpenId=:wx";
    Long id = (Long) super.createQuery(hql).setParameter("wx", StringUtils.trim(wxOpenId)).uniqueResult();
    return id;
  }

  /**
   * 查询smateOpenId对应的wechatOpenId记录
   * 
   * @param wxOpenId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> findWeChatOpenIdList(Long smateOpenId) {
    String hql = "select w.webChatOpenId from WeChatRelation w where w.smateOpenId=:smate";
    List<String> ids = super.createQuery(hql).setParameter("smate", smateOpenId).list();
    return ids;
  }

  /**
   * 获取没有UnionId数据的openId集合
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> findWeChatNoUnionIdList() {
    String hql = "select t.webChatOpenId from WeChatRelation t where t.weChatUnionId = '0'";
    List<String> ids = super.createQuery(hql).list();
    return ids;
  }

  /**
   * 更新关系表unionId信息
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public void refreshUnionId(String openId, String unionId) {
    String hql = "update V_WECHAT_RELATION t set t.wechat_unionId =? where t.wechat_openId=?";
    super.update(hql, new Object[] {unionId, openId});
  }

  /**
   * 检查是否有wxUnionId记录
   * 
   * @param wxUnionId
   * @return
   */
  public boolean checkWxUnionId(String wxUnionId) {
    String hql = "select w.id  from WeChatRelation w where w.weChatUnionId=:wx";
    Object obj = super.createQuery(hql).setParameter("wx", StringUtils.trim(wxUnionId)).uniqueResult();
    if (obj == null) {
      return false;
    } else {
      return true;
    }
  }

  public Long findSmateOpenIdByPsnId(Long psnId) {
    String hql = "select t.openId  from OpenUserUnion t where  t.psnId =:psnId";
    List list = this.createQuery(hql).setParameter("psnId", psnId).list();
    if (list != null && list.size() > 0) {
      return (Long) list.get(0);
    }
    return null;
  }

  public Integer deleteBy(Long scmOpenId, String unionid) {
    String hql = "delete from WeChatRelation t where t.smateOpenId=:scmOpenId " + " and t.weChatUnionId=:unionid";
    return this.createQuery(hql).setParameter("scmOpenId", scmOpenId).setParameter("unionid", unionid).executeUpdate();

  }

  /**
   * 通过微信unionId查找关联的科研之友人员openId
   * 
   * @param wxUnionId
   * @return
   */
  public Long findSmateOpenIdByWxUnionId(String wxUnionId) {
    String hql = "select t.smateOpenId from WeChatRelation t where t.weChatUnionId = :wxUnionId";
    return (Long) super.createQuery(hql).setParameter("wxUnionId", wxUnionId).uniqueResult();
  }
}
