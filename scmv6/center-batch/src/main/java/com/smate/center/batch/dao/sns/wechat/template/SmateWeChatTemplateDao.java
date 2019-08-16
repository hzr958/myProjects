package com.smate.center.batch.dao.sns.wechat.template;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.wechat.template.SmateWeChatTemplate;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * smate微信模板关系DAO.
 * 
 * @author xys
 *
 */
@Repository
public class SmateWeChatTemplateDao extends SnsHibernateDao<SmateWeChatTemplate, Long> {

  /**
   * 获取微信模板id.
   * 
   * @param smateTempId
   * @return
   */
  public String getWechatTempId(Long smateTempId) {
    return super.findUnique("select t.wechatTempId from SmateWeChatTemplate t where t.smateTempId=? and t.enabled='1'",
        smateTempId);
  }
}
