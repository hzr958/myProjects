package com.smate.center.batch.service.pdwh.isipub;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubAddr;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubAddrExc;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubAssign;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubMaddr;
import com.smate.core.base.utils.model.Page;


/**
 * isi成果匹配单位服务.
 * 
 * @author liqinghua
 * 
 */
public interface IsiPubInsMatchService extends Serializable {

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
  public Integer matchPubCache(Long insId, Long pubId, List<IsiPubAddr> orgs) throws ServiceException;

  /**
   * 查询待确认机构别名列表.
   * 
   * @param insIds
   * @param page
   * @return
   * @throws ServiceException
   */
  public Page<Object> pubStatistics(List<Long> insIds, Page<Object> page) throws ServiceException;

  /**
   * 加载匹配成果结果XML_ID列表.
   * 
   * @param matched
   * @param insId
   * @param page
   */
  public Page<Object> loadInsPubAssign(Integer matched, Long insId, Page<Object> page) throws ServiceException;

  /**
   * 加载指定XMLID，匹配状态的成果地址列表.
   * 
   * @param matched
   * @param xmlIds
   * @return
   * @throws ServiceException
   */
  public List<IsiPubMaddr> loadOrgs(Integer matched, Set<Long> pubIds, Long insId) throws ServiceException;

  /**
   * 加载指定XMLID，匹配状态的成果地址列表.
   * 
   * @param matched
   * @param xmlIds
   * @return
   * @throws ServiceException
   */
  public List<IsiPubMaddr> loadOrgs(List<Integer> matched, Set<Long> pubIds, Long insId) throws ServiceException;

  /**
   * 加载标题以及作者.
   * 
   * @param pubIds
   * @return
   * @throws ServiceException
   */
  public Map<Long, Map<String, Object>> loadPubTitleAuthor(List<Object> pubIds) throws ServiceException;

  /**
   * 获取地址匹配信息.
   * 
   * @param maddrId
   * @return
   * @throws ServiceException
   */
  public IsiPubMaddr getIsiPubMaddr(Long maddrId) throws ServiceException;

  /**
   * bpo重新设置匹配结果.
   * 
   * @param maddr
   * @throws ServiceException
   */
  public void saveResetIsiPubMaddr(IsiPubMaddr maddr) throws ServiceException;

  /**
   * 保存排除单位地址.
   * 
   * @param insId
   * @param addr
   * @return
   * @throws ServiceException
   */
  public IsiPubAddrExc saveIsiPubAddrExc(Long insId, String addr) throws ServiceException;

  /**
   * 修改匹配结果.
   * 
   * @param insId
   * @param pubId
   * @param matched
   * @throws ServiceException
   */
  public void updatePubAssignResult(Long insId, Long pubId, Integer matched) throws ServiceException;

  /**
   * 获取需要重新匹配的数据列表(2部分匹配上机构，3不确认是否是机构成果).
   * 
   * @param insId
   * @return
   * @throws ServiceException
   */
  public List<IsiPubAssign> getNeedMatchPub(Long insId) throws ServiceException;

  /**
   * 重新匹配单位成果.
   * 
   * @param pubAssign
   * @throws ServiceException
   */
  public void rematchInsPub(IsiPubAssign pubAssign) throws ServiceException;

  /**
   * 获取需要重新匹配的数据列表.
   * 
   * @param insId
   * @return
   */
  public List<IsiPubAssign> getRematchMatchPub(Long startId) throws ServiceException;

}
