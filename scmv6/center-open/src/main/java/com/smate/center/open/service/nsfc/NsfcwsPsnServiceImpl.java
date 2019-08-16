package com.smate.center.open.service.nsfc;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.nsfc.NsfcwsPersonDao;
import com.smate.center.open.model.nsfc.NsfcwsPerson;



/**
 * @author ajb
 * 
 */
@Service("nsfcwsPsnService")
@Transactional(rollbackFor = Exception.class)
public class NsfcwsPsnServiceImpl implements NsfcwsPsnService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private NsfcwsPersonDao nsfcwsPersonDao;



  @Override
  public NsfcwsPerson getNsfcwsPersonByPsnId(Long psnId) throws Exception {
    try {
      return nsfcwsPersonDao.get(psnId);
    } catch (Exception e) {
      logger.error(String.format("通过人员psnId={0}检索Google人员出现异常：", psnId), e);
      throw new Exception(e);
    }
  }


}
