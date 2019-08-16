package com.smate.web.v8pub.service.sns;

import com.smate.web.v8pub.enums.PubLikeStatusEnum;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubLikePO;
import com.smate.web.v8pub.service.BaseService;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

/**
 * 成果赞service
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
public interface PubLikeService extends BaseService<Long, PubLikePO> {

  public void updatePubLike(Long pubId, Long psnId, PubLikeStatusEnum likeStauts) throws ServiceException;

  public void likeOpt(PubOperateVO pubOperateVO) throws ServiceException;

  /**
   * 查询人员对某个成果的赞的状态
   * 
   * @param psnId
   * @param pubId
   * @throws ServiceException
   */
  boolean checkHasAwardPub(Long psnId, Long pubId) throws ServiceException;

  /**
   * 更新统计数
   * 
   * @param pubOperateVO
   * @param pubId
   * @param psnId
   * @throws ServiceException
   */
  public void updateSnsLikeStatistics(PubOperateVO pubOperateVO, Long pubId, Long psnId) throws ServiceException;

  /**
   * 根据pubId和psnId获取成果赞记录
   * 
   * @param pubId
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public PubLikePO findByPubIdAndPsnId(Long pubId, Long psnId) throws ServiceException;

}
