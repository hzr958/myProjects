package com.smate.web.dyn.dao.dynamic.group;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.BaseMongoDAO;
import com.smate.core.base.utils.exception.DynException;
import com.smate.web.dyn.model.mongodb.dynamic.group.GroupDynamicContent;

/**
 * 动态内容 dao
 * 
 * @author tsz
 *
 */
@Repository
public class GroupDynamicContentDao extends BaseMongoDAO<GroupDynamicContent> {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 获取动态内容
   * 
   * @author zzx
   * @param dynId
   * @param locale
   * @return
   * @throws DynException
   */
  public String getDynContent(Long dynId, String locale) {
    try {
      GroupDynamicContent groupDynamicContent = super.findById(dynId.toString());
      if (groupDynamicContent == null) {
        return null;
      }
      if ("en_US".equals(locale)) {
        return StringUtils.isBlank(groupDynamicContent.getDynContentEn()) == true
            ? groupDynamicContent.getDynContentZh()
            : groupDynamicContent.getDynContentEn();
      } else {
        return StringUtils.isBlank(groupDynamicContent.getDynContentZh()) == true
            ? groupDynamicContent.getDynContentEn()
            : groupDynamicContent.getDynContentZh();
      }
    } catch (Throwable e) {
      logger.error("获取群组获取动态内容出错,dynId=" + dynId, e);
      return null;
    }
  }


  /**
   * 获取json格式的动态信息
   * 
   * @param dynId
   * @return
   */
  public String getJsonDynInfo(Long dynId) {
    GroupDynamicContent groupDynamicContent = super.findById(dynId.toString());
    return groupDynamicContent != null ? groupDynamicContent.getResDetails() : "";
  }

}
