package com.smate.web.v8pub.service.sns.fulltextpsnrcmd;

import java.io.Serializable;

import com.smate.core.base.utils.exception.PubException;
import com.smate.core.base.utils.model.Page;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubFulltextPsnRcmd;
import com.smate.web.v8pub.service.BaseService;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

/**
 * 成果全文人员推荐Service.
 * 
 * @author yhx
 * 
 */
public interface PubFulltextPsnRcmdService extends Serializable, BaseService<Long, PubFulltextPsnRcmd> {

  /**
   * 获取成果详情页面的全文推荐
   * 
   * @param pubOperateVO
   * @throws ServiceException
   */
  public void getPubRcmdFulltext(PubOperateVO pubOperateVO) throws ServiceException;

  /**
   * 确认是这篇成果的全文
   * 
   * @param id
   * @throws ServiceException
   */
  public void confirmPubft(Long id, Long psnId) throws ServiceException;

  /**
   * 不是这篇成果的全文
   * 
   * @param id
   * @throws ServiceException
   */
  public void rejectPubft(Long id) throws ServiceException;

  /**
   * 获取当前用户成果全文推荐
   * 
   * @param pubOperateVO
   * @param page
   * @return
   * @throws ServiceException
   */
  public Page getRcmdFulltext(PubOperateVO pubOperateVO, Page page) throws ServiceException;

  /**
   * 获取全文认领数
   * 
   * @param currentUserId
   * @return
   * @throws PubException
   */
  public Long getFulltextCount(Long currentUserId) throws PubException;

  /**
   * 移动端-获取全文认领列表
   * 
   * @param form
   * @throws PubException
   */
  public void getPubFulltextList(PubOperateVO pubOperateVO) throws PubException;

  public PubFulltextPsnRcmd getPubFulltext(Long psnId, Long pubId);

  public void delePubFulltext(Long psnId, Long pubId);

  public Page getRcmdFulltextDetails(PubOperateVO pubOperateVO, Page page);

  /**
   * 根据snsPubId删除记录
   * 
   * @param srcPubId
   * @throws PubException
   */
  public void deleteBySnsPubId(Long srcPubId) throws PubException;

  /**
   * 获取全文推荐
   * 
   * @param pubId
   * @throws PubException
   */
  public PubFulltextPsnRcmd getPubFulltextPsnRcmd(Long pubId) throws PubException;



}
