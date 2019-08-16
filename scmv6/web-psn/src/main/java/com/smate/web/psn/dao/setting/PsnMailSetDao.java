package com.smate.web.psn.dao.setting;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.setting.ConstMailType;
import com.smate.web.psn.model.setting.PsnMailSet;

/**
 * 邮件发送设置
 * 
 * @author YPH
 * 
 */
@Repository
public class PsnMailSetDao extends SnsHibernateDao<PsnMailSet, Long> {
  /**
   * 获取邮件类型列表 , 界面上有的
   * 
   * @return
   */
  public List<ConstMailType> getMailTypeList() {
    String hql = "from ConstMailType t where    to_number(t.remark ) > 0 and t.status=1 order by t.mailTypeId";
    return this.createQuery(hql).list();
  }

  /**
   * 根据个人Id删除个人的邮件设置
   */
  public int removes(Long psnId) {
    String hql =
        "delete from PsnMailSet where psnId=? and mailTypeId in ( select t.mailTypeId from ConstMailType t where    to_number(t.remark ) > 0 and t.status=1   ) ";
    return createQuery(hql, psnId).executeUpdate();
  }

  /**
   * 
   * @return
   */
  public List<PsnMailSet> getPsnMailSetListByPsnId(Long psnId) {
    String hql = " from PsnMailSet p where p.psnId =:psnId ";
    return this.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 
   * @param psnId
   * @param mailTypeId
   * @return
   */
  public PsnMailSet getByPsnIdAndMailTypeId(Long psnId, Long mailTypeId) {
    String hql = " from PsnMailSet p where p.psnId =:psnId  and p.mailTypeId=:mailTypeId ";
    return (PsnMailSet) this.createQuery(hql).setParameter("psnId", psnId).setParameter("mailTypeId", mailTypeId)
        .uniqueResult();
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
   * 根据模板名字 收件地址，修改人员邮件设置表信息
   * 
   * @param toAddress
   */
  public void psnMailSet(Long psnId, Long typeId) {

    String sql = " update PsnMailSet set isReceive=0 where psnId=? and mailTypeId=?";
    super.createQuery(sql, new Object[] {psnId, typeId}).executeUpdate();
  }

  /**
   * 根据psnid得到email
   */

  public Object getEmailByPsnId(Long psnId) {
    String hql = "select t.email From Person t where t.personId=?";

    return super.createQuery(hql, new Object[] {psnId}).uniqueResult();
  }

}
