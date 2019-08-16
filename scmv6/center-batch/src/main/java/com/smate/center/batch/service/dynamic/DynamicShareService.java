package com.smate.center.batch.service.dynamic;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.dynamic.DynamicSharePsn;

/**
 * 分享接口.
 * 
 * @author chenxiangrong
 * 
 */
public interface DynamicShareService {
  /**
   * 获取资源的分享次数.
   * 
   * @param resId
   * @param resType
   * @param resNode
   * @return
   * @throws ServiceException
   */
  Long getResShareTimes(Long resId, int resType, int resNode) throws ServiceException;

  /**
   * 批量获取分享次数.
   * 
   * @param jsonParam
   * @return
   * @throws ServiceException
   */
  String getBatchShareCountByIds(String jsonParam) throws ServiceException;

  /**
   * 获取分享记录详情.
   * 
   * @param resType
   * @param resNode
   * @param resId
   * @return
   * @throws ServiceException
   */
  List<DynamicSharePsn> getShareRecordByPsn(int resType, int resNode, Long resId) throws ServiceException;

  /**
   * 分享ext类型资源.
   * 
   * @param jsonParam
   * @param dynFlag
   * @throws ServiceException
   */
  void shareExt(String jsonParam, int dynFlag, String receivers) throws ServiceException;

  /**
   * 分享ext类型基准库资源.
   * 
   * @param jsonParam
   * @param dynFlag
   * @param receivers
   * @throws ServiceException
   */
  void shareExtPdwh(String jsonParam, int dynFlag) throws ServiceException;

  /**
   * 分享运用.
   * 
   * @param jsonParam
   * @param dynFlag
   * @throws ServiceException
   */
  void shareApp(String jsonParam, int dynFlag, String receivers) throws ServiceException;

  /**
   * 分享群组.
   * 
   * @param jsonParam
   * @param dynFlag
   * @throws ServiceException
   */
  void shareGroup(String jsonParam, int dynFlag, String receivers) throws ServiceException;

  /**
   * 分享简历.
   * 
   * @param jsonParam
   * @param dynFlag
   * @throws ServiceException
   */
  void shareResume(String jsonParam, int dynFlag, String receivers) throws ServiceException;

  /**
   * 分享到群组.
   * 
   * @param jsonParam
   * @throws ServiceException
   */
  void shareToGroup(String jsonParam) throws ServiceException;

  /**
   * 同步分享记录.
   * 
   * @param jsonParam
   * @param sharerPsnId
   * @throws ServiceException
   */
  void syncShareRes(String jsonParam, Long sharerPsnId) throws ServiceException;

  /**
   * 转发式分享.
   * 
   * @param jsonParam
   * @param dynFlag
   * @throws ServiceException
   */
  void forwardShare(String jsonParam) throws ServiceException;

  /**
   * 获取分享显示的内容.
   * 
   * @param form
   * @return
   * @throws ServiceException
   */
  // String getShareTxt(DynamicForm form) throws ServiceException;

  /**
   * scm-7819 ajax添加分享记录 ，只要点击了分享到站外的，无论分享是否成功，都将分享计数加1
   * 
   * @param jsonParam
   * @param dynFlag
   * @param receivers
   * @throws ServiceException
   */
  void ajaxAddResShareCounts(String jsonParam, int dynFlag, String receivers) throws ServiceException;

}
