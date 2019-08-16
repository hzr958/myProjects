package com.smate.center.batch.service.rol.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubFundPrjRefresh;

/**
 * 成果项目信息.
 * 
 * @author liqinghua
 * 
 */
public interface PubFundInfoRolService extends Serializable {

  /**
   * 保存成果项目信息.
   * 
   * @param pubId
   * @param insId
   * @param fundInfo
   * @throws ServiceException
   */
  public void savePubFundInfo(Long pubId, Long insId, String fundInfo) throws ServiceException;

  /**
   * 删除成果项目信息.
   * 
   * @param pubId
   * @throws ServiceException
   */
  public void removePubFundInfo(Long pubId) throws ServiceException;

  /**
   * 解析成果XML中的项目信息.
   * 
   * @param pubId
   * @throws Exception
   */
  public void prasePubFundInfo(Long pubId) throws Exception;

  /**
   * 获取需要刷新成果关联项目的成果列表.
   * 
   * @return
   * @throws ServiceException
   */
  public List<PubFundPrjRefresh> loadRefreshList() throws ServiceException;

  /**
   * 标注刷新成果关联项目失败.
   * 
   * @param pubId
   */
  public void markRefreshError(Long pubId) throws ServiceException;

  /**
   * 删除刷新成果关联项目信息.
   * 
   * @param pubId
   */
  public void removeRefresh(Long pubId) throws ServiceException;

  /**
   * 刷新成果中的项目信息关联项目信息.
   * 
   * @param pubId
   * @param insId
   * @throws ServiceException
   */
  public void refreshPubFundPrj(Long pubId, Long insId) throws ServiceException;
}
