package com.smate.center.batch.service.pdwh.pubmatch;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.cnipr.CniprInsName;

/**
 * 单位Cnipr别名数据匹配服务.
 * 
 * @author liqinghua
 * 
 */
public interface CniprInsNameMatchService extends Serializable {

  /**
   * 传入成果地址，进行单位别名匹配.
   * 
   * @param pubAddr 成果地址，传入前必须转成小写.
   * @return
   * @throws ServiceException
   */
  public List<CniprInsName> cniprNameMatch(String pubAddr) throws ServiceException;

  /**
   * 匹配成果原始地址.
   * 
   * @param pubAddrs
   * @param insId
   * @return
   * @throws ServiceException
   */
  public boolean cniprNameMatchProtoAddr(String pubAddrs, Long insId) throws ServiceException;

  /**
   * 传入成果地址，进行单位别名匹配.
   * 
   * @param pubAddr 成果地址，传入前必须转成小写.
   * @return
   * @throws ServiceException
   */
  public CniprInsName cniprNameMatch(String pubAddr, Long insId) throws ServiceException;

  /**
   * 获取匹配上其他机构别名.
   * 
   * @param pubAddr
   * @param excInsId
   * @return
   * @throws ServiceException
   */
  public CniprInsName cniprNameMatchOther(String pubAddr, Long excInsId) throws ServiceException;

  /**
   * 匹配所有单位别名，顺序：先匹配本机构，再匹配其他机构.
   * 
   * @param pubAddr
   * @param insId
   * @return
   * @throws ServiceException
   */
  public CniprInsName cniprNameMatchAll(String pubAddr, Long insId) throws ServiceException;

  /**
   * 获取所有的单位cnipr别名数据.
   * 
   * @return
   * @throws ServiceException
   */
  public List<CniprInsName> getAllCniprInsName() throws ServiceException;

  /**
   * 获取单位Cnipr别名数据.
   * 
   * @return
   * @throws ServiceException
   */
  public List<CniprInsName> getCniprInsName(Long insId) throws ServiceException;

  /**
   * 创建cnipr别名.
   * 
   * @param insId
   * @param insName
   * @return
   * @throws ServiceException
   */
  public CniprInsName saveCniprInsName(Long insId, String insName) throws ServiceException;

  /**
   * 同步cnipr别名.
   * 
   * @param insName
   * @throws ServiceException
   */
  public void syncCniprInsName(CniprInsName insName) throws ServiceException;

  /**
   * 服务器启动的时候，初始化机构别名.
   * 
   * @throws ServiceException
   */
  public void initCniprInsName() throws ServiceException;
}
