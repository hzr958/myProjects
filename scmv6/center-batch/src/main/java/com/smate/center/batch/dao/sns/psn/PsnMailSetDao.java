package com.smate.center.batch.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.psn.ConstMailType;
import com.smate.center.batch.model.psn.PsnMailSet;
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

  /**
   * 是否关闭了发送邮件的功能,关闭返回true，否则返回false
   * 
   * @param psnId 个人Id
   * @param mailTypeId 邮件类型Id
   * @return
   */
  public boolean isClosed(Long psnId, Long mailTypeId) {
    // mailTypeId == null || mailTypeId=-1 || psnId == null || psnId == 0 表示参数为非法或忽略本次查询，返回false
    if (psnId == null || psnId == 0 || mailTypeId == null || mailTypeId == -1) {
      return false;
    }
    String hql = "from PsnMailSet where psnId=? and mailTypeId=?";
    List lst = createQuery(hql, psnId, mailTypeId).list();
    PsnMailSet pms = lst.size() > 0 ? (PsnMailSet) lst.get(0) : null;
    if (pms == null || pms.getIsReceive().equals(0L)) {
      return true;
    } else {
      return false;
      // return pms.getIsReceive() > 0 ? false : true;
    }
  }

  /**
   * 根据模板id判断 收件人id 得到isreceive
   */
  public Long getIsreceive(Long receivePsnId, Long typeId) throws DaoException {
    String hql = "select t.isReceive from PsnMailSet t where t.psnId=:psnId and t.mailTypeId=:typeId ";
    Long isReceive =
        (Long) super.createQuery(hql).setParameter("psnId", receivePsnId).setParameter("typeId", typeId).uniqueResult();
    // 如果为空，为默认允许发送邮件
    if (isReceive == null) {
      isReceive = 1L;
    }
    return isReceive;
  }

}
