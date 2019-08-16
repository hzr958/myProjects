package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import com.smate.web.v8pub.dao.sns.PubFileRelationDao;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.sns.PubFileRelationPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 成果文件关联关系
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubFileRelationSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());


  @Autowired
  private PubFileRelationDao pubFileRelationDao ;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    if (pub.pubSourceFileId ==null || pub.pubSourceFileId == 0L) {
      return null;
    }
    PubFileRelationPO pubFileRelationPO = null ;
    try {
      pubFileRelationPO = pubFileRelationDao.get(pub.pubId);
      if(pubFileRelationPO == null){
        pubFileRelationPO = new PubFileRelationPO();
        pubFileRelationPO.setPubId(pub.pubId);
      }
      pubFileRelationPO.setFileId(pub.pubSourceFileId);
      pubFileRelationDao.save(pubFileRelationPO);
    } catch (Exception e) {
      logger.error("保存或更新sns成果与文件关系异常！PubPdwhSnsRelationPO={}", pubFileRelationPO, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "保存或更新sns成果与文件关系异常", e);
    }
    return null;
  }

}
