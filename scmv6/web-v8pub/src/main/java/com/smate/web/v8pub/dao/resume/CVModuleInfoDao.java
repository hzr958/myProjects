package com.smate.web.v8pub.dao.resume;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.vo.sns.newresume.CVModuleInfo;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * 简历模块信息DAO
 * 
 * @author wsn
 *
 */
@Repository
public class CVModuleInfoDao extends SnsHibernateDao<CVModuleInfo, Long> {

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
}
