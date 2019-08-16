package com.smate.web.v8pub.service.handler.assembly.snspubdelete;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.dao.pdwh.PdwhPubAddrInsRecordDao;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;

/**
 * 删除基准库成果单位和scm单位地址常量信息匹配记录
 * 
 * @author YHX
 *
 *         2019年4月18日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubPdwhAddrInsRecordDeleteImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubAddrInsRecordDao pdwhPubAddrInsRecordDao;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    try {
      pdwhPubAddrInsRecordDao.delRecordByPubId(pub.pubId);
    } catch (Exception e) {
      logger.error("根据pubId删除基准库成果单位和scm单位地址常量信息匹配记录出错！pubId={}", pub.pubId, e);
      throw new PubHandlerAssemblyException(e);
    }
    return null;
  }

}
