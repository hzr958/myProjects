package com.smate.web.management.dao.psn;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 第三方系统与SNS关联表Dao
 * 
 * 
 */
@Repository
public class OpenUserUnionDao extends SnsHibernateDao<OpenUserUnion, Long> {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 通过psnId获取第一条记录的openId
   * 
   * @Parameter psnId
   */
  @SuppressWarnings("unchecked")
  public Long getOpenIdByPsnId(Long psnId) throws Exception {
    try {
      String hql = "select t.openId from OpenUserUnion t where t.psnId = :psnId";
      List<Long> openIdList = new ArrayList<Long>();
      openIdList = super.createQuery(hql).setParameter("psnId", psnId).list();
      if (CollectionUtils.isEmpty(openIdList)) {
        return null;
      } else {
        return openIdList.get(0);
      }
    } catch (Exception e) {
      logger.error("根据psnId从数据库获取openId异常! psnId=" + psnId);
      throw new Exception(e);
    }
  }


}
