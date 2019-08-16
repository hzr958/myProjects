package com.smate.web.psn.dao.ins;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.ins.InsPsnCount;

/**
 * 个人工作、教育经历的机构信息统计
 * 
 * @author xiexing
 * @date 2019年1月24日
 */
@Repository
public class InsPsnCountDao extends SnsHibernateDao<InsPsnCount, Long> {
  /**
   * 根据名称查询当前机构
   * 
   * @param insName
   * @return
   */
  public InsPsnCount findByName(String insName) {
    return (InsPsnCount) getSession()
        .createQuery("select new InsPsnCount(insId,pinYin,firstLetter,historyPsnCount,updateDate,zhName,enName) "
            + "from InsPsnCount t where lower(t.zhName) = :insName or lower(t.enName) = :insName")
        .setParameter("insName", insName).uniqueResult();
  }

  /**
   * 根据名称去更新当前机构数
   * 
   * @param insName
   */
  public void updateIns(String insName, Integer historyPsnCount) {
    String HQL =
        "update InsPsnCount t set t.historyPsnCount = :historyPsnCount where lower(t.zhName) = :insName or lower(t.enName) = :insName";
    getSession().createQuery(HQL).setParameter("insName", insName).setParameter("historyPsnCount", historyPsnCount)
        .executeUpdate();
  }
}
