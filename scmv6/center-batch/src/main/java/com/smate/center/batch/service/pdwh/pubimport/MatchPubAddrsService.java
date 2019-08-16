package com.smate.center.batch.service.pdwh.pubimport;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.PubRegionExclude;
import com.smate.center.batch.model.pdwh.pubimport.PdwhInsName;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubAddr;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubAddrExc;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubMaddr;



/**
 * 成果地址匹配接口
 * 
 * @author zll
 *
 */

public interface MatchPubAddrsService {


  public int pubAddrMatchCache(Long insId, Long pubId, List<PdwhPubAddr> pubAddrs, Integer dbid)
      throws ServiceException;

  /**
   * 匹配成果地址.
   * 
   * @param insId
   * @param pubId
   * @param pubAddrs
   * @return
   * @throws ServiceException
   */
  public int matchPubAddrs(Long insId, Long pubId, List<PdwhPubAddr> pubAddrs, Integer dbid) throws ServiceException;

  /**
   * 是否匹配上国外机构名.
   * 
   * @param protoAddr
   * @return
   * @throws ServiceException
   */
  public boolean matchedExcCtyName(String protoAddr, Long insId) throws ServiceException;

  /**
   * 匹配排除的机构别名.
   * 
   * @param insId
   * @param protoAddr
   * @return
   * @throws ServiceException
   */
  public PdwhPubAddrExc matchExcAddr(Long insId, String protoAddr) throws ServiceException;


  /**
   * 获取单位别名数据.
   * 
   * @param insid
   * @param dbid
   * @return
   * @throws ServiceException
   */
  public List<PdwhInsName> getPdwhInsName(Long insId, Integer dbid) throws ServiceException;

  /**
   * 传入成果地址，进行单位别名匹配.
   * 
   * @param protoAddr 成果地址，传入前必须转成小写.
   * @return
   * @throws ServiceException
   */
  public PdwhInsName PdwhInsNameMatch(String protoAddr, Long insId, Integer dbid) throws ServiceException;

  /**
   * 传入成果地址，进行单位cnki别名匹配.
   * 
   * @param ProtoAddr 成果地址，传入前必须转成小写.
   * @return
   * @throws ServiceException
   */
  public PdwhInsName PdwhCnkiInsNameMatch(String ProtoAddr, Long insId, Integer dbid) throws ServiceException;

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
   * 获取成果地址匹配上本机构的别名列表.
   * 
   * @param insId
   * @param maddrs
   * @param dbid
   * @return
   * @throws ServiceException
   */
  public int matchInsName(Long insId, List<PdwhPubMaddr> maddrs, Integer dbid) throws ServiceException;

}
