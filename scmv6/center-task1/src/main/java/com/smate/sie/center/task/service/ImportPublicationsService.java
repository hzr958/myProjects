package com.smate.sie.center.task.service;

import java.util.List;
import java.util.Map;

import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.core.base.utils.exception.SysServiceException;

public interface ImportPublicationsService {

  /**
   * 获取基准库成果list
   * 
   * @param size
   * @param importPdwhPub
   * @return
   * @throws SysServiceException
   */
  public List<PubPdwhPO> getPwdhPubList(Integer size, Long insId) throws SysServiceException;

  /**
   * 保存该次导入中最后一条记录的更新时间
   */
  public void saveImportPdwhPub(List<PubPdwhPO> pdwhPubList, Long insId, Integer status) throws SysServiceException;

  public Map<String, Object> importSiePublication(PubPdwhPO pdwhPublications, Long insId) throws SysServiceException;

  /**
   * 记录基准库成果导入的变化数
   * */
  public void stPdwhData(List<Map<String, Object>> resultList, Long insId);

}
