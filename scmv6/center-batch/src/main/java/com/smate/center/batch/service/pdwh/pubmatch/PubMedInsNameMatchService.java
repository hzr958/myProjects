package com.smate.center.batch.service.pdwh.pubmatch;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.pubmed.PubmedInsName;

/**
 * 单位PubMed别名数据匹配服务.
 * 
 * @author liqinghua
 * 
 */
public interface PubMedInsNameMatchService extends Serializable {

  /**
   * 匹配所有单位别名，顺序：先匹配本机构，再匹配其他机构.
   * 
   * @param pubAddr
   * @param currentInsId
   * @return
   * @throws ServiceException
   */
  public PubmedInsName pubMedNameMatchAll(String pubAddr, Long currentInsId) throws ServiceException;

  /**
   * 匹配成果原始地址，需要过滤成果作者等.
   * 
   * @param pubAddrs
   * @param insId
   * @return
   * @throws ServiceException
   */
  public boolean pubMedNameMatchProtoAddr(String pubAddrs, Long insId) throws ServiceException;

  /**
   * 传入成果地址，进行单位别名匹配.
   * 
   * @param pubAddr 成果地址，传入前必须转成小写.
   * @return
   * @throws ServiceException
   */
  public PubmedInsName pubMedNameMatch(String pubAddr, Long insId) throws ServiceException;

  /**
   * 获取部分匹配上成果地址的单位别名.
   * 
   * @param pubAddr
   * @param insId
   * @return
   * @throws ServiceException
   */
  public Map<String, Object> pubMedNameMatchPart(String pubAddr, Long insId) throws ServiceException;

  /**
   * 获取匹配上其他机构别名.
   * 
   * @param pubAddr
   * @param excInsId
   * @return
   * @throws ServiceException
   */
  public PubmedInsName pubMedNameMatchOther(String pubAddr, Long excInsId) throws ServiceException;

  /**
   * 获取单位SPS别名数据.
   * 
   * @return
   * @throws ServiceException
   */
  public List<PubmedInsName> getPubmedInsName(Long insId) throws ServiceException;

  /**
   * 保存SPS机构别名,BPO调用.
   * 
   * @param insId
   * @param insName
   * @throws ServiceException
   */
  public PubmedInsName savePubmedInsName(Long insId, String insName) throws ServiceException;

  /**
   * 接收同步SPS机构别名，BPO->ROL.
   * 
   * @param insName
   * @throws ServiceException
   */
  public void syncPubmedInsName(PubmedInsName insName) throws ServiceException;

  /**
   * 服务器启动的时候，初始化机构别名.
   * 
   * @throws ServiceException
   */
  public void initPubmedInsName() throws ServiceException;
}
