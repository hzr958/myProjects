package com.smate.web.psn.model.dynamic;



import com.smate.web.psn.exception.ServiceException;

/**
 * 动态处理接口.
 * 
 * @author chenxiangrong
 * 
 */
public interface DynamicHandlerService {



  /**
   * 取消关注.
   * 
   * @param psnId
   * @throws ServiceException
   */
  void minusAttentionVisible(Long psnId) throws ServiceException;
  /**
   * 取消关注.
   * 不是删除好友
   * @param psnId
   * @throws ServiceException
   */
  void  cancleAttentionVisible(Long psnId) throws ServiceException;

  /**
   * 添加关注.
   * 
   * @param psnId
   * @throws ServiceException
   */
  void addAttentionVisible(Long psnId) throws ServiceException;

  public void addAttentionVisible(Long currentPsnId  , Long psnId) throws ServiceException ;

}
