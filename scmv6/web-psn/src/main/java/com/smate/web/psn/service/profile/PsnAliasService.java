package com.smate.web.psn.service.profile;

import java.util.List;
import java.util.Map;

import com.smate.web.psn.exception.ServiceException;

/**
 * 
 * 文献检索中的人员检索服务
 * 
 * @author lrl
 */
public interface PsnAliasService {

  /**
   * 通过psnid和dbid找到对应的数据
   * 
   * @param psnId
   * @param psnName
   * @param dbCode
   * @return
   * @throws ServiceException
   */
  public List<Map> getAllPsnAliasByPsnDb(Long psnId, String psnName, String dbCode) throws ServiceException;

  /**
   * 获得一个用户所有文献库别名json数据
   * 
   * @param psnId
   * @param psnName
   * @param dbCodes
   * @return
   * @throws ServiceException
   */
  public String getAllPsnAliasToJson(Long psnId, String psnName, String dbCodes) throws ServiceException;

  /**
   * 更新人员别名
   * 
   * @param psnId
   * @param psnName
   * @param dbCode
   * @param psnAliasNames
   * @return
   * @throws ServiceException
   */
  public String saveOrDeletePsnAlias(Long psnId, String psnName, String dbCode, String psnAliasNames)
      throws ServiceException;

}
