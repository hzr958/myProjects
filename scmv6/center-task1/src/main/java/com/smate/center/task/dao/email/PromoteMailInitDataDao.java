package com.smate.center.task.dao.email;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.email.PromoteMailInitData;
import com.smate.core.base.utils.data.EmailSrvHibernateDao;

/**
 * 
 * 各节点初始数据表dao
 * 
 * @author zk
 * 
 */
@Repository
public class PromoteMailInitDataDao extends EmailSrvHibernateDao<PromoteMailInitData, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getDataByEmail(String email) {
    String hql =
        "select t.id from PromoteMailInitData t where dbms_lob.instr(t.mailData,:template) > 0 and t.toAddress = :email "
            + "and trunc( t.createDate) >= trunc(sysdate-7)";
    return super.createQuery(hql).setParameter("email", email)
        .setParameter("template", "Base_ResearchArea_FundChance_Recmd_zh_CN.ftl").list();
  }

}
