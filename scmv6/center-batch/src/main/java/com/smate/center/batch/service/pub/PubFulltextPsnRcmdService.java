package com.smate.center.batch.service.pub;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.DaoException;

/**
 * 成果全文人员推荐Service.
 * 
 * @author pwl
 * 
 */
public interface PubFulltextPsnRcmdService extends Serializable {

  public void updateStatusByPubId(Long pubId, Integer status);

  /**
   * 获取当前用户成果全文推荐总数（有可能会不准确，因为一条成果可能会多篇全文推荐记录，但是页面只显示一条）.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  public Long getRcmdFulltextCount();

  public Long getRcmdFulltextCount(Long psnId);

  /**
   * 删除成果对应的全文推荐记录.
   * 
   * @param pubId
   * @throws ServiceException
   */
  public void deletePubFulltextPsnRcmd(Long pubId);
}
