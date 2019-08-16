package com.smate.web.v8pub.service.pdwh;

import java.util.List;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubCommentPO;
import com.smate.web.v8pub.vo.pdwh.PdwhPubOperateVO;

/**
 * 基准库成果评论服务接口
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
public interface PdwhPubCommentService {

  public List<PdwhPubCommentPO> findByPubId(Long pubId) throws ServiceException;

  public void saveOrUpdate(PdwhPubCommentPO entity) throws ServiceException;

  public void deleteById(Long id) throws ServiceException;

  public void pdwhCommentOpt(PdwhPubOperateVO pdwhPubOperateVO) throws ServiceException;

  public void pdwhComment(PdwhPubOperateVO pdwhPubOperateVO) throws ServiceException;
}
