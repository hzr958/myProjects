package com.smate.center.batch.service.pdwh.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.cnkipat.CnkiPatPubAddr;
import com.smate.center.batch.model.pdwh.pub.cnkipat.CnkiPatPubAssign;

/**
 * cnkipat成果匹配单位服务.
 * 
 * @author liqinghua
 * 
 */
public interface CnkiPatPubInsMatchService extends Serializable {

  /**
   * 匹配成果地址.
   * 
   * @param insId
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public Integer matchPubCache(Long insId, Long pubId) throws ServiceException;

  /**
   * 匹配成果地址.
   * 
   * @param insId
   * @param orgs
   * @return
   * @throws ServiceException
   */
  public Integer matchPubCache(Long insId, Long pubId, List<CnkiPatPubAddr> orgs) throws ServiceException;

  /**
   * 获取需要重新匹配的数据列表.
   * 
   * @param insId
   * @return
   */
  public List<CnkiPatPubAssign> getRematchMatchPub(Long startId) throws ServiceException;

  /**
   * 重新匹配单位成果.
   * 
   * @param pubAssign
   * @throws ServiceException
   */
  public void rematchInsPub(CnkiPatPubAssign pubAssign) throws ServiceException;
}
