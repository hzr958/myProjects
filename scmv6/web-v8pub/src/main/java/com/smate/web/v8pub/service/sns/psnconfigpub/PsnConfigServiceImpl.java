package com.smate.web.v8pub.service.sns.psnconfigpub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smate.core.base.psn.model.psncnf.PsnConfig;
import com.smate.web.v8pub.dao.sns.psnconfigpub.PsnConfigDAO;
import com.smate.web.v8pub.exception.ServiceException;

@Service(value = "psnConfigService")
@Transactional(rollbackFor = Exception.class)
public class PsnConfigServiceImpl implements PsnConfigService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnConfigDAO psnConfigDAO;

  @Override
  public Long getCnfIdByPsnId(Long psnId) throws ServiceException {
    try {
      PsnConfig psnConfig = psnConfigDAO.getByPsnId(psnId);
      if (psnConfig != null) {
        return psnConfig.getCnfId();
      }
      return null;
    } catch (Exception e) {
      logger.error("通过psnId获取配置的cnfId出错！psnId={}", psnId);
      throw new ServiceException(e);
    }
  }

}
