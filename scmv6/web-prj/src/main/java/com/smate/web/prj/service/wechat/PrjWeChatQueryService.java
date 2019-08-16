package com.smate.web.prj.service.wechat;

import com.smate.core.base.exception.ServiceException;
import com.smate.web.prj.exception.PrjException;
import com.smate.web.prj.form.wechat.PrjWeChatForm;

/**
 * 项目-WeChat查询接口
 * 
 * @author tj
 * @since 6.0.1
 */
public interface PrjWeChatQueryService {

  /**
   * 查询项目
   * 
   * @param form
   * @throws PrjException
   */
  void queryPrjForWeChat(PrjWeChatForm form) throws PrjException;

  void queryPrjXml(PrjWeChatForm form) throws PrjException;

  void appHandlePrjStatistics(PrjWeChatForm form) throws Exception;

  boolean hasPrivatePrj(Long psnId) throws Exception;

  /**
   * 查询统统计数和评论
   * 
   * @param form
   * @throws Exception
   */
  void queryStaticAndcomment(PrjWeChatForm form) throws Exception;

  /**
   * 查询隐私项目数量
   * 
   * @param searchPsnId
   * @return
   * @throws ServiceException
   */
  Long queryPrivatePrjCount(Long searchPsnId) throws ServiceException;

}
