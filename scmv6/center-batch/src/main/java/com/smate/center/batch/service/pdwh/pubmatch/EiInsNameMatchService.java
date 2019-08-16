package com.smate.center.batch.service.pdwh.pubmatch;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.PubRegionExclude;
import com.smate.center.batch.model.pdwh.pub.ei.EiInsName;
import com.smate.center.batch.model.pdwh.pub.ei.EiPubAddrExc;


/**
 * 单位ISI别名数据匹配服务.
 * 
 * @author liqinghua
 * 
 */
public interface EiInsNameMatchService extends Serializable {

  /**
   * 匹配所有单位别名，顺序：先匹配本机构，再匹配其他机构.
   * 
   * @param pubAddr
   * @param currentInsId
   * @return
   * @throws ServiceException
   */
  public EiInsName eiNameMatchAll(String pubAddr, Long currentInsId) throws ServiceException;

  /**
   * 匹配成果原始地址，需要过滤成果作者等.
   * 
   * @param pubAddrs
   * @param insId
   * @return
   * @throws ServiceException
   */
  public boolean eiNameMatchProtoAddr(String pubAddrs, Long insId) throws ServiceException;

  /**
   * 传入成果地址，进行单位别名匹配.
   * 
   * @param pubAddr 成果地址，传入前必须转成小写.
   * @return
   * @throws ServiceException
   */
  public EiInsName eiNameMatch(String pubAddr, Long insId) throws ServiceException;

  /**
   * 获取部分匹配上成果地址的单位别名.
   * 
   * @param pubAddr
   * @param insId
   * @return
   * @throws ServiceException
   */
  public Map<String, Object> eiNameMatchPart(String pubAddr, Long insId) throws ServiceException;

  /**
   * 获取匹配上其他机构别名.
   * 
   * @param pubAddr
   * @param excInsId
   * @return
   * @throws ServiceException
   */
  public EiInsName eiNameMatchOther(String pubAddr, Long excInsId) throws ServiceException;

  /**
   * 匹配排除的机构别名.
   * 
   * @param insId
   * @param addr
   * @return
   * @throws ServiceException
   */
  public EiPubAddrExc matchExcAddr(Long insId, String addr) throws ServiceException;

  /**
   * 是否匹配上国外机构名.
   * 
   * @param pubAddr
   * @return
   * @throws ServiceException
   */
  public boolean matchedExcCtyName(String pubAddr, Long insId) throws ServiceException;

  /**
   * 获取匹配排除国外名称.
   * 
   * @return
   * @throws ServiceException
   */
  public List<PubRegionExclude> getRegionExclude() throws ServiceException;

  /**
   * 获取匹配单位所在国家名称.
   * 
   * @return
   * @throws ServiceException
   */
  public List<String> getRegionInclude(Long insId) throws ServiceException;

  /**
   * 获取需要BPO确认的机构别名.
   * 
   * @param pubAddr
   * @param insId
   * @return
   * @throws ServiceException
   */
  @Deprecated
  public Map<String, Object> getNeedComfirmEiName(String pubAddr, Long insId) throws ServiceException;

  /**
   * 获取单位Ei别名数据.
   * 
   * @return
   * @throws ServiceException
   */
  public List<EiInsName> getEiInsName(Long insId) throws ServiceException;

  /**
   * 保存Ei机构别名,BPO调用.
   * 
   * @param insId
   * @param insName
   * @throws ServiceException
   */
  public EiInsName saveEiInsName(Long insId, String insName) throws ServiceException;

  /**
   * 接收同步Ei机构别名，BPO->ROL.
   * 
   * @param insName
   * @throws ServiceException
   */
  public void syncEiInsName(EiInsName insName) throws ServiceException;

  /**
   * 服务器启动的时候，初始化机构别名.
   * 
   * @throws ServiceException
   */
  public void initEiInsName() throws ServiceException;
}
