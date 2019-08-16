package com.smate.center.open.dao.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.consts.ConstMailType;
import com.smate.center.open.model.psn.PsnMailSet;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 邮件发送设置
 * 
 * @author YPH
 * 
 */
@Repository
public class PsnMailSetDao extends SnsHibernateDao<PsnMailSet, Long> {
  /**
   * 新增或修改个人邮件设置
   * 
   * @param psnMailSet
   */
  public void saveOrUpdate(PsnMailSet psnMailSet) {
    this.save(psnMailSet);
  }

  /**
   * 根据id修改邮件接受与否
   * 
   * @param psnId
   * @param mailTypeId
   * @param value
   */
  public int updateisReceive(Long psnId, Long mailTypeId, Long value) {
    String hql = "update PsnMailSet set isReceive=? where psnId=? and mailTypeId=?  ";
    return createQuery(hql, new Object[] {value, psnId, mailTypeId}).executeUpdate();
  }



  /**
   * 根据个人Id删除个人的邮件设置
   */
  public int removes(Long psnId) {
    String hql = "delete from PsnMailSet where psnId=?";
    return createQuery(hql, psnId).executeUpdate();
  }

  /**
   * 根据个人Id获取该人的邮件设置信息列表
   * 
   * @param psnId
   * @return
   */
  public List<PsnMailSet> list(Long psnId) {
    String hql = "from PsnMailSet where psnId=? and isReceive=1";
    return this.createQuery(hql, psnId).list();
  }

  /**
   * 获取邮件类型列表
   * 
   * @return
   */
  public List<ConstMailType> getMailTypeList() {
    String hql = "from ConstMailType t where t.status=1 order by t.mailTypeId";
    return this.createQuery(hql).list();
  }



}
