package com.smate.web.v8pub.service.pdwh;

import com.smate.web.v8pub.enums.PubLikeStatusEnum;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.vo.pdwh.PdwhPubOperateVO;

/**
 * 基准库成赞服务接口
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
public interface PdwhPubLikeService {

  public void updatePubLike(Long pubId, Long psnId, PubLikeStatusEnum likeStauts) throws ServiceException;

  public void pdwhLikeOpt(PdwhPubOperateVO pdwhPubOperateVO) throws ServiceException;

  /**
   * 是否已赞过该基准库成果
   * 
   * @param pdwhPubOperateVO
   * @return true:已赞过，false:未赞过
   * @throws ServiceException
   */
  public boolean checkHasAwardPdwhPub(PdwhPubOperateVO pdwhPubOperateVO) throws ServiceException;

  public int pdwhLike(PdwhPubOperateVO pdwhPubOperateVO) throws ServiceException;
}
