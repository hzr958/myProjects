package com.smate.web.v8pub.service.sns;

import java.util.List;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubCommentPO;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

/**
 * 成果评论接口
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
public interface PubCommentService {

  public List<PubCommentPO> findByPubId(Long pubId) throws ServiceException;

  public void saveOrUpdate(PubCommentPO entity) throws ServiceException;

  public void deleteById(Long id) throws ServiceException;

  public void commentOpt(PubOperateVO pubOperateVO) throws ServiceException;

  void updateSnsCommentStatistics(PubOperateVO pubOperateVO, Long snsPubId) throws ServiceException;

}
