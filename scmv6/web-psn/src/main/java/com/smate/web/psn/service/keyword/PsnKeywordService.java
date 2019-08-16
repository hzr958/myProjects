package com.smate.web.psn.service.keyword;

import java.util.List;

import com.smate.core.base.psn.dto.profile.KeywordIdentificationDTO;
import com.smate.web.psn.exception.PsnException;

/**
 * 个人关键字（研究领域 ）服务接口
 * 
 * @author tsz
 *
 */
public interface PsnKeywordService {

  /**
   * 根据人员id 获取个人关键字
   * 
   * 按统计数排序 ，需要封装认同统计数 认同人员列表(预览列表)
   * 
   * @param psnId
   * @param resultSize 返回结果个数
   * @return
   * @throws PsnException
   */
  public List<KeywordIdentificationDTO> getPsnKeyWord(Long psnId, Integer resultSize) throws PsnException;
}
