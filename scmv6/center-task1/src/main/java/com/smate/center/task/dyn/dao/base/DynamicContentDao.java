package com.smate.center.task.dyn.dao.base;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.dyn.model.base.DynamicContent;
import com.smate.core.base.utils.data.BaseMongoDAO;
import com.smate.core.base.utils.exception.DynException;

/**
 * 动态内容Dao
 * 
 * @author zk
 *
 */
@Repository
public class DynamicContentDao extends BaseMongoDAO<DynamicContent> {

  /**
   * 获取动态内容
   * 
   * @param dynId
   * @param locale
   * @return
   * @throws DynException
   */
  public String getDynContent(Long dynId, String locale) {
    try {
      DynamicContent dynamicContent = super.findById(dynId.toString());
      if (dynamicContent == null) {
        return null;
      }
      if ("en_US".equals(locale)) {
        return StringUtils.isBlank(dynamicContent.getDynContentEn()) == true ? dynamicContent.getDynContentZh()
            : dynamicContent.getDynContentEn();
      } else {
        return StringUtils.isBlank(dynamicContent.getDynContentZh()) == true ? dynamicContent.getDynContentEn()
            : dynamicContent.getDynContentZh();
      }
    } catch (Throwable e) {
      return null;
    }

  }

  /**
   * 获取动态信息
   * 
   * @param dynId
   * @return
   */
  public String getDynInfo(Long dynId) {
    String resDetails = null;
    DynamicContent dynamicContent = super.findById(dynId.toString(), DynamicContent.class);
    if (dynamicContent != null) {
      resDetails = dynamicContent.getResDetails();
    }
    return resDetails;
  }



}
