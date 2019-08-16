package com.smate.web.psn.service.profile;

import java.io.Serializable;
import java.util.List;

import com.smate.web.psn.exception.PsnException;

/**
 * 研究领域关键词认同服务接口.
 * 
 * @author zyx
 * 
 */

public interface KeywordIdentificationService extends Serializable {

  /**
   * 记录被用户删除的推荐关键词
   */
  public void recordDropRmKeyword(Long psnId, String keyword) throws PsnException;

  /**
   * 认同研究领域
   * 
   * @param psnId 被认同的人员ID
   * @param keywordId 被认同的关键词ID
   * @param friendId 认同者
   * @throws ServiceException
   */
  void identificKeyword(Long psnId, Long keywordId, Long friendId) throws PsnException;

  /**
   * 查找多个认同统计数
   * 
   */
  List<Object[]> countIdentification(Long psnId, List<Long> kwIdList) throws PsnException;

  /**
   * 查询单个关键词认同数
   */
  Long countOneIdentification(Long psnId, Long kwId) throws PsnException;
}
