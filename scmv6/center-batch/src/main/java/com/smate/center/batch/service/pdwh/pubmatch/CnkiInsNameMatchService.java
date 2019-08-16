package com.smate.center.batch.service.pdwh.pubmatch;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiInsName;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiInsNameReg;

/**
 * 单位Cnki别名数据匹配服务.
 * 
 * @author liqinghua
 * 
 */
public interface CnkiInsNameMatchService extends Serializable {

  /**
   * 传入成果地址，进行单位别名匹配.
   * 
   * @param pubAddr 成果地址，传入前必须转成小写.
   * @return
   * @throws ServiceException
   */
  public List<CnkiInsName> cnkiNameMatch(String pubAddr) throws ServiceException;

  /**
   * 匹配成果原始地址.
   * 
   * @param pubAddrs
   * @param insId
   * @return
   * @throws ServiceException
   */
  public boolean cnkiNameMatchProtoAddr(String pubAddrs, Long insId) throws ServiceException;

  /**
   * 传入成果地址，进行单位别名匹配.
   * 
   * @param pubAddr 成果地址，传入前必须转成小写.
   * @return
   * @throws ServiceException
   */
  public CnkiInsName cnkiNameMatch(String pubAddr, Long insId) throws ServiceException;

  /**
   * 获取匹配上其他机构别名.
   * 
   * @param pubAddr
   * @param excInsId
   * @return
   * @throws ServiceException
   */
  public CnkiInsName cnkiNameMatchOther(String pubAddr, Long excInsId) throws ServiceException;

  /**
   * 匹配所有单位别名，顺序：先匹配本机构，再匹配其他机构.
   * 
   * @param pubAddr
   * @param insId
   * @return
   * @throws ServiceException
   */
  public CnkiInsName cnkiNameMatchAll(String pubAddr, Long insId) throws ServiceException;

  /**
   * 获取所有的单位CNKI别名正则数据.
   * 
   * @return
   * @throws ServiceException
   */
  public List<CnkiInsNameReg> getAllCnkiInsNameReg() throws ServiceException;

  /**
   * 获取所有的单位CNKI别名数据.
   * 
   * @return
   * @throws ServiceException
   */
  public List<CnkiInsName> getAllCnkiInsName() throws ServiceException;

  /**
   * 获取单位CNKI别名数据.
   * 
   * @return
   * @throws ServiceException
   */
  public List<CnkiInsNameReg> getCnkiInsNameReg(Long insId) throws ServiceException;

  /**
   * 获取单位Cnki别名数据.
   * 
   * @return
   * @throws ServiceException
   */
  public List<CnkiInsName> getCnkiInsName(Long insId) throws ServiceException;

  /**
   * 创建cnki别名.
   * 
   * @param insId
   * @param insName
   * @return
   * @throws ServiceException
   */
  public CnkiInsName saveCnkiInsName(Long insId, String insName) throws ServiceException;

  /**
   * 同步CNKI别名.
   * 
   * @param insName
   * @throws ServiceException
   */
  public void syncCnkiInsName(CnkiInsName insName) throws ServiceException;

  /**
   * 服务器启动的时候，初始化机构别名.
   * 
   * @throws ServiceException
   */
  public void initCnkiInsName() throws ServiceException;
}
