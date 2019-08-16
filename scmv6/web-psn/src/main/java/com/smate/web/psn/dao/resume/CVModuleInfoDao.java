package com.smate.web.psn.dao.resume;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.newresume.CVModuleInfo;

/**
 * 简历模块信息DAO
 * 
 * @author wsn
 *
 */
@Repository
public class CVModuleInfoDao extends SnsHibernateDao<CVModuleInfo, Long> {

  public String getModuleInfo(Long moduleId) {
    String hql = "select t.moduleInfo from CVModuleInfo t where t.id = :moduleId";
    return (String) super.createQuery(hql).setParameter("moduleId", moduleId).uniqueResult();
  }

  /**
   * 查找下一个主键值
   * 
   * @return
   */
  public Long findNextCVModuleInfoId() {
    BigDecimal CVInfoId = (BigDecimal) super.getSession()
        .createSQLQuery("select SEQ_RESUME_MODULE_INFO.nextval from dual").uniqueResult();
    return CVInfoId.longValue();
  }

  /**
   * 删除简历所有的模块信息
   * 
   * @param cvId
   * @param psnId
   */
  public void deleteAllCVModuleInfoByCvId(Long cvId) {
    String hql =
        "delete from CVModuleInfo t where t.id in(select t.moduleInfoId from ResumeModule t where t.resumeId = :cvId )";
    super.createQuery(hql).setParameter("cvId", cvId).executeUpdate();
  }

  /**
   * 插入新记录
   * 
   * @param info
   */
  @SuppressWarnings("unchecked")
  public void insertNewRecord(CVModuleInfo info) {
    String hql = "insert into V_CV_MODULE_INFO values(?,?)";
    super.update(hql, new Object[] {info.getId(), info.getModuleInfo()});
  }

}
