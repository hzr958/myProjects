package com.smate.center.open.service.snscode;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.snscode.IrisSnsCodeDao;
import com.smate.center.open.model.snscode.IrisSnsCode;

/**
 * IRIS业务系统关联验证码接口实现.
 * 
 * @author pwl
 * 
 */
@Service("irisSnsCodeService")
@Transactional(rollbackFor = Exception.class)
public class IrisSnsCodeServiceImpl implements IrisSnsCodeService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private IrisSnsCodeDao irisSnsCodeDao;

  @Override
  public void saveIrisSnsCode(String guid, Long psnId, String code) throws Exception {

    try {
      IrisSnsCode irisSnsCode = new IrisSnsCode(psnId, guid);
      irisSnsCode.setCode(code);
      irisSnsCode.setCreatedDate(new Date());
      irisSnsCodeDao.save(irisSnsCode);
    } catch (Exception e) {
      logger.error("保存关联验证码出现异常：", e);
      throw new Exception(e);
    }
  }


}
